#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 4: https://adventofcode.com/2023/day/4
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var totalPoints = 0

while (scanner.hasNext()) {
    val card = scanner.nextLine().split(" | ")
    val winningNumbers = card[0].split(": ")[1].split(" ").toSet()
    val candidateNumbers = card[1].split(" ").filter { it.isNotBlank() }

    var points = 0

    candidateNumbers.forEach { candidate ->
        if (winningNumbers.contains(candidate)) {
            points = if (points == 0) {
                1
            } else {
                points * 2
            }
        }
    }

    totalPoints += points
}

println("Total points: $totalPoints")