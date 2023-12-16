#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 16: https://adventofcode.com/2023/day/16
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

sealed class Tile
data class Empty(
	val visited: Boolean = false
) : Tile()
data class Mirror(
	val type: Char,
	val visitedFromTop: Boolean = false,
	val visitedFromBottom: Boolean = false,
	val visitedFromLeft: Boolean = false,
	val visitedFromRight: Boolean = false
) : Tile() {
	val visited: Boolean
		get() = visitedFromTop || visitedFromBottom || visitedFromLeft || visitedFromRight
}

val map = mutableMapOf<Pair<Int, Int>, Tile>()

var height = 0
var width = 0
while (scanner.hasNext()) {
	val row = scanner.nextLine()
	if (width == 0) {
		width = row.length
	}

	var col = 0
	row.forEach { char ->
		map[height to col] = when (char) {
			'.' -> Empty()
			else -> Mirror(type = char)
		}
		col++
	}

	height++
}

val energizedTile = getEnergizedTiles(0, 0, Direction.RIGHT)

println("Energized tiles: $energizedTile")

enum class Direction {
	UP, DOWN, LEFT, RIGHT
}

fun getEnergizedTiles(row: Int, col: Int, direction: Direction): Long {
	if (row >= height || row < 0 || col >= width || col < 0) {
		return 0
	}

	val tile = map[row to col] ?: return 0

	return when (tile) {
		is Empty -> {
			var tileEnergy = if (tile.visited) {
				0
			} else {
				map[row to col] = tile.copy(visited = true)
				1
			}
			
			tileEnergy + when (direction) {
				Direction.UP -> {
					getEnergizedTiles(row - 1, col, direction)
				}
				Direction.DOWN -> {
					getEnergizedTiles(row + 1, col, direction)
				}
				Direction.LEFT -> {
					getEnergizedTiles(row, col - 1, direction)
				}
				Direction.RIGHT -> {
					getEnergizedTiles(row, col + 1, direction)
				}
			}
		}
		is Mirror -> {
			var tileEnergy = if (tile.visited) {
				0
			} else {
				1
			}

			when (tile.type) {
				'-' -> {
					when (direction) {
						Direction.UP,  -> {
							if (!tile.visitedFromBottom) {
								map[row to col] = tile.copy(visitedFromBottom = true)
								tileEnergy + getEnergizedTiles(row, col - 1, Direction.LEFT) + getEnergizedTiles(row, col + 1, Direction.RIGHT)
							} else {
								0
							}
						}
						Direction.DOWN -> {
							if (!tile.visitedFromTop) {
								map[row to col] = tile.copy(visitedFromTop = true)
								tileEnergy + getEnergizedTiles(row, col - 1, Direction.LEFT) + getEnergizedTiles(row, col + 1, Direction.RIGHT)
							} else {
								0
							}
						}
						Direction.LEFT -> {
							if (!tile.visitedFromRight) {
								map[row to col] = tile.copy(visitedFromRight = true)
								tileEnergy + getEnergizedTiles(row, col - 1, direction)
							} else {
								0
							}
						}
						Direction.RIGHT -> {
							if (!tile.visitedFromLeft) {
								map[row to col] = tile.copy(visitedFromLeft = true)
								tileEnergy + getEnergizedTiles(row, col + 1, direction)
							} else {
								0
							}
						}
					}
				}
				'|' -> {
					when (direction) {
						Direction.LEFT -> {
							if (!tile.visitedFromRight) {
								map[row to col] = tile.copy(visitedFromRight = true)
								tileEnergy + getEnergizedTiles(row - 1, col, Direction.UP) + getEnergizedTiles(row + 1, col, Direction.DOWN)
							} else {
								0
							}
						}
						Direction.RIGHT -> {
							if (!tile.visitedFromLeft) {
								map[row to col] = tile.copy(visitedFromLeft = true)
								tileEnergy + getEnergizedTiles(row - 1, col, Direction.UP) + getEnergizedTiles(row + 1, col, Direction.DOWN)
							} else {
								0
							}
						}
						Direction.UP -> {
							if (!tile.visitedFromBottom) {
								map[row to col] = tile.copy(visitedFromBottom = true)
								tileEnergy + getEnergizedTiles(row - 1, col, direction)
							} else {
								0
							}
						}
						Direction.DOWN -> {
							if (!tile.visitedFromTop) {
								map[row to col] = tile.copy(visitedFromTop = true)
								tileEnergy + getEnergizedTiles(row + 1, col, direction)
							} else {
								0
							}
						}
					}
				}
				'\\' -> {
					when (direction) {
						Direction.UP -> {
							if (!tile.visitedFromBottom) {
								map[row to col] = tile.copy(visitedFromBottom = true)
								tileEnergy + getEnergizedTiles(row, col - 1, Direction.LEFT)
							} else {
								0
							}
						}
						Direction.DOWN -> {
							if (!tile.visitedFromTop) {
								map[row to col] = tile.copy(visitedFromTop = true)
								tileEnergy + getEnergizedTiles(row, col + 1, Direction.RIGHT)
							} else {
								0
							}
						}
						Direction.LEFT -> {
							if (!tile.visitedFromRight) {
								map[row to col] = tile.copy(visitedFromRight = true)
								tileEnergy + getEnergizedTiles(row - 1, col, Direction.UP)
							} else {
								0
							}
						}
						Direction.RIGHT -> {
							if (!tile.visitedFromLeft) {
								map[row to col] = tile.copy(visitedFromLeft = true)
								tileEnergy + getEnergizedTiles(row + 1, col, Direction.DOWN)
							} else {
								0
							}
						}
					}
				}
				'/' -> {
					when (direction) {
						Direction.UP -> {
							if (!tile.visitedFromBottom) {
								map[row to col] = tile.copy(visitedFromBottom = true)
								tileEnergy + getEnergizedTiles(row, col + 1, Direction.RIGHT)
							} else {
								0
							}
						}
						Direction.DOWN -> {
							if (!tile.visitedFromTop) {
								map[row to col] = tile.copy(visitedFromTop = true)
								tileEnergy + getEnergizedTiles(row, col - 1, Direction.LEFT)
							} else {
								0
							}
						}
						Direction.LEFT -> {
							if (!tile.visitedFromRight) {
								map[row to col] = tile.copy(visitedFromRight = true)
								tileEnergy + getEnergizedTiles(row + 1, col, Direction.DOWN)
							} else {
								0
							}
						}
						Direction.RIGHT -> {
							if (!tile.visitedFromLeft) {
								map[row to col] = tile.copy(visitedFromLeft = true)
								tileEnergy + getEnergizedTiles(row - 1, col, Direction.UP)
							} else {
								0
							}
						}
					}
				} else -> {
					error("Unexpected mirror type")
				}
			}
		} else ->{
			error("Unexpected tile type")
		}
	}
}