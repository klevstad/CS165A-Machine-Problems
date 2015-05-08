package mp2;

import java.util.HashMap;

public class Document {
	
	private int _label;
	private HashMap<String, Integer> _words;	

	public Document(int label, HashMap<String, Integer> words)
	{
		this.set_label(label);
		this.set_words(words);
	}

	public int get_label() {
		return _label;
	}

	private void set_label(int _label) {
		this._label = _label;
	}

	public HashMap<String, Integer> get_words() {
		return _words;
	}

	private void set_words(HashMap<String, Integer> _words) {
		this._words = _words;
	}
}