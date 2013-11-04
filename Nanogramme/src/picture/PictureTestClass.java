package picture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureTestClass {
   
   public static void main(String[] args) {
      
      int color1 = 230;
      int color2 = 30;
      int color3 = 20;
      
      int color11 = 30;
      int color12 = 130;
      int color13 = 224;
      PictureService ps = new PictureService();
      
      try {
		BufferedImage oi = ImageIO.read(new File("test.jpg"));
		int type = oi.getType() == 0? BufferedImage.TYPE_INT_ARGB : oi.getType();
		BufferedImage resizedImage = ps.scalePicture(150, 150, oi, type);
		ImageIO.write(resizedImage, "jpg", new File("test2.jpg"));
		
	} catch (IOException e) {
		e.printStackTrace();
	}
      
      Color color = new Color(color1, color2, color3);
      Color colorZwei = new Color(color11, color12, color13);
      Node root = new Node();
     
      ps.insertNode(color, root);
      ps.insertNode(colorZwei, root);
      ps.insertNode(colorZwei, root);
      System.out.println(root);
      
   }

}
