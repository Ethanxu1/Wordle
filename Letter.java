class Letter {
	String letter;
	int count; 

	public String getLetter() {return letter;}
	public void setLetter(String letter) {this.letter = letter;}
	public int getCount() {return count;}
	public void setCount(int count) {this.count = count;}

	Letter(String letter, int count) {
		this.letter = letter;
		this.count = count;
	}
}
