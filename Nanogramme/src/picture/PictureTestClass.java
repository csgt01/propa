package picture;


public class PictureTestClass {

//	public static void main(String[] args) {
//		PictureService ps = new PictureService();
//		BufferedImage resizedImage = null;
//		try {
//			BufferedImage oi = ImageIO.read(new File("test.jpg"));
//			resizedImage = oi;
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		Node root = new Node();
//
//		for (int i = 0; i < resizedImage.getHeight(); i++) {
//			for (int j = 0; j < resizedImage.getWidth(); j++) {
//				ps.insertNode(new Color(resizedImage.getRGB(j, i)), root);
//			}
//		}
//		TreeSet<Node> fathers = new TreeSet<Node>();
//		fathers = ps.getFathersOfLeafs(root, true, fathers);
//
//		while (fathers.size() > 7) {
//			fathers = ps.reduceColorsInFathers(fathers);
//		}
//		TreeSet<Node> fathersOfLeafs = new TreeSet<Node>();
//		fathersOfLeafs = ps.getFathersOfLeafs(root, true, fathersOfLeafs);
//		System.out.println(fathersOfLeafs.size());
//		while (fathersOfLeafs.size() > 6) {
//			fathersOfLeafs = ps.reduceColorsInFathers(fathersOfLeafs);
//		}
//		while (ps.getNumbersOfLeafs(root, false) > 4) {
//			if (ps.getChildrenOfNode(fathersOfLeafs.first()) > (ps.getNumbersOfLeafs(
//					root, false) - 4)) {
////				System.out.println(root);
//				ps.cluster(fathersOfLeafs.first());
//			} else {
//				fathersOfLeafs = ps.reduceColorsInFathers(fathersOfLeafs);
//			}
//		}
//		LinkedList<Color> colors = new LinkedList<Color>();
//		colors = ps.getColorsOfLeafs(root, colors);
//		resizedImage = ps.mapPictureToColors(resizedImage, colors);
//		try {
//			ImageIO.write(resizedImage, "jpg", new File("testBla.jpg"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}
