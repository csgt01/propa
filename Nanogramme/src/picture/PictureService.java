package picture;

import interfaces.IPictureService;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeSet;

import javax.imageio.ImageIO;

/**
 * Diese Klasse ist fuer das Erstellen eines Raetsels aus einem Bild zustaendig.
 * Das Bild wird dabei aus einer Datei geladen, auf die gewuenschte Groesse
 * skalliert und dann mit Hilfe eines Octree-Algorythmus auf die gewuenschte
 * Anzahl an Farben heruntergerechnet.
 * 
 * @author cschulte
 * 
 */
public class PictureService implements IPictureService {

   /**
    * ID fuer die Nodes.
    */
   int count = 0;

   /**
    * Rechnet die Farben mit Hilfe eines Octrees auf die gewuenschte Anzahl
    * Farben herunter. Ruft
    * {@link #mapPictureToColors(BufferedImage, LinkedList)} auf, um die Farben
    * des Originalbildes anzupassen. Gibt das Bild mit herruntergerechneten
    * Farben zurueck.
    * 
    * @param file
    *           Pfad zum Bild
    * @param height
    *           Hoehe des neuen Bildes
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
         for (int i = 0; i < resizedImage.getHeight(); i++) {
            for (int j = 0; j < resizedImage.getWidth(); j++) {
               insertNode(new Color(resizedImage.getRGB(j, i)), root);
            }
         }
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
         resizedImage = mapPictureToColors(resizedImage, colors);
      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
      return resizedImage;
   }

   /**
    * Fuegt einen Pixel (Farbe) dem root Element hinzu. Dies muss fuer alle Pixel
    * des Bildes gemacht werden.
    * 
    * @param color
    *           Die Farbe des Pixels.
    * @param root
    *           Das root Element, in das alle Pixel hinzugefuegt werden.
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

      if (binary12.length() < 8) {
         for (int i = binary12.length(); i < 8; i++) {
            binary12 = "0" + binary12;
         }
      }

      if (binary13.length() < 8) {
         for (int i = binary13.length(); i < 8; i++) {
            binary13 = "0" + binary13;
         }
      }
      Node node = root;
      for (int i = 0; i < 8; i++) {
         String indexString = "" + (binary11.charAt(i) + "" + binary12.charAt(i) + "" + binary13.charAt(i));
         int index = Integer.parseInt(indexString, 2);
         if (node.getNode(index) == null) {
            count++;
            node.setNode(index, new Node());
            node.getNode(index).setCount(count);
         }
         node.getNode(index).setFather(node);
         node = node.getNode(index);
         node.setReferences(node.getReferences() + 1);
      }
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
    *           Hoehe des skallierten Bildes.
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
    * Fuegt fathers alle Vaeter der Blaetter hinzu.
    * 
    * @param root
    *           Startelement
    * @param fathers
    *           Liste der Vaeter der Blaetter.
    * @return Liste der Vaeter
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
    * Gibt die Anzahl der Blaetter im Octree zurueck.
    * 
    * @param root
    * @return Anzahl an Blaettern im Octree
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
    *           Liste der bereits hinzugefuegten Farben.
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
    * Merged die beiden Blaetter, dessen Farben am aehnlichsten sind, miteinander.
    * 
    * @param less
    *           Der Knoten, dessen aehnlichste Blaetter gemerged werden sollen.
    */
   private void cluster(Node less) {
      Node similiar1 = null;
      Node similiar2 = null;
      int sim1 = 0;
      int sim2 = 0;
      double distance = 8000.0;
      // suche die aehnlichsten Blaetter
      for (int i = 0; i < less.getNodes().length; i++) {
         Node node1 = less.getNode(i);
         if (node1 != null) {
            for (int j = (i + 1); j < less.getNodes().length; j++) {
               Node node2 = less.getNode(j);
               if (node2 != null) {
                  Double newDistance = getDistance(node1, node2);
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
         // do nothing
      }
      // merge die aehnlichsten
      mergeTwoNodes(less, sim1, sim2);
   }

   /**
    * Merged die beiden Nodes mit Index sim1 und sim2 aus less.nodes
    * miteinander.
    * 
    * @param less
    *           Node mit den beiden zu verschmelzenden Nodes (in nodes)
    * @param sim1
    *           Index des ersten zu mergenden Nodes.
    * @param sim2
    *           Index des zweiten zu mergenden Nodes.
    */
   private void mergeTwoNodes(Node less, int sim1, int sim2) {
      Node node1 = less.getNode(sim1);
      Node node2 = less.getNode(sim2);
      if (node1 != null && node2 != null) {
         node1.setReferences(node1.getReferences() + node2.getReferences());
         node1.setRed(node1.getRed() + node2.getRed());
         node1.setGreen(node1.getGreen() + node2.getGreen());
         node1.setBlue(node1.getBlue() + node2.getBlue());
         less.setNode(sim2, null);
      }
   }

   /**
    * Liefert den Abstand der Farben der Blaetter zueinander (entspricht der
    * aehnlichkeit der Farben).
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
    * Berechnet die aehnlichkeit con zwei Farben.
    * 
    * @param color1
    * @param color2
    * @return aehnlichkeit
    */
   private Double getDistance(Color color1, Color color2) {
      return Math.sqrt((Math.pow((color2.getRed() - color1.getRed()), 2.0) + Math.pow((color2.getGreen() - color1.getGreen()), 2.0) + Math.pow((color2.getBlue() - color2.getBlue()), 2.0)));
   }

   /**
    * Sucht zu jedem Pixel die aehnlichste Farbe aus colors und setzt diese als
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
      return resizedImage;
   }

   /**
    * Sucht aus colors die aehnlichste Farbe.
    * 
    * @param rgb
    *           die zu ersetzenden Farbe als rgb-wert
    * @param colors
    *           Liste der Vergleichsfarben
    * @return den rgb-Wert der aehnlichsten Farbe.
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
    * Gibt die Anzahl der Kinder in {@link Node#getNodes()} zurueck.
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
    * der Vater dieses Knotens danach nur Blaetter als Kinder hat, wird dieser
    * fathers hinzugefuegt.
    * 
    * @param fathers
    *           Liste der Vaeter von nur Blaettern im Octree.
    * @return Aktuallisierte Liste der Vaeter von nur Blaettern im Octree.
    */
   private TreeSet<Node> reduceColorsInFathers(TreeSet<Node> fathers) {
      Node node = fathers.first();
      fathers.remove(node);
      if (getChildrenOfNode(node) > 0) {
         setRGBFromChildren(node);
         Node father = node.getFather();
         // Falls ein Kind des Vaters kein Blatt ist, wird isGood false, dann
         // ist der Knoten kein Vater von nur Blaettern.
         if (isNodeFatherJustFromLeafs(father)) {
            fathers.add(father);
         }
      }
      return fathers;
   }

   /**
    * Addiert red, green und blue der Kinder zu den eigenen Werten und loescht
    * dann das Kind.
    * 
    * @param node
    *           Node aus dessen kindern die RGB Werte kopiert werden
    */
   private void setRGBFromChildren(Node node) {
      int red = 0;
      int green = 0;
      int blue = 0;
      int length = node.getNodes().length;
      for (int i = 0; i < length; i++) {
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
   }

   /**
    * Falls ein Node in father.nodes kein Blatt ist, sondern ein Knoten, ist
    * father kein Vater von nur Blaettern.
    * 
    * @param father
    *           zu ueberpruefender Node
    * @return true, falls father nur Blaetter als Kinder hat.
    */
   private boolean isNodeFatherJustFromLeafs(Node father) {
      boolean isGood = true;
      for (Node child : father.getNodes()) {
         if (null != child) {
            for (Node childsChild : child.getNodes()) {
               if (childsChild != null) {
                  isGood = false;
               }
            }
         }
      }
      return isGood;
   }
}
