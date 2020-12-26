/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.JPanel;

/**
 *
 * @author Forcek László
 */
public class ImagePanel extends JPanel {

    private Image img;
    private BufferedImage bi;
    private RescaleOp rop;
    private float[] scales = { 1f, 1f, 1f, 0.5f };
    private float[] offsets = new float[4];


    public ImagePanel(Image img, Float opacity) {
        this.img = img;
        setOpacity(opacity);
        convertToBI();
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setOpaque(false);
        setLayout(null);
    }

    private void setOpacity(float opacity) {
        scales[3] = opacity;
        rop = new RescaleOp(scales, offsets, null);
    }

    private void convertToBI()
    {
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 0);
        try
        {
            mt.waitForID(0);
        }
        catch (InterruptedException ie) {}
        bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(bi, rop, 0, 0);
    }

}
