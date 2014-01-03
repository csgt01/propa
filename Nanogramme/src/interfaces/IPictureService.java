package interfaces;

import java.awt.image.BufferedImage;

/**
 * Interface stellt eine Methode zum laden und herunterskallieren eines Bildes bereit.
 * 
 * @author csgt
 * 
 */
public interface IPictureService {

   /**
    * Lädt ein Foto und verringert die Anzahl der Farben.
    * 
    * @param file
    *           das Foto
    * @param height
    *           Höhe des neuen Fotos
    * @param width
    *           Breite des neuen Fotos
    * @param numberOfColors
    *           Anzahl der Farben des neuen Fotos
    * @return Verkleinertes Image
    */
   BufferedImage loadAndDownColorPicture(String file, int height, int width, int numberOfColors);

}
