package Day09

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

fun main() {
    val testInput = Day09.loadInput("./src/Day09/Day09.test")
    val realInput = Day09.loadInput("./src/Day09/Day09.input")

    // 127
    println("Part one test: ${Day09.solvePartOne(testInput, 5)}")

    // 1863 too low, actual: 248131121
    println("Part one real: ${Day09.solvePartOne(realInput, 25)}")

    // 62
    println("Part two test: ${Day09.solvePartTwo(127, testInput)}")

    // 31580383
    println("Part two real: ${Day09.solvePartTwo(248131121, realInput)}")
}

private class Day09 {
    companion object {

        fun loadInput(path: String): List<Long> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8).stream()
                .map { it.toLong() }
                .collect(Collectors.toList())
        }

        fun solvePartOne(numbers: List<Long>, preamble: Int): Long {
            val firstIndex = Stream.iterate(preamble) { it + 1 }
                .limit(numbers.size.toLong() - preamble)
                .filter { !containsSum(numbers[it], numbers.subList(it - preamble, it)) }
                .findFirst()
                .get()

            return numbers[firstIndex]
        }

        fun solvePartTwo(wantedNumber: Long, numbers: List<Long>): Long {
            var (low, high) = arrayOf(0, 0)

            while (true) {
                val numberRange = numbers.subList(low, high)
                val sum = numberRange.sum()

                when {
                    sum < wantedNumber -> high += 1
                    sum > wantedNumber -> low += 1
                    sum == wantedNumber -> {
                        return numberRange.minOrNull()!! +
                            numberRange.maxOrNull()!!
                    }
                    else -> {
                        println("You br0k3 T|-|3 syst3nn :0")
                        return -1
                    }
                }
            }
        }

        private fun containsSum(requested: Long, numbers: List<Long>): Boolean {
            return numbers.stream()
                .parallel()
                .filter { it * 2 != requested }
                .anyMatch { requested - it in numbers }
        }

    }
}
