/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;

/**
 *
 * @author Forcek László
 */
public class RectangleButton extends JButton {

    public RectangleButton(String label) {
        super(label);
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed())
        {
            g.setColor(Color.lightGray);
        }
        else
        {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getSize().width-1, getSize().height-1);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        //Border
        g.drawOval(0, 0, getSize().width-1, getSize().height-1);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds()))
        {
            shape = new Rectangle2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    private Shape shape;
}
