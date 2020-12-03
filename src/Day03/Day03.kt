import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Stream

fun main() {
    val testMap = Day03.loadMap("./src/Day03/Day03.test")
    val realMap = Day03.loadMap("./src/Day03/Day03.input")

    // 7 trees
    println("Found ${Day03.solvePartOne(testMap)} trees for part 1 on test map")

    // 205 trees
    println("Found ${Day03.solvePartOne(realMap)} trees for part 1 on real map")

    // answer: 336 (multiplied)
    println("Found ${Day03.solvePartTwo(testMap)} as the solution for part 2 on test map")

    // answer: 3952146825 (multiplied)
    println("Found ${Day03.solvePartTwo(realMap)} as the solution for part 2 on real map")
}

class Day03 {
    companion object {

        fun loadMap(path: String): AocMap {
            val lines = Files.readAllLines(Paths.get(path), Charsets.UTF_8)
            return AocMap(lines)
        }

        fun solvePartOne(map: AocMap): Long = calculateTreesOnPath(map, 3 to 1)

        fun solvePartTwo(map: AocMap): Long {
            return listOf(
                1 to 1,
                3 to 1,
                5 to 1,
                7 to 1,
                1 to 2
            )
                .stream()
                .mapToLong { angle -> calculateTreesOnPath(map, angle) }
                .reduce(1) { a, b -> a * b }
        }

        private fun calculateTreesOnPath(map: AocMap, angle: Pair<Int, Int>): Long {
            var x = 0

            return Stream.iterate(angle.second) { it + angle.second }
                .limit(map.height / angle.second.toLong())
                .peek { x += angle.first }
                .filter { y -> map.isTree(x, y) }
                .count()
        }
    }
}

data class AocMap(val input: List<String>) {
    val height = input.size
    private val width = input.first().length

    fun isTree(x: Int, y: Int): Boolean = y < height && input[y][x % width] == '#'
}
