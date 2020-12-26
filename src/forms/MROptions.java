/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
import classes.JExtendedFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Forcek László
 */
public class MROptions extends JExtendedFrame implements TableModelListener{

    public MROptions()
    {
        initComponents();
    }

    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/options.png"));
        setFrameIcon(icon);
        setResizable(true);
        setMaximizable(true);
        setClosable(true);
        setIconifiable(true);
        setTitle("Anyagigény beállítások");
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 2),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2)));
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

        kilepMenu = new JMenuItem();
        mainMenu = new JMenu();
        menuBar = new JMenuBar();
        Munkahely = new JComboBox();
        Options = new JTable();
        oScroll = new JScrollPane();

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/close.png")));
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        
        mainMenu.setText("Menü");
        mainMenu.add(kilepMenu);
        menuBar.add(mainMenu);
        setJMenuBar(menuBar);

        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp ORDER BY megnevezes");
        Optionstolt();
        Munkahely.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Optionstolt();
            }
        });

        Combokeres(Munkahely,14);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Munkahely");

        inputPane = new JPanel(new FlowLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Munkahely);
        inputPane.add(Munkahely);

        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());
        oScroll.setViewportView(Options);
        tblPane.add(oScroll);

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
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             1,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);

        pack();
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

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        //String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        int value = (Boolean.parseBoolean(data.toString()) == true ? 1 : 0);
        UpdateChanges(model.getValueAt(row, 0).toString(), Integer.toString(value));
    }

    private void Optionstolt()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Munkahely");
        tableHeaders.add("Anyagtípus");
        tableHeaders.add("Anyagigény");
        Configuration cfg = new Configuration();
        if (Munkahely.getItemCount() > 0)
        {
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                sqlolvas("SELECT anyagigenybeallitas.azon,kosp.megnevezes," +
                         "anyagtipus.megnevezes, anyagigenybeallitas.anyagigeny " +
                         "FROM anyagigenybeallitas " +
                         "INNER JOIN anyagtipus ON (anyagigenybeallitas.anyagtipus = anyagtipus.azon) " +
                         "INNER JOIN kosp ON (anyagigenybeallitas.munkahely = kosp.azon) " +
                         "WHERE kosp.azon = '" +
                         ComboID(Munkahely) + "'");
                while(rSet.next())
                {
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(rSet.getString(1));
                    oneRow.add(rSet.getString(2));
                    oneRow.add(rSet.getString(3));
                    int value = rSet.getInt(4);
                    if (value == 0) oneRow.add(new Boolean(false));
                    else oneRow.add(new Boolean(true));
                    tableData.add(oneRow);
                }
                conn.close();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
            finally
            {
                try
                {conn.close();}
                catch (SQLException ex){}
            }
        }
        Options.setModel(new DefaultTableModel(tableData, tableHeaders)
        {
            Class[] types = new Class [] {
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Boolean.class,
            };
            
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            
            @Override
            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }
        });
        Options.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Options.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Options.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Options.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
        Options.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        Options.getModel().addTableModelListener(this);
    }
    
    private void UpdateChanges(String index, String value)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlupdate("UPDATE anyagigenybeallitas SET anyagigeny = '" + value + "' " +
                      "WHERE azon = '" + index + "'");
            conn.commit();
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
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
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
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

    private void Combokeres(JComboBox combo, int ertek)
    {
        int i = 0;
        while (combo.getItemCount() - 1 > i &&
               ((ListItem)combo.getItemAt(i)).getValue() != ertek)
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
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
        }
    }

    private JMenuItem kilepMenu;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private JComboBox Munkahely;
    private JPanel inputPane, tblPane;
    private JTable Options;
    private JScrollPane oScroll;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 12;

}
