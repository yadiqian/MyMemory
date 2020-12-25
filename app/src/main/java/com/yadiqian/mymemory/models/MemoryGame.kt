package com.yadiqian.mymemory.models

import com.yadiqian.mymemory.utils.DEFAULT_ICONS

class MemoryGame(
        private val boardSize: BoardSize,
        private val customImages: List<String>?
) {
    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numFlips = 0
    private var indexOfSingleSelectedCard: Int? = null
    private var indexOfCardOne: Int? = null
    private var indexOfCardTwo: Int? = null

    init {
        if (customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        } else {
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode() , it) }
        }
    }

    fun flipCard(position: Int): MatchResult {
        numFlips++
        val card = cards[position]
        var result = MatchResult.NO_MATCH_ONE

        if (indexOfSingleSelectedCard == null) {
            flipUnmatchedPair()
            indexOfSingleSelectedCard = position
        } else {
            result = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return result
    }

    private fun checkForMatch(position1: Int, position2: Int): MatchResult {
        if (cards[position1].id == cards[position2].id) {
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            numPairsFound++
            return MatchResult.MATCH
        }
        indexOfCardOne = position1
        indexOfCardTwo = position2
        return MatchResult.NO_MATCH_TWO
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun getNumMoves(): Int {
        return numFlips / 2
    }

    fun flipUnmatchedPair() {
        if (indexOfCardOne != null && !cards[indexOfCardOne!!].isMatched) {
            cards[indexOfCardOne!!].isFaceUp = false
        }
        if (indexOfCardTwo != null && !cards[indexOfCardTwo!!].isMatched) {
            cards[indexOfCardTwo!!].isFaceUp = false
        }
    }
}
