#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 8: https://adventofcode.com/2023/day/8
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

enum class Direction(val value: Char) {
    LEFT('L'),
    RIGHT('R');

    companion object {
        fun fromValue(value: Char): Direction =
            values().first { it.value == value}
    }
}

val steps = scanner.nextLine().toCharArray().map { Direction.fromValue(it) }

// empty line
scanner.nextLine()

val network = mutableMapOf<String, Pair<String, String>>()
val currentNodes = mutableListOf<String>()

while (scanner.hasNext()) {
    val rawNode = scanner.nextLine().split(" = ")
    val node = rawNode[0]
    val rawConnections = rawNode[1].substring(1, rawNode[1].length - 1).split(", ")
    val connections = rawConnections[0] to rawConnections[1]

    if (node[2] == 'A') {
        currentNodes.add(node)
    }
    network[node] = connections
}

var minStepsToZ = mutableListOf<Long>()
for (i in 0 until currentNodes.size) {
    var j = 0
    var currentNode = currentNodes[i]
    var stepsToZ = 0L

    while (currentNode[2] != 'Z') {
        currentNode = when (steps[j]) {
            Direction.RIGHT -> {
                network[currentNode]!!.second
            }
            Direction.LEFT -> {
                network[currentNode]!!.first
            }
        }
        j = (j + 1) % steps.size
        stepsToZ++
    }
    minStepsToZ.add(stepsToZ)
}

val numOfSteps = minStepsToZ.fold(minStepsToZ[0]) { acc, nextMinsStepsToZ -> lcm(acc, nextMinsStepsToZ) }
println("Num of steps: $numOfSteps")

fun lcm(a: Long, b: Long): Long =
    a * (b / gcd(a, b))
    
fun gcd(a: Long, b: Long): Long {
    var _b = b
    var _a = a
    var t = 0L

    while (_b != 0L) {
        t = _b
        _b = _a % _b
        _a = t
    }
    
    return _a
}
