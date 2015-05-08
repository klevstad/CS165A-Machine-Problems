package mp2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Parser {
	
	public static final List<String> STOP_WORDS = Arrays.asList("i", "a","able","about","above","abroad","according","accordingly","across","actually","adj","after","afterwards","again","against","ago","ahead","ain't","all","allow","allows","almost","alone","along","alongside","already","also","although","always","am","amid","amidst","among","amongst","an","and","another","any","anybody","anyhow","anyone","anything","anyway","anyways","anywhere","apart","appear","appreciate","appropriate","are","aren't","around","as","a's","aside","ask","asking","associated","at","available","away","awfully","back","backward","backwards","be","became","because","become","becomes","becoming","been","before","beforehand","begin","behind","being","believe","below","beside","besides","best","better","between","beyond","both","brief","but","by","came","can","cannot","cant","can't","caption","cause","causes","certain","certainly","changes","clearly","c'mon","co","co.","com","come","comes","concerning","consequently","consider","considering","contain","containing","contains","corresponding","could","couldn't","course","c's","currently","dare","daren't","definitely","described","despite","did","didn't","different","directly","do","does","doesn't","doing","done","don't","down","downwards","during","each","edu","eg","eight","eighty","either","else","elsewhere","end","ending","enough","entirely","especially","et","etc","even","ever","evermore","every","everybody","everyone","everything","everywhere","ex","exactly","example","except","fairly","far","farther","few","fewer","fifth","first","five","followed","following","follows","for","forever","former","formerly","forth","forward","found","four","from","further","furthermore","get","gets","getting","given","gives","go","goes","going","gone","got","gotten","greetings","had","hadn't","half","happens","hardly","has","hasn't","have","haven't","having","he","he'd","he'll","hello","help","hence","her","here","hereafter","hereby","herein","here's","hereupon","hers","herself","he's","hi","him","himself","his","hither","hopefully","how","howbeit","however","hundred","i'd","ie","if","ignored","i'll","i'm","immediate","in","inasmuch","inc","inc.","indeed","indicate","indicated","indicates","inner","inside","insofar","instead","into","inward","is","isn't","it","it'd","it'll","its","it's","itself","i've","just","k","keep","keeps","kept","know","known","knows","last","lately","later","latter","latterly","least","less","lest","let","let's","like","liked","likely","likewise","little","look","looking","looks","low","lower","ltd","made","mainly","make","makes","many","may","maybe","mayn't","me","mean","meantime","meanwhile","merely","might","mightn't","mine","minus","miss","more","moreover","most","mostly","mr","mrs","much","must","mustn't","my","myself","name","namely","nd","near","nearly","necessary","need","needn't","needs","neither","never","neverf","neverless","nevertheless","new","next","nine","ninety","no","nobody","non","none","nonetheless","noone","no-one","nor","normally","not","nothing","notwithstanding","novel","now","nowhere","obviously","of","off","often","oh","ok","okay","old","on","once","one","ones","one's","only","onto","opposite","or","other","others","otherwise","ought","oughtn't","our","ours","ourselves","out","outside","over","overall","own","particular","particularly","past","per","perhaps","placed","please","plus","possible","presumably","probably","provided","provides","que","quite","qv","rather","rd","re","really","reasonably","recent","recently","regarding","regardless","regards","relatively","respectively","right","round","said","same","saw","say","saying","says","second","secondly","see","seeing","seem","seemed","seeming","seems","seen","self","selves","sensible","sent","serious","seriously","seven","several","shall","shan't","she","she'd","she'll","she's","should","shouldn't","since","six","so","some","somebody","someday","somehow","someone","something","sometime","sometimes","somewhat","somewhere","soon","sorry","specified","specify","specifying","still","sub","such","sup","sure","take","taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that'll","thats","that's","that've","the","their","theirs","them","themselves","then","thence","there","thereafter","thereby","there'd","therefore","therein","there'll","there're","theres","there's","thereupon","there've","these","they","they'd","they'll","they're","they've","thing","things","think","third","thirty","this","thorough","thoroughly","those","though","three","through","throughout","thru","thus","till","to","together","too","took","toward","towards","tried","tries","truly","try","trying","t's","twice","two","un","under","underneath","undoing","unfortunately","unless","unlike","unlikely","until","unto","up","upon","upwards","us","use","used","useful","uses","using","usually","v","value","various","versus","very","via","viz","vs","want","wants","was","wasn't","way","we","we'd","welcome","well","we'll","went","were","we're","weren't","we've","what","whatever","what'll","what's","what've","when","whence","whenever","where","whereafter","whereas","whereby","wherein","where's","whereupon","wherever","whether","which","whichever","while","whilst","whither","who","who'd","whoever","whole","who'll","whom","whomever","who's","whose","why","will","willing","wish","with","within","without","wonder","won't","would","wouldn't","yes","yet","you","you'd","you'll","your","you're","yours","yourself","yourselves","you've","zero");	

	private HashMap<Integer, Document> _positiveDocuments;
	private HashMap<Integer, Document> _negativeDocuments;
	private HashMap<String, Word> _allWords;
	private HashMap<String, Integer> _wordFrequencies;
	private PorterStemmer _stemmer;

	public Parser() {
		this.set_positiveDocuments(new HashMap<Integer, Document>());
		this.set_negativeDocuments(new HashMap<Integer, Document>());
		this.set_allWords(new HashMap<String, Word>());
		this.set_stemmer(new PorterStemmer());
		this.set_wordFrequencies(new HashMap<String, Integer>());
	}
	
	public void readFile(String filename, int noGainThreshold) {
		String seperator = "\t";
		int i = 0;
		try {
	        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
	        String line;	        
	        while ((line = br.readLine()) != null) {

	        	String[] splitted = line.split(seperator,2);
	        	int label = Integer.parseInt(splitted[0]);
	        	String value = splitted[1];
	        	HashMap<String, Integer> words = buildWordFrequenciesForDocument(label, value);
	        	Document doc = new Document(label, words);
	        	
	        	if (doc.get_label() == 0) { get_negativeDocuments().put(i, doc); }
	        	else { get_positiveDocuments().put(i, doc); }
        		i++;
	        }
	        br.close();
	    } catch (FileNotFoundException e){
	    	e.printStackTrace();
	    	System.exit(1);
	    } catch (IOException e){
	    	e.printStackTrace();
	    }
		removeNoGainWords(noGainThreshold);
	}
	
	private void removeNoGainWords(int threshold) {
		HashMap<String, Word> newSet = new HashMap<String, Word>();
		for (String word : get_allWords().keySet()) {
			Word w = get_allWords().get(word);
			if (Math.abs(w.get_polarityOfWord()) >= threshold) { newSet.put(word, w); }
		}
		set_allWords(newSet);
	}
	
	private HashMap<String, Integer> buildWordFrequenciesForDocument(int label, String line) {
		String sanitized = sanitizeLine(line);
		HashMap<String, Integer> frequencies = new HashMap<String, Integer>();
		String[] value = sanitized.split(" ");
		
		ArrayList<String> wordsInThisDocument = new ArrayList<String>();
		
		for (String word : value) {
			if (word.isEmpty()) { continue; }
			word = get_stemmer().stemWord(word);
			
			int count = frequencies.containsKey(word) ? frequencies.get(word) : 0;
			frequencies.put(word, count + 1);
			
			if ( get_wordFrequencies().containsKey(word)) { get_wordFrequencies().put(word, get_wordFrequencies().get(word) + 1); }
			else { get_wordFrequencies().put(word, 1); }
			
			switch (label) {
			case 0:
				if (wordsInThisDocument.contains(word)) {
					continue;
				}
				if (get_allWords().containsKey(word) && !wordsInThisDocument.contains(word)) {
					wordsInThisDocument.add(word);
					get_allWords().get(word).update_negativeOccurrences();
					break;
				}
				else {
					Word w = new Word(word);
					w.update_negativeOccurrences();
					get_allWords().put(word, w);
					wordsInThisDocument.add(word);
					break;
				}
			case 1:
				if (wordsInThisDocument.contains(word)) {
					continue;
				}
				if (get_allWords().containsKey(word) && !wordsInThisDocument.contains(word)) {
					get_allWords().get(word).update_positiveOccurrences();
					wordsInThisDocument.add(word);
					break;
				}
				else {
					Word w = new Word(word);
					w.update_positiveOccurrences();
					get_allWords().put(word, w);
					wordsInThisDocument.add(word);
					break;
				}
			}
			get_allWords().get(word).update_polarityOfWord();
		}
		return frequencies;
	}
	
	private String sanitizeLine(String line) {
		String sanitized = line;
		sanitized = sanitized.replaceAll("\\<[^ >]*>","");
		sanitized = sanitized.replaceAll("<br />","");
		sanitized = sanitized.replaceAll("[^a-zA-Z_\\s]", "");
		sanitized = sanitized.replaceAll("  ", " ");
		sanitized = sanitized.toLowerCase();
		sanitized = removeStopWords(sanitized);
		return sanitized;
	}
	
	private String removeStopWords(String str){
		String[] splitted = str.split(" ");
		List<String> s = new LinkedList<String>(Arrays.asList(splitted));
		s.removeAll(STOP_WORDS);
		StringBuilder sb = new StringBuilder();
		for(String item : s) { sb.append(item + " "); }
		return sb.toString().trim();
	}
	
	public HashMap<Integer, Document> get_positiveDocuments() {
		return _positiveDocuments;
	}

	public HashMap<Integer, Document> get_negativeDocuments() {
		return _negativeDocuments;
	}

	private void set_positiveDocuments(HashMap<Integer, Document> _positiveDocuments) {
		this._positiveDocuments = _positiveDocuments;
	}

	private void set_negativeDocuments(HashMap<Integer, Document> _negativeDocuments) {
		this._negativeDocuments = _negativeDocuments;
	}

	public PorterStemmer get_stemmer() {
		return _stemmer;
	}

	private void set_stemmer(PorterStemmer _stemmer) {
		this._stemmer = _stemmer;
	}

	public HashMap<String, Word> get_allWords() {
		return _allWords;
	}

	private void set_allWords(HashMap<String, Word> _allWords) {
		this._allWords = _allWords;
	}

	public HashMap<String, Integer> get_wordFrequencies() {
		return _wordFrequencies;
	}

	private void set_wordFrequencies(HashMap<String, Integer> _wordFrequencies) {
		this._wordFrequencies = _wordFrequencies;
	}
}