import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testData = Day13.loadInput("./src/Day13/Day13.test")
    val realData = Day13.loadInput("./src/Day13/Day13.input")

    println(testData)

    // 295
    println("p1: test data is ${Day13.solvePartOne(testData)}")

    // 3464
    println("p1: real data is ${Day13.solvePartOne(realData)}")
}

class Day13 {
    companion object {

        fun loadInput(path: String): Schedule {
            val input = Files.readAllLines(Paths.get(path), Charsets.UTF_8)
            return Schedule(input)
        }

        fun solvePartOne(schedule: Schedule): Int {
            val departureTime = schedule.departureTime

            val earliestBus = schedule.getBusses()
                .stream()
                .map { it to ((departureTime / it) + 1) * it }
                .min { p1, p2 -> p1.second.compareTo(p2.second) }
                .get()

            val (busId, busDeparture) = earliestBus
            val waitingTime = busDeparture - departureTime

            println("Earliest bus is at $earliestBus, bus id is $busId it leaves in $waitingTime")

            return busId * waitingTime
        }

    }
}

data class Schedule(val input: List<String>) {
    val departureTime: Int = input.first().toInt()
    val busSchedules: List<String> = input[1].split(",")

    fun getBusses(): List<Int> {
        return busSchedules.stream()
            .filter { it != "x" }
            .map { it.toInt() }
            .collect(Collectors.toList())
    }
}
