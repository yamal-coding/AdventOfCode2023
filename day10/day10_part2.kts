#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 10: https://adventofcode.com/2023/day/10
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

enum class LoopSide {
	LEFT, RIGHT, UP, DOWN, INSIDE_CORNER, OUTSIDE_CORNER;
}

enum class PipeShape(val value: Char) {
	VERTICAL('|'),
	HORIZONTAL('-'),
	NORTH_EAST('L'),
	NORTH_WEST('J'),
	SOUTH_WEST('7'),
	SOUTH_EAST('F');

	companion object {
		fun fromValue(value: Char): PipeShape =
			values().first { it.value == value } 
	}
}

data class Pipe(
	val shape: PipeShape,
	val isMainLoop: Boolean = false,
	val sideInsideLoop: LoopSide? = null,
)

enum class Direction {
	NORTH, SOUTH, WEST, EAST;
}

val pipes = mutableMapOf<Pair<Int, Int>, Pipe>()

var startX = 0
var startY = 0

var width = 0
var height = 0

val groundTilesPositions = mutableSetOf<Pair<Int, Int>>()

var row = 0
while (scanner.hasNext()) {
	var column = 0
	scanner.nextLine().forEach { cell ->
		when (cell) {
			'S' -> {
				startX = row
				startY = column
				pipes[startX to startY] = Pipe(shape = PipeShape.HORIZONTAL, isMainLoop = true)
			}
			'.' -> {
				groundTilesPositions.add(row to column)
			}
			else -> {
				pipes[row to column] = Pipe(shape = PipeShape.fromValue(cell))
			}
		}
		column++
	}
	if (width == 0) {
		width = column
	}
	row++
}

height = row

val startPosition = startX to startY

var previousClockWiseDirection = Direction.EAST
var clockWisePosition = startX to startY + 1

while (clockWisePosition != startPosition) {
	var nextClockWiseRow = clockWisePosition.first
	var nextClockWiseColumn = clockWisePosition.second
	val nextClockWisePipe = pipes[clockWisePosition]!!.shape

	pipes[clockWisePosition] = pipes[clockWisePosition]!!.copy(isMainLoop = true)

	when (previousClockWiseDirection) {
		Direction.NORTH -> {
			when (nextClockWisePipe) {
				PipeShape.VERTICAL -> {
					nextClockWiseRow--
				}
				PipeShape.SOUTH_EAST -> {
					nextClockWiseColumn++
					previousClockWiseDirection = Direction.EAST
				}
				PipeShape.SOUTH_WEST -> {
					nextClockWiseColumn--
					previousClockWiseDirection = Direction.WEST
				}
				else -> {}
			}
		}
		Direction.SOUTH -> {
			when (nextClockWisePipe) {
				PipeShape.VERTICAL -> {
					nextClockWiseRow++
				}
				PipeShape.NORTH_EAST -> {
					nextClockWiseColumn++
					previousClockWiseDirection = Direction.EAST
				}
				PipeShape.NORTH_WEST -> {
					nextClockWiseColumn--
					previousClockWiseDirection = Direction.WEST
				}
				else -> {}
			}
		}
		Direction.EAST -> {
			when (nextClockWisePipe) {
				PipeShape.HORIZONTAL -> {
					nextClockWiseColumn++
				}
				PipeShape.NORTH_WEST -> {
					nextClockWiseRow--
					previousClockWiseDirection = Direction.NORTH
				}
				PipeShape.SOUTH_WEST -> {
					nextClockWiseRow++
					previousClockWiseDirection = Direction.SOUTH
				}
				else -> {}
			}
		}
		Direction.WEST -> {
			when (nextClockWisePipe) {
				PipeShape.HORIZONTAL -> {
					nextClockWiseColumn--
				}
				PipeShape.NORTH_EAST -> {
					nextClockWiseRow--
					previousClockWiseDirection = Direction.NORTH
				}
				PipeShape.SOUTH_EAST -> {
					nextClockWiseRow++
					previousClockWiseDirection = Direction.SOUTH
				}
				else -> {}
			}
		}
	}
	
	clockWisePosition = nextClockWiseRow to nextClockWiseColumn
}

// Fetch first pipe that belongs to the loop
var someLoopPipePosition = 0 to 0
var x = 0
var y = 0
var someLoopPipeFound = false

while (x < height && !someLoopPipeFound) {
	y = 0
	while (y < width && !someLoopPipeFound) {
		if (pipes[x to y]?.isMainLoop == true) {
			someLoopPipePosition = x to y
			someLoopPipeFound = true
		}
		y++
	}
	x++
}


// We fetched that pipe from left to start so it will never be of type HORIZONTAL, NORTH_WEST and SOUTH_WEST
val someLoopPipeSideInsideLoop = when (val shape = pipes[someLoopPipePosition]?.shape) {
	PipeShape.VERTICAL -> LoopSide.RIGHT
	PipeShape.NORTH_EAST, PipeShape.SOUTH_EAST -> LoopSide.INSIDE_CORNER
	else -> error("Invalid exepcted pipe shape $shape")
}
pipes[someLoopPipePosition] = pipes[someLoopPipePosition]!!.copy(sideInsideLoop = someLoopPipeSideInsideLoop)

// Mark every other pipe from the loop as part of the loop and set their side inside the loop

previousClockWiseDirection = when (pipes[someLoopPipePosition]?.shape) {
	PipeShape.VERTICAL -> Direction.NORTH
	PipeShape.NORTH_EAST, PipeShape.SOUTH_EAST -> Direction.EAST
	else -> error("Invalid exepcted pipe shape")
}
var previousClockWisePipe = pipes[someLoopPipePosition]!!.shape
clockWisePosition = when (pipes[someLoopPipePosition]?.shape) {
	PipeShape.VERTICAL -> someLoopPipePosition.first - 1 to someLoopPipePosition.second
	PipeShape.NORTH_EAST, PipeShape.SOUTH_EAST -> someLoopPipePosition.first to someLoopPipePosition.second + 1
	else -> error("Invalid exepcted pipe shape")
}
var previousSideInsideLoop = someLoopPipeSideInsideLoop

while (clockWisePosition != someLoopPipePosition) {
	var nextClockWiseRow = clockWisePosition.first
	var nextClockWiseColumn = clockWisePosition.second
	val nextClockWisePipe = pipes[clockWisePosition]!!.shape

	var side: LoopSide? = null

	when (previousClockWiseDirection) {
		Direction.NORTH -> {
			when (nextClockWisePipe) {
				PipeShape.VERTICAL -> {
					nextClockWiseRow--
					side = when (previousClockWisePipe) {
						PipeShape.VERTICAL -> {
							previousSideInsideLoop
						}
						PipeShape.NORTH_EAST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.RIGHT
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.LEFT
							}
						}
						else ->  { // PipeShape.NORTH_WEST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.LEFT
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.RIGHT
							}
						}
					}
				}
				PipeShape.SOUTH_EAST -> {
					nextClockWiseColumn++
					previousClockWiseDirection = Direction.EAST
					side = when (previousClockWisePipe) {
						PipeShape.VERTICAL -> {
							if (previousSideInsideLoop == LoopSide.LEFT) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.RIGHT
								LoopSide.INSIDE_CORNER
							}
						}
						PipeShape.NORTH_EAST -> {
							previousSideInsideLoop
						}
						else -> { // PipeShape.NORTH_WEST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
					}
				}
				PipeShape.SOUTH_WEST -> {
					nextClockWiseColumn--
					previousClockWiseDirection = Direction.WEST
					side = when (previousClockWisePipe) {
						PipeShape.VERTICAL -> {
							if (previousSideInsideLoop == LoopSide.LEFT) {
								LoopSide.INSIDE_CORNER
							} else { // LoopSide.RIGHT
								LoopSide.OUTSIDE_CORNER
							}
						}
						PipeShape.NORTH_EAST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
						else -> { // PipeShape.NORTH_WEST
							previousSideInsideLoop
						}
 					}
				}
				else -> {}
			}
		}
		Direction.SOUTH -> {
			when (nextClockWisePipe) {
				PipeShape.VERTICAL -> {
					nextClockWiseRow++
					side = when (previousClockWisePipe) {
						PipeShape.VERTICAL -> {
							previousSideInsideLoop
						}
						PipeShape.SOUTH_WEST -> {
							if (previousSideInsideLoop == LoopSide.OUTSIDE_CORNER) {
								LoopSide.RIGHT
							} else { // LoopSide.INSIDE_CORNER
								LoopSide.LEFT
							}
						}
						else -> { // PipeShape.SOUTH_EAST
							if (previousSideInsideLoop == LoopSide.OUTSIDE_CORNER) {
								LoopSide.LEFT
							} else { // LoopSide.INSIDE_CORNER
								LoopSide.RIGHT
							}
						}
					}
				}
				PipeShape.NORTH_EAST -> {
					nextClockWiseColumn++
					previousClockWiseDirection = Direction.EAST

					side = when (previousClockWisePipe) {
						PipeShape.VERTICAL -> {
							if (previousSideInsideLoop == LoopSide.LEFT) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.RIGHT
								LoopSide.INSIDE_CORNER
							}
						}
						PipeShape.SOUTH_EAST -> {
							previousSideInsideLoop
						}
						else -> { // PipeShape.SOUTH_WEST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
					}
				}
				PipeShape.NORTH_WEST -> {
					nextClockWiseColumn--
					previousClockWiseDirection = Direction.WEST

					side = when (previousClockWisePipe) {
						PipeShape.VERTICAL -> {
							if (previousSideInsideLoop == LoopSide.LEFT) {
								LoopSide.INSIDE_CORNER
							} else { // LoopSide.RIGHT
								LoopSide.OUTSIDE_CORNER
							}
						}
						PipeShape.SOUTH_EAST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
						else -> { // PipeShape.SOUTH_WEST
							previousSideInsideLoop
						}
					}
				}
				else -> {}
			}
		}
		Direction.EAST -> {
			when (nextClockWisePipe) {
				PipeShape.HORIZONTAL -> {
					nextClockWiseColumn++

					side = when (previousClockWisePipe) {
						PipeShape.HORIZONTAL -> {
							previousSideInsideLoop
						}
						PipeShape.SOUTH_EAST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.DOWN
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.UP
							}
						}
						else -> { // PipeShape.NORTH_EAST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.UP
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.DOWN
							}
						}
					}
				}
				PipeShape.NORTH_WEST -> {
					nextClockWiseRow--
					previousClockWiseDirection = Direction.NORTH

					side = when (previousClockWisePipe) {
						PipeShape.HORIZONTAL -> {
							if (previousSideInsideLoop == LoopSide.UP) {
								LoopSide.INSIDE_CORNER
							} else { // LoopSide.DOWN
								LoopSide.OUTSIDE_CORNER
							}
						}
						PipeShape.NORTH_EAST -> {
							previousSideInsideLoop
						}
						else -> { // PipeShape.SOUTH_EAST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
					}
				}
				PipeShape.SOUTH_WEST -> {
					nextClockWiseRow++
					previousClockWiseDirection = Direction.SOUTH

					side = when (previousClockWisePipe) {
						PipeShape.HORIZONTAL -> {
							if (previousSideInsideLoop == LoopSide.DOWN) {
								LoopSide.INSIDE_CORNER
							} else { // LoopSide.UP
								LoopSide.OUTSIDE_CORNER
							}
						}
						PipeShape.NORTH_EAST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
						else -> { // PipeShape.SOUTH_EAST
							previousSideInsideLoop
						}
					}
				}
				else -> {}
			}
		}
		Direction.WEST -> {
			when (nextClockWisePipe) {
				PipeShape.HORIZONTAL -> {
					nextClockWiseColumn--

					side = when (previousClockWisePipe) {
						PipeShape.HORIZONTAL -> {
							previousSideInsideLoop
						}
						PipeShape.NORTH_WEST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.UP
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.DOWN
							}
						} else -> { // PipeShape.SOUTH_WEST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.DOWN
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.UP
							}
						}
					}
				}
				PipeShape.NORTH_EAST -> {
					nextClockWiseRow--
					previousClockWiseDirection = Direction.NORTH

					side = when (previousClockWisePipe) {
						PipeShape.HORIZONTAL -> {
							if (previousSideInsideLoop == LoopSide.UP) {
								LoopSide.INSIDE_CORNER
							} else { // LoopSide.DOWN
								LoopSide.OUTSIDE_CORNER
							}
						}
						PipeShape.NORTH_WEST -> {
							previousSideInsideLoop
						} else -> { // PipeShape.SOUTH_WEST
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						}
					}
				}
				PipeShape.SOUTH_EAST -> {
					nextClockWiseRow++
					previousClockWiseDirection = Direction.SOUTH

					side = when (previousClockWisePipe) {
						PipeShape.HORIZONTAL -> {
							if (previousSideInsideLoop == LoopSide.UP) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.DOWN
								LoopSide.INSIDE_CORNER
							}
						}
						PipeShape.NORTH_WEST -> {
							if (previousSideInsideLoop == LoopSide.INSIDE_CORNER) {
								LoopSide.OUTSIDE_CORNER
							} else { // LoopSide.OUTSIDE_CORNER
								LoopSide.INSIDE_CORNER
							}
						} else -> { // PipeShape.SOUTH_WEST
							previousSideInsideLoop
						}
					}
				}
				else -> {}
			}
		}
	}

	previousClockWisePipe = nextClockWisePipe
	pipes[clockWisePosition] = pipes[clockWisePosition]!!.copy(sideInsideLoop = side)
	
	clockWisePosition = nextClockWiseRow to nextClockWiseColumn

	previousSideInsideLoop = side!!
}

var enclosedTilesCount = 0

// gather every pipe that is not part of the loop
val orphanPipes = pipes.entries.filter { !it.value.isMainLoop }.map { it.key }

// Look for the first pipe that is part of the loop at the right of every ground tile and orphan pipe and see if they are in their side inside the loop
(groundTilesPositions + orphanPipes).forEach {
	val (groundRow, groundColumn) = it

	var col = groundColumn + 1
	var pipeFromLoop: Pipe? = null
	while (col < width && pipeFromLoop?.isMainLoop != true) {
		pipeFromLoop = pipes[groundRow to col]
		col++
	}

	if (pipeFromLoop != null && pipeFromLoop.isMainLoop) {
		val tileIsInsideLoop = when (pipeFromLoop.shape) {
			PipeShape.VERTICAL -> pipeFromLoop.sideInsideLoop == LoopSide.LEFT
			PipeShape.NORTH_EAST, PipeShape.SOUTH_EAST -> pipeFromLoop.sideInsideLoop == LoopSide.OUTSIDE_CORNER
			else -> error("Invalid expected pipe shape")
		}

		if (tileIsInsideLoop) {
			enclosedTilesCount++
		}
	}
}

println("Enclosed tiles: $enclosedTilesCount")

