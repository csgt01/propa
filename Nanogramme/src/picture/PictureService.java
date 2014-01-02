package picture;

import interfaces.IPictureService;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * Diese Klasse ist für das Erstellen eines Rätsels aus einem Bild zuständig.
 * 
 * @author cschulte
 * 
 */
public class PictureService implements IPictureService {

   /**
    * ID für die Nodes.
    */
   int count = 0;

   /**
    * Rechnet die Farben mit Hilfe eines Octrees auf die gewünschte Anzahl
    * Farben herunter. Ruft
    * {@link #mapPictureToColors(BufferedImage, LinkedList)} auf, um die Farben
    * des Originalbildes anzupassen. Gibt das Bild mit herruntergerechneten
    * Farben zurück.
    * 
    * @param file
    *           Pfad zum Bild
    * @param height
    *           Höhe des neuen Bildes
    * @param width
    *           Breite des neuen Bildes
    * @param numberOfColors
    *           Anzahl der Farben
    * @return herruntergerechnetes Bild
    */
   @Override
   public BufferedImage loadAndDownColorPicture(String file, int height, int width, int numberOfColors) {
      BufferedImage resizedImage = null;
      try {
         BufferedImage oi = ImageIO.read(new File(file));
         int type = oi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : oi.getType();
         resizedImage = scalePicture(width, height, oi, type);
         Node root = new Node();
         Date date = new Date();
         for (int i = 0; i < resizedImage.getHeight(); i++) {
            for (int j = 0; j < resizedImage.getWidth(); j++) {
               insertNode(new Color(resizedImage.getRGB(j, i)), root);
            }
         }
         System.out.println("time:" + (new Date().getTime() - date.getTime()));
         date = new Date();
         TreeSet<Node> fathers = new TreeSet<Node>();
         fathers = getFathersOfLeafs(root, fathers);

         while (getNumbersOfLeafs(root) > numberOfColors) {
            if (getChildrenOfNode(fathers.first()) > (getNumbersOfLeafs(root) - numberOfColors)) {
               cluster(fathers.first());
            } else {
               fathers = reduceColorsInFathers(fathers);
            }
         }
         LinkedList<Color> colors = new LinkedList<Color>();
         colors = getColorsOfLeafs(root, colors);
         System.out.println("time:" + (new Date().getTime() - date.getTime()));
         date = new Date();
         resizedImage = mapPictureToColors(resizedImage, colors);
         System.out.println("time:" + (new Date().getTime() - date.getTime()));
         date = new Date();

      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
      return resizedImage;
   }

   /**
    * Fügt einen Pixel (Farbe) dem root Element hinzu. Dies muss für alle Pixel
    * des Bildes gemacht werden.
    * 
    * @param color
    *           Die Farbe des Pixels.
    * @param root
    *           Das root Element, in das alle Pixel hinzugefügt werden.
    */
   private void insertNode(Color color, Node root) {
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      root.setReferences(root.getReferences() + 1);
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
      // Node lastNode = root;
      for (int i = 0; i < 8; i++) {
         String indexString = "" + (binary11.charAt(i) + "" + binary12.charAt(i) + "" + binary13.charAt(i));
         int index = Integer.parseInt(indexString, 2);
         // System.out.println(index);
         if (node.getNode(index) == null) {
            count++;
            node.setNode(index, new Node());
            node.getNode(index).setCount(count);
         }
         // lastNode = node;
         node.getNode(index).setFather(node);
         node = node.getNode(index);
         node.setReferences(node.getReferences() + 1);
      }
      // lastNode.setReferencesOfChilds();

      node.setRed(node.getRed() + red);
      node.setGreen(node.getGreen() + green);
      node.setBlue(node.getBlue() + blue);
   }

   /**
    * Skalliert das Bild.
    * 
    * @param width
    *           Breite des skallierten Bildes.
    * @param height
    *           Höhe des skallierten Bildes.
    * @param originalImage
    *           zu skallierende Bild
    * @param type
    *           Typ
    * @return Das skallierte Bild
    */
   private BufferedImage scalePicture(int width, int height, Image originalImage, int type) {
      BufferedImage bi = new BufferedImage(width, height, type);
      Graphics2D g = bi.createGraphics();
      g.drawImage(originalImage, 0, 0, width, height, null);
      g.dispose();
      return bi;
   }

   /**
    * Fügt fathers alle Väter der Blätter hinzu.
    * 
    * @param root
    *           Startelement
    * @param fathers
    *           Liste der Väter der Blätter.
    * @return Liste der Väter
    */
   private TreeSet<Node> getFathersOfLeafs(Node root, TreeSet<Node> fathers) {
      Node[] nodes = root.getNodes();
      boolean isLeaf = true;
      for (int i = 0; i < nodes.length; i++) {
         if (nodes[i] != null) {
            isLeaf = false;
            fathers = (getFathersOfLeafs(nodes[i], fathers));
         }
      }
      if (isLeaf) {
         if (root.getFather() != null) {
            fathers.add(root.getFather());
         }
      }
      return fathers;
   }

   /**
    * Gibt die Anzahl der Blätter im Octree zurück.
    * 
    * @param root
    * @return Anzahl an Blättern im Octree
    */
   private int getNumbersOfLeafs(Node root) {
      int result = 0;

      Node[] nodes = root.getNodes();
      boolean isLeaf = true;
      for (int i = 0; i < nodes.length; i++) {
         if (nodes[i] != null) {
            isLeaf = false;
            result += getNumbersOfLeafs(nodes[i]);
         }
      }
      if (isLeaf) {
         result = 1;
      }
      return result;
   }

   /**
    * Liefert eine Liste der im Octree vorhandenen Farben. Ruft sich rekursiv
    * auf.
    * 
    * @param root
    *           Basiselement.
    * @param colors
    *           Liste der bereits hinzugefügten Farben.
    * @return Liste der Farben im Octree.
    */
   private LinkedList<Color> getColorsOfLeafs(Node root, LinkedList<Color> colors) {
      Node[] nodes = root.getNodes();
      boolean isLeaf = true;
      for (int i = 0; i < nodes.length; i++) {
         if (nodes[i] != null) {
            isLeaf = false;
            colors = getColorsOfLeafs(nodes[i], colors);
         }
      }
      if (isLeaf) {
         colors.add(new Color((root.getRed() / root.getReferences()), (root.getGreen() / root.getReferences()), (root.getBlue() / root.getReferences())));
      }
      return colors;
   }

   /**
    * Merged die beiden Blätter, dessen Farben am ähnlichsten sind miteinander.
    * 
    * @param less
    *           Der Knoten, dessen ähnlichste Blätter gemerged werden sollen.
    */
   private void cluster(Node less) {
      Node similiar1 = null;
      Node similiar2 = null;
      int sim1 = 0;
      int sim2 = 0;
      double distance = 800.0;
      // System.out.println(less.getNodes().length);
      // suche die ähnlichsten Blätter
      for (int i = 0; i < less.getNodes().length; i++) {
         Node node1 = less.getNode(i);
         if (node1 != null) {
            for (int j = (i + 1); j < less.getNodes().length; j++) {
               Node node2 = less.getNode(j);
               if (node2 != null) {
                  Double newDistance = getDistance(node1, node2);
                  // System.out.println(newDistance);
                  if (newDistance != null && newDistance < distance) {
                     distance = newDistance;
                     similiar1 = node1;
                     sim1 = i;
                     similiar2 = node2;
                     sim2 = j;
                  }
               }
            }
         }
      }
      if (similiar1 != null && similiar2 != null) {

      }
      // merge die Ähnlichsten
      Node node1 = less.getNode(sim1);
      Node node2 = less.getNode(sim2);
      if (node1 != null && node2 != null) {

         node1.setReferences(node1.getReferences() + node2.getReferences());
         node1.setRed(node1.getRed() + node2.getRed());
         node1.setGreen(node1.getGreen() + node2.getGreen());
         node1.setBlue(node1.getBlue() + node2.getBlue());
         less.setNode(sim2, null);
         // System.out.println("sim1:" + similiar1 + "  \n sim2:" +
         // similiar2);
      }
   }

   /**
    * Liefert den Abstand der Farben der Blätter zueinander (entspricht der
    * Ähnlichkeit der Farben).
    * 
    * @param node1
    * @param node2
    * @return Abstand der Nodes
    */
   private Double getDistance(Node node1, Node node2) {
      if (node1.getReferences() != 0 && node2.getReferences() != 0) {
         float r1 = node1.getRed() / node1.getReferences();
         float g1 = node1.getGreen() / node1.getReferences();
         float b1 = node1.getBlue() / node1.getReferences();
         float r2 = node2.getRed() / node2.getReferences();
         float g2 = node2.getGreen() / node2.getReferences();
         float b2 = node2.getBlue() / node2.getReferences();
         return Math.sqrt((Math.pow((r2 - r1), 2.0) + Math.pow((g2 - g1), 2.0) + Math.pow((b2 - b1), 2.0)));
      } else {
         return null;
      }
   }

   /**
    * Berechnet die Ähnlichkeit con zwei Farben.
    * 
    * @param color1
    * @param color2
    * @return Ähnlichkeit
    */
   private Double getDistance(Color color1, Color color2) {
      return Math.sqrt((Math.pow((color2.getRed() - color1.getRed()), 2.0) + Math.pow((color2.getGreen() - color1.getGreen()), 2.0) + Math.pow((color2.getBlue() - color2.getBlue()), 2.0)));
   }

   /**
    * Sucht zu jedem Pixel die Ähnlichste Farbe aus colors und setzt diese als
    * neue Farbe des Pixels. Dadurch wird das Bild auf die Anzahl der Farben in
    * colors herruntergerechnet.
    * 
    * @param resizedImage
    * @param colors
    * @return Angepasstes Image
    */
   private BufferedImage mapPictureToColors(BufferedImage resizedImage, LinkedList<Color> colors) {
      for (int i = 0; i < resizedImage.getHeight(); i++) {
         for (int j = 0; j < resizedImage.getWidth(); j++) {
            resizedImage.setRGB(j, i, getBestColor(resizedImage.getRGB(j, i), colors));
         }
      }
      // TODO: weg!!
      try {
         ImageIO.write(resizedImage, "jpg", new File("test3.jpg"));
      } catch (IOException e) {
         e.printStackTrace();
      }
      return resizedImage;
   }

   /**
    * Sucht aus colors die ähnlichste Farbe aus colors.
    * 
    * @param rgb
    *           der zu ersetzenden Farbe
    * @param colors
    *           Liste der Vergleichsfarben
    * @return den rgb-Wert der ähnlichsten Farbe.
    */
   private int getBestColor(int rgb, LinkedList<Color> colors) {
      Color color = colors.get(0);
      Color testColor = new Color(rgb);
      double distance = getDistance(color, testColor);
      for (int i = 1; i < colors.size(); i++) {
         Color color1 = colors.get(i);
         double distance1 = getDistance(color1, testColor);
         if (distance1 < distance) {
            color = color1;
            distance = distance1;
         }
      }
      return color.getRGB();
   }

   /**
    * Gibt die Anzahl der Kinder in {@link Node#getNodes()} zurück.
    * 
    * @param node
    * @return Anzahl der Kinder.
    */
   private int getChildrenOfNode(Node node) {
      int children = 0;
      for (Node child : node.getNodes()) {
         if (null != child) {
            children++;
         }
      }
      return children;
   }

   /**
    * Alle Kinder dieses Knotens werden in diesem Knoten zusammengefasst. Falls
    * der Vater dieses Knotens danach nur Blätter als Kinder hat, wird dieser
    * fathers hinzugefügt.
    * 
    * @param fathers
    *           Liste der Väter von nur Blättern im Octree.
    * @return Aktuallisierte Liste der Väter von nur Blättern im Octree.
    */
   private TreeSet<Node> reduceColorsInFathers(TreeSet<Node> fathers) {
      Node node = fathers.first();
      fathers.remove(node);
      int red = 0;
      int green = 0;
      int blue = 0;
      if (getChildrenOfNode(node) > 0) {
         // System.out.println(node);
         for (int i = 0; i < node.getNodes().length; i++) {
            Node child = node.getNode(i);
            if (null != child) {
               red += child.getRed();
               green += child.getGreen();
               blue += child.getBlue();
               node.setNode(i, null);
            }
         }
         node.setBlue(blue);
         node.setRed(red);
         node.setGreen(green);
         Node father = node.getFather();
         boolean isGood = true;
         // Falls ein Kind des Vaters kein Blatt iat, wird isGood false, dann
         // ist der
         // Knoten kein Vater von nur Blättern.
         for (Node child : father.getNodes()) {
            if (null != child) {
               for (Node childsChild : child.getNodes()) {
                  if (childsChild != null) {
                     isGood = false;
                  }
               }
            }
         }
         if (isGood) {
            fathers.add(father);
         }
      }
      return fathers;
   }
}
