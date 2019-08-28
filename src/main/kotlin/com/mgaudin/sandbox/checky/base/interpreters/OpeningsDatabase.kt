package com.mgaudin.sandbox.checky.base.interpreters

import com.mgaudin.sandbox.checky.base.Move
import com.mgaudin.sandbox.checky.base.Piece
import com.mgaudin.sandbox.checky.base.boards.x88Board

class OpeningsDatabase {
  private val longAlgebraicNotationInterpreter = LongAlgebraicNotationInterpreter()
  private val openings: List<Opening> = loadOpenings()

  fun getOpeningsFrom(moves: List<Move>): List<Opening> {
    return openings
      .filter {
        var found = true
        for (i in 0 until moves.size) {
          if (i >= it.moves.size || moves[i] != it.moves[i]) {
            found = false
            break
          }
        }
        found
      }
  }

  private fun loadOpenings(): ArrayList<Opening> {
    return parseOpenings(OPENING_DATA.split("\n"))
  }

  private fun parseOpenings(lines: List<String>): ArrayList<Opening> {
    var openings = ArrayList<Opening>()
    var openingLines = ArrayList<String>()
    for (line in lines) {
      if (line.isBlank()) {
        openings.add(parseOpening(openingLines))
        openingLines.clear()
      }

      openingLines.add(line)
    }

    return openings
  }

  internal fun parseOpening(openingLines: List<String>): Opening {
    val eco = openingLines[0].replace("\\[ECO \"([A-Z]{1}[0-9]{2})\"\\]".toRegex(), "$1")
    val name = openingLines[1].replace("\\[Opening \"(.*)\"\\]".toRegex(), "$1")
    val variation =
      if (openingLines.size == 4) openingLines[2].replace("\\[Variation \"(.*)\"\\]".toRegex(), "$1")
      else null

    var board = x88Board()
    Piece.STARTING_PIECES.forEach { board.setPiece(it) }
    val moves = openingLines.last().split(" ")
      .map { longAlgebraicNotationInterpreter.read(it) }
      .map {
        val piece = board.getPiece(it.from)
        val move = Move(piece!!, it.from, it.to, board.getPiece(it.to))
        board = board.copyWithMove(it.from, it.to)
        move
      }

    return Opening(eco, name, variation, moves)
  }

  companion object {
    val OPENING_DATA = """
      [ECO "A00"]
      [Opening "Polish (Sokolsky) opening"]
      b2b4
      
      [ECO "A00"]
      [Opening "Polish"]
      [Variation "Tuebingen variation"]
      b2b4 g8h6
      
      [ECO "A00"]
      [Opening "Polish"]
      [Variation "Outflank variation"]
      b2b4 c7c6
      
      [ECO "A00"]
      [Opening "Benko's opening"]
      g2g3
      
      [ECO "A00"]
      [Opening "Lasker simul special"]
      g2g3 h7h5
      
      [ECO "A00"]
      [Opening "Benko's opening"]
      [Variation "reversed Alekhine"]
      g2g3 e7e5 g1f3
      
      [ECO "A00"]
      [Opening "Grob's attack"]
      g2g4
      
      [ECO "A00"]
      [Opening "Grob"]
      [Variation "spike attack"]
      g2g4 d7d5 f1g2 c7c6 g4g5
      
      [ECO "A00"]
      [Opening "Grob"]
      [Variation "Fritz gambit"]
      g2g4 d7d5 f1g2 c8g4 c2c4
      
      [ECO "A00"]
      [Opening "Grob"]
      [Variation "Romford counter-gambit"]
      g2g4 d7d5 f1g2 c8g4 c2c4 d5d4
      
      [ECO "A00"]
      [Opening "Clemenz (Mead's, Basman's or de Klerk's) opening"]
      h2h3
      
      [ECO "A00"]
      [Opening "Global opening"]
      h2h3 e7e5 a2a3
      
      [ECO "A00"]
      [Opening "Amar (Paris) opening"]
      g1h3
      
      [ECO "A00"]
      [Opening "Amar gambit"]
      g1h3 d7d5 g2g3 e7e5 f2f4 c8h3 f1h3 e5f4
      
      [ECO "A00"]
      [Opening "Dunst (Sleipner, Heinrichsen) opening"]
      b1c3
      
      [ECO "A00"]
      [Opening "Dunst (Sleipner, Heinrichsen) opening"]
      b1c3 e7e5
      
      [ECO "A00"]
      [Opening "Battambang opening"]
      b1c3 e7e5 a2a3
      
      [ECO "A00"]
      [Opening "Novosibirsk opening"]
      b1c3 c7c5 d2d4 c5d4 d1d4 b8c6 d4h4
      
      [ECO "A00"]
      [Opening "Anderssen's opening"]
      a2a3
      
      [ECO "A00"]
      [Opening "Ware (Meadow Hay) opening"]
      a2a4
      
      [ECO "A00"]
      [Opening "Crab opening"]
      a2a4 e7e5 h2h4
      
      [ECO "A00"]
      [Opening "Saragossa opening"]
      c2c3
      
      [ECO "A00"]
      [Opening "Mieses opening"]
      d2d3
      
      [ECO "A00"]
      [Opening "Mieses opening"]
      d2d3 e7e5
      
      [ECO "A00"]
      [Opening "Valencia opening"]
      d2d3 e7e5 b1d2
      
      [ECO "A00"]
      [Opening "Venezolana opening"]
      d2d3 c7c5 b1c3 b8c6 g2g3
      
      [ECO "A00"]
      [Opening "Van't Kruijs opening"]
      e2e3
      
      [ECO "A00"]
      [Opening "Amsterdam attack"]
      e2e3 e7e5 c2c4 d7d6 b1c3 b8c6 b2b3 g8f6
      
      [ECO "A00"]
      [Opening "Gedult's opening"]
      f2f3
      
      [ECO "A00"]
      [Opening "Hammerschlag (Fried fox/Pork chop opening)"]
      f2f3 e7e5 e1f2
      
      [ECO "A00"]
      [Opening "Anti-Borg (Desprez) opening"]
      h2h4
      
      [ECO "A00"]
      [Opening "Durkin's attack"]
      b1a3
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      b2b3
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "modern variation"]
      b2b3 e7e5
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "Indian variation"]
      b2b3 g8f6
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "classical variation"]
      b2b3 d7d5
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "English variation"]
      b2b3 c7c5
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "Dutch variation"]
      b2b3 f7f5
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "Polish variation"]
      b2b3 b7b5
      
      [ECO "A01"]
      [Opening "Nimzovich-Larsen attack"]
      [Variation "symmetrical variation"]
      b2b3 b7b6
      
      [ECO "A02"]
      [Opening "Bird's opening"]
      f2f4
      
      [ECO "A02"]
      [Opening "Bird"]
      [Variation "From gambit"]
      f2f4 e7e5
      
      [ECO "A02"]
      [Opening "Bird"]
      [Variation "From gambit, Lasker variation"]
      f2f4 e7e5 f4e5 d7d6 e5d6 f8d6 g1f3 g7g5
      
      [ECO "A02"]
      [Opening "Bird"]
      [Variation "From gambit, Lipke variation"]
      f2f4 e7e5 f4e5 d7d6 e5d6 f8d6 g1f3 g8h6 d2d4
      
      [ECO "A02"]
      [Opening "Bird's opening, Swiss gambit"]
      f2f4 f7f5 e2e4 f5e4 b1c3 g8f6 g2g4
      
      [ECO "A02"]
      [Opening "Bird"]
      [Variation "Hobbs gambit"]
      f2f4 g7g5
      
      [ECO "A03"]
      [Opening "Bird's opening"]
      f2f4 d7d5
      
      [ECO "A03"]
      [Opening "Mujannah opening"]
      f2f4 d7d5 c2c4
      
      [ECO "A03"]
      [Opening "Bird's opening"]
      [Variation "Williams gambit"]
      f2f4 d7d5 e2e4
      
      [ECO "A03"]
      [Opening "Bird's opening"]
      [Variation "Lasker variation"]
      f2f4 d7d5 g1f3 g8f6 e2e3 c7c5
      
      [ECO "A04"]
      [Opening "Reti opening"]
      g1f3
      
      [ECO "A04"]
      [Opening "Reti v Dutch"]
      g1f3 f7f5
      
      [ECO "A04"]
      [Opening "Reti"]
      [Variation "Pirc-Lisitsin gambit"]
      g1f3 f7f5 e2e4
      
      [ECO "A04"]
      [Opening "Reti"]
      [Variation "Lisitsin gambit deferred"]
      g1f3 f7f5 d2d3 g8f6 e2e4
      
      [ECO "A04"]
      [Opening "Reti opening"]
      g1f3 d7d6
      
      [ECO "A04"]
      [Opening "Reti"]
      [Variation "Wade defence"]
      g1f3 d7d6 e2e4 c8g4
      
      [ECO "A04"]
      [Opening "Reti"]
      [Variation "Herrstroem gambit"]
      g1f3 g7g5
      
      [ECO "A05"]
      [Opening "Reti opening"]
      g1f3 g8f6
      
      [ECO "A05"]
      [Opening "Reti"]
      [Variation "King's Indian attack, Spassky's variation"]
      g1f3 g8f6 g2g3 b7b5
      
      [ECO "A05"]
      [Opening "Reti"]
      [Variation "King's Indian attack"]
      g1f3 g8f6 g2g3 g7g6
      
      [ECO "A05"]
      [Opening "Reti"]
      [Variation "King's Indian attack, Reti-Smyslov variation"]
      g1f3 g8f6 g2g3 g7g6 b2b4
      
      [ECO "A06"]
      [Opening "Reti opening"]
      g1f3 d7d5
      
      [ECO "A06"]
      [Opening "Reti"]
      [Variation "old Indian attack"]
      g1f3 d7d5 d2d3
      
      [ECO "A06"]
      [Opening "Santasiere's folly"]
      g1f3 d7d5 b2b4
      
      [ECO "A06"]
      [Opening "Tennison (Lemberg, Zukertort) gambit"]
      g1f3 d7d5 e2e4
      
      [ECO "A06"]
      [Opening "Reti"]
      [Variation "Nimzovich-Larsen attack"]
      g1f3 d7d5 b2b3
      
      [ECO "A07"]
      [Opening "Reti"]
      [Variation "King's Indian attack (Barcza system)"]
      g1f3 d7d5 g2g3
      
      [ECO "A07"]
      [Opening "Reti"]
      [Variation "King's Indian attack, Yugoslav variation"]
      g1f3 d7d5 g2g3 g8f6 f1g2 c7c6 e1g1 c8g4
      
      [ECO "A07"]
      [Opening "Reti"]
      [Variation "King's Indian attack, Keres variation"]
      g1f3 d7d5 g2g3 c8g4 f1g2 b8d7
      
      [ECO "A07"]
      [Opening "Reti"]
      [Variation "King's Indian attack"]
      g1f3 d7d5 g2g3 g7g6
      
      [ECO "A07"]
      [Opening "Reti"]
      [Variation "King's Indian attack, Pachman system"]
      g1f3 d7d5 g2g3 g7g6 f1g2 f8g7 e1g1 e7e5 d2d3 g8e7
      
      [ECO "A07"]
      [Opening "Reti"]
      [Variation "King's Indian attack (with ...c5)"]
      g1f3 d7d5 g2g3 c7c5
      
      [ECO "A08"]
      [Opening "Reti"]
      [Variation "King's Indian attack"]
      g1f3 d7d5 g2g3 c7c5 f1g2
      
      [ECO "A08"]
      [Opening "Reti"]
      [Variation "King's Indian attack, French variation"]
      g1f3 d7d5 g2g3 c7c5 f1g2 b8c6 e1g1 e7e6 d2d3 g8f6 b1d2 f8e7 e2e4 e8g8 f1e1
      
      [ECO "A09"]
      [Opening "Reti opening"]
      g1f3 d7d5 c2c4
      
      [ECO "A09"]
      [Opening "Reti"]
      [Variation "advance variation"]
      g1f3 d7d5 c2c4 d5d4
      
      [ECO "A09"]
      [Opening "Reti accepted"]
      g1f3 d7d5 c2c4 d5c4
      
      [ECO "A09"]
      [Opening "Reti accepted"]
      [Variation "Keres variation"]
      g1f3 d7d5 c2c4 d5c4 e2e3 c8e6
      
      [ECO "A10"]
      [Opening "English opening"]
      c2c4
      
      [ECO "A10"]
      [Opening "English opening"]
      c2c4 g7g6
      
      [ECO "A10"]
      [Opening "English"]
      [Variation "Adorjan defence"]
      c2c4 g7g6 e2e4 e7e5
      
      [ECO "A10"]
      [Opening "English"]
      [Variation "Jaenisch gambit"]
      c2c4 b7b5
      
      [ECO "A10"]
      [Opening "English"]
      [Variation "Anglo-Dutch defense"]
      c2c4 f7f5
      
      [ECO "A11"]
      [Opening "English"]
      [Variation "Caro-Kann defensive system"]
      c2c4 c7c6
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "Caro-Kann defensive system"]
      c2c4 c7c6 g1f3 d7d5 b2b3
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "Torre defensive system"]
      c2c4 c7c6 g1f3 d7d5 b2b3 g8f6 g2g3 c8g4
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "London defensive system"]
      c2c4 c7c6 g1f3 d7d5 b2b3 g8f6 g2g3 c8f5
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "Caro-Kann defensive system"]
      c2c4 c7c6 g1f3 d7d5 b2b3 g8f6 c1b2
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "Bled variation"]
      c2c4 c7c6 g1f3 d7d5 b2b3 g8f6 c1b2 g7g6
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "New York (London) defensive system"]
      c2c4 c7c6 g1f3 d7d5 b2b3 g8f6 c1b2 c8f5
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "Capablanca's variation"]
      c2c4 c7c6 g1f3 d7d5 b2b3 g8f6 c1b2 c8g4
      
      [ECO "A12"]
      [Opening "English"]
      [Variation "Caro-Kann defensive system, Bogolyubov variation"]
      c2c4 c7c6 g1f3 d7d5 b2b3 c8g4
      
      [ECO "A13"]
      [Opening "English opening"]
      c2c4 e7e6
      
      [ECO "A13"]
      [Opening "English"]
      [Variation "Romanishin gambit"]
      c2c4 e7e6 g1f3 g8f6 g2g3 a7a6 f1g2 b7b5
      
      [ECO "A13"]
      [Opening "English opening"]
      [Variation "Agincourt variation"]
      c2c4 e7e6 g1f3 d7d5
      
      [ECO "A13"]
      [Opening "English"]
      [Variation "Wimpey system"]
      c2c4 e7e6 g1f3 d7d5 b2b3 g8f6 c1b2 c7c5 e2e3
      
      [ECO "A13"]
      [Opening "English opening"]
      [Variation "Agincourt variation"]
      c2c4 e7e6 g1f3 d7d5 g2g3
      
      [ECO "A13"]
      [Opening "English"]
      [Variation "Kurajica defence"]
      c2c4 e7e6 g1f3 d7d5 g2g3 c7c6
      
      [ECO "A13"]
      [Opening "English"]
      [Variation "Neo-Catalan"]
      c2c4 e7e6 g1f3 d7d5 g2g3 g8f6
      
      [ECO "A13"]
      [Opening "English"]
      [Variation "Neo-Catalan accepted"]
      c2c4 e7e6 g1f3 d7d5 g2g3 g8f6 f1g2 d5c4
      
      [ECO "A14"]
      [Opening "English"]
      [Variation "Neo-Catalan declined"]
      c2c4 e7e6 g1f3 d7d5 g2g3 g8f6 f1g2 f8e7 e1g1
      
      [ECO "A14"]
      [Opening "English"]
      [Variation "Symmetrical, Keres defence"]
      c2c4 e7e6 g1f3 d7d5 g2g3 g8f6 f1g2 f8e7 e1g1 c7c5 c4d5 f6d5 b1c3 b8c6
      
      [ECO "A15"]
      [Opening "English, 1...Nf6 (Anglo-Indian defense)"]
      c2c4 g8f6
      
      [ECO "A15"]
      [Opening "English orang-utan"]
      c2c4 g8f6 b2b4
      
      [ECO "A15"]
      [Opening "English opening"]
      c2c4 g8f6 g1f3
      
      [ECO "A16"]
      [Opening "English opening"]
      c2c4 g8f6 b1c3
      
      [ECO "A16"]
      [Opening "English"]
      [Variation "Anglo-Gruenfeld defense"]
      c2c4 g8f6 b1c3 d7d5
      
      [ECO "A16"]
      [Opening "English"]
      [Variation "Anglo-Gruenfeld, Smyslov defense"]
      c2c4 g8f6 b1c3 d7d5 c4d5 f6d5 g2g3 g7g6 f1g2 d5c3
      
      [ECO "A16"]
      [Opening "English"]
      [Variation "Anglo-Gruenfeld, Czech defense"]
      c2c4 g8f6 b1c3 d7d5 c4d5 f6d5 g2g3 g7g6 f1g2 d5b6
      
      [ECO "A16"]
      [Opening "English"]
      [Variation "Anglo-Gruenfeld defense"]
      c2c4 g8f6 b1c3 d7d5 c4d5 f6d5 g1f3
      
      [ECO "A16"]
      [Opening "English"]
      [Variation "Anglo-Gruenfeld defense, Korchnoi variation"]
      c2c4 g8f6 b1c3 d7d5 c4d5 f6d5 g1f3 g7g6 g2g3 f8g7 f1g2 e7e5
      
      [ECO "A17"]
      [Opening "English opening"]
      c2c4 g8f6 b1c3 e7e6
      
      [ECO "A17"]
      [Opening "English"]
      [Variation "Queens Indian formation"]
      c2c4 g8f6 b1c3 e7e6 g1f3 b7b6
      
      [ECO "A17"]
      [Opening "English"]
      [Variation "Queens Indian, Romanishin variation"]
      c2c4 g8f6 b1c3 e7e6 g1f3 b7b6 e2e4 c8b7 f1d3
      
      [ECO "A17"]
      [Opening "English"]
      [Variation "Nimzo-English opening"]
      c2c4 g8f6 b1c3 e7e6 g1f3 f8b4
      
      [ECO "A18"]
      [Opening "English"]
      [Variation "Mikenas-Carls variation"]
      c2c4 g8f6 b1c3 e7e6 e2e4
      
      [ECO "A18"]
      [Opening "English"]
      [Variation "Mikenas-Carls, Flohr variation"]
      c2c4 g8f6 b1c3 e7e6 e2e4 d7d5 e4e5
      
      [ECO "A18"]
      [Opening "English"]
      [Variation "Mikenas-Carls, Kevitz variation"]
      c2c4 g8f6 b1c3 e7e6 e2e4 b8c6
      
      [ECO "A19"]
      [Opening "English"]
      [Variation "Mikenas-Carls, Sicilian variation"]
      c2c4 g8f6 b1c3 e7e6 e2e4 c7c5
      
      [ECO "A20"]
      [Opening "English opening"]
      c2c4 e7e5
      
      [ECO "A20"]
      [Opening "English, Nimzovich variation"]
      c2c4 e7e5 g1f3
      
      [ECO "A20"]
      [Opening "English, Nimzovich, Flohr variation"]
      c2c4 e7e5 g1f3 e5e4
      
      [ECO "A21"]
      [Opening "English opening"]
      c2c4 e7e5 b1c3
      
      [ECO "A21"]
      [Opening "English, Troeger defence"]
      c2c4 e7e5 b1c3 d7d6 g2g3 c8e6 f1g2 b8c6
      
      [ECO "A21"]
      [Opening "English, Keres variation"]
      c2c4 e7e5 b1c3 d7d6 g2g3 c7c6
      
      [ECO "A21"]
      [Opening "English opening"]
      c2c4 e7e5 b1c3 d7d6 g1f3
      
      [ECO "A21"]
      [Opening "English, Smyslov defence"]
      c2c4 e7e5 b1c3 d7d6 g1f3 c8g4
      
      [ECO "A21"]
      [Opening "English, Kramnik-Shirov counterattack"]
      c2c4 e7e5 b1c3 f8b4
      
      [ECO "A22"]
      [Opening "English opening"]
      c2c4 e7e5 b1c3 g8f6
      
      [ECO "A22"]
      [Opening "English"]
      [Variation "Bellon gambit"]
      c2c4 e7e5 b1c3 g8f6 g1f3 e5e4 f3g5 b7b5
      
      [ECO "A22"]
      [Opening "English"]
      [Variation "Carls' Bremen system"]
      c2c4 e7e5 b1c3 g8f6 g2g3
      
      [ECO "A22"]
      [Opening "English"]
      [Variation "Bremen, reverse dragon"]
      c2c4 e7e5 b1c3 g8f6 g2g3 d7d5
      
      [ECO "A22"]
      [Opening "English"]
      [Variation "Bremen, Smyslov system"]
      c2c4 e7e5 b1c3 g8f6 g2g3 f8b4
      
      [ECO "A23"]
      [Opening "English"]
      [Variation "Bremen system, Keres variation"]
      c2c4 e7e5 b1c3 g8f6 g2g3 c7c6
      
      [ECO "A24"]
      [Opening "English"]
      [Variation "Bremen system with ...g6"]
      c2c4 e7e5 b1c3 g8f6 g2g3 g7g6
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "Sicilian reversed"]
      c2c4 e7e5 b1c3 b8c6
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "closed system"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "closed, Taimanov variation"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 e2e3 d7d6 g1e2 g8h6
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "closed, Hort variation"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 e2e3 d7d6 g1e2 c8e6
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "closed, 5.Rb1"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 a1b1
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "closed, 5.Rb1 Taimanov variation"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 a1b1 g8h6
      
      [ECO "A25"]
      [Opening "English"]
      [Variation "closed system (without ...d6)"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3
      
      [ECO "A26"]
      [Opening "English"]
      [Variation "closed system"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6
      
      [ECO "A26"]
      [Opening "English"]
      [Variation "Botvinnik system"]
      c2c4 e7e5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6 e2e4
      
      [ECO "A27"]
      [Opening "English"]
      [Variation "three knights system"]
      c2c4 e7e5 b1c3 b8c6 g1f3
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights system"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "Nenarokov variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 d2d4 e5d4 f3d4 f8b4 c1g5 h7h6 g5h4 b4c3 b2c3 c6e5
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "Bradley Beach variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 d2d4 e5e4
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights, Nimzovich variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 e2e4
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights, Marini variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 a2a3
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights, Capablanca variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 d2d3
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights, 4.e3"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 e2e3
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights, Stean variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 e2e3 f8b4 d1c2 e8g8 c3d5 f8e8 c2f5
      
      [ECO "A28"]
      [Opening "English"]
      [Variation "four knights, Romanishin variation"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 e2e3 f8b4 d1c2 b4c3
      
      [ECO "A29"]
      [Opening "English"]
      [Variation "four knights, kingside fianchetto"]
      c2c4 e7e5 b1c3 b8c6 g1f3 g8f6 g2g3
      
      [ECO "A30"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5
      
      [ECO "A30"]
      [Opening "English"]
      [Variation "symmetrical, hedgehog system"]
      c2c4 c7c5 g1f3 g8f6 g2g3 b7b6 f1g2 c8b7 e1g1 e7e6 b1c3 f8e7
      
      [ECO "A30"]
      [Opening "English"]
      [Variation "symmetrical, hedgehog, flexible formation"]
      c2c4 c7c5 g1f3 g8f6 g2g3 b7b6 f1g2 c8b7 e1g1 e7e6 b1c3 f8e7 d2d4 c5d4 d1d4 d7d6 f1d1 a7a6 b2b3 b8d7
      
      [ECO "A31"]
      [Opening "English"]
      [Variation "symmetrical, Benoni formation"]
      c2c4 c7c5 g1f3 g8f6 d2d4
      
      [ECO "A32"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 g1f3 g8f6 d2d4 c5d4 f3d4 e7e6
      
      [ECO "A33"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 g1f3 g8f6 d2d4 c5d4 f3d4 e7e6 b1c3 b8c6
      
      [ECO "A33"]
      [Opening "English"]
      [Variation "symmetrical, Geller variation"]
      c2c4 c7c5 g1f3 g8f6 d2d4 c5d4 f3d4 e7e6 b1c3 b8c6 g2g3 d8b6
      
      [ECO "A34"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 b1c3
      
      [ECO "A34"]
      [Opening "English"]
      [Variation "symmetrical, three knights system"]
      c2c4 c7c5 b1c3 g8f6 g1f3 d7d5 c4d5 f6d5
      
      [ECO "A34"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 b1c3 g8f6 g2g3
      
      [ECO "A34"]
      [Opening "English"]
      [Variation "symmetrical, Rubinstein system"]
      c2c4 c7c5 b1c3 g8f6 g2g3 d7d5 c4d5 f6d5 f1g2 d5c7
      
      [ECO "A35"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 b1c3 b8c6
      
      [ECO "A35"]
      [Opening "English"]
      [Variation "symmetrical, four knights system"]
      c2c4 c7c5 b1c3 b8c6 g1f3 g8f6
      
      [ECO "A36"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 b1c3 b8c6 g2g3
      
      [ECO "A36"]
      [Opening "English"]
      [Variation "ultra-symmetrical variation"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7
      
      [ECO "A36"]
      [Opening "English"]
      [Variation "symmetrical, Botvinnik system reversed"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 e2e3 e7e5
      
      [ECO "A36"]
      [Opening "English"]
      [Variation "symmetrical, Botvinnik system"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 e2e4
      
      [ECO "A37"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 g1f3
      
      [ECO "A37"]
      [Opening "English"]
      [Variation "symmetrical, Botvinnik system reversed"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 g1f3 e7e5
      
      [ECO "A38"]
      [Opening "English"]
      [Variation "symmetrical variation"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 g1f3 g8f6
      
      [ECO "A38"]
      [Opening "English"]
      [Variation "symmetrical, main line with d3"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d3
      
      [ECO "A38"]
      [Opening "English"]
      [Variation "symmetrical, main line with b3"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 b2b3
      
      [ECO "A39"]
      [Opening "English"]
      [Variation "symmetrical, main line with d4"]
      c2c4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 g1f3 g8f6 e1g1 e8g8 d2d4
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      d2d4
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      [Variation "Lundin (Kevitz-Mikenas) defence"]
      d2d4 b8c6
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      [Variation "Charlick (Englund) gambit"]
      d2d4 e7e5
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      [Variation "Englund gambit"]
      d2d4 e7e5 d4e5 b8c6 g1f3 d8e7 d1d5 f7f6 e5f6 g8f6
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      [Variation "English defence"]
      d2d4 b7b6
      
      [ECO "A40"]
      [Opening "Polish defence"]
      d2d4 b7b5
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      d2d4 e7e6
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      [Variation "Keres defence"]
      d2d4 e7e6 c2c4 b7b6
      
      [ECO "A40"]
      [Opening "Queen's pawn"]
      [Variation "Franco-Indian (Keres) defence"]
      d2d4 e7e6 c2c4 f8b4
      
      [ECO "A40"]
      [Opening "Modern defence"]
      d2d4 g7g6
      
      [ECO "A40"]
      [Opening "Beefeater defence"]
      d2d4 g7g6 c2c4 f8g7 b1c3 c7c5 d4d5 g7c3 b2c3 f7f5
      
      [ECO "A41"]
      [Opening "Queen's Pawn"]
      d2d4 d7d6
      
      [ECO "A41"]
      [Opening "Old Indian"]
      [Variation "Tartakower (Wade) variation"]
      d2d4 d7d6 g1f3 c8g4
      
      [ECO "A41"]
      [Opening "Old Indian defence"]
      d2d4 d7d6 c2c4
      
      [ECO "A41"]
      [Opening "Modern defence"]
      d2d4 d7d6 c2c4 g7g6 b1c3 f8g7
      
      [ECO "A41"]
      [Opening "Robatsch defence"]
      [Variation "Rossolimo variation"]
      e2e4 g7g6 d2d4 f8g7 g1f3 d7d6 c2c4 c8g4
      
      [ECO "A42"]
      [Opening "Modern defence"]
      [Variation "Averbakh system"]
      d2d4 d7d6 c2c4 g7g6 b1c3 f8g7 e2e4
      
      [ECO "A42"]
      [Opening "Pterodactyl defence"]
      d2d4 d7d6 c2c4 g7g6 b1c3 f8g7 e2e4 c7c5 g1f3 d8a5
      
      [ECO "A42"]
      [Opening "Modern defence"]
      [Variation "Averbakh system, Randspringer variation"]
      d2d4 d7d6 c2c4 g7g6 b1c3 f8g7 e2e4 f7f5
      
      [ECO "A42"]
      [Opening "Modern defence"]
      [Variation "Averbakh system, Kotov variation"]
      d2d4 d7d6 c2c4 g7g6 b1c3 f8g7 e2e4 b8c6
      
      [ECO "A43"]
      [Opening "Old Benoni defence"]
      d2d4 c7c5
      
      [ECO "A43"]
      [Opening "Old Benoni"]
      [Variation "Franco-Benoni defence"]
      d2d4 c7c5 d4d5 e7e6 e2e4
      
      [ECO "A43"]
      [Opening "Old Benoni"]
      [Variation "Mujannah formation"]
      d2d4 c7c5 d4d5 f7f5
      
      [ECO "A43"]
      [Opening "Old Benoni defence"]
      d2d4 c7c5 d4d5 g8f6
      
      [ECO "A43"]
      [Opening "Woozle defence"]
      d2d4 c7c5 d4d5 g8f6 b1c3 d8a5
      
      [ECO "A43"]
      [Opening "Old Benoni defence"]
      d2d4 c7c5 d4d5 g8f6 g1f3
      
      [ECO "A43"]
      [Opening "Hawk (Habichd) defence"]
      d2d4 c7c5 d4d5 g8f6 g1f3 c5c4
      
      [ECO "A43"]
      [Opening "Old Benoni defence"]
      d2d4 c7c5 d4d5 d7d6
      
      [ECO "A43"]
      [Opening "Old Benoni"]
      [Variation "Schmid's system"]
      d2d4 c7c5 d4d5 d7d6 b1c3 g7g6
      
      [ECO "A44"]
      [Opening "Old Benoni defence"]
      d2d4 c7c5 d4d5 e7e5
      
      [ECO "A44"]
      [Opening "Semi-Benoni (`blockade variation')"]
      d2d4 c7c5 d4d5 e7e5 e2e4 d7d6
      
      [ECO "A45"]
      [Opening "Queen's pawn game"]
      d2d4 g8f6
      
      [ECO "A45"]
      [Opening "Queen's pawn"]
      [Variation "Bronstein gambit"]
      d2d4 g8f6 g2g4
      
      [ECO "A45"]
      [Opening "Canard opening"]
      d2d4 g8f6 f2f4
      
      [ECO "A45"]
      [Opening "Paleface attack"]
      d2d4 g8f6 f2f3
      
      [ECO "A45"]
      [Opening "Blackmar-Diemer gambit"]
      d2d4 g8f6 f2f3 d7d5 e2e4
      
      [ECO "A45"]
      [Opening "Gedult attack"]
      d2d4 g8f6 f2f3 d7d5 g2g4
      
      [ECO "A45"]
      [Opening "Trompovsky attack (Ruth, Opovcensky opening)"]
      d2d4 g8f6 c1g5
      
      [ECO "A46"]
      [Opening "Queen's pawn game"]
      d2d4 g8f6 g1f3
      
      [ECO "A46"]
      [Opening "Queen's pawn"]
      [Variation "Torre attack"]
      d2d4 g8f6 g1f3 e7e6 c1g5
      
      [ECO "A46"]
      [Opening "Queen's pawn"]
      [Variation "Torre attack, Wagner gambit"]
      d2d4 g8f6 g1f3 e7e6 c1g5 c7c5 e2e4
      
      [ECO "A46"]
      [Opening "Queen's pawn"]
      [Variation "Yusupov-Rubinstein system"]
      d2d4 g8f6 g1f3 e7e6 e2e3
      
      [ECO "A46"]
      [Opening "Doery defence"]
      d2d4 g8f6 g1f3 f6e4
      
      [ECO "A47"]
      [Opening "Queen's Indian defence"]
      d2d4 g8f6 g1f3 b7b6
      
      [ECO "A47"]
      [Opening "Queen's Indian"]
      [Variation "Marienbad system"]
      d2d4 g8f6 g1f3 b7b6 g2g3 c8b7 f1g2 c7c5
      
      [ECO "A47"]
      [Opening "Queen's Indian"]
      [Variation "Marienbad system, Berg variation"]
      d2d4 g8f6 g1f3 b7b6 g2g3 c8b7 f1g2 c7c5 c2c4 c5d4 d1d4
      
      [ECO "A48"]
      [Opening "King's Indian"]
      [Variation "East Indian defence"]
      d2d4 g8f6 g1f3 g7g6
      
      [ECO "A48"]
      [Opening "King's Indian"]
      [Variation "Torre attack"]
      d2d4 g8f6 g1f3 g7g6 c1g5
      
      [ECO "A48"]
      [Opening "King's Indian"]
      [Variation "London system"]
      d2d4 g8f6 g1f3 g7g6 c1f4
      
      [ECO "A49"]
      [Opening "King's Indian"]
      [Variation "fianchetto without c4"]
      d2d4 g8f6 g1f3 g7g6 g2g3
      
      [ECO "A50"]
      [Opening "Queen's pawn game"]
      d2d4 g8f6 c2c4
      
      [ECO "A50"]
      [Opening "Kevitz-Trajkovich defence"]
      d2d4 g8f6 c2c4 b8c6
      
      [ECO "A50"]
      [Opening "Queen's Indian accelerated"]
      d2d4 g8f6 c2c4 b7b6
      
      [ECO "A51"]
      [Opening "Budapest defence declined"]
      d2d4 g8f6 c2c4 e7e5
      
      [ECO "A51"]
      [Opening "Budapest"]
      [Variation "Fajarowicz variation"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6e4
      
      [ECO "A51"]
      [Opening "Budapest"]
      [Variation "Fajarowicz, Steiner variation"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6e4 d1c2
      
      [ECO "A52"]
      [Opening "Budapest defence"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6g4
      
      [ECO "A52"]
      [Opening "Budapest"]
      [Variation "Adler variation"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6g4 g1f3
      
      [ECO "A52"]
      [Opening "Budapest"]
      [Variation "Rubinstein variation"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6g4 c1f4
      
      [ECO "A52"]
      [Opening "Budapest"]
      [Variation "Alekhine variation"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6g4 e2e4
      
      [ECO "A52"]
      [Opening "Budapest"]
      [Variation "Alekhine, Abonyi variation"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6g4 e2e4 g4e5 f2f4 e5c6
      
      [ECO "A52"]
      [Opening "Budapest"]
      [Variation "Alekhine variation, Balogh gambit"]
      d2d4 g8f6 c2c4 e7e5 d4e5 f6g4 e2e4 d7d6
      
      [ECO "A53"]
      [Opening "Old Indian defence"]
      d2d4 g8f6 c2c4 d7d6
      
      [ECO "A53"]
      [Opening "Old Indian"]
      [Variation "Janowski variation"]
      d2d4 g8f6 c2c4 d7d6 b1c3 c8f5
      
      [ECO "A54"]
      [Opening "Old Indian"]
      [Variation "Ukrainian variation"]
      d2d4 g8f6 c2c4 d7d6 b1c3 e7e5
      
      [ECO "A54"]
      [Opening "Old Indian"]
      [Variation "Dus-Khotimirsky variation"]
      d2d4 g8f6 c2c4 d7d6 b1c3 e7e5 e2e3 b8d7 f1d3
      
      [ECO "A54"]
      [Opening "Old Indian"]
      [Variation "Ukrainian variation, 4.Nf3"]
      d2d4 g8f6 c2c4 d7d6 b1c3 e7e5 g1f3
      
      [ECO "A55"]
      [Opening "Old Indian"]
      [Variation "main line"]
      d2d4 g8f6 c2c4 d7d6 b1c3 e7e5 g1f3 b8d7 e2e4
      
      [ECO "A56"]
      [Opening "Benoni defence"]
      d2d4 g8f6 c2c4 c7c5
      
      [ECO "A56"]
      [Opening "Benoni defence, Hromodka system"]
      d2d4 g8f6 c2c4 c7c5 d4d5 d7d6
      
      [ECO "A56"]
      [Opening "Vulture defence"]
      d2d4 g8f6 c2c4 c7c5 d4d5 f6e4
      
      [ECO "A56"]
      [Opening "Czech Benoni defence"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e5
      
      [ECO "A56"]
      [Opening "Czech Benoni"]
      [Variation "King's Indian system"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e5 b1c3 d7d6 e2e4 g7g6
      
      [ECO "A57"]
      [Opening "Benko gambit"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5
      
      [ECO "A57"]
      [Opening "Benko gambit half accepted"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6
      
      [ECO "A57"]
      [Opening "Benko gambit"]
      [Variation "Zaitsev system"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b1c3
      
      [ECO "A57"]
      [Opening "Benko gambit"]
      [Variation "Nescafe Frappe attack"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b1c3 a6b5 e2e4 b5b4 c3b5 d7d6 f1c4
      
      [ECO "A58"]
      [Opening "Benko gambit accepted"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6
      
      [ECO "A58"]
      [Opening "Benko gambit"]
      [Variation "Nd2 variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6 c8a6 b1c3 d7d6 g1f3 g7g6 f3d2
      
      [ECO "A58"]
      [Opening "Benko gambit"]
      [Variation "fianchetto variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6 c8a6 b1c3 d7d6 g1f3 g7g6 g2g3
      
      [ECO "A59"]
      [Opening "Benko gambit"]
      [Variation "7.e4"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6 c8a6 b1c3 d7d6 e2e4
      
      [ECO "A59"]
      [Opening "Benko gambit"]
      [Variation "Ne2 variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6 c8a6 b1c3 d7d6 e2e4 a6f1 e1f1 g7g6 g1e2
      
      [ECO "A59"]
      [Opening "Benko gambit"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6 c8a6 b1c3 d7d6 e2e4 a6f1 e1f1 g7g6 g2g3
      
      [ECO "A59"]
      [Opening "Benko gambit"]
      [Variation "main line"]
      d2d4 g8f6 c2c4 c7c5 d4d5 b7b5 c4b5 a7a6 b5a6 c8a6 b1c3 d7d6 e2e4 a6f1 e1f1 g7g6 g2g3 f8g7 f1g2 e8g8 g1f3
      
      [ECO "A60"]
      [Opening "Benoni defence"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6
      
      [ECO "A61"]
      [Opening "Benoni defence"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6
      
      [ECO "A61"]
      [Opening "Benoni"]
      [Variation "Uhlmann variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6 c1g5
      
      [ECO "A61"]
      [Opening "Benoni"]
      [Variation "Nimzovich (knight's tour) variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6 f3d2
      
      [ECO "A61"]
      [Opening "Benoni"]
      [Variation "fianchetto variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6 g2g3
      
      [ECO "A62"]
      [Opening "Benoni"]
      [Variation "fianchetto variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6 g2g3 f8g7 f1g2 e8g8
      
      [ECO "A63"]
      [Opening "Benoni"]
      [Variation "fianchetto, 9...Nbd7"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6 g2g3 f8g7 f1g2 e8g8 e1g1 b8d7
      
      [ECO "A64"]
      [Opening "Benoni"]
      [Variation "fianchetto, 11...Re8"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 g1f3 g7g6 g2g3 f8g7 f1g2 e8g8 e1g1 b8d7 f3d2 a7a6 a2a4 f8e8
      
      [ECO "A65"]
      [Opening "Benoni"]
      [Variation "6.e4"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4
      
      [ECO "A66"]
      [Opening "Benoni"]
      [Variation "pawn storm variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f2f4
      
      [ECO "A66"]
      [Opening "Benoni"]
      [Variation "Mikenas variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f2f4 f8g7 e4e5
      
      [ECO "A67"]
      [Opening "Benoni"]
      [Variation "Taimanov variation"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f2f4 f8g7 f1b5
      
      [ECO "A68"]
      [Opening "Benoni"]
      [Variation "four pawns attack"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f2f4 f8g7 g1f3 e8g8
      
      [ECO "A69"]
      [Opening "Benoni"]
      [Variation "four pawns attack, main line"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 f2f4 f8g7 g1f3 e8g8 f1e2 f8e8
      
      [ECO "A70"]
      [Opening "Benoni"]
      [Variation "classical with e4 and Nf3"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3
      
      [ECO "A70"]
      [Opening "Benoni"]
      [Variation "classical without 9.O-O"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2
      
      [ECO "A71"]
      [Opening "Benoni"]
      [Variation "classical, 8.Bg5"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 c1g5
      
      [ECO "A72"]
      [Opening "Benoni"]
      [Variation "classical without 9.O-O"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8
      
      [ECO "A73"]
      [Opening "Benoni"]
      [Variation "classical, 9.O-O"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1
      
      [ECO "A74"]
      [Opening "Benoni"]
      [Variation "classical, 9...a6, 10.a4"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1 a7a6 a2a4
      
      [ECO "A75"]
      [Opening "Benoni"]
      [Variation "classical with ...a6 and 10...Bg4"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1 a7a6 a2a4 c8g4
      
      [ECO "A76"]
      [Opening "Benoni"]
      [Variation "classical, 9...Re8"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1 f8e8
      
      [ECO "A77"]
      [Opening "Benoni"]
      [Variation "classical, 9...Re8, 10.Nd2"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1 f8e8 f3d2
      
      [ECO "A78"]
      [Opening "Benoni"]
      [Variation "classical with ...Re8 and ...Na6"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1 f8e8 f3d2 b8a6
      
      [ECO "A79"]
      [Opening "Benoni"]
      [Variation "classical, 11.f3"]
      d2d4 g8f6 c2c4 c7c5 d4d5 e7e6 b1c3 e6d5 c4d5 d7d6 e2e4 g7g6 g1f3 f8g7 f1e2 e8g8 e1g1 f8e8 f3d2 b8a6 f2f3
      
      [ECO "A80"]
      [Opening "Dutch"]
      d2d4 f7f5
      
      [ECO "A80"]
      [Opening "Dutch, Spielmann gambit"]
      d2d4 f7f5 b1c3 g8f6 g2g4
      
      [ECO "A80"]
      [Opening "Dutch, Manhattan (Alapin, Ulvestad) variation"]
      d2d4 f7f5 d1d3
      
      [ECO "A80"]
      [Opening "Dutch, Von Pretzel gambit"]
      d2d4 f7f5 d1d3 e7e6 g2g4
      
      [ECO "A80"]
      [Opening "Dutch, Korchnoi attack"]
      d2d4 f7f5 h2h3
      
      [ECO "A80"]
      [Opening "Dutch, Krejcik gambit"]
      d2d4 f7f5 g2g4
      
      [ECO "A80"]
      [Opening "Dutch, 2.Bg5 variation"]
      d2d4 f7f5 c1g5
      
      [ECO "A81"]
      [Opening "Dutch defence"]
      d2d4 f7f5 g2g3
      
      [ECO "A81"]
      [Opening "Dutch defence, Blackburne variation"]
      d2d4 f7f5 g2g3 g8f6 f1g2 e7e6 g1h3
      
      [ECO "A81"]
      [Opening "Dutch defence"]
      d2d4 f7f5 g2g3 g8f6 f1g2 g7g6
      
      [ECO "A81"]
      [Opening "Dutch"]
      [Variation "Leningrad, Basman system"]
      d2d4 f7f5 g2g3 g7g6 f1g2 f8g7 g1f3 c7c6 e1g1 g8h6
      
      [ECO "A81"]
      [Opening "Dutch"]
      [Variation "Leningrad, Karlsbad variation"]
      d2d4 f7f5 g2g3 g7g6 f1g2 f8g7 g1h3
      
      [ECO "A82"]
      [Opening "Dutch"]
      [Variation "Staunton gambit"]
      d2d4 f7f5 e2e4
      
      [ECO "A82"]
      [Opening "Dutch"]
      [Variation "Balogh defence"]
      d2d4 f7f5 e2e4 d7d6
      
      [ECO "A82"]
      [Opening "Dutch"]
      [Variation "Staunton gambit"]
      d2d4 f7f5 e2e4 f5e4
      
      [ECO "A82"]
      [Opening "Dutch"]
      [Variation "Staunton gambit, Tartakower variation"]
      d2d4 f7f5 e2e4 f5e4 b1c3 g8f6 g2g4
      
      [ECO "A83"]
      [Opening "Dutch"]
      [Variation "Staunton gambit, Staunton's line"]
      d2d4 f7f5 e2e4 f5e4 b1c3 g8f6 c1g5
      
      [ECO "A83"]
      [Opening "Dutch"]
      [Variation "Staunton gambit, Alekhine variation"]
      d2d4 f7f5 e2e4 f5e4 b1c3 g8f6 c1g5 g7g6 h2h4
      
      [ECO "A83"]
      [Opening "Dutch"]
      [Variation "Staunton gambit, Lasker variation"]
      d2d4 f7f5 e2e4 f5e4 b1c3 g8f6 c1g5 g7g6 f2f3
      
      [ECO "A83"]
      [Opening "Dutch"]
      [Variation "Staunton gambit, Chigorin variation"]
      d2d4 f7f5 e2e4 f5e4 b1c3 g8f6 c1g5 c7c6
      
      [ECO "A83"]
      [Opening "Dutch"]
      [Variation "Staunton gambit, Nimzovich variation"]
      d2d4 f7f5 e2e4 f5e4 b1c3 g8f6 c1g5 b7b6
      
      [ECO "A84"]
      [Opening "Dutch defence"]
      d2d4 f7f5 c2c4
      
      [ECO "A84"]
      [Opening "Dutch defence"]
      [Variation "Bladel variation"]
      d2d4 f7f5 c2c4 g7g6 b1c3 g8h6
      
      [ECO "A84"]
      [Opening "Dutch defence"]
      d2d4 f7f5 c2c4 e7e6
      
      [ECO "A84"]
      [Opening "Dutch defence, Rubinstein variation"]
      d2d4 f7f5 c2c4 e7e6 b1c3
      
      [ECO "A84"]
      [Opening "Dutch"]
      [Variation "Staunton gambit deferred"]
      d2d4 f7f5 c2c4 e7e6 e2e4
      
      [ECO "A84"]
      [Opening "Dutch defence"]
      d2d4 f7f5 c2c4 g8f6
      
      [ECO "A85"]
      [Opening "Dutch with c4 & Nc3"]
      d2d4 f7f5 c2c4 g8f6 b1c3
      
      [ECO "A86"]
      [Opening "Dutch with c4 & g3"]
      d2d4 f7f5 c2c4 g8f6 g2g3
      
      [ECO "A86"]
      [Opening "Dutch"]
      [Variation "Hort-Antoshin system"]
      d2d4 f7f5 c2c4 g8f6 g2g3 d7d6 f1g2 c7c6 b1c3 d8c7
      
      [ECO "A86"]
      [Opening "Dutch"]
      [Variation "Leningrad variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 g7g6
      
      [ECO "A87"]
      [Opening "Dutch"]
      [Variation "Leningrad, main variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 g7g6 f1g2 f8g7 g1f3
      
      [ECO "A88"]
      [Opening "Dutch"]
      [Variation "Leningrad, main variation with c6"]
      d2d4 f7f5 c2c4 g8f6 g2g3 g7g6 f1g2 f8g7 g1f3 e8g8 e1g1 d7d6 b1c3 c7c6
      
      [ECO "A89"]
      [Opening "Dutch"]
      [Variation "Leningrad, main variation with Nc6"]
      d2d4 f7f5 c2c4 g8f6 g2g3 g7g6 f1g2 f8g7 g1f3 e8g8 e1g1 d7d6 b1c3 b8c6
      
      [ECO "A90"]
      [Opening "Dutch defence"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2
      
      [ECO "A90"]
      [Opening "Dutch defence"]
      [Variation "Dutch-Indian (Nimzo-Dutch) variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8b4
      
      [ECO "A90"]
      [Opening "Dutch-Indian, Alekhine variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8b4 c1d2 b4e7
      
      [ECO "A91"]
      [Opening "Dutch defence"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7
      
      [ECO "A92"]
      [Opening "Dutch defence"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8
      
      [ECO "A92"]
      [Opening "Dutch defence, Alekhine variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 f6e4
      
      [ECO "A92"]
      [Opening "Dutch"]
      [Variation "stonewall variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d5
      
      [ECO "A92"]
      [Opening "Dutch"]
      [Variation "stonewall with Nc3"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d5 b1c3
      
      [ECO "A93"]
      [Opening "Dutch"]
      [Variation "stonewall, Botwinnik variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d5 b2b3
      
      [ECO "A94"]
      [Opening "Dutch"]
      [Variation "stonewall with Ba3"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d5 b2b3 c7c6 c1a3
      
      [ECO "A95"]
      [Opening "Dutch"]
      [Variation "stonewall with Nc3"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d5 b1c3 c7c6
      
      [ECO "A95"]
      [Opening "Dutch"]
      [Variation "stonewall: Chekhover variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d5 b1c3 c7c6 d1c2 d8e8 c1g5
      
      [ECO "A96"]
      [Opening "Dutch"]
      [Variation "classical variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d6
      
      [ECO "A97"]
      [Opening "Dutch"]
      [Variation "Ilyin-Genevsky variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d6 b1c3 d8e8
      
      [ECO "A97"]
      [Opening "Dutch"]
      [Variation "Ilyin-Genevsky, Winter variation"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d6 b1c3 d8e8 f1e1
      
      [ECO "A98"]
      [Opening "Dutch"]
      [Variation "Ilyin-Genevsky variation with Qc2"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d6 b1c3 d8e8 d1c2
      
      [ECO "A99"]
      [Opening "Dutch"]
      [Variation "Ilyin-Genevsky variation with b3"]
      d2d4 f7f5 c2c4 g8f6 g2g3 e7e6 f1g2 f8e7 g1f3 e8g8 e1g1 d7d6 b1c3 d8e8 b2b3
      
      [ECO "B00"]
      [Opening "King's pawn opening"]
      e2e4
      
      [ECO "B00"]
      [Opening "Hippopotamus defence"]
      e2e4 g8h6 d2d4 g7g6 c2c4 f7f6
      
      [ECO "B00"]
      [Opening "Corn stalk defence"]
      e2e4 a7a5
      
      [ECO "B00"]
      [Opening "Lemming defence"]
      e2e4 b8a6
      
      [ECO "B00"]
      [Opening "Fred"]
      e2e4 f7f5
      
      [ECO "B00"]
      [Opening "Barnes defence"]
      e2e4 f7f6
      
      [ECO "B00"]
      [Opening "Fried fox defence"]
      e2e4 f7f6 d2d4 e8f7
      
      [ECO "B00"]
      [Opening "Carr's defence"]
      e2e4 h7h6
      
      [ECO "B00"]
      [Opening "Reversed Grob (Borg/Basman defence/macho Grob)"]
      e2e4 g7g5
      
      [ECO "B00"]
      [Opening "St. George (Baker) defence"]
      e2e4 a7a6
      
      [ECO "B00"]
      [Opening "Owen defence"]
      e2e4 b7b6
      
      [ECO "B00"]
      [Opening "Guatemala defence"]
      e2e4 b7b6 d2d4 c8a6
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Nimzovich defence"]
      e2e4 b8c6
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Nimzovich defence, Wheeler gambit"]
      e2e4 b8c6 b2b4 c6b4 c2c3 b4c6 d2d4
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Nimzovich defence"]
      e2e4 b8c6 g1f3
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Colorado counter"]
      e2e4 b8c6 g1f3 f7f5
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Nimzovich defence"]
      e2e4 b8c6 d2d4
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Nimzovich defence, Marshall gambit"]
      e2e4 b8c6 d2d4 d7d5 e4d5 d8d5 b1c3
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Nimzovich defence, Bogolyubov variation"]
      e2e4 b8c6 d2d4 d7d5 b1c3
      
      [ECO "B00"]
      [Opening "KP"]
      [Variation "Neo-Mongoloid defence"]
      e2e4 b8c6 d2d4 f7f6
      
      [ECO "B01"]
      [Opening "Scandinavian (centre counter) defence"]
      e2e4 d7d5
      
      [ECO "B01"]
      [Opening "Scandinavian defence, Lasker variation"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 g8f6 g1f3 c8g4 h2h3
      
      [ECO "B01"]
      [Opening "Scandinavian defence"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 g8f6 g1f3 c8f5
      
      [ECO "B01"]
      [Opening "Scandinavian defence, Gruenfeld variation"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 g8f6 g1f3 c8f5 f3e5 c7c6 g2g4
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Anderssen counter-attack"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 e7e5
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Anderssen counter-attack orthodox attack"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 e7e5 d4e5 f8b4 c1d2 b8c6 g1f3
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Anderssen counter-attack, Goteborg system"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 e7e5 g1f3
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Anderssen counter-attack, Collijn variation"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 d2d4 e7e5 g1f3 c8g4
      
      [ECO "B01"]
      [Opening "Scandinavian, Mieses-Kotrvc gambit"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5a5 b2b4
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Pytel-Wade variation"]
      e2e4 d7d5 e4d5 d8d5 b1c3 d5d6
      
      [ECO "B01"]
      [Opening "Scandinavian defence"]
      e2e4 d7d5 e4d5 g8f6
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Icelandic gambit"]
      e2e4 d7d5 e4d5 g8f6 c2c4 e7e6
      
      [ECO "B01"]
      [Opening "Scandinavian gambit"]
      e2e4 d7d5 e4d5 g8f6 c2c4 c7c6
      
      [ECO "B01"]
      [Opening "Scandinavian defence"]
      e2e4 d7d5 e4d5 g8f6 d2d4
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Marshall variation"]
      e2e4 d7d5 e4d5 g8f6 d2d4 f6d5
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Kiel variation"]
      e2e4 d7d5 e4d5 g8f6 d2d4 f6d5 c2c4 d5b4
      
      [ECO "B01"]
      [Opening "Scandinavian"]
      [Variation "Richter variation"]
      e2e4 d7d5 e4d5 g8f6 d2d4 g7g6
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      e2e4 g8f6
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Scandinavian variation"]
      e2e4 g8f6 b1c3 d7d5
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Spielmann variation"]
      e2e4 g8f6 b1c3 d7d5 e4e5 f6d7 e5e6
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Maroczy variation"]
      e2e4 g8f6 d2d3
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Krejcik variation"]
      e2e4 g8f6 f1c4
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Mokele Mbembe (Buecker) variation"]
      e2e4 g8f6 e4e5 f6e4
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Brooklyn defence"]
      e2e4 g8f6 e4e5 f6g8
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      e2e4 g8f6 e4e5 f6d5
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Kmoch variation"]
      e2e4 g8f6 e4e5 f6d5 f1c4 d5b6 c4b3 c7c5 d2d3
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Saemisch attack"]
      e2e4 g8f6 e4e5 f6d5 b1c3
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Welling variation"]
      e2e4 g8f6 e4e5 f6d5 b2b3
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      e2e4 g8f6 e4e5 f6d5 c2c4
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "Steiner variation"]
      e2e4 g8f6 e4e5 f6d5 c2c4 d5b6 b2b3
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "two pawns' (Lasker's) attack"]
      e2e4 g8f6 e4e5 f6d5 c2c4 d5b6 c4c5
      
      [ECO "B02"]
      [Opening "Alekhine's defence"]
      [Variation "two pawns' attack, Mikenas variation"]
      e2e4 g8f6 e4e5 f6d5 c2c4 d5b6 c4c5 b6d5 f1c4 e7e6 b1c3 d7d6
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      e2e4 g8f6 e4e5 f6d5 d2d4
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "O'Sullivan gambit"]
      e2e4 g8f6 e4e5 f6d5 d2d4 b7b5
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "Balogh variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 f1c4
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "exchange variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 e5d6
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "exchange, Karpov variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 e5d6 c7d6 g1f3 g7g6 f1e2 f8g7 e1g1 e8g8 h2h3 b8c6 b1c3 c8f5 c1f4
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, Korchnoi variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 d6e5 f4e5 c8f5 b1c3 e7e6 g1f3 f8e7 f1e2 e8g8 e1g1 f7f6
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, 6...Nc6"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 d6e5 f4e5 b8c6
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, Ilyin-Genevsky var."]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 d6e5 f4e5 b8c6 g1f3 c8g4 e5e6 f7e6 c4c5
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, 7.Be3"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 d6e5 f4e5 b8c6 c1e3
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, Tartakower variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 d6e5 f4e5 b8c6 c1e3 c8f5 b1c3 e7e6 g1f3 d8d7 f1e2 e8c8 e1g1 f8e7
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, Planinc variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 g7g5
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, fianchetto variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 g7g6
      
      [ECO "B03"]
      [Opening "Alekhine's defence"]
      [Variation "four pawns attack, Trifunovic variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 c2c4 d5b6 f2f4 c8f5
      
      [ECO "B04"]
      [Opening "Alekhine's defence"]
      [Variation "modern variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3
      
      [ECO "B04"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Larsen variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 d6e5
      
      [ECO "B04"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Schmid variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 d5b6
      
      [ECO "B04"]
      [Opening "Alekhine's defence"]
      [Variation "modern, fianchetto variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 g7g6
      
      [ECO "B04"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Keres variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 g7g6 f1c4 d5b6 c4b3 f8g7 a2a4
      
      [ECO "B05"]
      [Opening "Alekhine's defence"]
      [Variation "modern variation, 4...Bg4"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 c8g4
      
      [ECO "B05"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Flohr variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 c8g4 f1e2 c7c6
      
      [ECO "B05"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Panov variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 c8g4 h2h3
      
      [ECO "B05"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Alekhine variation"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 c8g4 c2c4
      
      [ECO "B05"]
      [Opening "Alekhine's defence"]
      [Variation "modern, Vitolins attack"]
      e2e4 g8f6 e4e5 f6d5 d2d4 d7d6 g1f3 c8g4 c2c4 d5b6 d4d5
      
      [ECO "B06"]
      [Opening "Robatsch (modern) defence"]
      e2e4 g7g6
      
      [ECO "B06"]
      [Opening "Norwegian defence"]
      e2e4 g7g6 d2d4 g8f6 e4e5 f6h5 g2g4 h5g7
      
      [ECO "B06"]
      [Opening "Robatsch (modern) defence"]
      e2e4 g7g6 d2d4 f8g7
      
      [ECO "B06"]
      [Opening "Robatsch defence"]
      [Variation "three pawns attack"]
      e2e4 g7g6 d2d4 f8g7 f2f4
      
      [ECO "B06"]
      [Opening "Robatsch defence"]
      e2e4 g7g6 d2d4 f8g7 b1c3
      
      [ECO "B06"]
      [Opening "Robatsch defence"]
      [Variation "Gurgenidze variation"]
      e2e4 g7g6 d2d4 f8g7 b1c3 c7c6 f2f4 d7d5 e4e5 h7h5
      
      [ECO "B06"]
      [Opening "Robatsch (modern) defence"]
      e2e4 g7g6 d2d4 f8g7 b1c3 d7d6
      
      [ECO "B06"]
      [Opening "Robatsch defence"]
      [Variation "two knights variation"]
      e2e4 g7g6 d2d4 f8g7 b1c3 d7d6 g1f3
      
      [ECO "B06"]
      [Opening "Robatsch defence"]
      [Variation "two knights, Suttles variation"]
      e2e4 g7g6 d2d4 f8g7 b1c3 d7d6 g1f3 c7c6
      
      [ECO "B06"]
      [Opening "Robatsch defence"]
      [Variation "Pseudo-Austrian attack"]
      e2e4 g7g6 d2d4 f8g7 b1c3 d7d6 f2f4
      
      [ECO "B07"]
      [Opening "Pirc defence"]
      e2e4 d7d6 d2d4 g8f6 b1c3
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "Ufimtsev-Pytel variation"]
      e2e4 d7d6 d2d4 g8f6 b1c3 c7c6
      
      [ECO "B07"]
      [Opening "Pirc defence"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "150 attack"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 c1e3 c7c6 d1d2
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "Sveshnikov system"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 g2g3
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "Holmov system"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f1c4
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "Byrne variation"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 c1g5
      
      [ECO "B07"]
      [Opening "Pirc defence"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f1e2
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "Chinese variation"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f1e2 f8g7 g2g4
      
      [ECO "B07"]
      [Opening "Pirc"]
      [Variation "bayonet (Mariotti) attack"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f1e2 f8g7 h2h4
      
      [ECO "B07"]
      [Opening "Robatsch defence"]
      [Variation "Geller's system"]
      e2e4 g7g6 d2d4 f8g7 g1f3 d7d6 c2c3
      
      [ECO "B08"]
      [Opening "Pirc"]
      [Variation "classical (two knights) system"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 g1f3
      
      [ECO "B08"]
      [Opening "Pirc"]
      [Variation "classical (two knights) system"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 g1f3 f8g7
      
      [ECO "B08"]
      [Opening "Pirc"]
      [Variation "classical, h3 system"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 g1f3 f8g7 h2h3
      
      [ECO "B08"]
      [Opening "Pirc"]
      [Variation "classical system, 5.Be2"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 g1f3 f8g7 f1e2
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4 f8g7 g1f3 e8g8
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack, 6.e5"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4 f8g7 g1f3 e8g8 e4e5
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack, 6.Be3"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4 f8g7 g1f3 e8g8 c1e3
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack, 6.Bd3"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4 f8g7 g1f3 e8g8 f1d3
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack, dragon formation"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4 f8g7 g1f3 c7c5
      
      [ECO "B09"]
      [Opening "Pirc"]
      [Variation "Austrian attack, Ljubojevic variation"]
      e2e4 d7d6 d2d4 g8f6 b1c3 g7g6 f2f4 f8g7 f1c4
      
      [ECO "B10"]
      [Opening "Caro-Kann defence"]
      e2e4 c7c6
      
      [ECO "B10"]
      [Opening "Caro-Kann"]
      [Variation "Hillbilly attack"]
      e2e4 c7c6 f1c4
      
      [ECO "B10"]
      [Opening "Caro-Kann"]
      [Variation "anti-Caro-Kann defence"]
      e2e4 c7c6 c2c4
      
      [ECO "B10"]
      [Opening "Caro-Kann"]
      [Variation "anti-anti-Caro-Kann defence"]
      e2e4 c7c6 c2c4 d7d5
      
      [ECO "B10"]
      [Opening "Caro-Kann"]
      [Variation "closed (Breyer) variation"]
      e2e4 c7c6 d2d3
      
      [ECO "B10"]
      [Opening "Caro-Kann defence"]
      e2e4 c7c6 b1c3
      
      [ECO "B10"]
      [Opening "Caro-Kann"]
      [Variation "Goldman (Spielmann) variation"]
      e2e4 c7c6 b1c3 d7d5 d1f3
      
      [ECO "B10"]
      [Opening "Caro-Kann"]
      [Variation "two knights variation"]
      e2e4 c7c6 b1c3 d7d5 g1f3
      
      [ECO "B11"]
      [Opening "Caro-Kann"]
      [Variation "two knights, 3...Bg4"]
      e2e4 c7c6 b1c3 d7d5 g1f3 c8g4
      
      [ECO "B12"]
      [Opening "Caro-Kann defence"]
      e2e4 c7c6 d2d4
      
      [ECO "B12"]
      [Opening "de Bruycker defence"]
      e2e4 c7c6 d2d4 b8a6 b1c3 a6c7
      
      [ECO "B12"]
      [Opening "Caro-Masi defence"]
      e2e4 c7c6 d2d4 g8f6
      
      [ECO "B12"]
      [Opening "Caro-Kann defence"]
      e2e4 c7c6 d2d4 d7d5
      
      [ECO "B12"]
      [Opening "Caro-Kann"]
      [Variation "Tartakower (fantasy) variation"]
      e2e4 c7c6 d2d4 d7d5 f2f3
      
      [ECO "B12"]
      [Opening "Caro-Kann"]
      [Variation "3.Nd2"]
      e2e4 c7c6 d2d4 d7d5 b1d2
      
      [ECO "B12"]
      [Opening "Caro-Kann"]
      [Variation "Edinburgh variation"]
      e2e4 c7c6 d2d4 d7d5 b1d2 d8b6
      
      [ECO "B12"]
      [Opening "Caro-Kann"]
      [Variation "advance variation"]
      e2e4 c7c6 d2d4 d7d5 e4e5
      
      [ECO "B12"]
      [Opening "Caro-Kann"]
      [Variation "advance, Short variation"]
      e2e4 c7c6 d2d4 d7d5 e4e5 c8f5 c2c3 e7e6 f1e2
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "exchange variation"]
      e2e4 c7c6 d2d4 d7d5 e4d5
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "exchange, Rubinstein variation"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 f1d3 b8c6 c2c3 g8f6 c1f4
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik attack"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik, Gunderam attack"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 c4c5
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik attack"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik, Herzog defence"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3 b8c6 c1g5 d5c4 d4d5 c6a5
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik, normal variation"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3 b8c6 c1g5 e7e6
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik, Czerniak variation"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3 b8c6 c1g5 d8a5
      
      [ECO "B13"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik, Reifir (Spielmann) variation"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3 b8c6 c1g5 d8b6
      
      [ECO "B14"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik attack, 5...e6"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3 e7e6
      
      [ECO "B14"]
      [Opening "Caro-Kann"]
      [Variation "Panov-Botvinnik attack, 5...g6"]
      e2e4 c7c6 d2d4 d7d5 e4d5 c6d5 c2c4 g8f6 b1c3 g7g6
      
      [ECO "B15"]
      [Opening "Caro-Kann defence"]
      e2e4 c7c6 d2d4 d7d5 b1c3
      
      [ECO "B15"]
      [Opening "Caro-Kann"]
      [Variation "Gurgenidze counter-attack"]
      e2e4 c7c6 d2d4 d7d5 b1c3 b7b5
      
      [ECO "B15"]
      [Opening "Caro-Kann"]
      [Variation "Gurgenidze system"]
      e2e4 c7c6 d2d4 d7d5 b1c3 g7g6
      
      [ECO "B15"]
      [Opening "Caro-Kann"]
      [Variation "Rasa-Studier gambit"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 f2f3
      
      [ECO "B15"]
      [Opening "Caro-Kann defence"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4
      
      [ECO "B15"]
      [Opening "Caro-Kann"]
      [Variation "Alekhine gambit"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 g8f6 f1d3
      
      [ECO "B15"]
      [Opening "Caro-Kann"]
      [Variation "Tartakower (Nimzovich) variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 g8f6 e4f6 e7f6
      
      [ECO "B15"]
      [Opening "Caro-Kann"]
      [Variation "Forgacs variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 g8f6 e4f6 e7f6 f1c4
      
      [ECO "B16"]
      [Opening "Caro-Kann"]
      [Variation "Bronstein-Larsen variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 g8f6 e4f6 g7f6
      
      [ECO "B17"]
      [Opening "Caro-Kann"]
      [Variation "Steinitz variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 b8d7
      
      [ECO "B18"]
      [Opening "Caro-Kann"]
      [Variation "classical variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 c8f5
      
      [ECO "B18"]
      [Opening "Caro-Kann"]
      [Variation "classical, Flohr variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 c8f5 e4g3 f5g6 g1h3
      
      [ECO "B18"]
      [Opening "Caro-Kann"]
      [Variation "classical, Maroczy attack"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 c8f5 e4g3 f5g6 f2f4
      
      [ECO "B18"]
      [Opening "Caro-Kann"]
      [Variation "classical, 6.h4"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 c8f5 e4g3 f5g6 h2h4
      
      [ECO "B19"]
      [Opening "Caro-Kann"]
      [Variation "classical, 7...Nd7"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 c8f5 e4g3 f5g6 h2h4 h7h6 g1f3 b8d7
      
      [ECO "B19"]
      [Opening "Caro-Kann"]
      [Variation "classical, Spassky variation"]
      e2e4 c7c6 d2d4 d7d5 b1c3 d5e4 c3e4 c8f5 e4g3 f5g6 h2h4 h7h6 g1f3 b8d7 h4h5
      
      [ECO "B20"]
      [Opening "Sicilian defence"]
      e2e4 c7c5
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "Gloria variation"]
      e2e4 c7c5 c2c4 d7d6 b1c3 b8c6 g2g3 h7h5
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "Steinitz variation"]
      e2e4 c7c5 g2g3
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "wing gambit"]
      e2e4 c7c5 b2b4
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "wing gambit, Santasiere variation"]
      e2e4 c7c5 b2b4 c5b4 c2c4
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "wing gambit, Marshall variation"]
      e2e4 c7c5 b2b4 c5b4 a2a3
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "wing gambit, Marienbad variation"]
      e2e4 c7c5 b2b4 c5b4 a2a3 d7d5 e4d5 d8d5 c1b2
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "wing gambit, Carlsbad variation"]
      e2e4 c7c5 b2b4 c5b4 a2a3 b4a3
      
      [ECO "B20"]
      [Opening "Sicilian"]
      [Variation "Keres variation (2.Ne2)"]
      e2e4 c7c5 g1e2
      
      [ECO "B21"]
      [Opening "Sicilian"]
      [Variation "Grand Prix attack"]
      e2e4 c7c5 f2f4
      
      [ECO "B21"]
      [Opening "Sicilian"]
      [Variation "Smith-Morra gambit"]
      e2e4 c7c5 d2d4
      
      [ECO "B21"]
      [Opening "Sicilian"]
      [Variation "Andreaschek gambit"]
      e2e4 c7c5 d2d4 c5d4 g1f3 e7e5 c2c3
      
      [ECO "B21"]
      [Opening "Sicilian"]
      [Variation "Smith-Morra gambit"]
      e2e4 c7c5 d2d4 c5d4 c2c3
      
      [ECO "B21"]
      [Opening "Sicilian"]
      [Variation "Smith-Morra gambit, Chicago defence"]
      e2e4 c7c5 d2d4 c5d4 c2c3 d4c3 b1c3 b8c6 g1f3 d7d6 f1c4 e7e6 e1g1 a7a6 d1e2 b7b5 c4b3 a8a7
      
      [ECO "B22"]
      [Opening "Sicilian"]
      [Variation "Alapin's variation (2.c3)"]
      e2e4 c7c5 c2c3
      
      [ECO "B22"]
      [Opening "Sicilian"]
      [Variation "2.c3, Heidenfeld variation"]
      e2e4 c7c5 c2c3 g8f6 e4e5 f6d5 g1f3 b8c6 b1a3
      
      [ECO "B23"]
      [Opening "Sicilian"]
      [Variation "closed"]
      e2e4 c7c5 b1c3
      
      [ECO "B23"]
      [Opening "Sicilian"]
      [Variation "closed, Korchnoi variation"]
      e2e4 c7c5 b1c3 e7e6 g2g3 d7d5
      
      [ECO "B23"]
      [Opening "Sicilian"]
      [Variation "closed, 2...Nc6"]
      e2e4 c7c5 b1c3 b8c6
      
      [ECO "B23"]
      [Opening "Sicilian"]
      [Variation "chameleon variation"]
      e2e4 c7c5 b1c3 b8c6 g1e2
      
      [ECO "B23"]
      [Opening "Sicilian"]
      [Variation "Grand Prix attack"]
      e2e4 c7c5 b1c3 b8c6 f2f4
      
      [ECO "B23"]
      [Opening "Sicilian"]
      [Variation "Grand Prix attack, Schofman variation"]
      e2e4 c7c5 b1c3 b8c6 f2f4 g7g6 g1f3 f8g7 f1c4 e7e6 f4f5
      
      [ECO "B24"]
      [Opening "Sicilian"]
      [Variation "closed"]
      e2e4 c7c5 b1c3 b8c6 g2g3
      
      [ECO "B24"]
      [Opening "Sicilian"]
      [Variation "closed, Smyslov variation"]
      e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 e7e6 c1e3 c6d4 c3e2
      
      [ECO "B25"]
      [Opening "Sicilian"]
      [Variation "closed"]
      e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6
      
      [ECO "B25"]
      [Opening "Sicilian"]
      [Variation "closed, 6.Ne2 e5 (Botvinnik)"]
      e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6 g1e2 e7e5
      
      [ECO "B25"]
      [Opening "Sicilian"]
      [Variation "closed, 6.f4"]
      e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6 f2f4
      
      [ECO "B25"]
      [Opening "Sicilian"]
      [Variation "closed, 6.f4 e5 (Botvinnik)"]
      e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6 f2f4 e7e5
      
      [ECO "B26"]
      [Opening "Sicilian"]
      [Variation "closed, 6.Be3"]
      e2e4 c7c5 b1c3 b8c6 g2g3 g7g6 f1g2 f8g7 d2d3 d7d6 c1e3
      
      [ECO "B27"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3
      
      [ECO "B27"]
      [Opening "Sicilian"]
      [Variation "Stiletto (Althouse) variation"]
      e2e4 c7c5 g1f3 d8a5
      
      [ECO "B27"]
      [Opening "Sicilian"]
      [Variation "Quinteros variation"]
      e2e4 c7c5 g1f3 d8c7
      
      [ECO "B27"]
      [Opening "Sicilian"]
      [Variation "Katalimov variation"]
      e2e4 c7c5 g1f3 b7b6
      
      [ECO "B27"]
      [Opening "Sicilian"]
      [Variation "Hungarian variation"]
      e2e4 c7c5 g1f3 g7g6
      
      [ECO "B27"]
      [Opening "Sicilian"]
      [Variation "Acton extension"]
      e2e4 c7c5 g1f3 g7g6 c2c4 f8h6
      
      [ECO "B28"]
      [Opening "Sicilian"]
      [Variation "O'Kelly variation"]
      e2e4 c7c5 g1f3 a7a6
      
      [ECO "B29"]
      [Opening "Sicilian"]
      [Variation "Nimzovich-Rubinstein variation"]
      e2e4 c7c5 g1f3 g8f6
      
      [ECO "B29"]
      [Opening "Sicilian"]
      [Variation "Nimzovich-Rubinstein; Rubinstein counter-gambit"]
      e2e4 c7c5 g1f3 g8f6 e4e5 f6d5 b1c3 e7e6 c3d5 e6d5 d2d4 b8c6
      
      [ECO "B30"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3 b8c6
      
      [ECO "B30"]
      [Opening "Sicilian"]
      [Variation "Nimzovich-Rossolimo attack (without ...d6)"]
      e2e4 c7c5 g1f3 b8c6 f1b5
      
      [ECO "B31"]
      [Opening "Sicilian"]
      [Variation "Nimzovich-Rossolimo attack (with ...g6, without ...d6)"]
      e2e4 c7c5 g1f3 b8c6 f1b5 g7g6
      
      [ECO "B31"]
      [Opening "Sicilian"]
      [Variation "Nimzovich-Rossolimo attack, Gurgenidze variation"]
      e2e4 c7c5 g1f3 b8c6 f1b5 g7g6 e1g1 f8g7 f1e1 e7e5 b2b4
      
      [ECO "B32"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3 b8c6 d2d4
      
      [ECO "B32"]
      [Opening "Sicilian"]
      [Variation "Flohr variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 d8c7
      
      [ECO "B32"]
      [Opening "Sicilian"]
      [Variation "Nimzovich variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 d7d5
      
      [ECO "B32"]
      [Opening "Sicilian"]
      [Variation "Labourdonnais-Loewenthal variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 e7e5
      
      [ECO "B32"]
      [Opening "Sicilian"]
      [Variation "Labourdonnais-Loewenthal (Kalashnikov) variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 e7e5 d4b5 d7d6
      
      [ECO "B33"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6
      
      [ECO "B33"]
      [Opening "Sicilian"]
      [Variation "Pelikan (Lasker/Sveshnikov) variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e5
      
      [ECO "B33"]
      [Opening "Sicilian"]
      [Variation "Pelikan, Bird variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e5 d4b5 d7d6 c1g5 a7a6 b5a3 c8e6
      
      [ECO "B33"]
      [Opening "Sicilian"]
      [Variation "Pelikan, Chelyabinsk variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e5 d4b5 d7d6 c1g5 a7a6 b5a3 b7b5
      
      [ECO "B33"]
      [Opening "Sicilian"]
      [Variation "Sveshnikov variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e5 d4b5 d7d6 c1g5 a7a6 b5a3 b7b5 g5f6 g7f6 c3d5 f6f5
      
      [ECO "B34"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, exchange variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 d4c6
      
      [ECO "B34"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, modern variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 b1c3
      
      [ECO "B35"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, modern variation with Bc4"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 b1c3 f8g7 c1e3 g8f6 f1c4
      
      [ECO "B36"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, Maroczy bind"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 c2c4
      
      [ECO "B36"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, Gurgenidze variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 c2c4 g8f6 b1c3 c6d4 d1d4 d7d6
      
      [ECO "B37"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, Maroczy bind, 5...Bg7"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 c2c4 f8g7
      
      [ECO "B37"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, Simagin variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 c2c4 f8g7 d4c2 d7d6 f1e2 g8h6
      
      [ECO "B38"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, Maroczy bind, 6.Be3"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 c2c4 f8g7 c1e3
      
      [ECO "B39"]
      [Opening "Sicilian"]
      [Variation "accelerated fianchetto, Breyer variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g7g6 c2c4 f8g7 c1e3 g8f6 b1c3 f6g4
      
      [ECO "B40"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3 e7e6
      
      [ECO "B40"]
      [Opening "Sicilian"]
      [Variation "Marshall variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 d7d5
      
      [ECO "B40"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4
      
      [ECO "B40"]
      [Opening "Sicilian"]
      [Variation "Anderssen variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 g8f6
      
      [ECO "B40"]
      [Opening "Sicilian"]
      [Variation "Pin variation (Sicilian counter-attack)"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 g8f6 b1c3 f8b4
      
      [ECO "B40"]
      [Opening "Sicilian"]
      [Variation "Pin, Jaffe variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 g8f6 b1c3 f8b4 f1d3 e6e5
      
      [ECO "B40"]
      [Opening "Sicilian"]
      [Variation "Pin, Koch variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 g8f6 b1c3 f8b4 e4e5
      
      [ECO "B41"]
      [Opening "Sicilian"]
      [Variation "Kan variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6
      
      [ECO "B41"]
      [Opening "Sicilian"]
      [Variation "Kan, Maroczy bind (Reti variation)"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 c2c4
      
      [ECO "B41"]
      [Opening "Sicilian"]
      [Variation "Kan, Maroczy bind - Bronstein variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 c2c4 g8f6 b1c3 f8b4 f1d3 b8c6 d3c2
      
      [ECO "B42"]
      [Opening "Sicilian"]
      [Variation "Kan, 5.Bd3"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 f1d3
      
      [ECO "B42"]
      [Opening "Sicilian"]
      [Variation "Kan, Gipslis variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 f1d3 g8f6 e1g1 d7d6 c2c4 g7g6
      
      [ECO "B42"]
      [Opening "Sicilian"]
      [Variation "Kan, Polugaievsky variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 f1d3 f8c5
      
      [ECO "B42"]
      [Opening "Sicilian"]
      [Variation "Kan, Swiss cheese variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 f1d3 g7g6
      
      [ECO "B43"]
      [Opening "Sicilian"]
      [Variation "Kan, 5.Nc3"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 a7a6 b1c3
      
      [ECO "B44"]
      [Opening "Sicilian defence"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6
      
      [ECO "B44"]
      [Opening "Sicilian, Szen (`anti-Taimanov') variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 d4b5
      
      [ECO "B44"]
      [Opening "Sicilian, Szen, hedgehog variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 d4b5 d7d6 c2c4 g8f6 b1c3 a7a6 b5a3 f8e7 f1e2 e8g8 e1g1 b7b6
      
      [ECO "B44"]
      [Opening "Sicilian, Szen variation, Dely-Kasparov gambit"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 d4b5 d7d6 c2c4 g8f6 b1c3 a7a6 b5a3 d6d5
      
      [ECO "B45"]
      [Opening "Sicilian"]
      [Variation "Taimanov variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 b1c3
      
      [ECO "B45"]
      [Opening "Sicilian"]
      [Variation "Taimanov, American attack"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 b1c3 g8f6 d4b5 f8b4 b5d6
      
      [ECO "B46"]
      [Opening "Sicilian"]
      [Variation "Taimanov variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 b1c3 a7a6
      
      [ECO "B47"]
      [Opening "Sicilian"]
      [Variation "Taimanov (Bastrikov) variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 b1c3 d8c7
      
      [ECO "B48"]
      [Opening "Sicilian"]
      [Variation "Taimanov variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 b1c3 d8c7 c1e3
      
      [ECO "B49"]
      [Opening "Sicilian"]
      [Variation "Taimanov variation"]
      e2e4 c7c5 g1f3 e7e6 d2d4 c5d4 f3d4 b8c6 b1c3 d8c7 c1e3 a7a6 f1e2
      
      [ECO "B50"]
      [Opening "Sicilian"]
      e2e4 c7c5 g1f3 d7d6
      
      [ECO "B50"]
      [Opening "Sicilian"]
      [Variation "wing gambit deferred"]
      e2e4 c7c5 g1f3 d7d6 b2b4
      
      [ECO "B51"]
      [Opening "Sicilian"]
      [Variation "Canal-Sokolsky (Nimzovich-Rossolimo, Moscow) attack"]
      e2e4 c7c5 g1f3 d7d6 f1b5
      
      [ECO "B52"]
      [Opening "Sicilian"]
      [Variation "Canal-Sokolsky attack, 3...Bd7"]
      e2e4 c7c5 g1f3 d7d6 f1b5 c8d7
      
      [ECO "B52"]
      [Opening "Sicilian"]
      [Variation "Canal-Sokolsky attack, Bronstein gambit"]
      e2e4 c7c5 g1f3 d7d6 f1b5 c8d7 b5d7 d8d7 e1g1 b8c6 c2c3 g8f6 d2d4
      
      [ECO "B52"]
      [Opening "Sicilian"]
      [Variation "Canal-Sokolsky attack, Sokolsky variation"]
      e2e4 c7c5 g1f3 d7d6 f1b5 c8d7 b5d7 d8d7 c2c4
      
      [ECO "B53"]
      [Opening "Sicilian, Chekhover variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 d1d4
      
      [ECO "B53"]
      [Opening "Sicilian"]
      [Variation "Chekhover, Zaitsev variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 d1d4 b8c6 f1b5 d8d7
      
      [ECO "B54"]
      [Opening "Sicilian"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4
      
      [ECO "B54"]
      [Opening "Sicilian"]
      [Variation "Prins (Moscow) variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 f2f3
      
      [ECO "B55"]
      [Opening "Sicilian"]
      [Variation "Prins variation, Venice attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 f2f3 e7e5 f1b5
      
      [ECO "B56"]
      [Opening "Sicilian"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3
      
      [ECO "B56"]
      [Opening "Sicilian"]
      [Variation "Venice attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e5 f1b5
      
      [ECO "B56"]
      [Opening "Sicilian"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6
      
      [ECO "B57"]
      [Opening "Sicilian"]
      [Variation "Sozin, not Scheveningen"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 f1c4
      
      [ECO "B57"]
      [Opening "Sicilian"]
      [Variation "Magnus Smith trap"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 f1c4 g7g6 d4c6 b7c6 e4e5
      
      [ECO "B57"]
      [Opening "Sicilian"]
      [Variation "Sozin, Benko variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 f1c4 d8b6
      
      [ECO "B58"]
      [Opening "Sicilian"]
      [Variation "classical"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 d7d6 f1e2
      
      [ECO "B58"]
      [Opening "Sicilian"]
      [Variation "Boleslavsky variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 d7d6 f1e2 e7e5
      
      [ECO "B58"]
      [Opening "Sicilian"]
      [Variation "Boleslavsky, Louma variation"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 d7d6 f1e2 e7e5 d4c6
      
      [ECO "B59"]
      [Opening "Sicilian"]
      [Variation "Boleslavsky variation, 7.Nb3"]
      e2e4 c7c5 g1f3 b8c6 d2d4 c5d4 f3d4 g8f6 b1c3 d7d6 f1e2 e7e5 d4b3
      
      [ECO "B60"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5
      
      [ECO "B60"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Bondarevsky variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 g7g6
      
      [ECO "B60"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Larsen variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 c8d7
      
      [ECO "B61"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Larsen variation, 7.Qd2"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 c8d7 d1d2
      
      [ECO "B62"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, 6...e6"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6
      
      [ECO "B62"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Podvebrady variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d4b3
      
      [ECO "B62"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Margate (Alekhine) variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 f1b5
      
      [ECO "B62"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Richter attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d4c6
      
      [ECO "B62"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Keres variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d3
      
      [ECO "B63"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2
      
      [ECO "B63"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...Be7"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 f8e7
      
      [ECO "B64"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...Be7 defence, 9.f4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 f8e7 e1c1 e8g8 f2f4
      
      [ECO "B64"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, Geller variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 f8e7 e1c1 e8g8 f2f4 e6e5
      
      [ECO "B65"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...Be7 defence, 9...Nxd4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 f8e7 e1c1 e8g8 f2f4 c6d4
      
      [ECO "B65"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...Be7 defence, 9...Nxd4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 f8e7 e1c1 e8g8 f2f4 c6d4 d2d4
      
      [ECO "B66"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...a6"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 a7a6
      
      [ECO "B67"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...a6 defence, 8...Bd7"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 a7a6 e1c1 c8d7
      
      [ECO "B68"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...a6 defence, 9...Be7"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 a7a6 e1c1 c8d7 f2f4 f8e7
      
      [ECO "B69"]
      [Opening "Sicilian"]
      [Variation "Richter-Rauzer, Rauzer attack, 7...a6 defence, 11.Bxf6"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 b8c6 c1g5 e7e6 d1d2 a7a6 e1c1 c8d7 f2f4 f8e7 d4f3 b7b5 g5f6
      
      [ECO "B70"]
      [Opening "Sicilian"]
      [Variation "dragon variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6
      
      [ECO "B71"]
      [Opening "Sicilian"]
      [Variation "dragon, Levenfish variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 f2f4
      
      [ECO "B71"]
      [Opening "Sicilian"]
      [Variation "dragon, Levenfish; Flohr variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 f2f4 b8d7
      
      [ECO "B72"]
      [Opening "Sicilian"]
      [Variation "dragon, 6.Be3"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3
      
      [ECO "B72"]
      [Opening "Sicilian"]
      [Variation "dragon, classical attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2
      
      [ECO "B72"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Amsterdam variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 d1d2
      
      [ECO "B72"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Grigoriev variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 d1d2 e8g8 e1c1
      
      [ECO "B72"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Nottingham variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 d4b3
      
      [ECO "B73"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, 8.O-O"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1
      
      [ECO "B73"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Zollner gambit"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 f2f4 d8b6 e4e5
      
      [ECO "B73"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Richter variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d1d2
      
      [ECO "B74"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, 9.Nb3"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d4b3
      
      [ECO "B74"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Stockholm attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d4b3 c8e6 f2f4 c6a5 f4f5 e6c4 b3a5 c4e2 d1e2 d8a5 g2g4
      
      [ECO "B74"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Spielmann variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d4b3 c8e6 f2f4 c6a5 f4f5 e6c4 e2d3
      
      [ECO "B74"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Bernard defence"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d4b3 c8e6 f2f4 c6a5 f4f5 e6c4 e2d3 c4d3 c2d3 d6d5
      
      [ECO "B74"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Reti-Tartakower variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d4b3 c8e6 f2f4 d8c8
      
      [ECO "B74"]
      [Opening "Sicilian"]
      [Variation "dragon, classical, Alekhine variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f1e2 b8c6 e1g1 e8g8 d4b3 a7a5
      
      [ECO "B75"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3
      
      [ECO "B76"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, 7...O-O"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8
      
      [ECO "B76"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, Rauser variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8 d1d2 b8c6 e1c1
      
      [ECO "B77"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, 9.Bc4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8 d1d2 b8c6 f1c4
      
      [ECO "B77"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, Byrne variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8 d1d2 b8c6 f1c4 a7a5
      
      [ECO "B77"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, 9...Bd7"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8 d1d2 b8c6 f1c4 c8d7
      
      [ECO "B78"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, 10.O-O-O"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8 d1d2 b8c6 f1c4 c8d7 e1c1
      
      [ECO "B79"]
      [Opening "Sicilian"]
      [Variation "dragon, Yugoslav attack, 12.h4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 g7g6 c1e3 f8g7 f2f3 e8g8 d1d2 b8c6 f1c4 c8d7 e1c1 d8a5 c4b3 f8c8 h2h4
      
      [ECO "B80"]
      [Opening "Sicilian"]
      [Variation "Scheveningen variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6
      
      [ECO "B80"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, English variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 c1e3 a7a6 d1d2
      
      [ECO "B80"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, Vitolins variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1b5
      
      [ECO "B80"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, fianchetto variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 g2g3
      
      [ECO "B81"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, Keres attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 g2g4
      
      [ECO "B82"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, 6.f4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f2f4
      
      [ECO "B82"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, Tal variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f2f4 b8c6 c1e3 f8e7 d1f3
      
      [ECO "B83"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, 6.Be2"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2
      
      [ECO "B83"]
      [Opening "Sicilian"]
      [Variation "modern Scheveningen"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 b8c6
      
      [ECO "B83"]
      [Opening "Sicilian"]
      [Variation "modern Scheveningen, main line"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 b8c6 e1g1 f8e7 c1e3 e8g8 f2f4
      
      [ECO "B83"]
      [Opening "Sicilian"]
      [Variation "modern Scheveningen, main line with Nb3"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 b8c6 e1g1 f8e7 c1e3 e8g8 f2f4 c8d7 d4b3
      
      [ECO "B84"]
      [Opening "Sicilian"]
      [Variation "Scheveningen (Paulsen), classical variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6
      
      [ECO "B84"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, classical, Nd7 system"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6 e1g1 b8d7
      
      [ECO "B84"]
      [Opening "Sicilian"]
      [Variation "Scheveningen (Paulsen), classical variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6 e1g1 d8c7
      
      [ECO "B85"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, classical variation with ...Qc7 and ...Nc6"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6 e1g1 d8c7 f2f4 b8c6
      
      [ECO "B85"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, classical, Maroczy system"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6 e1g1 d8c7 f2f4 b8c6 g1h1 f8e7 a2a4
      
      [ECO "B85"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, classical"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6 e1g1 d8c7 f2f4 b8c6 c1e3
      
      [ECO "B85"]
      [Opening "Sicilian"]
      [Variation "Scheveningen, classical main line"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1e2 a7a6 e1g1 d8c7 f2f4 b8c6 c1e3 f8e7 d1e1 e8g8
      
      [ECO "B86"]
      [Opening "Sicilian"]
      [Variation "Sozin attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1c4
      
      [ECO "B87"]
      [Opening "Sicilian"]
      [Variation "Sozin with ...a6 and ...b5"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1c4 a7a6 c4b3 b7b5
      
      [ECO "B88"]
      [Opening "Sicilian"]
      [Variation "Sozin, Leonhardt variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1c4 b8c6
      
      [ECO "B88"]
      [Opening "Sicilian"]
      [Variation "Sozin, Fischer variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1c4 b8c6 c4b3 f8e7 c1e3 e8g8 f2f4
      
      [ECO "B89"]
      [Opening "Sicilian"]
      [Variation "Sozin, 7.Be3"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1c4 b8c6 c1e3
      
      [ECO "B89"]
      [Opening "Sicilian"]
      [Variation "Velimirovic attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 e7e6 f1c4 b8c6 c1e3 f8e7 d1e2
      
      [ECO "B90"]
      [Opening "Sicilian"]
      [Variation "Najdorf"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6
      
      [ECO "B90"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Adams attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 h2h3
      
      [ECO "B90"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Lipnitzky attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 f1c4
      
      [ECO "B90"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Byrne (English) attack"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1e3
      
      [ECO "B91"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Zagreb (fianchetto) variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 g2g3
      
      [ECO "B92"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Opovcensky variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 f1e2
      
      [ECO "B93"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 6.f4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 f2f4
      
      [ECO "B94"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 6.Bg5"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5
      
      [ECO "B94"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Ivkov variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 b8d7 f1c4 d8a5 d1d2 e7e6 e1c1 b7b5 c4b3 c8b7 h1e1 d7c5 e4e5
      
      [ECO "B95"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 6...e6"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6
      
      [ECO "B96"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 7.f4"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4
      
      [ECO "B96"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Polugayevsky variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 b7b5
      
      [ECO "B96"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Polugayevsky, Simagin variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 b7b5 e4e5 d6e5 f4e5 d8c7 d1e2
      
      [ECO "B97"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 7...Qb6"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 d8b6
      
      [ECO "B97"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Poisoned pawn variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 d8b6 d1d2 b6b2 a1b1 b2a3
      
      [ECO "B98"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 7...Be7"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 f8e7
      
      [ECO "B98"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Browne variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 f8e7 d1f3 h7h6 g5h4 d8c7
      
      [ECO "B98"]
      [Opening "Sicilian"]
      [Variation "Najdorf, Goteborg (Argentine) variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 f8e7 d1f3 h7h6 g5h4 g7g5
      
      [ECO "B98"]
      [Opening "Sicilian"]
      [Variation "Najdorf variation"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 f8e7 d1f3 d8c7
      
      [ECO "B99"]
      [Opening "Sicilian"]
      [Variation "Najdorf, 7...Be7 main line"]
      e2e4 c7c5 g1f3 d7d6 d2d4 c5d4 f3d4 g8f6 b1c3 a7a6 c1g5 e7e6 f2f4 f8e7 d1f3 d8c7 e1c1 b8d7
      
      [ECO "C00"]
      [Opening "French defence"]
      e2e4 e7e6
      
      [ECO "C00"]
      [Opening "French defence, Steiner variation"]
      e2e4 e7e6 c2c4
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Reti (Spielmann) variation"]
      e2e4 e7e6 b2b3
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Steinitz attack"]
      e2e4 e7e6 e4e5
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Labourdonnais variation"]
      e2e4 e7e6 f2f4
      
      [ECO "C00"]
      [Opening "French defence"]
      e2e4 e7e6 g1f3
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Wing gambit"]
      e2e4 e7e6 g1f3 d7d5 e4e5 c7c5 b2b4
      
      [ECO "C00"]
      [Opening "French defence"]
      e2e4 e7e6 b1c3
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Pelikan variation"]
      e2e4 e7e6 b1c3 d7d5 f2f4
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Two knights variation"]
      e2e4 e7e6 b1c3 d7d5 g1f3
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Chigorin variation"]
      e2e4 e7e6 d1e2
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "King's Indian attack"]
      e2e4 e7e6 d2d3
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Reversed Philidor formation"]
      e2e4 e7e6 d2d3 d7d5 b1d2 g8f6 g1f3 b8c6 f1e2
      
      [ECO "C00"]
      [Opening "French defence"]
      e2e4 e7e6 d2d4
      
      [ECO "C00"]
      [Opening "Lengfellner system"]
      e2e4 e7e6 d2d4 d7d6
      
      [ECO "C00"]
      [Opening "St. George defence"]
      e2e4 e7e6 d2d4 a7a6
      
      [ECO "C00"]
      [Opening "French defence"]
      e2e4 e7e6 d2d4 d7d5
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Schlechter variation"]
      e2e4 e7e6 d2d4 d7d5 f1d3
      
      [ECO "C00"]
      [Opening "French"]
      [Variation "Alapin variation"]
      e2e4 e7e6 d2d4 d7d5 c1e3
      
      [ECO "C01"]
      [Opening "French"]
      [Variation "exchange variation"]
      e2e4 e7e6 d2d4 d7d5 e4d5
      
      [ECO "C01"]
      [Opening "French"]
      [Variation "exchange, Svenonius variation"]
      e2e4 e7e6 d2d4 d7d5 e4d5 e6d5 b1c3 g8f6 c1g5
      
      [ECO "C01"]
      [Opening "French"]
      [Variation "exchange, Bogolyubov variation"]
      e2e4 e7e6 d2d4 d7d5 e4d5 e6d5 b1c3 g8f6 c1g5 b8c6
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Steinitz variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 d4c5
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Nimzovich variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 d1g4
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Nimzovich system"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 g1f3
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Wade variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3 d8b6 g1f3 c8d7
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3 b8c6
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Paulsen attack"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3 b8c6 g1f3
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Milner-Barry gambit"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3 b8c6 g1f3 d8b6 f1d3
      
      [ECO "C02"]
      [Opening "French"]
      [Variation "advance, Euwe variation"]
      e2e4 e7e6 d2d4 d7d5 e4e5 c7c5 c2c3 b8c6 g1f3 c8d7
      
      [ECO "C03"]
      [Opening "French"]
      [Variation "Tarrasch"]
      e2e4 e7e6 d2d4 d7d5 b1d2
      
      [ECO "C03"]
      [Opening "French"]
      [Variation "Tarrasch, Haberditz variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 f7f5
      
      [ECO "C03"]
      [Opening "French"]
      [Variation "Tarrasch, Guimard variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 b8c6
      
      [ECO "C04"]
      [Opening "French"]
      [Variation "Tarrasch, Guimard main line"]
      e2e4 e7e6 d2d4 d7d5 b1d2 b8c6 g1f3 g8f6
      
      [ECO "C05"]
      [Opening "French"]
      [Variation "Tarrasch, closed variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 g8f6
      
      [ECO "C05"]
      [Opening "French"]
      [Variation "Tarrasch, Botvinnik variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 g8f6 e4e5 f6d7 f1d3 c7c5 c2c3 b7b6
      
      [ECO "C05"]
      [Opening "French"]
      [Variation "Tarrasch, closed variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 g8f6 e4e5 f6d7 f1d3 c7c5 c2c3 b8c6
      
      [ECO "C06"]
      [Opening "French"]
      [Variation "Tarrasch, closed variation, main line"]
      e2e4 e7e6 d2d4 d7d5 b1d2 g8f6 e4e5 f6d7 f1d3 c7c5 c2c3 b8c6 g1e2 c5d4 c3d4
      
      [ECO "C06"]
      [Opening "French"]
      [Variation "Tarrasch, Leningrad variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 g8f6 e4e5 f6d7 f1d3 c7c5 c2c3 b8c6 g1e2 c5d4 c3d4 d7b6
      
      [ECO "C07"]
      [Opening "French"]
      [Variation "Tarrasch, open variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 c7c5
      
      [ECO "C07"]
      [Opening "French"]
      [Variation "Tarrasch, Eliskases variation"]
      e2e4 e7e6 d2d4 d7d5 b1d2 c7c5 e4d5 d8d5 g1f3 c5d4 f1c4 d5d8
      
      [ECO "C08"]
      [Opening "French"]
      [Variation "Tarrasch, open, 4.ed ed"]
      e2e4 e7e6 d2d4 d7d5 b1d2 c7c5 e4d5 e6d5
      
      [ECO "C09"]
      [Opening "French"]
      [Variation "Tarrasch, open variation, main line"]
      e2e4 e7e6 d2d4 d7d5 b1d2 c7c5 e4d5 e6d5 g1f3 b8c6
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Paulsen variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Marshall variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 c7c5
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Rubinstein variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 d5e4
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Fort Knox variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 d5e4 c3e4 c8d7 g1f3 d7c6
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Rubinstein variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 d5e4 c3e4 b8d7
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Rubinstein, Capablanca line"]
      e2e4 e7e6 d2d4 d7d5 b1c3 d5e4 c3e4 b8d7 g1f3 g8f6 e4f6 d7f6 f3e5
      
      [ECO "C10"]
      [Opening "French"]
      [Variation "Frere (Becker) variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 d5e4 c3e4 d8d5
      
      [ECO "C11"]
      [Opening "French defence"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Swiss variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 f1d3
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Henneberger variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1e3
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz, Bradford attack"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5 f6d7 f2f4 c7c5 d4c5 f8c5 d1g4
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5 f6d7 f2f4 c7c5 d4c5 b8c6
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz, Brodsky-Jones variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5 f6d7 f2f4 c7c5 d4c5 b8c6 a2a3 f8c5 d1g4 e8g8 g1f3 f7f6
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5 f6d7 f2f4 c7c5 g1f3
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz, Boleslavsky variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5 f6d7 f2f4 c7c5 g1f3 b8c6 c1e3
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Steinitz, Gledhill attack"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 e4e5 f6d7 d1g4
      
      [ECO "C11"]
      [Opening "French"]
      [Variation "Burn variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 d5e4
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Bogolyubov variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4d5 d8d5 g5f6 g7f6 d1d2 d5a5
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, advance variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Chigorin variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 e5f6
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Grigoriev variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 e5f6 h6g5 f6g7 h8g8 h2h4 g5h4 d1g4
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Bernstein variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5h4
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Janowski variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5e3
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Dr. Olland (Dutch) variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5c1
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Tartakower variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5d2 f6d7
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Lasker variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5d2 b4c3
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Duras variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5d2 b4c3 b2c3 f6e4 d1g4 e8f8 d2c1
      
      [ECO "C12"]
      [Opening "French"]
      [Variation "MacCutcheon, Lasker variation, 8...g6"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8b4 e4e5 h7h6 g5d2 b4c3 b2c3 f6e4 d1g4 g7g6
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "classical"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "classical, Anderssen variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 g5f6
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "classical, Anderssen-Richter variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 g5f6 e7f6 e4e5 f6e7 d1g4
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "classical, Vistaneckis (Nimzovich) variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6g8
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "classical, Frankfurt variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6g8 g5e3 b7b6
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "classical, Tartakower variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6e4
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "Albin-Alekhine-Chatard attack"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 h2h4
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "Albin-Alekhine-Chatard attack, Maroczy variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 h2h4 a7a6
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "Albin-Alekhine-Chatard attack, Breyer variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 h2h4 c7c5
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "Albin-Alekhine-Chatard attack, Teichmann variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 h2h4 f7f6
      
      [ECO "C13"]
      [Opening "French"]
      [Variation "Albin-Alekhine-Chatard attack, Spielmann variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 h2h4 e8g8
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical, Tarrasch variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 f1d3
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical, Rubinstein variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 d1d2
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical, Alapin variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 c3b5
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical, Pollock variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 d1g4
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical, Steinitz variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 f2f4
      
      [ECO "C14"]
      [Opening "French"]
      [Variation "classical, Stahlberg variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 g8f6 c1g5 f8e7 e4e5 f6d7 g5e7 d8e7 f2f4 e8g8 g1f3 c7c5 d1d2 b8c6 e1c1 c5c4
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer (Nimzovich) variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer, Kondratiyev variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 f1d3 c7c5 e4d5 d8d5 c1d2
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer, fingerslip variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 c1d2
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer, Alekhine (Maroczy) gambit"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 g1e2
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer, Alekhine gambit, Alatortsev variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 g1e2 d5e4 a2a3 b4e7 c3e4 g8f6 e2g3 e8g8 f1e2 b8c6
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer, Alekhine gambit"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 g1e2 d5e4 a2a3 b4c3
      
      [ECO "C15"]
      [Opening "French"]
      [Variation "Winawer, Alekhine gambit, Kan variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 g1e2 d5e4 a2a3 b4c3 e2c3 b8c6
      
      [ECO "C16"]
      [Opening "French"]
      [Variation "Winawer, advance variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5
      
      [ECO "C16"]
      [Opening "French"]
      [Variation "Winawer, Petrosian variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 d8d7
      
      [ECO "C17"]
      [Opening "French"]
      [Variation "Winawer, advance variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5
      
      [ECO "C17"]
      [Opening "French"]
      [Variation "Winawer, advance, Bogolyubov variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 c1d2
      
      [ECO "C17"]
      [Opening "French"]
      [Variation "Winawer, advance, Russian variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 d1g4
      
      [ECO "C17"]
      [Opening "French"]
      [Variation "Winawer, advance, 5.a3"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3
      
      [ECO "C17"]
      [Opening "French"]
      [Variation "Winawer, advance, Rauzer variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 c5d4 a3b4 d4c3 g1f3
      
      [ECO "C18"]
      [Opening "French"]
      [Variation "Winawer, advance variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3
      
      [ECO "C18"]
      [Opening "French"]
      [Variation "Winawer, classical variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 d8c7
      
      [ECO "C19"]
      [Opening "French"]
      [Variation "Winawer, advance, 6...Ne7"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7
      
      [ECO "C19"]
      [Opening "French"]
      [Variation "Winawer, advance, Smyslov variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7 a3a4
      
      [ECO "C19"]
      [Opening "French"]
      [Variation "Winawer, advance, positional main line"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7 g1f3
      
      [ECO "C19"]
      [Opening "French"]
      [Variation "Winawer, advance, poisoned pawn variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7 d1g4
      
      [ECO "C19"]
      [Opening "French"]
      [Variation "Winawer, advance, poisoned pawn, Euwe-Gligoric variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7 d1g4 d8c7 g4g7 h8g8 g7h7 c5d4 e1d1
      
      [ECO "C19"]
      [Opening "French"]
      [Variation "Winawer, advance, poisoned pawn, Konstantinopolsky variation"]
      e2e4 e7e6 d2d4 d7d5 b1c3 f8b4 e4e5 c7c5 a2a3 b4c3 b2c3 g8e7 d1g4 d8c7 g4g7 h8g8 g7h7 c5d4 g1e2
      
      [ECO "C20"]
      [Opening "King's pawn game"]
      e2e4 e7e5
      
      [ECO "C20"]
      [Opening "KP"]
      [Variation "Indian opening"]
      e2e4 e7e5 d2d3
      
      [ECO "C20"]
      [Opening "KP"]
      [Variation "Mengarini's opening"]
      e2e4 e7e5 a2a3
      
      [ECO "C20"]
      [Opening "KP"]
      [Variation "King's head opening"]
      e2e4 e7e5 f2f3
      
      [ECO "C20"]
      [Opening "KP"]
      [Variation "Patzer opening"]
      e2e4 e7e5 d1h5
      
      [ECO "C20"]
      [Opening "KP"]
      [Variation "Napoleon's opening"]
      e2e4 e7e5 d1f3
      
      [ECO "C20"]
      [Opening "KP"]
      [Variation "Lopez opening"]
      e2e4 e7e5 c2c3
      
      [ECO "C20"]
      [Opening "Alapin's opening"]
      e2e4 e7e5 g1e2
      
      [ECO "C21"]
      [Opening "Centre game"]
      e2e4 e7e5 d2d4 e5d4
      
      [ECO "C21"]
      [Opening "Centre game, Kieseritsky variation"]
      e2e4 e7e5 d2d4 e5d4 g1f3 c7c5 f1c4 b7b5
      
      [ECO "C21"]
      [Opening "Halasz gambit"]
      e2e4 e7e5 d2d4 e5d4 f2f4
      
      [ECO "C21"]
      [Opening "Danish gambit"]
      e2e4 e7e5 d2d4 e5d4 c2c3
      
      [ECO "C21"]
      [Opening "Danish gambit"]
      [Variation "Collijn defence"]
      e2e4 e7e5 d2d4 e5d4 c2c3 d4c3 f1c4 c3b2 c1b2 d8e7
      
      [ECO "C21"]
      [Opening "Danish gambit"]
      [Variation "Schlechter defence"]
      e2e4 e7e5 d2d4 e5d4 c2c3 d4c3 f1c4 c3b2 c1b2 d7d5
      
      [ECO "C21"]
      [Opening "Danish gambit"]
      [Variation "Soerensen defence"]
      e2e4 e7e5 d2d4 e5d4 c2c3 d7d5
      
      [ECO "C21"]
      [Opening "Centre game"]
      e2e4 e7e5 d2d4 e5d4 d1d4
      
      [ECO "C22"]
      [Opening "Centre game"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6
      
      [ECO "C22"]
      [Opening "Centre game"]
      [Variation "Paulsen attack"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6 d4e3
      
      [ECO "C22"]
      [Opening "Centre game"]
      [Variation "Charousek variation"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6 d4e3 f8b4 c2c3 b4e7
      
      [ECO "C22"]
      [Opening "Centre game"]
      [Variation "l'Hermet variation"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6 d4e3 f7f5
      
      [ECO "C22"]
      [Opening "Centre game"]
      [Variation "Berger variation"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6 d4e3 g8f6
      
      [ECO "C22"]
      [Opening "Centre game"]
      [Variation "Kupreichik variation"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6 d4e3 g8f6 b1c3 f8b4 c1d2 e8g8 e1c1 f8e8 f1c4 d7d6 g1h3
      
      [ECO "C22"]
      [Opening "Centre game"]
      [Variation "Hall variation"]
      e2e4 e7e5 d2d4 e5d4 d1d4 b8c6 d4c4
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      e2e4 e7e5 f1c4
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Philidor counter-attack"]
      e2e4 e7e5 f1c4 c7c6
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Lisitsyn variation"]
      e2e4 e7e5 f1c4 c7c6 d2d4 d7d5 e4d5 c6d5 c4b5 c8d7 b5d7 b8d7 d4e5 d7e5 g1e2
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Calabrese counter-gambit"]
      e2e4 e7e5 f1c4 f7f5
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Calabrese counter-gambit, Jaenisch variation"]
      e2e4 e7e5 f1c4 f7f5 d2d3
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Classical variation"]
      e2e4 e7e5 f1c4 f8c5
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Lopez gambit"]
      e2e4 e7e5 f1c4 f8c5 d1e2 b8c6 c2c3 g8f6 f2f4
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Philidor variation"]
      e2e4 e7e5 f1c4 f8c5 c2c3
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Pratt variation"]
      e2e4 e7e5 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 e4e5 d7d5 e5f6 d5c4 d1h5 e8g8
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Lewis counter-gambit"]
      e2e4 e7e5 f1c4 f8c5 c2c3 d7d5
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "del Rio variation"]
      e2e4 e7e5 f1c4 f8c5 c2c3 d8g5
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Lewis gambit"]
      e2e4 e7e5 f1c4 f8c5 d2d4
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Wing gambit"]
      e2e4 e7e5 f1c4 f8c5 b2b4
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "MacDonnell double gambit"]
      e2e4 e7e5 f1c4 f8c5 b2b4 c5b4 f2f4
      
      [ECO "C23"]
      [Opening "Bishop's opening"]
      [Variation "Four pawns' gambit"]
      e2e4 e7e5 f1c4 f8c5 b2b4 c5b4 f2f4 e5f4 g1f3 b4e7 d2d4 e7h4 g2g3 f4g3 e1g1 g3h2 g1h1
      
      [ECO "C24"]
      [Opening "Bishop's opening"]
      [Variation "Berlin defence"]
      e2e4 e7e5 f1c4 g8f6
      
      [ECO "C24"]
      [Opening "Bishop's opening"]
      [Variation "Greco gambit"]
      e2e4 e7e5 f1c4 g8f6 f2f4
      
      [ECO "C24"]
      [Opening "Bishop's opening"]
      [Variation "Ponziani gambit"]
      e2e4 e7e5 f1c4 g8f6 d2d4
      
      [ECO "C24"]
      [Opening "Bishop's opening"]
      [Variation "Urusov gambit"]
      e2e4 e7e5 f1c4 g8f6 d2d4 e5d4 g1f3
      
      [ECO "C24"]
      [Opening "Bishop's opening"]
      [Variation "Urusov gambit, Panov variation"]
      e2e4 e7e5 f1c4 g8f6 d2d4 e5d4 g1f3 d7d5 e4d5 f8b4 c2c3 d8e7
      
      [ECO "C25"]
      [Opening "Vienna game"]
      e2e4 e7e5 b1c3
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Zhuravlev countergambit"]
      e2e4 e7e5 b1c3 f8b4 d1g4 g8f6
      
      [ECO "C25"]
      [Opening "Vienna game, Max Lange defence"]
      e2e4 e7e5 b1c3 b8c6
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Paulsen variation"]
      e2e4 e7e5 b1c3 b8c6 g2g3
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Fyfe gambit"]
      e2e4 e7e5 b1c3 b8c6 d2d4
      
      [ECO "C25"]
      [Opening "Vienna gambit"]
      e2e4 e7e5 b1c3 b8c6 f2f4
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Steinitz gambit"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 d2d4
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Steinitz gambit, Zukertort defence"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 d2d4 d8h4 e1e2 d7d5
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Steinitz gambit, Fraser-Minckwitz variation"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 d2d4 d8h4 e1e2 b7b6
      
      [ECO "C25"]
      [Opening "Vienna gambit"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Hamppe-Allgaier gambit"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Hamppe-Allgaier gambit, Alapin variation"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 d7d6
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Hamppe-Muzio gambit"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Hamppe-Muzio, Dubois variation"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 g4f3 d1f3 c6e5 f3f4 d8f6
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Pierce gambit"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3 g7g5 d2d4
      
      [ECO "C25"]
      [Opening "Vienna"]
      [Variation "Pierce gambit, Rushmere attack"]
      e2e4 e7e5 b1c3 b8c6 f2f4 e5f4 g1f3 g7g5 d2d4 g5g4 f1c4 g4f3 e1g1 d7d5 e4d5 c8g4 d5c6
      
      [ECO "C26"]
      [Opening "Vienna"]
      [Variation "Falkbeer variation"]
      e2e4 e7e5 b1c3 g8f6
      
      [ECO "C26"]
      [Opening "Vienna"]
      [Variation "Mengarini variation"]
      e2e4 e7e5 b1c3 g8f6 a2a3
      
      [ECO "C26"]
      [Opening "Vienna"]
      [Variation "Paulsen-Mieses variation"]
      e2e4 e7e5 b1c3 g8f6 g2g3
      
      [ECO "C26"]
      [Opening "Vienna game"]
      e2e4 e7e5 b1c3 g8f6 f1c4
      
      [ECO "C27"]
      [Opening "Vienna game"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4
      
      [ECO "C27"]
      [Opening "Vienna"]
      [Variation "`Frankenstein-Dracula' variation"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4 d1h5 e4d6 c4b3 b8c6 c3b5 g7g6 h5f3 f7f5 f3d5 d8e7 b5c7 e8d8 c7a8 b7b6
      
      [ECO "C27"]
      [Opening "Vienna"]
      [Variation "Adams' gambit"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4 d1h5 e4d6 c4b3 b8c6 d2d4
      
      [ECO "C27"]
      [Opening "Vienna game"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4 d1h5 e4d6 c4b3 f8e7
      
      [ECO "C27"]
      [Opening "Vienna"]
      [Variation "Alekhine variation"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4 d1h5 e4d6 c4b3 f8e7 g1f3 b8c6 f3e5
      
      [ECO "C27"]
      [Opening "Boden-Kieseritsky gambit"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4 g1f3
      
      [ECO "C27"]
      [Opening "Boden-Kieseritsky gambit"]
      [Variation "Lichtenhein defence"]
      e2e4 e7e5 b1c3 g8f6 f1c4 f6e4 g1f3 d7d5
      
      [ECO "C28"]
      [Opening "Vienna game"]
      e2e4 e7e5 b1c3 g8f6 f1c4 b8c6
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      [Variation "Kaufmann variation"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 g1f3 c8g4 d1e2
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      [Variation "Breyer variation"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 g1f3 f8e7
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      [Variation "Paulsen attack"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 d1f3
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      [Variation "Bardeleben variation"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 d1f3 f7f5
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      [Variation "Heyde variation"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 d1f3 f7f5 d2d4
      
      [ECO "C29"]
      [Opening "Vienna gambit"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 d2d3
      
      [ECO "C29"]
      [Opening "Vienna gambit, Wurzburger trap"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 f4e5 f6e4 d2d3 d8h4 g2g3 e4g3 g1f3 h4h5 c3d5
      
      [ECO "C29"]
      [Opening "Vienna gambit, Steinitz variation"]
      e2e4 e7e5 b1c3 g8f6 f2f4 d7d5 d2d3
      
      [ECO "C30"]
      [Opening "King's gambit"]
      e2e4 e7e5 f2f4
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "Keene's defence"]
      e2e4 e7e5 f2f4 d8h4 g2g3 h4e7
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "Mafia defence"]
      e2e4 e7e5 f2f4 c7c5
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "Norwalde variation"]
      e2e4 e7e5 f2f4 d8f6
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "Norwalde variation, Buecker gambit"]
      e2e4 e7e5 f2f4 d8f6 g1f3 f6f4 b1c3 f8b4 f1c4
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical variation"]
      e2e4 e7e5 f2f4 f8c5
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, Svenonius variation"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 b1c3 g8f6 f1c4 b8c6 d2d3 c8g4 h2h3 g4f3 d1f3 e5f4
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, Hanham variation"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 b1c3 b8d7
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, 4.c3"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 c2c3
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, Marshall attack"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 c2c3 c8g4 f4e5 d6e5 d1a4
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical counter-gambit"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 c2c3 f7f5
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, Reti variation"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 c2c3 f7f5 f4e5 d6e5 d2d4 e5d4 f1c4
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, Soldatenkov variation"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 f4e5
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "classical, Heath variation"]
      e2e4 e7e5 f2f4 f8c5 g1f3 d7d6 b2b4
      
      [ECO "C30"]
      [Opening "KGD"]
      [Variation "2...Nf6"]
      e2e4 e7e5 f2f4 g8f6
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer counter-gambit"]
      e2e4 e7e5 f2f4 d7d5
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, Tartakower variation"]
      e2e4 e7e5 f2f4 d7d5 g1f3
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, Milner-Barry variation"]
      e2e4 e7e5 f2f4 d7d5 b1c3
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer counter-gambit"]
      e2e4 e7e5 f2f4 d7d5 e4d5
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Nimzovich counter-gambit"]
      e2e4 e7e5 f2f4 d7d5 e4d5 c7c6
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, 3...e4"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, Rubinstein variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 b1c3 g8f6 d1e2
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, Nimzovich variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 f1b5
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, 4.d3"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3
      
      [ECO "C31"]
      [Opening "KGD"]
      [Variation "Falkbeer, Morphy gambit"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 b1c3 f8b4 c1d2 e4e3
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, 5.de"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d3e4
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, Alapin variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d3e4 f6e4 g1f3 f8c5 d1e2 c5f2 e1d1 d8d5 f3d2
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, main line, 7...Bf5"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d3e4 f6e4 g1f3 f8c5 d1e2 c8f5
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, Tarrasch variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d3e4 f6e4 g1f3 f8c5 d1e2 c8f5 g2g4 e8g8
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, Charousek gambit"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d3e4 f6e4 d1e2
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, Charousek variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d3e4 f6e4 d1e2 d8d5 b1d2 f7f5 g2g4
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, Keres variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 b1d2
      
      [ECO "C32"]
      [Opening "KGD"]
      [Variation "Falkbeer, Reti variation"]
      e2e4 e7e5 f2f4 d7d5 e4d5 e5e4 d2d3 g8f6 d1e2
      
      [ECO "C33"]
      [Opening "King's gambit accepted"]
      e2e4 e7e5 f2f4 e5f4
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Tumbleweed gambit"]
      e2e4 e7e5 f2f4 e5f4 e1f2
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Orsini gambit"]
      e2e4 e7e5 f2f4 e5f4 b2b3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Pawn's gambit (Stamma gambit)"]
      e2e4 e7e5 f2f4 e5f4 h2h4
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Schurig gambit"]
      e2e4 e7e5 f2f4 e5f4 f1d3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Carrera (Basman) gambit"]
      e2e4 e7e5 f2f4 e5f4 d1e2
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Villemson (Steinitz) gambit"]
      e2e4 e7e5 f2f4 e5f4 d2d4
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Keres (Mason-Steinitz) gambit"]
      e2e4 e7e5 f2f4 e5f4 b1c3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Breyer gambit"]
      e2e4 e7e5 f2f4 e5f4 d1f3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Lesser bishop's (Petroff-Jaenisch-Tartakower) gambit"]
      e2e4 e7e5 f2f4 e5f4 f1e2
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit"]
      e2e4 e7e5 f2f4 e5f4 f1c4
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Chigorin's attack"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 d7d5 c4d5 g7g5 g2g3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Greco variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 f8c5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, classical defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Grimm attack"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5 b1c3 f8g7 d2d4 d7d6 e4e5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, classical defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5 b1c3 f8g7 d2d4 g8e7
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, McDonnell attack"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5 b1c3 f8g7 d2d4 g8e7 g2g3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, McDonnell attack"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5 b1c3 f8g7 g2g3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Fraser variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5 b1c3 f8g7 g2g3 f4g3 d1f3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, classical defence, Cozio attack"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 g7g5 d1f3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Boden defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 b8c6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Bryan counter-gambit"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d8h4 e1f1 b7b5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Bryan counter-gambit"]
      e2e4 e7e5 f2f4 e5f4 f1c4 b7b5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Steinitz defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 g8e7
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Maurian defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 b8c6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Ruy Lopez defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 c7c6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Lopez-Gianutio counter-gambit"]
      e2e4 e7e5 f2f4 e5f4 f1c4 f7f5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "Lopez-Gianutio counter-gambit, Hein variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 f7f5 d1e2 d8h4 e1d1 f5e4 b1c3 e8d8
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Bledow variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d7d5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Gifford variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d7d5 c4d5 d8h4 e1f1 g7g5 g2g3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Boren-Svenonius variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d7d5 c4d5 d8h4 e1f1 f8d6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Anderssen variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d7d5 c4d5 c7c6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Morphy variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 d7d5 c4d5 g8f6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Cozio (Morphy) defence"]
      e2e4 e7e5 f2f4 e5f4 f1c4 g8f6
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Bogolyubov variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 g8f6 b1c3
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Paulsen attack"]
      e2e4 e7e5 f2f4 e5f4 f1c4 g8f6 b1c3 f8b4 e4e5
      
      [ECO "C33"]
      [Opening "KGA"]
      [Variation "bishop's gambit, Jaenisch variation"]
      e2e4 e7e5 f2f4 e5f4 f1c4 g8f6 b1c3 c7c6
      
      [ECO "C34"]
      [Opening "King's knight's gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3
      
      [ECO "C34"]
      [Opening "KGA"]
      [Variation "Bonsch-Osmolovsky variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g8e7
      
      [ECO "C34"]
      [Opening "KGA"]
      [Variation "Gianutio counter-gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 f7f5
      
      [ECO "C34"]
      [Opening "KGA"]
      [Variation "Fischer defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 d7d6
      
      [ECO "C34"]
      [Opening "KGA"]
      [Variation "Becker defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 h7h6
      
      [ECO "C34"]
      [Opening "KGA"]
      [Variation "Schallop defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g8f6
      
      [ECO "C35"]
      [Opening "KGA"]
      [Variation "Cunningham defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 f8e7
      
      [ECO "C35"]
      [Opening "KGA"]
      [Variation "Cunningham, Bertin gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 f8e7 f1c4 e7h4 g2g3
      
      [ECO "C35"]
      [Opening "KGA"]
      [Variation "Cunningham, three pawns gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 f8e7 f1c4 e7h4 g2g3 f4g3 e1g1 g3h2 g1h1
      
      [ECO "C35"]
      [Opening "KGA"]
      [Variation "Cunningham, Euwe defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 f8e7 f1c4 g8f6
      
      [ECO "C36"]
      [Opening "KGA"]
      [Variation "Abbazia defence (classical defence, modern defence[!])"]
      e2e4 e7e5 f2f4 e5f4 g1f3 d7d5
      
      [ECO "C36"]
      [Opening "KGA"]
      [Variation "Abbazia defence, modern variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 d7d5 e4d5 g8f6
      
      [ECO "C36"]
      [Opening "KGA"]
      [Variation "Abbazia defence, Botvinnik variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 d7d5 e4d5 g8f6 f1b5 c7c6 d5c6 b7c6 b5c4 f6d5
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Quaade gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 b1c3
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Rosentreter gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 d2d4
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Soerensen gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 d2d4 g5g4 f3e5
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "King's knight's gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Blachly gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 b8c6
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Lolli gambit (wild Muzio gambit)"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 c4f7
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Lolli gambit, Young variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 c4f7 e8f7 e1g1 g4f3 d1f3 d8f6 d2d4 f6d4 c1e3 d4f6 b1c3
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Ghulam Kassim gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 d2d4
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "MacDonnell gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 b1c3
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Salvio gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 f3e5
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Silberschmidt gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 f3e5 d8h4 e1f1 g8h6 d2d4 f4f3
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Salvio gambit, Anderssen counter-attack"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 f3e5 d8h4 e1f1 g8h6 d2d4 d7d6
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Cochrane gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 f3e5 d8h4 e1f1 f4f3
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Herzfeld gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 f3e5 d8h4 e1f1 b8c6
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Muzio gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Muzio gambit, Paulsen variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 g4f3 d1f3 d8f6 e4e5 f6e5 d2d3 f8h6 b1c3 g8e7 c1d2 b8c6 a1e1
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "double Muzio gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 g4f3 d1f3 d8f6 e4e5 f6e5 c4f7
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Muzio gambit, From defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 g4f3 d1f3 d8e7
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Muzio gambit, Holloway defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 g4f3 d1f3 b8c6
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Muzio gambit, Kling and Horwitz counter-attack"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 d8e7
      
      [ECO "C37"]
      [Opening "KGA"]
      [Variation "Muzio gambit, Brentano defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 g5g4 e1g1 d7d5
      
      [ECO "C38"]
      [Opening "King's knight's gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 f8g7
      
      [ECO "C38"]
      [Opening "KGA"]
      [Variation "Hanstein gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 f8g7 e1g1
      
      [ECO "C38"]
      [Opening "KGA"]
      [Variation "Philidor gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 f8g7 h2h4
      
      [ECO "C38"]
      [Opening "KGA"]
      [Variation "Greco gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 f8g7 h2h4 h7h6 d2d4 d7d6 b1c3 c7c6 h4g5 h6g5 h1h8 g7h8 f3e5
      
      [ECO "C38"]
      [Opening "KGA"]
      [Variation "Philidor gambit, Schultz variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 f1c4 f8g7 h2h4 h7h6 d2d4 d7d6 d1d3
      
      [ECO "C39"]
      [Opening "King's knight's gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Horny defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 h7h6 g5f7 e8f7 d1g4 g8f6 g4f4 f8d6
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Thorold variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 h7h6 g5f7 e8f7 d2d4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Cook variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 h7h6 g5f7 e8f7 d2d4 d7d5 c1f4 d5e4 f1c4 f7g7 f4e5
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Blackburne gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 h7h6 g5f7 e8f7 b1c3
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Walker attack"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 h7h6 g5f7 e8f7 f1c4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Urusov attack"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 h7h6 g5f7 e8f7 f1c4 d7d5 c4d5 f7g7 d2d4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Allgaier, Schlechter defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3g5 g8f6
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Paulsen defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 f8g7
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, long whip (Stockwhip, classical) defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 h7h5
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, long whip defence, Jaenisch variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 h7h5 f1c4 h8h7 d2d4 f8h6 b1c3
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Brentano (Campbell) defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d7d5
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Brentano defence, Kaplanek variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d7d5 d2d4 g8f6 e4d5 d8d5 b1c3 f8b4 e1f2
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Brentano defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d7d5 d2d4 g8f6 c1f4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Brentano defence, Caro variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d7d5 d2d4 g8f6 c1f4 f6e4 b1d2
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Salvio (Rosenthal) defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d8e7
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Salvio defence, Cozio variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d8e7 d2d4 f7f5 f1c4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Polerio defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 f8e7
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Neumann defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 b8c6
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Kolisch defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 d7d6
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Berlin defence"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 g8f6
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Berlin defence, Riviere variation"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 g8f6 e5g4 d7d5
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Berlin defence, 6.Bc4"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 g8f6 f1c4
      
      [ECO "C39"]
      [Opening "KGA"]
      [Variation "Kieseritsky, Rice gambit"]
      e2e4 e7e5 f2f4 e5f4 g1f3 g7g5 h2h4 g5g4 f3e5 g8f6 f1c4 d7d5 e4d5 f8d6 e1g1
      
      [ECO "C40"]
      [Opening "King's knight opening"]
      e2e4 e7e5 g1f3
      
      [ECO "C40"]
      [Opening "Gunderam defence"]
      e2e4 e7e5 g1f3 d8e7
      
      [ECO "C40"]
      [Opening "Greco defence"]
      e2e4 e7e5 g1f3 d8f6
      
      [ECO "C40"]
      [Opening "Damiano's defence"]
      e2e4 e7e5 g1f3 f7f6
      
      [ECO "C40"]
      [Opening "QP counter-gambit (elephant gambit)"]
      e2e4 e7e5 g1f3 d7d5
      
      [ECO "C40"]
      [Opening "QP counter-gambit"]
      [Variation "Maroczy gambit"]
      e2e4 e7e5 g1f3 d7d5 e4d5 f8d6
      
      [ECO "C40"]
      [Opening "Latvian counter-gambit"]
      e2e4 e7e5 g1f3 f7f5
      
      [ECO "C40"]
      [Opening "Latvian"]
      [Variation "Nimzovich variation"]
      e2e4 e7e5 g1f3 f7f5 f3e5 d8f6 d2d4 d7d6 e5c4 f5e4 c4e3
      
      [ECO "C40"]
      [Opening "Latvian"]
      [Variation "Fraser defence"]
      e2e4 e7e5 g1f3 f7f5 f3e5 b8c6
      
      [ECO "C40"]
      [Opening "Latvian gambit, 3.Bc4"]
      e2e4 e7e5 g1f3 f7f5 f1c4
      
      [ECO "C40"]
      [Opening "Latvian"]
      [Variation "Behting variation"]
      e2e4 e7e5 g1f3 f7f5 f1c4 f5e4 f3e5 d8g5 e5f7 g5g2 h1f1 d7d5 f7h8 g8f6
      
      [ECO "C40"]
      [Opening "Latvian"]
      [Variation "Polerio variation"]
      e2e4 e7e5 g1f3 f7f5 f1c4 f5e4 f3e5 d7d5
      
      [ECO "C40"]
      [Opening "Latvian"]
      [Variation "corkscrew counter-gambit"]
      e2e4 e7e5 g1f3 f7f5 f1c4 f5e4 f3e5 g8f6
      
      [ECO "C41"]
      [Opening "Philidor's defence"]
      e2e4 e7e5 g1f3 d7d6
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 d7d6 f1c4 f8e7 c2c3
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Lopez counter-gambit"]
      e2e4 e7e5 g1f3 d7d6 f1c4 f7f5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Lopez counter-gambit, Jaenisch variation"]
      e2e4 e7e5 g1f3 d7d6 f1c4 f7f5 d2d4 e5d4 f3g5 g8h6 g5h7
      
      [ECO "C41"]
      [Opening "Philidor's defence"]
      e2e4 e7e5 g1f3 d7d6 d2d4
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Philidor counter-gambit"]
      e2e4 e7e5 g1f3 d7d6 d2d4 f7f5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Philidor counter-gambit, del Rio attack"]
      e2e4 e7e5 g1f3 d7d6 d2d4 f7f5 d4e5 f5e4 f3g5 d6d5 e5e6
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Philidor counter-gambit, Berger variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 f7f5 d4e5 f5e4 f3g5 d6d5 e5e6 f8c5 b1c3
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Philidor counter-gambit, Zukertort variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 f7f5 b1c3
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "exchange variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Boden variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4 d1d4 c8d7
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "exchange variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4 f3d4
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Paulsen attack"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4 f3d4 d6d5 e4d5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "exchange variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4 f3d4 g8f6
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Berger variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4 f3d4 g8f6 b1c3 f8e7 f1e2 e8g8 e1g1 c7c5 d4f3 b8c6 c1g5 c8e6 f1e1
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Larsen variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 e5d4 f3d4 g7g6
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich (Jaenisch) variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Improved Hanham variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 b1c3 b8d7
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich, Sozin variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 b1c3 b8d7 f1c4 f8e7 e1g1 e8g8 d1e2 c7c6 a2a4 e5d4
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich, Larobok variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 b1c3 b8d7 f1c4 f8e7 f3g5 e8g8 c4f7
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 d4e5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich, Sokolsky variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 d4e5 f6e4 b1d2
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich, Rellstab variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 d4e5 f6e4 d1d5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich, Locock variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 f3g5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Nimzovich, Klein variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 g8f6 f1c4
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham, Krause variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7 f1c4 c7c6 e1g1
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham, Steiner variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7 f1c4 c7c6 e1g1 f8e7 d4e5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham, Kmoch variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7 f1c4 c7c6 f3g5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham, Berger variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7 f1c4 c7c6 f3g5 g8h6 f2f4 f8e7 e1g1 e8g8 c2c3 d6d5
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham, Schlechter variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7 f1c4 c7c6 b1c3
      
      [ECO "C41"]
      [Opening "Philidor"]
      [Variation "Hanham, Delmar variation"]
      e2e4 e7e5 g1f3 d7d6 d2d4 b8d7 f1c4 c7c6 c2c3
      
      [ECO "C42"]
      [Opening "Petrov's defence"]
      e2e4 e7e5 g1f3 g8f6
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "French attack"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d3
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Kaufmann attack"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 c2c4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Nimzovich attack"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 b1c3
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Cozio (Lasker) attack"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d1e2
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Chigorin variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8e7 e1g1 b8c6 f1e1
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Berger variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8e7 e1g1 b8c6 f1e1 c8g4 c2c3 f7f5 b1d2
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Krause variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8e7 e1g1 b8c6 f1e1 c8g4 c2c3 f7f5 c3c4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Maroczy variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8e7 e1g1 b8c6 f1e1 c8g4 c2c3 f7f5 c3c4 e7h4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Jaenisch variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8e7 e1g1 b8c6 c2c4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Mason variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8e7 e1g1 e8g8
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Marshall variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8d6
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Tarrasch variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8d6 e1g1 e8g8 c2c4 c8g4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, Marshall trap"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 d6d5 f1d3 f8d6 e1g1 e8g8 c2c4 c8g4 c4d5 f7f5 f1e1 d6h2
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "classical attack, close variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f3 f6e4 d2d4 e4f6
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Cochrane gambit"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5f7
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Paulsen attack"]
      e2e4 e7e5 g1f3 g8f6 f3e5 d7d6 e5c4
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Damiano variation"]
      e2e4 e7e5 g1f3 g8f6 f3e5 f6e4
      
      [ECO "C42"]
      [Opening "Petrov three knights game"]
      e2e4 e7e5 g1f3 g8f6 b1c3
      
      [ECO "C42"]
      [Opening "Petrov"]
      [Variation "Italian variation"]
      e2e4 e7e5 g1f3 g8f6 f1c4
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "modern (Steinitz) attack"]
      e2e4 e7e5 g1f3 g8f6 d2d4
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "modern attack, main line"]
      e2e4 e7e5 g1f3 g8f6 d2d4 e5d4 e4e5 f6e4 d1d4
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "modern attack, Steinitz variation"]
      e2e4 e7e5 g1f3 g8f6 d2d4 e5d4 e4e5 f6e4 d1e2
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "modern attack, Bardeleben variation"]
      e2e4 e7e5 g1f3 g8f6 d2d4 e5d4 e4e5 f6e4 d1e2 e4c5 f3d4 b8c6
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "Urusov gambit"]
      e2e4 e7e5 g1f3 g8f6 d2d4 e5d4 f1c4
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "modern attack, Symmetrical variation"]
      e2e4 e7e5 g1f3 g8f6 d2d4 f6e4
      
      [ECO "C43"]
      [Opening "Petrov"]
      [Variation "modern attack, Trifunovic variation"]
      e2e4 e7e5 g1f3 g8f6 d2d4 f6e4 f1d3 d7d5 f3e5 f8d6 e1g1 e8g8 c2c4 d6e5
      
      [ECO "C44"]
      [Opening "King's pawn game"]
      e2e4 e7e5 g1f3 b8c6
      
      [ECO "C44"]
      [Opening "Irish (Chicago) gambit"]
      e2e4 e7e5 g1f3 b8c6 f3e5 c6e5 d2d4
      
      [ECO "C44"]
      [Opening "Konstantinopolsky opening"]
      e2e4 e7e5 g1f3 b8c6 g2g3
      
      [ECO "C44"]
      [Opening "Dresden opening"]
      e2e4 e7e5 g1f3 b8c6 c2c4
      
      [ECO "C44"]
      [Opening "Inverted Hungarian"]
      e2e4 e7e5 g1f3 b8c6 f1e2
      
      [ECO "C44"]
      [Opening "Inverted Hanham"]
      e2e4 e7e5 g1f3 b8c6 f1e2 g8f6 d2d3 d7d5 b1d2
      
      [ECO "C44"]
      [Opening "Tayler opening"]
      e2e4 e7e5 g1f3 b8c6 f1e2 g8f6 d2d4
      
      [ECO "C44"]
      [Opening "Ponziani opening"]
      e2e4 e7e5 g1f3 b8c6 c2c3
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Caro variation"]
      e2e4 e7e5 g1f3 b8c6 c2c3 d7d5 d1a4 c8d7
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Leonhardt variation"]
      e2e4 e7e5 g1f3 b8c6 c2c3 d7d5 d1a4 g8f6
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 c2c3 d7d5 d1a4 f7f6
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Jaenisch counter-attack"]
      e2e4 e7e5 g1f3 b8c6 c2c3 g8f6
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Fraser defence"]
      e2e4 e7e5 g1f3 b8c6 c2c3 g8f6 d2d4 f6e4 d4d5 f8c5
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Reti variation"]
      e2e4 e7e5 g1f3 b8c6 c2c3 g8e7
      
      [ECO "C44"]
      [Opening "Ponziani"]
      [Variation "Romanishin variation"]
      e2e4 e7e5 g1f3 b8c6 c2c3 f8e7
      
      [ECO "C44"]
      [Opening "Ponziani counter-gambit"]
      e2e4 e7e5 g1f3 b8c6 c2c3 f7f5
      
      [ECO "C44"]
      [Opening "Ponziani counter-gambit, Schmidt attack"]
      e2e4 e7e5 g1f3 b8c6 c2c3 f7f5 d2d4 d7d6 d4d5
      
      [ECO "C44"]
      [Opening "Ponziani counter-gambit, Cordel variation"]
      e2e4 e7e5 g1f3 b8c6 c2c3 f7f5 d2d4 d7d6 d4d5 f5e4 f3g5 c6b8 g5e4 g8f6 f1d3 f8e7
      
      [ECO "C44"]
      [Opening "Scotch opening"]
      e2e4 e7e5 g1f3 b8c6 d2d4
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Lolli variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 c6d4
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Cochrane variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 c6d4 f3e5 d4e6 f1c4 c7c6 e1g1 g8f6 e5f7
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Relfsson gambit ('MacLopez')"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1b5
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Goering gambit"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 c2c3
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Sea-cadet mate"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 c2c3 d4c3 b1c3 d7d6 f1c4 c8g4 e1g1 c6e5 f3e5 g4d1 c4f7 e8e7 c3d5
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Goering gambit"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 c2c3 d4c3 b1c3 f8b4
      
      [ECO "C44"]
      [Opening "Scotch"]
      [Variation "Goering gambit, Bardeleben variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 c2c3 d4c3 b1c3 f8b4 f1c4 g8f6
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Anderssen (Paulsen, Suhle) counter-attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8c5 e1g1 d7d6 c2c3 c8g4
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8c5 f3g5
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Cochrane-Shumov defence"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8c5 f3g5 g8h6 g5f7 h6f7 c4f7 e8f7 d1h5 g7g6 h5c5 d7d5
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Vitzhum attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8c5 f3g5 g8h6 d1h5
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8b4
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Hanneken variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8b4 c2c3 d4c3 e1g1 c3b2 c1b2 g8f6 f3g5 e8g8 e4e5 c6e5
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8b4 c2c3 d4c3 b2c3
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Cochrane variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8b4 c2c3 d4c3 b2c3 b4a5 e4e5
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Benima defence"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 f8e7
      
      [ECO "C44"]
      [Opening "Scotch gambit"]
      [Variation "Dubois-Reti defence"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f1c4 g8f6
      
      [ECO "C45"]
      [Opening "Scotch game"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Ghulam Kassim variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 c6d4 d1d4 d7d6 f1d3
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Pulling counter-attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Horwitz attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4 d4b5
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Berger variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4 d4b5 f8b4 b1d2 h4e4 f1e2 e4g2 e2f3 g2h3 b5c7 e8d8 c7a8 g8f6 a2a3
      
      [ECO "C45"]
      [Opening "Scotch game"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4 d4b5 f8b4 c1d2
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Rosenthal variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4 d4b5 f8b4 c1d2 h4e4 f1e2 e8d8 e1g1 b4d2 b1d2 e4g6
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Fraser attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4 d4f3
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 d8h4 b1c3
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Schmidt variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 g8f6
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Mieses variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 g8f6 d4c6 b7c6 e4e5
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Tartakower variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 g8f6 d4c6 b7c6 b1d2
      
      [ECO "C45"]
      [Opening "Scotch game"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Blackburne attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 c2c3 g8e7 d1d2
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Gottschall variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 c2c3 g8e7 d1d2 d7d5 d4b5 c5e3 d2e3 e8g8 b5c7 a8b8 c7d5 e7d5 e4d5 c6b4
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Paulsen attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 c2c3 g8e7 f1b5
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Paulsen, Gunsberg defence"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 c2c3 g8e7 f1b5 c6d8
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Meitner variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 c2c3 g8e7 d4c2
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Blumenfeld attack"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 c1e3 d8f6 d4b5
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Potter variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 d4b3
      
      [ECO "C45"]
      [Opening "Scotch"]
      [Variation "Romanishin variation"]
      e2e4 e7e5 g1f3 b8c6 d2d4 e5d4 f3d4 f8c5 d4b3 c5b4
      
      [ECO "C46"]
      [Opening "Three knights game"]
      e2e4 e7e5 g1f3 b8c6 b1c3
      
      [ECO "C46"]
      [Opening "Three knights"]
      [Variation "Schlechter variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 f8b4 c3d5 g8f6
      
      [ECO "C46"]
      [Opening "Three knights"]
      [Variation "Winawer defence (Gothic defence)"]
      e2e4 e7e5 g1f3 b8c6 b1c3 f7f5
      
      [ECO "C46"]
      [Opening "Three knights"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g7g6
      
      [ECO "C46"]
      [Opening "Three knights"]
      [Variation "Steinitz, Rosenthal variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g7g6 d2d4 e5d4 c3d5
      
      [ECO "C46"]
      [Opening "Four knights game"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6
      
      [ECO "C46"]
      [Opening "Four knights"]
      [Variation "Schultze-Mueller gambit"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f3e5
      
      [ECO "C46"]
      [Opening "Four knights"]
      [Variation "Italian variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1c4
      
      [ECO "C46"]
      [Opening "Four knights"]
      [Variation "Gunsberg variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 a2a3
      
      [ECO "C47"]
      [Opening "Four knights"]
      [Variation "Scotch variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 d2d4
      
      [ECO "C47"]
      [Opening "Four knights"]
      [Variation "Scotch, Krause variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 d2d4 f8b4 f3e5
      
      [ECO "C47"]
      [Opening "Four knights"]
      [Variation "Scotch, 4...exd4"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 d2d4 e5d4
      
      [ECO "C47"]
      [Opening "Four knights"]
      [Variation "Belgrade gambit"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 d2d4 e5d4 c3d5
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Spanish variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Ranken variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 a7a6 b5c6
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Spielmann variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 a7a6 b5c6 d7c6 f3e5 f6e4 c3e4 d8d4 e1g1 d4e5 f1e1 c8e6 d2d4 e5d5
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Spanish, classical defence"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8c5
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Bardeleben variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8c5 e1g1 e8g8 f3e5 c6e5 d2d4 c5d6 f2f4 e5c6 e4e5 d6b4
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Marshall variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8c5 e1g1 e8g8 f3e5 c6d4
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Rubinstein counter-gambit"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 c6d4
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Rubinstein counter-gambit, Bogolyubov variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 c6d4 f3e5 d8e7 f2f4
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Rubinstein counter-gambit, 5.Be2"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 c6d4 b5e2
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Rubinstein counter-gambit Maroczy variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 c6d4 b5e2 d4f3 e2f3 f8c5 e1g1 e8g8 d2d3 d7d6 c3a4 c5b6
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Rubinstein counter-gambit, exchange variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 c6d4 f3d4
      
      [ECO "C48"]
      [Opening "Four knights"]
      [Variation "Rubinstein counter-gambit, Henneberger variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 c6d4 e1g1
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "double Ruy Lopez"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "Gunsberg counter-attack"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 c3d5 f6d5 e4d5 e5e4
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "double Ruy Lopez"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "Alatortsev variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d8e7 c3e2 d7d5
      
      [ECO "C49"]
      [Opening "Four knights"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 b4c3
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "Janowski variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 b4c3 b2c3 d7d6 f1e1
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "Svenonius variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 b4c3 b2c3 d7d5
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical, Metger unpin"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c1g5 b4c3 b2c3 d8e7
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical, Capablanca variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c1g5 b4c3 b2c3 d8e7 f1e1 c6d8 d3d4 c8g4
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical, Pillsbury variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c1g5 c6e7
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical, Blake variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c1g5 c6e7 f3h4 c7c6 b5c4 d6d5 c4b3 d8d6
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical, Tarrasch variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c1g5 c8e6
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "symmetrical, Maroczy system"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 d2d3 d7d6 c3e2
      
      [ECO "C49"]
      [Opening "Four knights"]
      [Variation "Nimzovich (Paulsen) variation"]
      e2e4 e7e5 g1f3 b8c6 b1c3 g8f6 f1b5 f8b4 e1g1 e8g8 b5c6
      
      [ECO "C50"]
      [Opening "King's pawn game"]
      e2e4 e7e5 g1f3 b8c6 f1c4
      
      [ECO "C50"]
      [Opening "Blackburne shilling gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 c6d4 f3e5 d8g5 e5f7 g5g2 h1f1 g2e4 c4e2 d4f3
      
      [ECO "C50"]
      [Opening "Rousseau gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f7f5
      
      [ECO "C50"]
      [Opening "Hungarian defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8e7
      
      [ECO "C50"]
      [Opening "Hungarian defence"]
      [Variation "Tartakower variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8e7 d2d4 e5d4 c2c3 g8f6 e4e5 f6e4
      
      [ECO "C50"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5
      
      [ECO "C50"]
      [Opening "Giuoco Piano"]
      [Variation "four knights variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b1c3 g8f6
      
      [ECO "C50"]
      [Opening "Giuoco Piano"]
      [Variation "Jerome gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c4f7
      
      [ECO "C50"]
      [Opening "Giuoco Pianissimo"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 d2d3
      
      [ECO "C50"]
      [Opening "Giuoco Pianissimo"]
      [Variation "Dubois variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 d2d3 f7f5 f3g5 f5f4
      
      [ECO "C50"]
      [Opening "Giuoco Pianissimo"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 d2d3 g8f6
      
      [ECO "C50"]
      [Opening "Giuoco Pianissimo"]
      [Variation "Italian four knights variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 d2d3 g8f6 b1c3
      
      [ECO "C50"]
      [Opening "Giuoco Pianissimo"]
      [Variation "Canal variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 d2d3 g8f6 b1c3 d7d6 c1g5
      
      [ECO "C51"]
      [Opening "Evans gambit declined"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Lange variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 b4b5 c6a5 f3e5 g8h6
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Pavlov variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 b4b5 c6a5 f3e5 g8h6 d2d4 d7d6 c1h6 d6e5 h6g7 h8g8 c4f7 e8f7 g7e5 d8g5 b1d2
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Hirschbach variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 b4b5 c6a5 f3e5 d8g5
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Vasquez variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 b4b5 c6a5 f3e5 d8g5 c4f7 e8e7 d1h5
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Hicken variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 b4b5 c6a5 f3e5 d8g5 d1f3 g5e5 f3f7 e8d8 c1b2
      
      [ECO "C51"]
      [Opening "Evans gambit declined, 5.a4"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 a2a4
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Showalter variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 a2a4 a7a6 b1c3
      
      [ECO "C51"]
      [Opening "Evans gambit declined, Cordel variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b6 c1b2
      
      [ECO "C51"]
      [Opening "Evans counter-gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 d7d5
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "normal variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Ulvestad variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 d4d5 c6a5 c1b2
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Paulsen variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 d4d5 c6a5 c1b2 g8e7
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Morphy attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 b1c3
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Goering attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 b1c3 c6a5 c1g5
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 b1c3 c6a5 c1g5 f7f6 g5e3
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 b1c3 c8g4
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Fraser attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 b1c3 c8g4 d1a4
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Fraser-Mortimer attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4c5 d2d4 e5d4 e1g1 d7d6 c3d4 c5b6 b1c3 c8g4 d1a4 g4d7 a4b3 c6a5 c4f7 e8f8 b3c2
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Stone-Ware variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4d6
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Mayet defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4f8
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "5...Be7"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4e7
      
      [ECO "C51"]
      [Opening "Evans gambit"]
      [Variation "Cordel variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4e7 d2d4 c6a5
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "compromised defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 e5d4 e1g1 d4c3
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "compromised defence, Paulsen variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 e5d4 e1g1 d4c3 d1b3 d8f6 e4e5 f6g6 b1c3 g8e7 c1a3
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "compromised defence, Potter variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 e5d4 e1g1 d4c3 d1b3 d8f6 e4e5 f6g6 b1c3 g8e7 f1d1
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Leonhardt variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 b7b5
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 d7d6
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Tartakower attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 d7d6 d1b3
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Levenfish variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 d7d6 d1b3 d8d7 d4e5 d6e5 e1g1 a5b6 c1a3 c6a5 f3e5
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Sokolsky variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 d2d4 d7d6 c1g5
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Richardson attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1 g8f6 d2d4 e8g8 f3e5
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1 d7d6
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Waller attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1 d7d6 d2d4 e5d4 d1b3
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Lasker defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1 d7d6 d2d4 a5b6
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Sanders-Alapin variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1 d7d6 d2d4 c8d7
      
      [ECO "C52"]
      [Opening "Evans gambit"]
      [Variation "Alapin-Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 b2b4 c5b4 c2c3 b4a5 e1g1 d7d6 d2d4 c8g4
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "LaBourdonnais variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 d7d6 d2d4 e5d4 c3d4 c5b6
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "close variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 d8e7
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "centre-holding variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 d8e7 d2d4 c5b6
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "Tarrasch variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 d8e7 d2d4 c5b6 e1g1 g8f6 a2a4 a7a6 f1e1 d7d6 h2h3
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "Mestel variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 d8e7 d2d4 c5b6 c1g5
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "Eisinger variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 d8e7 d2d4 c5b6 d4d5 c6b8 d5d6
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "Bird's attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 b2b4
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "Ghulam Kassim variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 e4e5 f6e4 c4d5 e4f2 e1f2 d4c3 f2g3
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 e4e5 d7d5
      
      [ECO "C53"]
      [Opening "Giuoco Piano"]
      [Variation "Anderssen variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 e4e5 d7d5 c4b5 f6e4 c3d4 c5b4
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Krause variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 c1d2 f6e4 d2b4 c6b4 c4f7 e8f7 d1b3 d7d5 f3e5 f7f6 f2f3
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Cracow variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 e1f1
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Greco's attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Greco variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 e4c3
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Bernstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 e4c3 b2c3 b4c3 d1b3 d7d5
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Aitken variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 e4c3 b2c3 b4c3 c1a3
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 b4c3
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 b4c3 b2c3 d7d5 c1a3
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Moeller (Therkatz) attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 b4c3 d4d5
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Therkatz-Herzog variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 b4c3 d4d5 c3f6 f1e1 c6e7 e1e4 d7d6 c1g5 f6g5 f3g5 e8g8 g5h7
      
      [ECO "C54"]
      [Opening "Giuoco Piano"]
      [Variation "Moeller, bayonet attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 f8c5 c2c3 g8f6 d2d4 e5d4 c3d4 c5b4 b1c3 f6e4 e1g1 b4c3 d4d5 c3f6 f1e1 c6e7 e1e4 d7d6 g2g4
      
      [ECO "C55"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6
      
      [ECO "C55"]
      [Opening "Giuoco piano"]
      [Variation "Rosentreter variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 e1g1 f8c5 d2d4 c5d4 f3d4 c6d4 c1g5 h7h6 g5h4 g7g5 f2f4
      
      [ECO "C55"]
      [Opening "Giuoco piano"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 e1g1 f8c5 d2d4 c5d4 f3d4 c6d4 c1g5 d7d6
      
      [ECO "C55"]
      [Opening "Giuoco piano"]
      [Variation "Holzhausen attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 e1g1 f8c5 d2d4 c5d4 f3d4 c6d4 c1g5 d7d6 f2f4 d8e7 f4e5 d6e5 b1c3
      
      [ECO "C55"]
      [Opening "Two knights defence (Modern bishop's opening)"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d3
      
      [ECO "C55"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4
      
      [ECO "C55"]
      [Opening "Two knights defence, Keidanz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e4e5 d7d5 c4b5 f6e4 f3d4 f8c5 d4c6 c5f2 e1f1 d8h4
      
      [ECO "C55"]
      [Opening "Two knights defence, Perreux variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 f3g5
      
      [ECO "C55"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Berger variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 d7d5 e5f6 d5c4 f1e1 c8e6 f3g5 d8d5 b1c3 d5f5 g2g4 f5g6 c3e4 c5b6 f2f4 e8c8
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Marshall variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 d7d5 e5f6 d5c4 f1e1 c8e6 f3g5 d8d5 b1c3 d5f5 c3e4
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Rubinstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 d7d5 e5f6 d5c4 f1e1 c8e6 f3g5 d8d5 b1c3 d5f5 c3e4 c5f8
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Loman defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 d7d5 e5f6 d5c4 f1e1 c8e6 f3g5 g7g6
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Schlechter variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 d7d5 e5f6 d5c4 f1e1 c8e6 f6g7
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 f6g4
      
      [ECO "C55"]
      [Opening "two knights"]
      [Variation "Max Lange attack, Krause variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f8c5 e4e5 f6g4 c2c3
      
      [ECO "C56"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f6e4
      
      [ECO "C56"]
      [Opening "two knights defence"]
      [Variation "Yurdansky attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f6e4 f1e1 d7d5 c4d5 d8d5 b1c3 d5a5 c3e4 c8e6 c1g5 h7h6 g5h4 g7g5 e4f6 e8e7 b2b4
      
      [ECO "C56"]
      [Opening "two knights defence"]
      [Variation "Canal variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 d2d4 e5d4 e1g1 f6e4 f1e1 d7d5 b1c3
      
      [ECO "C57"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Wilkes Barre (Traxler) variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 f8c5
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Ulvestad variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 b7b5
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Fritz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6d4
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Fritz, Gruber variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6d4 c2c3 b7b5 c4f1 f6d5 g5e4
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Lolli attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 f6d5 d2d4
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Pincus variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 f6d5 d2d4 f8b4
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Fegatello attack"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 f6d5 g5f7
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Fegatello attack, Leonhardt variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 f6d5 g5f7 e8f7 d1f3 f7e6 b1c3 c6b4 f3e4 c7c6 a2a3 b4a6 d2d4 a6c7
      
      [ECO "C57"]
      [Opening "two knights defence"]
      [Variation "Fegatello attack, Polerio defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 f6d5 g5f7 e8f7 d1f3 f7e6 b1c3 c6e7
      
      [ECO "C58"]
      [Opening "two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Kieseritsky variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 d2d3
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Yankovich variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 d2d3 h7h6 g5f3 e5e4 d1e2 a5c4 d3c4 f8c5 f3d2
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Maroczy variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 d2d3 h7h6 g5f3 e5e4 d1e2 a5c4 d3c4 f8e7
      
      [ECO "C58"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Bogolyubov variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 d1f3
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Paoli variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 d1f3 d8c7 b5d3
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Colman variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 d1f3 a8b8
      
      [ECO "C58"]
      [Opening "two knights defence"]
      [Variation "Blackburne variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 d1f3 c6b5
      
      [ECO "C58"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 b5e2
      
      [ECO "C59"]
      [Opening "Two knights defence"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 b5e2 h7h6
      
      [ECO "C59"]
      [Opening "two knights defence"]
      [Variation "Knorre variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 b5e2 h7h6 g5f3 e5e4 f3e5 f8d6 d2d4 d8c7 c1d2
      
      [ECO "C59"]
      [Opening "two knights defence"]
      [Variation "Goering variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 b5e2 h7h6 g5f3 e5e4 f3e5 d8c7
      
      [ECO "C59"]
      [Opening "two knights defence"]
      [Variation "Steinitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1c4 g8f6 f3g5 d7d5 e4d5 c6a5 c4b5 c7c6 d5c6 b7c6 b5e2 h7h6 g5h3
      
      [ECO "C60"]
      [Opening "Ruy Lopez (Spanish opening)"]
      e2e4 e7e5 g1f3 b8c6 f1b5
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Nuernberg variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f7f6
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Pollock defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 c6a5
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Lucena defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8e7
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Vinogradov variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 d8e7
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Brentano defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g7g5
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "fianchetto (Smyslov/Barnes) defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g7g6
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Cozio defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8e7
      
      [ECO "C60"]
      [Opening "Ruy Lopez"]
      [Variation "Cozio defence, Paulsen variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8e7 b1c3 g7g6
      
      [ECO "C61"]
      [Opening "Ruy Lopez"]
      [Variation "Bird's defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 c6d4
      
      [ECO "C61"]
      [Opening "Ruy Lopez"]
      [Variation "Bird's defence, Paulsen variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 c6d4 f3d4 e5d4 e1g1 g8e7
      
      [ECO "C62"]
      [Opening "Ruy Lopez"]
      [Variation "old Steinitz defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 d7d6
      
      [ECO "C62"]
      [Opening "Ruy Lopez"]
      [Variation "old Steinitz defence, Nimzovich attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 d7d6 d2d4 c8d7 b1c3 g8f6 b5c6
      
      [ECO "C62"]
      [Opening "Ruy Lopez"]
      [Variation "old Steinitz defence, semi-Duras variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 d7d6 d2d4 c8d7 c2c4
      
      [ECO "C63"]
      [Opening "Ruy Lopez"]
      [Variation "Schliemann defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f7f5
      
      [ECO "C63"]
      [Opening "Ruy Lopez"]
      [Variation "Schliemann defence, Berger variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f7f5 b1c3
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "classical (Cordel) defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "classical defence, Zaitsev variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 e1g1 c6d4 b2b4
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "classical defence, 4.c3"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 c2c3
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "classical defence, Benelux variation  "]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 c2c3 g8f6 e1g1 e8g8 d2d4 c5b6
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "classical defence, Charousek variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 c2c3 c5b6
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "classical defence, Boden variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 c2c3 d8e7
      
      [ECO "C64"]
      [Opening "Ruy Lopez"]
      [Variation "Cordel gambit"]
      e2e4 e7e5 g1f3 b8c6 f1b5 f8c5 c2c3 f7f5
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Nyholm attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 d2d4 e5d4 e1g1
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Mortimer variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 d2d3 c6e7
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Mortimer trap"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 d2d3 c6e7 f3e5 c7c6
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Anderssen variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 d2d3 d7d6 b5c6
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Duras variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 d2d3 d7d6 c2c4
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Kaufmann variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 d2d3 f8c5 c1e3
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, 4.O-O"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1
      
      [ECO "C65"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Beverwijk variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f8c5
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, 4.O-O, d6"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, hedgehog variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6 d2d4 c8d7 b1c3 f8e7
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Tarrasch trap"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6 d2d4 c8d7 b1c3 f8e7 f1e1 e8g8
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "closed Berlin defence, Bernstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6 d2d4 c8d7 b1c3 f8e7 c1g5
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "closed Berlin defence, Showalter variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6 d2d4 c8d7 b1c3 f8e7 b5c6
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "closed Berlin defence, Wolf variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6 d2d4 c8d7 b1c3 e5d4
      
      [ECO "C66"]
      [Opening "Ruy Lopez"]
      [Variation "closed Berlin defence, Chigorin variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 d7d6 d2d4 f6d7
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, open variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "open Berlin defence, l'Hermet variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 e4d6 d4e5
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "open Berlin defence, Showalter variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 e4d6 b5a4
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "open Berlin defence, 5...Be7"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Rio de Janeiro variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 e4d6 b5c6 b7c6 d4e5 d6b7 b1c3 e8g8 f1e1 b7c5 f3d4 c5e6 c1e3 e6d4 e3d4 c6c5
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Zukertort variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 e4d6 b5c6 b7c6 d4e5 d6b7 c2c4
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Pillsbury variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 e4d6 b5c6 b7c6 d4e5 d6b7 b2b3
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Winawer attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 e4d6 b5c6 b7c6 d4e5 d6b7 f3d4
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Cordel variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 e4d6 b5c6 b7c6 d4e5 d6f5
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Trifunovic variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d1e2 d7d5
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Minckwitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 f8e7 d4e5
      
      [ECO "C67"]
      [Opening "Ruy Lopez"]
      [Variation "Berlin defence, Rosenthal variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 g8f6 e1g1 f6e4 d2d4 a7a6
      
      [ECO "C68"]
      [Opening "Ruy Lopez"]
      [Variation "exchange variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6
      
      [ECO "C68"]
      [Opening "Ruy Lopez"]
      [Variation "exchange, Alekhine variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 d2d4 e5d4 d1d4 d8d4 f3d4 c8d7
      
      [ECO "C68"]
      [Opening "Ruy Lopez"]
      [Variation "exchange, Keres variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 b1c3
      
      [ECO "C68"]
      [Opening "Ruy Lopez"]
      [Variation "exchange, Romanovsky variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 b1c3 f7f6 d2d3
      
      [ECO "C69"]
      [Opening "Ruy Lopez"]
      [Variation "exchange variation, 5.O-O"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 e1g1
      
      [ECO "C69"]
      [Opening "Ruy Lopez"]
      [Variation "exchange variation, Alapin gambit"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 e1g1 c8g4 h2h3 h7h5
      
      [ECO "C69"]
      [Opening "Ruy Lopez"]
      [Variation "exchange, Gligoric variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 e1g1 f7f6
      
      [ECO "C69"]
      [Opening "Ruy Lopez"]
      [Variation "exchange, Bronstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5c6 d7c6 e1g1 d8d6
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "fianchetto defence deferred"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g7g6
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Cozio defence deferred"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8e7
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Bird's defence deferred"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 c6d4
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Alapin's defence deferred"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 f8b4
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Classical defence deferred"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 f8c5
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Caro variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 b7b5
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Graz variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 b7b5 a4b3 f8c5
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Taimanov (chase/wing/accelerated counterthrust) variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 b7b5 a4b3 c6a5
      
      [ECO "C70"]
      [Opening "Ruy Lopez"]
      [Variation "Schliemann defence deferred"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 f7f5
      
      [ECO "C71"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6
      
      [ECO "C71"]
      [Opening "Ruy Lopez"]
      [Variation "Noah's ark trap"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 d2d4 b7b5 a4b3 c6d4 f3d4 e5d4 d1d4 c7c5
      
      [ECO "C71"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, Three knights variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 b1c3
      
      [ECO "C71"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, Duras (Keres) variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c4
      
      [ECO "C72"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, 5.O-O"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 e1g1
      
      [ECO "C73"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, Richter variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 a4c6 b7c6 d2d4
      
      [ECO "C73"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, Alapin variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 a4c6 b7c6 d2d4 f7f6
      
      [ECO "C74"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c3
      
      [ECO "C74"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, siesta variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c3 f7f5
      
      [ECO "C74"]
      [Opening "Ruy Lopez"]
      [Variation "Siesta, Kopayev variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c3 f7f5 e4f5 c8f5 e1g1
      
      [ECO "C75"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c3 c8d7
      
      [ECO "C75"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, Rubinstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c3 c8d7 d2d4 g8e7
      
      [ECO "C76"]
      [Opening "Ruy Lopez"]
      [Variation "modern Steinitz defence, fianchetto (Bronstein) variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 d7d6 c2c3 c8d7 d2d4 g7g6
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "Morphy defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "four knights (Tarrasch) variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 b1c3
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "Treybal (Bayreuth) variation (exchange var. deferred)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 a4c6
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "Wormald (Alapin) attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 d1e2
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "Wormald attack, Gruenfeld variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 d1e2 b7b5 a4b3 f8e7 d2d4 d7d6 c2c3 c8g4
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "Anderssen variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 d2d3
      
      [ECO "C77"]
      [Opening "Ruy Lopez"]
      [Variation "Morphy defence, Duras variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 d2d3 d7d6 c2c4
      
      [ECO "C78"]
      [Opening "Ruy Lopez"]
      [Variation "5.O-O"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1
      
      [ECO "C78"]
      [Opening "Ruy Lopez"]
      [Variation "Wing attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 b7b5 a4b3 f8e7 a2a4
      
      [ECO "C78"]
      [Opening "Ruy Lopez"]
      [Variation "...b5 & ...d6"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 b7b5 a4b3 d7d6
      
      [ECO "C78"]
      [Opening "Ruy Lopez"]
      [Variation "Rabinovich variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 b7b5 a4b3 d7d6 f3g5 d6d5 e4d5 c6d4 f1e1 f8c5 e1e5 e8f8
      
      [ECO "C78"]
      [Opening "Ruy Lopez"]
      [Variation "Archangelsk (counterthrust) variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 b7b5 a4b3 c8b7
      
      [ECO "C78"]
      [Opening "Ruy Lopez"]
      [Variation "Moeller defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8c5
      
      [ECO "C79"]
      [Opening "Ruy Lopez"]
      [Variation "Steinitz defence deferred (Russian defence)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 d7d6
      
      [ECO "C79"]
      [Opening "Ruy Lopez"]
      [Variation "Steinitz defence deferred, Lipnitsky variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 d7d6 a4c6 b7c6 d2d4 c8g4
      
      [ECO "C79"]
      [Opening "Ruy Lopez"]
      [Variation "Steinitz defence deferred, Rubinstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 d7d6 a4c6 b7c6 d2d4 f6e4
      
      [ECO "C79"]
      [Opening "Ruy Lopez"]
      [Variation "Steinitz defence deferred, Boleslavsky variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 d7d6 a4c6 b7c6 d2d4 f6e4 f1e1 f7f5 d4e5 d6d5 b1c3
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open (Tarrasch) defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Tartakower variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d1e2
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Knorre variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 b1c3
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, 6.d4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Riga variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 e5d4
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, 6.d4 b5"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Friess attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 f3e5
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Richter variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 d4d5
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, 7.Bb3"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Schlechter defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 a2a4 c6d4
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Berger variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 a2a4 c6d4 f3d4 e5d4 b1c3
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Harksen gambit"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 c2c4
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, 8.de"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Zukertort variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c6e7
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, 8...Be6"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Bernstein variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 b1d2
      
      [ECO "C80"]
      [Opening "Ruy Lopez"]
      [Variation "open, Bernstein variation, Karpov gambit"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 b1d2 e4c5 c2c3 d5d4 f3g5
      
      [ECO "C81"]
      [Opening "Ruy Lopez"]
      [Variation "open, Howell attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 d1e2
      
      [ECO "C81"]
      [Opening "Ruy Lopez"]
      [Variation "open, Howell attack, Ekstroem variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 d1e2 f8e7 f1d1 e8g8 c2c4 b5c4 b3c4 d8d7
      
      [ECO "C81"]
      [Opening "Ruy Lopez"]
      [Variation "open, Howell attack, Adam variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 d1e2 f8e7 c2c4
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, 9.c3"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, Berlin variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 e4c5
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, Italian variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8c5
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, St. Petersburg variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8c5 b1d2
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, Dilworth variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8c5 b1d2 e8g8 b3c2 e4f2
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, Motzko attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8c5 d1d3
      
      [ECO "C82"]
      [Opening "Ruy Lopez"]
      [Variation "open, Motzko attack, Nenarokov variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8c5 d1d3 c6e7
      
      [ECO "C83"]
      [Opening "Ruy Lopez"]
      [Variation "open, classical defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8e7
      
      [ECO "C83"]
      [Opening "Ruy Lopez"]
      [Variation "open, Malkin variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8e7 b1d2 e8g8 d1e2
      
      [ECO "C83"]
      [Opening "Ruy Lopez"]
      [Variation "open, 9...Be7, 10.Re1"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8e7 f1e1
      
      [ECO "C83"]
      [Opening "Ruy Lopez"]
      [Variation "open, Tarrasch trap"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8e7 f1e1 e8g8 f3d4 d8d7 d4e6 f7e6 e1e4
      
      [ECO "C83"]
      [Opening "Ruy Lopez"]
      [Variation "open, Breslau variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f6e4 d2d4 b7b5 a4b3 d7d5 d4e5 c8e6 c2c3 f8e7 f1e1 e8g8 f3d4 c6e5
      
      [ECO "C84"]
      [Opening "Ruy Lopez"]
      [Variation "closed defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7
      
      [ECO "C84"]
      [Opening "Ruy Lopez"]
      [Variation "closed, centre attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 d2d4
      
      [ECO "C84"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Basque gambit (North Spanish variation)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 d2d4 e5d4 e4e5 f6e4 c2c3
      
      [ECO "C85"]
      [Opening "Ruy Lopez"]
      [Variation "Exchange variation doubly deferred (DERLD)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 a4c6
      
      [ECO "C86"]
      [Opening "Ruy Lopez"]
      [Variation "Worrall attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 d1e2
      
      [ECO "C86"]
      [Opening "Ruy Lopez"]
      [Variation "Worrall attack, sharp line"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 d1e2 b7b5 a4b3 e8g8
      
      [ECO "C86"]
      [Opening "Ruy Lopez"]
      [Variation "Worrall attack, solid line"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 d1e2 b7b5 a4b3 d7d6
      
      [ECO "C87"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Averbach variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 d7d6
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Leonhardt variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 d7d6 c2c3 c6a5 b3c2 c7c5 d2d4 d8c7 h2h3 a5c6 d4d5 c6b8 b1d2 g7g5
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Balla variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 d7d6 c2c3 c6a5 b3c2 c7c5 d2d4 d8c7 a2a4
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed, 7...d6, 8.d4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 d7d6 d2d4
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "Noah's ark trap"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 d7d6 d2d4 c6d4 f3d4 e5d4 d1d4 c7c5
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "Trajkovic counter-attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 c8b7
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed, 7...O-O"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed, anti-Marshall 8.a4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 a2a4
      
      [ECO "C88"]
      [Opening "Ruy Lopez"]
      [Variation "closed, 8.c3"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall counter-attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall counter-attack, 11...c6"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5 e4d5 f6d5 f3e5 c6e5 e1e5 c7c6
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall, Kevitz variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5 e4d5 f6d5 f3e5 c6e5 e1e5 c7c6 b3d5 c6d5 d2d4 e7d6 e5e3
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall, main line, 12.d2d4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5 e4d5 f6d5 f3e5 c6e5 e1e5 c7c6 d2d4
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall, main line, 14...Qh3"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5 e4d5 f6d5 f3e5 c6e5 e1e5 c7c6 d2d4 e7d6 e5e1 d8h4 g2g3 h4h3
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall, main line, Spassky variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5 e4d5 f6d5 f3e5 c6e5 e1e5 c7c6 d2d4 e7d6 e5e1 d8h4 g2g3 h4h3 c1e3 c8g4 d1d3 a8e8 b1d2 e8e6 a2a4 h3h5
      
      [ECO "C89"]
      [Opening "Ruy Lopez"]
      [Variation "Marshall, Herman Steiner variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d5 e4d5 e5e4
      
      [ECO "C90"]
      [Opening "Ruy Lopez"]
      [Variation "closed (with ...d6)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6
      
      [ECO "C90"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Pilnik variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 d2d3
      
      [ECO "C90"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Lutikov variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 b3c2
      
      [ECO "C90"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Suetin variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 a2a3
      
      [ECO "C91"]
      [Opening "Ruy Lopez"]
      [Variation "closed, 9.d4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 d2d4
      
      [ECO "C91"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Bogolyubov variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 d2d4 c8g4
      
      [ECO "C92"]
      [Opening "Ruy Lopez"]
      [Variation "closed, 9.h3"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3
      
      [ECO "C92"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Keres (9...a5) variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 a6a5
      
      [ECO "C92"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Kholmov variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c8e6
      
      [ECO "C92"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Ragozin-Petrosian (`Keres') variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 f6d7
      
      [ECO "C92"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Flohr-Zaitsev system (Lenzerheide variation)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c8b7
      
      [ECO "C93"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Smyslov defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 h7h6
      
      [ECO "C94"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Breyer defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6b8
      
      [ECO "C95"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Breyer, 10.d4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6b8 d2d4
      
      [ECO "C95"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Breyer, Borisenko variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6b8 d2d4 b8d7
      
      [ECO "C95"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Breyer, Gligoric variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6b8 d2d4 b8d7 b1d2 c8b7 b3c2 c7c5
      
      [ECO "C95"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Breyer, Simagin variation"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6b8 d2d4 b8d7 f3h4
      
      [ECO "C96"]
      [Opening "Ruy Lopez"]
      [Variation "closed (8...Na5)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2
      
      [ECO "C96"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Rossolimo defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c6 d2d4 d8c7
      
      [ECO "C96"]
      [Opening "Ruy Lopez"]
      [Variation "closed (10...c5)"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5
      
      [ECO "C96"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Borisenko defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 a5c6
      
      [ECO "C96"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Keres (...Nd7) defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 f6d7
      
      [ECO "C97"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Chigorin defence"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 d8c7
      
      [ECO "C97"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Chigorin, Yugoslav system"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 d8c7 b1d2 c8d7 d2f1 f8e8 f1e3 g7g6
      
      [ECO "C98"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Chigorin, 12...Nc6"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 d8c7 b1d2 a5c6
      
      [ECO "C98"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Chigorin, Rauzer attack"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 d8c7 b1d2 a5c6 d4c5
      
      [ECO "C99"]
      [Opening "Ruy Lopez"]
      [Variation "closed, Chigorin, 12...c5d4"]
      e2e4 e7e5 g1f3 b8c6 f1b5 a7a6 b5a4 g8f6 e1g1 f8e7 f1e1 b7b5 a4b3 e8g8 c2c3 d7d6 h2h3 c6a5 b3c2 c7c5 d2d4 d8c7 b1d2 c5d4 c3d4
      
      [ECO "D00"]
      [Opening "Queen's pawn game"]
      d2d4 d7d5
      
      [ECO "D00"]
      [Opening "Queen's pawn, Mason variation"]
      d2d4 d7d5 c1f4
      
      [ECO "D00"]
      [Opening "Queen's pawn, Mason variation, Steinitz counter-gambit"]
      d2d4 d7d5 c1f4 c7c5
      
      [ECO "D00"]
      [Opening "Levitsky attack (Queen's bishop attack)"]
      d2d4 d7d5 c1g5
      
      [ECO "D00"]
      [Opening "Blackmar gambit"]
      d2d4 d7d5 e2e4
      
      [ECO "D00"]
      [Opening "Queen's pawn"]
      [Variation "stonewall attack"]
      d2d4 d7d5 e2e3 g8f6 f1d3
      
      [ECO "D00"]
      [Opening "Queen's pawn"]
      [Variation "Chigorin variation"]
      d2d4 d7d5 b1c3
      
      [ECO "D00"]
      [Opening "Queen's pawn"]
      [Variation "Anti-Veresov"]
      d2d4 d7d5 b1c3 c8g4
      
      [ECO "D00"]
      [Opening "Blackmar-Diemer gambit"]
      d2d4 d7d5 b1c3 g8f6 e2e4
      
      [ECO "D00"]
      [Opening "Blackmar-Diemer"]
      [Variation "Euwe defence"]
      d2d4 d7d5 b1c3 g8f6 e2e4 d5e4 f2f3 e4f3 g1f3 e7e6
      
      [ECO "D00"]
      [Opening "Blackmar-Diemer"]
      [Variation "Lemberg counter-gambit"]
      d2d4 d7d5 b1c3 g8f6 e2e4 e7e5
      
      [ECO "D01"]
      [Opening "Richter-Veresov attack"]
      d2d4 d7d5 b1c3 g8f6 c1g5
      
      [ECO "D01"]
      [Opening "Richter-Veresov attack, Veresov variation"]
      d2d4 d7d5 b1c3 g8f6 c1g5 c8f5 g5f6
      
      [ECO "D01"]
      [Opening "Richter-Veresov attack, Richter variation"]
      d2d4 d7d5 b1c3 g8f6 c1g5 c8f5 f2f3
      
      [ECO "D02"]
      [Opening "Queen's pawn game"]
      d2d4 d7d5 g1f3
      
      [ECO "D02"]
      [Opening "Queen's pawn game, Chigorin variation"]
      d2d4 d7d5 g1f3 b8c6
      
      [ECO "D02"]
      [Opening "Queen's pawn game, Krause variation"]
      d2d4 d7d5 g1f3 c7c5
      
      [ECO "D02"]
      [Opening "Queen's pawn game"]
      d2d4 d7d5 g1f3 g8f6
      
      [ECO "D02"]
      [Opening "Queen's bishop game"]
      d2d4 d7d5 g1f3 g8f6 c1f4
      
      [ECO "D03"]
      [Opening "Torre attack (Tartakower variation)"]
      d2d4 d7d5 g1f3 g8f6 c1g5
      
      [ECO "D04"]
      [Opening "Queen's pawn game"]
      d2d4 d7d5 g1f3 g8f6 e2e3
      
      [ECO "D05"]
      [Opening "Queen's pawn game"]
      d2d4 d7d5 g1f3 g8f6 e2e3 e7e6
      
      [ECO "D05"]
      [Opening "Queen's pawn game, Zukertort variation"]
      d2d4 d7d5 g1f3 g8f6 e2e3 e7e6 b1d2 c7c5 b2b3
      
      [ECO "D05"]
      [Opening "Queen's pawn game"]
      d2d4 d7d5 g1f3 g8f6 e2e3 e7e6 f1d3
      
      [ECO "D05"]
      [Opening "Queen's pawn game, Rubinstein (Colle-Zukertort) variation"]
      d2d4 d7d5 g1f3 g8f6 e2e3 e7e6 f1d3 c7c5 b2b3
      
      [ECO "D05"]
      [Opening "Colle system"]
      d2d4 d7d5 g1f3 g8f6 e2e3 e7e6 f1d3 c7c5 c2c3
      
      [ECO "D06"]
      [Opening "Queen's Gambit"]
      d2d4 d7d5 c2c4
      
      [ECO "D06"]
      [Opening "QGD"]
      [Variation "Grau (Sahovic) defence"]
      d2d4 d7d5 c2c4 c8f5
      
      [ECO "D06"]
      [Opening "QGD"]
      [Variation "Marshall defence"]
      d2d4 d7d5 c2c4 g8f6
      
      [ECO "D06"]
      [Opening "QGD"]
      [Variation "symmetrical (Austrian) defence"]
      d2d4 d7d5 c2c4 c7c5
      
      [ECO "D07"]
      [Opening "QGD"]
      [Variation "Chigorin defence"]
      d2d4 d7d5 c2c4 b8c6
      
      [ECO "D07"]
      [Opening "QGD"]
      [Variation "Chigorin defence, Janowski variation"]
      d2d4 d7d5 c2c4 b8c6 b1c3 d5c4 g1f3
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit"]
      d2d4 d7d5 c2c4 e7e5
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit, Lasker trap"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 e2e3 f8b4 c1d2 d4e3
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 g1f3
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit, Alapin variation"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 g1f3 b8c6 b1d2
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit, Krenosz variation"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 g1f3 b8c6 b1d2 c8g4 h2h3 g4f3 d2f3 f8b4 c1d2 d8e7
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit, Janowski variation"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 g1f3 b8c6 b1d2 f7f6
      
      [ECO "D08"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit, Balogh variation"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 g1f3 b8c6 b1d2 d8e7
      
      [ECO "D09"]
      [Opening "QGD"]
      [Variation "Albin counter-gambit, 5.g3"]
      d2d4 d7d5 c2c4 e7e5 d4e5 d5d4 g1f3 b8c6 g2g3
      
      [ECO "D10"]
      [Opening "QGD Slav defence"]
      d2d4 d7d5 c2c4 c7c6
      
      [ECO "D10"]
      [Opening "QGD Slav defence, Alekhine variation"]
      d2d4 d7d5 c2c4 c7c6 b1c3 d5c4 e2e4
      
      [ECO "D10"]
      [Opening "QGD Slav"]
      [Variation "Winawer counter-gambit"]
      d2d4 d7d5 c2c4 c7c6 b1c3 e7e5
      
      [ECO "D10"]
      [Opening "QGD Slav defence"]
      [Variation "exchange variation"]
      d2d4 d7d5 c2c4 c7c6 c4d5
      
      [ECO "D11"]
      [Opening "QGD Slav"]
      [Variation "3.Nf3"]
      d2d4 d7d5 c2c4 c7c6 g1f3
      
      [ECO "D11"]
      [Opening "QGD Slav"]
      [Variation "Breyer variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1d2
      
      [ECO "D11"]
      [Opening "QGD Slav"]
      [Variation "4.e3"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 e2e3
      
      [ECO "D12"]
      [Opening "QGD Slav"]
      [Variation "4.e3 Bf5"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 e2e3 c8f5
      
      [ECO "D12"]
      [Opening "QGD Slav"]
      [Variation "Landau variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 e2e3 c8f5 c4d5 c6d5 d1b3 d8c8 c1d2 e7e6 b1a3
      
      [ECO "D12"]
      [Opening "QGD Slav"]
      [Variation "exchange variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 e2e3 c8f5 c4d5 c6d5 b1c3
      
      [ECO "D12"]
      [Opening "QGD Slav"]
      [Variation "Amsterdam variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 e2e3 c8f5 c4d5 c6d5 b1c3 e7e6 f3e5 f6d7
      
      [ECO "D13"]
      [Opening "QGD Slav"]
      [Variation "exchange variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 c4d5 c6d5
      
      [ECO "D14"]
      [Opening "QGD Slav"]
      [Variation "exchange variation, 6.Bf4 Bf5"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 c4d5 c6d5 b1c3 b8c6 c1f4 c8f5
      
      [ECO "D14"]
      [Opening "QGD Slav"]
      [Variation "exchange, Trifunovic variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 c4d5 c6d5 b1c3 b8c6 c1f4 c8f5 e2e3 e7e6 d1b3 f8b4
      
      [ECO "D15"]
      [Opening "QGD Slav"]
      [Variation "4.Nc3"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3
      
      [ECO "D15"]
      [Opening "QGD Slav"]
      [Variation "Suechting variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d8b6
      
      [ECO "D15"]
      [Opening "QGD Slav"]
      [Variation "Schlechter variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 g7g6
      
      [ECO "D15"]
      [Opening "QGD Slav accepted"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4
      
      [ECO "D15"]
      [Opening "QGD Slav"]
      [Variation "5.e3 (Alekhine variation)"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 e2e3
      
      [ECO "D15"]
      [Opening "QGD Slav"]
      [Variation "Slav gambit"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 e2e4
      
      [ECO "D15"]
      [Opening "QGD Slav"]
      [Variation "Tolush-Geller gambit"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 e2e4 b7b5 e4e5
      
      [ECO "D16"]
      [Opening "QGD Slav accepted"]
      [Variation "Alapin variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4
      
      [ECO "D16"]
      [Opening "QGD Slav"]
      [Variation "Smyslov variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 b8a6 e2e4 c8g4
      
      [ECO "D16"]
      [Opening "QGD Slav"]
      [Variation "Soultanbeieff variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 e7e6
      
      [ECO "D16"]
      [Opening "QGD Slav"]
      [Variation "Steiner variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8g4
      
      [ECO "D17"]
      [Opening "QGD Slav"]
      [Variation "Czech defence"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5
      
      [ECO "D17"]
      [Opening "QGD Slav"]
      [Variation "Krause attack"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 f3e5
      
      [ECO "D17"]
      [Opening "QGD Slav"]
      [Variation "Carlsbad variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 f3e5 b8d7 e5c4 d8c7 g2g3 e7e5
      
      [ECO "D17"]
      [Opening "QGD Slav"]
      [Variation "Wiesbaden variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 f3e5 e7e6
      
      [ECO "D18"]
      [Opening "QGD Slav"]
      [Variation "Dutch variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 e2e3
      
      [ECO "D18"]
      [Opening "QGD Slav"]
      [Variation "Dutch, Lasker variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 e2e3 b8a6
      
      [ECO "D19"]
      [Opening "QGD Slav"]
      [Variation "Dutch variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 e2e3 e7e6 f1c4 f8b4 e1g1
      
      [ECO "D19"]
      [Opening "QGD Slav"]
      [Variation "Dutch variation, main line"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 e2e3 e7e6 f1c4 f8b4 e1g1 e8g8 d1e2
      
      [ECO "D19"]
      [Opening "QGD Slav"]
      [Variation "Dutch, Saemisch variation"]
      d2d4 d7d5 c2c4 c7c6 g1f3 g8f6 b1c3 d5c4 a2a4 c8f5 e2e3 e7e6 f1c4 f8b4 e1g1 e8g8 d1e2 f6e4 g2g4
      
      [ECO "D20"]
      [Opening "Queen's gambit accepted"]
      d2d4 d7d5 c2c4 d5c4
      
      [ECO "D20"]
      [Opening "QGA"]
      [Variation "3.e4"]
      d2d4 d7d5 c2c4 d5c4 e2e4
      
      [ECO "D20"]
      [Opening "QGA"]
      [Variation "Linares variation"]
      d2d4 d7d5 c2c4 d5c4 e2e4 c7c5 d4d5 g8f6 b1c3 b7b5
      
      [ECO "D20"]
      [Opening "QGA"]
      [Variation "Schwartz defence"]
      d2d4 d7d5 c2c4 d5c4 e2e4 f7f5
      
      [ECO "D21"]
      [Opening "QGA"]
      [Variation "3.Nf3"]
      d2d4 d7d5 c2c4 d5c4 g1f3
      
      [ECO "D21"]
      [Opening "QGA"]
      [Variation "Ericson variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 b7b5
      
      [ECO "D21"]
      [Opening "QGA"]
      [Variation "Alekhine defense, Borisenko-Furman variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 a7a6 e2e4
      
      [ECO "D22"]
      [Opening "QGA"]
      [Variation "Alekhine defence"]
      d2d4 d7d5 c2c4 d5c4 g1f3 a7a6
      
      [ECO "D22"]
      [Opening "QGA"]
      [Variation "Alekhine defence, Alatortsev variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 a7a6 e2e3 c8g4 f1c4 e7e6 d4d5
      
      [ECO "D22"]
      [Opening "QGA"]
      [Variation "Haberditz variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 a7a6 e2e3 b7b5
      
      [ECO "D23"]
      [Opening "Queen's gambit accepted"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6
      
      [ECO "D23"]
      [Opening "QGA"]
      [Variation "Mannheim variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 d1a4
      
      [ECO "D24"]
      [Opening "QGA, 4.Nc3"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 b1c3
      
      [ECO "D24"]
      [Opening "QGA, Bogolyubov variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 b1c3 a7a6 e2e4
      
      [ECO "D25"]
      [Opening "QGA, 4.e3"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3
      
      [ECO "D25"]
      [Opening "QGA, Smyslov variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 g7g6
      
      [ECO "D25"]
      [Opening "QGA, Janowsky-Larsen variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 c8g4
      
      [ECO "D25"]
      [Opening "QGA, Flohr variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 c8e6
      
      [ECO "D26"]
      [Opening "QGA"]
      [Variation "4...e6"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6
      
      [ECO "D26"]
      [Opening "QGA"]
      [Variation "classical variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5
      
      [ECO "D26"]
      [Opening "QGA"]
      [Variation "classical, Furman variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 d1e2 a7a6 d4c5 f8c5 e1g1 b8c6 e3e4 b7b5 e4e5
      
      [ECO "D26"]
      [Opening "QGA"]
      [Variation "classical variation, 6.O-O"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1
      
      [ECO "D26"]
      [Opening "QGA"]
      [Variation "classical, Steinitz variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 c5d4
      
      [ECO "D27"]
      [Opening "QGA"]
      [Variation "classical, 6...a6"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6
      
      [ECO "D27"]
      [Opening "QGA"]
      [Variation "classical, Rubinstein variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 a2a4
      
      [ECO "D27"]
      [Opening "QGA"]
      [Variation "classical, Geller variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 e3e4
      
      [ECO "D28"]
      [Opening "QGA"]
      [Variation "classical, 7.Qe2"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 d1e2
      
      [ECO "D28"]
      [Opening "QGA"]
      [Variation "classical, 7...b5"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 d1e2 b7b5
      
      [ECO "D28"]
      [Opening "QGA"]
      [Variation "classical, Flohr variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 d1e2 b7b5 c4b3 b8c6 f1d1 c5c4 b3c2 c6b4 b1c3 b4c2 e2c2 c8b7 d4d5 d8c7
      
      [ECO "D29"]
      [Opening "QGA"]
      [Variation "classical, 8...Bb7"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 d1e2 b7b5 c4b3 c8b7
      
      [ECO "D29"]
      [Opening "QGA"]
      [Variation "classical, Smyslov variation"]
      d2d4 d7d5 c2c4 d5c4 g1f3 g8f6 e2e3 e7e6 f1c4 c7c5 e1g1 a7a6 d1e2 b7b5 c4b3 c8b7 f1d1 b8d7 b1c3 f8d6
      
      [ECO "D30"]
      [Opening "Queen's gambit declined"]
      d2d4 d7d5 c2c4 e7e6
      
      [ECO "D30"]
      [Opening "QGD Slav"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 e2e3 c7c6 b1d2
      
      [ECO "D30"]
      [Opening "QGD"]
      [Variation "Stonewall variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 e2e3 c7c6 b1d2 f6e4 f1d3 f7f5
      
      [ECO "D30"]
      [Opening "QGD Slav"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 e2e3 c7c6 b1d2 b8d7
      
      [ECO "D30"]
      [Opening "QGD Slav"]
      [Variation "Semmering variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 e2e3 c7c6 b1d2 b8d7 f1d3 c6c5
      
      [ECO "D30"]
      [Opening "QGD"]
      [Variation "Spielmann variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 e2e3 c7c6 b1d2 g7g6
      
      [ECO "D30"]
      [Opening "QGD"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 c1g5
      
      [ECO "D30"]
      [Opening "QGD"]
      [Variation "Capablanca variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 c1g5 b8d7 e2e3 c7c6 b1d2
      
      [ECO "D30"]
      [Opening "QGD"]
      [Variation "Vienna variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 c1g5 f8b4
      
      [ECO "D30"]
      [Opening "QGD"]
      [Variation "Capablanca-Duras variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 c1g5 h7h6
      
      [ECO "D30"]
      [Opening "QGD"]
      [Variation "Hastings variation"]
      d2d4 d7d5 c2c4 e7e6 g1f3 g8f6 c1g5 h7h6 g5f6 d8f6 b1c3 c7c6 d1b3
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "3.Nc3"]
      d2d4 d7d5 c2c4 e7e6 b1c3
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "Janowski variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 a7a6
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "Alapin variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 b7b6
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "Charousek (Petrosian) variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 f8e7
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "semi-Slav"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c6
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "semi-Slav, Noteboom variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c6 g1f3 d5c4
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "semi-Slav, Koomen variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c6 g1f3 d5c4 a2a4 f8b4 e2e3 b7b5 c1d2 d8e7
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "semi-Slav, Junge variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c6 g1f3 d5c4 a2a4 f8b4 e2e3 b7b5 c1d2 d8b6
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "semi-Slav, Abrahams variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c6 g1f3 d5c4 a2a4 f8b4 e2e3 b7b5 c1d2 a7a5
      
      [ECO "D31"]
      [Opening "QGD"]
      [Variation "semi-Slav, Marshall gambit"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c6 e2e4
      
      [ECO "D32"]
      [Opening "QGD"]
      [Variation "Tarrasch defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5
      
      [ECO "D32"]
      [Opening "QGD"]
      [Variation "Tarrasch, von Hennig-Schara gambit"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 c5d4
      
      [ECO "D32"]
      [Opening "QGD"]
      [Variation "Tarrasch defence, 4.cd ed"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5
      
      [ECO "D32"]
      [Opening "QGD"]
      [Variation "Tarrasch defence, Tarrasch gambit"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 d4c5 d5d4 c3a4 b7b5
      
      [ECO "D32"]
      [Opening "QGD"]
      [Variation "Tarrasch defence, Marshall gambit"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 e2e4
      
      [ECO "D32"]
      [Opening "QGD"]
      [Variation "Tarrasch defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3
      
      [ECO "D33"]
      [Opening "QGD"]
      [Variation "Tarrasch, Schlechter-Rubinstein system"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3
      
      [ECO "D33"]
      [Opening "QGD"]
      [Variation "Tarrasch, Folkestone (Swedish) variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 c5c4
      
      [ECO "D33"]
      [Opening "QGD"]
      [Variation "Tarrasch, Schlechter-Rubinstein system, Rey Ardid variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 c5c4 e2e4
      
      [ECO "D33"]
      [Opening "QGD"]
      [Variation "Tarrasch, Prague variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6
      
      [ECO "D33"]
      [Opening "QGD"]
      [Variation "Tarrasch, Wagner variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 c8g4
      
      [ECO "D34"]
      [Opening "QGD"]
      [Variation "Tarrasch, Prague variation, 7...Be7"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7
      
      [ECO "D34"]
      [Opening "QGD"]
      [Variation "Tarrasch, Prague variation, Normal position"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8
      
      [ECO "D34"]
      [Opening "QGD"]
      [Variation "Tarrasch, Reti variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8 d4c5 e7c5 c3a4
      
      [ECO "D34"]
      [Opening "QGD"]
      [Variation "Tarrasch, Prague variation, 9.Bg5"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8 c1g5
      
      [ECO "D34"]
      [Opening "QGD"]
      [Variation "Tarrasch, Bogolyubov variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8 c1g5 c8e6 a1c1 c5c4
      
      [ECO "D34"]
      [Opening "QGD"]
      [Variation "Tarrasch, Stoltz variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 c7c5 c4d5 e6d5 g1f3 b8c6 g2g3 g8f6 f1g2 f8e7 e1g1 e8g8 c1g5 c8e6 a1c1 b7b6
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "3...Nf6"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "Harrwitz attack"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1f4
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "exchange variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c4d5
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "exchange, Saemisch variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c4d5 e6d5 g1f3 b8d7 c1f4
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "exchange, positional line"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c4d5 e6d5 c1g5
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "exchange, chameleon variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c4d5 e6d5 c1g5 f8e7 e2e3 e8g8 f1d3 b8d7 d1c2 f8e8 g1e2 d7f8 e1c1
      
      [ECO "D35"]
      [Opening "QGD"]
      [Variation "exchange, positional line, 5...c6"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c4d5 e6d5 c1g5 c7c6
      
      [ECO "D36"]
      [Opening "QGD"]
      [Variation "exchange, positional line, 6.Qc2"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c4d5 e6d5 c1g5 c7c6 d1c2
      
      [ECO "D37"]
      [Opening "QGD"]
      [Variation "4.Nf3"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3
      
      [ECO "D37"]
      [Opening "QGD"]
      [Variation "classical variation (5.Bf4)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 f8e7 c1f4
      
      [ECO "D38"]
      [Opening "QGD"]
      [Variation "Ragozin variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 f8b4
      
      [ECO "D39"]
      [Opening "QGD"]
      [Variation "Ragozin, Vienna variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 f8b4 c1g5 d5c4
      
      [ECO "D40"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5
      
      [ECO "D40"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, symmetrical variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 e2e3 b8c6 f1d3 f8d6 e1g1 e8g8
      
      [ECO "D40"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, Levenfish variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 e2e3 b8c6 f1d3 f8d6 e1g1 e8g8 d1e2 d8e7 d4c5 d6c5 e3e4
      
      [ECO "D40"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch defence, Pillsbury variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 c1g5
      
      [ECO "D41"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, 5.cd"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 c4d5
      
      [ECO "D41"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, Kmoch variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 c4d5 f6d5 e2e4 d5c3 b2c3 c5d4 c3d4 f8b4 c1d2 b4d2 d1d2 e8g8 f1b5
      
      [ECO "D41"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, San Sebastian variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 c4d5 f6d5 e2e4 d5c3 b2c3 c5d4 c3d4 f8b4 c1d2 d8a5
      
      [ECO "D41"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch with e3"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 c4d5 f6d5 e2e3
      
      [ECO "D42"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, 7.Bd3"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c5 c4d5 f6d5 e2e3 b8c6 f1d3
      
      [ECO "D43"]
      [Opening "QGD semi-Slav"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6
      
      [ECO "D43"]
      [Opening "QGD semi-Slav"]
      [Variation "Hastings variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 h7h6 g5f6 d8f6 d1b3
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "5.Bg5 dc"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "Botvinnik system (anti-Meran)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4 e2e4
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "Ekstrom variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4 e2e4 b7b5 e4e5 h7h6 g5h4 g7g5 e5f6 g5h4 f3e5
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "anti-Meran gambit"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4 e2e4 b7b5 e4e5 h7h6 g5h4 g7g5 f3g5
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "anti-Meran, Lilienthal variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4 e2e4 b7b5 e4e5 h7h6 g5h4 g7g5 f3g5 h6g5 h4g5 b8d7 g2g3
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "anti-Meran, Szabo variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4 e2e4 b7b5 e4e5 h7h6 g5h4 g7g5 f3g5 h6g5 h4g5 b8d7 d1f3
      
      [ECO "D44"]
      [Opening "QGD semi-Slav"]
      [Variation "anti-Meran, Alatortsev system"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 c1g5 d5c4 e2e4 b7b5 e4e5 h7h6 g5h4 g7g5 f3g5 f6d5
      
      [ECO "D45"]
      [Opening "QGD semi-Slav"]
      [Variation "5.e3"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3
      
      [ECO "D45"]
      [Opening "QGD semi-Slav"]
      [Variation "stonewall defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 f6e4 f1d3 f7f5
      
      [ECO "D45"]
      [Opening "QGD semi-Slav"]
      [Variation "accelerated Meran (Alekhine variation)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 a7a6
      
      [ECO "D45"]
      [Opening "QGD semi-Slav"]
      [Variation "5...Nd7"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7
      
      [ECO "D45"]
      [Opening "QGD semi-Slav"]
      [Variation "Stoltz variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 d1c2
      
      [ECO "D45"]
      [Opening "QGD semi-Slav"]
      [Variation "Rubinstein (anti-Meran) system"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f3e5
      
      [ECO "D46"]
      [Opening "QGD semi-Slav"]
      [Variation "6.Bd3"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3
      
      [ECO "D46"]
      [Opening "QGD semi-Slav"]
      [Variation "Bogolyubov variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 f8e7
      
      [ECO "D46"]
      [Opening "QGD semi-Slav"]
      [Variation "Romih variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 f8b4
      
      [ECO "D46"]
      [Opening "QGD semi-Slav"]
      [Variation "Chigorin defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 f8d6
      
      [ECO "D47"]
      [Opening "QGD semi-Slav"]
      [Variation "7.Bc4"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4
      
      [ECO "D47"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5
      
      [ECO "D47"]
      [Opening "QGD semi-Slav"]
      [Variation "neo-Meran (Lundin variation)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 b5b4
      
      [ECO "D47"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Wade variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 c8b7
      
      [ECO "D48"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, 8...a6"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6
      
      [ECO "D48"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Pirc variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 b5b4
      
      [ECO "D48"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5
      
      [ECO "D48"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Reynolds' variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 d4d5
      
      [ECO "D48"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, old main line"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5
      
      [ECO "D49"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Blumenfeld variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5 c5d4 c3b5
      
      [ECO "D49"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Rabinovich variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5 c5d4 c3b5 f6g4
      
      [ECO "D49"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Sozin variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5 c5d4 c3b5 d7e5
      
      [ECO "D49"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Stahlberg variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5 c5d4 c3b5 d7e5 f3e5 a6b5 d1f3
      
      [ECO "D49"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Sozin variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5 c5d4 c3b5 d7e5 f3e5 a6b5 e1g1
      
      [ECO "D49"]
      [Opening "QGD semi-Slav"]
      [Variation "Meran, Rellstab attack"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 g1f3 c7c6 e2e3 b8d7 f1d3 d5c4 d3c4 b7b5 c4d3 a7a6 e3e4 c6c5 e4e5 c5d4 c3b5 d7e5 f3e5 a6b5 e1g1 d8d5 d1e2 c8a6 c1g5
      
      [ECO "D50"]
      [Opening "QGD"]
      [Variation "4.Bg5"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5
      
      [ECO "D50"]
      [Opening "QGD"]
      [Variation "Been-Koomen variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 c7c5
      
      [ECO "D50"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, Krause variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 c7c5 g1f3 c5d4 f3d4 e6e5 d4b5 a7a6 d1a4
      
      [ECO "D50"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch, Primitive Pillsbury variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 c7c5 g1f3 c5d4 d1d4
      
      [ECO "D50"]
      [Opening "QGD"]
      [Variation "Semi-Tarrasch"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 c7c5 c4d5
      
      [ECO "D50"]
      [Opening "QGD"]
      [Variation "Canal (Venice) variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 c7c5 c4d5 d8b6
      
      [ECO "D51"]
      [Opening "QGD"]
      [Variation "4.Bg5 Nbd7"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7
      
      [ECO "D51"]
      [Opening "QGD"]
      [Variation "Rochlin variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 g1f3 c7c6 a1c1 d8a5 g5d2
      
      [ECO "D51"]
      [Opening "QGD"]
      [Variation "Alekhine variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 g1f3 c7c6 e2e4
      
      [ECO "D51"]
      [Opening "QGD"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3
      
      [ECO "D51"]
      [Opening "QGD"]
      [Variation "Manhattan variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 f8b4
      
      [ECO "D51"]
      [Opening "QGD"]
      [Variation "5...c6"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6
      
      [ECO "D51"]
      [Opening "QGD"]
      [Variation "Capablanca anti-Cambridge Springs variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 a2a3
      
      [ECO "D52"]
      [Opening "QGD"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence, Bogoljubow variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 f3d2 f8b4 d1c2
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence, Argentine variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 f3d2 f8b4 d1c2 e8g8 g5h4
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence, Rubinstein variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 f3d2 d5c4
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence, Capablanca variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 g5f6
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence, 7.cd"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 c4d5
      
      [ECO "D52"]
      [Opening "QGD"]
      [Variation "Cambridge Springs defence, Yugoslav variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 b8d7 e2e3 c7c6 g1f3 d8a5 c4d5 f6d5
      
      [ECO "D53"]
      [Opening "QGD"]
      [Variation "4.Bg5 Be7"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7
      
      [ECO "D53"]
      [Opening "QGD"]
      [Variation "Lasker variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 f6e4
      
      [ECO "D53"]
      [Opening "QGD"]
      [Variation "4.Bg5 Be7, 5.e3 O-O"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8
      
      [ECO "D54"]
      [Opening "QGD"]
      [Variation "Anti-neo-orthodox variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 a1c1
      
      [ECO "D55"]
      [Opening "QGD"]
      [Variation "6.Nf3"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3
      
      [ECO "D55"]
      [Opening "QGD"]
      [Variation "Pillsbury attack"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b7b6 f1d3 c8b7 c4d5 e6d5 f3e5
      
      [ECO "D55"]
      [Opening "QGD"]
      [Variation "Neo-orthodox variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6
      
      [ECO "D55"]
      [Opening "QGD"]
      [Variation "Neo-orthodox variation, 7.Bxf6"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5f6
      
      [ECO "D55"]
      [Opening "QGD"]
      [Variation "Petrosian variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5f6 e7f6 a1c1 c7c6 f1d3 b8d7 e1g1 d5c4 d3c4
      
      [ECO "D55"]
      [Opening "QGD"]
      [Variation "Neo-orthodox variation, 7.Bh4"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4
      
      [ECO "D56"]
      [Opening "QGD"]
      [Variation "Lasker defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 f6e4
      
      [ECO "D56"]
      [Opening "QGD"]
      [Variation "Lasker defence, Teichmann variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 f6e4 h4e7 d8e7 d1c2
      
      [ECO "D56"]
      [Opening "QGD"]
      [Variation "Lasker defence, Russian variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 f6e4 h4e7 d8e7 d1c2 e4f6 f1d3 d5c4 d3c4 c7c5 e1g1 b8c6 f1d1 c8d7
      
      [ECO "D57"]
      [Opening "QGD"]
      [Variation "Lasker defence, main line"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 f6e4 h4e7 d8e7 c4d5 e4c3 b2c3
      
      [ECO "D57"]
      [Opening "QGD"]
      [Variation "Lasker defence, Bernstein variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 f6e4 h4e7 d8e7 c4d5 e4c3 b2c3 e6d5 d1b3 e7d6
      
      [ECO "D58"]
      [Opening "QGD"]
      [Variation "Tartakower (Makagonov-Bondarevsky) system"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 b7b6
      
      [ECO "D59"]
      [Opening "QGD"]
      [Variation "Tartakower (Makagonov-Bondarevsky) system, 8.cd Nxd5"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 b7b6 c4d5 f6d5
      
      [ECO "D59"]
      [Opening "QGD"]
      [Variation "Tartakower variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 h7h6 g5h4 b7b6 c4d5 f6d5 h4e7 d8e7 c3d5 e6d5 a1c1 c8e6
      
      [ECO "D60"]
      [Opening "QGD"]
      [Variation "Orthodox defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7
      
      [ECO "D60"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Botvinnik variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 f1d3
      
      [ECO "D60"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rauzer variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 d1b3
      
      [ECO "D61"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rubinstein variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 d1c2
      
      [ECO "D62"]
      [Opening "QGD"]
      [Variation "Orthodox defence, 7.Qc2 c5, 8.cd (Rubinstein)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 d1c2 c7c5 c4d5
      
      [ECO "D63"]
      [Opening "QGD"]
      [Variation "Orthodox defence, 7.Rc1"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1
      
      [ECO "D63"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Pillsbury attack"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 b7b6 c4d5 e6d5 f1d3
      
      [ECO "D63"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Capablanca variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 b7b6 c4d5 e6d5 f1b5
      
      [ECO "D63"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Swiss (Henneberger) variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 a7a6
      
      [ECO "D63"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Swiss, Karlsbad variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 a7a6 c4d5
      
      [ECO "D63"]
      [Opening "QGD"]
      [Variation "Orthodox defence"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6
      
      [ECO "D64"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rubinstein attack (with Rc1)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 d1c2
      
      [ECO "D64"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rubinstein attack, Wolf variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 d1c2 f6e4
      
      [ECO "D64"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rubinstein attack, Karlsbad variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 d1c2 a7a6
      
      [ECO "D64"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rubinstein attack, Gruenfeld variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 d1c2 a7a6 a2a3
      
      [ECO "D65"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Rubinstein attack, main line"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 d1c2 a7a6 c4d5
      
      [ECO "D66"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3
      
      [ECO "D66"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line, fianchetto variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 b7b5
      
      [ECO "D67"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line, Capablanca freeing manoevre"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5
      
      [ECO "D67"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line, Janowski variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 h2h4
      
      [ECO "D67"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7
      
      [ECO "D67"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line, Alekhine variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7 c3e4
      
      [ECO "D67"]
      [Opening "QGD"]
      [Variation "Orthodox defence, Bd3 line, 11.O-O"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7 e1g1
      
      [ECO "D68"]
      [Opening "QGD"]
      [Variation "Orthodox defence, classical variation"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7 e1g1 d5c3 c1c3 e6e5
      
      [ECO "D68"]
      [Opening "QGD"]
      [Variation "Orthodox defence, classical, 13.d1b1 (Maroczy)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7 e1g1 d5c3 c1c3 e6e5 d1b1
      
      [ECO "D68"]
      [Opening "QGD"]
      [Variation "Orthodox defence, classical, 13.d1c2 (Vidmar)"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7 e1g1 d5c3 c1c3 e6e5 d1c2
      
      [ECO "D69"]
      [Opening "QGD"]
      [Variation "Orthodox defence, classical, 13.de"]
      d2d4 d7d5 c2c4 e7e6 b1c3 g8f6 c1g5 f8e7 e2e3 e8g8 g1f3 b8d7 a1c1 c7c6 f1d3 d5c4 d3c4 f6d5 g5e7 d8e7 e1g1 d5c3 c1c3 e6e5 d4e5 d7e5 f3e5 e7e5
      
      [ECO "D70"]
      [Opening "Neo-Gruenfeld defence"]
      d2d4 g8f6 c2c4 g7g6 f2f3 d7d5
      
      [ECO "D70"]
      [Opening "Neo-Gruenfeld (Kemeri) defence"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5
      
      [ECO "D71"]
      [Opening "Neo-Gruenfeld, 5.cd"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 c4d5 f6d5
      
      [ECO "D72"]
      [Opening "Neo-Gruenfeld, 5.cd, main line"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 c4d5 f6d5 e2e4 d5b6 g1e2
      
      [ECO "D73"]
      [Opening "Neo-Gruenfeld, 5.Nf3"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3
      
      [ECO "D74"]
      [Opening "Neo-Gruenfeld, 6.cd Nxd5, 7.O-O"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 c4d5 f6d5 e1g1
      
      [ECO "D75"]
      [Opening "Neo-Gruenfeld, 6.cd Nxd5, 7.O-O c5, 8.Nc3"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 c4d5 f6d5 e1g1 c7c5 b1c3
      
      [ECO "D75"]
      [Opening "Neo-Gruenfeld, 6.cd Nxd5, 7.O-O c5, 8.dc"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 c4d5 f6d5 e1g1 c7c5 d4c5
      
      [ECO "D76"]
      [Opening "Neo-Gruenfeld, 6.cd Nxd5, 7.O-O Nb6"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 c4d5 f6d5 e1g1 d5b6
      
      [ECO "D77"]
      [Opening "Neo-Gruenfeld, 6.O-O"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 e1g1
      
      [ECO "D78"]
      [Opening "Neo-Gruenfeld, 6.O-O c6"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 e1g1 c7c6
      
      [ECO "D79"]
      [Opening "Neo-Gruenfeld, 6.O-O, main line"]
      d2d4 g8f6 c2c4 g7g6 g2g3 d7d5 f1g2 f8g7 g1f3 e8g8 e1g1 c7c6 c4d5 c6d5
      
      [ECO "D80"]
      [Opening "Gruenfeld defence"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5
      
      [ECO "D80"]
      [Opening "Gruenfeld"]
      [Variation "Spike gambit"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g2g4
      
      [ECO "D80"]
      [Opening "Gruenfeld"]
      [Variation "Stockholm variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1g5
      
      [ECO "D80"]
      [Opening "Gruenfeld"]
      [Variation "Lundin variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1g5 f6e4 c3e4 d5e4 d1d2 c7c5
      
      [ECO "D81"]
      [Opening "Gruenfeld"]
      [Variation "Russian variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 d1b3
      
      [ECO "D82"]
      [Opening "Gruenfeld"]
      [Variation "4.Bf4"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1f4
      
      [ECO "D83"]
      [Opening "Gruenfeld"]
      [Variation "Gruenfeld gambit"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1f4 f8g7 e2e3 e8g8
      
      [ECO "D83"]
      [Opening "Gruenfeld"]
      [Variation "Gruenfeld gambit, Capablanca variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1f4 f8g7 e2e3 e8g8 a1c1
      
      [ECO "D83"]
      [Opening "Gruenfeld"]
      [Variation "Gruenfeld gambit, Botvinnik variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1f4 f8g7 e2e3 e8g8 a1c1 c7c5 d4c5 c8e6
      
      [ECO "D84"]
      [Opening "Gruenfeld"]
      [Variation "Gruenfeld gambit accepted"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c1f4 f8g7 e2e3 e8g8 c4d5 f6d5 c3d5 d8d5 f4c7
      
      [ECO "D85"]
      [Opening "Gruenfeld"]
      [Variation "exchange variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5
      
      [ECO "D85"]
      [Opening "Gruenfeld"]
      [Variation "modern exchange variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 g1f3
      
      [ECO "D86"]
      [Opening "Gruenfeld"]
      [Variation "exchange, classical variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4
      
      [ECO "D86"]
      [Opening "Gruenfeld"]
      [Variation "exchange, Larsen variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 d8d7 e1g1 b7b6
      
      [ECO "D86"]
      [Opening "Gruenfeld"]
      [Variation "exchange, Simagin's lesser variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 b7b6
      
      [ECO "D86"]
      [Opening "Gruenfeld"]
      [Variation "exchange, Simagin's improved variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 b8c6
      
      [ECO "D87"]
      [Opening "Gruenfeld"]
      [Variation "exchange, Spassky variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 c7c5
      
      [ECO "D87"]
      [Opening "Gruenfeld"]
      [Variation "exchange, Seville variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 c7c5 e1g1 b8c6 c1e3 c8g4 f2f3 c6a5 c4f7
      
      [ECO "D88"]
      [Opening "Gruenfeld"]
      [Variation "Spassky variation, main line, 10...cd, 11.cd"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 c7c5 e1g1 b8c6 c1e3 c5d4 c3d4
      
      [ECO "D89"]
      [Opening "Gruenfeld"]
      [Variation "Spassky variation, main line, 13.Bd3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 c7c5 e1g1 b8c6 c1e3 c5d4 c3d4 c8g4 f2f3 c6a5 c4d3 g4e6
      
      [ECO "D89"]
      [Opening "Gruenfeld"]
      [Variation "exchange, Sokolsky variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 c4d5 f6d5 e2e4 d5c3 b2c3 f8g7 f1c4 e8g8 g1e2 c7c5 e1g1 b8c6 c1e3 c5d4 c3d4 c8g4 f2f3 c6a5 c4d3 g4e6 d4d5
      
      [ECO "D90"]
      [Opening "Gruenfeld"]
      [Variation "Three knights variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3
      
      [ECO "D90"]
      [Opening "Gruenfeld"]
      [Variation "Schlechter variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 c7c6
      
      [ECO "D90"]
      [Opening "Gruenfeld"]
      [Variation "Three knights variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7
      
      [ECO "D90"]
      [Opening "Gruenfeld"]
      [Variation "Flohr variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1a4
      
      [ECO "D91"]
      [Opening "Gruenfeld"]
      [Variation "5.Bg5"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 c1g5
      
      [ECO "D92"]
      [Opening "Gruenfeld"]
      [Variation "5.Bf4"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 c1f4
      
      [ECO "D93"]
      [Opening "Gruenfeld with Bf4    e3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 c1f4 e8g8 e2e3
      
      [ECO "D94"]
      [Opening "Gruenfeld"]
      [Variation "5.e3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3
      
      [ECO "D94"]
      [Opening "Gruenfeld"]
      [Variation "Makogonov variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 b2b4
      
      [ECO "D94"]
      [Opening "Gruenfeld"]
      [Variation "Opovcensky variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 c1d2
      
      [ECO "D94"]
      [Opening "Gruenfeld with e3    Bd3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 f1d3
      
      [ECO "D94"]
      [Opening "Gruenfeld"]
      [Variation "Smyslov defence"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 f1d3 c7c6 e1g1 c8g4
      
      [ECO "D94"]
      [Opening "Gruenfeld"]
      [Variation "Flohr defence"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 f1d3 c7c6 e1g1 c8f5
      
      [ECO "D95"]
      [Opening "Gruenfeld with e3 & Qb3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 d1b3
      
      [ECO "D95"]
      [Opening "Gruenfeld"]
      [Variation "Botvinnik variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 d1b3 e7e6
      
      [ECO "D95"]
      [Opening "Gruenfeld"]
      [Variation "Pachman variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 e2e3 e8g8 d1b3 d5c4 f1c4 b8d7 f3g5
      
      [ECO "D96"]
      [Opening "Gruenfeld"]
      [Variation "Russian variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3
      
      [ECO "D97"]
      [Opening "Gruenfeld"]
      [Variation "Russian variation with e4"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4
      
      [ECO "D97"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Alekhine (Hungarian) variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 a7a6
      
      [ECO "D97"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Szabo (Boleslavsky) variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 c7c6
      
      [ECO "D97"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Levenfish variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 b7b6
      
      [ECO "D97"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Byrne (Simagin) variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 b8c6
      
      [ECO "D97"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Prins variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 b8a6
      
      [ECO "D98"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Smyslov variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 c8g4
      
      [ECO "D98"]
      [Opening "Gruenfeld"]
      [Variation "Russian, Keres variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 c8g4 c1e3 f6d7 f1e2 d7b6 c4d3 b8c6 e1c1
      
      [ECO "D99"]
      [Opening "Gruenfeld defence"]
      [Variation "Smyslov, main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 c8g4 c1e3 f6d7 c4b3
      
      [ECO "D99"]
      [Opening "Gruenfeld defence"]
      [Variation "Smyslov, Yugoslav variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 d7d5 g1f3 f8g7 d1b3 d5c4 b3c4 e8g8 e2e4 c8g4 c1e3 f6d7 c4b3 c7c5
      
      [ECO "E00"]
      [Opening "Queen's pawn game"]
      d2d4 g8f6 c2c4 e7e6
      
      [ECO "E00"]
      [Opening "Neo-Indian (Seirawan) attack"]
      d2d4 g8f6 c2c4 e7e6 c1g5
      
      [ECO "E00"]
      [Opening "Catalan opening"]
      d2d4 g8f6 c2c4 e7e6 g2g3
      
      [ECO "E01"]
      [Opening "Catalan"]
      [Variation "closed"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2
      
      [ECO "E02"]
      [Opening "Catalan"]
      [Variation "open, 5.Qa4"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 d5c4 d1a4
      
      [ECO "E03"]
      [Opening "Catalan"]
      [Variation "open, Alekhine variation"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 d5c4 d1a4 b8d7 a4c4 a7a6 c4c2
      
      [ECO "E03"]
      [Opening "Catalan"]
      [Variation "open, 5.Qa4 Nbd7, 6.Qxc4"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 d5c4 d1a4 b8d7 a4c4
      
      [ECO "E04"]
      [Opening "Catalan"]
      [Variation "open, 5.Nf3"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 d5c4 g1f3
      
      [ECO "E05"]
      [Opening "Catalan"]
      [Variation "open, classical line"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 d5c4 g1f3 f8e7
      
      [ECO "E06"]
      [Opening "Catalan"]
      [Variation "closed, 5.Nf3"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3
      
      [ECO "E07"]
      [Opening "Catalan"]
      [Variation "closed, 6...Nbd7"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7
      
      [ECO "E07"]
      [Opening "Catalan"]
      [Variation "closed, Botvinnik variation"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 b1c3 c7c6 d1d3
      
      [ECO "E08"]
      [Opening "Catalan"]
      [Variation "closed, 7.Qc2"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 d1c2
      
      [ECO "E08"]
      [Opening "Catalan"]
      [Variation "closed, Zagoryansky variation"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 d1c2 c7c6 f1d1 b7b6 a2a4
      
      [ECO "E08"]
      [Opening "Catalan"]
      [Variation "closed, Qc2 & b3"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 d1c2 c7c6 b2b3
      
      [ECO "E08"]
      [Opening "Catalan"]
      [Variation "closed, Spassky gambit"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 d1c2 c7c6 b2b3 b7b6 f1d1 c8b7 b1c3 b6b5
      
      [ECO "E09"]
      [Opening "Catalan"]
      [Variation "closed, main line"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 d1c2 c7c6 b1d2
      
      [ECO "E09"]
      [Opening "Catalan"]
      [Variation "closed, Sokolsky variation"]
      d2d4 g8f6 c2c4 e7e6 g2g3 d7d5 f1g2 f8e7 g1f3 e8g8 e1g1 b8d7 d1c2 c7c6 b1d2 b7b6 b2b3 a7a5 c1b2 c8a6
      
      [ECO "E10"]
      [Opening "Queen's pawn game"]
      d2d4 g8f6 c2c4 e7e6 g1f3
      
      [ECO "E10"]
      [Opening "Blumenfeld counter-gambit"]
      d2d4 g8f6 c2c4 e7e6 g1f3 c7c5 d4d5 b7b5
      
      [ECO "E10"]
      [Opening "Blumenfeld counter-gambit accepted"]
      d2d4 g8f6 c2c4 e7e6 g1f3 c7c5 d4d5 b7b5 d5e6 f7e6 c4b5 d7d5
      
      [ECO "E10"]
      [Opening "Blumenfeld counter-gambit, Dus-Chotimursky variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 c7c5 d4d5 b7b5 c1g5
      
      [ECO "E10"]
      [Opening "Blumenfeld counter-gambit, Spielmann variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 c7c5 d4d5 b7b5 c1g5 e6d5 c4d5 h7h6
      
      [ECO "E10"]
      [Opening "Dzindzikhashvili defence"]
      d2d4 g8f6 c2c4 e7e6 g1f3 a7a6
      
      [ECO "E10"]
      [Opening "Doery defence"]
      d2d4 g8f6 c2c4 e7e6 g1f3 f6e4
      
      [ECO "E11"]
      [Opening "Bogo-Indian defence"]
      d2d4 g8f6 c2c4 e7e6 g1f3 f8b4
      
      [ECO "E11"]
      [Opening "Bogo-Indian defence, Gruenfeld variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 f8b4 b1d2
      
      [ECO "E11"]
      [Opening "Bogo-Indian defence, Nimzovich variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 f8b4 c1d2 d8e7
      
      [ECO "E11"]
      [Opening "Bogo-Indian defence, Monticelli trap"]
      d2d4 g8f6 c2c4 e7e6 g1f3 f8b4 c1d2 b4d2 d1d2 b7b6 g2g3 c8b7 f1g2 e8g8 b1c3 f6e4 d2c2 e4c3 f3g5
      
      [ECO "E12"]
      [Opening "Queen's Indian defence"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6
      
      [ECO "E12"]
      [Opening "Queen's Indian"]
      [Variation "Miles variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 c1f4
      
      [ECO "E12"]
      [Opening "Queen's Indian"]
      [Variation "Petrosian system"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 a2a3
      
      [ECO "E12"]
      [Opening "Queen's Indian"]
      [Variation "4.Nc3"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 b1c3
      
      [ECO "E12"]
      [Opening "Queen's Indian"]
      [Variation "4.Nc3, Botvinnik variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 b1c3 c8b7 c1g5 h7h6 g5h4 g7g5 h4g3 f6h5
      
      [ECO "E13"]
      [Opening "Queen's Indian"]
      [Variation "4.Nc3, main line"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 b1c3 c8b7 c1g5 h7h6 g5h4 f8b4
      
      [ECO "E14"]
      [Opening "Queen's Indian"]
      [Variation "4.e3"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 e2e3
      
      [ECO "E14"]
      [Opening "Queen's Indian"]
      [Variation "Averbakh variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 e2e3 c8b7 f1d3 c7c5 e1g1 f8e7 b2b3 e8g8 c1b2 c5d4 f3d4
      
      [ECO "E15"]
      [Opening "Queen's Indian"]
      [Variation "4.g3"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3
      
      [ECO "E15"]
      [Opening "Queen's Indian"]
      [Variation "Nimzovich variation (exaggerated fianchetto)"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8a6
      
      [ECO "E15"]
      [Opening "Queen's Indian"]
      [Variation "4.g3 Bb7"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7
      
      [ECO "E15"]
      [Opening "Queen's Indian"]
      [Variation "Rubinstein variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 c7c5 d4d5 e6d5 f3h4
      
      [ECO "E15"]
      [Opening "Queen's Indian"]
      [Variation "Buerger variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 c7c5 d4d5 e6d5 f3g5
      
      [ECO "E16"]
      [Opening "Queen's Indian"]
      [Variation "Capablanca variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8b4
      
      [ECO "E16"]
      [Opening "Queen's Indian"]
      [Variation "Yates variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8b4 c1d2 a7a5
      
      [ECO "E16"]
      [Opening "Queen's Indian"]
      [Variation "Riumin variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8b4 c1d2 b4e7
      
      [ECO "E17"]
      [Opening "Queen's Indian"]
      [Variation "5.Bg2 Be7"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7
      
      [ECO "E17"]
      [Opening "Queen's Indian"]
      [Variation "anti-Queen's Indian system"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 b1c3
      
      [ECO "E17"]
      [Opening "Queen's Indian"]
      [Variation "Opovcensky variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 b1c3 f6e4 c1d2
      
      [ECO "E17"]
      [Opening "Queen's Indian"]
      [Variation "old main line, 6.O-O"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 e1g1
      
      [ECO "E17"]
      [Opening "Queen's Indian"]
      [Variation "Euwe variation"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 e1g1 e8g8 b2b3
      
      [ECO "E18"]
      [Opening "Queen's Indian"]
      [Variation "old main line, 7.Nc3"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 e1g1 e8g8 b1c3
      
      [ECO "E19"]
      [Opening "Queen's Indian"]
      [Variation "old main line, 9.Qxc3"]
      d2d4 g8f6 c2c4 e7e6 g1f3 b7b6 g2g3 c8b7 f1g2 f8e7 e1g1 e8g8 b1c3 f6e4 d1c2 e4c3 c2c3
      
      [ECO "E20"]
      [Opening "Nimzo-Indian defence"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4
      
      [ECO "E20"]
      [Opening "Nimzo-Indian"]
      [Variation "Kmoch variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 f2f3
      
      [ECO "E20"]
      [Opening "Nimzo-Indian"]
      [Variation "Mikenas attack"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1d3
      
      [ECO "E20"]
      [Opening "Nimzo-Indian"]
      [Variation "Romanishin-Kasparov (Steiner) system"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 g2g3
      
      [ECO "E21"]
      [Opening "Nimzo-Indian"]
      [Variation "three knights variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 g1f3
      
      [ECO "E21"]
      [Opening "Nimzo-Indian"]
      [Variation "three knights, Korchnoi variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 g1f3 c7c5 d4d5
      
      [ECO "E21"]
      [Opening "Nimzo-Indian"]
      [Variation "three knights, Euwe variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 g1f3 c7c5 d4d5 f6e4
      
      [ECO "E22"]
      [Opening "Nimzo-Indian"]
      [Variation "Spielmann variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1b3
      
      [ECO "E23"]
      [Opening "Nimzo-Indian"]
      [Variation "Spielmann, 4...c5, 5.dc Nc6"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1b3 c7c5 d4c5 b8c6
      
      [ECO "E23"]
      [Opening "Nimzo-Indian"]
      [Variation "Spielmann, Karlsbad variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1b3 c7c5 d4c5 b8c6 g1f3 f6e4 c1d2 e4d2
      
      [ECO "E23"]
      [Opening "Nimzo-Indian"]
      [Variation "Spielmann, San Remo variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1b3 c7c5 d4c5 b8c6 g1f3 f6e4 c1d2 e4c5
      
      [ECO "E23"]
      [Opening "Nimzo-Indian"]
      [Variation "Spielmann, Staahlberg variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1b3 c7c5 d4c5 b8c6 g1f3 f6e4 c1d2 e4c5 b3c2 f7f5 g2g3
      
      [ECO "E24"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3
      
      [ECO "E24"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch, Botvinnik variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 c7c5 f2f3 d7d5 e2e3 e8g8 c4d5 f6d5
      
      [ECO "E25"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 c7c5 f2f3 d7d5 c4d5
      
      [ECO "E25"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch, Keres variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 c7c5 f2f3 d7d5 c4d5 f6d5 d4c5
      
      [ECO "E25"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch, Romanovsky variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 c7c5 f2f3 d7d5 c4d5 f6d5 d4c5 f7f5
      
      [ECO "E26"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 c7c5 e2e3
      
      [ECO "E26"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch, O'Kelly variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 c7c5 e2e3 b7b6
      
      [ECO "E27"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 e8g8
      
      [ECO "E28"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 e8g8 e2e3
      
      [ECO "E29"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch, main line"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 e8g8 e2e3 c7c5 f1d3 b8c6
      
      [ECO "E29"]
      [Opening "Nimzo-Indian"]
      [Variation "Saemisch, Capablanca variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 a2a3 b4c3 b2c3 e8g8 e2e3 c7c5 f1d3 b8c6 g1e2 b7b6 e3e4 f6e8
      
      [ECO "E30"]
      [Opening "Nimzo-Indian"]
      [Variation "Leningrad variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 c1g5
      
      [ECO "E30"]
      [Opening "Nimzo-Indian"]
      [Variation "Leningrad, ...b5 gambit"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 c1g5 h7h6 g5h4 c7c5 d4d5 b7b5
      
      [ECO "E31"]
      [Opening "Nimzo-Indian"]
      [Variation "Leningrad, main line"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 c1g5 h7h6 g5h4 c7c5 d4d5 d7d6
      
      [ECO "E32"]
      [Opening "Nimzo-Indian"]
      [Variation "classical variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2
      
      [ECO "E32"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Adorjan gambit"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 e8g8 a2a3 b4c3 c2c3 b7b5
      
      [ECO "E33"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, 4...Nc6"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 b8c6
      
      [ECO "E33"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Milner-Barry (Zurich) variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 b8c6 g1f3 d7d6
      
      [ECO "E34"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Noa variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5
      
      [ECO "E35"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Noa variation, 5.cd ed"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 c4d5 e6d5
      
      [ECO "E36"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Noa variation, 5.a3"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 a2a3
      
      [ECO "E36"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Botvinnik variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 a2a3 b4c3 c2c3 b8c6
      
      [ECO "E36"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Noa variation, main line"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 a2a3 b4c3 c2c3 f6e4
      
      [ECO "E37"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Noa variation, main line, 7.Qc2"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 a2a3 b4c3 c2c3 f6e4 c3c2
      
      [ECO "E37"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, San Remo variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 d7d5 a2a3 b4c3 c2c3 f6e4 c3c2 b8c6 e2e3 e6e5
      
      [ECO "E38"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, 4...c5"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 c7c5
      
      [ECO "E39"]
      [Opening "Nimzo-Indian"]
      [Variation "classical, Pirc variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 d1c2 c7c5 d4c5 e8g8
      
      [ECO "E40"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3
      
      [ECO "E40"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Taimanov variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 b8c6
      
      [ECO "E41"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 c5"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 c7c5
      
      [ECO "E41"]
      [Opening "Nimzo-Indian"]
      [Variation "e3, Huebner variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 c7c5 f1d3 b8c6 g1f3 b4c3 b2c3 d7d6
      
      [ECO "E42"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 c5, 5.Ne2 (Rubinstein)"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 c7c5 g1e2
      
      [ECO "E43"]
      [Opening "Nimzo-Indian"]
      [Variation "Fischer variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 b7b6
      
      [ECO "E44"]
      [Opening "Nimzo-Indian"]
      [Variation "Fischer variation, 5.Ne2"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 b7b6 g1e2
      
      [ECO "E45"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Bronstein (Byrne) variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 b7b6 g1e2 c8a6
      
      [ECO "E46"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 O-O"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8
      
      [ECO "E46"]
      [Opening "Nimzo-Indian"]
      [Variation "Reshevsky variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1e2
      
      [ECO "E46"]
      [Opening "Nimzo-Indian"]
      [Variation "Simagin variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1e2 d7d5 a2a3 b4d6
      
      [ECO "E47"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 O-O, 5.Bd3"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 f1d3
      
      [ECO "E48"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 O-O, 5.Bd3 d5"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 f1d3 d7d5
      
      [ECO "E49"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Botvinnik system"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 f1d3 d7d5 a2a3 b4c3 b2c3
      
      [ECO "E50"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 e8g8, 5.Nf3, without ...d5"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3
      
      [ECO "E51"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3 e8g8, 5.Nf3 d7d5"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5
      
      [ECO "E51"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Ragozin variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 b8c6 e1g1 d5c4
      
      [ECO "E52"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, main line with ...b6"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 b7b6
      
      [ECO "E53"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, main line with ...c5"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5
      
      [ECO "E53"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Keres variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 b7b6
      
      [ECO "E53"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Gligoric system with 7...Nbd7"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 b8d7
      
      [ECO "E54"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Gligoric system with 7...dc"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 d5c4 d3c4
      
      [ECO "E54"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Gligoric system, Smyslov variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 d5c4 d3c4 d8e7
      
      [ECO "E55"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, Gligoric system, Bronstein variation"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 d5c4 d3c4 b8d7
      
      [ECO "E56"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, main line with 7...Nc6"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 b8c6
      
      [ECO "E57"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, main line with 8...dc and 9...cd"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 b8c6 a2a3 d5c4 d3c4 c5d4
      
      [ECO "E58"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, main line with 8...Bxc3"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 b8c6 a2a3 b4c3 b2c3
      
      [ECO "E59"]
      [Opening "Nimzo-Indian"]
      [Variation "4.e3, main line"]
      d2d4 g8f6 c2c4 e7e6 b1c3 f8b4 e2e3 e8g8 g1f3 d7d5 f1d3 c7c5 e1g1 b8c6 a2a3 b4c3 b2c3 d5c4 d3c4
      
      [ECO "E60"]
      [Opening "King's Indian defence"]
      d2d4 g8f6 c2c4 g7g6
      
      [ECO "E60"]
      [Opening "King's Indian, 3.Nf3"]
      d2d4 g8f6 c2c4 g7g6 g1f3
      
      [ECO "E60"]
      [Opening "Queen's pawn"]
      [Variation "Mengarini attack"]
      d2d4 g8f6 c2c4 g7g6 d1c2
      
      [ECO "E60"]
      [Opening "King's Indian"]
      [Variation "Anti-Gruenfeld"]
      d2d4 g8f6 c2c4 g7g6 d4d5
      
      [ECO "E60"]
      [Opening "King's Indian"]
      [Variation "Danube gambit"]
      d2d4 g8f6 c2c4 g7g6 d4d5 b7b5
      
      [ECO "E60"]
      [Opening "King's Indian"]
      [Variation "3.g3"]
      d2d4 g8f6 c2c4 g7g6 g2g3
      
      [ECO "E60"]
      [Opening "King's Indian"]
      [Variation "3.g3, counterthrust variation"]
      d2d4 g8f6 c2c4 g7g6 g2g3 f8g7 f1g2 d7d5
      
      [ECO "E61"]
      [Opening "King's Indian defence, 3.Nc3"]
      d2d4 g8f6 c2c4 g7g6 b1c3
      
      [ECO "E61"]
      [Opening "King's Indian"]
      [Variation "Smyslov system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 c1g5
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Larsen system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 c7c6 e1g1 c8f5
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Kavalek (Bronstein) variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 c7c6 e1g1 d8a5
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto with ...Nc6"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8c6
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Uhlmann (Szabo) variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8c6 e1g1 e7e5
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto, lesser Simagin (Spassky) variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8c6 e1g1 c8f5
      
      [ECO "E62"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Simagin variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8c6 e1g1 c8g4
      
      [ECO "E63"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Panno variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8c6 e1g1 a7a6
      
      [ECO "E64"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Yugoslav system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 c7c5
      
      [ECO "E65"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Yugoslav, 7.O-O"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 c7c5 e1g1
      
      [ECO "E66"]
      [Opening "King's Indian"]
      [Variation "fianchetto, Yugoslav Panno"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 c7c5 e1g1 b8c6 d4d5
      
      [ECO "E67"]
      [Opening "King's Indian"]
      [Variation "fianchetto with ...Nd7"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8d7
      
      [ECO "E67"]
      [Opening "King's Indian"]
      [Variation "fianchetto, classical variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8d7 e1g1 e7e5
      
      [ECO "E68"]
      [Opening "King's Indian"]
      [Variation "fianchetto, classical variation, 8.e4"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8d7 e1g1 e7e5 e2e4
      
      [ECO "E69"]
      [Opening "King's Indian"]
      [Variation "fianchetto, classical main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 g1f3 d7d6 g2g3 e8g8 f1g2 b8d7 e1g1 e7e5 e2e4 c7c6 h2h3
      
      [ECO "E70"]
      [Opening "King's Indian"]
      [Variation "4.e4"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4
      
      [ECO "E70"]
      [Opening "King's Indian"]
      [Variation "Kramer system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1e2
      
      [ECO "E70"]
      [Opening "King's Indian"]
      [Variation "accelerated Averbakh system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 c1g5
      
      [ECO "E71"]
      [Opening "King's Indian"]
      [Variation "Makagonov system (5.h3)"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 h2h3
      
      [ECO "E72"]
      [Opening "King's Indian with e4 & g3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g2g3
      
      [ECO "E72"]
      [Opening "King's Indian"]
      [Variation "Pomar system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g2g3 e8g8 f1g2 e7e5 g1e2
      
      [ECO "E73"]
      [Opening "King's Indian"]
      [Variation "5.Be2"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f1e2
      
      [ECO "E73"]
      [Opening "King's Indian"]
      [Variation "Semi-Averbakh system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f1e2 e8g8 c1e3
      
      [ECO "E73"]
      [Opening "King's Indian"]
      [Variation "Averbakh system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f1e2 e8g8 c1g5
      
      [ECO "E74"]
      [Opening "King's Indian"]
      [Variation "Averbakh, 6...c5"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f1e2 e8g8 c1g5 c7c5
      
      [ECO "E75"]
      [Opening "King's Indian"]
      [Variation "Averbakh, main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f1e2 e8g8 c1g5 c7c5 d4d5 e7e6
      
      [ECO "E76"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4
      
      [ECO "E76"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack, dynamic line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 g1f3 c7c5 d4d5
      
      [ECO "E77"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack, 6.Be2"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 f1e2
      
      [ECO "E77"]
      [Opening "King's Indian"]
      [Variation "Six pawns attack"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 f1e2 c7c5 d4d5 e7e6 d5e6 f7e6 g2g4 b8c6 h2h4
      
      [ECO "E77"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 f1e2 c7c5 d4d5 e7e6 g1f3
      
      [ECO "E77"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack, Florentine gambit"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 f1e2 c7c5 d4d5 e7e6 g1f3 e6d5 e4e5
      
      [ECO "E78"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack, with Be2 and Nf3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 f1e2 c7c5 g1f3
      
      [ECO "E79"]
      [Opening "King's Indian"]
      [Variation "Four pawns attack, main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f4 e8g8 f1e2 c7c5 g1f3 c5d4 f3d4 b8c6 c1e3
      
      [ECO "E80"]
      [Opening "King's Indian"]
      [Variation "Saemisch variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3
      
      [ECO "E81"]
      [Opening "King's Indian"]
      [Variation "Saemisch, 5...O-O"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8
      
      [ECO "E81"]
      [Opening "King's Indian"]
      [Variation "Saemisch, Byrne variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 c7c6 f1d3 a7a6
      
      [ECO "E82"]
      [Opening "King's Indian"]
      [Variation "Saemisch, double fianchetto variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 b7b6
      
      [ECO "E83"]
      [Opening "King's Indian"]
      [Variation "Saemisch, 6...Nc6"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 b8c6
      
      [ECO "E83"]
      [Opening "King's Indian"]
      [Variation "Saemisch, Ruban variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 b8c6 g1e2 a8b8
      
      [ECO "E83"]
      [Opening "King's Indian"]
      [Variation "Saemisch, Panno formation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 b8c6 g1e2 a7a6
      
      [ECO "E84"]
      [Opening "King's Indian"]
      [Variation "Saemisch, Panno main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 b8c6 g1e2 a7a6 d1d2 a8b8
      
      [ECO "E85"]
      [Opening "King's Indian"]
      [Variation "Saemisch, orthodox variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5
      
      [ECO "E86"]
      [Opening "King's Indian"]
      [Variation "Saemisch, orthodox, 7.Nge2 c6"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5 g1e2 c7c6
      
      [ECO "E87"]
      [Opening "King's Indian"]
      [Variation "Saemisch, orthodox, 7.d5"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5 d4d5
      
      [ECO "E87"]
      [Opening "King's Indian"]
      [Variation "Saemisch, orthodox, Bronstein variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5 d4d5 f6h5 d1d2 d8h4 g2g3 h5g3 d2f2 g3f1 f2h4 f1e3 e1e2 e3c4
      
      [ECO "E88"]
      [Opening "King's Indian"]
      [Variation "Saemisch, orthodox, 7.d5 c6"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5 d4d5 c7c6
      
      [ECO "E89"]
      [Opening "King's Indian"]
      [Variation "Saemisch, orthodox main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 f2f3 e8g8 c1e3 e7e5 d4d5 c7c6 g1e2 c6d5
      
      [ECO "E90"]
      [Opening "King's Indian"]
      [Variation "5.Nf3"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3
      
      [ECO "E90"]
      [Opening "King's Indian"]
      [Variation "Larsen variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 c1e3
      
      [ECO "E90"]
      [Opening "King's Indian"]
      [Variation "Zinnowitz variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 c1g5
      
      [ECO "E91"]
      [Opening "King's Indian"]
      [Variation "6.Be2"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2
      
      [ECO "E91"]
      [Opening "King's Indian"]
      [Variation "Kazakh variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 b8a6
      
      [ECO "E92"]
      [Opening "King's Indian"]
      [Variation "classical variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5
      
      [ECO "E92"]
      [Opening "King's Indian"]
      [Variation "Andersson variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 d4e5
      
      [ECO "E92"]
      [Opening "King's Indian"]
      [Variation "Gligoric-Taimanov system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 c1e3
      
      [ECO "E92"]
      [Opening "King's Indian"]
      [Variation "Petrosian system"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 d4d5
      
      [ECO "E92"]
      [Opening "King's Indian"]
      [Variation "Petrosian system, Stein variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 d4d5 a7a5
      
      [ECO "E93"]
      [Opening "King's Indian"]
      [Variation "Petrosian system, main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 d4d5 b8d7
      
      [ECO "E93"]
      [Opening "King's Indian"]
      [Variation "Petrosian system, Keres variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 d4d5 b8d7 c1g5 h7h6 g5h4 g6g5 h4g3 f6h5 h2h4
      
      [ECO "E94"]
      [Opening "King's Indian"]
      [Variation "orthodox variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1
      
      [ECO "E94"]
      [Opening "King's Indian"]
      [Variation "orthodox, Donner variation"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 c7c6
      
      [ECO "E94"]
      [Opening "King's Indian"]
      [Variation "orthodox, 7...Nbd7"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8d7
      
      [ECO "E95"]
      [Opening "King's Indian"]
      [Variation "orthodox, 7...Nbd7, 8.Re1"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8d7 f1e1
      
      [ECO "E96"]
      [Opening "King's Indian"]
      [Variation "orthodox, 7...Nbd7, main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8d7 f1e1 c7c6 e2f1 a7a5
      
      [ECO "E97"]
      [Opening "King's Indian"]
      [Variation "orthodox, Aronin-Taimanov variation (Yugoslav attack / Mar del Plata variation)"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8c6
      
      [ECO "E97"]
      [Opening "King's Indian"]
      [Variation "orthodox, Aronin-Taimanov, bayonet attack"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8c6 d4d5 c6e7 b2b4
      
      [ECO "E98"]
      [Opening "King's Indian"]
      [Variation "orthodox, Aronin-Taimanov, 9.Ne1"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8c6 d4d5 c6e7 f3e1
      
      [ECO "E99"]
      [Opening "King's Indian"]
      [Variation "orthodox, Aronin-Taimanov, main line"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8c6 d4d5 c6e7 f3e1 f6d7 f2f3 f7f5
      
      [ECO "E99"]
      [Opening "King's Indian"]
      [Variation "orthodox, Aronin-Taimanov, Benko attack"]
      d2d4 g8f6 c2c4 g7g6 b1c3 f8g7 e2e4 d7d6 g1f3 e8g8 f1e2 e7e5 e1g1 b8c6 d4d5 c6e7 f3e1 f6d7 f2f3 f7f5 g2g4
    """.trimIndent()
  }

}

data class Opening(
  val eco: String,
  val name: String,
  val variation: String?,
  val moves: List<Move>
)
