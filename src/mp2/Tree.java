package mp2;

import java.util.Stack;

public class Tree {

	private Node root;
	private Node previous;
	private int maxDepth;
	private Stack<Node> splitNodes;
	
	public Tree(int maxDepth) {
		setSplitNodes(new Stack<Node>());
		setMaxDepth(maxDepth);
	}
	
	public int ClassifyDocument(Document document, Node current) {
		while(!current.isLeaf() ) {
			if (document.get_words().containsKey(current.getName())) {
				Node next = current.getLeft_child();
				return ClassifyDocument(document, next);
			}
			else {
				Node next = current.getRight_child();
				return ClassifyDocument(document, next);
			}
		}
		
		if (current.getPredicted_label() == -1) {
			double val = (Math.random() * 2);
			return (int) val;
		}
		return current.getPredicted_label();
	}
	
	public void AddDecisionNode(int key, Feature feature, boolean split) {
		Node node = new Node(key, feature, split);
		
		if (getRoot() == null) {
			node.setDepth(0);
			setRoot(node);
			setPrevious(node);
			return;
		}
		else {
			node.setDepth(getPrevious().getDepth() + 1);
			Node focusNode = getPrevious(); //getRoot();
			Node parent;
			
			if (split) { getSplitNodes().push(node); }
			
			while(true) {
				parent = focusNode;
				
				Node leftChild = focusNode.getLeft_child();
				if (leftChild == null) {
					parent.setLeft_child(node);
					setPrevious(node);
					return;
				}
				Node rightChild = focusNode.getRight_child();
				if (rightChild == null) {
					parent.setRight_child(node);
					setPrevious(node);
					return;
				}
				focusNode = rightChild;
			}
		}
	}
	
	public void AddLeafNode(int key, int judgement){
		Node focus = getPrevious();
		Node leaf = new Node(key, judgement);
		Node parent;
		
		while(true) {
			parent = focus;
			if (focus.getLeft_child() == null) {
				parent.setLeft_child(leaf);
				return;
			}
			else {
				parent.setRight_child(leaf);
				return;
			}
		}
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	public Node getPrevious() {
		return previous;
	}

	public void setPrevious(Node previous) {
		this.previous = previous;
	}

	public Stack<Node> getSplitNodes() {
		return splitNodes;
	}

	private void setSplitNodes(Stack<Node> splitNodes) {
		this.splitNodes = splitNodes;
	}

	
	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
