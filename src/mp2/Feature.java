package mp2;

public class Feature {
	private double _entropy;
	private Word _word;
	private double _informationGain;

	public Feature(Word word, double entropy, double informationGain) {
		this.set_word(word);
		this.set_entropy(entropy);
		this.set_informationGain(informationGain);
	}

	public double get_entropy() {
		return _entropy;
	}

	public void set_entropy(double _entropy) {
		this._entropy = _entropy;
	}

	public Word get_word() {
		return _word;
	}

	private void set_word(Word _word) {
		this._word = _word;
	}

	public double get_informationGain() {
		return _informationGain;
	}

	public void set_informationGain(double _informationGain) {
		this._informationGain = _informationGain;
	}
}