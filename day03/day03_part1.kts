#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 3: https://adventofcode.com/2023/day/3
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())


data class NumberCoordinate(
    val number: Int,
    val row: Int,
    val columns: List<Int>,
)

var sum = 0

var row = 0
val symbolsCoordinates = mutableSetOf<Pair<Int, Int>>()
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

                if (part != '.') {
                    symbolsCoordinates.add(row to column)
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
    var isValidPart = false
    val numberRow = numberCoordinate.row
    val columns = numberCoordinate.columns

    // left
    if (columns[0] - 1 >= 0 && symbolsCoordinates.contains(numberRow to columns[0] - 1)) {
        isValidPart = true
    }

    // top left corner
    if (!isValidPart && numberRow - 1 >= 0 && columns[0] - 1 >= 0 
        && symbolsCoordinates.contains(numberRow - 1 to columns[0] - 1)) {
        isValidPart = true
    }

    // bottom left corner
    if (!isValidPart && numberRow + 1 < maxRows && columns[0] - 1 >= 0 
        && symbolsCoordinates.contains(numberRow + 1 to columns[0] - 1)) {
        isValidPart = true
    }

    // top and bottom edges
    if (!isValidPart) {
        var i = 0
        while (i < columns.size && !isValidPart) {
            // top
            if (numberRow - 1 >= 0 && symbolsCoordinates.contains(numberRow - 1 to columns[i])) {
                isValidPart = true
            }

            // bottom
            if (!isValidPart && numberRow + 1 < maxRows && symbolsCoordinates.contains(numberRow + 1 to columns[i])) {
                isValidPart = true
            }

            i++
        }
    }

    // top right corner
    if (!isValidPart && numberRow - 1 >= 0 && columns[columns.size - 1] + 1 < maxColumns 
        && symbolsCoordinates.contains(numberRow - 1 to columns[columns.size - 1] + 1)) {
        isValidPart = true
    }

    // bottom right corner
    if (!isValidPart && numberRow + 1 < maxRows && columns[columns.size - 1] + 1 < maxColumns 
        && symbolsCoordinates.contains(numberRow + 1 to columns[columns.size - 1] + 1)) {
        isValidPart = true
    }

    // right
    if (columns[columns.size - 1] + 1 < maxColumns 
        && symbolsCoordinates.contains(numberRow to columns[columns.size - 1] + 1)) {
        isValidPart = true
    }

    if (isValidPart) {
        sum += numberCoordinate.number
    }
}

println("Sum is $sum")