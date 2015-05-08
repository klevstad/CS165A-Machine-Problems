package mp2;

public class Word {
	
	private String _name;
	private int _positiveOccurrences;
	private int _negativeOccurrences;
	private int _polarityOfWord;
	
	public Word(String name) {
		this.set_name(name);
	}
	
	public String get_name() {
		return _name;
	}
	private void set_name(String _name) {
		this._name = _name;
	}
	public int get_positiveOccurrences() {
		return _positiveOccurrences;
	}
	public void update_positiveOccurrences() {
		this._positiveOccurrences += 1;
	}
	public int get_negativeOccurrences() {
		return _negativeOccurrences;
	}
	public void update_negativeOccurrences() {
		this._negativeOccurrences += 1;
	}
	public int get_polarityOfWord() {
		return _polarityOfWord;
	}
	public void update_polarityOfWord() {
		this._polarityOfWord = get_positiveOccurrences() - get_negativeOccurrences();
	}
}