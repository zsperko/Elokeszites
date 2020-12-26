/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

/**
 *
 * @author Forcek László
 */
public class AniMsgBox extends JDialog implements ActionListener{

    public AniMsgBox(String message1, String message2)
    {
        startMessage = message1;
        doneMessage = message2;
        initComponents();
    }

    public void initComponents()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dim.getSize().getWidth() / 2) - 180,
                    (int)(dim.getSize().getHeight() / 2) - 90);
        setPreferredSize(new Dimension(220,100));

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

        imgPane =  new JPanel(new BorderLayout());
        btnPane = new JPanel(new FlowLayout());
        lblPane = new JPanel(new BorderLayout());

        msgLabel = new JLabel();
        Font font = msgLabel.getFont();
        font = font.deriveFont((float)(font.getSize2D() * 12.0 / 11.0));
        font = font.deriveFont(Font.BOLD);
        msgLabel.setFont(font);
        msgLabel.setFocusable(false);
        msgLabel.setText(startMessage);
        msgLabel.setHorizontalAlignment(JLabel.LEFT);
        imageLabel = new JLabel();
        acceptButton = new JButton("OK");
        acceptButton.setEnabled(false);
        acceptButton.addActionListener(this);

        getRootPane().setDefaultButton(acceptButton);

        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/progressrun.gif"));
        imageLabel.setIcon(icon);
        lblPane.add(msgLabel);
        imgPane.add("Center",imageLabel);
        btnPane.add(acceptButton);
        contentPane.add(imgPane);
        contentPane.add(lblPane);
        contentPane.add(btnPane);

        //imgPane
        layout.putConstraint(SpringLayout.WEST, imgPane,
                             10,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, imgPane,
                             10,
                             SpringLayout.NORTH, contentPane);

        //lblPane
        layout.putConstraint(SpringLayout.WEST, lblPane,
                             10,
                             SpringLayout.EAST, imgPane);
        layout.putConstraint(SpringLayout.NORTH, lblPane,
                             10,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, lblPane,
                             0,
                             SpringLayout.SOUTH, imgPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             10,
                             SpringLayout.EAST, lblPane);

        //btnPane
        layout.putConstraint(SpringLayout.WEST, btnPane,
                             10,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btnPane,
                             10,
                             SpringLayout.SOUTH, lblPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             10,
                             SpringLayout.SOUTH, btnPane);
        layout.putConstraint(SpringLayout.EAST, btnPane,
                             0,
                             SpringLayout.EAST, lblPane);



        pack();
    }

    public void formWindowOpened(WindowEvent e) {
        timer = new javax.swing.Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Configuration cfg = new Configuration();
                if (cfg.getthreadFinished())
                {
                    timer.stop();
                    acceptButton.setEnabled(true);
                    msgLabel.setText(doneMessage);
                    ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/progressdone.gif"));
                    imageLabel.setIcon(icon);
                }
            }
        });
        timer.start();
    }

    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource().equals(acceptButton))
        {
            dispose();
        }
    }

    private JPanel imgPane, btnPane, lblPane;
    private JLabel imageLabel, msgLabel;
    private JButton acceptButton;
    private Timer timer;
    private String startMessage, doneMessage;
}
