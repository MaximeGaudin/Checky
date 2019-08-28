package com.mgaudin.sandbox.checky.base.boards

import com.mgaudin.sandbox.checky.base.*
import com.mgaudin.sandbox.checky.base.boards.BitBoardPieceColors.Companion.PIECE_COLOR_MASK
import com.mgaudin.sandbox.checky.base.boards.BitBoardPieceTypes.Companion.PIECE_TYPE_MASK
import kotlin.math.abs

typealias Representation = Array<Code>
typealias Code = Int
typealias BitBoardIndex = Int

// 0x88 Board
class x88Board(
  private val bitBoard: Representation = Array(16 * 8) { 0 },
  private var whiteCanCastle: Boolean = true,
  private var blackCanCastle: Boolean = true

) : Board {
  override fun canCastle(kingColor: PieceColors) =
    when (kingColor) {
      PieceColors.WHITE -> whiteCanCastle
      PieceColors.BLACK -> blackCanCastle
    }

  private val moveGenerator = MoveGenerator()

  override fun isKingInCheck(kingColor: PieceColors): Boolean {
    val kingPiece = getPieces(kingColor, PieceTypes.KING).getOrNull(0)
      ?: throw IllegalStateException("Each color must have a king !")

    val eligiblePieces = getAllPieces(kingColor.opposite()).filter { canAttack(it, kingPiece.position) }
    return moveGenerator.generatePseudoLegalMoves(this, eligiblePieces)
      .any { it.to == kingPiece.position }
  }

  private fun canAttack(piece: Piece, to: Position): Boolean {
    return when (piece.type) {
      PieceTypes.PAWN -> piece.position.rank == to.rank - 1 && (piece.position.file == to.file - 1 || piece.position.file == to.file + 1)
      PieceTypes.KNIGHT -> piece.position.rank - to.rank <= 2 && piece.position.file - to.file <= 2
      PieceTypes.ROOK -> piece.position.rank == to.rank || piece.position.file == to.file
      PieceTypes.KING -> abs(piece.position.rank - to.rank) <= 1 && abs(piece.position.file - to.file) <= 1
      else -> true
    }
  }

  override fun getPiece(position: Position): Piece? {
    val bitBoardIndex = toBitBoardIndex(position)
    if (!isInsideTheBoard(bitBoardIndex)) {
      return null
    }
    return decodePiece(position, bitBoard[bitBoardIndex])
  }

  override fun setPiece(piece: Piece) {
    bitBoard[toBitBoardIndex(piece.position)] = encodePiece(piece.color, piece.type)
  }

  override fun getPieces(color: PieceColors, type: PieceTypes): List<Piece> {
    val expectedColorCode = BitBoardPieceColors.from(color).code
    val expectedTypeCode = BitBoardPieceTypes.from(type).code

    return bitBoard
      .mapIndexed { index, code ->
        if (code == 0 || code and PIECE_COLOR_MASK != expectedColorCode || code and PIECE_TYPE_MASK != expectedTypeCode) null
        else decodePiece(toPosition(index), code)!!
      }
      .filterNotNull()
  }


  override fun getAllPieces(color: PieceColors): List<Piece> {
    val colorCode = BitBoardPieceColors.from(color).code
    return bitBoard
      .mapIndexed { index, code ->
        if (code == 0) null
        else {
          if (bitBoard[index] and PIECE_COLOR_MASK == colorCode) decodePiece(toPosition(index), code)
          else null
        }

      }
      .filterNotNull()
  }

  override fun getAllPieces(): List<Piece> {
    return bitBoard
      .mapIndexed { index, code ->
        if (code == 0) null
        else decodePiece(toPosition(index), code)
      }
      .filterNotNull()
  }

  override fun copyWithMove(from: Position, to: Position): x88Board {
    val piece = getPiece(from) ?: throw IllegalArgumentException("No piece at position $from")
    val bitBoardClone = bitBoard.copyOf()
    val originIndex = toBitBoardIndex(piece.position)

    // Register king movements for castling rights computation
    if (piece.color == PieceColors.WHITE && (piece.type == PieceTypes.KING || piece.type == PieceTypes.ROOK)) whiteCanCastle = false
    else if (piece.color == PieceColors.BLACK && (piece.type == PieceTypes.KING || piece.type == PieceTypes.ROOK)) blackCanCastle = false

    // Castling check
    if (piece.type == PieceTypes.KING) {
      val whiteInitialRankMove = piece.type == PieceTypes.KING
        && piece.color == PieceColors.WHITE
        && from.rank == 0 && to.rank == 0

      val blackInitialRankMove = piece.type == PieceTypes.KING
        && piece.color == PieceColors.BLACK
        && from.rank == 7 && to.rank == 7

      val whiteKingSideCastling = whiteInitialRankMove && from.file == 4 && to.file == 6
      val blackKingSideCastling = blackInitialRankMove && from.file == 4 && to.file == 6

      if (whiteKingSideCastling || blackKingSideCastling) {
        bitBoardClone[originIndex] = 0
        bitBoardClone[originIndex + 1] = encodePiece(piece.color, PieceTypes.ROOK)
        bitBoardClone[originIndex + 2] = encodePiece(piece.color, PieceTypes.KING)
        bitBoardClone[originIndex + 3] = 0
        return x88Board(bitBoardClone, whiteCanCastle, blackCanCastle)
      }

      val whiteQueenSideCastling = whiteInitialRankMove && from.file == 4 && to.file == 2
      val blackQueenSideCastling = blackInitialRankMove && from.file == 4 && to.file == 2
      if (whiteQueenSideCastling || blackQueenSideCastling) {
        bitBoardClone[originIndex] = 0
        bitBoardClone[originIndex - 1] = encodePiece(piece.color, PieceTypes.ROOK)
        bitBoardClone[originIndex - 2] = encodePiece(piece.color, PieceTypes.KING)
        bitBoardClone[originIndex - 4] = 0
        return x88Board(bitBoardClone, whiteCanCastle, blackCanCastle)
      }
    }

    val pawnPromotion = (piece.type == PieceTypes.PAWN) &&
      ((piece.color == PieceColors.WHITE && to.rank == 7) || (piece.color == PieceColors.BLACK && to.rank == 0))

    val destinationIndex = toBitBoardIndex(to)
    bitBoardClone[originIndex] = 0
    bitBoardClone[destinationIndex] = encodePiece(piece.color, if (pawnPromotion) PieceTypes.QUEEN else piece.type)

    return x88Board(bitBoardClone, whiteCanCastle, blackCanCastle)
  }

  override fun isFree(position: Position): Boolean {
    val index = toBitBoardIndex(position)
    return isInsideTheBoard(index) && bitBoard[index] == 0
  }

  override fun isFreeAndNotAttacked(position: Position, by: PieceColors, excludeKingAttacks: Boolean): Boolean {
    val index = toBitBoardIndex(position)
    return isInsideTheBoard(index)
      && bitBoard[index] == 0
      && moveGenerator.generatePseudoLegalMoves(this, by, excludeKingAttacks).none { it.to == position }
  }

  override fun isOpponentPiece(piece: Piece, position: Position): Boolean {
    val destinationIndex = toBitBoardIndex(position)
    return isInsideTheBoard(destinationIndex)
      && !isFree(position)
      && BitBoardPieceColors.from(bitBoard[destinationIndex]) == piece.color.opposite()
  }

  override fun isFreeOrWithOpponentPiece(piece: Piece, position: Position): Boolean {
    val destinationIndex = toBitBoardIndex(position)
    return isInsideTheBoard(destinationIndex)
      && (bitBoard[destinationIndex] == 0
      || BitBoardPieceColors.from(bitBoard[destinationIndex]) == piece.color.opposite())
  }

  private fun isInsideTheBoard(index: BitBoardIndex): Boolean {
    return index >= 0 && index < 16 * 8 && index and 0x88 == 0
  }

  private fun toPosition(index: BitBoardIndex): Position {
    return Position(index and 7, index shr 4)
  }

  private fun toBitBoardIndex(position: Position): Int {
    return position.rank * 16 + position.file
  }

  private fun decodePiece(position: Position, code: Code): Piece? {
    if (code == 0) {
      return null
    }
    return Piece(position, BitBoardPieceColors.from(code), BitBoardPieceTypes.from(code))
  }

  private fun encodePiece(color: PieceColors, type: PieceTypes): Code {
    return BitBoardPieceColors.from(color).code or BitBoardPieceTypes.from(type).code
  }
}


enum class BitBoardPieceTypes(val code: Int) {
  PAWN(0b0000_0100),
  KNIGHT(0b0000_1000),
  BISHOP(0b0001_0000),
  ROOK(0b0010_0000),
  QUEEN(0b0100_0000),
  KING(0b1000_0000);

  companion object {
    fun from(code: Code) =
      when (code and PIECE_TYPE_MASK) {
        BitBoardPieceTypes.PAWN.code -> PieceTypes.PAWN
        BitBoardPieceTypes.KNIGHT.code -> PieceTypes.KNIGHT
        BitBoardPieceTypes.BISHOP.code -> PieceTypes.BISHOP
        BitBoardPieceTypes.ROOK.code -> PieceTypes.ROOK
        BitBoardPieceTypes.QUEEN.code -> PieceTypes.QUEEN
        BitBoardPieceTypes.KING.code -> PieceTypes.KING
        else -> throw IllegalArgumentException("Invalid code $code")
      }

    fun from(type: PieceTypes) =
      when (type) {
        PieceTypes.PAWN -> BitBoardPieceTypes.PAWN
        PieceTypes.KNIGHT -> BitBoardPieceTypes.KNIGHT
        PieceTypes.BISHOP -> BitBoardPieceTypes.BISHOP
        PieceTypes.ROOK -> BitBoardPieceTypes.ROOK
        PieceTypes.QUEEN -> BitBoardPieceTypes.QUEEN
        PieceTypes.KING -> BitBoardPieceTypes.KING
      }

    const val PIECE_TYPE_MASK = 0b1111_1100
  }
}

enum class BitBoardPieceColors(val code: Code) {
  WHITE(0b0000_0001),
  BLACK(0b0000_0010);

  companion object {
    const val PIECE_COLOR_MASK = 0b0000_0011

    fun from(code: Code) =
      when (code and 0b0000_0011) {
        BitBoardPieceColors.WHITE.code -> PieceColors.WHITE
        BitBoardPieceColors.BLACK.code -> PieceColors.BLACK
        else -> throw IllegalArgumentException("Invalid code $code")
      }

    fun from(color: PieceColors) =
      when (color) {
        PieceColors.WHITE -> BitBoardPieceColors.WHITE
        PieceColors.BLACK -> BitBoardPieceColors.BLACK
      }

  }
}
