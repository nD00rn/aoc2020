import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val testValues = Day06.readInput("./src/Day06/Day06.test")
    val realValues = Day06.readInput("./src/Day06/Day06.input")

    println("Test values $testValues")

    // 11
    println("The sum of unique test answers in part one is ${Day06.solvePartOne(testValues)}")

    // 6763
    println("The sum of unique real answers in part one is ${Day06.solvePartOne(realValues)}")

    // 6
    println("The sum of unique test answers in part two is ${Day06.solvePartTwo(testValues)}")

    // 3512
    println("The sum of unique real answers in part two is ${Day06.solvePartTwo(realValues)}")
}

class Day06 {
    companion object {

        fun readInput(path: String): List<Group> {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)

            val groups = mutableListOf<Group>()
            val answers = mutableListOf<Answer>()

            lines.forEach {
                if (it.isNotEmpty()) {
                    answers.add(Answer(it))
                } else {
                    groups.add(Group(answers.toList()))
                    answers.clear()
                }
            }

            groups.add(Group(answers))

            return groups
        }

        fun solvePartOne(groups: List<Group>): Int {
            return groups.stream()
                .mapToInt { it.uniqueAnswers() }
                .sum()
        }

        fun solvePartTwo(groups: List<Group>): Int {
            return groups.stream()
                .mapToInt { it.allSameAnswers() }
                .sum()
        }
    }
}

data class Group(val answers: List<Answer>) {
    fun uniqueAnswers(): Int {
        return answers.stream()
            .reduce { a, b -> Answer(a.input + b.input) }
            .map { it.input.chars().distinct().count() }
            .get()
            .toInt()
    }

    fun allSameAnswers(): Int {
        return ('a'..'z').filter { letter ->
            answers.stream().allMatch { it.input.contains(letter) }
        }.count()
    }
}

data class Answer(val input: String)
