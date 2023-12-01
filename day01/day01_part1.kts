#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 1: https://adventofcode.com/2023/day/1
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())


var calibrationSum = 0

while (scanner.hasNext()) {
    val calibration = scanner.next()

    calibrationSum += getCalibrationNumber(calibration)
}

println("Total calibration is $calibrationSum")

fun getCalibrationNumber(calibration: String): Int {
    val iterator = calibration.iterator()
    var firstDigit: Int? = null
    var lastDigit: Int? = null

    while (iterator.hasNext()) {
        val nextChar = iterator.nextChar()
        if (nextChar.isDigit()) {
            val digit = nextChar.digitToInt()
            if (firstDigit == null) {
                firstDigit = digit
            } else {
                lastDigit = digit
            }
        }
    }

    if (lastDigit == null) {
        lastDigit = firstDigit
    }

    return firstDigit!! * 10 + lastDigit!!
}