package com.mgaudin.sandbox.checky.base

sealed class EngineEvent

data class CurrentlyExploringDepthEvent(
  val depth: Int
) : EngineEvent()

data class NodePerSecondsStatisticsEvent(
  val value: Long
) : EngineEvent()

data class BestLineFoundSoFar(
  val score: Double,
  val depth: Int,
  val nodes: Long,
  val moves: List<Move>
) : EngineEvent()
