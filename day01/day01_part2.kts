#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 1: https://adventofcode.com/2023/day/1
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val ONE_NEXT_EXPECTED_CHARS = "ne".toCharArray()
val TWO_NEXT_EXPECTED_CHARS = "o".toCharArray()
val THREE_NEXT_EXPECTED_CHARS = "ree".toCharArray()
val FOUR_NEXT_EXPECTED_CHARS = "ur".toCharArray()
val FIVE_NEXT_EXPECTED_CHARS = "ve".toCharArray()
val SIX_NEXT_EXPECTED_CHARS = "x".toCharArray()
val SEVEN_NEXT_EXPECTED_CHARS = "ven".toCharArray()
val EIGHT_NEXT_EXPECTED_CHARS = "ight".toCharArray()
val NINE_NEXT_EXPECTED_CHARS = "ine".toCharArray()

var calibrationSum = 0

while (scanner.hasNext()) {
    val calibration = scanner.next()
    calibrationSum += getCalibrationNumber(calibration.toCharArray())
}

println("Total calibration is $calibrationSum")

fun getCalibrationNumber(text: CharArray): Int {
    var currentCharIndex = 0
    var firstDigit: Int? = null
    var lastDigit: Int? = null

    fun updateFirstOrLastDigit(digit: Int) {
        if (firstDigit == null) {
            firstDigit = digit
        } else {
            lastDigit = digit
        }
    }

    fun getPotentialWrittenDigit(expectedDigit: Int, nextExpectedCharsIndex: Int, nextExpectedChars: CharArray): Int? =
        if (currentCharIndex < text.size && nextExpectedCharsIndex < nextExpectedChars.size) {
            if (text[currentCharIndex++] == nextExpectedChars[nextExpectedCharsIndex]) {
                getPotentialWrittenDigit(expectedDigit, nextExpectedCharsIndex + 1, nextExpectedChars)
            } else {
                currentCharIndex--
                null
            }
        } else if (nextExpectedCharsIndex == nextExpectedChars.size) {
            expectedDigit
        } else {
            null
        }

    fun Int?.undoAttemptIfNull(): Int? = apply {
        if (this == null) {
            currentCharIndex--
        }
    }
    
    while (currentCharIndex < text.size) {
        val currentChar = text[currentCharIndex++]

        if (currentChar.isDigit()) {
            updateFirstOrLastDigit(currentChar.digitToInt())
        } else {
            val digit = when (currentChar) {
                'o' -> { // one
                    getPotentialWrittenDigit(1, 0, ONE_NEXT_EXPECTED_CHARS)
                }
                't' -> { // two and three
                    if (currentCharIndex < text.size) {
                        when (text[currentCharIndex++]) {
                            'w' -> getPotentialWrittenDigit(2, 0, TWO_NEXT_EXPECTED_CHARS)
                            'h' -> getPotentialWrittenDigit(3, 0, THREE_NEXT_EXPECTED_CHARS)
                            else -> null
                        }.undoAttemptIfNull()
                    } else {
                        null
                    }
                }
                'f' -> { // four and five
                    if (currentCharIndex < text.size) {
                        when (text[currentCharIndex++]) {
                            'o' -> getPotentialWrittenDigit(4, 0, FOUR_NEXT_EXPECTED_CHARS)
                            'i' -> getPotentialWrittenDigit(5, 0, FIVE_NEXT_EXPECTED_CHARS)
                            else -> null
                        }.undoAttemptIfNull()
                    } else {
                        null
                    }
                }
                's' -> { // six and seven
                    if (currentCharIndex < text.size) {
                        when (text[currentCharIndex++]) {
                            'i' -> getPotentialWrittenDigit(6, 0, SIX_NEXT_EXPECTED_CHARS)
                            'e' -> getPotentialWrittenDigit(7, 0, SEVEN_NEXT_EXPECTED_CHARS)
                            else -> null
                        }.undoAttemptIfNull()
                    } else {
                        null
                    }
                }
                'e' -> { // eight
                    getPotentialWrittenDigit(8, 0, EIGHT_NEXT_EXPECTED_CHARS)
                }
                'n' -> { // nine
                    getPotentialWrittenDigit(9, 0, NINE_NEXT_EXPECTED_CHARS)
                }
                else -> {
                    null
                }
            }

            if (digit != null) {
                updateFirstOrLastDigit(digit)
            }
        }
    }

    if (lastDigit == null) {
        lastDigit = firstDigit
    }

    return firstDigit!! * 10 + lastDigit!!
}