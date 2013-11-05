package picture;

import java.util.Arrays;

public class Node {

	int references = 0;

	int red;
	int green;
	int blue;
	
	Integer referencesOfChilds = null;
	
	public Integer getReferencesOfChilds() {
		return referencesOfChilds;
	}
	
	public void setReferencesOfChilds() {
		if (null == referencesOfChilds) {
			referencesOfChilds = 1;
		} else {
			referencesOfChilds ++;
		}
	}
	
	public void setReferencesOfChilds(Integer refs) {
		this.referencesOfChilds = refs;
	}

	private Node[] nodes = new Node[8];

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node \n{references=" + references + ", red=" + red + ", green="
				+ green + ", blue=" + blue + ", referencesOfChilds="
				+ referencesOfChilds + ", \nnodes=" + Arrays.toString(nodes)
				+ "}";
	}

	

}
