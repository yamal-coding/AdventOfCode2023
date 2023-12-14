#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 12: https://adventofcode.com/2023/day/12
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var sumOfPossibilities = 0

while (scanner.hasNext()) {
	val raw = scanner.nextLine()
	val records = raw.split(" ")
	val damagedRecords = records[0].toCharArray().toList()
	val consecutiveDamagedSprings = records[1].split(",").map { it.toInt() }

	sumOfPossibilities += getPossibilities(damagedRecords, 0, consecutiveDamagedSprings, 0)
}

println("Sum of possibilities: $sumOfPossibilities")

fun getPossibilities(
	damagedRecords: List<Char>, 
	firstDamagedRecordPosition: Int, 
	consecutiveDamagedSprings: List<Int>,
	firstConsecutiveDamagedSpringPosition: Int,
): Int {
	if (firstDamagedRecordPosition >= damagedRecords.size 
		&& firstConsecutiveDamagedSpringPosition >= consecutiveDamagedSprings.size) {
		return 1
	} else if (firstDamagedRecordPosition < damagedRecords.size
		&& firstConsecutiveDamagedSpringPosition >= consecutiveDamagedSprings.size){
		var remainingDamagedRecords = 0

		for (i in firstDamagedRecordPosition until damagedRecords.size) {
			if (damagedRecords[i] == '#') {
				remainingDamagedRecords++
			}
		}

		return if (remainingDamagedRecords == 0) {
			 1
		} else {
			0
		}
	}

	val nextConsecutiveSprings = consecutiveDamagedSprings[firstConsecutiveDamagedSpringPosition]
	var addedSprings = 0
	var discarded = false
	var possibilities = 0
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
					possibilities = getPossibilities(
						damagedRecords,
						i + 1, 
						consecutiveDamagedSprings,
						firstConsecutiveDamagedSpringPosition,
					)
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
		getPossibilities(damagedRecords, i + 1, consecutiveDamagedSprings, firstConsecutiveDamagedSpringPosition + 1)
	} else {
		0
	}
}
