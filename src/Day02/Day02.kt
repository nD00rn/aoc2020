import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val testPasswords = Day02.loadPasswords("./src/Day02/Day02.test")
    val realPasswords = Day02.loadPasswords("./src/Day02/Day02.input")

    // 2 valid passwords
    println("Part one contains ${Day02.solvePartOne(testPasswords)} valid test passwords")

    // 424 valid passwords
    println("Part one contains ${Day02.solvePartOne(realPasswords)} valid real passwords")

    // 1 valid password
    println("Part two contains ${Day02.solvePartTwo(testPasswords)} valid test password")

    // 747 valid passwords
    println("Part two contains ${Day02.solvePartTwo(realPasswords)} valid real password")
}

class Day02 {
    companion object {
        fun loadPasswords(path: String): List<PasswordAndRule> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8)
                .map { PasswordAndRule(it) }
                .toList()
        }

        fun solvePartOne(input: List<PasswordAndRule>): Int {
            return input.stream()
                .filter { validatePasswordPartOne(it) }
                .count().toInt()
        }

        fun solvePartTwo(input: List<PasswordAndRule>): Int {
            return input.stream()
                .filter { validatePasswordPartTwo(it) }
                .count().toInt()
        }

        private fun validatePasswordPartOne(p: PasswordAndRule): Boolean {
            val foundChars = p.password.chars()
                .filter { it == p.testChar }
                .count()

            return foundChars in p.minAmount..p.maxAmount
        }

        private fun validatePasswordPartTwo(p: PasswordAndRule): Boolean {
            val a = p.password[p.minAmount - 1].toInt() == p.testChar
            val b = p.password[p.maxAmount - 1].toInt() == p.testChar

            return a xor b
        }
    }
}

data class PasswordAndRule(private val input: String) {
    val password: String
    val testChar: Int
    val minAmount: Int
    val maxAmount: Int

    init {
        val firstSplit = input.split(":", limit = 2)
        password = firstSplit[1].trim()
        val rule = firstSplit[0]

        val secondSplit = rule.split(" ", limit = 2)
        val range = secondSplit[0]
        testChar = secondSplit[1].chars().findFirst().asInt

        val thirdSplit = range.split("-", limit = 2)
        minAmount = thirdSplit[0].toInt()
        maxAmount = thirdSplit[1].toInt()
    }
}
