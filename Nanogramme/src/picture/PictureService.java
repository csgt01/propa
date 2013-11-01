package picture;

import java.awt.Color;

public class PictureService {

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
      System.out.println(binary11);
      
      if (binary12.length() < 8) {
         for (int i = binary12.length(); i < 8; i++) {
            binary12 = "0" + binary12;
         }
      }
      
      System.out.println(binary13);
      
      if (binary13.length() < 8) {
         for (int i = binary13.length(); i < 8; i++) {
            binary13 = "0" + binary13;
         }
      }
      System.out.println(binary13);
      
      
      Node node = root;
      for (int i = 0; i < 8; i++) {
         String indexString = "" + (binary11.charAt(i) + "" + binary12.charAt(i) + "" + binary13.charAt(i));
         int index = Integer.parseInt(indexString, 2);
         System.out.println(index);
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

}
