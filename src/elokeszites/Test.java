/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Forcek László
 */
public class Test extends JExtendedFrame{

    public Test()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/printtoexcel.png"));
        this.setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        setTitle("Leltáradatok");
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 3),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2)));
        setformID(formID);

        menuBar = new JMenuBar();
        mainMenu = new javax.swing.JMenu();
        xlsMenu = new javax.swing.JMenuItem();
        navMenu = new javax.swing.JMenuItem();
        kilepMenu = new javax.swing.JMenuItem();

        mainMenu.setText("Menü");

        xlsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        xlsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/disk.png"))); // NOI18N
        xlsMenu.setText("Excel lista mentés");
        xlsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xlsMenuActionPerformed(evt);
            }
        });
        mainMenu.add(xlsMenu);
        mainMenu.add(new JSeparator());

        navMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        navMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/loaddata.png"))); // NOI18N
        navMenu.setText("Navison leltárlista");
        navMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                navMenuActionPerformed(evt);
            }
        });
        mainMenu.add(navMenu);
        mainMenu.add(new JSeparator());

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/close.png"))); // NOI18N
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);
        menuBar.add(mainMenu);
        setJMenuBar(menuBar);
        
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        datePane = new Canvas();
        datePane.setBorder(new EtchedBorder());
        pane = new JPanel();
        pane.setOpaque(true);
        tScroll = new JScrollPane(datePane);
        tScroll.getViewport().add(datePane);
        
        contentPane.add(pane);

        layout.putConstraint(SpringLayout.WEST, pane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, pane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             1,
                             SpringLayout.SOUTH, pane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             1,
                             SpringLayout.EAST, pane);
        pack();
        
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void xlsMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (g2d == null) g2d = (Graphics2D)pane.getGraphics();
        g2d.draw(new Line2D.Double(100,100,250,130));
    }

    private void navMenuActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void sqlolvas(String sqlcmd)
    {
        rSet = null;
        try
        {
            rSet = stmt.executeQuery(sqlcmd);
        }
        catch (SQLException ex)
        {
            System.out.println("kapcsolódási hiba: " + ex.getMessage());
        }
    }

    class Canvas extends JPanel {

        public Canvas () {
            setSize(getPreferredSize());
            this.setBackground(Color.white);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 1000);
        }
        
//        @Override
//        public void paint(Graphics g)
//        {
//            super.paint(g);
//        }
//
//        @Override
//        public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 10;
    private Canvas datePane;
    private JPanel pane;
    private JScrollPane tScroll;
    private JMenuBar menuBar;
    private JMenuItem xlsMenu, navMenu, kilepMenu;
    private JMenu mainMenu;
    private Graphics2D g2d;
}
