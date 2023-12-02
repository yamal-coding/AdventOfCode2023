#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 2: https://adventofcode.com/2023/day/2
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var sumOfPowers = 0

while (scanner.hasNext()) {
    val game = scanner.nextLine().split(": ")

    val subsets = game[1].split("; ")

    sumOfPowers += getPower(subsets) 
}

println("Sum of powers is $sumOfPowers")


fun getPower(subsets: List<String>): Int {
    var maxRedCount = 0
    var maxGreenCount = 0
    var maxBlueCount = 0
    
    subsets.forEach { subset ->
        val cubesInfo = subset.split(", ")

        cubesInfo.forEach {
            val info = it.split(" ")

            val cubeCount = info[0].toInt()
            val cubeColor = info[1]

            when (cubeColor) {
                "red" -> {
                    if (cubeCount > maxRedCount) {
                        maxRedCount = cubeCount
                    }
                }
                "green" -> {
                    if (cubeCount > maxGreenCount) {
                        maxGreenCount = cubeCount
                    }
                }
                "blue" -> {
                    if (cubeCount > maxBlueCount) {
                        maxBlueCount = cubeCount
                    }
                }
            }
        }
    }

    return maxRedCount * maxGreenCount * maxBlueCount
}

