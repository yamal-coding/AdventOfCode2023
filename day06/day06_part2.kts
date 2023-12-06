#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 6: https://adventofcode.com/2023/day/6
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val time = scanner.nextLine().split(":")[1].trim().split(" ")
    .filter { it.isNotBlank()}
    .map { it.trim() }
    .joinToString(separator = "")
    .toLong()

val record = scanner.nextLine().split(":")[1].trim().split(" ")
    .filter { it.isNotBlank()}
    .map { it.trim() }
    .joinToString(separator = "")
    .toLong()

var waysToWin = 0

for (holdTime in 0..time) {
    val distance = (time - holdTime) * holdTime
    if (distance > record) {
        waysToWin++
    }
}


println("Ways to win: $waysToWin")
