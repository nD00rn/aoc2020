package Day17

import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val testData = Day17.loadInput("./src/Day17/Day17.test")
    val realData = Day17.loadInput("./src/Day17/Day17.input")

    println("testData = $testData")

    // 112
    println("day 17 p1 - test data - ${Day17.solvePartOne(testData)}")

    // 380
    println("day 17 p1 - real data - ${Day17.solvePartOne(realData)}")

    // 848
//    println("day 17 p2 - test data - ${Day17.solvePartTwo(testData)}")
}

class Day17 {
    companion object {

        fun loadInput(path: String): List<String> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8)
        }

        fun solvePartOne(initialState: List<String>): Int {
            val cube = Cube(initialState)

            repeat(6) {
                cube.tick(
                    inactiveToActive = { list -> list.filter { it == State.Active }.count() == 3 },
                    activeToInactive = { list -> list.filter { it == State.Active }.count() !in 2..3 }
                )
            }

            return cube.count()
        }

    }
}

data class Cube(val initialState: List<String>) {
    private var activePoints: MutableList<Point> = mutableListOf()

    init {
        initialState.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { charIndex, char ->
                if (char == State.Active.c) {
                    activePoints.add(Point(charIndex, lineIndex, 0))
                }
            }
        }
    }

    fun count(): Int {
        return activePoints.size
    }

    fun tick(
        inactiveToActive: (List<State>) -> Boolean,
        activeToInactive: (List<State>) -> Boolean
    ) {
        val pointsToCheck = mutableSetOf<Point>()
        activePoints.forEach { point ->
            pointsToCheck.addAll(getCoordinatesInRange(point))
        }

        println("Points to check | ${pointsToCheck.size}")

        val newPoints = mutableListOf<Point>()
        pointsToCheck.forEach { point ->
            val neighborStates = getNeighbourStates(point)

            when (getState(point)) {
                State.Active -> if (!activeToInactive.invoke(neighborStates)) {
                    newPoints.add(point)
                }

                State.Inactive -> if (inactiveToActive.invoke(neighborStates)) {
                    newPoints.add(point)
                }
            }
        }

        activePoints = newPoints
    }

    private fun getNeighbourStates(point: Point): List<State> {
        return getCoordinatesInRange(point)
            .filter { it != point }
            .map { getState(it) }
    }

    private fun getCoordinatesInRange(point: Point, range: Int = 1): List<Point> {
        val output = mutableListOf<Point>()

        val (x, y, z) = point

        for (xx in (x - range)..(x + range)) {
            for (yy in (y - range)..(y + range)) {
                for (zz in (z - range)..(z + range)) {
                    output.add(Point(xx, yy, zz))
                }
            }
        }

        return output.toList()
    }

    private fun getState(point: Point): State {
        return if (point in activePoints)
            State.Active
        else
            State.Inactive
    }

    override fun toString(): String {
        return activePoints.toString()
    }
}

data class Point(val x: Int, val y: Int, val z: Int)

enum class State(val c: Char) {
    Active('#'),
    Inactive('.'),
}
