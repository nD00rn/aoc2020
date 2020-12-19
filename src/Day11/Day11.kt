package Day11

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val testInput = Day11.loadInput("./src/Day11/Day11.test")
    val realInput = Day11.loadInput("./src/Day11/Day11.input")

    // 37
    println("p1 test input contains ${Day11.solvePartOne(testInput)} taken seats")

    // 2222
    println("p1 real input contains ${Day11.solvePartOne(realInput)} taken seats")

    // 26
    println("p2 test input contains ${Day11.solvePartTwo(testInput)} taken seats")

    // 2032
    println("p2 real input contains ${Day11.solvePartTwo(realInput)} taken seats")
}

private class Day11 {
    companion object {

        fun loadInput(path: String): GameOfLifeMap {
            val input = Files.readAllLines(Paths.get(path), Charsets.UTF_8)

            return GameOfLifeMap(input)
        }

        fun solvePartOne(input: GameOfLifeMap): Int {
            return run(
                input,
                viewRange = 1,
                freeToOccupied = { it == 0 },
                occupiedToFree = { it >= 4 }
            )
        }

        fun solvePartTwo(input: GameOfLifeMap): Int {
            return run(
                input,
                viewRange = maxOf(input.width, input.height),
                freeToOccupied = { it == 0 },
                occupiedToFree = { it >= 5 }
            )
        }

        private fun run(
            input: GameOfLifeMap,
            viewRange: Int,
            occupiedToFree: (Int) -> Boolean,
            freeToOccupied: (Int) -> Boolean
        ): Int {
            var previous: GameOfLifeMap
            var new = input

            do {
                previous = new
                new = new.tick(
                    viewRange,
                    occupiedToFree,
                    freeToOccupied,
                )
            } while (new != previous)

            return new.count('#')
        }
    }
}

private data class GameOfLifeMap(val input: List<String>) {
    val width = input.first().length
    val height = input.size

    fun tick(
        viewRange: Int,
        occupiedToFree: (Int) -> Boolean,
        freeToOccupied: (Int) -> Boolean
    ): GameOfLifeMap {
        val output = mutableListOf<String>()
        for (y in 0 until height) {
            var lineOutput = ""
            for (x in 0 until width) {
                lineOutput += when (getCell(x, y)) {
                    '#' -> {
                        if (occupiedToFree.invoke(canSeeType(x, y, viewRange, '#'))) {
                            'L'
                        } else {
                            '#'
                        }
                    }
                    'L' -> {
                        if (freeToOccupied.invoke(canSeeType(x, y, viewRange, '#'))) {
                            '#'
                        } else {
                            'L'
                        }
                    }
                    else -> getCell(x, y)
                }
            }
            output.add(lineOutput)
        }

        return GameOfLifeMap(output)
    }

    private fun getCell(x: Int, y: Int): Char {
        if (x !in 0 until width || y !in 0 until height) {
            return ' '
        }

        return input[y][x]
    }

    private fun canSeeType(x: Int, y: Int, range: Int, type: Char): Int {
        return listOf(
            findFirstCell(x, y, range) { xx, yy -> xx - 1 to yy },
            findFirstCell(x, y, range) { xx, yy -> xx + 1 to yy },

            findFirstCell(x, y, range) { xx, yy -> xx - 1 to yy - 1 },
            findFirstCell(x, y, range) { xx, yy -> xx to yy - 1 },
            findFirstCell(x, y, range) { xx, yy -> xx + 1 to yy - 1 },

            findFirstCell(x, y, range) { xx, yy -> xx - 1 to yy + 1 },
            findFirstCell(x, y, range) { xx, yy -> xx to yy + 1 },
            findFirstCell(x, y, range) { xx, yy -> xx + 1 to yy + 1 }
        )
            .filter { it == type }
            .count()
    }

    private fun findFirstCell(x: Int, y: Int, range: Int, action: (Int, Int) -> Pair<Int, Int>): Char {
        val (newX, newY) = action.invoke(x, y)

        if (range == 1) {
            return getCell(newX, newY)
        }

        return when (getCell(newX, newY)) {
            '#' -> return '#'
            'L' -> return 'L'
            '.' -> findFirstCell(newX, newY, range - 1, action)
            else -> ' '
        }
    }

    fun count(type: Char): Int {
        var output = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (getCell(x, y) == type) {
                    output += 1
                }
            }
        }

        return output
    }

    override fun toString(): String {
        return input.joinToString(separator = "\n") { it }
    }
}
