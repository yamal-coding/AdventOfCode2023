#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 6: https://adventofcode.com/2023/day/6
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var multipliedWaysToWinCount = 1

val times = scanner.nextLine().split(":")[1].trim().split(" ").filter { it.isNotBlank()}.map { it.trim().toInt() }
val records = scanner.nextLine().split(":")[1].trim().split(" ").filter { it.isNotBlank()}.map { it.trim().toInt() }

for (i in 0 until times.size) {
    val time = times[i]
    val record = records[i]

    var waysToWin = 0

    for (holdTime in 1 until time) {
        val distance = (time - holdTime) * holdTime
        if (distance > record) {
            waysToWin++
        }
    }

    multipliedWaysToWinCount *= waysToWin
}


println("Multiplied ways to win: $multipliedWaysToWinCount")
