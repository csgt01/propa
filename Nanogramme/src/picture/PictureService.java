package picture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
		for (int i = 0; i < 8; i++) {
			String indexString = ""
					+ (binary11.charAt(i) + "" + binary12.charAt(i) + "" + binary13
							.charAt(i));
			int index = Integer.parseInt(indexString, 2);
			// System.out.println(index);
			if (node.getNode(index) == null) {
				node.setNode(index);
			}
			node = node.getNode(index);
		}
		node.setRed(red);
		node.setGreen(green);
		node.setBlue(blue);
		node.setReferences(node.getReferences() + 1);

	}
	 /**
	  * Skalliert das Bild.
	 * @param originalImage 
	  */
	public BufferedImage scalePicture(int width, int height, Image originalImage, int type) {
		BufferedImage bi = new BufferedImage(width, height, type);
		Graphics2D g = bi.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();
		return bi;
	}

}
