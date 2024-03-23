import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Table {

    private ArrayList<Word> words;
    private ArrayList<String> charactersUp;
    private ArrayList<String> charactersRemaining;
    private ArrayList<String> dictionary;
    private ArrayList<String> wordsToNotUse;

    public Table () throws IOException {

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

        charactersRemaining = new ArrayList<>(Arrays.asList("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
                                              "B", "B", "B",
                                              "C", "C", "C",
                                              "D", "D", "D", "D", "D", "D",
                                              "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E",
                                              "F", "F", "F",
                                              "G", "G", "G", "G",
                                              "H", "H", "H",
                                              "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I", "I",
                                              "J", "J",
                                              "K", "K",
                                              "L", "L", "L", "L", "L",
                                              "M", "M", "M",
                                              "N", "N", "N", "N", "N", "N", "N", "N",
                                              "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O",
                                              "P", "P", "P",
                                              "Q", "Q",
                                              "R", "R", "R", "R", "R", "R", "R", "R", "R",
                                              "S", "S", "S", "S", "S", "S",
                                              "T", "T", "T", "T", "T", "T", "T", "T", "T",
                                              "U", "U", "U", "U", "U", "U",
                                              "V", "V", "V",
                                              "W", "W", "W",
                                              "X", "X",
                                              "Y", "Y", "Y",
                                              "Z", "Z"));

        charactersUp = new ArrayList<>();
        words = new ArrayList<>();
        wordsToNotUse = new ArrayList<>();
    }

    public ArrayList<String> getFlippedTiles(){
        return this.charactersUp;
    }
    public ArrayList<String> getCharactersRemaining() { return this.charactersRemaining; }
    public ArrayList<Word> getWords(){
        return words;
    }

    public int getWordIndex(String thisWord){
        for(int lcv = 0; lcv < words.size(); lcv ++){
            if(words.get(lcv).getName().equals(thisWord)){
                return lcv;
            }
        }
        return -1;
    }

    public String getLastChange(String change){
        if(change.equals("")){
            return "NONE";
        }
        String toReturn = "";
        String [] parts = change.split(" ");

        if(parts[2].equals("0")){
            toReturn += "made from letters : ";
            String [] letters = parts[0].split("");
            for(int lcv = 0; lcv < letters.length; lcv ++){
                toReturn += letters[lcv] + "+";
            }
            toReturn = toReturn.substring(0, toReturn.length()-1); // get rid of last '+'
            toReturn += " = " + parts[1];
        }
        else if(parts[2].equals("1")){
            String [] subpart = parts[0].split("-");
            if(subpart.length == 1){
                toReturn += "anagram made from existing word : ";
                toReturn += subpart[0] + " --> " + parts[1];
            }
            else if(subpart.length == 2){
                toReturn += "made from existing word plus letter : ";
                toReturn += subpart[0] + "+" + subpart[1] + " = " + parts[1];
            }
            else{
                toReturn += "made from existing word plus 2 letters : ";
                toReturn += subpart[0] + "+" + subpart[1] + "+" + subpart[2] + " = " + parts[1];
            }
        }
        else{
            toReturn += "made from existing words : ";
            String [] subpart = parts[0].split("-");
            toReturn += subpart[0] + " became " + parts[1] + " using " + subpart[1];
        }
        return toReturn;
    }

    public ArrayList<String> colorCode(){

        ArrayList<String> toReturn = new ArrayList<>();
        for(int lcv = 0; lcv < words.size(); lcv ++){
            String toConsider = words.get(lcv).getName();
            if(toConsider.length() <= 4){
                String tan = "\033[38;2;163;152;109m" + toConsider + "\033[0m";
                toReturn.add(tan);
            }
            else if(toConsider.length() <= 6){
                String bronze = "\033[38;5;136m" + toConsider + "\033[0m";
                toReturn.add(bronze);
            }
            else{
                String gold = "\033[38;2;212;179;13m" + toConsider + "\033[0m";
                toReturn.add(gold);
            }
        }
        return toReturn;
    }

    public void addLetters(String [] customLetters){
        for(int lcv = 0; lcv < customLetters.length; lcv ++){
            charactersUp.add(customLetters[lcv]);
//            charactersRemaining.remove(customLetters[lcv]);
        }
    }
    public void addWords(String [] customWords){
        for(int lcv = 0; lcv < customWords.length; lcv ++){
            String myWord = customWords[lcv];
            String [] workingOn = myWord.split("");
            for(int lcv2 = 0; lcv2 < workingOn.length; lcv2 ++){
//                charactersRemaining.remove(workingOn[lcv2]);
            }
            words.add(new Word(myWord));
        }
    }

    public boolean gameOver(){
        return charactersRemaining.size() == 0;
    }

    public String flipTile(){
        Random rand = new Random();
        int number = rand.nextInt(charactersRemaining.size());
        String flipped = charactersRemaining.get(number);
        charactersUp.add(flipped);
        charactersRemaining.remove(number);

        return flipped;
    }

    public boolean isInDictionary(String w){
        return dictionary.contains(w);
    }

    public boolean isAnagram(String [] w1, String [] w2){
        if(w1.length != w2.length){
            return false;
        }

        Map<String, Integer> map1 = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();
        String word = "";

        for(int lcv = 0; lcv < w1.length; lcv ++){
            word += w2[lcv];
            if(map1.putIfAbsent(w1[lcv], 1) != null){
                map1.put(w1[lcv], map1.get(w1[lcv]) + 1);
            }
            if(map2.putIfAbsent(w2[lcv], 1) != null){
                map2.put(w2[lcv], map2.get(w2[lcv]) + 1);
            }
        }
        for(int lcv = 0; lcv < w1.length; lcv ++){
            if(map1.get(w1[lcv]) != map2.get(w1[lcv])){
                return false;
            }
        }

        return isInDictionary(word);
    }

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

    public boolean claimAnagram (Word original, String proposed){

        if(isAnagram(original.getName().split(""), proposed.split(""))){
            int index = getWordIndex(original.getName());
            words.get(index).setName(proposed);
            return true;
        }
        return false;
    }

    public ArrayList<String> buildAnagrams (Word original){
        String myWord = original.getName();
        String [] myWordList = myWord.split("");

        ArrayList<String> toReturn = new ArrayList<>();

        for(int lcv = 0; lcv < dictionary.size(); lcv ++){
            String workingOn = dictionary.get(lcv);
            if(workingOn.length() > myWord.length()){
                break;
            }
            if(workingOn.length() < myWord.length() - 1){
                lcv += 1000;
            }
            if(myWord.length() == workingOn.length()){
                if(!wordsToNotUse.contains(dictionary.get(lcv)) && isAnagram(myWordList, workingOn.split(""))){
                    toReturn.add(workingOn);
//                    wordsToNotUse.add(dictionary.get(lcv));
                }
            }
        }

        return toReturn;
    }

    public ArrayList<ArrayList<String>> onlyLetters(){
        Set<ArrayList<String>> master = new HashSet<>();

        ArrayList<String> possible = new ArrayList<>();

        if(charactersUp.size() >= 3){
            for(int lcv = 0; lcv < charactersUp.size(); lcv ++){

                for(int lcv2 = 0; lcv2 < charactersUp.size(); lcv2 ++){
                    if(lcv != lcv2){

                        for(int lcv3 = 0; lcv3 < charactersUp.size(); lcv3 ++){
                            if(lcv2 != lcv3){

                                String addTo = charactersUp.get(lcv) + charactersUp.get(lcv2) + charactersUp.get(lcv3);
                                possible = buildAnagrams(new Word(addTo));

                                for(int lcv4 = 0; lcv4 < possible.size(); lcv4 ++){
                                    ArrayList<String> duo = new ArrayList<>();
                                    duo.add(addTo);
                                    duo.add(possible.get(lcv4));
                                    master.add(duo);
                                }

                                if(master.size() > 1){
                                    return new ArrayList<>(master);
                                }

                            }
                        }
                    }
                }
            }
        }

        return new ArrayList<>(master);
    }

    public ArrayList<ArrayList<String>> allWordPlusAnagrams(){

        Set<ArrayList<String>> master = new HashSet<>();
        ArrayList<String> possible = new ArrayList<>();

        for(int lcv = 0; lcv < words.size(); lcv ++){
            Random rand = new Random();
            int chance = rand.nextInt(10);
            if(words.get(lcv).getLength() < 6 ||
                    (words.get(lcv).getLength() >= 6 && chance == 5)) { // 10 % of the time, try to add onto a word with length >= 6

                possible = buildAnagrams(new Word(words.get(lcv).getName()));
                for (int lcv2 = 0; lcv2 < possible.size(); lcv2++) {
                    if (!possible.get(lcv2).equals(words.get(lcv).getName())) {
                        ArrayList<String> duo = new ArrayList<>();
                        duo.add(words.get(lcv).getName());
                        duo.add(possible.get(lcv2));
                        master.add(duo);
                    }
                }
                possible.clear();

                for (int lcv2 = 0; lcv2 < charactersUp.size(); lcv2++) {
                    String newPossibility = words.get(lcv).getName() + charactersUp.get(lcv2);

                    possible = buildAnagrams(new Word(newPossibility));
                    for (int lcv3 = 0; lcv3 < possible.size(); lcv3++) {
                        ArrayList<String> duo = new ArrayList<>();
                        duo.add((words.get(lcv).getName() + "-" + charactersUp.get(lcv2)));
                        duo.add(possible.get(lcv3));
                        master.add(duo);
                    }
                    possible.clear();

                    // word + 2 flipped letters
                    for (int lcv3 = 0; lcv3 < charactersUp.size(); lcv3++) {
                        if (lcv3 != lcv2) {
                            String nextPossibility = newPossibility + charactersUp.get(lcv3);
                            possible = buildAnagrams(new Word(nextPossibility));
                            for (int lcv4 = 0; lcv4 < possible.size(); lcv4++) {
                                ArrayList<String> duo = new ArrayList<>();
                                duo.add((words.get(lcv).getName() + "-" + charactersUp.get(lcv2) + "-" + charactersUp.get(lcv3)));
                                duo.add(possible.get(lcv4));
                                master.add(duo);
                            }
                            possible.clear();
                        }
                        if (master.size() > 2) {
                            return new ArrayList<>(master);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(master);
    }

    public ArrayList<ArrayList<String>> wordPlusWord(){

        Set<ArrayList<String>> master = new HashSet<>();
        ArrayList<String> possible = new ArrayList<>();

        for(int lcv = 0; lcv < words.size(); lcv ++){
            String wordOn = words.get(lcv).getName();
            if(wordOn.length() < 5) {
                for (int lcv2 = lcv + 1; lcv2 < words.size(); lcv2++) {
                    String wordAdded = words.get(lcv2).getName();

                    String newPossibility = wordOn + wordAdded;
                    possible = buildAnagrams(new Word(newPossibility));

                    for (int lcv3 = 0; lcv3 < possible.size(); lcv3++) {
                        ArrayList<String> duo = new ArrayList<>();
                        duo.add((wordOn + "-" + wordAdded));
                        duo.add(possible.get(lcv3));

                        master.add(duo);
                    }
                    possible.clear();

                    if (master.size() > 0) {
                        return new ArrayList<>(master);
                    }
                }
            }
        }

        return new ArrayList<>(master);
    }

    public void playGame(){

        System.out.println(charactersRemaining.size());

        String recentChange = "";
        String currentChange = "";

        String changeLog = "";

        while(charactersRemaining.size() > 0){

            flipTile();

            if(!recentChange.equals(currentChange)){
                String report = "| \033[38;5;10m Change \033[0m | " + getLastChange(currentChange);
                System.out.println();
                System.out.println(report);
                System.out.println();
                recentChange = currentChange;
                changeLog += report + "\n";
            }

            System.out.println(charactersUp + " " + charactersRemaining.size());
            System.out.println(colorCode()); // color codes the word list

            ArrayList<ArrayList<String>> anagramsFromLetters = onlyLetters();
            ArrayList<ArrayList<String>> anagramsWordPlus = allWordPlusAnagrams();
            ArrayList<ArrayList<String>> anagramsOfWordPlusWord = wordPlusWord();

            boolean selected = false;
            boolean toggle = false;
            while(!selected){
                Random rand = new Random();
                int choice = rand.nextInt(3);

                if(choice == 0 && anagramsFromLetters.size() > 0){

                    int secondChoice = rand.nextInt(anagramsFromLetters.size());
                    ArrayList<String> picked = anagramsFromLetters.get(secondChoice);
                    mergeLetters(picked.get(0).split(""), picked.get(1));

                    wordsToNotUse.add(picked.get(1));

                    currentChange = picked.get(0) + " " + picked.get(1) + " 0";

                    selected = true;
                }
                else if(choice == 1 && anagramsWordPlus.size() > 0){

                    int secondChoice = rand.nextInt(anagramsWordPlus.size());
                    ArrayList<String> picked = anagramsWordPlus.get(secondChoice);
                    String makeUp = picked.get(0);
                    String proposed = picked.get(1);

                    wordsToNotUse.add(picked.get(1));

                    String [] makeUpSplitUp = makeUp.split("-");
                    if(makeUpSplitUp.length == 1){
                        claimAnagram(new Word(makeUpSplitUp[0]), proposed);
                    }
                    else if(makeUpSplitUp.length == 2){
                        int origin = 0;
                        for(int lcv = 0; lcv < words.size(); lcv ++){
                            if(words.get(lcv).getName().equals(makeUpSplitUp[0])){
                                origin = lcv;
                                lcv = words.size();
                            }
                        }
                        mergeWordAndWord(words.get(origin), new Word(makeUpSplitUp[1]), proposed, false);
                    }
                    else{ // makeUpSplitUp.length == 3
                        int origin = 0;
                        for(int lcv = 0; lcv < words.size(); lcv ++){
                            if(words.get(lcv).getName().equals(makeUpSplitUp[0])){
                                origin = lcv;
                                lcv = words.size();
                            }
                        }
                        mergeWordAndWord(words.get(origin), new Word((makeUpSplitUp[1] + makeUpSplitUp[2])), proposed, false);
                    }

                    currentChange = picked.get(0) + " " + picked.get(1) + " 1";

                    selected = true;
                }
                else if(choice == 2 && anagramsOfWordPlusWord.size() > 0){

                    int secondChoice = rand.nextInt(anagramsOfWordPlusWord.size());
                    ArrayList<String> picked = anagramsOfWordPlusWord.get(secondChoice);
                    String makeUp = picked.get(0);
                    String proposed = picked.get(1);

                    wordsToNotUse.add(picked.get(1));

                    String [] makeUpSplitUp = makeUp.split("-");

                    int origin1 = 0;
                    int origin2 = 0;
                    for(int lcv = 0; lcv < words.size(); lcv ++){
                        if(words.get(lcv).getName().equals(makeUpSplitUp[0])){
                            origin1 = lcv;
                        }
                        if(words.get(lcv).getName().equals(makeUpSplitUp[1])){
                            origin2 = lcv;
                        }
                    }
                    mergeWordAndWord(words.get(origin1), words.get(origin2), proposed, true);

                    currentChange = picked.get(0) + " " + picked.get(1) + " 2";

                    selected = true;
                }
                else{
                    if(toggle){
                        selected = true;
                    }
                    else{
                        toggle = true;
                    }
                }
            }

        }

        System.out.println();
        System.out.println("--- END OF GAME ---");
        System.out.println("---- CHANGELOG ----");
        System.out.println();

        System.out.println(changeLog);
    }

}
