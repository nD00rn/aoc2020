import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

fun main() {
    val testData = Day14.loadFile("./src/Day14/Day14.test")
    val testData2 = Day14.loadFile("./src/Day14/Day14.test2")
    val realData = Day14.loadFile("./src/Day14/Day14.input")

    // p1 test data - 165
    println("d14p1 test - ${Day14.solvePartOne(testData)}")

    // p1 real data - 12408060320841
    println("d14p1 real - ${Day14.solvePartOne(realData)}")

    // p2 test data2 - 208
    println("d14p2 test - ${Day14.solvePartTwo(testData2)}")

    // p2 real data -
    println("d14p2 real - ${Day14.solvePartTwo(realData)}")
}

private class Day14 {
    companion object {

        fun loadFile(path: String): List<DockOperation> {
            return Files.readAllLines(Paths.get(path), Charsets.UTF_8).stream()
                .map { convertToOperation(it) }
                .collect(Collectors.toList())
        }

        fun solvePartOne(input: List<DockOperation>): Long {
            val program = DockProgram()

            input.forEach {
                when (it) {
                    is MaskOperation -> program.mask = it
                    is MemoryOperation -> {
                        val value = it.value or program.mask.orMap and program.mask.andMap
                        program.memoryBank[it.address] = value
                    }
                }
            }

            return program.getTotal()
        }

        fun solvePartTwo(input: List<DockOperation>): Long {
            val program = DockProgram()

            input.forEach { operation ->
                when (operation) {
                    is MaskOperation -> program.mask = operation
                    is MemoryOperation -> {
                        val v = operation.address.toString(2)
                        val maskedAddress = v and program.mask.mask

                        getNodes(maskedAddress)
                            .getValues()
                            .map { it.toLong(2) }
                            .forEach { program.memoryBank[it] = operation.value }
                    }
                }
            }

            return program.getTotal()
        }

        fun getNodes(input: String): Node {
            val firstNode = Node(0)

            input.forEach {
                when (it) {
                    '0' -> firstNode.insert(listOf(0))
                    '1' -> firstNode.insert(listOf(1))
                    'X' -> firstNode.insert(listOf(0, 1))
                    else -> {
                        println("I SHOULD NOT BE CALLED")
                        Node(0)
                    }
                }
            }

            return firstNode
        }

        private fun convertToOperation(input: String): DockOperation {
            val (action, value) = input.split(" = ")

            return when (action.trim()) {
                "mask" -> MaskOperation(value)
                else -> {
                    val address = action.substringAfter('[').substringBefore(']').toLong()
                    MemoryOperation(address, value.toLong())
                }
            }
        }

    }
}

private infix fun String.and(mask: String): String {
    val fullString = "0".repeat(mask.length - this.length) + this

    var output = ""
    fullString.forEachIndexed { index, c ->
        val right = mask[index]

        output += when (right) {
            '0' -> c
            '1' -> '1'
            'X' -> 'X'
            else -> '_'
        }
    }

    return output
}

private data class DockProgram(var mask: MaskOperation = MaskOperation("X".repeat(31))) {
    val memoryBank = mutableMapOf<Long, Long>()

    fun getTotal(): Long {
        return memoryBank.values.stream()
            .reduce(0) { a, b -> a + b }
    }
}

private sealed class DockOperation

private data class MaskOperation(val mask: String) : DockOperation() {
    // X -> ignore
    // 1 -> always set 1
    // 0 -> always set 0

    val andMap = mask.replace('X', '1').toLong(2)
    val orMap = mask.replace('X', '0').toLong(2)
}

private data class MemoryOperation(val address: Long, val value: Long) : DockOperation()

private data class Node(val value: Int) {
    val children = mutableListOf<Node>()

    fun insert(nodes: List<Int>) {
        if (children.isEmpty()) {
            children.addAll(nodes.map { Node(it) })
        } else {
            children.stream()
                .forEach { child -> child.insert(nodes) }
        }
    }

    fun getValues(): List<String> {
        return if (children.isEmpty()) {
            listOf(value.toString(10))
        } else {
            children.flatMap { it.getValues() }.map { "$value$it" }
        }
    }
}
