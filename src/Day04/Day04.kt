import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testPassports = Day04.readPassports("./src/Day04/Day04.test")
    val realPassports = Day04.readPassports("./src/Day04/Day04.input")

    // 2 valid passports
    println("Part one: There are ${Day04.solvePartOne(testPassports)} valid test passports in this list")

    // 202 valid passports (~355 passports, 60% valid -> 213 valid passports)
    println("Part one: There are ${Day04.solvePartOne(realPassports)} valid real passports in this list")

    // 2 valid passports
    println("Part two: There are ${Day04.solvePartTwo(testPassports)} valid test passports in this list")

    // 137 valid passports (it is not 138, that answer is too high)
    println("Part two: There are ${Day04.solvePartTwo(realPassports)} valid real passports in this list")
}

class Day04 {
    companion object {

        fun readPassports(path: String): List<Passport> {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)

            val passports = mutableListOf<String>()
            var passport = ""

            lines.forEach { line ->
                if (line.isEmpty()) {
                    passports.add(passport)
                    passport = ""
                } else {
                    passport += " $line"
                }
            }
            passports.add(passport)

            return passports.stream()
                .map { Passport(it.trim()) }
                .collect(Collectors.toList())
        }

        fun solvePartOne(passports: List<Passport>): Int {
            return passports.stream()
                .filter { it.isValidForPartOne() }
                .count()
                .toInt()
        }

        fun solvePartTwo(passports: List<Passport>): Int {
            return passports.stream()
                .filter { it.isValidForPartTwo() }
                .count()
                .toInt()
        }

    }
}

data class Passport(private val input: String) {
    private var ecl: String = ""
    private var pid: String = ""
    private var eyr: String = ""
    private var hcl: String = ""
    private var byr: String = ""
    private var iyr: String = ""
    private var cid: String = ""
    private var hgt: String = ""

    init {
        input.split(" ").forEach { kv ->
            val (key, value) = kv.split(":")

            when (key) {
                "ecl" -> ecl = value
                "pid" -> pid = value
                "eyr" -> eyr = value
                "hcl" -> hcl = value
                "byr" -> byr = value
                "iyr" -> iyr = value
                "cid" -> cid = value
                "hgt" -> hgt = value
            }
        }
    }

    fun isValidForPartOne(): Boolean {
        return ecl.isNotEmpty() &&
            pid.isNotEmpty() &&
            eyr.isNotEmpty() &&
            hcl.isNotEmpty() &&
            byr.isNotEmpty() &&
            iyr.isNotEmpty() &&
            hgt.isNotEmpty()
    }

    fun isValidForPartTwo(): Boolean {
        return isValidForPartOne()
            && (1920..2002).contains(byr.toInt())
            && (2010..2020).contains(iyr.toInt())
            && (2020..2030).contains(eyr.toInt())
            && hcl matches Regex("#[0-9a-f]{6}")
            && listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(ecl)
            && pid matches Regex("^[\\d]{9}$")
            && validHeight()
    }

    private fun validHeight() = when {
        hgt.endsWith("in") -> (59..76).contains(hgt.substringBefore("in").toInt())
        hgt.endsWith("cm") -> (150..193).contains(hgt.substringBefore("cm").toInt())
        else -> false
    }
}
