import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val requestedNumber = 2020
    val testNumbers = Day01.loadNumbers("./src/Day01.test")
    val realNumbers = Day01.loadNumbers("./src/Day01.input")

    // 514579
    println("test part 1 - " + Day01.solvePartOne(testNumbers, requestedNumber))

    // 1006176
    println("real part 1 - " + Day01.solvePartOne(realNumbers, requestedNumber))

    // 241861950
    println("test part 2 - " + Day01.solvePartTwo(testNumbers, requestedNumber))

    // 199132160
    println("real part 2 - " + Day01.solvePartTwo(realNumbers, requestedNumber))
}

class Day01 {
    companion object {
        fun loadNumbers(path: String): IntArray {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8)

                // Filter any null or zero length values
                .filter { it != null && it.isNotEmpty() }

                // Convert from string to integer
                .mapNotNull { Integer.valueOf(it) }

                // Put all in a int array
                .toIntArray()
        }

        fun solvePartOne(values: IntArray, target: Int): Int {
            val setOfRemainingValues = mutableSetOf<Int>()

            values.forEach {
                val remaining = target - it

                if (setOfRemainingValues.contains(it)) {
                    // Found the solution!
                    return it * remaining
                }

                setOfRemainingValues.add(remaining)
            }

            // Unable to find a solution
            return -1
        }

        fun solvePartTwo(values: IntArray, target: Int): Int {
            val uniqueCombinations = generateAllOptions(values)

            uniqueCombinations.forEach {
                val a = it.first
                val b = it.second
                val c = target - (a + b)

                if (values.contains(c) && a + b + c == target) {
                    return a * b * c
                }
            }

            // Answer was not found
            return -1
        }

        private fun generateAllOptions(input: IntArray): Set<Pair<Int, Int>> {
            val output = mutableSetOf<Pair<Int, Int>>()

            input.forEach a@{ a ->
                input.forEach b@{ b ->
                    // Put the lower value on the left side
                    if (a < b) {
                        output.add(a to b)
                    } else {
                        output.add(b to a)
                    }
                }
            }

            // Convert to immutable set
            return output.toSet()
        }
    }
}
