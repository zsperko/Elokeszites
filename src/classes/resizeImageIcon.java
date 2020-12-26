/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.awt.Image;
import javax.swing.ImageIcon;
/**
 *
 * @author Forcek László
 */

public class resizeImageIcon {

    private ImageIcon image;

    public resizeImageIcon(ImageIcon imageIcon, int width, int height)
    {
        Image tmpImage = imageIcon.getImage();
        Image newImage  = tmpImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        image = new ImageIcon(newImage);
    }

    public ImageIcon getImage()
    {
        return image;
    }
}