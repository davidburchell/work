# &#x1F4D6; Anagrams

## description
> Anagrams is a word game played with small, square tiles that each contain an individual English letter. While not an officially produced game, the game can be played using a set of Banagrams tiles.
> The game is played by putting all tiles face down and gradually turning the tiles over, pausing after each one to allow for word formation.
> Words (>= 3 letters) can be formed in the following ways:
> 1. individual flipped tiles combine to form a word **M G E -> GEM**
> 2. flipped tile(s) plus an existing word formed to make a new word **R CAT -> CART** or **T S BALE -> STABLE**
> 3. existing word(s) formed with other existing word(s) to make a new word **LUMEN BINARY -> INNUMERABLY**
> 4. strict anagram: anagram of an existing word **FEEL -> FLEE**
>
> The person who forms the word, gets the word. So the game is a continual cycle of tiles shifting ownership. A winner is determined as the one with the most words formed following the overturn of the last tile.

## problem
> We need a way to represent a given board state. Effectively a board state has two groups of items. One group is the set of flipped, unclaimed tiles. The other group is the set of built words.
>
> We need a way to look at both groups and check if a new word can be formed in any of the 4 ways listed above.
>
> We want to minimize computation time.
> 
> **Goal : Find as many word formations (ways 2-4) as possible for each word in the set of built words.**

## approach

## source code
### [Game](../src/Game.java)
### [Table](../src/Table.java)
