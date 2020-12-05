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

        fun readInput(path: String): List<RawBoardingPass> {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)

            return lines.stream()
                .map { RawBoardingPass(it) }
                .collect(toList())
        }

        fun solvePartOne(rawPasses: List<RawBoardingPass>): Int {
            val plane = generatePlane()

            return rawPasses.stream()
                .map { generateBoardingPass(plane, it) }
                .map { generateSeatId(it) }
                .sorted { a, b -> compareValues(b, a) }
                .findFirst()
                .get()
        }

        fun solvePartTwo(rawPasses: List<RawBoardingPass>): Int {
            val plane = generatePlane()

            val boardingPasses = rawPasses.stream()
                .map { generateBoardingPass(plane, it) }
                .collect(toList())

            val validRows = findAvailableRows(boardingPasses)
            println("First valid row is ${validRows.first()}, last is ${validRows.last()}")

            val validTakenSeatIds = findValidSeatIds(validRows, boardingPasses)

            println(validTakenSeatIds)

            val gapsInSeatIds = findGapsInSeatIds(validTakenSeatIds)

            println(gapsInSeatIds)

            return -1
        }

        private fun findGapsInSeatIds(input: List<Int>): List<Int> {
            val missingIds = mutableListOf<Int>()

            for (i in input.first()..input.last()) {
                if (i + 1 in input && i - 1 in input && i !in input) {
                    missingIds.add(i)
                }
            }

            return missingIds.toList()
        }

        private fun findValidSeatIds(
            validRows: List<Int>,
            boardingPasses: List<BoardingPass>
        ): List<Int> {
            return boardingPasses.stream()
                .filter { validRows.contains(it.row) }
                .map { generateSeatId(it) }
                .sorted { a, b -> a.compareTo(b) }
                .collect(toList())
        }

        private fun findAvailableRows(boardingPass: List<BoardingPass>): List<Int> {
            return boardingPass.stream()
                .map { it.row }
                .distinct()
                .sorted()
                .collect(toList())
        }

        private fun generatePlane() = Plane(128, 8)

        private fun generateSeatId(boardingPass: BoardingPass): Int = boardingPass.row * 8 + boardingPass.column

        private fun generateBoardingPass(plane: Plane, boardingPass: RawBoardingPass): BoardingPass {
            var rowRange = 0 until plane.length
            var colRange = 0 until plane.width

            boardingPass.rawInput.forEach {
                val rowsAvailable = rowRange.last - rowRange.first + 1
                val colsAvailable = colRange.last - colRange.first + 1

                when (it) {
                    'F' -> rowRange = rowRange.first..(rowRange.last - (rowsAvailable / 2))
                    'B' -> rowRange = (rowRange.first + (rowsAvailable / 2))..rowRange.last

                    'L' -> colRange = colRange.first..(colRange.last - (colsAvailable / 2))
                    'R' -> colRange = (colRange.first + (colsAvailable / 2))..colRange.last
                }
            }
//            println("Generated boarding pass with rowRange $rowRange and colRange $colRange")
            return BoardingPass(rowRange.first, colRange.last)
        }
    }
}

data class Plane(val length: Int, val width: Int)
data class RawBoardingPass(val rawInput: String)
data class BoardingPass(val row: Int, val column: Int)
