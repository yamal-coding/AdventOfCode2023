#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 8: https://adventofcode.com/2023/day/8
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

while (scanner.hasNext()) {
    val rawNode = scanner.nextLine().split(" = ")
    val node = rawNode[0]
    val rawConnections = rawNode[1].substring(1, rawNode[1].length - 1).split(", ")
    val connections = rawConnections[0] to rawConnections[1]

    network[node] = connections
}

var currentNode = "AAA"
var numOfSteps = 0
var i = 0

while (currentNode != "ZZZ") {
    currentNode = when (steps[i]) {
        Direction.RIGHT -> {
            network[currentNode]!!.second
        }
        Direction.LEFT -> {
            network[currentNode]!!.first
        }
    }
    numOfSteps++
    i = (i + 1) % steps.size
}

println("Num of steps: $numOfSteps")
