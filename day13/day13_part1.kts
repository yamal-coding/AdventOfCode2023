#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 13: https://adventofcode.com/2023/day/13
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var sumOfRows = 0

var index = 0
while (scanner.hasNext()) {
	var mirrorRow = ""
	var width = 0

	val mirror = mutableListOf<String>()

	do {
		mirrorRow = scanner.nextLine()
		
		if (width == 0) {
			width = mirrorRow.length
		}

		if (mirrorRow.isNotEmpty()) {
			mirror.add(mirrorRow)
		}

	} while (mirrorRow.isNotEmpty() && scanner.hasNext())

	val horizontalReflectionPosition = getHorizontalReflectionPosition(mirror)

	if (horizontalReflectionPosition != null) {
		sumOfRows += horizontalReflectionPosition * 100
	} else {
		val verticalReflectionPosition = getVerticalReflectionPosition(mirror, width)
		if (verticalReflectionPosition != null) {
			sumOfRows += verticalReflectionPosition
		}
	}
}

println("Sum of rows: $sumOfRows")


fun getHorizontalReflectionFromTheStart(mirror: List<String>, start: Int): Int? {
	if (start >= mirror.size - 1) {
		return null
	}

	var firstReflectedHorizontalRow = start
	var reflectedPosition = mirror.size - 1
	var found = false

	// fetch from top to bottom
	while (firstReflectedHorizontalRow < reflectedPosition && !found) {
		if (mirror[firstReflectedHorizontalRow] == mirror[reflectedPosition]) {
			found = true
		} else {
			firstReflectedHorizontalRow++
		}
	}

	return if (found) {
		val nextPossibleStart = firstReflectedHorizontalRow
		// every other horizontal row needs to be reflected
		var isMirror = true
		var start = firstReflectedHorizontalRow
		var end = reflectedPosition

		while (firstReflectedHorizontalRow < reflectedPosition && isMirror) {
			if (mirror[firstReflectedHorizontalRow] != mirror[reflectedPosition]) {
				isMirror = false
			}
			firstReflectedHorizontalRow++
			reflectedPosition--
		}

		if (firstReflectedHorizontalRow == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalRow.takeIf { isMirror } 
			?: getHorizontalReflectionFromTheStart(mirror, nextPossibleStart + 1)
	} else {
		null
	}
}

fun getHorizontalReflectionFromTheEnd(mirror: List<String>, end: Int): Int? {
	if (end <= 0) {
		return null
	}

	var firstReflectedHorizontalRow = 0
	var reflectedPosition = end
	var found = false

	while (reflectedPosition > firstReflectedHorizontalRow && !found) {
		if (mirror[firstReflectedHorizontalRow] == mirror[reflectedPosition]) {
			found = true
		} else {
			reflectedPosition--
		}
	}

	return if (found) {
		val nextPossibleEnd = reflectedPosition
		// every other horizontal row needs to be reflected
		var isMirror = true
		var start = firstReflectedHorizontalRow
		var end = reflectedPosition

		while (firstReflectedHorizontalRow < reflectedPosition && isMirror) {
			if (mirror[firstReflectedHorizontalRow] != mirror[reflectedPosition]) {
				isMirror = false
			}
			firstReflectedHorizontalRow++
			reflectedPosition--
		}

		if (firstReflectedHorizontalRow == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalRow.takeIf { isMirror } 
			?: getHorizontalReflectionFromTheEnd(mirror, nextPossibleEnd - 1)
	} else {
		null
	}
}

fun getHorizontalReflectionPosition(mirror: List<String>): Int? =
	getHorizontalReflectionFromTheStart(mirror, 0)
		?: getHorizontalReflectionFromTheEnd(mirror, mirror.size - 1)

fun getVerticalReflectionPositionFromTheStart(mirror: List<String>, width: Int, start: Int): Int? {
	
	if (start >= width) {
		return null
	}

	var firstReflectedHorizontalColumn = start
	var reflectedPosition = width - 1
	var found = false
	
	// fetch from left to right
	while (firstReflectedHorizontalColumn < reflectedPosition && !found) {
		var i = 0
		var reflected = true
		while (i < mirror.size && reflected) {
			if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
				reflected = false
			}
			i++
		}
		if (reflected) {
			found = true
		} else {
			firstReflectedHorizontalColumn++
		}
	}

	return if (found) {
		val nextPossibleStart = firstReflectedHorizontalColumn
		// every other horizontal row needs to be reflected
		var isMirror = true

		while (firstReflectedHorizontalColumn < reflectedPosition && isMirror) {
			var i = 0
			var reflected = true
			while (i < mirror.size && reflected) {
				
				if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
					
					reflected = false
				}
				i++
			}	
			
			if (!reflected) {
				isMirror = false
			}
			firstReflectedHorizontalColumn++
			reflectedPosition--
		}

		if (firstReflectedHorizontalColumn == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalColumn.takeIf { isMirror } 
			?: getVerticalReflectionPositionFromTheStart(mirror, width, nextPossibleStart + 1)
	} else {
		null
	}
}

fun getVerticalReflectionPositionFromTheEnd(mirror: List<String>, width: Int, end: Int): Int? {
	if (end <= 0) {
		return null
	}

	var firstReflectedHorizontalColumn = 0
	var reflectedPosition = end
	var found = false

	while (reflectedPosition > firstReflectedHorizontalColumn && !found) {
		var i = 0
		var reflected = true
		while (i < mirror.size && reflected) {
			if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
				reflected = false
			}
			i++
		}
		if (reflected) {
			found = true
		} else {
			reflectedPosition--
		}
	}

	return if (found) {
		val nextPossibleEnd = reflectedPosition
		// every other horizontal row needs to be reflected
		var isMirror = true

		while (firstReflectedHorizontalColumn < reflectedPosition && isMirror) {
			var i = 0
			var reflected = true
			while (i < mirror.size && reflected) {
				
				if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
					reflected = false
				}
				i++
			}	
			
			if (!reflected) {
				isMirror = false
			}
			firstReflectedHorizontalColumn++
			reflectedPosition--
		}
		
		if (firstReflectedHorizontalColumn == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalColumn.takeIf { isMirror } 
			?: getVerticalReflectionPositionFromTheEnd(mirror, width, nextPossibleEnd - 1)
	} else {
		null
	}
}

fun getVerticalReflectionPosition(mirror: List<String>, width: Int): Int? =
	getVerticalReflectionPositionFromTheStart(mirror, width, 0) ?: 
		getVerticalReflectionPositionFromTheEnd(mirror, width, width - 1)
