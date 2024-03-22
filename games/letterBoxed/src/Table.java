import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Table {

    private String [] north;
    private String [] east;
    private String [] south;
    private String [] west;

    private String [][] nesw;

    private Map<String, Integer> visited;
    private Map<String, Integer> neswPairings;

    private ArrayList<String> dictionary;


    public Table(String north, String east, String south, String west) throws IOException {
        this.north = north.toUpperCase().split("");
        this.east = east.toUpperCase().split("");
        this.south = south.toUpperCase().split("");
        this.west = west.toUpperCase().split("");

        this.nesw = new String [4][3];
        this.nesw[0] = this.north;
        this.nesw[1] = this.east;
        this.nesw[2] = this.south;
        this.nesw[3] = this.west;

        visited = new HashMap<>(); // all initially 0 (unvisited)
        neswPairings = new HashMap<>();
        for(int r = 0; r < nesw.length; r ++){
            for(int c = 0; c < nesw[r].length; c ++){
                visited.put(nesw[r][c], 0);
                neswPairings.put(nesw[r][c], r);
            }
        }

        dictionary = new ArrayList<>();
        BufferedReader fileIn = new BufferedReader(new FileReader("resources/dictionary.txt"));
        String line = null;
        while ((line = fileIn.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;   //ignore comments

            dictionary.add(line.toUpperCase());
        }
        fileIn.close();

        Collections.sort(dictionary, Comparator.comparing(String::length));
    }

    public String toString(){
        String toReturn = " ";
        for(int lcv = 0; lcv < 3; lcv ++){
            toReturn += north[lcv] + " ";
        }
        toReturn += "\n";
        for(int lcv = 0; lcv < 3; lcv ++){
            toReturn += west[lcv] + "     " + east[lcv] + "\n";
        }
        toReturn += " ";
        for(int lcv = 0; lcv < 3; lcv ++){
            toReturn += south[lcv] + " ";
        }
        return toReturn;
    }

    public boolean isInDictionary(String w){
        return dictionary.contains(w);
    }

    public int countVowels(String [] w){
        int count = 0;
        for(int lcv = 0; lcv < w.length; lcv ++){
            if(w[lcv].equals("A") ||
                w[lcv].equals("E") ||
                w[lcv].equals("I") ||
                w[lcv].equals("O") ||
                w[lcv].equals("U")){

                count ++;
            }
        }
        return count;
    }

    public void addToVisited(String [] w){
        for(int lcv = 0; lcv < w.length; lcv ++){
            visited.put(w[lcv], visited.get(w[lcv]) + 1);
        }
    }

    public void resetVisited(){
        for(int r = 0; r < nesw.length; r ++){
            for(int c = 0; c < nesw[r].length; c ++){
                visited.put(nesw[r][c], 0);
            }
        }
    }

    public int getDictionaryRange(String firstChar, int desiredLength){
        int lcv = 0;
        for(lcv = 0; lcv < dictionary.size(); lcv ++){
            if(dictionary.get(lcv).length() < desiredLength){
                lcv += 100;
            }
            else if(dictionary.get(lcv).substring(0, 1).equals(firstChar)){
                return lcv;
            }
        }
        return lcv;
    }

    public boolean isPossibleMove(String [] w){

        if(neswPairings.get(w[0]) == null){
            return false;
        }

        for(int lcv = 1; lcv < w.length; lcv ++){
            if(neswPairings.get(w[lcv]) == null){
                return false;
            }
            else if(neswPairings.get(w[lcv]) == neswPairings.get(w[lcv-1])){
                return false;
            }
        }
        return true;
    }

    public boolean worthTaking(String [] w){
        int unvisitedCount = 0;
        for(int lcv = 0; lcv < w.length; lcv ++){
            if(visited.get(w[lcv]) == 0){
                unvisitedCount ++;
            }
        }

        return unvisitedCount >= 2;
    }

    public String [] alternateSolution(int numSteps){
        Random random = new Random();

        String [] wordList = new String [numSteps];
        int indexOn = 0;
        int sideOn = random.nextInt(4);
        int letterOn = random.nextInt(3);
        String wordOn = "";

        String firstChar = nesw[sideOn][letterOn];

        boolean keepGoing = true;
        boolean foundWord = false;

        int searchLowerBound = 12;
        int searchUpperBound = 14;

        int restartTimes = 0;

        for(int lcv = 0; lcv < numSteps; lcv ++){

            while(keepGoing){

                int seekOutLength = random.nextInt(searchLowerBound,searchUpperBound);
                int startIndex = getDictionaryRange(firstChar, seekOutLength);

                for(int dictTraverse = startIndex; dictTraverse < startIndex + 400; dictTraverse ++){
                    wordOn = dictionary.get(dictTraverse);
                    if(!wordOn.substring(0, 1).equals(firstChar)){
                        dictTraverse = startIndex + 400;
                        continue;
                    }
                    if(isPossibleMove(wordOn.split(""))){

                        if(worthTaking(wordOn.split("")) || !visited.containsValue(0)){
                            wordList[indexOn] = wordOn;
                            addToVisited(wordOn.split(""));
                            indexOn ++;
                            firstChar = wordOn.substring(wordOn.length()-1);
                            foundWord = true;
                            dictTraverse = startIndex + 400;
                        }
                    }
                    if(!visited.containsValue(0)){
                        lcv = numSteps;
                        dictTraverse = startIndex + 400;
                    }
                }
                if(!foundWord){
                    lcv = numSteps - 1;
                }
                keepGoing = false;
                foundWord = false;
            }

            keepGoing = true;

            if(lcv + 1 == numSteps && visited.containsValue(0)){
                lcv = -1;
                indexOn = 0;
                System.out.println(visited.keySet());
                System.out.println(visited.values());
                resetVisited();
                System.out.println("didn't get all letters, restarting");

                restartTimes ++;

                if(restartTimes % 100 == 0){
                    searchLowerBound = Math.max(4, searchLowerBound - 2);
                    searchUpperBound = Math.max(8, searchUpperBound - 2);

                    System.out.println("now looking for words in range: " + searchLowerBound + " - " + searchUpperBound);
                }
            }
        }

        System.out.println("==========================");
        for(int lcv2 = 0; lcv2 < indexOn; lcv2 ++){
            System.out.println(wordList[lcv2]);
        }
        System.out.println("==========================");

        return wordList;
    }

    public String [] computeSolution(int numSteps){

        Random random = new Random();

        String [] wordList = new String [numSteps];
        int indexOn = 0;
        int sideOn = random.nextInt(4);
        int letterOn = random.nextInt(3);

        int firstCharSideOn = sideOn;
        String wordOn = nesw[sideOn][letterOn];

        boolean keepGoing = true;

        for(int lcv = 0; lcv < numSteps; lcv ++){

            while(keepGoing){
                int seekingForUnused = 0;
                int nextSideOn = random.nextInt(4);
                while(nextSideOn == sideOn){
                    nextSideOn = random.nextInt(4);
                }
                int nextLetterOn = random.nextInt(3);
                while(visited.get(nesw[nextSideOn][nextLetterOn]) > 0 && seekingForUnused < 2){
                    nextSideOn = random.nextInt(4);
                    while(nextSideOn == sideOn){
                        nextSideOn = random.nextInt(4);
                    }
                    nextLetterOn = random.nextInt(3);
                    seekingForUnused ++;
                }

                if(wordOn.length() == 1){
                    firstCharSideOn = sideOn;
                }

                sideOn = nextSideOn;
                letterOn = nextLetterOn;

                wordOn += nesw[sideOn][letterOn];

                if(isInDictionary(wordOn) && wordOn.length() >= 4){
                    addToVisited(wordOn.split(""));
                    wordList[indexOn] = wordOn;
                    indexOn ++;
                    wordOn = nesw[sideOn][letterOn];
                    System.out.println("old word on : " + wordList[indexOn-1] + " new word on: " + wordOn);
                    keepGoing = false;
                }
                else if(countVowels(wordOn.split("")) == 0 && wordOn.length() >= 3){
                    wordOn = wordOn.substring(0, 1);
                    sideOn = firstCharSideOn;
                }
                else if(wordOn.length() > 6){
                    wordOn = wordOn.substring(0, 1);
                    sideOn = firstCharSideOn;
                }
            }

            keepGoing = true;

            if(lcv + 1 == numSteps && visited.containsValue(0)){
                lcv = -1;
                indexOn = 0;
                System.out.println(visited.keySet());
                System.out.println(visited.values());
                resetVisited();
                System.out.println("didn't get all letters, restarting");
            }
        }

        System.out.println("==========================");
        for(int lcv2 = 0; lcv2 < indexOn; lcv2 ++){
            System.out.println(wordList[lcv2]);
        }
        System.out.println("==========================");

        return wordList;

    }

}
