import java.lang.Math.abs
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testData = Day12.loadInput("./src/Day12/Day12.test")
    val realData = Day12.loadInput("./src/Day12/Day12.input")

    // 25
    println("p1 - test distance is ${Day12.solvePartOne(testData)}")

    // 2057
    println("p1 - real distance is ${Day12.solvePartOne(realData)}")

    // 286
    println("p2 - test distance is ${Day12.solvePartTwo(testData)}")

    // 71504
    println("p2 - real distance is ${Day12.solvePartTwo(realData)}")
}

class Day12 {
    companion object {

        fun loadInput(path: String): List<Instruction> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8).stream()
                .map(::Instruction)
                .collect(Collectors.toList())
        }

        fun solvePartOne(input: List<Instruction>): Int {
            // East to North
            var facing = 1 to 0

            // Start coordinates of the ship
            var ship = 0 to 0

            input.forEach { instruction ->
                val num = instruction.number
                when (instruction.letter) {
                    'N' -> ship = mutateWayPoint(ship, 0 to 1, num)
                    'E' -> ship = mutateWayPoint(ship, 1 to 0, num)
                    'W' -> ship = mutateWayPoint(ship, -1 to 0, num)
                    'S' -> ship = mutateWayPoint(ship, 0 to -1, num)
                    'F' -> ship = mutateWayPoint(ship, facing, num)
                    'L' -> facing = mutateDirection(facing, -1 * num)
                    'R' -> facing = mutateDirection(facing, num)
                }
            }

            return abs(ship.first) + abs(ship.second)
        }

        fun solvePartTwo(input: List<Instruction>): Int {
            // east to north
            // Start coordinates of the way point
            var wayPoint = 10 to 1

            // Start coordinates of the ship
            var ship = 0 to 0

            input.forEach { instruction ->
                val num = instruction.number
                when (instruction.letter) {
                    'N' -> wayPoint = mutateWayPoint(wayPoint, 0 to 1, num)
                    'E' -> wayPoint = mutateWayPoint(wayPoint, 1 to 0, num)
                    'W' -> wayPoint = mutateWayPoint(wayPoint, -1 to 0, num)
                    'S' -> wayPoint = mutateWayPoint(wayPoint, 0 to -1, num)
                    'F' -> ship = mutateWayPoint(ship, wayPoint, num)
                    'L' -> wayPoint = mutateDirection(wayPoint, -1 * num)
                    'R' -> wayPoint = mutateDirection(wayPoint, num)
                }
            }

            return abs(ship.first) + abs(ship.second)
        }

        private fun mutateDirection(wayPoint: Pair<Int, Int>, angle: Int): Pair<Int, Int> {
            var mutations = angle / 90
            mutations %= 4

            while (mutations < 0) mutations += 4

            // Raw translation  || translation to variables
            // east 10, north 4 || east 10, north 4
            // south 10, east 4 || east 4, north -10
            // west 10, south 4 || east -10, north -4
            // north 10, west 4 || east -4, north 10

            var output = wayPoint
            repeat(mutations) {
                output = output.second to output.first * -1
            }
            return output
        }

        private fun mutateWayPoint(
            input: Pair<Int, Int>,
            mutation: Pair<Int, Int>,
            repeat: Int = 1
        ): Pair<Int, Int> {
            return input.first + (mutation.first * repeat) to input.second + (mutation.second * repeat)
        }

    }
}

data class Instruction(val input: String) {
    val letter: Char = input[0]
    val number: Int = input.substring(1 until input.length).toInt()
}
