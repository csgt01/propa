package picture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * Diese Klasse ist für das Erstellen eines Rätsels aus einem Bild zuständig.
 * 
 * @author cschulte
 * 
 */
public class PictureService {

	int count = 0;

	public BufferedImage getDownColoredPicture(BufferedImage resizedImage,
			int numberOfColors) {
		Node root = new Node();
		root.setCount(0);

		for (int i = 0; i < resizedImage.getHeight(); i++) {
			for (int j = 0; j < resizedImage.getWidth(); j++) {
				insertNode(new Color(resizedImage.getRGB(j, i)), root);
			}
		}
		// System.out.println(root);
		LinkedList<Color> colors = new LinkedList<Color>();
		colors = getColorsOfLeafs(root, colors);
		// System.out.println(colors);

		return mapPictureToColors(resizedImage, colors);
	}

	public BufferedImage loadAndDowColorPicture(String file, int height, int width, int numberOfColors) {
		BufferedImage resizedImage = null;
		try {
			BufferedImage oi = ImageIO.read(new File(file));
			int type = oi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : oi
					.getType();
			
			resizedImage = oi;
			
			Node root = new Node();

			for (int i = 0; i < resizedImage.getHeight(); i++) {
				for (int j = 0; j < resizedImage.getWidth(); j++) {
					insertNode(new Color(resizedImage.getRGB(j, i)), root);
				}
			}
			TreeSet<Node> fathers = new TreeSet<Node>();
			fathers = getFathersOfLeafs(root, true, fathers);

			while (fathers.size() > 7) {
				fathers = reduceColorsInFathers(fathers);
			}
			TreeSet<Node> fathersOfLeafs = new TreeSet<Node>();
			fathersOfLeafs = getFathersOfLeafs(root, true, fathersOfLeafs);
			System.out.println(fathersOfLeafs.size());
			while (fathersOfLeafs.size() > 15) {
				fathersOfLeafs = reduceColorsInFathers(fathersOfLeafs);
			}
			while (getNumbersOfLeafs(root, false) > numberOfColors) {
				if (getChildrenOfNode(fathersOfLeafs.first()) > (getNumbersOfLeafs(
						root, false) - 4)) {
					cluster(fathersOfLeafs.first());
				} else {
					fathersOfLeafs = reduceColorsInFathers(fathersOfLeafs);
				}
			}
			LinkedList<Color> colors = new LinkedList<Color>();
			colors = getColorsOfLeafs(root, colors);
			resizedImage = mapPictureToColors(resizedImage, colors);
			ImageIO.write(resizedImage, "jpg", new File("testBla.jpg"));
			// TODO: resize, but when
			resizedImage = scalePicture(width, height, resizedImage, type);
			ImageIO.write(resizedImage, "jpg", new File("testResi.jpg"));
						
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return resizedImage;
	}

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
		root.setReferences(root.getReferences() + 1);
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
				count++;
				node.setNode(index, new Node());
				node.getNode(index).setCount(count);
			}
			// lastNode = node;
			node.getNode(index).setFather(node);
			node = node.getNode(index);
			node.setReferences(node.getReferences() + 1);
		}
		// lastNode.setReferencesOfChilds();

		node.setRed(node.getRed() + red);
		node.setGreen(node.getGreen() + green);
		node.setBlue(node.getBlue() + blue);
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

//	public Node findNodeWithLessChildsReferences(Node root) {
//		Node node = root;
//		for (int i = 0; i < root.getNodes().length; i++) {
//			if (root.getNode(i) != null) {
//				Node node2 = findNodeWithLessChildsReferences(root.getNode(i));
//				// if (node == null && ) {
//				//
//				// }
//				// else
//				if (node2.getReferencesOfChilds() <= node
//						.getReferencesOfChilds() && node2.children > 1) {
//					node = node2;
//				}
//			}
//		}
//		return node;
//	}

	public TreeSet<Node> getFathersOfLeafs(Node root, boolean debug,
			TreeSet<Node> fathers) {
		Node[] nodes = root.getNodes();
		boolean isLeaf = true;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				isLeaf = false;
				fathers = (getFathersOfLeafs(nodes[i], debug, fathers));
			}
		}
		if (isLeaf) {
			if (root.getFather() != null) {
				fathers.add(root.getFather());				
			}
		}
		return fathers;
	}

	public int getNumbersOfLeafs(Node root, boolean debug) {
		int result = 0;

		Node[] nodes = root.getNodes();
		boolean isLeaf = true;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				isLeaf = false;
				result += getNumbersOfLeafs(nodes[i], debug);
			}
		}
		if (isLeaf) {
			if (debug) {
				// System.out.println("Father:" + root.getFather());
				// System.out.println("YYYYYYYYYYYYYYYY:");
			}
			result = 1;
		}
		return result;
	}

	public LinkedList<Color> getColorsOfLeafs(Node root,
			LinkedList<Color> colors) {
		Node[] nodes = root.getNodes();
		boolean isLeaf = true;
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				isLeaf = false;
				colors = getColorsOfLeafs(nodes[i], colors);
			}
		}
		if (isLeaf) {
			colors.add(new Color((root.getRed() / root.getReferences()), (root
					.getGreen() / root.getReferences()), (root.getBlue() / root
					.getReferences())));
		}
		return colors;
	}

	public void cluster(Node less) {
		Node similiar1 = null;
		Node similiar2 = null;
		int sim1 = 0;
		int sim2 = 0;
		double distance = 800.0;
		// System.out.println(less.getNodes().length);
		for (int i = 0; i < less.getNodes().length; i++) {
			Node node1 = less.getNode(i);
			if (node1 != null) {
				for (int j = (i + 1); j < less.getNodes().length; j++) {
					Node node2 = less.getNode(j);
					if (node2 != null) {
						Double newDistance = getDistance(node1, node2);
						// System.out.println(newDistance);
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
		Node node1 = less.getNode(sim1);
		Node node2 = less.getNode(sim2);
		if (node1 != null && node2 != null) {

			node1.setReferences(node1.getReferences() + node2.getReferences());
			node1.setRed(node1.getRed() + node2.getRed());
			node1.setGreen(node1.getGreen() + node2.getGreen());
			node1.setBlue(node1.getBlue() + node2.getBlue());
			less.setNode(sim2, null);
			// System.out.println("sim1:" + similiar1 + "  \n sim2:" +
			// similiar2);
		}
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

	private Double getDistance(Color color1, Color color2) {
		return Math.sqrt((Math.pow((color2.getRed() - color1.getRed()), 2.0)
				+ Math.pow((color2.getGreen() - color1.getGreen()), 2.0) + Math
				.pow((color2.getBlue() - color2.getBlue()), 2.0)));
	}

	public BufferedImage mapPictureToColors(BufferedImage resizedImage,
			LinkedList<Color> colors) {
		for (int i = 0; i < resizedImage.getHeight(); i++) {
			for (int j = 0; j < resizedImage.getWidth(); j++) {
				resizedImage.setRGB(j, i,
						getBestColor(resizedImage.getRGB(j, i), colors));
			}
		}
		try {
			ImageIO.write(resizedImage, "jpg", new File("test3.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resizedImage;
	}

	private int getBestColor(int rgb, LinkedList<Color> colors) {
		Color color = colors.get(0);
		Color testColor = new Color(rgb);
		double distance = getDistance(color, testColor);
		for (int i = 1; i < colors.size(); i++) {
			Color color1 = colors.get(i);
			double distance1 = getDistance(color1, testColor);
			if (distance1 < distance) {
				color = color1;
				distance = distance1;
			}
		}
		return color.getRGB();
	}

	public int getChildrenOfNode(Node node) {
		int children = 0;
		for (Node child : node.getNodes()) {
			if (null != child) {
				children++;
			}
		}
		return children;
	}

	public TreeSet<Node> reduceColorsInFathers(TreeSet<Node> fathers) {
		Node node = fathers.first();
		fathers.remove(node);
		int red = 0;
		int green = 0;
		int blue = 0;
		if (getChildrenOfNode(node) > 0) {
			// System.out.println(node);
			for (int i = 0; i < node.getNodes().length; i++) {
				Node child = node.getNode(i);
				if (null != child) {
					red += child.getRed();
					green += child.getGreen();
					blue += child.getBlue();
					node.setNode(i, null);
				}
			}
			node.setBlue(blue);
			node.setRed(red);
			node.setGreen(green);
			Node father = node.getFather();
			boolean isGood = true;
			for (Node child : father.getNodes()) {
				if (null != child) {
					for (Node childsChild : child.getNodes()) {
						if (childsChild != null) {
							isGood = false;
						}
					}
				}
			}
			if (isGood) {
				fathers.add(father);
			}
		}
		return fathers;
	}
}
