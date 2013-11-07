package picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class PictureTestClass {

   public static void main(String[] args) {

      int color1 = 1;
      int color2 = 1;
      int color3 = 1;

      int color11 = 255;
      int color12 = 255;
      int color13 = 255;

      // int color21 = 30;
      // int color22 = 130;
      // int color23 = 225;
      PictureService ps = new PictureService();
      BufferedImage resizedImage = null;
      try {
         BufferedImage oi = ImageIO.read(new File("test.jpg"));
         int type = oi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : oi.getType();
         resizedImage = ps.scalePicture(150, 150, oi, type);
         ImageIO.write(resizedImage, "jpg", new File("test2.jpg"));

      } catch (IOException e) {
         e.printStackTrace();
      }

      Color color = new Color(color1, color2, (color3));
      Color colorZwei = new Color(color11, color12, color13);
      Color colorDrei = new Color(color11, color12, (color13 - 1));
      Color colorVier = new Color(color11, color12, (color13 - 2));
      Node root = new Node();

      for (int i = 0; i < resizedImage.getHeight(); i++) {
         for (int j = 0; j < resizedImage.getWidth(); j++) {
            ps.insertNode(new Color(resizedImage.getRGB(j, i)), root);
         }
      }
      System.out.println(root);
      root.copyChildSums();
      while (ps.getNumbersOfLeafs(root) > 5) {
         System.out.println("%%%%%%%%%%");
         Node less = ps.findNodeWithLessChildsReferences(root);
         System.out.println(less);
         System.out.println("&/&$§§§$%%%§$%");
         if (less.children > (ps.getNumbersOfLeafs(root) - 4)) {
            System.out.println("break");
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
