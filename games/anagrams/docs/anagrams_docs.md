# &#x1F4D6; anagrams

## description
Anagrams is a word game played with small, square tiles that each contain an individual English letter. While not an officially produced game, the game can be played using a set of [Bananagrams](https://en.wikipedia.org/wiki/Bananagrams) tiles.
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

We need a way to look at both groups and check if a new word can be formed in any of the 4 ways listed above.\
We need to have a collection of acceptable English words.\
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
*charactersRemaining* is populated with letters following the Bananagrams' letter distribution
```java
charactersRemaining = new ArrayList<>(Arrays.asList("A", "A", "A", ... "J", "J", "K", "K", ... "Z", "Z"));
```
A random element, representing a tile, is taken from *charactersRemaining* each time *__flipTile()__* is called
```java
public String flipTile(){
    Random rand = new Random();
    int number = rand.nextInt(charactersRemaining.size());
    String flipped = charactersRemaining.get(number);
    charactersUp.add(flipped);
    charactersRemaining.remove(number);
    return flipped;
}
```
Consider how the user selection for forming a word is taken in **Game**
```java
System.out.println("make a word with letters by typing the word");
System.out.println("combine existing word with letters or other word by typing 'word1-word2-newWord'");
System.out.println("make an anagram of existing word by typing 'word1-newWord'");
String response = input.next().toUpperCase();
```
And later, to parse *response*
```java
String [] potential = response.split("-");
if(potential.length == 3){
    int word1Index = 0;
    int word2Index = -1;

    ArrayList<Word> myWordList = myTable.getWords();
    for(int lcv = 0; lcv < myWordList.size(); lcv ++){
        if(myWordList.get(lcv).getName().equals(potential[0])){
            word1Index = lcv;
        }
        if(myWordList.get(lcv).getName().equals(potential[1])){
            word2Index = lcv;
        }
    }

    if(word2Index == -1){
        Word addOn = new Word(potential[1]);
        myTable.mergeWordAndWord(myWordList.get(word1Index), addOn, potential[2], false);
    }
    else{
        myTable.mergeWordAndWord(myWordList.get(word1Index), myWordList.get(word2Index), potential[2], true);
    }
}
else if(potential.length == 2){
    int word1Index = 0;
    
    ArrayList<Word> myWordList = myTable.getWords();
    
    for(int lcv = 0; lcv < myWordList.size(); lcv ++){
        if(myWordList.get(lcv).getName().equals(potential[0])){
            word1Index = lcv;
        }
    }
    myTable.claimAnagram(myWordList.get(word1Index), potential[1]);
}
else{
    myTable.mergeLetters(response.split(""), response);
}
```
The user indicates how they want to form a word, and with which word(s) and/or letter(s)\
*__mergeLetters()__* or *__mergeWordAndWord()__* or *__claimAnagram()__* are called accordingly
```java
public boolean mergeLetters (String [] letters, String proposed){
    if(isInDictionary(proposed)){
        Word newWord = new Word(proposed);
        words.add(newWord);
        for(int lcv = 0; lcv < letters.length; lcv ++){
            this.charactersUp.remove(letters[lcv]);
        }
        return true;
    }
    return false;
}
```
```java
public boolean mergeWordAndWord (Word original, Word toAdd, String proposed, boolean mode){
    String addString = toAdd.getName();
    String workOn = original.getName() + addString;

    if(isAnagram(workOn.split(""), proposed.split(""))){
        original.setName(proposed);
        if(mode){ // toAdd was a Word
            words.remove(toAdd);
        }
        else{ // toAdd was letters
            String [] toDelete = addString.split("");
            for(int lcv = 0; lcv < toDelete.length; lcv ++){
                charactersUp.remove(toDelete[lcv]);
            }
        }
        return true;
    }
    return false;
}
```
```java
public boolean claimAnagram (Word original, String proposed){
    if(isAnagram(original.getName().split(""), proposed.split(""))){
        int index = getWordIndex(original.getName());
        words.get(index).setName(proposed);
        return true;
    }
    return false;
}
```
As seen, when making a word from letters only, it is easily validated as an English word by calling *__isInDictionary()__*
```java
public boolean isInDictionary(String w){
    return dictionary.contains(w);
}
```
When making a word using an existing word, the problem is a bit trickier\

## result

## source code
### [Game](../src/Game.java)
### [Table](../src/Table.java)
