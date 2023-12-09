#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 9: https://adventofcode.com/2023/day/9
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var sumOfPredictions = 0

while (scanner.hasNext()) {
    var measurements = scanner.nextLine().split(" ").map { it.toInt() }
    
    val lastValues = mutableListOf<Int>()
    lastValues.add(measurements.last())

    var numOfExtrapolatedZeroes = 0
    val extrapolations = mutableListOf<Int>()
    extrapolations.addAll(measurements)

    while (numOfExtrapolatedZeroes < extrapolations.size - 1) {
        numOfExtrapolatedZeroes = 0
        val nextExtrapolations = mutableListOf<Int>()

        for (i in 0 until extrapolations.size - 1) {
            val extrapolation = extrapolations[i + 1] - extrapolations[i]
            if (extrapolation == 0) {
                numOfExtrapolatedZeroes++
            }
            nextExtrapolations.add(extrapolation)
        }

        extrapolations.clear()
        extrapolations.addAll(nextExtrapolations)

        lastValues.add(extrapolations.last())
    }

    val prediction = lastValues.sum()
    sumOfPredictions += prediction
}

println("Sum of predictions: $sumOfPredictions")
