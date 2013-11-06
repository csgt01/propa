package picture;

import java.util.Arrays;

public class Node {
	
	private Node[] nodes = new Node[8];

	int references = 0;

	int red;
	int green;
	int blue;
	int redSum;
	int greenSum;
	int blueSum;
	
	int children;

	Integer referencesOfChilds = 0;

	public void copyChildSums() {
		if (getChildSum() == 0) {
			return;
		}
		redSum = 0;
		greenSum = 0;
		blueSum = 0;
		references = 0;
		children = 0;
		for (int i = 0; i < nodes.length; i++) {
			Node child = nodes[i];
			if (child != null) {
				child.copyChildSums();
				red += child.red;
				green += child.green;
				blue += child.blue;
				
				referencesOfChilds += child.references;
				if (child.getReferencesOfChilds() != null) {
					referencesOfChilds += child.getReferencesOfChilds();
				}
				children++;
			}
		}
	}

	private int getChildSum() {
		int sum = 0;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				sum++;
			}
		}
		return sum;
	}

	public Integer getReferencesOfChilds() {
		return referencesOfChilds;
	}

	public void setReferencesOfChilds() {
		if (null == referencesOfChilds) {
			referencesOfChilds = 1;
		} else {
			referencesOfChilds++;
		}
	}

	public void setReferencesOfChilds(Integer refs) {
		this.referencesOfChilds = refs;
	}

	public Node[] getNodes() {
		return nodes;
	}

	public Node getNode(int index) {
		return nodes[index];
	}

	public void setNode(int index, Node node) {
		nodes[index] = node;
	}

	/**
	 * @return the references
	 */
	public int getReferences() {
		return references;
	}

	/**
	 * @param references
	 *            the references to set
	 */
	public void setReferences(int references) {
		this.references = references;
	}

	/**
	 * @return the red
	 */
	public int getRed() {
		return red;
	}

	/**
	 * @param red
	 *            the red to set
	 */
	public void setRed(int red) {
		this.red = red;
	}

	/**
	 * @return the green
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * @param green
	 *            the green to set
	 */
	public void setGreen(int green) {
		this.green = green;
	}

	/**
	 * @return the blue
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * @param blue
	 *            the blue to set
	 */
	public void setBlue(int blue) {
		this.blue = blue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node \n{references=" + references + ", children=" + children + ", red=" + red + ", green="
				+ green + ", blue=" + blue + ", referencesOfChilds="
				+ referencesOfChilds + ", \nnodes=" + Arrays.toString(nodes)
				+ "}";
	}

}
