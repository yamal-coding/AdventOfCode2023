#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 14: https://adventofcode.com/2023/day/14
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

var northLoad = 0

val map = mutableMapOf<Pair<Int, Int>, Char>()

var row = 0
var width = 0
var height = 0

while (scanner.hasNext()) {
	val rocksRow = scanner.nextLine()
	if (width == 0) {
		width = rocksRow.length
	}


	var col = 0
	rocksRow.forEach { 
		map[row to col] = it
		col++
	}

	if (height == 0) {
		height = col
	}

	row++
}

for (col in 0 until width) {
	var row = 0
	while (row < width) {
		val northestPosition = row
		var roundedRocksCountUntilNextSquareRock = 0
		var squareRockFound = false
		while (row < width && !squareRockFound) {
			when (map[row to col]) {
				'O' -> {
					roundedRocksCountUntilNextSquareRock++
				}
				'#' -> {
					squareRockFound = true
				}
			}
			row++
		}


		for (i in northestPosition until northestPosition + roundedRocksCountUntilNextSquareRock) {
			northLoad += height - i 
		}
	}
}

println("North load is $northLoad")