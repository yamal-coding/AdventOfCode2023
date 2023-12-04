#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner

/*
 * Part 2 of day 4: https://adventofcode.com/2023/day/4
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

val repeatedCardCopiesById = mutableMapOf<Int, Int>()

while (scanner.hasNext()) {
    val card = scanner.nextLine().split(" | ")
    val cardInfo = card[0].split(": ")
    val cardId = cardInfo[0].removePrefix("Card").trim().toInt()
    val winningNumbers = cardInfo[1].split(" ").toSet()
    val candidateNumbers = card[1].split(" ").filter { it.isNotBlank() }
    
    repeatedCardCopiesById[cardId] = (repeatedCardCopiesById[cardId] ?: 0) + 1

    var repeatedNumbers = 0
    candidateNumbers.forEach { candidate ->
        if (winningNumbers.contains(candidate)) {
            repeatedNumbers++
        }
    }

    repeat(repeatedCardCopiesById[cardId] ?: 1) {
        for (i in 1 .. repeatedNumbers) {
            repeatedCardCopiesById[cardId + i] = (repeatedCardCopiesById[cardId + i] ?: 0)+ 1
        }
    }
}

val totalOfCopies = repeatedCardCopiesById.map { it.value }.sum()

println("Toal of copies: $totalOfCopies")