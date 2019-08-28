package com.mgaudin.sandbox.checky

import com.mgaudin.sandbox.checky.base.*
import com.mgaudin.sandbox.checky.base.boards.x88Board
import com.mgaudin.sandbox.checky.base.interpreters.FenInterpreter
import com.mgaudin.sandbox.checky.base.interpreters.OpeningsDatabase
import kotlin.js.Date
import kotlin.math.max
import kotlin.math.min

class CheckyEngine : Engine {
  private val moveGenerator = MoveGenerator()
  private val evaluator = NodeScoreEvaluator()
  private val openingsDatabase = OpeningsDatabase()
  private val fenInterpreter = FenInterpreter()

  private var eventsHandler: EngineEventsHandler? = null

  private var rootNode: BoardNode? = null

  private var moveHistory = ArrayList<Move>()

  private var currentExplorationDepth = 0
  private var totalExploredNodes = 0L
  private var exploredNodesSinceLastStatisticOutput = 0L
  private var lastStatisticOutput = 0.0

  override fun setEventsHandler(eventsHandler: EngineEventsHandler) {
    this.eventsHandler = eventsHandler
  }

  override fun toFen(): String {
    return rootNode?.let {
      fenInterpreter.write(it.board, it.colorToPlay, moveHistory.size)
    } ?: ""
  }

  override fun setupBoard() {
    setupBoard(Piece.STARTING_PIECES)
    moveHistory.clear()
    totalExploredNodes = 0
  }

  override fun setupBoard(pieces: List<Piece>) {
    val startingBoard = x88Board()
    pieces.forEach { startingBoard.setPiece(it) }
    rootNode = BoardNode(startingBoard)
  }

  override fun isMoveValid(from: Position, to: Position): Boolean {
    return rootNode?.let {
      generateAndStreamNextMoves(it).any { it.move?.from == from && it.move.to == to }
    } ?: false
  }

  override fun applyMove(from: Position, to: Position) {
    rootNode?.let {
      moveHistory.add(Move(it.board.getPiece(from)!!, from, to, it.board.getPiece(to)))
      rootNode = BoardNode(it.board.copyWithMove(from, to), it.colorToPlay.opposite())
    }
  }

  override fun getName() = "Checky"

  override fun getAuthor() = "Maxime Gaudin"

  override fun setupEngine() {

  }

  override fun setupNewGame() {
    setupBoard()
  }

  override fun startComputeBestMove() {
    currentExplorationDepth = 0
    exploredNodesSinceLastStatisticOutput = 0
    lastStatisticOutput = Date.now()
  }

  override fun stopComputeBestMove() {
  }

  override fun getCurrentBestMove(): Move? {
    getBestMoveFromOpeningIfPossible()?.let { return it }
    return getBestMoveFromIterativeDeepening()
  }

  private fun getBestMoveFromIterativeDeepening(maxDepth: Int = DEFAULT_MAX_DEPTH): Move? {
    return rootNode?.let {
      var bestMoveFoundSoFar: Move? = null

      for (i in 1..maxDepth) {
        bestMoveFoundSoFar = getBestMoveFromSearch(it.copy(), i)
      }

      bestMoveFoundSoFar
    }
  }

  private fun getBestMoveFromSearch(root: BoardNode, maxDepth: Int = DEFAULT_MAX_DEPTH): Move? {
    minMaxWithAlphaBetaPruning(root, root.colorToPlay, 0, maxDepth)

    val bestMove = root.getChildren().maxBy { it.getLineScore() }
    bestMove?.let {
      eventsHandler?.handle(
        BestLineFoundSoFar(
          bestMove.getLineScore(),
          currentExplorationDepth,
          totalExploredNodes,
          extractBestLine(root)
        )
      )
    }

    return bestMove?.move
  }

  private fun getBestMoveFromOpeningIfPossible(): Move? {
    val availableOpenings = openingsDatabase.getOpeningsFrom(moveHistory)
      .filter { it.moves.size >= moveHistory.size + 1 }
    if (availableOpenings.isEmpty()) {
      return null
    }

    val opening = availableOpenings.random()
    eventsHandler?.handle(BestLineFoundSoFar(0.0, opening.moves.size, 0, opening.moves))
    return opening.moves[moveHistory.size]
  }

  private fun extractBestLine(node: BoardNode): List<Move> {
    val moves = ArrayList<Move>()
    var isMaximizingPlayer = true

    var currentNode: BoardNode? = node
    while (currentNode != null) {
      currentNode =
        if (isMaximizingPlayer) currentNode.getChildren().sortedByDescending { it.getLineScore() }.firstOrNull()
        else currentNode.getChildren().sortedBy { it.getLineScore() }.firstOrNull()
      currentNode?.let { it.move?.let { moves.add(it) } }
      isMaximizingPlayer = !isMaximizingPlayer
    }

    return moves
  }

  private fun minMaxWithAlphaBetaPruning(
    node: BoardNode,
    maximizingPlayer: PieceColors,
    depth: Int = 0, maxDepth: Int = DEFAULT_MAX_DEPTH,
    alpha: Double = Double.NEGATIVE_INFINITY, beta: Double = Double.POSITIVE_INFINITY
  ): Double {
    if (currentExplorationDepth < depth) {
      currentExplorationDepth = depth
      eventsHandler?.handle(CurrentlyExploringDepthEvent(depth))
    }

    if (depth == maxDepth) {
      val boardScore = evaluator.getScore(node, maximizingPlayer)
      node.setLineScore(boardScore)
      node.setBoardScore(boardScore)
      return node.getBoardScore()
    }

    val isMaximizingPlayer = node.colorToPlay == maximizingPlayer
    if (isMaximizingPlayer) {
      var v = Double.NEGATIVE_INFINITY
      var a = alpha
      for (child in generateAndStreamNextMoves(node)) {
        v = max(v, minMaxWithAlphaBetaPruning(child, maximizingPlayer, depth + 1, maxDepth, a, beta))
        a = max(a, v)
        if (a >= beta) {
          break
        }
      }

//            val kingInCheck = node.board.isKingInCheck(node.colorToPlay)
      // Check mate !
//            if (!node.hasChildren() && kingInCheck) v = -500.0
      // Stale mate !
//            if (!node.hasChildren() && !kingInCheck) v = 50.0

      node.setLineScore(v)
      return v
    } else {
      var v = Double.POSITIVE_INFINITY
      var b = beta
      for (child in generateAndStreamNextMoves(node)) {
        v = min(v, minMaxWithAlphaBetaPruning(child, maximizingPlayer, depth + 1, maxDepth, alpha, b))
        b = min(b, v)
        if (alpha >= b) {
          break
        }
      }

//            val kingInCheck = node.board.isKingInCheck(node.colorToPlay)
      // Check mate !
//            if (!node.hasChildren() && kingInCheck) v = 500.0
      // Stale mate !
//            if (!node.hasChildren() && !kingInCheck) v = -500.0

      node.setLineScore(v)
      return v
    }
  }

  private fun generateAndStreamNextMoves(node: BoardNode): Sequence<BoardNode> {
    if (Date.now() > lastStatisticOutput + 200) {
      lastStatisticOutput = Date.now()
      eventsHandler?.handle(NodePerSecondsStatisticsEvent(exploredNodesSinceLastStatisticOutput * 5))
      exploredNodesSinceLastStatisticOutput = 0
    }

    return moveGenerator.generatePseudoLegalMoves(node.board, node.colorToPlay)
      .sortedByDescending { pieceValue(it.captured) }
      .filter { it.captured?.type != PieceTypes.KING }
      .map {
        val board = node.board.copyWithMove(it.from, it.to)
        exploredNodesSinceLastStatisticOutput += 1
        totalExploredNodes += 1
        Pair(it, board)
      }
      .filter { !it.second.isKingInCheck(node.colorToPlay) }
      .map {
        val childNode = BoardNode(it.second, node.colorToPlay.opposite(), it.first, node)
        node.addChild(childNode)
        childNode
      }
  }

  private fun pieceValue(captured: Piece?): Double {
    return when (captured?.type) {
      PieceTypes.PAWN -> 1.0
      PieceTypes.KNIGHT -> 3.0
      PieceTypes.BISHOP -> 3.2
      PieceTypes.ROOK -> 5.0
      PieceTypes.QUEEN -> 9.0
      PieceTypes.KING -> 200.0
      null -> 0.0
    }
  }

  companion object {
    const val DEFAULT_MAX_DEPTH = 5
  }
}

class NodeScoreEvaluator {
  private val moveGenerator = MoveGenerator()

  fun getScore(node: BoardNode, maximizingPlayer: PieceColors): Double {
    val pieceMap = HashMap<Pair<PieceColors, PieceTypes>, Int>()
    val board = node.board
    val pieces = board.getAllPieces()

    var minorPieceDevelopmentScore = 0.0
    var centerPositionalScore = 0.0
    for (piece in pieces) {
      val key = Pair(piece.color, piece.type)
      pieceMap[key] = pieceMap.getOrElse(key, { 0 }) + 1

      minorPieceDevelopmentScore +=
        if (!isAtInitialPosition(piece)) colorValue(piece.color, maximizingPlayer) * 0.3
        else colorValue(piece.color, maximizingPlayer) * -0.1

      centerPositionalScore +=
        if ((piece.position.file in 3..4) && (piece.position.rank in 3..4)) colorValue(piece.color, maximizingPlayer) * 0.2
        else if ((piece.position.file in 2..5) && (piece.position.rank in 2..5)) colorValue(piece.color, maximizingPlayer) * 0.1
        else 0.0
    }

    val materialScore = materialComparisonScore(maximizingPlayer, PieceTypes.KING, 200.0, pieceMap) +
      materialComparisonScore(maximizingPlayer, PieceTypes.QUEEN, 9.0, pieceMap) +
      materialComparisonScore(maximizingPlayer, PieceTypes.ROOK, 5.0, pieceMap) +
      materialComparisonScore(maximizingPlayer, PieceTypes.BISHOP, 3.2, pieceMap) +
      materialComparisonScore(maximizingPlayer, PieceTypes.KNIGHT, 3.0, pieceMap) +
      materialComparisonScore(maximizingPlayer, PieceTypes.PAWN, 1.0, pieceMap)

    return materialScore + centerPositionalScore + minorPieceDevelopmentScore
  }

  private fun isAtInitialPosition(piece: Piece): Boolean {
    if (piece.type == PieceTypes.KNIGHT) {
      val initialRank = if (piece.color == PieceColors.WHITE) 0 else 7
      return piece.position.rank == initialRank && (piece.position.file == 1 || piece.position.file == 6)
    } else if (piece.type == PieceTypes.BISHOP) {
      val initialRank = if (piece.color == PieceColors.WHITE) 0 else 7
      return piece.position.rank == initialRank && (piece.position.file == 2 || piece.position.file == 5)
    }

    return false
  }

  private fun colorValue(color: PieceColors, maximizingPlayer: PieceColors) =
    if (color == maximizingPlayer) 1.0
    else -1.0

  private fun materialComparisonScore(
    maximizingPlayer: PieceColors,
    type: PieceTypes,
    factor: Double,
    pieceMap: HashMap<Pair<PieceColors, PieceTypes>, Int>
  ): Double {
    return factor * (
      pieceMap.getOrElse(Pair(maximizingPlayer, type), { 0 })
        - pieceMap.getOrElse(Pair(maximizingPlayer.opposite(), type), { 0 })
      )
  }
}

data class BoardNode(
  val board: Board,
  val colorToPlay: PieceColors = PieceColors.WHITE,
  val move: Move? = null,
  val parent: BoardNode? = null
) {
  private val children: ArrayList<BoardNode> = ArrayList()
  private var lineScore: Double = Double.NEGATIVE_INFINITY
  private var boardScore: Double = Double.NEGATIVE_INFINITY

  fun setBoardScore(score: Double) {
    this.boardScore = score
  }

  fun getBoardScore() = boardScore

  fun setLineScore(score: Double) {
    this.lineScore = score
  }

  fun getLineScore() = lineScore

  fun addChild(child: BoardNode) {
    children.add(child)
  }

  fun getChildren() = children

  override fun toString(): String {
    return "${move?.toAlgebraicNotation()} (Line = $lineScore, Board = $boardScore, Children=${children.size})"
  }

  fun hasChildren() = children.isNotEmpty()
}
