package Day15

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val testData = Day15.loadInput("./src/Day15/Day15.test")
    val realData = Day15.loadInput("./src/Day15/Day15.input")

    // 436
    println("d15 p1 - test data ${Day15.solvePartOne(testData)}")

    // 475
    println("d15 p1 - real data ${Day15.solvePartOne(realData)}")

    // 175594
    println("d15 p2 - test data ${Day15.solvePartTwo(testData)}")

    // 71164 too high, actual 11261
    println("d15 p2 - real data ${Day15.solvePartTwo(realData)}")
}

private class Day15 {
    companion object {

        fun loadInput(path: String): List<Numbers> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8)
                .map(::Numbers)
        }

        fun solvePartOne(input: List<Numbers>): Long {
            return input
                .map { runNumbers(it, 2020) }
                .first()
        }

        fun solvePartTwo(input: List<Numbers>): Long {
            return input
                .map { runNumbers(it, 30_000_000) }
                .first()
        }

        fun runNumbers(numbers: Numbers, limit: Int): Long {
            val lastSpoken = mutableMapOf<Long, Long>()
            val history = numbers.numbers.map { it.toLong() }.toMutableList()

            for (ind in numbers.numbers.indices) {
                lastSpoken[history[ind]] = ind.toLong()
            }

            while (history.size <= limit) {
                val ind = history.size - 1
                if (history[ind] !in lastSpoken.keys)
                    history.add(0)
                else
                    history.add(ind - lastSpoken[history[ind]]!!)

                lastSpoken[history[ind]] = ind.toLong()
            }

            return history[limit - 1]
        }
    }
}

data class Numbers(val input: String) {
    val numbers = input.split(",").map { it.toInt() }.toMutableList()
}
