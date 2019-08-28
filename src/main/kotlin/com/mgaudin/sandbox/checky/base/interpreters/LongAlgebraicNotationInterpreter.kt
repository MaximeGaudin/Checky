package com.mgaudin.sandbox.checky.base.interpreters

import com.mgaudin.sandbox.checky.base.Position

class LongAlgebraicNotationInterpreter {
  fun read(moveInLongAlgebraicNotation: String): LongAlgebraicMove {
    var fromFile = moveInLongAlgebraicNotation[0]
    var fromRank = moveInLongAlgebraicNotation[1]

    var toFile = moveInLongAlgebraicNotation[2]
    var toRank = moveInLongAlgebraicNotation[3]

    return LongAlgebraicMove(
      toPosition(fromFile, fromRank),
      toPosition(toFile, toRank)
    )
  }

  private fun toPosition(file: Char, rank: Char): Position {
    return Position(
      when (file) {
        'a' -> 0
        'b' -> 1
        'c' -> 2
        'd' -> 3
        'e' -> 4
        'f' -> 5
        'g' -> 6
        'h' -> 7
        else -> throw IllegalArgumentException("Invalid file $file")
      },
      rank.toString().toInt() - 1
    )
  }
}

data class LongAlgebraicMove(
  val from: Position,
  val to: Position
)
