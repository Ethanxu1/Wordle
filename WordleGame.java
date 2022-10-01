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

		ArrayList<String> guessUniq = new ArrayList<>();
		ArrayList<String> wordUniq = new ArrayList<>();

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
		ArrayList<String> special = new ArrayList<String>(); 

		for(int a = 0; a < wordUniq.size(); a++) {
			for(int b = 0; b < guessUniq.size(); b++) {
				String w = wordUniq.get(a); 
				String g = guessUniq.get(b);
        
				if(w.substring(0, 1).equals(g.substring(0, 1))) {
					if(Integer.valueOf(w.substring(1, 2)) < Integer.valueOf(g.substring(1, 2))) {
						special.add(w);
            
					} else {
						if (correctWord.indexOf(guess.get(a)) == a) {
							colors[a] = new Color(112, 172, 100); // green 
              
						} else if (correctWord.contains(guess.get(a))) {
							colors[a] = new Color(208, 180, 92); // yellow
              
						} else {
							colors[a] = new Color(128, 124, 124); // grey
						}
					}
				}
			}	
		}
		// ArrayList<String>
		for(int a = 0; a < guess.size(); a++) {	
			for(int b = 0; b < special.size(); b++) {
        
				if (guess.get(a).equals(special.get(b).substring(0, 1))) {
					continue;
          
				} else {
					if (correctWord.indexOf(guess.get(a)) == a) {
						colors[a] = new Color(112, 172, 100); // green
            
					} else if (correctWord.contains(guess.get(a))) {
						colors[a] = new Color(208, 180, 92); // yellow
            
					} else{
						colors[a] = new Color(128, 124, 124); // grey
					}
				}
			}	
		}

		int left = 0; 
		for (int a = 0; a < colors.length; a++) {
			for (int b = 0; b < special.size(); b++) {
				left = Integer.valueOf(special.get(b).substring(1, 2));
				if (colors[a] == null) {
					if (correctWord.indexOf(guess.get(a)) == a) {
						colors[a] = new Color(112, 172, 100); // green
						left--;
					}
				}
			}
		}
		for (int a = 0; a <= left; a++) {
			for (int b = 0; b < colors.length; b++) {
				if (colors[b] == null) {
					colors[b] = new Color(128, 124, 124);
				}	
			}
		}
		

    return new ArrayList<Color>(Arrays.asList(colors));
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

	public static ArrayList<String> removeDuplicatesAndConvert(ArrayList<Letter> list) {
		ArrayList<String> newList = new ArrayList<>();

		list.forEach((x) -> {
			newList.add(x.getLetter() + x.getCount()); 
		});

		// convert to linkedHashSet because it doesn't allow duplicates
		Set<String> set = new LinkedHashSet<>();
		set.addAll(newList);
		newList.clear();
		newList.addAll(set);

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
