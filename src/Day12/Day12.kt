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
            var facing = 'E'
            var coordinate = 0 to 0

            input.forEach { instruction ->
                val (x, y) = coordinate
                val num = instruction.number
                when (instruction.letter) {
                    'N' -> coordinate = x to y + num
                    'E' -> coordinate = x + num to y
                    'W' -> coordinate = x - num to y
                    'S' -> coordinate = x to y - num
                    'F' -> coordinate = goForwardPart1(coordinate, num, facing)
                    'L' -> facing = newDirection(facing, -1 * num)
                    'R' -> facing = newDirection(facing, num)
                }
            }

            return abs(coordinate.first) + abs(coordinate.second)
        }

        fun solvePartTwo(input: List<Instruction>): Int {
            // east to north
            var wayPoint = 10 to 1
            var ship = 0 to 0

            input.forEach { instruction ->
                val (wayX, wayY) = wayPoint
                val num = instruction.number
                when (instruction.letter) {
                    'N' -> wayPoint = wayX to wayY + num
                    'E' -> wayPoint = wayX + num to wayY
                    'W' -> wayPoint = wayX - num to wayY
                    'S' -> wayPoint = wayX to wayY - num
                    'F' -> ship = goForwardPart2(ship, wayPoint, num)
                    'L' -> wayPoint = newDirectionPart2(wayPoint, -1 * num)
                    'R' -> wayPoint = newDirectionPart2(wayPoint, num)
                }
            }

            return abs(ship.first) + abs(ship.second)
        }

        fun goForwardPart2(
            shipPosition: Pair<Int, Int>,
            wayPointPosition: Pair<Int, Int>,
            number: Int
        ): Pair<Int, Int> {
            return shipPosition.first + (wayPointPosition.first * number) to
                shipPosition.second + (wayPointPosition.second * number)
        }

        fun goForwardPart1(currentPosition: Pair<Int, Int>, distanceToTravel: Int, direction: Char): Pair<Int, Int> {
            val (x, y) = currentPosition
            return when (direction) {
                'N' -> x to y + distanceToTravel
                'E' -> x + distanceToTravel to y
                'W' -> x - distanceToTravel to y
                'S' -> x to y - distanceToTravel
                else -> currentPosition
            }
        }

        fun newDirectionPart2(wayPoint: Pair<Int, Int>, angle: Int): Pair<Int, Int> {
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

        private fun newDirection(currentDirection: Char, angle: Int): Char {
            val direction = "NESW"
            val shiftPositions = angle / 90
            val currentIndex = direction.indexOf(currentDirection)

            var newIndex = (currentIndex + shiftPositions) % 4
            while (newIndex < 0) newIndex += 4

            return direction[newIndex]
        }

    }
}

data class Instruction(val input: String) {
    val letter: Char = input[0]
    val number: Int = input.substring(1 until input.length).toInt()
}
