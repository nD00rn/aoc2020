import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors.toList

fun main() {
    val testInput = Day05.readInput("./src/Day05/Day05.test")
    val realInput = Day05.readInput("./src/Day05/Day05.input")

    // seat id: 820
    println("The highest seat id is ${Day05.solvePartOne(testInput)} using test data in part one")

    // seat id: 848
    println("The highest seat id is ${Day05.solvePartOne(realInput)} using real data in part one")

    // seat id: 682
    println("My seat id is ${Day05.solvePartTwo(realInput)} for part two")
}

class Day05 {
    companion object {

        fun readInput(path: String): List<Int> {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)

            return lines.stream()
                .map(::generateBoardingPass)
                .map(::generateSeatId)
                .collect(toList())
        }

        fun solvePartOne(seatIds: List<Int>): Int {
            return seatIds.maxOrNull()!!
        }

        fun solvePartTwo(seatIds: List<Int>): Int {
            return findGapsInSeatIds(seatIds.sorted())
        }

        private fun findGapsInSeatIds(seatIds: List<Int>): Int {
            val seatIdIndexLeftOfMe = (seatIds.indices)
                .first { index -> seatIds[index] == seatIds[index + 1] - 2 }

            return seatIds[seatIdIndexLeftOfMe] + 1
        }

        private fun generateSeatId(boardingPass: BoardingPass): Int = boardingPass.row * 8 + boardingPass.column

        private fun generateBoardingPass(boardingPass: String): BoardingPass {
            var rowRange = 0 until 128
            var colRange = 0 until 8

            boardingPass.forEach {
                val rowsAvailable = rowRange.last - rowRange.first + 1
                val colsAvailable = colRange.last - colRange.first + 1

                when (it) {
                    'F' -> rowRange = rowRange.first..(rowRange.last - (rowsAvailable / 2))
                    'B' -> rowRange = (rowRange.first + (rowsAvailable / 2))..rowRange.last

                    'L' -> colRange = colRange.first..(colRange.last - (colsAvailable / 2))
                    'R' -> colRange = (colRange.first + (colsAvailable / 2))..colRange.last
                }
            }
            return BoardingPass(rowRange.first, colRange.last)
        }
    }
}

data class BoardingPass(val row: Int, val column: Int)
