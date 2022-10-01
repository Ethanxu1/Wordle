import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.border.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

interface Result {
	public void end(String status);
}

public class GameWindow extends JFrame implements KeyListener{
	String word = "";
	int level = 0; 
	JPanel container; // panel for all labels
	JLabel resultText;
	JPanel result;
	ArrayList<JLabel> boxes;
	public ArrayList<Integer> lettersTyped = new ArrayList<>();
	private int rowIndex = 0;
	private int boxNum = 0;
	private String guess = "";
	private boolean canSet = true; // regulates the amount of enters pressed
	public int getRowIndex() {return rowIndex;}
	public void setRowIndex(int rowIndex) {this.rowIndex = rowIndex;}
	public int getBoxNum() {return boxNum;}
	public void setBoxNum(int boxNum) {this.boxNum = boxNum;}
	public boolean getCanSet() {return canSet;}
	public void setCanSet(boolean canSet) {this.canSet = canSet;}
	public String getGuess() {return guess;}
	public void setGuess(String guess) {this.guess = guess;}

	// <Summary> 
	// instantiates main variables and creates the GUI components 
	// </Summary>
	// @param name: the name of your window
	// @param word: the random word to guess
	// @param level: the level you are on
	// @param width: width of frame
	// @param height: height of frame
	GameWindow(String name, String word, int level, int width, int height) {
		this.word = word;
		this.level = level;

		resultText = new JLabel("", SwingConstants.CENTER);    
		result = new JPanel();
		result.setBounds(0, height/2-50, width, 100);
		result.add(resultText);
		result.setVisible(false);


		boxes = new ArrayList<>();
		for (int i = 1; i <= 36; i++) {
			JLabel label = new JLabel("", SwingConstants.CENTER);
			label.setBorder(new LineBorder(new Color(125, 125, 124), 2, true));
			label.setFont(new Font("Serif", Font.BOLD, 20));
			label.setForeground(Color.white);
			label.setOpaque(true);
			label.setBackground(new Color(33, 33, 32));
			boxes.add(label);  
		}

		container = new JPanel();
		container.setBounds(0, 0, width, height);
		container.setBackground(new Color(33, 33, 32));
		// adds all the boxes to the container
		if (level==1){
			container.setLayout(new GridLayout(6, 4, 3, 3));
			for (int i = 0; i < 24; i++) {
				container.add(boxes.get(i));
			}
		}
		if (level==2){
			container.setLayout(new GridLayout(6, 5, 3, 3));
			for (int i = 0; i < 30; i++) {
				container.add(boxes.get(i));
			}
		}
		if (level==3){
			container.setLayout(new GridLayout(6, 6, 3, 3));
			for (int i = 0; i < 36; i++) {
				container.add(boxes.get(i));
			}
		}

		// Frame settings
		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Frame is quit but does not end game
		this.setLayout(null);
		this.setSize(width, height + 36);
		this.add(result);
		this.add(container);
		this.addKeyListener(this);
		this.setVisible(true);
	} // end of GameWindow constructor

	// <Summary> 
	// Processes the keypresses and what they do ([a-z], backspace, enter)
	// </Summary> 
	// @param e: the event listener that is used 
	// @param a: the starting box num of the row you are on
	// @param b: the ending box num + 1 of the row you are on
	// @param c: the length of the row that you are on
	public void run(KeyEvent e, int a, int b, int c) {
		WordleGame wg = new WordleGame();
		Pattern pattern = Pattern.compile("[a-z]");
		Matcher matcher = pattern.matcher(Character.toString(e.getKeyChar()));


		Result gameResults = (x) -> {
			if (x.equals("win")) {
				resultText.setText("You Win!");
				result.setBackground(Color.green);
				result.setVisible(true);
				System.out.println("You Win!");
			} else {
				resultText.setText("You lost, the word was " + word);
				result.setBackground(Color.red);
				result.setVisible(true);
				System.out.println("You lost, the word was " + word);
			}
		};

		// used to make sure you can only press enter once per row
		if(getCanSet()) {
			setBoxNum(a);
			setCanSet(false);
		}

		if(getBoxNum() >= a && getBoxNum() <= b) { // if box num is between a and b
			if (getBoxNum() < b) { 
				if (matcher.find() || e.getKeyCode()==10){ // if it is [a-z] or enter key add to lettersTyped array
					matcher = pattern.matcher(Character.toString(e.getKeyChar()));
					lettersTyped.add(e.getKeyCode());
				}
			}
			if(e.getKeyCode()==8) { // backspace
				// split into 2 if statements becuase at the first box, you just want to delete the text and not go back another one
				if(getBoxNum() != a) {
					boxes.get(getBoxNum()-1).setText(""); // set text to blank
					lettersTyped.remove(lettersTyped.size()-1); // remove last element in arraylist
					boxes.get(getBoxNum()-1).setBorder(new LineBorder(new Color(125, 125, 124), 2, true));
					setBoxNum(getBoxNum() - 1);
				}
				if(getBoxNum() == a) {// a
					boxes.get(getBoxNum()).setText("");
				}
			} 
			// if(e.getKeyCode()==10 && getBoxNum() % c == 0 && lettersTyped.get(lettersTyped.size()-1) != 10) { // enter, end of line and first enter
			if(e.getKeyCode()==10 && lettersTyped.get(lettersTyped.size()-1) != 10) { // enter, end of line and first enter
				lettersTyped.forEach((x) -> {if(x !=10) setGuess(getGuess() + (char) ((int) x));}); // sets the guess variable
				guess = guess.substring(guess.length() - c); // gets last c letters
				ArrayList<Color> color= wg.checkLetterPositions(guess.toLowerCase(), word, level);
				if(wg.isCorrectWord(guess, word)){ // if guess is same as answer 
					for (int i = 0; i < c; i ++) { // sets colors for all the letters 
						boxes.get(getBoxNum() - (c-i)).setBackground(color.get(c-(c-i)));
					}
					gameResults.end("win");
				} else if (wg.isWord(guess, level)) { // if guess is in word list
					for (int i = 0; i < c; i ++) {// sets colors for all the letters
						boxes.get(getBoxNum() - (c-i)).setBackground(color.get(c-(c-i)));
					}
					setRowIndex(getRowIndex() + 1);  
					setCanSet(true); 
				} else {
					System.out.println("Not in word list");
				}
			} else { // not enter or backspace
				matcher = pattern.matcher(Character.toString(e.getKeyChar()));
				if (getBoxNum() != b && matcher.find()){ // between a and b and is valid char
					boxes.get(getBoxNum()).setText(Character.toString(e.getKeyChar()).toUpperCase()); // set the text
					boxes.get(getBoxNum()).setBorder(new LineBorder(new Color(171, 171, 169), 2, true)); // change border color
					setBoxNum(getBoxNum() + 1);
				}
			}
		} if (getRowIndex() == 6) {
			gameResults.end("loss");
		}

		} // end of run method

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
			if(level==1){
				if(getRowIndex()==0) {run(e, 0, 4, 4);}    
				if(getRowIndex()==1) {run(e, 4, 8, 4);}
				if(getRowIndex()==2) {run(e, 8, 12, 4);}
				if(getRowIndex()==3) {run(e, 12, 16, 4);}
				if(getRowIndex()==4) {run(e, 16, 20, 4);}
				if(getRowIndex()==5) {run(e, 20, 24, 4);}
			}
			if(level==2){  
				if(getRowIndex()==0) {run(e, 0, 5, 5);}
				if(getRowIndex()==1) {run(e, 5, 10, 5);}
				if(getRowIndex()==2) {run(e, 10, 15, 5);}
				if(getRowIndex()==3) {run(e, 15, 20, 5);}
				if(getRowIndex()==4) {run(e, 20, 25, 5);}
				if(getRowIndex()==5) {run(e, 25, 30, 5);}
			}
			if(level==3){
				if(getRowIndex()==0) {run(e, 0, 6, 6);}
				if(getRowIndex()==1) {run(e, 6, 12, 6);}
				if(getRowIndex()==2) {run(e, 12, 18, 6);}
				if(getRowIndex()==3) {run(e, 18, 24, 6);}
				if(getRowIndex()==4) {run(e, 24, 30, 6);}
				if(getRowIndex()==5) {run(e, 30, 36, 6);}
			}
		}// end of keyReleased method
	}
