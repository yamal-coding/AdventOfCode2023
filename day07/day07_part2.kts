#!/usr/bin/env kotlin

import java.io.File
import java.util.Scanner
import java.util.SortedSet
import java.util.TreeSet

/*
 * Part 2 of day 7: https://adventofcode.com/2023/day/7
 */

val file = File("input.txt")
val scanner = Scanner(file.inputStream())

enum class Card(val label: Char, val weight: Int) {
    A('A', 12), 
    K('K', 11), 
    Q('Q', 10), 
    T('T', 9),
    NINE('9', 8),
    EIGHT('8', 7),
    SEVEN('7', 6),
    SIX('6', 5),
    FIVE('5', 4),
    FOUR('4', 3),
    THREE('3', 2),
    TWO('2', 1),
    JOKER('J', 0);

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
    val identicalCardsCountWithoutJoker = mutableMapOf<Int, Int>()
    var jokersCount = 0

    numOfCardsByLabel.forEach {
        val (card, count) = it

        identicalCardsCount[count] = (identicalCardsCount[count] ?: 0) + 1

        if (card == Card.JOKER) {
            jokersCount += count
        }
    }

    var strength = when {
        identicalCardsCount[5] == 1 -> Strength.FIVE_OF_A_KIND
        identicalCardsCount[4] == 1 -> Strength.FOUR_OF_A_KIND
        identicalCardsCount[3] == 1 && identicalCardsCount[2] == 1 -> Strength.FULL_HOUSE
        identicalCardsCount[3] == 1 && identicalCardsCount[1] == 2 -> Strength.THREE_OF_A_KIND
        identicalCardsCount[2] == 2 -> Strength.TWO_PAIR
        identicalCardsCount[2] == 1 && identicalCardsCount[1] == 3 -> Strength.ONE_PAIR
        else -> Strength.HIGH_CARD
    }

    if (jokersCount > 0) {
        strength = when (strength) {
            Strength.FIVE_OF_A_KIND -> Strength.FIVE_OF_A_KIND
            Strength.FOUR_OF_A_KIND -> Strength.FIVE_OF_A_KIND
            Strength.FULL_HOUSE -> Strength.FIVE_OF_A_KIND
            Strength.THREE_OF_A_KIND -> Strength.FOUR_OF_A_KIND
            Strength.TWO_PAIR -> if (jokersCount == 1) {
                Strength.FULL_HOUSE
            } else {
                Strength.FOUR_OF_A_KIND
            }
            Strength.ONE_PAIR -> Strength.THREE_OF_A_KIND        
            Strength.HIGH_CARD -> Strength.ONE_PAIR
        }
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
