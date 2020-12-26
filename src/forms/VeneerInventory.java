/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.NoEditModel;
import classes.SpringUtilities;
import classes.FormatRenderer;
import classes.AutoComplete;
import classes.JExtendedFrame;
import dialogs.AnimatedDialog;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Forcek László
 */
public class VeneerInventory extends JExtendedFrame{

    public VeneerInventory()
    {
        initComponents();
        Cikkszam.grabFocus();
    }
    
    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/invonhand.png"));
        setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setTitle("Furnér leltárűrlap");
        setResizable(true);
        setMaximizable(true);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 1.5),
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

        Cikkszam = new JComboBox();
        Mennyiseg = new JTextField(20);
        Rakatszam = new JTextField(20);
        Leltarlista = new JTable();
        Modosit = new JCheckBox("Módosítás");
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        hozzaadMenu = new JMenuItem();
        modositMenu = new JMenuItem();
        xlsMenu = new JMenuItem();
        torlesMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        ScrollPane = new JScrollPane();

        Cikkszam.setEditable(true);

        Rakatszam.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                RakatszamFocusLost(evt);
            }
        });

        Rakatszam.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_ENTER:
                        Cikkszam.grabFocus();
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

        Leltarlista.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        Leltarlista.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Leltarlista.getTableHeader().setResizingAllowed(false);
        Leltarlista.getTableHeader().setReorderingAllowed(false);
        Leltarlista.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                LeltarlistaMouseReleased(evt);
            }
        });
        ScrollPane.setViewportView(Leltarlista);

        Modosit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ModositItemStateChanged(evt);
            }
        });

        mainMenu.setText("Menü");

        hozzaadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        hozzaadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        hozzaadMenu.setText("Hozzáad");
        hozzaadMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hozzaadMenuActionPerformed(evt);
            }
        });
        mainMenu.add(hozzaadMenu);

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

        torlesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        torlesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png"))); // NOI18N
        torlesMenu.setText("Törlés");
        torlesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torlesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(torlesMenu);
        mainMenu.add(new JSeparator());

        xlsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        xlsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png"))); // NOI18N
        xlsMenu.setText("Excel lista mentés");
        xlsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xlsMenuActionPerformed(evt);
            }
        });
        mainMenu.add(xlsMenu);
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

        Leltarnaplo();
        
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Cikkszám");
        JLabel Label2 = new JLabel("Mennyiség");
        JLabel Label3 = new JLabel("Rakatszám");
        JLabel Label4 = new JLabel("");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());

        inputPane.add(Label3);
        Label3.setLabelFor(Rakatszam);
        inputPane.add(Rakatszam);

        inputPane.add(Label1);
        Label1.setLabelFor(Cikkszam);
        inputPane.add(Cikkszam);
        
        inputPane.add(Label2);
        Label2.setLabelFor(Mennyiseg);
        inputPane.add(Mennyiseg);

        inputPane.add(Label4);
        Label4.setLabelFor(Modosit);
        inputPane.add(Modosit);
        
        SpringUtilities.makeCompactGrid(inputPane,
                                        4, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        contentPane.add(inputPane);
        tblPane.add(ScrollPane);
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
                             SpringLayout.EAST, inputPane);
        layout.putConstraint(SpringLayout.NORTH, tblPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             1,
                             SpringLayout.SOUTH, tblPane);
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             1,
                             SpringLayout.EAST, contentPane);

        pack();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void RakatszamFocusLost(java.awt.event.FocusEvent evt) {
        if (!Modosit.isSelected() && Rakatszam.getText().compareTo("") != 0)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                sqlolvas("SELECT cikkszam,mennyiseg FROM furnerraktar WHERE " +
                         "rakatszam = '" + Rakatszam.getText() + "'");
                if (rSet.next())
                {
                    do
                    {
                        Cikkszam.setSelectedItem(rSet.getString(1));
                        Mennyiseg.setText(rSet.getString(2));
                    } while (rSet.next());
                }
                conn.close();
            }
            catch (SQLException ex)
            {
                System.out.println("Kapcsolódási hiba: " + ex.getMessage());
            }
            finally
            {
                try
                {conn.close();}
                catch (SQLException ex){}
            }
        }
    }

    private void MennyisegFocusGained(java.awt.event.FocusEvent evt) {                                      
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

    private void hozzaadMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Hozzaad();
    }                                           

    private void Hozzaad()
    {
        if (Mennyiseg.getText().compareTo("") != 0 &&
            Mennyiseg.getText().compareTo("0") != 0 &&
            Rakatszam.getText().compareTo("") != 0 &&
            Cikkszam.getSelectedIndex() != -1 &&
            !Modosit.isSelected())
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                sqlolvas("SELECT rakatszam FROM furnerleltar WHERE rakatszam = '" +
                         Rakatszam.getText() + "'");
                if (rSet.next())
                {
                    JOptionPane.showMessageDialog(null,"A megadott rakatszám már létezik!");
                }
                else
                {
                    sqlupdate("INSERT INTO furnerleltar (rakatszam,cikkszam,mennyiseg) VALUES ('" +
                              Rakatszam.getText() + "','" + Cikkszam.getSelectedItem().toString() + "','" +
                              Mennyiseg.getText() + "')");
                }
                Mennyiseg.setText("0");
                Cikkszam.setSelectedIndex(0);
                conn.commit();
                conn.close();
                Leltarnaplo();
                Rakatszam.setText("");
                Rakatszam.grabFocus();
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

    private void modositMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (Modosit.isSelected() && Leltarlista.getSelectedRow() != -1 &&
            Mennyiseg.getText().compareTo("") != 0 &&
            Mennyiseg.getText().compareTo("0") != 0 &&
            Rakatszam.getText().compareTo("") != 0 &&
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
                    sqlupdate("UPDATE furnerleltar SET cikkszam = '" +
                              Cikkszam.getSelectedItem().toString() + "'," +
                              "mennyiseg = '" + Mennyiseg.getText() + "' " +
                              "WHERE rakatszam = '" +
                              Rakatszam.getText() + "'");
                    conn.commit();
                    conn.close();
                    Mennyiseg.setText("0");
                    Modosit.setSelected(false);
                    Rakatszam.setEnabled(true);
                    Cikkszam.setSelectedIndex(0);
                    Rakatszam.setText("");
                    Rakatszam.grabFocus();
                    Leltarnaplo();
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

    private void xlsMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (Leltarlista.getRowCount() > 0)
        {
            Configuration cfg = new Configuration();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("c://"));
            fileChooser.setDialogTitle("Válassza ki a mentési könyvtárat");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            int status = fileChooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION)
            {
                cfg.setthreadFinished(false);
                ArrayList<String> MessageList = new ArrayList<>();
                MessageList.add("Frissítés folyamatban...");
                AnimatedDialog form = new AnimatedDialog(MessageList);
                Calendar cal = Calendar.getInstance();
                int Year = cal.get(Calendar.YEAR);
                int Month = cal.get(Calendar.MONTH) + 1;
                int Day = cal.get(Calendar.DATE);
                SaveData save = new SaveData(Integer.toString(Year),Integer.toString(Month),
                                             Integer.toString(Day),fileChooser.getSelectedFile().getAbsolutePath());
                Thread thread;
                thread = new Thread(save);
                thread.start();
                form.setVisible(true);
            }
        }
    }

    private void Cikkszambeallit()
    {
        int i = 0;
        while (i < Cikkszam.getItemCount() &&
               Cikkszam.getItemAt(i).toString().compareTo(
               Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 1).toString()) != 0)
        {
            i++;
        }
        if (i <= Cikkszam.getItemCount() - 1) Cikkszam.setSelectedIndex(i);
    }

    private void ModositItemStateChanged(java.awt.event.ItemEvent evt) {                                         
        if (Modosit.isSelected())
        {
            if (Leltarlista.getSelectedRow() != -1)
            {
                Rakatszam.setText(Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 0).toString());
                Cikkszambeallit();
                Mennyiseg.setText(Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 2).toString());
            }
            Rakatszam.setEnabled(false);
        }
        else
        {
            Rakatszam.setText("");
            Rakatszam.setEnabled(true);
            Mennyiseg.setText("0");
            Cikkszam.setSelectedIndex(0);
        }
    }                                        

    private void LeltarlistaMouseReleased(java.awt.event.MouseEvent evt) {                                          
        if (Modosit.isSelected() && Leltarlista.getSelectedRow() != -1)
        {
            Rakatszam.setText(Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 0).toString());
            Rakatszam.setEnabled(false);
            Cikkszambeallit();
            Mennyiseg.setText(Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 2).toString());
        }
    }                                         

    private void torlesMenuActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if (!Modosit.isSelected() &&
            Leltarlista.getSelectedRow() != -1)
        {
            int valaszt = JOptionPane.showConfirmDialog(null,"Biztosan törli a kijelölt rakatszámot?",
                                                             "Törlés megerősítése",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                Configuration cfg = new Configuration();
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    sqlupdate("DELETE furnerleltar.* FROM furnerleltar " +
                              "WHERE furnerleltar.rakatszam = '" +
                              Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 0).toString() + "'");
                    conn.commit();
                    conn.close();
                    Leltarnaplo();
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
                    Hozzaad();
                    break;
            }
        }
        catch (Exception ex){}

    }                                     

    private void Leltarnaplo()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Rakatszám");
        tableHeaders.add("Cikkszám");
        tableHeaders.add("Mennyiség");
        rSet = null;
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT furnerleltar.rakatszam,furnerleltar.cikkszam," +
                     "furnerleltar.mennyiseg FROM furnerleltar");
            if (rSet.next())
            {
                do
                {
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(rSet.getString(1));
                    oneRow.add(rSet.getString(2));
                    oneRow.add(rSet.getString(3));
                    tableData.add(oneRow);
                } while (rSet.next());
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
        Leltarlista.setModel(new NoEditModel(tableData, tableHeaders));
        TableColumnModel tcm = Leltarlista.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.RIGHT);
        tcm.getColumn(2).setCellRenderer(render);
        Leltarlista.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }
    
    private void comboboxtolt(JComboBox combo,String sqlcmd)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            combo.setModel (new DefaultComboBoxModel ());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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
    private JPanel inputPane, tblPane;
    private JTable Leltarlista;
    private JTextField Mennyiseg, Rakatszam;
    private JCheckBox Modosit;
    private JMenuItem hozzaadMenu, modositMenu, torlesMenu, xlsMenu, kilepMenu;
    private JScrollPane ScrollPane;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private String oldMennyi;
    private int formID = 15;
}
