/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

/**
 *
 * @author Forcek László
 */
import java.awt.*;
import javax.swing.*;

public class TransparentBackgroundImage extends JInternalFrame{

        public TransparentBackgroundImage()
        {

                JPanel panel = new JPanel(new GridLayout(2, 2)) {
                        ImageIcon image = new javax.swing.ImageIcon(getClass().getResource("icons/password.gif"));

                        public void paint(Graphics g) {
                                Dimension d = getSize();
                                for (int x = 0; x < d.width; x += image.getIconWidth())
                                        for (int y = 0; y < d.height; y += image.getIconHeight())
                                                g.drawImage(image.getImage(), x, y, null, null);
                                super.paint(g);
                        }
                };
                JLabel l1 = new JLabel("Name");
                JLabel l2 = new JLabel("Address");
                JTextField text = new JTextField(20);
                JTextField text1 = new JTextField(20);
                panel.setOpaque(false);
                text.setOpaque(false);
                text.setBackground(new Color(0,0,0,0));
                text1.setOpaque(false);
                panel.add(l1);
                panel.add(text);
                panel.add(l2);
                panel.add(text1);
                add(panel);
                setSize(300, 100);
                pack();
        }
}
