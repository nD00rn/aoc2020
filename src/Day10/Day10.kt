import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testResults1 = Day10.loadInput("./src/Day10/Day10.test.1")
    val testResults2 = Day10.loadInput("./src/Day10/Day10.test.2")
    val realResults = Day10.loadInput("./src/Day10/Day10.input")

    // 35
    println("p1 Test 1 answer is ${Day10.solvePartOne(testResults1)}")

    // 220
    println("p1 Test 2 answer is ${Day10.solvePartOne(testResults2)}")

    // 2368
    println("p1 Real answer is ${Day10.solvePartOne(realResults)}")

    // 8
    println("p2 test results 1 answer has ${Day10.solvePartTwo(testResults1)} options")

    // 19208
    println("p2 test results 2 answer has ${Day10.solvePartTwo(testResults2)} options")

    // 517996544 is too low, actual answer is 1727094849536
    println("p2 real results answer has ${Day10.solvePartTwo(realResults)} options")
}

class Day10 {
    companion object {

        fun loadInput(path: String): List<Int> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8).stream()
                .map { it.toInt() }
                .collect(Collectors.toList())
        }

        fun solvePartOne(input: List<Int>): Int {
            return findDifferencesOf(1, input) * findDifferencesOf(3, input)
        }

        fun solvePartTwo(input: List<Int>): Long {
            val output = input.maxOrNull()!! + 3
            val fullRange = (input + output + 0).sorted()

            val splitParts = splitInput(fullRange)
            return splitParts.stream()
                .mapToLong { findOptions(it).size.toLong() }
                .reduce(1) { a, b -> a * b }
        }

        private fun splitInput(input: List<Int>): List<List<Int>> {
            val output = mutableListOf<List<Int>>()

            var startIndex = 0

            input.forEachIndexed { index, _ ->
                if (index == input.size - 1) {
                    return@forEachIndexed
                }

                val isThreeApart = input[index] + 3 == input[index + 1]

                if (isThreeApart) {
                    output.add(input.subList(startIndex, index + 1))
                    startIndex = index + 1
                }
            }

            output.add(input.subList(startIndex, input.size))
            return output
        }

        private fun findDifferencesOf(difference: Int, input: List<Int>): Int {
            var found = 1
            val sorted = input.sorted()
            input
                .sorted()
                .forEachIndexed { index, value ->
                    if (index == input.size - 1) {
                        return@forEachIndexed
                    }
                    if (sorted[index] + difference == sorted[index + 1]) {
                        found += 1
                    }
                }
            return found
        }

        private fun valuesInRange(input: List<Int>, start: Int, range: Int): List<Int> {
            return input.filter { start + range >= it }
        }

        private fun findOptions(input: List<Int>): MutableSet<List<Int>> {
            if (input.size == 1) {
                return mutableSetOf(listOf())
            }

            val firstValue = input.first()
            val valuesInRange = valuesInRange(input.subList(1, input.size), firstValue, 3)

            val mutSet = mutableSetOf<List<Int>>()

            valuesInRange.forEach { valueInRange ->
                val index = input.indexOf(valueInRange)
                val options = findOptions(input.subList(index, input.size))
                options.forEach { option ->
                    val newList = (option + valueInRange).sorted()
                    mutSet.add(newList)
                }
            }

            return mutSet
        }

    }
}
