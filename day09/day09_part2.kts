#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 9: https://adventofcode.com/2023/day/9
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var sumOfPredictions = 0

while (scanner.hasNext()) {
    var measurements = scanner.nextLine().split(" ").map { it.toInt() }
    
    val firstValues = mutableListOf<Int>()
    firstValues.add(measurements.first())

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

        firstValues.add(extrapolations.first())
    }

    var prediction = 0
    for (i in firstValues.size downTo 1) {
        prediction = firstValues[i - 1] - prediction
    }
    sumOfPredictions += prediction
}

println("Sum of predictions: $sumOfPredictions")
