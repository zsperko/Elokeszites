/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.SpringUtilities;
import classes.AutoComplete;
import classes.JExtendedFrame;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Forcek László
 */
public class Items extends JExtendedFrame{

    public Items()
    {
        initComponents();
        Cikkszam.grabFocus();
    }
    
    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/grid.png"));
        setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setTitle("Cikkszámok");
        //Toolkit defaultTool = Toolkit.getDefaultToolkit();
        //setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 1.5),
        //                               (int)(defaultTool.getScreenSize().getHeight() / 1.5)));
        setformID(formID);

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
            }
        });

        Cikkszam = new JComboBox();
        Mennyiseg = new JTextField(20);
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        modositMenu = new JMenuItem();
        kilepMenu = new JMenuItem();

        Cikkszam.setEditable(true);

        Mennyiseg.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        Mennyiseg.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                MennyisegFocusGained(evt);
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                MennyisegFocusLost(evt);
            }
        });
        Mennyiseg.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                MennyisegKeyReleased(evt);
            }
        });

        Mennyiseg.setText("0");

        mainMenu.setText("Menü");

        modositMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        modositMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/change.png"))); // NOI18N
        modositMenu.setText("Módosítás");
        modositMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositMenuActionPerformed(evt);
            }
        });
        mainMenu.add(modositMenu);
        mainMenu.add(new JSeparator());

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/close.png"))); // NOI18N
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);

        menuBar.add(mainMenu);

        setJMenuBar(menuBar);

        comboboxtolt(Cikkszam,"SELECT azon,cikkszam FROM cikktorzs ORDER BY cikkszam");
        JTextComponent editor = (JTextComponent)Cikkszam.getEditor().getEditorComponent();
        editor.setDocument(new AutoComplete(Cikkszam));
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_ENTER:
                        Mennyiseg.grabFocus();
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Cikkszám");
        JLabel Label2 = new JLabel("Mennyiség");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Cikkszam);
        inputPane.add(Cikkszam);
        
        inputPane.add(Label2);
        Label2.setLabelFor(Mennyiseg);
        inputPane.add(Mennyiseg);

        SpringUtilities.makeCompactGrid(inputPane,
                                        2, 2, //rows, cols
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
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             1,
                             SpringLayout.EAST, inputPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             1,
                             SpringLayout.SOUTH, inputPane);
        
        pack();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void MennyisegFocusGained(java.awt.event.FocusEvent evt) {                                      
        CikkszamKeres();
        if (Mennyiseg.getText().compareTo("") != 0)
        {
            oldMennyi = Mennyiseg.getText();
            Mennyiseg.selectAll();
        }
    }                                     

    private void MennyisegFocusLost(java.awt.event.FocusEvent evt) {                                    
        if (Mennyiseg.getText().compareTo("") != 0)
        {
            try
            {
                Mennyiseg.setText(Double.toString(Double.parseDouble(Mennyiseg.getText().replace(",", "."))));
            }
            catch (Exception ex)
            {
                Mennyiseg.setText(oldMennyi);
            }
        }
    }

    private void CikkszamKeres()
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT utem FROM cikktorzs WHERE cikkszam = '" +
                      Cikkszam.getSelectedItem().toString() + "'");
            Mennyiseg.setText("0");
            if (rSet.next())
            {
                rSet.first();
                Mennyiseg.setText(rSet.getString(1));
                oldMennyi = Mennyiseg.getText();
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
        finally
        {
            try
            {conn.close();}
            catch (SQLException ex){}
        }
    }

    private void sqlupdate(String sqlcmd)
    {
        try
        {
            stmt.executeUpdate(sqlcmd);
        }
        catch (SQLException ex)
        {
            System.out.println("Kapcsolódási hiba: " + ex.getMessage());
        }
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }                                         

    private void modositMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Modosit();
    }

    private void Modosit()
    {
        if (Mennyiseg.getText().compareTo("") != 0 &&
            Mennyiseg.getText().compareTo("0") != 0 &&
            Mennyiseg.getText().compareTo(oldMennyi) != 0 &&
            Cikkszam.getSelectedIndex() != -1)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null, "Biztosan módosítja az adatokat?",
                                                              "Adatok módosítása",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlupdate("UPDATE cikktorzs SET utem = '" + Mennyiseg.getText() + "' " +
                              "WHERE cikkszam = '" +  Cikkszam.getSelectedItem().toString() + "'");
                    conn.commit();
                    conn.close();
                    Mennyiseg.setText("0");
                    Cikkszam.setSelectedIndex(0);
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
                }
                finally
                {
                    try
                    {conn.close();}
                    catch (SQLException ex){}
                }
            }
        }
    }

    private void MennyisegKeyReleased(java.awt.event.KeyEvent evt) {                                      
        try
        {
            Double.parseDouble(Mennyiseg.getText().replace(",", "."));
            switch (evt.getKeyCode())
            {
                case KeyEvent.VK_ENTER:
                    Modosit();
                    break;
            }
        }
        catch (Exception ex){}

    }                                     

    private void comboboxtolt(JComboBox combo,String sqlcmd)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            combo.setModel (new DefaultComboBoxModel ());
            stmt = conn.createStatement();
            sqlolvas(sqlcmd);
            if (rSet.next())
            {
                do
                {
                    combo.addItem(rSet.getString(2));
                } while (rSet.next());
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
        finally
        {
            try
            {conn.close();}
            catch (SQLException ex){}
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
    
    private JComboBox Cikkszam;
    private JPanel inputPane;
    private JTextField Mennyiseg;
    private JMenuItem modositMenu, kilepMenu;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private String oldMennyi;
    private int formID = 23;
}
