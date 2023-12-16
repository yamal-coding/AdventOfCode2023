#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 1 of day 15: https://adventofcode.com/2023/day/15
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val steps = scanner.nextLine().split(",")

var sumOfHashes = 0L

steps.forEach { step ->
	sumOfHashes += step.hash()
}

println("Sum of hashes $sumOfHashes")

fun String.hash(): Int {
	var hash = 0
	forEach { char ->
		val asciiCode = char.toInt()
		hash += asciiCode
		hash *= 17
		hash = hash % 256
	}
	return hash
}