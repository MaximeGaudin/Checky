package com.mgaudin.sandbox.checky.base

interface Board {
  fun setPiece(piece: Piece)

  fun getPieces(color: PieceColors, type: PieceTypes): List<Piece>
  fun getAllPieces(): List<Piece>
  fun getAllPieces(color: PieceColors): List<Piece>
  fun getPiece(position: Position): Piece?

  fun isFree(position: Position): Boolean
  fun isFreeAndNotAttacked(position: Position, by: PieceColors, excludeKingAttacks: Boolean = false): Boolean
  fun isFreeOrWithOpponentPiece(piece: Piece, position: Position): Boolean
  fun isOpponentPiece(piece: Piece, position: Position): Boolean

  fun copyWithMove(from: Position, to: Position): Board
  fun isKingInCheck(kingColor: PieceColors): Boolean
  fun canCastle(kingColor: PieceColors): Boolean
}
