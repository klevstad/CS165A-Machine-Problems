package mp2;

public class Node {
	
	private int node_id;
	private String name;
	private Feature feature;
	private Node left_child;
	private Node right_child;
	private int predicted_label;
	private int polarity;
	private int depth;
	private boolean leaf;
	private boolean split;
	
	// Decision node
	public Node(int id, Feature feature, boolean split) {
		setNode_id(id);
		setFeature(feature);
		setName(feature.get_word().get_name());
		setLeaf(false);
		setSplit(split);
	}
	
	// Leaf node
	public Node(int id, int judgement) {
		setNode_id(id);
		setPredicted_label(judgement);
		setLeaf(true);
	}
	
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public Node getLeft_child() {
		return left_child;
	}
	public void setLeft_child(Node left_child) {
		this.left_child = left_child;
	}
	public Node getRight_child() {
		return right_child;
	}
	public void setRight_child(Node right_child) {
		this.right_child = right_child;
	}
	public int getPredicted_label() {
		return predicted_label;
	}
	public void setPredicted_label(int predicted_label) {
		this.predicted_label = predicted_label;
	}
	public int getPolarity() {
		return polarity;
	}
	public void setPolarity(int polarity) {
		this.polarity = polarity;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
}