#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import java.util.SortedSet
import java.util.TreeSet

/*
 * Part 1 of day 7: https://adventofcode.com/2023/day/7
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

enum class Card(val label: Char, val weight: Int) {
    A('A', 13), 
    K('K', 12), 
    Q('Q', 11), 
    J('J', 10), 
    T('T', 9),
    NINE('9', 8),
    EIGHT('8', 7),
    SEVEN('7', 6),
    SIX('6', 5),
    FIVE('5', 4),
    FOUR('4', 3),
    THREE('3', 2),
    TWO('2', 1);

    companion object {
        fun fromLabel(label: Char): Card =
            values().first { it.label == label} 
    }
}

enum class Strength(val weight: Int) {
    FIVE_OF_A_KIND(7),
    FOUR_OF_A_KIND(6),
    FULL_HOUSE(5),
    THREE_OF_A_KIND(4),
    TWO_PAIR(3),
    ONE_PAIR(2),
    HIGH_CARD(1);
}

data class Hand(
    val cards: List<Card>,
    val bid: Long,
    val strength: Strength, 
) : Comparable<Hand> {
    override fun compareTo(otherHand: Hand): Int =
        when {
            this.strength.weight > otherHand.strength.weight -> 1
            this.strength.weight < otherHand.strength.weight -> -1
            else -> {
                var i = 0
                var comparisonResult = 0
                while (i < this.cards.size && comparisonResult == 0) {
                    when {
                        this.cards[i].weight > otherHand.cards[i].weight -> {
                            comparisonResult = 1
                        }
                        this.cards[i].weight < otherHand.cards[i].weight -> {
                            comparisonResult = -1
                        }
                    }
                    i++
                }
                comparisonResult
            }
        }
}

val sortedHandsByStrength: SortedSet<Hand> = TreeSet()

while (scanner.hasNext()) {
    val cards = scanner.next().toCharArray().map { Card.fromLabel(it) }
    val bid = scanner.next().toLong()

    val numOfCardsByLabel = mutableMapOf<Card, Int>()

    cards.forEach { card ->
        numOfCardsByLabel[card] = (numOfCardsByLabel[card] ?: 0) + 1
    }

    val identicalCardsCount = mutableMapOf<Int, Int>()

    numOfCardsByLabel.forEach {
        val count = it.value

        identicalCardsCount[count] = (identicalCardsCount[count] ?: 0) + 1
    }

    val strength = when {
        identicalCardsCount[5] == 1 -> Strength.FIVE_OF_A_KIND
        identicalCardsCount[4] == 1 -> Strength.FOUR_OF_A_KIND
        identicalCardsCount[3] == 1 && identicalCardsCount[2] == 1 -> Strength.FULL_HOUSE
        identicalCardsCount[3] == 1 && identicalCardsCount[1] == 2 -> Strength.THREE_OF_A_KIND
        identicalCardsCount[2] == 2 -> Strength.TWO_PAIR
        identicalCardsCount[2] == 1 && identicalCardsCount[1] == 3 -> Strength.ONE_PAIR
        else -> Strength.HIGH_CARD
    }

    val hand = Hand(cards, bid, strength)
    sortedHandsByStrength.add(hand)
}

var totalWinnings = 0L
sortedHandsByStrength.withIndex().forEach {
    val (i, hand) = it
    totalWinnings += hand.bid * (i + 1)
}

println("Total winnings: $totalWinnings")


