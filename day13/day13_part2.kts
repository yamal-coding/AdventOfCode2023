#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 13: https://adventofcode.com/2023/day/13
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

	val horizontalReflectionPosition = getHorizontalReflectionPosition(mirror, width)

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

fun getHorizontalReflectionFromTheStart(mirror: List<String>, width: Int, start: Int): Int? {
	if (start >= mirror.size - 1) {
		return null
	}

	var firstReflectedHorizontalRow = start
	var reflectedPosition = mirror.size - 1
	var found = false
	var withSmudge = false

	// fetch from top to bottom
	while (firstReflectedHorizontalRow < reflectedPosition && !found) {
		var i = 0
		var differences = 0

		while (i < width) {
			if (mirror[firstReflectedHorizontalRow][i] != mirror[reflectedPosition][i]) {
				differences++
			}
			i++
		}

		if (differences <= 1) {
			found = true
			withSmudge = differences == 1
			reflectedPosition--
		}
		firstReflectedHorizontalRow++
	}

	return if (found) {
		val nextPossibleStart = firstReflectedHorizontalRow
		// every other horizontal row needs to be reflected
		var isMirror = true
		var start = firstReflectedHorizontalRow
		var end = reflectedPosition

		while (firstReflectedHorizontalRow < reflectedPosition && isMirror) {
			var i = 0
			var differences = 0
			while (i < width) {
				if (mirror[firstReflectedHorizontalRow][i] != mirror[reflectedPosition][i]) {
					differences++
				}
				i++
			}

			if (differences > 1) {
				isMirror = false
			} else if (differences == 1) {
				if (withSmudge) {
					isMirror = false
				} else {
					withSmudge = true
				}
			}

			firstReflectedHorizontalRow++
			reflectedPosition--
		}

		if (firstReflectedHorizontalRow == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalRow.takeIf { isMirror && withSmudge } 
			?: getHorizontalReflectionFromTheStart(mirror, width, nextPossibleStart)
	} else {
		null
	}
}

fun getHorizontalReflectionFromTheEnd(mirror: List<String>, width: Int, end: Int): Int? {
	if (end <= 0) {
		return null
	}

	var firstReflectedHorizontalRow = 0
	var reflectedPosition = end
	var found = false
	var withSmudge = false

	while (reflectedPosition > firstReflectedHorizontalRow && !found) {
		var i = 0
		var differences = 0

		while (i < width) {
			if (mirror[firstReflectedHorizontalRow][i] != mirror[reflectedPosition][i]) {
				differences++
			}
			i++
		}

		if (differences <= 1) {
			found = true
			withSmudge = differences == 1
			firstReflectedHorizontalRow++
		}
		reflectedPosition--
	}

	return if (found) {
		val nextPossibleEnd = reflectedPosition
		// every other horizontal row needs to be reflected
		var isMirror = true
		var start = firstReflectedHorizontalRow
		var end = reflectedPosition

		while (firstReflectedHorizontalRow < reflectedPosition && isMirror) {
			var i = 0
			var differences = 0
			while (i < width) {
				if (mirror[firstReflectedHorizontalRow][i] != mirror[reflectedPosition][i]) {
					differences++
				}
				i++
			}

			if (differences > 1) {
				isMirror = false
			} else if (differences == 1) {
				if (withSmudge) {
					isMirror = false
				} else {
					withSmudge = true
				}
			}

			firstReflectedHorizontalRow++
			reflectedPosition--
		}

		if (firstReflectedHorizontalRow == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalRow.takeIf { isMirror && withSmudge } 
			?: getHorizontalReflectionFromTheEnd(mirror, width, nextPossibleEnd)
	} else {
		null
	}
}

fun getHorizontalReflectionPosition(mirror: List<String>, width: Int): Int? =
	getHorizontalReflectionFromTheStart(mirror, width, 0)
		?: getHorizontalReflectionFromTheEnd(mirror, width, mirror.size - 1)

fun getVerticalReflectionPositionFromTheStart(mirror: List<String>, width: Int, start: Int): Int? {
	
	if (start >= width) {
		return null
	}

	var firstReflectedHorizontalColumn = start
	var reflectedPosition = width - 1
	var found = false
	var withSmudge = false
	
	// fetch from left to right
	while (firstReflectedHorizontalColumn < reflectedPosition && !found) {
		var i = 0
		var differences = 0
		while (i < mirror.size) {
			if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
				differences++
			}
			i++
		}
		if (differences <= 1) {
			found = true
			withSmudge = differences == 1
			reflectedPosition--
		}
		firstReflectedHorizontalColumn++
	}

	return if (found) {
		val nextPossibleStart = firstReflectedHorizontalColumn
		// every other horizontal row needs to be reflected
		var isMirror = true

		while (firstReflectedHorizontalColumn < reflectedPosition && isMirror) {
			var i = 0
			var differences = 0

			while (i < mirror.size) {
				if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
					differences++
				}
				i++
			}	

			if (differences > 1) {
				isMirror = false
			} else if (differences == 1) {
				if (withSmudge) {
					isMirror = false
				} else {
					withSmudge = true
				}
			}

			firstReflectedHorizontalColumn++
			reflectedPosition--
		}

		if (firstReflectedHorizontalColumn == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalColumn.takeIf { isMirror && withSmudge } 
			?: getVerticalReflectionPositionFromTheStart(mirror, width, nextPossibleStart)
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
	var withSmudge = false

	while (reflectedPosition > firstReflectedHorizontalColumn && !found) {
		var i = 0
		var differences = 0
		while (i < mirror.size) {
			if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
				differences++
			}
			i++
		}
		if (differences <= 1) {
			found = true
			withSmudge = differences == 1
			firstReflectedHorizontalColumn++
		}
		reflectedPosition--
	}

	return if (found) {
		val nextPossibleEnd = reflectedPosition
		// every other horizontal row needs to be reflected
		var isMirror = true

		while (firstReflectedHorizontalColumn < reflectedPosition && isMirror) {
			var i = 0
			var differences = 0

			while (i < mirror.size) {
				if (mirror[i][firstReflectedHorizontalColumn] != mirror[i][reflectedPosition]) {
					differences++
				}
				i++
			}	
			
			if (differences > 1) {
				isMirror = false
			} else if (differences == 1) {
				if (withSmudge) {
					isMirror = false
				} else {
					withSmudge = true
				}
			}

			firstReflectedHorizontalColumn++
			reflectedPosition--
		}
		
		if (firstReflectedHorizontalColumn == reflectedPosition) {
			isMirror = false
		}

		firstReflectedHorizontalColumn.takeIf { isMirror && withSmudge } 
			?: getVerticalReflectionPositionFromTheEnd(mirror, width, nextPossibleEnd)
	} else {
		null
	}
}

fun getVerticalReflectionPosition(mirror: List<String>, width: Int): Int? =
	getVerticalReflectionPositionFromTheStart(mirror, width, 0) ?: 
		getVerticalReflectionPositionFromTheEnd(mirror, width, width - 1)
