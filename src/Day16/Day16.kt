package Day16

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

fun main() {
    val testData = Day16.readInput("./src/Day16/Day16.test")
    val testData2 = Day16.readInput("./src/Day16/Day16.test2")
    val realData = Day16.readInput("./src/Day16/Day16.input")

    println(testData)

    // 71
    println("Day 16 part 1 - test data ${Day16.solvePartOne(testData)}")

    // 25916
    println("Day 16 part 1 - real data ${Day16.solvePartOne(realData)}")

    // 71
    println("Day 16 part 2 - test data ${Day16.solvePartTwo(testData2)}")

    // 2564529489989
    println("Day 16 part 2 - real data ${Day16.solvePartTwo(realData)}")
}

class Day16 {
    companion object {

        fun readInput(path: String): Notes {
            return Notes(
                Files.readAllLines(
                    Paths.get(path),
                    Charsets.UTF_8
                )
            )
        }

        fun solvePartOne(notes: Notes): Long {
            var output: Long = 0

            notes.nearby.forEach { tickets ->
                tickets.forEach { ticketValue ->
                    val notFoundValue = notes.fields.values.none { field ->
                        field.any { it == ticketValue }
                    }

                    if (notFoundValue) {
                        output += ticketValue
                    }
                }
            }

            return output
        }

        fun solvePartTwo(notes: Notes): Long {
            val validNearby = notes.nearby
                .filter { tickets ->
                    tickets.all { ticketValue ->
                        notes.fields.values.any { field ->
                            field.any { it == ticketValue }
                        }
                    }
                }

            val ticketWidth = validNearby[0].size
            val remainingCategories = mutableListOf<Int>()
            for (i in 0 until ticketWidth) remainingCategories.add(i)

            val outputMapping: MutableMap<String, Int> = mutableMapOf()

            val pairs = notes.fields.map { (fieldName, validValuesForField) ->
                val list = Stream.iterate(0) { it + 1 }
                    .limit(ticketWidth.toLong())
                    .filter { !validNearby.all { nearby -> nearby[it] in validValuesForField } }
                    .collect(Collectors.toList())

                fieldName to list
            }.sortedByDescending { it.second.size }

            for (pair in pairs) {
                val (key, value) = pair
                val missingValue = remainingCategories.stream().filter { it !in value }.findAny().get()

                outputMapping.put(key, missingValue)
                remainingCategories.remove(missingValue)
            }

            println(outputMapping)

            return outputMapping
                .filter { it.key.startsWith("departure ") }
                .values
                .map { notes.ticket[it].toLong() }
                .stream()
                .reduce(1L) { a, b -> a * b }
        }
    }
}

data class Notes(val input: List<String>) {
    internal val fields: MutableMap<String, List<Int>> = mutableMapOf()
    internal val ticket: MutableList<Int> = mutableListOf()
    internal val nearby: MutableList<List<Int>> = mutableListOf()

    init {
        var mode = 0

        input.forEach { line ->
            if (line.isEmpty()) {
                mode++
                return@forEach
            }
            if (line.endsWith(":")) {
                return@forEach
            }

            when (mode) {
                0 -> {
                    val (name, ranges) = line.split(": ")
                    val (first, second) = ranges.split(" or ")
                    val (min1, max1) = first.split("-").map { it.toInt() }
                    val (min2, max2) = second.split("-").map { it.toInt() }
                    val values = mutableListOf<Int>()
                    values.addAll(IntRange(min1, max1).map { it })
                    values.addAll(IntRange(min2, max2).map { it })
                    fields[name] = values
                }
                1 -> ticket.addAll(line.split(",").map { it.toInt() })
                2 -> nearby.add(line.split(",").map { it.toInt() })
            }
        }
    }

    override fun toString() = "$fields | $ticket | $nearby"
}
