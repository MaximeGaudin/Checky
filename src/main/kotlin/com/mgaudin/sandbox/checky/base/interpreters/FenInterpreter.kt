package com.mgaudin.sandbox.checky.base.interpreters

import com.mgaudin.sandbox.checky.base.*

class FenInterpreter {
  fun read(fenString: String): List<Piece> {
    val partialFenString = fenString.substring(0, fenString.indexOf(" "))

    val pieces = ArrayList<Piece>()
    var rank = 7
    var file = 0
    for (c in partialFenString) {
      if (c.toString().toIntOrNull() != null) {
        file += c.toString().toInt()
      }

      when (c) {
        'r' -> pieces.add(Piece(Position(file, rank), PieceColors.BLACK, PieceTypes.ROOK))
        'n' -> pieces.add(Piece(Position(file, rank), PieceColors.BLACK, PieceTypes.KNIGHT))
        'b' -> pieces.add(Piece(Position(file, rank), PieceColors.BLACK, PieceTypes.BISHOP))
        'q' -> pieces.add(Piece(Position(file, rank), PieceColors.BLACK, PieceTypes.QUEEN))
        'k' -> pieces.add(Piece(Position(file, rank), PieceColors.BLACK, PieceTypes.KING))
        'p' -> pieces.add(Piece(Position(file, rank), PieceColors.BLACK, PieceTypes.PAWN))

        'R' -> pieces.add(Piece(Position(file, rank), PieceColors.WHITE, PieceTypes.ROOK))
        'N' -> pieces.add(Piece(Position(file, rank), PieceColors.WHITE, PieceTypes.KNIGHT))
        'B' -> pieces.add(Piece(Position(file, rank), PieceColors.WHITE, PieceTypes.BISHOP))
        'Q' -> pieces.add(Piece(Position(file, rank), PieceColors.WHITE, PieceTypes.QUEEN))
        'K' -> pieces.add(Piece(Position(file, rank), PieceColors.WHITE, PieceTypes.KING))
        'P' -> pieces.add(Piece(Position(file, rank), PieceColors.WHITE, PieceTypes.PAWN))
      }


      if (c == '/') {
        rank -= 1
        file = 0
      } else {
        file += 1
      }
    }

    return pieces
  }

  fun write(
    board: Board,
    colorToPlay: PieceColors,
    totalNumberOfMoves: Int
  ): String {
    val fenStringBuilder = StringBuilder()
    for (rank in 7 downTo 0) {
      for (file in 0..7) {
        fenStringBuilder.append(toFenChar(board.getPiece(Position(file, rank))))
      }

      if (rank != 0) {
        fenStringBuilder.append("/")
      }
    }
    val pieceString = fenStringBuilder.toString()
      .replace("--------", "8")
      .replace("-------", "7")
      .replace("------", "6")
      .replace("-----", "5")
      .replace("----", "4")
      .replace("---", "3")
      .replace("--", "2")
      .replace("-", "1")

    return "$pieceString ${toFenChar(colorToPlay)} KQkq - 0 ${totalNumberOfMoves / 2}"
  }

  private fun toFenChar(colorToPlay: PieceColors) =
    when (colorToPlay) {
      PieceColors.WHITE -> "w"
      PieceColors.BLACK -> "b"
    }

  private fun toFenChar(piece: Piece?): String {
    if (piece == null) {
      return "-"
    }

    val pieceCode = when (piece.type) {
      PieceTypes.PAWN -> "p"
      PieceTypes.KNIGHT -> "n"
      PieceTypes.BISHOP -> "b"
      PieceTypes.ROOK -> "r"
      PieceTypes.QUEEN -> "q"
      PieceTypes.KING -> "k"
    }

    return if (piece.color == PieceColors.BLACK) pieceCode
    else pieceCode.toUpperCase()
  }

}
