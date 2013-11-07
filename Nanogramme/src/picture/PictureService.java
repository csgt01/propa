package picture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Diese Klasse ist für das Erstellen eines Rätsels aus einem Bild zuständig.
 * 
 * @author cschulte
 * 
 */
public class PictureService {

	/**
	 * Fügt einen Pixel (Farbe) dem root Element hinzu. Dies muss für alle Pixel
	 * des Bildes gemacht werden.
	 * 
	 * @param color
	 *            Die Farbe des Pixels.
	 * @param root
	 *            Das root Element, in das alle Pixel hinzugefügt werden.
	 */
	public void insertNode(Color color, Node root) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		String binary11 = Integer.toBinaryString(red);
		String binary12 = Integer.toBinaryString(green);
		String binary13 = Integer.toBinaryString(blue);

		if (binary11.length() < 8) {
			for (int i = binary11.length(); i < 8; i++) {
				binary11 = "0" + binary11;
			}
		}
		// System.out.println(binary11);

		if (binary12.length() < 8) {
			for (int i = binary12.length(); i < 8; i++) {
				binary12 = "0" + binary12;
			}
		}

		// System.out.println(binary13);

		if (binary13.length() < 8) {
			for (int i = binary13.length(); i < 8; i++) {
				binary13 = "0" + binary13;
			}
		}
		// System.out.println(binary13);

		Node node = root;
		// Node lastNode = root;
		for (int i = 0; i < 8; i++) {
			String indexString = ""
					+ (binary11.charAt(i) + "" + binary12.charAt(i) + "" + binary13
							.charAt(i));
			int index = Integer.parseInt(indexString, 2);
			// System.out.println(index);
			if (node.getNode(index) == null) {
				node.setNode(index, new Node());
			}
			// lastNode = node;
			node = node.getNode(index);
		}
		// lastNode.setReferencesOfChilds();
		node.setRed(node.getRed() + red);
		node.setGreen(node.getGreen() + green);
		node.setBlue(node.getBlue() + blue);
		node.setReferences(node.getReferences() + 1);

	}

	/**
	 * Skalliert das Bild.
	 * 
	 * @param originalImage
	 */
	public BufferedImage scalePicture(int width, int height,
			Image originalImage, int type) {
		BufferedImage bi = new BufferedImage(width, height, type);
		Graphics2D g = bi.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return bi;
	}

	// public void setReferenceChilds(Node root) {
	// boolean mustDo = false;
	// for (int i = 0; i < root.getNodes().length; i++) {
	// if (root.getNode(i) != null) {
	// if (root.getNode(i).getReferences() == 0) {
	// setReferenceChilds(root.getNode(i));
	// } else {
	// if (root.getReferencesOfChilds() == null) {
	// mustDo = true;
	// }
	// }
	// }
	// }
	// if (mustDo) {
	// int childsReferences = 0;
	// for (int i = 0; i < root.getNodes().length; i++) {
	// if (root.getNode(i) != null) {
	// childsReferences += root.getNode(i).getReferences();
	// }
	// }
	// root.setReferencesOfChilds(childsReferences);
	// }
	// }

	public void reduceColors(Node root) {
		root.setReferences(root.getReferencesOfChilds());
		root.setReferencesOfChilds(0);
		root.children = 0;
		Node[] nodes = root.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			if (root.getNode(i) != null) {
				reduceColors(root.getNode(i));
				root.setNode(i, null);
			}
		}
	}

	public Node findNodeWithLessChildsReferences(Node root) {
		Node node = root;
		for (int i = 0; i < root.getNodes().length; i++) {
			if (root.getNode(i) != null) {
				Node node2 = findNodeWithLessChildsReferences(root.getNode(i));
				// if (node == null && ) {
				//
				// }
				// else
				if (node2.getReferencesOfChilds() <= node
						.getReferencesOfChilds() && node2.children > 1) {
					node = node2;
				}
			}
		}
		return node;
	}

	public int getNumbersOfLeafs(Node root) {
		int result = 0;

		Node[] nodes = root.getNodes();
		boolean isLeaf = true;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				isLeaf = false;
				result += getNumbersOfLeafs(nodes[i]);
			}
		}
		if (isLeaf) {
			result = 1;
		}
		return result;
	}
	
	public LinkedList<Color> getColorsOfLeafs(Node root, LinkedList<Color> colors) {
		Node[] nodes = root.getNodes();
		boolean isLeaf = true;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				isLeaf = false;
				colors = getColorsOfLeafs(nodes[i], colors);
			}
		}
		if (isLeaf) {
			colors.add(new Color((root.getRed()/root.getReferences()),(root.getGreen()/root.getReferences()),(root.getBlue()/root.getReferences())));
		}
		return colors;
	}

	public void cluster(Node less) {
		System.out.println("cluster");
		Node similiar1 = null;
		Node similiar2 = null;
		int sim1 = 0;
		int sim2 = 0;
		double distance = 800.0;
		for (int i = 0; i < less.getNodes().length; i++) {
			Node node1 = less.getNode(i);
			if (node1 != null) {
				for (int j = (i + 1); j < less.getNodes().length; j++) {
					Node node2 = less.getNode(j);
					if (node2 != null) {
						Double newDistance = getDistance(node1, node2);
						System.out.println(newDistance);
						if (newDistance != null && newDistance < distance) {
							distance = newDistance;
							similiar1 = node1;
							sim1 = i;
							similiar2 = node2;
							sim2 = j;
						}
					}
				}
			}
		}
		less.getNode(sim1).setReferences(
				less.getNode(sim1).getReferences()
						+ less.getNode(sim2).getReferences());
		less.getNode(sim1).setRed(
				less.getNode(sim1).getRed() + less.getNode(sim2).getRed());
		less.getNode(sim1).setGreen(
				less.getNode(sim1).getGreen() + less.getNode(sim2).getGreen());
		less.getNode(sim1).setBlue(
				less.getNode(sim1).getBlue() + less.getNode(sim2).getBlue());
		less.setNode(sim2, null);
		System.out.println("sim1:" + similiar1 + "  \n sim2:" + similiar2);
	}

	private Double getDistance(Node node1, Node node2) {
		if (node1.getReferences() != 0 && node2.getReferences() != 0) {
			float r1 = node1.red / node1.getReferences();
			float g1 = node1.green / node1.getReferences();
			float b1 = node1.blue / node1.getReferences();
			float r2 = node2.red / node2.getReferences();
			float g2 = node2.green / node2.getReferences();
			float b2 = node2.blue / node2.getReferences();
			return Math.sqrt((Math.pow((r2 - r1), 2.0)
					+ Math.pow((g2 - g1), 2.0) + Math.pow((b2 - b1), 2.0)));
		} else {
			return null;
		}
	}

	public void mapPictureToColors(BufferedImage resizedImage,
			LinkedList<Color> colors) {
		
	}

}
