import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testCode = Day08.loadInput("./src/Day08/Day08.test")
    val realCode = Day08.loadInput("./src/Day08/Day08.input")

    // 5
    println("The global value using test data is ${Day08.solvePartOne(testCode)}")

    // 1586
    println("The global value using real data is ${Day08.solvePartOne(realCode)}")

    //8
    println("The global value using test data for part two is ${Day08.solvePartTwo(testCode)}")

    // 703
    println("The global value using real data for part two is ${Day08.solvePartTwo(realCode)}")
}

class Day08 {
    companion object {

        fun loadInput(path: String): List<Operation> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8).stream()
                .map(::Operation)
                .collect(Collectors.toList())
        }

        fun solvePartOne(operations: List<Operation>): Int {
            val program = Program()
            val executedOperations: MutableSet<Int> = mutableSetOf()

            do {
                val currentOperation = operations[program.programCounter]
                executedOperations.add(program.programCounter)

                when (currentOperation.execution) {
                    "acc" -> {
                        program.globalValue += currentOperation.argument
                        program.programCounter += 1
                    }

                    "jmp" -> program.programCounter += currentOperation.argument

                    "nop" -> program.programCounter += 1
                }

            } while (program.programCounter !in executedOperations)

            return program.globalValue
        }

        fun solvePartTwo(operations: List<Operation>): Int {
            operations.forEachIndexed { index, operation ->
                val copy = operations.toMutableList()

                when (operation.execution) {
                    "nop" -> {
                        copy.removeAt(index)
                        copy.add(index, Operation("jmp ${operation.argument}"))
                    }

                    "jmp" -> {
                        copy.removeAt(index)
                        copy.add(index, Operation("nop ${operation.argument}"))
                    }

                    else -> {
                        return@forEachIndexed
                    }
                }

                val ranProgram = runProgram(copy)

                if (ranProgram.programCounter >= operations.size) {
                    return ranProgram.globalValue
                }
            }

            return -1
        }

        private fun runProgram(operations: List<Operation>): Program {
            val program = Program()
            val executedOperations: MutableSet<Int> = mutableSetOf()

            do {
                val currentOperation = operations[program.programCounter]
                executedOperations.add(program.programCounter)

                when (currentOperation.execution) {
                    "acc" -> {
                        program.globalValue += currentOperation.argument
                        program.programCounter += 1
                    }

                    "jmp" -> program.programCounter += currentOperation.argument

                    "nop" -> program.programCounter += 1
                }

                if (program.programCounter >= operations.size) {
                    return program
                }

            } while (program.programCounter !in executedOperations)

            println("Exit program counter is ${program.programCounter}")
            return program
        }
    }
}

data class Program(
    var globalValue: Int = 0,
    var programCounter: Int = 0
)

data class Operation(val input: String) {
    val execution: String
    val argument: Int

    init {
        val split = input.split(" ", limit = 2)
        execution = split[0].trim()
        argument = split[1].toInt()
    }

    override fun toString(): String {
        return "${execution}_${argument}"
    }
}
