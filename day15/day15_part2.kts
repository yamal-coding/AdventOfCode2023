#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import java.util.LinkedList;

/*
 * Part 2 of day 15: https://adventofcode.com/2023/day/15
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val steps = scanner.nextLine().split(",")

val boxes = mutableMapOf<Int, LinkedList<Pair<String, Int>>>()

steps.forEach { step ->
	when {
		step.contains('-') -> {
			val stepParts = step.split("-")
			val label = stepParts[0]
			val boxKey = label.hash()

			val box = boxes[boxKey]
			if (box != null) {
				val iterator = box.iterator()
				var deleted = false
				while (iterator.hasNext() && !deleted) {
					val currentLens = iterator.next()
					if (currentLens.first == label) {
						iterator.remove()
						deleted = true
					}
				}
			}
		}
		step.contains('=') -> {
			val stepParts = step.split("=")
			val label = stepParts[0]
			val boxKey = label.hash()
			val lensValue = stepParts[1].toInt()

			val box = boxes[boxKey]
			if (box != null) {
				val iterator = box.listIterator(0)
				var inserted = false
				while (iterator.hasNext() && !inserted) {
					val currentLens = iterator.next()
					if (currentLens.first == label) {
						iterator.remove()
						iterator.add(label to lensValue)
						inserted = true
					}
				}
				if (!inserted) {
					box.addLast(label to lensValue)
				}
			} else {
				val lenses =  LinkedList<Pair<String, Int>>()
				lenses.add(label to lensValue)
				boxes[boxKey] = lenses
			}
		}
	}
}

var lensPower = 0

boxes.forEach {
	val (boxIndex, lenses) = it

	lenses.withIndex().forEach {
		val (index, lens) = it
		val focalLength = lens.second
		lensPower += (1 + boxIndex) * (index + 1) * focalLength
	}
}

println("Total lens power $lensPower")


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