import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Game {

    public static void main(String [] args) throws IOException {
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to the Game of Anagrams!");
        System.out.println("---------------------------------");

        boolean superman = false;
        boolean supermanTotal = false;

        Table myTable = new Table();
        System.out.println(myTable.getCharactersRemaining());

        while(!myTable.gameOver()){

            System.out.println("FLIPPED TILES");
            System.out.println("--------------------------------");
            System.out.println(myTable.getFlippedTiles());

            System.out.println();

            System.out.println("WORDS FORMED");
            System.out.println("--------------------------------");
            System.out.println(myTable.getWords());

            System.out.println();

//            System.out.println("make a word with letters by typing the word, or flip by typing '-flip'");
//            System.out.println("combine existing word with letters or other word by typing 'word1-word2-newWord'");
//            System.out.println("make an anagram of existing word by typing 'word1-newWord'");
            String response = input.next().toUpperCase();

            System.out.println();

            if(response.equals("-FLIP")){
                System.out.println("FLIPPED : " + myTable.flipTile());
            }
            else if(response.equals("-FLIP10")){
                for(int lcv = 0; lcv < 10; lcv ++){
                    System.out.println("FLIPPED : " + myTable.flipTile());
                }
            }
            else if(response.equals("-SUPERMAN")){
                System.out.println("ACTIVATED SUPERMAN");
                superman = true;
            }
            else if(response.equals("-TOTAL")){
                System.out.println("SHOWING TOTAL ANAGRAM COMBINATIONS");
                supermanTotal = true;
            }
            else if(response.equals("-PLAYGAME")){
                System.out.println("RUNNING SIMULATION");
                myTable.playGame();
                break;
            }
            else if(response.equals("-QUIT")){
                break;
            }
            else if(response.equals("-CUSTOM")){
                System.out.println("CUSTOM BOARD");
                System.out.println("FORMAT: 'flippedTile1,flippedTile2,...-word1,word2...");
                String customInput = input.next().toUpperCase();
                String [] customList = customInput.split("-");
                String [] customLetters = customList[0].split(",");
                String [] customWords = customList[1].split(",");
                myTable.addLetters(customLetters);
                myTable.addWords(customWords);
            }
            else{
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

            }

            System.out.println();

            if(superman){
                ArrayList<Word> myWordList = myTable.getWords();
                for(int lcv = 0; lcv < myWordList.size(); lcv ++){
                    System.out.println(myWordList.get(lcv) + " : " + myTable.buildAnagrams(myWordList.get(lcv)));
                }
                superman = false;
                System.out.println();
            }
            if(supermanTotal){
                ArrayList<String> characters = myTable.getFlippedTiles();
                ArrayList<Word> myWordList = myTable.getWords();

                Set<String> noAway = new HashSet<>();
                Set<String> oneAway = new HashSet<>();
                Set<String> twoAway = new HashSet<>();

                ArrayList<String> possible = new ArrayList<>();

                // word + 1 flipped letter
                for(int lcv = 0; lcv < myWordList.size(); lcv ++){
                    possible = myTable.buildAnagrams(new Word(myWordList.get(lcv).getName()));
                    for(int lcv2 = 0; lcv2 < possible.size(); lcv2 ++){
                        if(!possible.get(lcv2).equals(myWordList.get(lcv).getName())){
                            String tan = "\033[38;2;163;152;109m" + possible.get(lcv2) + "\033[0m";
                            noAway.add(tan);
                        }

                    }
                    possible.clear();

                    for(int lcv2 = 0; lcv2 < characters.size(); lcv2 ++){
                        String newPossibility = myWordList.get(lcv).getName() + characters.get(lcv2);

                        possible = myTable.buildAnagrams(new Word(newPossibility));
                        for(int lcv3 = 0; lcv3 < possible.size(); lcv3 ++){
                            String bronze = "\033[38;5;136m" + possible.get(lcv3) + "\033[0m";
                            oneAway.add(bronze);
                        }
                        possible.clear();

                        // word + 2 flipped letters
                        for(int lcv3 = 0; lcv3 < characters.size(); lcv3 ++){
                            if(lcv3 != lcv2){
                                String nextPossibility = newPossibility + characters.get(lcv3);
                                possible = myTable.buildAnagrams(new Word(nextPossibility));
                                for(int lcv4 = 0; lcv4 < possible.size(); lcv4 ++){
                                    String gold = "\033[38;2;212;179;13m" + possible.get(lcv4) + "\033[0m";
                                    twoAway.add(gold);
                                }
                                possible.clear();

                                // word + 3 flipped letters
//                                for(int lcv4 = 0; lcv4 < characters.size(); lcv4 ++){
//                                    if(lcv4 != lcv3 && lcv4 != lcv2){
//                                        String newestPossibility = nextPossibility + characters.get(lcv4);
//                                        possible = myTable.buildAnagrams(new Word(newestPossibility));
//                                        for(int lcv5 = 0; lcv5 < possible.size(); lcv5 ++){
//                                            anagrams.add(possible.get(lcv5));
//                                        }
//                                        possible.clear();
//                                    }
//                                }
                            }
                        }
                    }
                    int totalAway = oneAway.size() + twoAway.size();
                    String front = myWordList.get(lcv) + "(" + totalAway + ") : ";
                    System.out.format("%-11s %s\n", front, noAway);
                    System.out.format("%-11s %s\n", " ", oneAway);
                    System.out.format("%-11s %s\n", " ", twoAway);
                    System.out.println();
                    noAway.clear();
                    oneAway.clear();
                    twoAway.clear();
                }

                possible.clear();

                // new words from existing words
                for(int lcv = 0; lcv < myWordList.size(); lcv ++){
                    String wordOn = myWordList.get(lcv).getName();
                    for(int lcv2 = lcv+1; lcv2 < myWordList.size(); lcv2 ++){
                        String wordAdded = myWordList.get(lcv2).getName();

                        String newPossibility = wordOn + wordAdded;
                        possible = myTable.buildAnagrams(new Word(newPossibility));

                        for(int lcv3 = 0; lcv3 < possible.size(); lcv3 ++){
                            String itself = "\033[38;2;247;207;2m" + possible.get(lcv3) + "\033[0m";
                            String explanation = "\033[37m" + "(" + wordOn + "+" + wordAdded + ")" + "\033[0m";
                            String together = itself + explanation;

                            oneAway.add(together);
                        }
                        possible.clear();
                    }
                }
                System.out.println();
                System.out.println("FROM EXISTING (" + oneAway.size() + ") : " + oneAway);

                supermanTotal = false;
                System.out.println();
            }
        }
    }
}
