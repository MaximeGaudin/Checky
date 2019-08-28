package com.mgaudin.sandbox.checky.base

import com.mgaudin.sandbox.checky.base.*

// TODO: En passant
class MoveGenerator {
  fun generatePseudoLegalMoves(board: Board, colorToPlay: PieceColors, excludeKingMoves: Boolean = false): Sequence<Move> {
    return board.getAllPieces(colorToPlay).asSequence()
      .filter { !excludeKingMoves || it.type != PieceTypes.KING }
      .flatMap { generatePseudoLegalMoves(board, it) }
  }

  fun generatePseudoLegalMoves(board: Board, pieces: List<Piece>): Sequence<Move> {
    return pieces.asSequence()
      .flatMap { generatePseudoLegalMoves(board, it) }
  }

  private fun generatePseudoLegalMoves(board: Board, piece: Piece) =
    when (piece.type) {
      PieceTypes.PAWN -> generatePawnPseudoLegalMoves(board, piece)
      PieceTypes.KNIGHT -> generateKnightPseudoLegalMoves(board, piece)
      PieceTypes.BISHOP -> generateBishopPseudoLegalMoves(board, piece)
      PieceTypes.ROOK -> generateRookPseudoLegalMoves(board, piece)
      PieceTypes.QUEEN -> generateQueenPseudoLegalMoves(board, piece)
      PieceTypes.KING -> generateKingPseudoLegalMoves(board, piece)
    }

  private fun generatePawnPseudoLegalMoves(board: Board, piece: Piece) =
    when (piece.color) {
      PieceColors.WHITE -> generateWhitePawnPseudoLegalMoves(board, piece)
      PieceColors.BLACK -> generateBlackPawnPseudoLegalMoves(board, piece)
    }

  private fun generateKnightPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForNonSlidingPieces(
      board, piece,
      PotentialMoves(
        board::isFreeOrWithOpponentPiece,
        piece.position.add(1, 2),
        piece.position.add(2, 1),
        piece.position.add(2, -1),
        piece.position.add(1, -2),
        piece.position.add(-1, -2),
        piece.position.add(-2, -1),
        piece.position.add(-2, 1),
        piece.position.add(-1, 2)
      )
    )
  }

  private fun generateKingPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForNonSlidingPieces(
      board, piece,
      PotentialMoves(
        board::isFreeOrWithOpponentPiece,
        piece.position.add(0, 1),
        piece.position.add(1, 1),
        piece.position.add(1, 0),
        piece.position.add(1, -1),
        piece.position.add(-1, 0),
        piece.position.add(-1, -1),
        piece.position.add(-1, 0),
        piece.position.add(-1, 1)
      ),

      // King side castle
      PotentialMoves({ _, _ ->
        !board.canCastle(piece.color)
          && board.getPiece(piece.position.add(-3, 0))?.type == PieceTypes.ROOK
          && board.isFreeAndNotAttacked(piece.position.add(1, 0), piece.color.opposite(), true)
          && board.isFreeAndNotAttacked(piece.position.add(2, 0), piece.color.opposite(), true)
      }, piece.position.add(2, 0)),

      // Queen side castle
      PotentialMoves({ _, _ ->
        !board.canCastle(piece.color)
          && board.getPiece(piece.position.add(-4, 0))?.type == PieceTypes.ROOK
          && board.isFreeAndNotAttacked(piece.position.add(-1, 0), piece.color.opposite(), true)
          && board.isFreeAndNotAttacked(piece.position.add(-2, 0), piece.color.opposite(), true)
          && board.isFreeAndNotAttacked(piece.position.add(-3, 0), piece.color.opposite(), true)
      }, piece.position.add(-2, 0))
    )
  }

  private fun generateBishopPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForSlidingPieces(
      board, piece,
      arrayOf(Directions.UP_RIGHT, Directions.DOWN_RIGHT, Directions.DOWN_LEFT, Directions.UP_LEFT)
    )
  }

  private fun generateRookPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForSlidingPieces(
      board, piece,
      arrayOf(Directions.UP, Directions.RIGHT, Directions.DOWN, Directions.LEFT)
    )
  }

  private fun generateQueenPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForSlidingPieces(board, piece, Directions.values())
  }

  private fun generateWhitePawnPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForNonSlidingPieces(
      board, piece,
      PotentialMoves({ _, newPosition -> board.isFree(newPosition) }, piece.position.up()),
      PotentialMoves({ _, newPosition -> piece.position.rank == 1 && board.isFree(newPosition.down()) && board.isFree(newPosition) }, piece.position.doubleUp()),
      PotentialMoves(board::isOpponentPiece, piece.position.upRight(), piece.position.upLeft())
    )
  }

  private fun generateBlackPawnPseudoLegalMoves(board: Board, piece: Piece): Sequence<Move> {
    return generateMovesForNonSlidingPieces(
      board, piece,
      PotentialMoves({ _, newPosition -> board.isFree(newPosition) }, piece.position.down()),
      PotentialMoves({ _, newPosition -> piece.position.rank == 6 && board.isFree(newPosition.up()) && board.isFree(newPosition) }, piece.position.doubleDown()),
      PotentialMoves(board::isOpponentPiece, piece.position.downRight(), piece.position.downLeft())
    )
  }

  private fun generateMovesForNonSlidingPieces(board: Board, piece: Piece, vararg moves: PotentialMoves): Sequence<Move> {
    return moves.asSequence()
      .flatMap { move -> move.positions.asSequence().filter { position -> move.evaluationFunction(piece, position) } }
      .map { Move(piece, piece.position, it, board.getPiece(it)) }
  }

  private fun generateMovesForSlidingPieces(
    board: Board,
    piece: Piece, directions: Array<Directions>
  ): Sequence<Move> {
    val initialPosition = piece.position
    val pseudoLegalMoves = ArrayList<Move>()

    for (direction in directions) {
      var stopDirectionExploration = false
      var previousPosition = initialPosition
      while (!stopDirectionExploration) {
        val newPosition = previousPosition.add(direction)

        if (board.isOpponentPiece(piece, newPosition)) {
          pseudoLegalMoves.add(Move(piece, initialPosition, newPosition, board.getPiece(newPosition)))
          stopDirectionExploration = true
        } else if (board.isFree(newPosition)) {
          pseudoLegalMoves.add(Move(piece, initialPosition, newPosition, board.getPiece(newPosition)))
          previousPosition = newPosition
        } else {
          // Same player piece
          stopDirectionExploration = true
        }
      }
    }

    return pseudoLegalMoves.asSequence()
  }
}

class PotentialMoves(
  val evaluationFunction: (originalPiece: Piece, newPosition: Position) -> Boolean,
  vararg val positions: Position
)
