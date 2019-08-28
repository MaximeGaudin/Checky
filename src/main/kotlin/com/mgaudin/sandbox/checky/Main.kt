@file:Suppress("UNUSED_PARAMETER")

package com.mgaudin.sandbox.checky

import com.mgaudin.sandbox.checky.base.interpreters.LongAlgebraicNotationInterpreter
import org.w3c.dom.HTMLParagraphElement
import kotlin.browser.document
import kotlin.browser.window

external class Chessboard(id: String, config: ChessboardConfiguration) {
  fun position(fenString: String)
}

data class ChessboardConfiguration(
  val position: String = "start",
  val draggable: Boolean = false,
  val onDragStart: ((source: String, piece: String, position: String, orientation: String) -> Boolean)? = null,
  val onDrop: ((source: String, target: String) -> String?)? = null,
  val onSnapEnd: (() -> Unit)? = null
) {
  fun toJsObject(): dynamic {
    val o = js("{}")
    o["position"] = position
    o["draggable"] = draggable
    o["onDragStart"] = onDragStart
    o["onDrop"] = onDrop
    o["onSnapEnd"] = onSnapEnd
    return o
  }
}

var board: Chessboard? = null
val engine = CheckyEngine()
val lanInterpreter = LongAlgebraicNotationInterpreter()

fun onDragStart(
  source: String,
  piece: String,
  position: String,
  orientation: String
): Boolean {
  if (piece.startsWith("b")) {
    return false
  }

  return true
}

fun onDrop(
  source: String,
  target: String
): String? {
  val move = lanInterpreter.read(source + target)
  if (!engine.isMoveValid(move.from, move.to)) {
    return "snapback"
  }
  engine.applyMove(move.from, move.to)

  val status = document.getElementById("status") as HTMLParagraphElement
  status.textContent = "Checky is thinking (~5-10s)..."

  window.setTimeout({
    engine.startComputeBestMove()
    val engineMove = engine.getCurrentBestMove()
    engineMove?.let {
      engine.applyMove(it.from, it.to)
      board?.position(engine.toFen())
    }

    status.textContent = "White to play : Your turn"
  }, 250
  )

  return null
}

fun onSnapEnd() {

}

fun main() {
  engine.setupBoard()

  board = Chessboard(
    "myBoard",
    ChessboardConfiguration(
      "start",
      true,
      { source, piece, position, orientation -> onDragStart(source, piece, position, orientation) },
      { source, target -> onDrop(source, target) },
      { onSnapEnd() }
    ).toJsObject()
  )
}
