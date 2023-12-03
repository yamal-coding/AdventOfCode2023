#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 3: https://adventofcode.com/2023/day/3
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())


data class NumberCoordinate(
    val number: Int,
    val row: Int,
    val columns: List<Int>,
)

var row = 0
val symbolsCoordinates = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
val numberCoordinates = mutableListOf<NumberCoordinate>()
var maxColumns = 0

while (scanner.hasNext()) {
    val parts = scanner.nextLine().toCharArray()

    var column = 0
    var shouldConcat = false
    var currentNumber = ""
    val currentNumberColumns = mutableListOf<Int>()

    while (column < parts.size) {
        val part = parts[column]

        when {
            part.isDigit() -> {
                if (!shouldConcat) {
                    shouldConcat = true
                }

                currentNumber += part
                currentNumberColumns.add(column)
            }
            else -> {
                if (shouldConcat) {
                    shouldConcat = false
                    val number = currentNumber.toInt()
                    currentNumber = ""
                    numberCoordinates.add(
                        NumberCoordinate(
                            number = number,
                            row = row,
                            columns = currentNumberColumns.toList()
                        )
                    )
                    currentNumberColumns.clear()
                }

                if (part == '*') {
                    symbolsCoordinates[row to column] = mutableListOf()
                }
            }
        }
        column++
    }

    if (maxColumns == 0) {
        maxColumns = column
    }

    if (shouldConcat) {
            val number = currentNumber.toInt()
            numberCoordinates.add(
                NumberCoordinate(
                    number = number,
                    row = row,
                    columns = currentNumberColumns.toList()
                )
            )
        }

    row++
}

val maxRows = row

numberCoordinates.forEach { numberCoordinate ->
    val number = numberCoordinate.number
    val numberRow = numberCoordinate.row
    val columns = numberCoordinate.columns

    // left
    if (columns[0] - 1 >= 0 && symbolsCoordinates.contains(numberRow to columns[0] - 1)) {
        symbolsCoordinates[numberRow to columns[0] - 1]?.add(number)
    }

    // top left corner
    if (numberRow - 1 >= 0 && columns[0] - 1 >= 0 
        && symbolsCoordinates.contains(numberRow - 1 to columns[0] - 1)) {
        symbolsCoordinates[numberRow - 1 to columns[0] - 1]?.add(number)
    }

    // bottom left corner
    if (numberRow + 1 < maxRows && columns[0] - 1 >= 0 
        && symbolsCoordinates.contains(numberRow + 1 to columns[0] - 1)) {
        symbolsCoordinates[numberRow + 1 to columns[0] - 1]?.add(number)
    }

    // top and bottom edges
    var i = 0
    while (i < columns.size) {
        // top
        if (numberRow - 1 >= 0 && symbolsCoordinates.contains(numberRow - 1 to columns[i])) {
            symbolsCoordinates[numberRow - 1 to columns[i]]?.add(number)
        }

        // bottom
        if (numberRow + 1 < maxRows && symbolsCoordinates.contains(numberRow + 1 to columns[i])) {
            symbolsCoordinates[numberRow + 1 to columns[i]]?.add(number)
        }

        i++
    }

    // top right corner
    if (numberRow - 1 >= 0 && columns[columns.size - 1] + 1 < maxColumns 
        && symbolsCoordinates.contains(numberRow - 1 to columns[columns.size - 1] + 1)) {
        symbolsCoordinates[numberRow - 1 to columns[columns.size - 1] + 1]?.add(number)
    }

    // bottom right corner
    if (numberRow + 1 < maxRows && columns[columns.size - 1] + 1 < maxColumns 
        && symbolsCoordinates.contains(numberRow + 1 to columns[columns.size - 1] + 1)) {
        symbolsCoordinates[numberRow + 1 to columns[columns.size - 1] + 1]?.add(number)
    }

    // right
    if (columns[columns.size - 1] + 1 < maxColumns 
        && symbolsCoordinates.contains(numberRow to columns[columns.size - 1] + 1)) {
        symbolsCoordinates[numberRow to columns[columns.size - 1] + 1]?.add(number)
    }
}

var gearRatiosSum = 0
symbolsCoordinates.asSequence().forEach {
    val adjacentPartsToGear = it.value
    if (adjacentPartsToGear.size == 2) {
        gearRatiosSum += adjacentPartsToGear[0] * adjacentPartsToGear[1]
    } 
}

println("Gear ratios sum is $gearRatiosSum")