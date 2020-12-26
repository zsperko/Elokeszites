/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
import classes.NoEditModel;
import classes.SpringUtilities;
import classes.FormatRenderer;
import classes.AutoComplete;
import classes.JExtendedFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;
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
public class Inventory extends JExtendedFrame{

    public Inventory()
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
        setTitle("Leltárűrlap");
        setResizable(true);
        setMaximizable(true);
        setTitle("Leltárűrlap");
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
        Raktarkod = new JComboBox();
        Mennyiseg = new JTextField(20);
        Leltarlista = new JTable();
        Modosit = new JCheckBox();
        Year = new JComboBox();
        Month = new JComboBox();
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        hozzaadMenu = new JMenuItem();
        modositMenu = new JMenuItem();
        torlesMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        ScrollPane = new JScrollPane();

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

        Raktarkod.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RaktarkodItemStateChanged(evt);
            }
        });

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

        Modosit.setText("Módosítás");
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

        Comboboxtolt(Cikkszam,"SELECT azon,cikkszam FROM cikktorzs ORDER BY cikkszam");
        JTextComponent editor = (JTextComponent)Cikkszam.getEditor().getEditorComponent();
        editor.setDocument(new AutoComplete(Cikkszam));
        Comboboxtolt(Raktarkod,"SELECT azon,raktarnev FROM raktarak ORDER BY raktarnev");
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

        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0;i < 25;i++)
        {
            Year.addItem(year -10 + i);
        }
        for (int i = 1;i < 13;i++)
        {
            Month.addItem(i);
        }
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        int i = 0;
        while (Year.getItemAt(i).toString().compareTo(Integer.toString(year)) != 0)
        {
            i++;
        }
        Year.setSelectedIndex(i);
        i = 0;
        while (Month.getItemAt(i).toString().compareTo(Integer.toString(month)) != 0)
        {
            i++;
        }
        Month.setSelectedIndex(i);

        Year.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leltarnaplo();
            }
        });
        Month.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leltarnaplo();
            }
        });

        Leltarnaplo();
        
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Év");
        JLabel Label2 = new JLabel("Hónap");
        JLabel Label3 = new JLabel("Raktár");
        JLabel Label4 = new JLabel("Cikkszám");
        JLabel Label5 = new JLabel("Mennyiség");
        JLabel Label6 = new JLabel("");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Year);
        inputPane.add(Year);

        inputPane.add(Label2);
        Label2.setLabelFor(Month);
        inputPane.add(Month);

        inputPane.add(Label3);
        Label3.setLabelFor(Raktarkod);
        inputPane.add(Raktarkod);

        inputPane.add(Label4);
        Label4.setLabelFor(Cikkszam);
        inputPane.add(Cikkszam);
        
        inputPane.add(Label5);
        Label5.setLabelFor(Mennyiseg);
        inputPane.add(Mennyiseg);

        inputPane.add(Label6);
        Label6.setLabelFor(Mennyiseg);
        inputPane.add(Modosit);
        
        SpringUtilities.makeCompactGrid(inputPane,
                                        6, 2, //rows, cols
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
    
    private void MennyisegFocusGained(java.awt.event.FocusEvent evt) {                                      
        if (Mennyiseg.getText().compareTo("") != 0)
        {
            oldMennyi = Mennyiseg.getText();
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
        dispose();
    }                                         

    private void hozzaadMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Hozzaad();
    }                                           

    private void Hozzaad()
    {
        if (Mennyiseg.getText().compareTo("") != 0 &&
            Mennyiseg.getText().compareTo("0") != 0 &&
            !Modosit.isSelected())
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                sqlupdate("INSERT INTO leltarnaplo(cikkszam,mennyiseg,raktarkod,ev,honap) VALUES('" +
                          Cikkszam.getSelectedItem().toString() + "','" +
                          Mennyiseg.getText() + "','" +
                          ComboID(Raktarkod) + "','" +
                          Year.getSelectedItem().toString() + "','" +
                          Month.getSelectedItem().toString() + "')");
                Mennyiseg.setText("0");
                conn.commit();
                conn.close();
                Leltarnaplo();
                Cikkszam.grabFocus();
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

    private void modositMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (Modosit.isSelected() && Leltarlista.getSelectedRow() != -1 &&
            Mennyiseg.getText().compareTo("") != 0 &&
            Mennyiseg.getText().compareTo("0") != 0)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                sqlupdate("UPDATE leltarnaplo SET mennyiseg = '" +
                          Mennyiseg.getText() + "',raktarkod = '" +
                          ComboID(Raktarkod) + "' " +
                          "WHERE leltarnaplo.azon = '" +
                          Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 0).toString() + "'");
                conn.commit();
                conn.close();
                Mennyiseg.setText("0");
                Leltarnaplo();
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

    private void Raktarbeallit()
    {
        int i = 0;
        while (i < Raktarkod.getItemCount() &&
               ((ListItem)Raktarkod.getItemAt(i)).toString().compareTo(
               Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 3).toString()) != 0)
        {
            i++;
        }
        if (i <= Raktarkod.getItemCount() - 1) Raktarkod.setSelectedIndex(i);
    }

    private void ModositItemStateChanged(java.awt.event.ItemEvent evt) {                                         
        if (Modosit.isSelected())
        {
            if (Leltarlista.getSelectedRow() != -1)
            {
                Cikkszambeallit();
                Mennyiseg.setText(Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 2).toString());
                Raktarbeallit();
            }
            Cikkszam.setEnabled(false);
        }
        else
        {
            Cikkszam.setEnabled(true);
        }
    }                                        

    private void LeltarlistaMouseReleased(java.awt.event.MouseEvent evt) {                                          
        if (Modosit.isSelected() && Leltarlista.getSelectedRow() != -1)
        {
            Cikkszam.setEnabled(true);
            Cikkszambeallit();
            Cikkszam.setEnabled(false);
            Mennyiseg.setText(Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 2).toString());
            Raktarbeallit();
        }
    }                                         

    private void torlesMenuActionPerformed(java.awt.event.ActionEvent evt) {                                           
        if (!Modosit.isSelected() &&
            Leltarlista.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                sqlupdate("DELETE leltarnaplo.* FROM leltarnaplo " +
                          "WHERE leltarnaplo.azon = '" +
                          Leltarlista.getValueAt(Leltarlista.getSelectedRow(), 0).toString() + "'");
                conn.commit();
                conn.close();
                Leltarnaplo();
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

    private void RaktarkodItemStateChanged(java.awt.event.ItemEvent evt) {                                           
        Leltarnaplo();
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
        tableHeaders.add("Azon");
        tableHeaders.add("Cikkszám");
        tableHeaders.add("Mennyiség");
        tableHeaders.add("Raktár");
        rSet = null;
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT leltarnaplo.azon,leltarnaplo.cikkszam,leltarnaplo.mennyiseg," +
                     "raktarak.raktarnev FROM leltarnaplo " +
                     "INNER JOIN raktarak ON (leltarnaplo.raktarkod = raktarak.azon) " +
                     "WHERE raktarak.azon = '" +
                     ComboID(Raktarkod) + "' AND " +
                     "leltarnaplo.ev = '" + Year.getSelectedItem().toString() + "' AND " +
                     "leltarnaplo.honap = '" + Month.getSelectedItem().toString() + "'");
            if (rSet.next())
            {
                do
                {
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(rSet.getString(1));
                    oneRow.add(rSet.getString(2));
                    oneRow.add(rSet.getString(3));
                    oneRow.add(rSet.getString(4));
                    tableData.add(oneRow);
                } while (rSet.next());
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Kapcsolódási hiba: " + ex.getMessage());
        }
        Leltarlista.setModel(new NoEditModel(tableData, tableHeaders));
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
        TableColumnModel tcm = Leltarlista.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.RIGHT);
        tcm.getColumn(2).setCellRenderer(render);
        Leltarlista.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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

    private JComboBox Cikkszam, Month, Raktarkod, Year;
    private JPanel inputPane, tblPane;
    private JTable Leltarlista;
    private JTextField Mennyiseg;
    private JCheckBox Modosit;
    private JMenuItem hozzaadMenu, modositMenu, torlesMenu, kilepMenu;
    private JScrollPane ScrollPane;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private String oldMennyi;
    private int formID = 9;
}
