package picture;

import java.util.Arrays;

/**
 * Diese Klasse kapselt einen Knoten im Octree für das Runterrechnen der Farben
 * in einem Bild.
 * 
 * @author cschulte
 * 
 */
public class Node implements Comparable<Node> {

	/**
	 * Kinder von diesem Knoten.
	 */
	private Node[] nodes = new Node[8];

	/**
	 * Anzahl der Referenzen.
	 */
	private Integer references = 0;

	/**
	 * ID.
	 */
	private Integer count = 0;

	/**
	 * Rotwert.
	 */
	private int red;
	/**
	 * Grünwert.
	 */
	private int green;
	/**
	 * Blauwert.
	 */
	private int blue;

	/**
	 * Anzahl der Kinder.
	 */
	int children;

	private Node father;

	/**
	 * @return the father
	 */
	public Node getFather() {
		return father;
	}

	/**
	 * @param father
	 *            the father to set
	 */
	public void setFather(Node father) {
		this.father = father;
	}

	// private void copyChildSums() {
	// if (getChildSum() == 0) {
	// return;
	// }
	// redSum = 0;
	// greenSum = 0;
	// blueSum = 0;
	// references = 0;
	// children = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// Node child = nodes[i];
	// if (child != null) {
	// child.copyChildSums();
	// red += child.red;
	// green += child.green;
	// blue += child.blue;
	//
	// referencesOfChilds += child.references;
	// if (child.getReferencesOfChilds() != null) {
	// referencesOfChilds += child.getReferencesOfChilds();
	// }
	// children++;
	// }
	// }
	// }

	// private int getChildSum() {
	// int sum = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// if (nodes[i] != null) {
	// sum++;
	// }
	// }
	// return sum;
	// }

	/**
	 * Gibt die Kinder des Knotens zurück.
	 * 
	 * @return Node[]
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * Gibt den Knoten an der Position index zurück.
	 * 
	 * @param index
	 *            Position
	 * @return Node an Position
	 */
	public Node getNode(int index) {
		return nodes[index];
	}

	/**
	 * Setzt den Node an der Position neu.
	 * 
	 * @param index
	 *            Position
	 * @param node
	 *            Node
	 */
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

	/**
	 * Gibt die ID zurück
	 * @return
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * Setzt die ID.
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Node \n{count=" + count + ", references=" + references
				+ ", children=" + children + ", red=" + red + ", green="
				+ green + ", blue=" + blue + ", \nnodes="
				+ Arrays.toString(nodes) + "}";
	}

	@Override
	public int compareTo(Node arg0) {
		int returnInt = this.references.compareTo(arg0.getReferences());
		if (returnInt == 0) {
			returnInt = this.count.compareTo(arg0.getCount());
		}
		return returnInt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blue;
		result = prime * result + children;
		result = prime * result + ((father == null) ? 0 : father.hashCode());
		result = prime * result + green;
		result = prime * result + Arrays.hashCode(nodes);
		result = prime * result + red;
		result = prime * result
				+ ((references == null) ? 0 : references.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (blue != other.blue)
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
		if (!Arrays.equals(nodes, other.nodes))
			return false;
		if (red != other.red)
			return false;
		if (references == null) {
			if (other.references != null)
				return false;
		} else if (!references.equals(other.references))
			return false;
		return true;
	}

}
