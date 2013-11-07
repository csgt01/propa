package picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class PictureTestClass {

   public static void main(String[] args) {

      PictureService ps = new PictureService();
      BufferedImage resizedImage = null;
      try {
         BufferedImage oi = ImageIO.read(new File("test.jpg"));
         int type = oi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : oi.getType();
         resizedImage = ps.scalePicture(60, 60, oi, type);
         ImageIO.write(resizedImage, "jpg", new File("test2.jpg"));
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
//      System.out.println(root);
      root.copyChildSums();
      while (ps.getNumbersOfLeafs(root) > 5) {
//         System.out.println("%%%%%%%%%%");
         Node less = ps.findNodeWithLessChildsReferences(root);
//         System.out.println(less);
//         System.out.println("&/&$§§§$%%%§$%");
         if (less.children > (ps.getNumbersOfLeafs(root) - 4)) {
//            System.out.println("break");
            ps.cluster(less);
         } else {
            ps.reduceColors(less);
         }
      }
      System.out.println(ps.getNumbersOfLeafs(root));
      System.out.println(root);
      LinkedList<Color> colors = new LinkedList<Color>();
      colors = ps.getColorsOfLeafs(root, colors);
      System.out.println(colors);
      
      ps.mapPictureToColors(resizedImage, colors);
      
   }

}
