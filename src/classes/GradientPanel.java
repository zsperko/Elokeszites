/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *
 * @author Forcek László
 */
public class GradientPanel extends JPanel{

    @Override
    public void paintComponent(Graphics _g){
        Graphics2D g = (Graphics2D)_g;
        Rectangle bounds = getBounds();
        Paint gradientPaint = new GradientPaint(0, 0, Color.blue, bounds.width, bounds.height, Color.white);
        g.setPaint(gradientPaint);
        g.fillRect(0, 0, bounds.width, bounds.height);
    }
}

