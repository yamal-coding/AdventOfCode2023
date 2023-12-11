#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 10: https://adventofcode.com/2023/day/10
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

enum class Pipe(val value: Char) {
	VERTICAL('|'),
	HORIZONTAL('-'),
	NORTH_EAST('L'),
	NORTH_WEST('J'),
	SOUTH_WEST('7'),
	SOUTH_EAST('F');

	companion object {
		fun fromValue(value: Char): Pipe =
			values().first { it.value == value } 
	}
}

val pipes = mutableMapOf<Pair<Int, Int>, Pipe>()

var startX = 0
var startY = 0
val startPipe = Pipe.HORIZONTAL

var row = 0
while (scanner.hasNext()) {
	var column = 0
	scanner.nextLine().forEach { cell ->
		when (cell) {
			'S' -> {
				startX = row
				startY = column
				pipes[startX to startY] = startPipe
			}
			'.' -> {
				// skip ground
			}
			else -> {
				pipes[row to column] = Pipe.fromValue(cell)
			}
		}
		column++
	}
	row++
}

enum class Direction {
	NORTH, SOUTH, WEST, EAST;
}

val startPosition = startX to startY

var previousClockWiseDirection = Direction.EAST
var previousClockWisePipe = startPipe
var clockWisePosition = startX to (startY + 1)

var steps = 1

while (clockWisePosition != startPosition) {
	var nextClockWiseRow = clockWisePosition.first
	var nextClockWiseColumn = clockWisePosition.second
	val nextClockWisePipe = pipes[clockWisePosition]!!

	when (previousClockWiseDirection) {
		Direction.NORTH -> {
			when (nextClockWisePipe) {
				Pipe.VERTICAL -> {
					nextClockWiseRow--
				}
				Pipe.SOUTH_EAST -> {
					nextClockWiseColumn++
					previousClockWiseDirection = Direction.EAST
				}
				Pipe.SOUTH_WEST -> {
					nextClockWiseColumn--
					previousClockWiseDirection = Direction.WEST
				}
				else -> {}
			}
		}
		Direction.SOUTH -> {
			when (nextClockWisePipe) {
				Pipe.VERTICAL -> {
					nextClockWiseRow++
				}
				Pipe.NORTH_EAST -> {
					nextClockWiseColumn++
					previousClockWiseDirection = Direction.EAST
				}
				Pipe.NORTH_WEST -> {
					nextClockWiseColumn--
					previousClockWiseDirection = Direction.WEST
				}
				else -> {}
			}
		}
		Direction.EAST -> {
			when (nextClockWisePipe) {
				Pipe.HORIZONTAL -> {
					nextClockWiseColumn++
				}
				Pipe.NORTH_WEST -> {
					nextClockWiseRow--
					previousClockWiseDirection = Direction.NORTH
				}
				Pipe.SOUTH_WEST -> {
					nextClockWiseRow++
					previousClockWiseDirection = Direction.SOUTH
				}
				else -> {}
			}
		}
		Direction.WEST -> {
			when (nextClockWisePipe) {
				Pipe.HORIZONTAL -> {
					nextClockWiseColumn--
				}
				Pipe.NORTH_EAST -> {
					nextClockWiseRow--
					previousClockWiseDirection = Direction.NORTH
				}
				Pipe.SOUTH_EAST -> {
					nextClockWiseRow++
					previousClockWiseDirection = Direction.SOUTH
				}
				else -> {}
			}
		}
	}
	previousClockWisePipe = nextClockWisePipe
	clockWisePosition = nextClockWiseRow to nextClockWiseColumn

	steps++
}

println("Farthest distance: ${steps / 2}")
