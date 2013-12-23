package interfaces;

import java.awt.image.BufferedImage;


public interface IPictureService {

   BufferedImage loadAndDowColorPicture(String file, int height,
         int width, int numberOfColors);

}
