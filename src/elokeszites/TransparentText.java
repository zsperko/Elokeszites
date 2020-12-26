/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JTextArea;

/**
 *
 * @author Forcek László
 */
public class TransparentText extends JTextArea{

    public TransparentText(String text, Color back, int rows, int columns)
    {
        super(text);
        setOpaque(false);
        setBackground(back);
        setRows(rows);
        setColumns(columns);
        setBorder(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0,0, getWidth(),getHeight());
        super.paintComponent(g);
    }
}
