package mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import mp2.Document;
import mp2.Entropy;
import mp2.Parser;

public class DecisionTreeClassifier {

	private final int THRESHOLD = 10; // Threshold to specify the lowest polarity of a word that will be considered
	private final int MAXDEPTH = 500;

	private HashMap<Integer, Document> _positiveDocuments;
	private HashMap<Integer, Document> _negativeDocuments;
	private HashMap<String, Integer> _wordsInPositiveDocuments;
	private HashMap<String, Integer> _wordsInNegativeDocuments;
	private Parser _parser;
	private Entropy _entropyCalculator;
	private Tree myTree;
	private int treeId;
	
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("Please splecify your training and test files. Correct usage is: java 'DecistionTreeClassifier' <training set> <validation set>'");
			System.exit(1);
		}

		String trainingset = args[0];
		String testset = args[1];
		
		DecisionTreeClassifier classifier = new DecisionTreeClassifier();
		
		// Doing the training
		long startTraining = System.currentTimeMillis();

		classifier.executeTraining(trainingset);
		HashMap<Integer, Document> p_training = classifier.get_parser().get_positiveDocuments();
		HashMap<Integer, Document> n_training = classifier.get_parser().get_negativeDocuments();
		double trainingaccuracy = classifier.analyze(p_training, n_training, classifier.myTree, false);
		
		long timeSpentTraining = System.currentTimeMillis() - startTraining;
		
		// Doing the testing
		long startTesting = System.currentTimeMillis();
		
		classifier.executeTesting(testset);
		HashMap<Integer, Document> p_testing = classifier.get_parser().get_positiveDocuments();
		HashMap<Integer, Document> n_testing = classifier.get_parser().get_negativeDocuments();
		double testingaccuracy = classifier.analyze(p_testing, n_testing, classifier.myTree, true);
		
		long timeSpentTesting = System.currentTimeMillis() - startTesting;
		
		System.out.println(timeSpentTraining / 1000.0 + " seconds");
		System.out.println(timeSpentTesting / 1000.0 + " seconds");
		
		System.out.println(trainingaccuracy + " training accuracy");
		System.out.println(testingaccuracy + " testing accuracy");
	}
	
	public void executeTesting(String filename) {
		set_parser(new Parser());
		get_parser().readFile(filename, THRESHOLD);
	}
	
	public void executeTraining(String filename) {
		set_parser(new Parser());
		set_entropy(new Entropy());
		get_parser().readFile(filename, THRESHOLD);
		myTree = new Tree(MAXDEPTH);
		treeId = 0;
		
		set_positiveDocuments(copyMap(get_parser().get_positiveDocuments()));
		set_negativeDocuments(copyMap(get_parser().get_negativeDocuments()));
		HashMap<String, Word> allWords = get_parser().get_allWords();
		ArrayList<Feature> features = get_entropy().computeSortedInformationGains(allWords);
		
		HashMap<Integer, Document> positives = get_positiveDocuments();
		HashMap<Integer, Document> negatives = get_negativeDocuments();
		ArrayList<String> usedFeatures = new ArrayList<String>();
		buildTree(positives, negatives, features, usedFeatures);
	}
	
	public double analyze(HashMap<Integer, Document> positiveDocuments, HashMap<Integer, Document> negativeDocuments, Tree tree, boolean isTesting) {
		HashMap<Integer, Document> documents = new HashMap<Integer, Document>();
		documents.putAll(positiveDocuments);
		documents.putAll(negativeDocuments);
		
		SortedSet<Integer> keys = new TreeSet<Integer>(documents.keySet());
		
		int correct = 0;
		for(Integer key : keys) {
			Document document = documents.get(key);
			int judgement = tree.ClassifyDocument(document, tree.getRoot());
			if (isTesting) { System.out.println("Predicted label: " + judgement + " Actual label: " + document.get_label()); }
			if (judgement == document.get_label()) { correct += 1; }
		}
		return (double) correct / documents.size();
	}
	
	private void buildTree(HashMap<Integer, Document> positives, HashMap<Integer, Document> negatives, ArrayList<Feature> features, ArrayList<String> usedFeatures) {

		if (myTree.getPrevious() != null) {
			if (features.isEmpty() || myTree.getPrevious().getDepth() > myTree.getMaxDepth()) {
				// Adding a leaf node if we're out of features / reached max depth
				int polarity = positives.size() - negatives.size();
				
				if (polarity > 0) { myTree.AddLeafNode(treeId, 1); }
				else if (polarity < 0) { myTree.AddLeafNode(treeId, 0);}
				else { myTree.AddLeafNode(treeId, -1); }
				
				treeId++;

				if (myTree.getSplitNodes().isEmpty()) {return; }
				myTree.setPrevious(myTree.getSplitNodes().pop());
				return;
			}
		}
			
		Feature feature = features.get(0);
		usedFeatures.add(feature.get_word().get_name());

		if (feature.get_informationGain() == 1.0) {
			myTree.AddDecisionNode(treeId, feature, false);
			treeId++;
			
			if (feature.get_word().get_polarityOfWord() > 0) { myTree.AddLeafNode(treeId, 1); }
			else { myTree.AddLeafNode(treeId, 0); }
			treeId++;
			removeProcessedDocuments(positives, negatives, feature, false);
			features = recomputeTheRemainingFeaturesInformationGain(positives, negatives, usedFeatures);
			buildTree(positives, negatives, features, usedFeatures);
		}
		else {
			myTree.AddDecisionNode(treeId, feature, true);
			treeId++;
			branchOut(true, positives, negatives, features, usedFeatures); // Left branch after split
			branchOut(false, positives, negatives, features, usedFeatures); // Right branch after split
		}
		return;
	}
	
	private void branchOut(boolean goLeft, HashMap<Integer, Document> positives, HashMap<Integer, Document> negatives, ArrayList<Feature> features, ArrayList<String> usedFeatures) {
		HashMap<Integer, Document> copyPos = copyMap(positives);
		HashMap<Integer, Document> copyNeg = copyMap(negatives);
		ArrayList<Feature> copyFeatures = copyList(features);
		ArrayList<String> copyUsedFeatures = copyList(usedFeatures);

		if(goLeft) {
			removeProcessedDocuments(copyPos, copyNeg, features.get(0), true);
			copyFeatures = recomputeTheRemainingFeaturesInformationGain(copyPos, copyNeg, copyUsedFeatures);
		}
		else {
			removeProcessedDocuments(positives, negatives, features.get(0), false);
			copyFeatures.remove(0);	
		}
		
		buildTree(copyPos, copyNeg, copyFeatures, copyUsedFeatures);
	}
	
	private void removeProcessedDocuments(HashMap<Integer, Document> positives, HashMap<Integer, Document> negatives, Feature feature, boolean left) {

		Iterator<Entry<Integer, Document>> positiveIterator = positives.entrySet().iterator();
		Iterator<Entry<Integer, Document>> negativeIterator = negatives.entrySet().iterator();
		while (positiveIterator.hasNext()) {
		    Entry<Integer, Document> entry = positiveIterator.next();
		    if (left && !entry.getValue().get_words().containsKey(feature.get_word().get_name())) { positiveIterator.remove(); }
		    else if(!left && entry.getValue().get_words().containsKey(feature.get_word().get_name())) { positiveIterator.remove(); } 
		}

		while (negativeIterator.hasNext()) {
		    Entry<Integer, Document> entry = negativeIterator.next();
		    if (left && !entry.getValue().get_words().containsKey(feature.get_word().get_name())) { negativeIterator.remove(); }
		    else if(!left && entry.getValue().get_words().containsKey(feature.get_word().get_name())) { negativeIterator.remove(); }
		}
	}
	
	private ArrayList<Feature> recomputeTheRemainingFeaturesInformationGain(HashMap<Integer, Document> remainingPos, HashMap<Integer, Document> remainingNeg, ArrayList<String> usedFeatures) {
		HashMap<String, Word> allwords = new HashMap<String, Word>();
				
		for (Integer id : remainingPos.keySet()) {
			Document document = remainingPos.get(id);
			for (String word : document.get_words().keySet()) {
				if (usedFeatures.contains(word)) { continue; }
				if (allwords.containsKey(word)) { allwords.get(word).update_positiveOccurrences(); }
				else {
					Word w = new Word(word);
					w.update_positiveOccurrences();
					allwords.put(word, w);
				}
				allwords.get(word).update_polarityOfWord();
			}
		}
		
		for (Integer id : remainingNeg.keySet()) {
			Document document = remainingNeg.get(id);
			for (String word : document.get_words().keySet()) {
				if (usedFeatures.contains(word)) { continue; }
				if (allwords.containsKey(word)) { allwords.get(word).update_negativeOccurrences(); }
				else {
					Word w = new Word(word);
					w.update_negativeOccurrences();
					allwords.put(word, w);
				}
				allwords.get(word).update_polarityOfWord();
			}
		}
		
		
		HashMap<String, Word> newSet = new HashMap<String, Word>();
		for (String word : allwords.keySet()) {
			if (Math.abs(allwords.get(word).get_polarityOfWord()) >= THRESHOLD) { newSet.put(word, allwords.get(word)); }
		}
		return get_entropy().computeSortedInformationGains(newSet);
	}
	


	public HashMap<Integer, Document> get_positiveDocuments() {
		return _positiveDocuments;
	}

	public void set_positiveDocuments(HashMap<Integer, Document> _positiveDocuments) {
		this._positiveDocuments = _positiveDocuments;
	}

	public HashMap<Integer, Document> get_negativeDocuments() {
		return _negativeDocuments;
	}

	public void set_negativeDocuments(HashMap<Integer, Document> _negativeDocuments) {
		this._negativeDocuments = _negativeDocuments;
	}

	public HashMap<String, Integer> get_wordsInPositiveDocuments() {
		return _wordsInPositiveDocuments;
	}

	public void set_wordsInPositiveDocuments(
			HashMap<String, Integer> _wordsInPositiveDocuments) {
		this._wordsInPositiveDocuments = _wordsInPositiveDocuments;
	}

	public HashMap<String, Integer> get_wordsInNegativeDocuments() {
		return _wordsInNegativeDocuments;
	}

	public void set_wordsInNegativeDocuments(
			HashMap<String, Integer> _wordsInNegativeDocuments) {
		this._wordsInNegativeDocuments = _wordsInNegativeDocuments;
	}

	public Parser get_parser() {
		return _parser;
	}

	public void set_parser(Parser _parser) {
		this._parser = _parser;
	}

	public Entropy get_entropy() {
		return _entropyCalculator;
	}

	public void set_entropy(Entropy entropy) {
		this._entropyCalculator = entropy;
	}
	
	private <T, U>HashMap<T, U> copyMap(HashMap<T, U> original) {
		HashMap<T, U> copy = new HashMap<T, U>();
		for (T key : original.keySet()) { copy.put(key, original.get(key)); }
		return copy;
	}
	
	private <T> ArrayList<T> copyList(ArrayList<T> original) {
		ArrayList<T> copy = new ArrayList<T>();
		for (T feature : original) { copy.add(feature); }
		return copy;
	}
	
}
