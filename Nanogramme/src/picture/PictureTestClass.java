package picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
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
//		System.out.println(root);
		System.out.println(ps.getNumbersOfLeafs(root, false));
		TreeSet<Node> fathers = new TreeSet<Node>();
		fathers = ps.getFathersOfLeafs(root, true, fathers);
		System.out.println("GGGGGGGGGGGG");
		System.out.println(fathers.size());
		
		while (fathers.size() > 7) {
			fathers = ps.reduceColorsInFathers(fathers);
		}
		System.out.println("RRRRRRRRRRRRRR");
		System.out.println(fathers.size());
		System.out.println("SSSSSSSSSSSSSS");
		TreeSet<Node> fathersOfLeafs = new TreeSet<Node>();
		fathersOfLeafs = ps.getFathersOfLeafs(root, true, fathersOfLeafs);
		System.out.println(fathersOfLeafs.size());
		while (fathersOfLeafs.size() > 4) {
			fathersOfLeafs = ps.reduceColorsInFathers(fathersOfLeafs);
		}
		System.out.println("RRRRRRRRRRRRRR");
		System.out.println(fathersOfLeafs.size());
		System.out.println("SSSSSSSSSSSSSS");
		while (ps.getNumbersOfLeafs(root, false) > 4) {
			fathersOfLeafs = ps.reduceColorsInFathers(fathersOfLeafs);
		}
		System.out.println("RRRRRRRRRRRRRR");
		System.out.println(fathersOfLeafs.size());
		System.out.println(ps.getNumbersOfLeafs(root, false));
		System.out.println("SSSSSSSSSSSSSS");
	}

}
