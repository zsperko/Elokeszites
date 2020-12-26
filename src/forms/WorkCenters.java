/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
import classes.NoEditModel;
import classes.SpringUtilities;
import classes.FormatRenderer;
import classes.JExtendedFrame;
import classes.JTextFieldLimit;
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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Forcek László
 */
public class WorkCenters extends JExtendedFrame{

    
    public WorkCenters()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/groupping.png"));
        setFrameIcon(icon);
        setTitle("Műhelyek");
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 2.0),
                                       (int)(defaultTool.getScreenSize().getHeight() / 1.5)));
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

        mScroll = new JScrollPane();
        tScroll = new JScrollPane();
        muhelyTabla = new JTable();
        muhelyFilter = new JTextField();
        Muhelyszam = new JTextField(20);
        Modosit = new JCheckBox();
        Megnevezes = new JTextArea(3,20);
        Kosp = new JComboBox();
        Terulet = new JComboBox();
        Gepszam = new JTextField(20);
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        mentesMenu = new JMenuItem();
        modositMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();

        muhelyTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        muhelyTabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        muhelyTabla.getTableHeader().setResizingAllowed(false);
        muhelyTabla.getTableHeader().setReorderingAllowed(false);
        muhelyTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                muhelyTablaMouseReleased(evt);
            }
        });
        tScroll.setViewportView(muhelyTabla);

        muhelyFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                muhelyFilterKeyReleased(evt);
            }
        });

        Modosit.setText("Módosítás");
        Modosit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ModositItemStateChanged(evt);
            }
        });

        Muhelyszam.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Muhelyszam.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                MuhelyszamFocusGained(evt);
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                MuhelyszamFocusLost(evt);
            }
        });

        mScroll.setViewportView(Megnevezes);
        Gepszam.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        mainMenu.setText("Menü");

        mentesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png")));
        mentesMenu.setText("Mentés");
        mentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentesMenu);

        modositMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        modositMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/change.png"))); // NOI18N
        modositMenu.setText("Módosítás");
        modositMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modositMenuActionPerformed(evt);
            }
        });
        mainMenu.add(modositMenu);
        mainMenu.add(new JSeparator());

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png"))); // NOI18N
        torolMenu.setText("Törlés");
        torolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torolMenuActionPerformed(evt);
            }
        });
        mainMenu.add(torolMenu);
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

        Muhelytablatolt();
        Muhelyszam.setDocument(new JTextFieldLimit(10));
        Gepszam.setDocument(new JTextFieldLimit(5));
        muhelyFilter.setDocument(new JTextFieldLimit(10));
        Comboboxtolt(Kosp,"SELECT azon, CONCAT(szam,' - ', megnevezes) FROM kosp ORDER BY szam");
        Comboboxtolt(Terulet,"SELECT azon,megnevezes FROM terulet ORDER BY megnevezes");

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Műhelyszám");
        JLabel Label2 = new JLabel("Név");
        JLabel Label3 = new JLabel("Költséghely");
        JLabel Label4 = new JLabel("Gépszám");
        JLabel Label5 = new JLabel("Műhelyszám szűrő");
        JLabel Label6 = new JLabel("Terület");
        JLabel Label7 = new JLabel("");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());

        inputPane.add(Label1);
        Label1.setLabelFor(Muhelyszam);
        inputPane.add(Muhelyszam);

        inputPane.add(Label2);
        Label2.setLabelFor(mScroll);
        inputPane.add(mScroll);

        inputPane.add(Label3);
        Label3.setLabelFor(Kosp);
        inputPane.add(Kosp);

        inputPane.add(Label4);
        Label4.setLabelFor(Gepszam);
        inputPane.add(Gepszam);

        inputPane.add(Label5);
        Label5.setLabelFor(muhelyFilter);
        inputPane.add(muhelyFilter);

        inputPane.add(Label6);
        Label6.setLabelFor(Terulet);
        inputPane.add(Terulet);

        inputPane.add(Label7);
        inputPane.add(Modosit);

        tblPane.add(tScroll);

        SpringUtilities.makeCompactGrid(inputPane,
                                        7, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad
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

    private void muhelyFilterKeyReleased(java.awt.event.KeyEvent evt) {                                         
        Muhelytablatolt();
    }                                        

    private void muhelyTablaMouseReleased(java.awt.event.MouseEvent evt) {                                          
        if (Modosit.isSelected())
        {
            if (muhelyTabla.getSelectedRow() != -1)
            {
                Muhelyszam.setText(muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 1).toString());
                Megnevezes.setText(muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 2).toString());
                if (muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 6) != null)
                {
                    Gepszam.setText(muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 6).toString());
                }
                else
                {
                    Gepszam.setText("");
                }
                Combokeres(Kosp,muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 3).toString());
                Combokeres(Terulet,muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 7).toString());
            }
        }
        else
        {
            Muhelyszam.setText("");
            Megnevezes.setText("");
            Gepszam.setText("");
        }
    }                                         

    private void MuhelyszamFocusGained(java.awt.event.FocusEvent evt) {                                       
        oldMuhelyszam = Muhelyszam.getText();
    }                                      

    private void MuhelyszamFocusLost(java.awt.event.FocusEvent evt) {                                     
        try
        {
            if (Muhelyszam.getText().compareTo("") != 0)
            {
                Muhelyszam.setText(Integer.toString(Integer.parseInt(Muhelyszam.getText())));
            }
        }
        catch (Exception ex)
        {
            Muhelyszam.setText(oldMuhelyszam);
        }
    }                                    

    private void ModositItemStateChanged(java.awt.event.ItemEvent evt) {                                         
        if (Modosit.isSelected())
        {
            if (muhelyTabla.getSelectedRow() != -1)
            {
                Muhelyszam.setText(muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 1).toString());
                Megnevezes.setText(muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 2).toString());
                if (muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 6) != null)
                {
                    Gepszam.setText(muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 6).toString());
                }
                else
                {
                    Gepszam.setText("");
                }
                Combokeres(Kosp,muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 3).toString());
                Combokeres(Terulet,muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 7).toString());
            }
        }
        else
        {
            Muhelyszam.setText("");
            Megnevezes.setText("");
            Gepszam.setText("");
        }
    }                                        

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (Modosit.isSelected() == false &&
            muhelyTabla.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null,"Biztosan törli a kijelölt műhelyszámot?","Törlés jóváhagyása",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlolvas("SELECT azon FROM szerszamok WHERE muhelykod = '" +
                             muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 0).toString() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"A megadott műhely már használatban van egy vagy több gépcsoportnál!");
                    }
                    else
                    {
                        sqlupdate("DELETE muhelyek.* FROM muhelyek WHERE azon = '" +
                                  muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 0).toString() + "'");
                    }
                    conn.commit();
                    conn.close();
                    Muhelytablatolt();
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
            Muhelyszam.getText().compareTo("") != 0)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null,"Felviszi az új műhelyszámot?","Felvitel jóváhagyása",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlolvas("SELECT azon FROM muhelyek WHERE szam = '" +
                             Muhelyszam.getText() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"A megadott műhelyszám már létezik!");
                    }
                    else
                    {
                        sqlupdate("INSERT INTO muhelyek (szam,megnevezes,ktghely,gepszam,terulet) VALUES ('" +
                                  Muhelyszam.getText() + "','" + Megnevezes.getText() + "','" +
                                  ComboID(Kosp) + "','" +
                                  Gepszam.getText() + "','" + 
                                  ComboID(Terulet) + "')");
                    }
                    conn.commit();
                    conn.close();
                    Muhelytablatolt();
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
        Muhelyszam.setText("");
        Megnevezes.setText("");
        Modosit.setSelected(false);
    }

    private void modositMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (Modosit.isSelected() && muhelyTabla.getSelectedRow() != -1 &&
            Megnevezes.getText().compareTo("") != 0 && Muhelyszam.getText().compareTo("") != 0)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null,"Módosítja a kijelölt műhelyszámot?","Módosítás jóváhagyása",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    sqlolvas("SELECT azon FROM muhelyek WHERE szam = '" +
                             Muhelyszam.getText() + "' AND azon <> '" +
                             muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 0).toString() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"A megadott műhelyszám már létezik!");
                    }
                    else
                    {
                        sqlupdate("UPDATE muhelyek SET szam = '" +
                                  Muhelyszam.getText() + "',megnevezes = '" +
                                  Megnevezes.getText() + "',ktghely = '" +
                                  ComboID(Kosp) + "',gepszam = '" +
                                  Gepszam.getText() + "',terulet = '" +
                                  ComboID(Terulet) + "' " +
                                  " WHERE azon = '" +
                                  muhelyTabla.getValueAt(muhelyTabla.getSelectedRow(), 0).toString() + "'");
                    }
                    conn.commit();
                    conn.close();
                    Muhelytablatolt();
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

    private void Muhelytablatolt()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Műhelyszám");
        tableHeaders.add("Név");
        tableHeaders.add("Ktghely");
        tableHeaders.add("KOSP kód");
        tableHeaders.add("Költséghely neve");
        tableHeaders.add("Gépszám");
        tableHeaders.add("Terület");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            int hosz = muhelyFilter.getText().length();
            sqlolvas("SELECT muhelyek.azon,muhelyek.szam AS muhelyszam,muhelyek.megnevezes," +
                     "muhelyek.ktghely,kosp.szam,kosp.megnevezes AS kosp,muhelyek.gepszam," +
                     "muhelyek.terulet FROM muhelyek " +
                     "INNER JOIN kosp ON (muhelyek.ktghely = kosp.azon) WHERE " +
                     "LEFT(muhelyek.szam,'" + hosz + "') = '" + muhelyFilter.getText() + "' " +
                     "ORDER BY muhelyek.szam ");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                oneRow.add(rSet.getString(4));
                oneRow.add(rSet.getString(5));
                oneRow.add(rSet.getString(6));
                oneRow.add(rSet.getString(7));
                oneRow.add(rSet.getString(8));
                tableData.add(oneRow);
            }
            conn.close();
            muhelyTabla.setModel(new NoEditModel(tableData, tableHeaders));
            muhelyTabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

            muhelyTabla.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(3).setResizable(false);

            muhelyTabla.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(7).setPreferredWidth(0);
            muhelyTabla.getTableHeader().getColumnModel().getColumn(7).setResizable(false);

            muhelyTabla.setAutoCreateRowSorter(true);
            muhelyTabla.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            TableColumnModel tcm = muhelyTabla.getColumnModel();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
            FormatRenderer render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.RIGHT);
            tcm.getColumn(4).setCellRenderer(render);
            tcm.getColumn(6).setCellRenderer(render);
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

    private JTextField Gepszam, Muhelyszam, muhelyFilter;
    private JComboBox Kosp,Terulet;
    private JTextArea Megnevezes;
    private JCheckBox Modosit;
    private JScrollPane mScroll, tScroll;
    private JPanel inputPane, tblPane;
    private JMenuItem kilepMenu;
    private JMenu mainMenu;
    private JMenuItem mentesMenu, modositMenu, torolMenu;
    private JMenuBar menuBar;
    private JTable muhelyTabla;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 7;
    private String oldMuhelyszam;
}
