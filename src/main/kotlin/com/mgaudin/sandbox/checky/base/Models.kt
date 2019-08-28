package com.mgaudin.sandbox.checky.base

data class Move(
  val piece: Piece,
  val from: Position,
  val to: Position,
  val captured: Piece? = null
) {
  fun toAlgebraicNotation(): String {
    return from.toLongAlgebraicNotation() + to.toLongAlgebraicNotation()
  }
}

data class Piece(
  val position: Position,
  val color: PieceColors,
  val type: PieceTypes
) {
  companion object {
    val STARTING_PIECES = listOf(
      Piece(Position(0, 0), PieceColors.WHITE, PieceTypes.ROOK),
      Piece(Position(7, 0), PieceColors.WHITE, PieceTypes.ROOK),
      Piece(Position(1, 0), PieceColors.WHITE, PieceTypes.KNIGHT),
      Piece(Position(6, 0), PieceColors.WHITE, PieceTypes.KNIGHT),
      Piece(Position(2, 0), PieceColors.WHITE, PieceTypes.BISHOP),
      Piece(Position(5, 0), PieceColors.WHITE, PieceTypes.BISHOP),
      Piece(Position(3, 0), PieceColors.WHITE, PieceTypes.QUEEN),
      Piece(Position(4, 0), PieceColors.WHITE, PieceTypes.KING),
      Piece(Position(0, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(1, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(2, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(3, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(4, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(5, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(6, 1), PieceColors.WHITE, PieceTypes.PAWN),
      Piece(Position(7, 1), PieceColors.WHITE, PieceTypes.PAWN),

      Piece(Position(0, 7), PieceColors.BLACK, PieceTypes.ROOK),
      Piece(Position(7, 7), PieceColors.BLACK, PieceTypes.ROOK),
      Piece(Position(1, 7), PieceColors.BLACK, PieceTypes.KNIGHT),
      Piece(Position(6, 7), PieceColors.BLACK, PieceTypes.KNIGHT),
      Piece(Position(2, 7), PieceColors.BLACK, PieceTypes.BISHOP),
      Piece(Position(5, 7), PieceColors.BLACK, PieceTypes.BISHOP),
      Piece(Position(3, 7), PieceColors.BLACK, PieceTypes.QUEEN),
      Piece(Position(4, 7), PieceColors.BLACK, PieceTypes.KING),
      Piece(Position(0, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(1, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(2, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(3, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(4, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(5, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(6, 6), PieceColors.BLACK, PieceTypes.PAWN),
      Piece(Position(7, 6), PieceColors.BLACK, PieceTypes.PAWN)
    )
  }
}

data class Position(
  val file: Int,
  val rank: Int
) {
  fun toLongAlgebraicNotation(): String {
    val fileCode = when (file) {
      0 -> "a"
      1 -> "b"
      2 -> "c"
      3 -> "d"
      4 -> "e"
      5 -> "f"
      6 -> "g"
      7 -> "h"
      else -> throw IllegalArgumentException("File cannot be greater than 7")
    }

    if (rank > 7) {
      throw IllegalArgumentException("Rank cannot be greater than 7")
    }

    return "$fileCode${rank + 1}"
  }

  fun up(): Position {
    return Position(file, rank + 1)
  }

  fun doubleUp(): Position {
    return Position(file, rank + 2)
  }

  fun upRight(): Position {
    return Position(file + 1, rank + 1)
  }

  fun upLeft(): Position {
    return Position(file - 1, rank + 1)
  }


  fun down(): Position {
    return Position(file, rank - 1)
  }

  fun doubleDown(): Position {
    return Position(file, rank - 2)
  }

  fun downRight(): Position {
    return Position(file + 1, rank - 1)
  }

  fun downLeft(): Position {
    return Position(file - 1, rank - 1)
  }

  fun add(fileIncrement: Int, rankIncrement: Int): Position {
    return Position(file + fileIncrement, rank + rankIncrement)
  }

  fun add(direction: Directions): Position {
    return add(direction.fileIncrement, direction.rankIncrement)
  }
}

enum class PieceTypes {
  PAWN,
  KNIGHT,
  BISHOP,
  ROOK,
  QUEEN,
  KING;
}

enum class PieceColors {
  WHITE,
  BLACK;

  fun opposite() =
    when (this) {
      WHITE -> BLACK
      BLACK -> WHITE
    }
}

enum class Directions(val fileIncrement: Int, val rankIncrement: Int) {
  UP(0, 1),
  UP_RIGHT(1, 1),
  RIGHT(1, 0),
  DOWN_RIGHT(1, -1),
  DOWN(0, -1),
  DOWN_LEFT(-1, -1),
  LEFT(-1, 0),
  UP_LEFT(-1, 1);
}
