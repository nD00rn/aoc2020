package Day18

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val testData = Day18.loadInput("./src/Day18/Day18.test")
    val realData = Day18.loadInput("./src/Day18/Day18.input")

    // 26335
    println("Day 18 part 1 - test data - ${Day18.solvePartOne(testData)}")

    // 280014646144
    println("Day 18 part 1 - real data - ${Day18.solvePartOne(realData)}")

    println("Day 18 part 2 - test data - ${Day18.solvePartTwo(testData)}")
}

class Day18 {
    companion object {

        fun loadInput(path: String): List<String> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8)
        }

        fun solvePartOne(inputs: List<String>): Long {
            return inputs.stream()
                .map(this::solveEquation)
                .mapToLong { it.first }
                .sum()
        }

        fun solveEquation(input: String): Pair<Long, Int> {
            var output: Long = 0
            var number: Long? = null
            var operation = "plus"

            var index = 0
            while (true) {
                if (index == input.length) {
//                    println("End of input reached, output=$output, number=$number, operation=$operation")
                    return when (operation) {
                        "plus" -> output + (number ?: 0) to index
                        "multiply" -> output * (number ?: 1) to index
                        else -> (-1).toLong() to -1
                    }
                }

                val c = input[index]
//                println("processing      $index  $c")
                when (c) {
                    '(' -> {
                        index++
                        val (subOutput, subIndex) = solveEquation(input.substring(index))
//                        println("Got subOutput=$subOutput, subIndex=$subIndex")
                        when (operation) {
                            "plus" -> output += subOutput
                            "multiply" -> output *= subOutput
                        }
                        index += subIndex
                    }
                    ')' -> {
//                        println("closing bracket reached, output=$output, number=$number, operation=$operation")
                        return when (operation) {
                            "plus" -> output + (number ?: 0) to index
                            "multiply" -> output * (number ?: 1) to index
                            else -> (-1).toLong() to -1
                        }
                    }
                    in '0'..'9' -> {
                        number = (number ?: 0) * 10 + (c - '0')
//                        println("Number is $number")
                    }
                    '+' -> {
                        when (operation) {
                            "plus" -> output += (number ?: 0)
                            "multiply" -> output *= (number ?: 1)
                        }
                        operation = "plus"
                        number = null
                        index++
                    }
                    '*' -> {
                        when (operation) {
                            "plus" -> output += (number ?: 0)
                            "multiply" -> output *= (number ?: 1)
                        }
                        operation = "multiply"
                        number = null
                        index++
                    }
                }
                index++
            }

        }

    }
}
