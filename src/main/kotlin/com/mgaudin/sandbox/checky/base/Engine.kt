package com.mgaudin.sandbox.checky.base

interface Engine {
  fun getName(): String
  fun getAuthor(): String

  fun setupEngine()
  fun setupNewGame()

  fun setupBoard()
  fun setupBoard(pieces: List<Piece>)

  fun isMoveValid(from: Position, to: Position): Boolean
  fun applyMove(from: Position, to: Position)

  fun startComputeBestMove()
  fun stopComputeBestMove()
  fun getCurrentBestMove(): Move?

  fun setEventsHandler(eventsHandler: EngineEventsHandler)

  fun toFen(): String
}
