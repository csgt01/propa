package interfaces;

import java.awt.image.BufferedImage;

/**
 * Interface für die Kommunikation zwischen MainFrame und PictureService.
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
    * @return
    */
   BufferedImage loadAndDownColorPicture(String file, int height, int width, int numberOfColors);

}
