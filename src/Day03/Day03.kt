import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val testMap = Day03.loadMap("./src/Day03/Day03.test")
    val realMap = Day03.loadMap("./src/Day03/Day03.input")

    // 7 trees
    println("Found ${Day03.solvePartOne(testMap)} trees for part 1 on test map")

    // 205 trees
    println("Found ${Day03.solvePartOne(realMap)} trees for part 1 on real map")

    // 336 trees (multiplied)
    println("Found ${Day03.solvePartTwo(testMap)} trees for part 2 on test map")

    // 3952146825 trees (multiplied)
    println("Found ${Day03.solvePartTwo(realMap)} trees for part 2 on real map")
}

class Day03 {
    companion object {

        fun loadMap(path: String): AocMap {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)
            return AocMap(lines)
        }

        fun solvePartOne(map: AocMap): Long = calculateTreesOnPath(map, 3 to 1)

        fun solvePartTwo(map: AocMap): Long {
            val angles = listOf(
                1 to 1,
                3 to 1,
                5 to 1,
                7 to 1,
                1 to 2
            )

            return angles.stream()
                .mapToLong { calculateTreesOnPath(map, it) }
                .reduce(1) { a, b -> a * b }
        }

        private fun calculateTreesOnPath(map: AocMap, angle: Pair<Int, Int>): Long {
            var x = 0
            var y = 0
            var treesFound = 0

            do {
                x += angle.first
                y += angle.second

                val coordinate = map.getCoordinate(x, y)

                if (coordinate == AocMap.Entries.Tree) {
                    treesFound += 1
                }

            } while (map.getCoordinate(x, y) != AocMap.Entries.Exit)

            return treesFound.toLong()
        }

    }
}

data class AocMap(private val input: List<String>) {
    fun getCoordinate(x: Int, y: Int): Entries {
        // Check if outside of perimeter
        if (y > input.size - 1) {
            return Entries.Exit
        }

        val line = input[y]

        // Since the line is wrapping around you will need to get the specific
        // index by performing a module operation.
        return when (line[x % line.length]) {
            '#' -> Entries.Tree
            '.' -> Entries.Space
            else -> Entries.Exit
        }
    }

    enum class Entries { Tree, Space, Exit }
}
