import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class HelpFrame extends JFrame {
	ImageIcon rules;
	JLabel label;

	HelpFrame(String name) {
		rules = new ImageIcon("./Rules.png");

		label = new JLabel("", SwingConstants.CENTER);
		label.setIcon(rules);


		this.setTitle(name);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Frame is quit but does not end game
		this.setSize(500, 500);
		this.getContentPane().setBackground(new Color(18,18,19,255));

		this.setVisible(true);
		this.add(label);
	}
}
