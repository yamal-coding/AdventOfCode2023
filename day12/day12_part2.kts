#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 12: https://adventofcode.com/2023/day/12
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val processedPossibilities = mutableMapOf<Pair<Int, Int>, Long>()

var sumOfPossibilities = 0L

while (scanner.hasNext()) {
	val rawRecords = scanner.nextLine().split(" ")
	val damagedRecords = "${rawRecords[0]}?${rawRecords[0]}?${rawRecords[0]}?${rawRecords[0]}?${rawRecords[0]}".toCharArray().toList()
	val foldedConsecutiveDamagedSprings = rawRecords[1].split(",").map { it.toInt() }
	val consecutiveDamagedSprings = buildList {
		addAll(foldedConsecutiveDamagedSprings)
		addAll(foldedConsecutiveDamagedSprings)
		addAll(foldedConsecutiveDamagedSprings)
		addAll(foldedConsecutiveDamagedSprings)
		addAll(foldedConsecutiveDamagedSprings)
	}

	processedPossibilities.clear()
	sumOfPossibilities += getPossibilities(damagedRecords, 0, consecutiveDamagedSprings, 0)
}

println("Sum of possibilities: $sumOfPossibilities")

fun getPossibilities(
	damagedRecords: List<Char>, 
	firstDamagedRecordPosition: Int, 
	consecutiveDamagedSprings: List<Int>,
	firstConsecutiveDamagedSpringPosition: Int,
): Long {
	if (firstDamagedRecordPosition >= damagedRecords.size 
		&& firstConsecutiveDamagedSpringPosition >= consecutiveDamagedSprings.size) {
		return 1L
	} else if (firstDamagedRecordPosition < damagedRecords.size
		&& firstConsecutiveDamagedSpringPosition >= consecutiveDamagedSprings.size){
		var remainingDamagedRecords = 0

		for (i in firstDamagedRecordPosition until damagedRecords.size) {
			if (damagedRecords[i] == '#') {
				remainingDamagedRecords++
			}
		}

		return if (remainingDamagedRecords == 0) {
			 1L
		} else {
			0L
		}
	}

	val nextConsecutiveSprings = consecutiveDamagedSprings[firstConsecutiveDamagedSpringPosition]
	var addedSprings = 0
	var discarded = false
	var possibilities = 0L
	var i = firstDamagedRecordPosition
	while (addedSprings < nextConsecutiveSprings && i < damagedRecords.size && !discarded) {
		val damagedRecord = damagedRecords[i]
		when (damagedRecord) {
			'.' -> {
				if (addedSprings == 0) {
					i++
				} else {
					discarded = true
					i++
				}
			}
			'#' -> {
				addedSprings++
				i++
			}
			'?' -> {
				if (addedSprings == 0) {
					if (!processedPossibilities.containsKey(i + 1 to firstConsecutiveDamagedSpringPosition)) {
						val remainingDamagedRecords = damagedRecords.size - 1 - i
						var remainingConsecutiveSprings = nextConsecutiveSprings - addedSprings
						val neededSpacesBetweenConsecutiveSprings = consecutiveDamagedSprings.size - 1 - firstConsecutiveDamagedSpringPosition
						for (j in firstConsecutiveDamagedSpringPosition + 1 until consecutiveDamagedSprings.size) {
							remainingConsecutiveSprings += consecutiveDamagedSprings[j]
						}
						var numOfEmptySpaces = 0
						for (j in i until damagedRecords.size) {
							if (damagedRecords[j] != '.') {
								numOfEmptySpaces++
							}
						}

						if (remainingDamagedRecords >= remainingConsecutiveSprings + neededSpacesBetweenConsecutiveSprings
							&& numOfEmptySpaces >= neededSpacesBetweenConsecutiveSprings) {
							possibilities = getPossibilities(
								damagedRecords, 
								i + 1, 
								consecutiveDamagedSprings,
								firstConsecutiveDamagedSpringPosition,
							).also {
								processedPossibilities[i + 1 to firstConsecutiveDamagedSpringPosition] = it
							}
						}
					} else {
						possibilities = processedPossibilities[i + 1 to firstConsecutiveDamagedSpringPosition] ?: 0L
					}
				}
				addedSprings++
				i++
			}
		}
	}

	if (!discarded) {
		if (addedSprings == nextConsecutiveSprings) {
			if (i < damagedRecords.size && damagedRecords[i] == '#') {
				discarded = true
			}
		} else {
			discarded = true
		}
	}

	return possibilities + if (!discarded) {
		if (!processedPossibilities.containsKey(i + 1 to firstConsecutiveDamagedSpringPosition + 1)) {
			val remainingDamagedRecords = damagedRecords.size - 1 - (i - 1)
			var remainingConsecutiveSprings = 0
			val neededSpacesBetweenConsecutiveSprings = consecutiveDamagedSprings.size - 1 - firstConsecutiveDamagedSpringPosition
			for (j in firstConsecutiveDamagedSpringPosition + 1 until consecutiveDamagedSprings.size) {
				remainingConsecutiveSprings += consecutiveDamagedSprings[j]
			}
			var numOfEmptySpaces = 0
			for (j in i - 1 until damagedRecords.size) {
				if (damagedRecords[j] != '.') {
					numOfEmptySpaces++
				}
			}

			if (remainingDamagedRecords >= remainingConsecutiveSprings + neededSpacesBetweenConsecutiveSprings
				&& numOfEmptySpaces >= neededSpacesBetweenConsecutiveSprings) {
					getPossibilities(damagedRecords, i + 1, consecutiveDamagedSprings, firstConsecutiveDamagedSpringPosition + 1).also {
						processedPossibilities[i + 1 to firstConsecutiveDamagedSpringPosition + 1] = it
					}
			} else {
				0L
			}
		} else {
			processedPossibilities[i + 1 to firstConsecutiveDamagedSpringPosition + 1] ?: 0L
		}
	} else {
		0L
	}
}




