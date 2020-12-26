/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Container;
import java.awt.Font;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;

/**
 *
 * @author Forcek László
 */
public class PasswordChange extends JExtendedFrame{

    public PasswordChange()
    {
        initComponents();       
    }

    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/key.png"));
        setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setTitle("Jelszó váltás");
        setformID(formID);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameActivated(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        mentesMenu = new JMenuItem();
        kilepMenu = new JMenuItem();

        mainMenu.setText("Menü");

        mentesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/disk.png")));
        mentesMenu.setText("Mentés");
        mentesMenu.setName("mentesMenu");
        mentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentesMenu);

        JSeparator Separator1 = new JSeparator();
        mainMenu.add(Separator1);

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/close.png")));
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);

        menuBar.add(mainMenu);
        setJMenuBar(menuBar);

        fullName = new JTextField(15);
        userName = new JTextField(15);
        fullName.setEditable(false);
        userName.setEditable(false);

        oldPass = new JPasswordField();
        newPass1 = new JPasswordField();
        newPass2 = new JPasswordField();
        
        JLabel Label1 = new JLabel("Felhasználó");
        JLabel Label2 = new JLabel("Azonosító");
        JLabel Label3 = new JLabel("Régi jelszó");
        JLabel Label4 = new JLabel("Új jelszó");
        JLabel Label5 = new JLabel("Új jelszó megerősítés");
        Font font = Label1.getFont();
        font = font.deriveFont((float)(font.getSize2D() * 14.0 / 11.0));
        font = font.deriveFont(Font.BOLD);
        Label1.setFont(font);
        Label2.setFont(font);
        Label3.setFont(font);
        Label4.setFont(font);
        Label5.setFont(font);
        
        inputPane = new JPanel(new SpringLayout());
        inputPane.setOpaque(false);

        inputPane.add(Label1);
        Label1.setLabelFor(fullName);
        inputPane.add(fullName);

        inputPane.add(Label2);
        Label2.setLabelFor(userName);
        inputPane.add(userName);

        inputPane.add(Label3);
        Label3.setLabelFor(oldPass);
        inputPane.add(oldPass);

        inputPane.add(Label4);
        Label4.setLabelFor(newPass1);
        inputPane.add(newPass1);

        inputPane.add(Label5);
        Label5.setLabelFor(newPass2);
        inputPane.add(newPass2);

        SpringUtilities.makeCompactGrid(inputPane,
                                        5, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad
        contentPane.add(inputPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             1,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             1,
                             SpringLayout.EAST, inputPane);
        pack();
    }


    private void formInternalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/password.gif"));
        resizeImageIcon imgr = new resizeImageIcon(icon,inputPane.getWidth(),inputPane.getHeight());
        imgPane = new ImagePanel(imgr.getImage().getImage(), 0.4f);
        getContentPane().add(imgPane);

        Configuration cfg = new Configuration();
        oldPass.grabFocus();
        try
        {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement();
                sqlolvas("SELECT nev, username FROM users WHERE azon = '" +
                         Integer.toString(cfg.getcurrentUser()) + "'");
                rSet.first();
                fullName.setText(rSet.getString(1));
                userName.setText(rSet.getString(2));
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"A beírt adat hibás: " + ex.getMessage());
        }
    }

    private void mentesMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        String oldPassStr = PassToString(oldPass.getPassword());
        String newPassStr1 = PassToString(newPass1.getPassword());
        String newPassStr2 = PassToString(newPass2.getPassword());
        if (oldPassStr.compareTo("") != 0 &&
            newPassStr1.compareTo("") != 0 &&
            newPassStr2.compareTo("") != 0)
        {
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                sqlolvas("SELECT username FROM users WHERE azon = '" + Integer.toString(cfg.getcurrentUser()) +
                         "' AND password = MD5('" + oldPassStr + "')");
                if (rSet.next())
                {
                    if (newPassStr1.compareTo(newPassStr2) == 0)
                    {
                        sqlupdate("UPDATE users SET password = MD5('" +
                                  newPassStr1 + "') WHERE azon = '" +
                                  Integer.toString(cfg.getcurrentUser()) + "'");
                        oldPass.setText("");
                        newPass1.setText("");
                        newPass2.setText("");
                        JOptionPane.showMessageDialog(null,"A jelszava megváltozott!");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Az új jelszó nem egyezik a megerősítő jelszóval!");
                        newPass1.setText("");
                        newPass2.setText("");
                        newPass1.grabFocus();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"A beírt jelszó nem megfelelő!");
                    oldPass.setText("");
                    oldPass.grabFocus();
                }
                conn.commit();
                conn.close();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
            }
        }
    }

    private String PassToString(char[] pass)
    {
        String passStr = "";
        for (int i = 0;i < pass.length;i++) passStr += pass[i];
        return passStr;
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void sqlupdate(String sqlcmd)
    {
        try
        {
            stmt.executeUpdate(sqlcmd);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
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
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
    }

    private JPanel inputPane;
    private ImagePanel imgPane;
    private JTextField fullName, userName;
    private JPasswordField oldPass, newPass1, newPass2;
    private JMenuBar menuBar;
    private JMenu mainMenu;
    private JMenuItem mentesMenu, kilepMenu;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 2;
}