# &#x1F4D6; anagrams

## description
Anagrams is a word game played with small, square tiles that each contain an individual English letter. While not an officially produced game, the game can be played using a set of [Banagrams](https://en.wikipedia.org/wiki/Bananagrams) tiles.
The game is played by putting all tiles face down and gradually turning the tiles over, pausing after each one to allow for word formation.

Words (**>= 3 letters**) can be formed in the following ways:
1. individual flipped tiles combined to form a word **M G E -> GEM**
2. flipped tile(s) plus an existing word formed to make a new word **R CAT -> CART** or **T S BALE -> STABLE**
3. existing word(s) formed with other existing word(s) to make a new word **LUMEN BINARY -> INNUMERABLY**
4. strict anagram: anagram of an existing word **FEEL -> FLEE**

A restriction in forming words: **you cannot form a word that has been previously formed**

The person who forms the word, gets the word. So the game is a continual cycle of tiles shifting ownership. A winner is determined as the one with the most words formed following the overturn of the last tile.

## problem
We need a way to represent a given board state. Effectively a board state has two groups of items. One group is the set of flipped, unclaimed tiles. The other group is the set of built words.

We need a way to look at both groups and check if a new word can be formed in any of the 4 ways listed above.

We need to have a collection of acceptable English words.

We want to minimize computation time.

**Goal : Find as many word formations (ways 2-4) as possible for each word in the set of built words.**

## approach
- a **Game** class (for prompting user and running the game)
- a **Table** class (for tracking the board and housing the functions required to solve the board)
- a **Word** class (for built words, to practice OOP principles, and for extensibility options)

a **Word** object has the following member variables
```java
    private String name;
    private int length;
```
a **Table** object has the following member variables
```java
    private ArrayList<Word> words; // formed words
    private ArrayList<String> charactersUp; // flipped tiles
    private ArrayList<String> charactersRemaining; // unflipped tiles
    private ArrayList<String> dictionary; // list of acceptable English words
    private ArrayList<String> wordsToNotUse; // list of all words that have already been formed
```
*dictionary* is populated with words from [this](../resources/dictionary.txt) Scrabble dictionary. It is then sorted **by length**
```java
Collections.sort(dictionary, Comparator.comparing(String::length));
```
*charactersRemaining* is populated with letters following the Banagrams' letter distribution
```java
charactersRemaining = new ArrayList<>(Arrays.asList("A", "A", "A", ... "J", "J", "K", "K", ... "Z", "Z"));
```

## result

## source code
### [Game](../src/Game.java)
### [Table](../src/Table.java)
