#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 2: https://adventofcode.com/2023/day/2
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val RED_CUBES_COUNT = 12
val GREEN_CUBES_COUNT = 13
val BLUE_CUBES_COUNT = 14

var possibleGamesIDsSum = 0

while (scanner.hasNext()) {
    val game = scanner.nextLine().split(": ")

    val gameID = game[0].split(" ")[1].toInt()

    val subsets = game[1].split("; ")

    if (isGamePossible(subsets)) {
        possibleGamesIDsSum += gameID
    }
}

println("Possible games IDs sum: $possibleGamesIDsSum")

fun isGamePossible(subsets: List<String>): Boolean {
    var isPossible = true
    var i = 0

    while (i < subsets.size && isPossible) {
        val subset = subsets[i]

        val cubesInfo = subset.split(", ")

        var j = 0
        while (j < cubesInfo.size && isPossible) {
            val info = cubesInfo[j].split(" ")
            
            val cubeCount = info[0].toInt()
            val cubeColor = info[1]

            if (
                (cubeColor == "red" && cubeCount > RED_CUBES_COUNT) ||
                (cubeColor == "green" && cubeCount > GREEN_CUBES_COUNT) ||
                (cubeColor == "blue" && cubeCount > BLUE_CUBES_COUNT)
            ) {
                isPossible = false
            }
            j++
        }

        i++
    }

    return isPossible
}

