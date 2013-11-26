package picture;

import java.util.Arrays;

public class Node implements Comparable<Node> {
	
	private Node[] nodes = new Node[8];

	Integer references = 0;

	int red;
	int green;
	int blue;
	int redSum;
	int greenSum;
	int blueSum;
	
	int children;
	
	Node father;

	/**
	 * @return the father
	 */
	public Node getFather() {
		return father;
	}

	/**
	 * @param father the father to set
	 */
	public void setFather(Node father) {
		this.father = father;
	}

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
	public Integer getReferences() {
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

	@Override
	public int compareTo(Node arg0) {
		int returnInt = this.references.compareTo(arg0.getReferences());
		if (returnInt == 0) {
			
		}
		return returnInt;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + blueSum;
		result = prime * result + children;
		result = prime * result + ((father == null) ? 0 : father.hashCode());
		result = prime * result + green;
		result = prime * result + greenSum;
		result = prime * result + Arrays.hashCode(nodes);
		result = prime * result + red;
		result = prime * result + redSum;
		result = prime * result
				+ ((references == null) ? 0 : references.hashCode());
		result = prime
				* result
				+ ((referencesOfChilds == null) ? 0 : referencesOfChilds
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (blue != other.blue)
			return false;
		if (blueSum != other.blueSum)
			return false;
		if (children != other.children)
			return false;
		if (father == null) {
			if (other.father != null)
				return false;
		} else if (!father.equals(other.father))
			return false;
		if (green != other.green)
			return false;
		if (greenSum != other.greenSum)
			return false;
		if (!Arrays.equals(nodes, other.nodes))
			return false;
		if (red != other.red)
			return false;
		if (redSum != other.redSum)
			return false;
		if (references == null) {
			if (other.references != null)
				return false;
		} else if (!references.equals(other.references))
			return false;
		if (referencesOfChilds == null) {
			if (other.referencesOfChilds != null)
				return false;
		} else if (!referencesOfChilds.equals(other.referencesOfChilds))
			return false;
		return true;
	}
	
	

}
