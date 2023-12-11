#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import kotlin.math.abs

/*
 * Part 1 of day 11: https://adventofcode.com/2023/day/11
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())


val galaxyPositions = mutableListOf<Pair<Int, Int>>()
val emptyRowsIndexes = mutableSetOf<Int>()
val emptyColumnsIndexes = mutableSetOf<Int>()

var numOfColumns = 0

var x = 0
while (scanner.hasNext()) {
	val row = scanner.nextLine()

	if (numOfColumns == 0) {
		numOfColumns = row.length
	}

	var galaxiesInRowCount = 0
	var y = 0
	row.forEach { element ->
		if (element == '#') {
			galaxyPositions.add(x to y)
			galaxiesInRowCount++
		}
		y++
	}

	if (galaxiesInRowCount == 0) {
		emptyRowsIndexes.add(x)
	}

	x++
}

val galaxyColumns = galaxyPositions.map { it.second }.toSet()

for (col in 0 until numOfColumns) {
	if (!galaxyColumns.contains(col)) {
		emptyColumnsIndexes.add(col)	
	}
}

var shortestPathsSum = 0L

for (i in 0 until galaxyPositions.size) {
	val galaxyPosition = galaxyPositions[i]
	
	for (j in i + 1 until galaxyPositions.size) {
		val otherGalaxyPosition = galaxyPositions[j]
		val distance = abs(galaxyPosition.first - otherGalaxyPosition.first) + abs(galaxyPosition.second - otherGalaxyPosition.second)

		val rowsRange = if (galaxyPosition.first > otherGalaxyPosition.first) otherGalaxyPosition.first..galaxyPosition.first else galaxyPosition.first..otherGalaxyPosition.first
		val columnsRange = if (galaxyPosition.second > otherGalaxyPosition.second) otherGalaxyPosition.second..galaxyPosition.second else galaxyPosition.second..otherGalaxyPosition.second
		val numOfEmptyRowsBetweenThem = emptyRowsIndexes.count { it in rowsRange }
		val numOfEmptyColumnsBetweenThem = emptyColumnsIndexes.count { it in columnsRange }

		shortestPathsSum += distance + numOfEmptyRowsBetweenThem + numOfEmptyColumnsBetweenThem
	}
}

println("Shortest paths sum: $shortestPathsSum")
