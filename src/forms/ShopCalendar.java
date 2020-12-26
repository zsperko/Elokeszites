/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
import classes.SpringUtilities;
import classes.JExtendedFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class ShopCalendar extends JExtendedFrame implements TableModelListener{

    public ShopCalendar()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/calendar.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 2),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2)));
        setTitle("Üzemi naptárak");
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

        Machines = new JComboBox();
        Shift = new JComboBox();
        Datum = new JCalendarCombo();
        UntilDate = new JCalendarCombo();
        mainMenu = new JMenu();
        szamolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();
        ctScroll = new JScrollPane();

        mainMenu.setText("Menü");

        szamolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        szamolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/calculator.png"))); // NOI18N
        szamolMenu.setText("Üzeminaptár kiszámol/újraszámol");
        szamolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szamolMenuActionPerformed(evt);
            }
        });
        mainMenu.add(szamolMenu);
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

        Comboboxtolt(Machines,"SELECT azon,szam FROM muhelyek ORDER BY szam");
        Comboboxtolt(Shift,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");

        Machines.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tablafeltolt();
            }
        });

        Tablafeltolt();
        GepekFeltolt();

        listener = new MyDateListener();
        Datum.addDateListener(listener);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Munkahely");
        JLabel Label2 = new JLabel("Dátum");
        JLabel Label3 = new JLabel("Végdátum");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Machines);
        inputPane.add(Machines);

        inputPane.add(Label2);
        Label2.setLabelFor(Datum);
        inputPane.add(Datum);

        inputPane.add(Label3);
        Label3.setLabelFor(UntilDate);
        inputPane.add(UntilDate);

        SpringUtilities.makeCompactGrid(inputPane,
                                3, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        tablePane = new JPanel(new BorderLayout());
        tablePane.setBorder(new EtchedBorder());
        ctScroll.setViewportView(CalendarTable);
        tablePane.add(ctScroll);

        contentPane.add(inputPane);
        contentPane.add(tablePane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             0,
                             SpringLayout.NORTH, contentPane);

        //tablePane
        layout.putConstraint(SpringLayout.WEST, tablePane,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, tablePane,
                             0,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.EAST, tablePane,
                             0,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, tablePane,
                             0,
                             SpringLayout.SOUTH, contentPane);
        pack();

    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void szamolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        int valaszt = 0;

        if (CalendarTable.getRowCount() > 0)
        {
            valaszt = JOptionPane.showConfirmDialog(null,"Már létezik üzemi naptár!Újraszámolja?","Újraszámolás", 
                                                   JOptionPane.YES_NO_OPTION);
        }
        if (valaszt == 0)
        {
            Calendar cal = Calendar.getInstance();
            Date untilDate= UntilDate.getDate();
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                sqlupdate("DELETE uzeminaptar.* FROM uzeminaptar WHERE datum > '"
                        + cfg.sqlDate(Datum.getDate()) + "' AND gepazon = '"
                        + ComboID(Machines) + "'");
                int termeles = 0;
                for (int j = 0;j < Gepek.getRowCount();j++)
                {
                    System.out.println(Gepek.getValueAt(j, 0).toString());
                    Date date = Datum.getDate();
                    while (date.before(untilDate))
                    {
                        cal.setTime(date);
                        cal.add(Calendar.DATE,1);
                        termeles = 1;
                        if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7)
                            termeles = 0;
                        date = cal.getTime();
                        for (int i = 0;i < Shift.getItemCount();i++)
                        {
                            sqlupdate("INSERT INTO uzeminaptar (gepazon,datum,muszak,termeles) VALUES ('"
                                    + Gepek.getValueAt(j, 0).toString() + "','"
                                    + cfg.sqlDate(date) + "','"
                                    + ((ListItem)Shift.getItemAt(i)).getValue() + "','" + termeles + "')");
                        }
                    }
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
            Tablafeltolt();
        }
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void Tablafeltolt()
    {
        CalendarTable = null;
        CalendarTable = new JTable();
        ctScroll.setViewportView(CalendarTable);
        String[] tableHeaders = {"Azon","Dátum","Műszak","Termelés"};
        Object[][] tableData = new Object[0][0];
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sqlCmd = "SELECT uzeminaptar.azon,uzeminaptar.datum,muszakok.megnevezes,"
                          + "uzeminaptar.termeles FROM uzeminaptar "
                          + "INNER JOIN muszakok ON (uzeminaptar.muszak = muszakok.azon) "
                          + "WHERE uzeminaptar.gepazon = '"
                          + ComboID(Machines) + "' AND "
                          + "uzeminaptar.datum >= '"
                          + cfg.sqlDate(Datum.getDate())
                          + "' ORDER BY uzeminaptar.datum,uzeminaptar.muszak";
            sqlolvas(sqlCmd);
            rSet.last();
            int count = rSet.getRow();
            rSet.beforeFirst();
            tableData = new Object[count][tableHeaders.length];
            while(rSet.next())
            {
                tableData[rSet.getRow()-1][0] = rSet.getString(1);
                tableData[rSet.getRow()-1][1] = rSet.getString(2);
                tableData[rSet.getRow()-1][2] = rSet.getString(3);
                boolean termeles = false;
                if (rSet.getInt(4) == 1) termeles = true;
                tableData[rSet.getRow()-1][3] = termeles;
            }
            conn.close();

            CalendarTable.setModel(new DefaultTableModel(tableData, tableHeaders)
            {
                Class[] types = new Class [] {
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Boolean.class
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
            CalendarTable.getModel().addTableModelListener(this);
            CalendarTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            CalendarTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            CalendarTable.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            CalendarTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
            CalendarTable.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
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

    private void GepekFeltolt()
    {
        Gepek = null;
        Gepek = new JTable();
        String[] tableHeaders = {"Azon"};
        Object[][] tableData = new Object[0][0];
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sqlCmd = "SELECT muhelyek.azon FROM muhelyek";
            sqlolvas(sqlCmd);
            rSet.last();
            int count = rSet.getRow();
            rSet.beforeFirst();
            tableData = new Object[count][tableHeaders.length];
            while(rSet.next())
            {
                tableData[rSet.getRow()-1][0] = rSet.getString(1);
            }
            conn.close();

            Gepek.setModel(new DefaultTableModel(tableData, tableHeaders)
            {
                Class[] types = new Class [] {
                    java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false
                };
            });
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

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        //String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        boolean value = Boolean.parseBoolean(data.toString());
        int valueInInt = (value == true ? 1 : 0);
        UpdateChanges(CalendarTable.getValueAt(row, 0).toString(),valueInInt);
    }

    private void UpdateChanges(String index, int value)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlupdate("UPDATE uzeminaptar SET termeles = '" + value + "' " +
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

    private void sqlolvas(String sqlcmd)
    {
        rSet = null;
        try
        {
            rSet = stmt.executeQuery(sqlcmd);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
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

    class MyDateListener implements DateListener
    {

        public void dateChanged(DateEvent e)
        {
            Calendar c = e.getSelectedDate();
            if (c != null) {
                Tablafeltolt();
            }
            else
            {
                //System.out.println("No time selected.");
            }
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 21;
    private JTable CalendarTable, Gepek;
    private JScrollPane ctScroll;
    private JPanel inputPane, tablePane;
    private JCalendarCombo Datum, UntilDate;
    private JComboBox Shift, Machines;
    private JMenu mainMenu;
    private JMenuItem szamolMenu, kilepMenu;
    private JMenuBar menuBar;
    private MyDateListener listener;
}
