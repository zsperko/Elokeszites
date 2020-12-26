/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Container;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Forcek László
 */
public class Connect extends JExtendedFrame{


    public Connect()
    {
        initComponents();
    }

    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/refresh.png"));
        setResizable(false);
        setClosable(true);
        setIconifiable(true);
        setTitle("Kapcsolat beállítása");
        setName("Form");
        setFrameIcon(icon);
        setformID(formID);

        JLabel Label1 = new JLabel("Host");
        JLabel Label2 = new JLabel("Port");
        JLabel Label3 = new JLabel("Felhasználói név");
        JLabel Label4 = new JLabel("Jelszó");

        userName = new JTextField(15);
        portField = new JTextField(15);
        hostField = new JTextField(15);
        passField = new JPasswordField(15);
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        mentesMenu = new JMenuItem();
        testMenu = new JMenuItem();
        Separator1 = new JSeparator();
        kilepMenu = new JMenuItem();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
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
                formInternalFrameOpened(evt);
            }
        });

        portField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                portFieldFocusLost(evt);
            }
        });

        mainMenu.setText("Menü");

        mentesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/disk.png")));
        mentesMenu.setText("Mentés");
        mentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentesMenu);

        testMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        testMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/test.png")));
        testMenu.setText("Kapcsolat teszt");
        testMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testMenuActionPerformed(evt);
            }
        });
        mainMenu.add(testMenu);
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
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JPanel inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(hostField);
        inputPane.add(hostField);
        
        inputPane.add(Label2);
        Label2.setLabelFor(portField);
        inputPane.add(portField);

        inputPane.add(Label3);
        Label3.setLabelFor(userName);
        inputPane.add(userName);

        inputPane.add(Label4);
        Label4.setLabelFor(passField);
        inputPane.add(passField);

        SpringUtilities.makeCompactGrid(inputPane,
                                        4, 2, //rows, cols
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

    private void portFieldFocusLost(java.awt.event.FocusEvent evt) {
        try
        {
            Integer.parseInt(portField.getText());
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null,"A beírt adat hibás: " + ex.getMessage());
            portField.setText("");
        }
    }

    private void testMenuActionPerformed(java.awt.event.ActionEvent evt) {
        String pass = "";
        for (int i = 0;i < passField.getPassword().length;i++)
            pass += passField.getPassword()[i];
        if (userName.getText().compareTo("") != 0 &&
            pass.compareTo("") != 0 &&
            hostField.getText().compareTo("") != 0 &&
            portField.getText().compareTo("") != 0)
        {
            sqlConnStr = "jdbc:mysql://" + hostField.getText() +
                         ":" + portField.getText() + "/?user=" + userName.getText() +
                         "&password=" + pass;
            try
            {
                conn = DriverManager.getConnection(sqlConnStr);
                conn.close();
                JOptionPane.showMessageDialog(null,"Kapcsolat rendben!");
                adatatad();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex){}
            }
        }
    }

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        boolean exists = (new File("config.dat")).exists();
        if (exists)
        {
            cfg.beolvas();
            Array = cfg.getArray();
            userName.setText(Array[0]);
            passField.setText(Array[1]);
            hostField.setText(Array[2]);
            portField.setText(Array[3]);
        }
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/cog.png"));
        this.setFrameIcon(icon);
    }

    private void mentesMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        String pass = "";
        for (int i = 0;i < passField.getPassword().length;i++)
            pass += passField.getPassword()[i];
        if (userName.getText().compareTo("") != 0 &&
            pass.compareTo("") != 0 &&
            hostField.getText().compareTo("") != 0 &&
            portField.getText().compareTo("") != 0)
        {
            adatatad();
            cfg.ment();
            cfg.beolvas();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.close();
                cfg.setconnOK(true);
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex){}
            }
        }
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

    private void adatatad()
    {
        Configuration cfg = new Configuration();
        String pass = "";
        for (int i = 0;i < passField.getPassword().length;i++)
            pass += passField.getPassword()[i];
        Array[0] = userName.getText();
        Array[1] = pass;
        Array[2] = hostField.getText();
        Array[3] = portField.getText();
        cfg.setArray(Array);
    }

    private Connection conn = null;
    private String Array[] = new String[4];
    private String sqlConnStr = "";
    private int formID = 1;
    private JTextField userName,hostField,portField;
    private JPasswordField passField;
    private JMenuBar menuBar;
    private JMenu mainMenu;
    private JMenuItem mentesMenu, testMenu, kilepMenu;
    private JSeparator Separator1;
}
