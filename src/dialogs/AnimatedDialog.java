/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dialogs;

import classes.SpringUtilities;
import forms.Configuration;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Forcek László
 */
public final class AnimatedDialog extends JDialog{

    public AnimatedDialog(ArrayList<String> MessageList)
    {
        AnimatedDialog.MessageList = MessageList;
        initComponents();
    }

    public void initComponents()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dim.getSize().getWidth() / 2) - 100,
                    (int)(dim.getSize().getHeight() / 2) - 40);
        setPreferredSize(new Dimension(200,80));

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        ((JPanel)contentPane).setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black));

        pgsPane = new JPanel(new SpringLayout());
        pgsPane.setBorder(new EtchedBorder());
        
        msgLabel = new JTextField(10);
        
        msgLabel.setEditable(false);
        Font font = msgLabel.getFont();
        font = font.deriveFont((float)(font.getSize2D() * 12.0 / 11.0));
        font = font.deriveFont(Font.BOLD);
        msgLabel.setFont(font);
        msgLabel.setFocusable(false);
        msgLabel.setText(MessageList.get(0));
        msgLabel.setHorizontalAlignment(JLabel.LEFT);
        
        pgsPane.add(msgLabel);
        
        SpringUtilities.makeCompactGrid(pgsPane,
                                        1, 1, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        imgPane =  new JPanel(new BorderLayout());
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/animatedprogressbar.gif"));
        imageLabel = new JLabel(icon,JLabel.CENTER);
        imgPane.add("Center",imageLabel);
        
        contentPane.add(pgsPane);
        contentPane.add(imgPane);
        
        //pgsPane
        layout.putConstraint(SpringLayout.WEST, pgsPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, pgsPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, pgsPane,
                             1,
                             SpringLayout.EAST, contentPane);
        
        //imgPane
        layout.putConstraint(SpringLayout.WEST, imgPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, imgPane,
                             1,
                             SpringLayout.SOUTH, pgsPane);
        layout.putConstraint(SpringLayout.EAST, imgPane,
                             1,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, imgPane,
                             1,
                             SpringLayout.SOUTH, contentPane);

        pack();
    }

    public void formWindowOpened(WindowEvent e) {
        timer = new javax.swing.Timer(10, (ActionEvent ev) -> {
            if (Configuration.isThreadFinished())
            {
                timer.stop();
                dispose();
            }
        });
        timer.start();
    }

    private JPanel pgsPane, imgPane;
    private JLabel imageLabel;
    private static JTextField msgLabel;
    private Timer timer;
    private static ArrayList<String> MessageList = new ArrayList<>();
}
