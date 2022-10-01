import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LevelWindow extends JFrame implements ActionListener{
	JPanel container;
	JButton level1;
	JButton level2;
	JButton level3;
	JButton help;


	LevelWindow() {
		level1 = new JButton("level 1 - 4 letter words");
		level1.setFocusable(false);
		level1.addActionListener(this);

		level2 = new JButton("level 2 - 5 letter words");
		level2.setFocusable(false);
		level2.addActionListener(this);

		level3 = new JButton("level 3 - 6 letter words");
		level3.setFocusable(false);
		level3.addActionListener(this);

		help = new JButton("Help");
		help.setFocusable(false);
		help.addActionListener(this);

		container = new JPanel();
		container.setLayout(new GridLayout(4, 1));
		container.add(level1);
		container.add(level2);
		container.add(level3);
		container.add(help);

		this.setTitle("Level Window");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.add(container);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		File fourLetterWords = new File("4 letters csv2.csv");
		File fiveLetterWords = new File("5 letters csv2.csv");
		File sixLetterWords = new File("6 letters csv2.csv");
		WordleGame wg = new WordleGame();
		if (e.getSource()==level1) {
			String word = wg.getRandomWord(fourLetterWords);
			GameWindow gw = new GameWindow("Level 1",word, 1, 450, 550);
			System.out.println(word);
		}
		if (e.getSource()==level2) {
			String word = wg.getRandomWord(fiveLetterWords);
			GameWindow gw = new GameWindow("Level 2", word, 2, 500, 550);
			System.out.println(word);
		}
		if (e.getSource()==level3) {
			String word = wg.getRandomWord(sixLetterWords);
			GameWindow gw = new GameWindow("Level 3", word, 3, 550, 550);
			System.out.println(word);
		}
		if (e.getSource()==help) {
			HelpFrame hf = new HelpFrame("How to Play");
		}
	}
}
