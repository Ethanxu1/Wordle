import java.util.*;
import java.io.*;
import java.awt.*;

class WordleGame {
        // Uses CSW19 word list
        File fourLetterWords = new File("4 letters csv.csv");
        File fiveLetterWords = new File("5 letters csv.csv");
        File sixLetterWords = new File("6 letters csv.csv");

        public static void main(String[] args) {
                LevelWindow lw = new LevelWindow(); 
        }

        // <Summary> 
        // Produces a random word from one of the files
        // </Summary> 
        // @return value: returns the randomized word
        // @param file: the file to choose from
        public static String getRandomWord(File file) {
                ArrayList<String> words = convertFile(file);
                int index = (int) (Math.random() * words.size());
                return words.get(index);
        }

        // <Summary> 
        // Determines if the word is correct or not
        // </Summary> 
        // @return value: returns true if word matches the correct word and false if it doesn't
        // @param guess: user guess
        // @param correctWord: the correct word
        public static Boolean isCorrectWord(String guess, String correctWord) {
                if (guess.toLowerCase().equals(correctWord)) {return true;} 
                return false;
        }

        // <Summary> 
        // Evaluates letter positions and returns color based on position
        // </Summary>
        // @return value: returns an ArrayList of colors corresponding to each tile
        // @param userWord: the word the user guesses
        // @param correctWord: the correct word
        public static ArrayList<Color> checkLetterPositions(String userWord, String correctWord, int level) {
                Color[] colors = new Color[0];
                if (level == 1) {colors = new Color[4];}
                if (level == 2) {colors = new Color[5];}
                if (level == 3) {colors = new Color[6];}

                String[] splitWord = userWord.split("");
                String[] splitWord2 = correctWord.split("");

                ArrayList<String> guess = new ArrayList<>(Arrays.asList(splitWord)); 
                ArrayList<String> word = new ArrayList<>(Arrays.asList(splitWord2)); 

                ArrayList<Letter> wordLetters = new ArrayList<>();
                ArrayList<Letter> guessLetters = new ArrayList<>();

                ArrayList<ArrayList<String>> guessUniq = new ArrayList<>();
                ArrayList<ArrayList<String>> wordUniq = new ArrayList<>();

                for(String i : guess) {
                        guessLetters.add(new Letter(i, countAll(i, userWord)));
                        guessUniq = removeDuplicatesAndConvert(guessLetters);
                } 

                for(String i : word) {
                        wordLetters.add(new Letter(i, countAll(i, correctWord)));
                        wordUniq = removeDuplicatesAndConvert(wordLetters);
                }

                // loop through wordUniq and figure out if is uniq situation
                // and set weights for the numbers
                // make function to assign weights
          
                ArrayList<String> used = new ArrayList<>();
                for(int i = 0; i < guess.size(); i++) {
                        System.out.println(countInArray(guess.get(i), used));
                        System.out.println(extract(guess.get(i), wordUniq));
                        if (correctWord.indexOf(guess.get(i)) == i) {
                                if (countInArray(guess.get(i), used) < extract(guess.get(i), wordUniq)) {
                                        colors[i] = new Color(112, 172, 100); // green
                                        used.add(guess.get(i));
                                } else {
                                        colors[i] = new Color(112, 172, 100); // green
                                        colors[guess.indexOf(guess.get(i))] = new Color(128, 124, 124);
                                }
                        } else if (correctWord.contains(guess.get(i))) {
                                if (countInArray(guess.get(i), used) < extract(guess.get(i), wordUniq)) {
                                        colors[i] = new Color(208, 180, 92); // yellow
                                        used.add(guess.get(i));
                                }
                        } else {
                                colors[i] = new Color(128, 124, 124); // grey
                        }
                }
                int left = 0; 
                
                for (int i = 0; i < colors.length; i++) {
                        if (colors[i] == null) {
                                colors[i] = new Color(128, 124, 124);
                        }
                }


                return new ArrayList<Color>(Arrays.asList(colors));
        }
        public static int extract(String value, ArrayList<ArrayList<String>> list) {
                String extractedVal = "0";
                for (ArrayList<String> x : list) {
                        if (x.get(0).equals(value)) {
                                extractedVal = x.get(1);
                        }
                };
                return Integer.parseInt(extractedVal);
        }
        public static int countInArray(String value, ArrayList<String> list) {
                int count = 0; 
                for (String x : list){
                        if (x.equals(value)) {
                                count++; 
                        }
                };
                return count;
        }

        public static int countAll(String letter, String word) {
                int count = 0; 
                String[] letters = word.split("");
                for(String i : letters) {
                        if(i.equals(letter)) {
                                count++;
                        }
                }
                return count;
        }

        public static ArrayList<ArrayList<String>> removeDuplicatesAndConvert(ArrayList<Letter> list) {
                ArrayList<ArrayList<String>> newList = new ArrayList<>();

                list.forEach((x) -> {
                        newList.add(new ArrayList<String>() {
                                {
                                        add(x.getLetter());
                                        add(Integer.toString(x.getCount()));
                                }
                        }); 
                });

                return newList;
        }

        // <Summary>
        // Checks if word is inside the wordlist
        // </Summary>
        // @return value: returns true if is in word list and false if not in word list 
        // @param word: the word you want to find
        // @param level: the level you are at (to determine which file to look in)
        public boolean isWord(String word, int level){
                System.out.println("-" + word.toLowerCase() + "-");
                if(level == 1) {
                        if(convertFile(fourLetterWords).contains(word.toLowerCase())) {return true;}
                } if(level == 2) {
                        if(convertFile(fiveLetterWords).contains(word.toLowerCase())) {return true;}
                } if(level == 3) {
                        if(convertFile(sixLetterWords).contains(word.toLowerCase())) {return true;}
                } return false;
        }

        // <Summary>
        // Take contents of file and store into an array list 
        // </Summary
        // @return value: ArrayList of words that were in the file
        // @param file: file to be converted into ArrayList
        public static ArrayList<String> convertFile(File file){

                ArrayList<String> words = new ArrayList<>();

                try {
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNextLine()) {
                                words.add(scanner.nextLine());
                        }
                } catch (FileNotFoundException e){
                        System.out.println("file not found");
                } 
                return words;
        }
}
