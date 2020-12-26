/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Forcek László
 */
public class Tools extends JExtendedFrame{

    public Tools()
    {
        initComponents();
    }

    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/tools.png"));
        setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        setTitle("Gépcsoportok");
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 2.0),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2.0)));
        setformID(formID);
        
        gepekTabla = new JTable();
        gepszamFilter = new JTextField(20);
        Gepszam = new JTextField(20);
        Modosit = new JCheckBox();
        mScroll = new JScrollPane();
        tScroll = new JScrollPane();
        Megnevezes = new JTextPane();
        Muhely = new JComboBox();
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        mentesMenu = new JMenuItem();
        modositMenu = new JMenuItem();
        torolMenu = new JMenuItem();
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
            }
        });

        gepekTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        gepekTabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        gepekTabla.getTableHeader().setResizingAllowed(false);
        gepekTabla.getTableHeader().setReorderingAllowed(false);
        gepekTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                gepekTablaMouseReleased(evt);
            }
        });
        tScroll.setViewportView(gepekTabla);

        gepszamFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                gepszamFilterKeyReleased(evt);
            }
        });

        Modosit.setText("Módosítás");
        Modosit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ModositItemStateChanged(evt);
            }
        });

        Gepszam.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Gepszam.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                GepszamFocusGained(evt);
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                GepszamFocusLost(evt);
            }
        });

        mScroll.setViewportView(Megnevezes);

        mainMenu.setText("Menü");

        mentesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/disk.png"))); // NOI18N
        mentesMenu.setText("Mentés");
        mentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentesMenu);

        modositMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        modositMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/change.png"))); // NOI18N
        modositMenu.setText("Módosítás");
        modositMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositMenuActionPerformed(evt);
            }
        });
        mainMenu.add(modositMenu);
        mainMenu.add(new JSeparator());

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/eraseall.png"))); // NOI18N
        torolMenu.setText("Törlés");
        torolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torolMenuActionPerformed(evt);
            }
        });
        mainMenu.add(torolMenu);
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

        Geptablatolt();
        Gepszam.setDocument(new JTextFieldLimit(10));
        gepszamFilter.setDocument(new JTextFieldLimit(10));
        Comboboxtolt(Muhely,"SELECT azon, CONCAT(CAST(muhelyek.szam as char charset utf8),' - ', "
                   + "megnevezes) FROM muhelyek ORDER BY szam");

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Gépszám");
        JLabel Label2 = new JLabel("Név");
        JLabel Label3 = new JLabel("Műhely");
        JLabel Label4 = new JLabel("Gépszám szűrő");
        JLabel Label5 = new JLabel("");
        
        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());

        inputPane.add(Label1);
        Label1.setLabelFor(Gepszam);
        inputPane.add(Gepszam);

        inputPane.add(Label2);
        Label2.setLabelFor(mScroll);
        inputPane.add(mScroll);

        inputPane.add(Label3);
        Label3.setLabelFor(Muhely);
        inputPane.add(Muhely);

        inputPane.add(Label4);
        Label4.setLabelFor(gepszamFilter);
        inputPane.add(gepszamFilter);

        inputPane.add(Label5);
        inputPane.add(Modosit);

        SpringUtilities.makeCompactGrid(inputPane,
                                        5, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        tblPane.add(tScroll);
        contentPane.add(inputPane);
        contentPane.add(tblPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);

         //tblPane
        layout.putConstraint(SpringLayout.WEST, tblPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, tblPane,
                             1,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             1,
                             SpringLayout.EAST, contentPane);

        pack();
    }
    
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {                                          
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }                                         

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }                                         

    private void gepszamFilterKeyReleased(java.awt.event.KeyEvent evt) {                                          
        Geptablatolt();
    }                                         

    private void gepekTablaMouseReleased(java.awt.event.MouseEvent evt) {                                         
        if (Modosit.isSelected() && gepekTabla.getSelectedRow() != 0)
        {
            Gepszam.setText(gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 1).toString());
            Megnevezes.setText(gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 2).toString());
            Combokeres(Muhely,gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 3).toString());
        }
    }                                        

    private void GepszamFocusGained(java.awt.event.FocusEvent evt) {                                    
        oldGepszam = Gepszam.getText();
    }                                   

    private void GepszamFocusLost(java.awt.event.FocusEvent evt) {                                  
        try
        {
            if (Gepszam.getText().compareTo("") != 0)
            {
                Gepszam.setText(Integer.toString(Integer.parseInt(Gepszam.getText())));
            }
        }
        catch (Exception ex)
        {
            Gepszam.setText(oldGepszam);
        }
    }                                 

    private void ModositItemStateChanged(java.awt.event.ItemEvent evt) {                                         
        if (Modosit.isSelected())
        {
            if (gepekTabla.getSelectedRow() != 0)
            {
                Gepszam.setText(gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 1).toString());
                Megnevezes.setText(gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 2).toString());
                Combokeres(Muhely,gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 3).toString());
            }
        }
        else
        {
            Gepszam.setText("");
            Megnevezes.setText("");
        }
    }                                        

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (Modosit.isSelected() == false &&
            gepekTabla.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null, "Biztosan törli a kijelölt gépcsoportot?","Törlés jóváhagyása", JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlolvas("SELECT azon FROM gr_muveletterv WHERE gepszam = '" +
                             gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 0).toString() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"A megadott gépcsoport már használatban van egy vagy több gyártási rendelésben!");
                    }
                    else
                    {
                        sqlupdate("DELETE szerszamok.* FROM szerszamok WHERE azon = '" +
                                  gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 0).toString() + "'");
                    }
                    conn.commit();
                    conn.close();
                    Geptablatolt();
                    Alaphelyzet();
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

    private void mentesMenuActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if (!Modosit.isSelected() &&
            Megnevezes.getText().compareTo("") != 0 &&
            Gepszam.getText().compareTo("") != 0)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null, "Felviszi az új gépcsoportot?","Felvitel jóváhagyása", JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlolvas("SELECT azon FROM szerszamok WHERE szam = '" +
                             Gepszam.getText() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"A megadott gépcsoport már létezik!");
                    }
                    else
                    {
                        sqlupdate("INSERT INTO szerszamok (szam,megnevezes,muhelykod) VALUES ('" +
                                  Gepszam.getText() + "','" + Megnevezes.getText() + "','" +
                                  ComboID(Muhely) + "')");
                    }
                    conn.commit();
                    conn.close();
                    Geptablatolt();
                    Alaphelyzet();
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

    private void modositMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (Modosit.isSelected() && gepekTabla.getSelectedRow() != 0 &&
            Megnevezes.getText().compareTo("") != 0 && Gepszam.getText().compareTo("") != 0)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null, "Módosítja a kijelölt gépcsoportot?","Módosítás jóváhagyása", JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlolvas("SELECT azon FROM szerszamok WHERE szam = '" +
                             Gepszam.getText() + "' AND azon <> '" +
                             gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 0).toString() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"A megadott gépcsoport már létezik!");
                    }
                    else
                    {
                        sqlupdate("UPDATE szerszamok SET szam = '" +
                                  Gepszam.getText() + "',megnevezes = '" +
                                  Megnevezes.getText() + "',muhelykod = '" +
                                  ComboID(Muhely) + "' WHERE azon = '" +
                                  gepekTabla.getValueAt(gepekTabla.getSelectedRow(), 0).toString() + "'");
                    }
                    conn.commit();
                    conn.close();
                    Geptablatolt();
                    Alaphelyzet();
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

    private void Alaphelyzet()
    {
        Gepszam.setText("");
        Megnevezes.setText("");
        Modosit.setSelected(false);
    }

    private void Comboboxtolt(JComboBox combo, String sqlcmd)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas(sqlcmd);
            if (rSet.next())
            {
                do
                {
                    combo.addItem(new ListItem(rSet.getInt(1),rSet.getString(2).trim()));
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

    private int ComboID(JComboBox combo)
    {
        ListItem selectedType = (ListItem)combo.getSelectedItem();
        return selectedType.getValue();
    }

    private String ComboVAL(JComboBox combo)
    {
        ListItem selectedType = (ListItem)combo.getSelectedItem();
        return selectedType.toString();
    }

    private void Combokeres(JComboBox combo, String ertek)
    {
        int i = 0;
        while (combo.getItemCount() - 1 > i &&
               ((ListItem)combo.getItemAt(i)).toString().compareTo(ertek) != 0)
        {
            i++;
        }

        combo.setSelectedIndex(i);
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

    private void Geptablatolt()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Gépszám");
        tableHeaders.add("Név");
        tableHeaders.add("Műhelykód");
        tableHeaders.add("Műhelyszám");
        tableHeaders.add("Műhely név");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            int hosz = gepszamFilter.getText().length();
            sqlolvas("SELECT szerszamok.azon,szerszamok.szam,szerszamok.megnevezes," +
                     "szerszamok.muhelykod,muhelyek.szam,muhelyek.megnevezes FROM " +
                     "szerszamok INNER JOIN muhelyek ON (szerszamok.muhelykod = muhelyek.azon) " +
                     "WHERE LEFT(szerszamok.szam,'" + hosz + "') = '" + gepszamFilter.getText() + "' " +
                     "ORDER BY szerszamok.szam");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                oneRow.add(rSet.getString(4));
                oneRow.add(rSet.getString(5));
                oneRow.add(rSet.getString(6));
                tableData.add(oneRow);
            }
            conn.close();
            gepekTabla.setModel(new NoEditModel(tableData, tableHeaders));
            gepekTabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            gepekTabla.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            gepekTabla.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
            gepekTabla.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

            gepekTabla.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
            gepekTabla.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
            gepekTabla.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(0);
            gepekTabla.getTableHeader().getColumnModel().getColumn(3).setResizable(false);
            gepekTabla.setAutoCreateRowSorter(true);
            gepekTabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            TableColumnModel tcm = gepekTabla.getColumnModel();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
            FormatRenderer render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.RIGHT);
            tcm.getColumn(4).setCellRenderer(render);
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

    private JTextField Gepszam, gepszamFilter;
    private JTextPane Megnevezes;
    private JCheckBox Modosit;
    private JComboBox Muhely;
    private JTable gepekTabla;
    private JScrollPane tScroll, mScroll;
    private JPanel inputPane, tblPane;
    private JMenuItem kilepMenu;
    private JMenu mainMenu;
    private JMenuItem mentesMenu, modositMenu, torolMenu;
    private JMenuBar menuBar;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 8;
    private String oldGepszam;
}
