package picture;

public class PictureTestClass {
   
   public static void main(String[] args) {
      
      int color1 = 230;
      int color2 = 30;
      int color3 = 20;
      
      String binary1 = Integer.toBinaryString(color1);
      String binary2 = Integer.toBinaryString(color2);
      String binary3 = Integer.toBinaryString(color3);
      
      int color11 = 30;
      int color12 = 130;
      int color13 = 224;
      
      String binary11 = Integer.toBinaryString(color11);
      String binary12 = Integer.toBinaryString(color12);
      String binary13 = Integer.toBinaryString(color13);
      
      if (binary1.length() < 8) {
         for (int i = binary1.length(); i < 8; i++) {
            binary1 = "0" + binary1;
         }
      }
      System.out.println(binary1);
      
      if (binary2.length() < 8) {
         for (int i = binary2.length(); i < 8; i++) {
            binary2 = "0" + binary2;
         }
      }
      
      System.out.println(binary2);
      
      if (binary3.length() < 8) {
         for (int i = binary3.length(); i < 8; i++) {
            binary3 = "0" + binary3;
         }
      }
      System.out.println(binary3);
      
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
      
      System.out.println(binary12);
      
      if (binary13.length() < 8) {
         for (int i = binary13.length(); i < 8; i++) {
            binary13 = "0" + binary13;
         }
      }
      System.out.println(binary13);
      
      Node root = new Node();
      Node node = root;
      for (int i = 0; i < 8; i++) {
         String indexString = "" + (binary1.charAt(i) + "" + binary2.charAt(i)+ "" + binary3.charAt(i));
         int index = Integer.parseInt(indexString, 2);
         System.out.println(index);
         if (node.getNode(index) == null) {
            node.setNode(index);
         }
         node = node.getNode(index);
      }
      node.setRed(color1);
      node.setGreen(color2);
      node.setBlue(color3);
      node.setReferences(1);
      
      System.out.println(root);
      
      node = root;
      for (int i = 0; i < 8; i++) {
         String indexString = "" + (binary11.charAt(i) + "" + binary12.charAt(i)+ "" + binary13.charAt(i));
         int index = Integer.parseInt(indexString, 2);
         System.out.println(index);
         if (node.getNode(index) == null) {
            node.setNode(index);
         }
         node = node.getNode(index);
      }
      node.setRed(color11);
      node.setGreen(color12);
      node.setBlue(color13);
      node.setReferences(node.getReferences() +1);
      
      System.out.println(root);
      
      node = root;
      for (int i = 0; i < 8; i++) {
         String indexString = "" + (binary11.charAt(i) + "" + binary12.charAt(i)+ "" + binary13.charAt(i));
         int index = Integer.parseInt(indexString, 2);
         System.out.println(index);
         if (node.getNode(index) == null) {
            node.setNode(index);
         }
         node = node.getNode(index);
      }
      node.setRed(color11);
      node.setGreen(color12);
      node.setBlue(color13);
      node.setReferences(node.getReferences() +1);
      
      System.out.println(root);
      
   }

}
