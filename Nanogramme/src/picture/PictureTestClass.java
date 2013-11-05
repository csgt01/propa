package picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureTestClass {

   public static void main(String[] args) {

      int color1 = 10;
      int color2 = 130;
      int color3 = 220;

      int color11 = 130;
      int color12 = 30;
      int color13 = 224;

      int color21 = 30;
      int color22 = 130;
      int color23 = 225;
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
      Color colorDrei = new Color(color21, color22, color23);
      Node root = new Node();

//      ps.insertNode(colorDrei, root);
//      ps.insertNode(color, root);
//      ps.insertNode(color, root);
//      ps.insertNode(colorZwei, root);
//      ps.insertNode(colorZwei, root);
      
      for (int i = 0; i < resizedImage.getHeight();i++) {
         for(int j = 0 ; j < resizedImage.getWidth(); j++) {
            ps.insertNode(new Color(resizedImage.getRGB(j, i)), root);
         }
      }
      System.out.println(root);

      while (ps.getNumbersOfLeafs(root) > 4) {
         System.out.println(ps.getNumbersOfLeafs(root));
         Node findNodeWithLessChildsReferences = ps.findNodeWithLessChildsReferences(root);
         System.out.println("less:" + findNodeWithLessChildsReferences);
         ps.reduceColors(findNodeWithLessChildsReferences);
         ps.setReferenceChilds(root);
      }
      System.out.println(ps.getNumbersOfLeafs(root));
      System.out.println(root);
   }

}
