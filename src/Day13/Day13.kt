import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import java.util.stream.Stream

fun main() {
    val testData = Day13.loadInput("./src/Day13/Day13.test")
    val realData = Day13.loadInput("./src/Day13/Day13.input")
    val fakeData = Schedule(listOf("0", "17,x,13,19"))
    val fakeData2 = Schedule(listOf("0", "1789,37,47,1889"))

    // 295
    println("p1: test data is ${Day13.solvePartOne(testData)}")

    // 3464
    println("p1: real data is ${Day13.solvePartOne(realData)}")

    // 3417
    println("p2: fake data [1] is ${Day13.solvePartTwo(fakeData)}")

    // 1202161486
    println("p2: fake data [2] is ${Day13.solvePartTwo(fakeData2)}")

    // 1_068_781
    println("p2: test data is ${Day13.solvePartTwo(testData)}")

    // 760171380521445
    println("p2: real data is ${Day13.solvePartTwo(realData)}")
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

        fun solvePartTwo(schedule: Schedule): Long {
            val bus = schedule.getBusses()
            val firstMax = bus.sorted()[bus.size - 1]
            val secondMax = bus.sorted()[bus.size - 2]
            val thirdMax = bus.sorted()[bus.size - 3]

            val firstMaxIndex = schedule.busSchedules.indexOf(firstMax.toString())
            val secondMaxIndex = schedule.busSchedules.indexOf(secondMax.toString())
            val thirdMaxIndex = schedule.busSchedules.indexOf(thirdMax.toString())

            val(firstMeet, secondMeet) = Stream.iterate(1) { it + 1 }
                .filter { (it + firstMaxIndex) % firstMax == 0
                    && (it + secondMaxIndex) % secondMax == 0
                    && (it + thirdMaxIndex) % thirdMax == 0
                }
                .limit(2)
                .collect(Collectors.toList())

            println("$firstMeet, ${secondMeet - firstMeet}")

            return Stream.iterate((firstMeet).toLong()) { it + (secondMeet - firstMeet) }
                .parallel()
                .filter(schedule::isOnFancySchedule)
                .findFirst()
                .get()
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

    fun isOnFancySchedule(value: Long): Boolean {
        busSchedules.forEachIndexed { index, busId ->
            if (busId.matches(Regex("[0-9]+")) && (value + index.toLong()) % busId.toLong() != 0.toLong()) {
                return false
            }
        }
        return true
    }
}
