package com.mgaudin.sandbox.checky.base.interpreters

import com.mgaudin.sandbox.checky.base.*
import kotlin.math.roundToInt

class UciInterpreter(val engine: Engine) {
  private val fenInterpreter = FenInterpreter()
  private val lanInterpreter = LongAlgebraicNotationInterpreter()

  fun shouldExit(): Boolean {
    return false
  }

  fun handle(input: String?): String? {
    if (input == null) {
      return null
    }

    return when {
      input == "uci" -> identifyEngine()
      input == "isready" -> initializeEngine()
      input.startsWith("position") -> setEngineBoard(input)
      input.startsWith("go") -> startComputeNextMove()
      input == "stop" -> stopComputationAndPrintBestMove()
      else -> null
    }
  }

  private fun stopComputationAndPrintBestMove(): String? {
    engine.stopComputeBestMove()
    val bestMove = engine.getCurrentBestMove()

    return "bestmove ${bestMove?.toAlgebraicNotation()}"
  }

  private fun startComputeNextMove(): String? {
    engine.startComputeBestMove()
    engine.stopComputeBestMove()
    return "bestmove ${engine.getCurrentBestMove()?.toAlgebraicNotation()}"
  }

  private fun setEngineBoard(command: String): String? {
    val fromStart = command.contains("startpos")
    if (fromStart) {
      engine.setupBoard()
    } else {
      TODO("Load from FEN, not really used in practice")
    }

    if (command.contains("moves")) {
      val moves = command.substring(command.indexOf("moves") + 6).split(" ")
      moves.forEach {
        val (from, to) = lanInterpreter.read(it)
        engine.applyMove(from, to)
      }
    }

    return null
  }

  private fun initializeEngine(): String {
    engine.setupEngine()
    return "readyok"
  }

  private fun identifyEngine() = "id name ${engine.getName()}\nid author ${engine.getAuthor()}\noption name Hash type spin default 1 min 1 max 128\nuciok"
}

class UciEngineEventsHandler : EngineEventsHandler {
  override fun handle(event: EngineEvent) =
    when (event) {
      is CurrentlyExploringDepthEvent -> printDepth(event)
      is NodePerSecondsStatisticsEvent -> printNps(event)
      is BestLineFoundSoFar -> printBestLine(event)
    }

  private fun printBestLine(event: BestLineFoundSoFar) {
    println("info score cp ${event.score.roundToInt() * 100} depth ${event.depth} nodes ${event.depth} pv ${event.moves.map { it.toAlgebraicNotation() }.joinToString(" ")}")
  }

  private fun printDepth(event: CurrentlyExploringDepthEvent) {
    println("info depth ${event.depth}")
  }

  private fun printNps(event: NodePerSecondsStatisticsEvent) {
    println("info nps ${event.value}")
  }
}
