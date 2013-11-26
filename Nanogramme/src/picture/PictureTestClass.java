package picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;

public class PictureTestClass {

	public static void main(String[] args) {
		Date date = new Date();
		PictureService ps = new PictureService();
		BufferedImage resizedImage = null;
		try {
			BufferedImage oi = ImageIO.read(new File("test22.jpg"));
			int type = oi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : oi
					.getType();
			// resizedImage = ps.scalePicture(60, 60, oi, type);
			// ImageIO.write(resizedImage, "jpg", new File("test2.jpg"));
			resizedImage = oi;

		} catch (IOException e) {
			e.printStackTrace();
		}

		Node root = new Node();

		for (int i = 0; i < resizedImage.getHeight(); i++) {
			for (int j = 0; j < resizedImage.getWidth(); j++) {
				ps.insertNode(new Color(resizedImage.getRGB(j, i)), root);
			}
		}
		 System.out.println(root);
		System.out.println(ps.getNumbersOfLeafs(root, false));
		root.copyChildSums();
		while (ps.getNumbersOfLeafs(root, false) > 10) {
//			System.out.println(ps.getNumbersOfLeafs(root));
			// System.out.println("%%%%%%%%%%");
			Node less = ps.findNodeWithLessChildsReferences(root);
			// System.out.println(less);
			// System.out.println("&/&$§§§$%%%§$%");
			if (less.children > (ps.getNumbersOfLeafs(root, false) - 10)) {
				// System.out.println("break");
				ps.cluster(less);
			} else {
				ps.reduceColors(less);
			}
		}
		System.out.println(ps.getNumbersOfLeafs(root, false));
		System.out.println(root);
		LinkedList<Color> colors = new LinkedList<Color>();
		colors = ps.getColorsOfLeafs(root, colors);
		System.out.println(colors);
		BufferedImage downSacaled = ps.mapPictureToColors(resizedImage, colors);
		System.out.println("" + (new Date().getTime() - date.getTime()));
		System.out.println(root);
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
//		ps.
//		getNumbersOfLeafs(root, true);
		System.out.println("ZZZZZZZZZZZZ");
		TreeSet<Node> fathers = new TreeSet<Node>();
		fathers = ps.getFathersOfLeafs(root, true, fathers);
		System.out.println("GGGGGGGGGGGG");
		System.out.println(fathers.size());
		System.out.println(fathers);
		try {
			ImageIO.write(downSacaled, "jpg", new File("testDown.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
