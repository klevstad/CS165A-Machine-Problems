package mp2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Entropy {
	
	public Entropy() {
		
	}
	
	public double calculateInformationGain(double parentEntropy, double entropy){
		if(entropy == 0.0) { return parentEntropy - entropy; }
		return parentEntropy - entropy;
	}
	
	public double calculateEntropy(int positiveInstances, int negativeInstances, int totalFeatures){
		if(positiveInstances == 0 || negativeInstances == 0) {return 0.0;}
		int total = positiveInstances + negativeInstances;
		double p = (double) positiveInstances/total;
		double q = (double) negativeInstances/total;

		double first = - p * (Math.log(p)/Math.log(2));
		double second = q * (Math.log(q)/Math.log(2));

		return first - second;
	}
	
	public ArrayList<Feature> computeSortedInformationGains(HashMap<String, Word> words){
		double parentEntropy = 1.0;
		ArrayList<Feature> features = new ArrayList<Feature>();
		for (String word : words.keySet()) {
			Word w = words.get(word);
			double entropy = calculateEntropy(w.get_positiveOccurrences(), w.get_negativeOccurrences(), words.size());
			double informationGain = calculateInformationGain(parentEntropy, entropy);
			if(informationGain > 0.05) { 
				Feature feature = new Feature(w, entropy, informationGain);
				features.add(feature);
			}
		}
		
		Collections.sort(features, new Comparator<Feature>(){
		    public int compare(Feature f1, Feature f2) {
		        return ((int) (f1.get_informationGain() * 100000)) - ((int) (f2.get_informationGain() * 100000));
		    }
		});
		
		Collections.reverse(features);
		return features;
	}
}
