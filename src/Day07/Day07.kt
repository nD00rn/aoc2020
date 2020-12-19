package Day07

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testBags = Day07.readInput("./src/Day07/Day07.test")
    val testBags2 = Day07.readInput("./src/Day07/Day07.test.part2")
    val realBags = Day07.readInput("./src/Day07/Day07.input")

//    println("Test bags contain ${Day07.solvePartOne(testBags)} shiny paths")

    // 527 is too high, actual answer is 378
//    println("Real bags contain ${Day07.solvePartOne(realBags)} shiny paths")

    // 32
    println("Test bags contain ${Day07.solvePartTwo(testBags)} shiny things")
    // 126
    println("Test bags contain ${Day07.solvePartTwo(testBags2)} shiny things - test two")

    // 24285 too low, 31177 too high, 27526 actual answer
    println("Real bags contain ${Day07.solvePartTwo(realBags)} shiny things - real")

    println("done...")
}

private class Day07 {
    companion object {

        fun readInput(path: String): List<Bag> {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)

            return lines.stream()
                .map(::Bag)
                .collect(Collectors.toList())
        }

        fun solvePartOne(bags: List<Bag>): Int {
            bags.forEach { bag -> bag.findOtherBags(bags) }

            return bags.stream()
                .filter { it.bags.stream().anyMatch { a -> a.color.contains("shiny gold") } }
                .peek { println("check: ${it.color}") }
                .count()
                .toInt()
        }

        fun solvePartTwo(bags: List<Bag>): Int {
//            bags.forEach { bag -> bag.findOtherBags(bags) }

            return bags.first { it.color == "shiny gold" }
                .linkOtherBags(bags)
        }
    }
}

private data class Bag(val input: String) {
    val color: String = input.split("bags", limit = 2)[0].trim()
    var amount: Long = 1

    val bags: MutableList<Bag> = mutableListOf()

    fun linkOtherBags(allBags: List<Bag>): Int {
        val holdingBags = input.split("contain", limit = 2)[1].trim()

        return holdingBags
            .split(",")
            .map map@{ holdingBag ->
                val bagArguments = holdingBag.trim().split(" ")

                if (holdingBag.contains("no other bag")) {
                    return@map 0;
                }

                val amount = bagArguments[0].toInt()
                val holdingBagColor = "${bagArguments[1]} ${bagArguments[2]}"

                val newFound = allBags.stream()
                    .filter { allBags -> allBags.color == holdingBagColor }
                    .findFirst()
                    .get()
                    .linkOtherBags(allBags)

                return@map (amount * newFound) + amount
            }
            .toIntArray()
            .sum()
    }

    fun findOtherBags(allBags: List<Bag>) {
        val foundBags = findOtherBags(allBags, mutableListOf())
        bags.addAll(foundBags)
    }

    fun findOtherBags(allBags: List<Bag>, foundBags: MutableList<Bag>): List<Bag> {
        val holdingBags = input.split("contain", limit = 2)[1].trim()

        holdingBags.split(",").forEach { holdingBag ->
            val bagArguments = holdingBag.trim().split(" ")

            if (holdingBag.contains("no other bags")) {
                return@forEach
            }

            val amount = bagArguments[0].toLong()
            val holdingBagColor = "${bagArguments[1]} ${bagArguments[2]}"

            allBags.stream()
                .filter { allBags -> allBags.color == holdingBagColor }
                .peek { it.amount = amount; foundBags.add(it) }
                .forEach { bag -> bag.findOtherBags(allBags, foundBags) }
        }

        return foundBags.distinct().toList()
    }
}


// light red - bright white + muted yellow + shiny gold + faded blue + dark olive + vibrant plum + dotted black

// light red - bright white + muted yellow
// bright white - shiny gold
// muted yellow - shiny gold + faded blue
// shiny gold - dark olive + vibrant plum
// dark olive - faded blue + dotted black
// vibrant plum - faded blue + dotted black
// dotted black - X
// faded blue - X


