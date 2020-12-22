package Day18

import java.nio.file.Files
import java.nio.file.Paths
import java.util.LinkedList

fun main() {
    val testData = Day18.loadInput("./src/Day18/Day18.test")
    val realData = Day18.loadInput("./src/Day18/Day18.input")

    // 26335
    println("Day 18 part 1 - test data - ${Day18.solvePartOne(testData)}")

    // 280014646144
    println("Day 18 part 1 - real data - ${Day18.solvePartOne(realData)}")

    //
    println("Day 18 part 2 - test data - ${Day18.solvePartTwo(testData)}")

    // 6156103595723549 too high, 1973139638849395 too high
    // Actual is 9966990988262
    println("Day 18 part 2 - real data - ${Day18.solvePartTwo(realData)}")
}

class Day18 {
    companion object {

        fun loadInput(path: String): List<String> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8)
        }

        fun solvePartOne(inputs: List<String>): Long {
            return inputs.stream()
                .map { toLinkedList(it) }
                .mapToLong { evaluate(it, false) }
                .sum()
        }

        fun solvePartTwo(inputs: List<String>): Long {
            return inputs.stream()
                .map { toLinkedList(it) }
                .mapToLong { evaluate(it, true) }
                .sum()
        }

        private fun toLinkedList(input: String): LinkedList<Char> {
            var output = LinkedList<Char>()
            input.forEach { output.add(it) }
            return output
        }

        private fun evaluate(tokens: LinkedList<Char>, plusFirst: Boolean): Long {
            var multiplier: Long = 1
            var accumulator: Long = 0
            var op = "ADD"

            while (tokens.isNotEmpty()) {
                val token = tokens.removeAt(0)
                when (token) {
                    in '0'..'9' -> {
                        val value = (token - '0') * multiplier
                        accumulator = operation(op, accumulator, value)
                    }
                    '+' -> op = "ADD"
                    '*' -> {
                        if (plusFirst) {
                            multiplier = accumulator
                            accumulator = 0
                        } else {
                            op = "MUL"
                        }
                    }
                    '(' -> {
                        val value = evaluate(tokens, plusFirst) * multiplier
                        accumulator = operation(op, accumulator, value)
                    }
                    ' ' -> continue
                    else -> break
                }
            }

            return accumulator
        }

        private fun operation(operation: String, a: Long, b: Long): Long {
            return when (operation) {
                "ADD" -> a + b
                "MUL" -> a * b
                else -> -1
            }
        }

    }
}
