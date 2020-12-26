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
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 *
 * @author Forcek László
 */
public class WorkPlaces extends JExtendedFrame{
    
    public WorkPlaces()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/workplace.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 1.5),
                                       (int)(defaultTool.getScreenSize().getHeight() / 1.5)));
        setTitle("Munkahelyek");
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

        dszamFilter = new JTextField(10);
        Munkahely = new JComboBox();
        dtorzsTabla = new JTable();
        munkahelyTabla = new JTable();
        mScroll = new javax.swing.JScrollPane();
        dScroll = new JScrollPane();
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        hozzaadMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();

        munkahelyTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        munkahelyTabla.getTableHeader().setResizingAllowed(false);
        munkahelyTabla.getTableHeader().setReorderingAllowed(false);
        mScroll.setViewportView(munkahelyTabla);

        dtorzsTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        dtorzsTabla.getTableHeader().setResizingAllowed(false);
        dtorzsTabla.getTableHeader().setReorderingAllowed(false);
        dScroll.setViewportView(dtorzsTabla);

        dszamFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dszamFilterKeyReleased(evt);
            }
        });

        mainMenu.setText("Menü");

        hozzaadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        hozzaadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        hozzaadMenu.setText("Hozzáad");
        hozzaadMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hozzaadMenuActionPerformed(evt);
            }
        });
        mainMenu.add(hozzaadMenu);

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

        Dolgozotolt();
        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp ORDER BY megnevezes");
        Tablatolt();
        Munkahely.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tablatolt();
            }
        });
        Combokeres(Munkahely,"14");
        
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Dolgozó száma");
        JLabel Label2 = new JLabel("Munkahely");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        dPane = new JPanel(new BorderLayout());
        dPane.setBorder(new EtchedBorder());
        mPane = new JPanel(new BorderLayout());
        mPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(dszamFilter);
        inputPane.add(dszamFilter);

        inputPane.add(Label2);
        Label2.setLabelFor(Munkahely);
        inputPane.add(Munkahely);

        dPane.add(dScroll);
        mPane.add(mScroll);

        SpringUtilities.makeCompactGrid(inputPane,
                                        2, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        contentPane.add(inputPane);
        contentPane.add(dPane);
        contentPane.add(mPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);

        //dPane
        layout.putConstraint(SpringLayout.WEST, dPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, dPane,
                             1,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.SOUTH, dPane,
                             1,
                             SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, dPane,
                             0,
                             SpringLayout.EAST, inputPane);

        //mPane
        layout.putConstraint(SpringLayout.WEST, mPane,
                             1,
                             SpringLayout.EAST, inputPane);
        layout.putConstraint(SpringLayout.NORTH, mPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, mPane,
                             1,
                             SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, mPane,
                             0,
                             SpringLayout.EAST, contentPane);

        pack();
    }
    
    private void Dolgozotolt()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Dolgozószám");
        tableHeaders.add("Név");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            int hosz = dszamFilter.getText().length();
            sqlolvas("SELECT azon,dszam,nev FROM dtorzs WHERE dtorzs.elment = 0 " +
                     "AND LEFT(dtorzs.dszam,'" + hosz + "') = '" + dszamFilter.getText() + "' " +
                     "ORDER BY nev ");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                tableData.add(oneRow);
            }
            conn.close();
            dtorzsTabla.setModel(new NoEditModel(tableData, tableHeaders));
            dtorzsTabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            dtorzsTabla.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            dtorzsTabla.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
            dtorzsTabla.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
            dtorzsTabla.setAutoCreateRowSorter(true);
            dtorzsTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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

    private void Tablatolt()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Dolgozószám");
        tableHeaders.add("Név");
        tableHeaders.add("Munkahely");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT dolgozok.azon,dolgozok.dszam,dtorzs.nev,kosp.megnevezes " +
                     "FROM dtorzs INNER JOIN dolgozok ON (dtorzs.dszam = dolgozok.dszam) " +
                     "INNER JOIN kosp ON (dolgozok.munkahely = kosp.azon) " +
                     "WHERE kosp.azon = '" + ComboID(Munkahely) + "'");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                oneRow.add(rSet.getString(4));
                tableData.add(oneRow);
            }
            conn.close();
            munkahelyTabla.setModel(new NoEditModel(tableData, tableHeaders));
            munkahelyTabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            munkahelyTabla.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            munkahelyTabla.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
            munkahelyTabla.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
            munkahelyTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
            munkahelyTabla.setAutoCreateRowSorter(true);
            TableColumnModel tcm = munkahelyTabla.getColumnModel();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
            FormatRenderer render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.CENTER);
            tcm.getColumn(1).setCellRenderer(render);
            tcm.getColumn(3).setCellRenderer(render);
            render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.LEFT);
            tcm.getColumn(2).setCellRenderer(render);
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
    
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {                                          
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }                                         

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }                                         

    private void hozzaadMenuActionPerformed(java.awt.event.ActionEvent evt) {                                            
        Configuration cfg = new Configuration();
        if (dtorzsTabla.getSelectedRow() != -1)
        {
            String message = "";
            if (dtorzsTabla.getSelectedRows().length == 1)
            {
                message = "Hozzáadja a dolgozót a megadott munkahelyre?";
            }
            else
            {
                message = "Hozzáadja a kijelölt dolgozókat a megadott munkahelyre?";
            }
            int valaszt = JOptionPane.showConfirmDialog(null,message, "felvitel jóváhagyása",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                Vizsgal();
                if (letezik && dtorzsTabla.getSelectedRows().length == 1)
                {
                    JOptionPane.showMessageDialog(null,"A dolgozó már hozzá van rendelve munkahelyhez!");
                }
                else
                {
                    if (letezik && dtorzsTabla.getSelectedRows().length > 1)
                    {
                        JOptionPane.showMessageDialog(null,"Egy vagy több dolgozó már hozzá van rendelve munkahelyhez!");
                    }
                    else
                    {
                        try
                        {
                            conn = DriverManager.getConnection(cfg.getsqlConn());
                            conn.setAutoCommit(false);
                            stmt = conn.createStatement();
                            int i = 0;
                            while (i < dtorzsTabla.getSelectedRows().length)
                            {
                                sqlupdate("INSERT INTO dolgozok (dszam,munkahely) VALUES ('" +
                                         dtorzsTabla.getValueAt(dtorzsTabla.getSelectedRows()[i],1).toString() + "','" +
                                         ComboID(Munkahely) + "')");
                                i++;
                            }
                            conn.commit();
                            conn.close();
                            Tablatolt();
                        }
                        catch (SQLException ex)
                        {
                            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
                        }
                        finally
                        {
                            try{conn.close();}
                            catch (SQLException ex){}
                        }
                    }
                }
            }
        }
    }                                           

    private void Vizsgal()
    {
        Configuration cfg = new Configuration();
        letezik = false;
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            int i = 0;
            while (i < dtorzsTabla.getSelectedRows().length)
            {
                sqlolvas("SELECT dszam FROM dolgozok WHERE " +
                         "dszam = '" +
                         dtorzsTabla.getValueAt(dtorzsTabla.getSelectedRows()[i],1).toString() + "'");
                if (rSet.next())
                {
                    letezik = true;
                    break;
                }
                i++;
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
        finally
        {
            try{conn.close();}
            catch (SQLException ex){}
        }
    }

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (munkahelyTabla.getRowCount() != 0)
        {
            Configuration cfg = new Configuration();
            String message = "Törli az összes tételt?";
            int valaszt = JOptionPane.showConfirmDialog(null,message, "felvitel jóváhagyása",JOptionPane.YES_NO_CANCEL_OPTION);
            if (valaszt == 0)
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                if (cfg.getchangeEnable())
                {
                    sqlupdate("DELETE dolgozok.* FROM dolgozok WHERE munkahely = '" +
                              ComboID(Munkahely) + "'");
                }
                else
                {
                    if (valaszt == 1)
                    {
                        for (int i = 0;i < munkahelyTabla.getSelectedRows().length;i++)
                        {
                            sqlupdate("DELETE dolgozok.* FROM dolgozok WHERE (azon = '" +
                                      munkahelyTabla.getValueAt(munkahelyTabla.getSelectedRows()[i], 0).toString() + "');");
                        }
                    }
                }
                conn.commit();
                conn.close();
                Tablatolt();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
            }
            finally
            {
                try{conn.close();}
                catch (SQLException ex){}
            }
        }
    }                                         

    private void dszamFilterKeyReleased(java.awt.event.KeyEvent evt) {                                        
        Dolgozotolt();
    }                                       

    private JComboBox Munkahely;
    private JTextField dszamFilter;
    private JTable dtorzsTabla, munkahelyTabla;
    private JMenuItem hozzaadMenu, kilepMenu, torolMenu;
    private JScrollPane dScroll, mScroll;
    private JPanel inputPane, dPane, mPane;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 5;
    private Vector<ListItem> mLista = new Vector<ListItem>();
    private boolean letezik;

}
