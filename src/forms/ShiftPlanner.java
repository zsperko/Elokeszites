/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Forcek László
 */
public class ShiftPlanner extends JExtendedFrame{

    public ShiftPlanner()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/timerec.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() - 200),
                                       (int)(defaultTool.getScreenSize().getHeight() - 200)));
        setformID(formID);
        setTitle("Műszakbeosztások");
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

        Week = new JComboBox();
        Shift = new JComboBox();
        Workplace = new JComboBox();
        Workers = new JTable();
        Shifts  = new JTable();
        wScroll = new JScrollPane();
        sScroll = new JScrollPane();
        startDate = new JTextField(20);
        endDate = new JTextField(20);
        WeekCheck = new JCheckBox[7];
        for (int i = 0;i < 7;i++) WeekCheck[i] = new JCheckBox(WeekNames[i]);

        mainMenu = new JMenu();
        addMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();

        startDate.setEditable(false);
        endDate.setEditable(false);
        startDate.setHorizontalAlignment(JTextField.RIGHT);
        endDate.setHorizontalAlignment(JTextField.RIGHT);
        Dates = new String[7];

        Locale.setDefault(Locale.getDefault());
        Date curr = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(curr);
        int currWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int maxWeek = cal.getActualMaximum(Calendar.WEEK_OF_YEAR);
        for (int i = 1;i < maxWeek+1;i++)
        {
            Week.addItem(Integer.toString(i) + ". hét");
        }
        for (int i = 0;i < 5;i++) WeekCheck[i].setSelected(true);

        Week.setSelectedIndex(currWeek - 2);
        Datumbeallit(Week.getSelectedIndex() + 2);

        mainMenu.setText("Menü");
        menuBar.setLayout(new BorderLayout());

        addMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        addMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        addMenu.setText("Felvitel");
        addMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMenuActionPerformed(evt);
            }
        });
        mainMenu.add(addMenu);
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

        Week.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                Datumbeallit(Week.getSelectedIndex() + 2);
                Muszakoktolt();
            }
        });

        Comboboxtolt(Workplace,"SELECT azon,megnevezes FROM kosp WHERE tervez = 1 ORDER BY megnevezes");
        Comboboxtolt(Shift,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");

        Combokeres(Workplace,14);
        Dolgozotolt();
        Muszakoktolt();
        wScroll.setViewportView(Workers);
        sScroll.setViewportView(Shifts);

        Shift.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Muszakoktolt();
            }
        });

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Hét");
        JLabel Label2 = new JLabel("Kezdő dátum");
        JLabel Label3 = new JLabel("Végdátum");
        JLabel Label4 = new JLabel("Műszak");
        JLabel Label5 = new JLabel("Munkahely");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Week);
        inputPane.add(Week);

        inputPane.add(Label2);
        Label2.setLabelFor(startDate);
        inputPane.add(startDate);

        inputPane.add(Label3);
        Label3.setLabelFor(endDate);
        inputPane.add(endDate);

        inputPane.add(Label4);
        Label4.setLabelFor(Shift);
        inputPane.add(Shift);

        inputPane.add(Label5);
        Label5.setLabelFor(Workplace);
        inputPane.add(Workplace);

        SpringUtilities.makeCompactGrid(inputPane,
                                        5, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        weekPane = new JPanel(new SpringLayout());
        weekPane.setBorder(new EtchedBorder());

        for (int i = 0;i < 7;i++)
        {
            weekPane.add(WeekCheck[i]);
        }

        SpringUtilities.makeCompactGrid(weekPane,
                                        1, 7, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());
        tblPane.add(wScroll);

        stblPane = new JPanel(new BorderLayout());
        stblPane.setBorder(new EtchedBorder());
        stblPane.add(sScroll);

        contentPane.add(inputPane);
        contentPane.add(weekPane);
        contentPane.add(tblPane);
        contentPane.add(stblPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);

        //weekPane
        layout.putConstraint(SpringLayout.WEST, weekPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, weekPane,
                             1,
                             SpringLayout.SOUTH, inputPane);

        //tblPane
        layout.putConstraint(SpringLayout.WEST, tblPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, tblPane,
                             1,
                             SpringLayout.SOUTH, weekPane);
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             1,
                             SpringLayout.EAST, weekPane);
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);
        //stblPane
        layout.putConstraint(SpringLayout.WEST, stblPane,
                             1,
                             SpringLayout.EAST, tblPane);
        layout.putConstraint(SpringLayout.NORTH, stblPane,
                             0,
                             SpringLayout.NORTH, tblPane);
        layout.putConstraint(SpringLayout.EAST, stblPane,
                             1,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, stblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);
        pack();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }
    
    private void addMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (Workers.getRowCount() > 0 && Keres())
        {
            if (Datacheck())
            {
                Configuration cfg = new Configuration();
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    conn.setAutoCommit(false);
                    stmt = conn.createStatement();
                    String sqlTmp = "";
                    int i = 0;
                    while (i < Workers.getRowCount())
                    {
                        if (Boolean.parseBoolean(Workers.getValueAt(i, 3).toString()) == true)
                        {
                            for (int j = 0;j < 7;j++)
                            {
                                if (WeekCheck[j].isSelected())
                                {
                                    if (sqlTmp.compareTo("") != 0)
                                    {
                                        sqlTmp += ",";
                                    }
                                    sqlTmp += "('" + Workers.getValueAt(i, 1).toString() + "','" +
                                              ComboID(Shift) + "','" +
                                              Dates[j] + "')";
                                }
                            }
                        }
                        Workers.setValueAt(false, i, 3);
                        i++;
                    }
                    sqlupdate("INSERT INTO muszakbeosztas(dszam,muszak,datum) VALUES " + sqlTmp);
                    conn.commit();
                    conn.close();
                    Muszakoktolt();
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
            else
            {
                JOptionPane.showMessageDialog(null,"A megadott adatokkal már létezik felvitel! Kérem ellenőrizze!");
            }
        }
    }

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (Shifts.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null,"Biztosan törölni akarja a kijelölt műszakbeosztás adat(okat))?",
                                                             "Törlés jóváhagyása", JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    stmt = conn.createStatement();
                    for (int i = 0;i < Shifts.getSelectedRowCount();i++)
                    {
                        sqlupdate("DELETE muszakbeosztas.* FROM muszakbeosztas WHERE "
                                + "muszakbeosztas.azon = "
                                + Shifts.getValueAt(Shifts.getSelectedRows()[i], 0).toString() + "");
                    }
                    conn.close();
                    Muszakoktolt();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,"kapcsolódási hiba: " + ex.getMessage());
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

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void Datumbeallit(int week)
    {
        Date curr = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(curr);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        startDate.setText(year + "-" + 
                          (month < 10 ? "0" : "") + month +"-" +
                          (day < 10 ? "0" : "") + day);
        Dates[0] = startDate.getText();
        for (int i = 1;i < 7;i++)
        {
            cal.add(Calendar.DATE, 1);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            day = cal.get(Calendar.DATE);
            Dates[i] = (year + "-" +
                        (month < 10 ? "0" : "") + month +"-" +
                        (day < 10 ? "0" : "") + day);
        }
        endDate.setText(year + "-" +
                        (month < 10 ? "0" : "") + month +"-" +
                        (day < 10 ? "0" : "") + day);
    }

    private void Dolgozotolt()
    {
        Configuration cfg = new Configuration();
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Dolgozó szám");
        tableHeaders.add("Név");
        tableHeaders.add("Műszakba");
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            String sqlComm = "SELECT dtorzs.azon,dtorzs.dszam,dtorzs.nev FROM "
                           + "dtorzs INNER JOIN dolgozok ON (dtorzs.dszam = dolgozok.dszam) "
                           + "WHERE dolgozok.munkahely = '"
                           + ComboID(Workplace) + "' "
                           + "ORDER BY dtorzs.nev";
            sqlolvas(sqlComm);
            if (rSet.next())
            {
                do
                {
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(rSet.getInt(1));
                    oneRow.add(rSet.getInt(2));
                    oneRow.add(rSet.getString(3));
                    boolean atad = false;
                    oneRow.add(atad);
                    tableData.add(oneRow);
                } while (rSet.next());
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null, "Kapcsolódási hiba: " + ex.getMessage());
        }
        Workers.setModel(new DefaultTableModel(tableData, tableHeaders)
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
        Workers.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Workers.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Workers.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Workers.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

        TableColumnModel tcm = Workers.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.LEFT);
        tcm.getColumn(1).setCellRenderer(render);

        Workers.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        Workers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        Workers.setAutoCreateRowSorter(true);
    }

    private void Muszakoktolt()
    {
        Configuration cfg = new Configuration();
        String[] tableHeaders = {"Azon","Dolgozói szám","Név","Műszak","Dátum","Nap"};
        Object[][] tableData = new Object[0][0];
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT muszakbeosztas.azon,muszakbeosztas.dszam,dtorzs.nev,muszakok.megnevezes,"
                   + "muszakbeosztas.datum FROM muszakbeosztas "
                   + "INNER JOIN dtorzs ON (muszakbeosztas.dszam = dtorzs.dszam) "
                   + "INNER JOIN muszakok ON (muszakbeosztas.muszak = muszakok.azon) "
                   + "WHERE muszakbeosztas.datum between '" + startDate.getText() + "' AND '"
                   + endDate.getText() + "' AND muszakbeosztas.muszak = '"
                   + ComboID(Shift) + "'");
            rSet.last();
            int count = rSet.getRow();
            rSet.beforeFirst();
            tableData = new Object[count][tableHeaders.length];
            while(rSet.next())
            {
                tableData[rSet.getRow()-1][0] = rSet.getString(1);
                tableData[rSet.getRow()-1][1] = rSet.getString(2);
                tableData[rSet.getRow()-1][2] = rSet.getString(3);
                tableData[rSet.getRow()-1][3] = rSet.getString(4);
                tableData[rSet.getRow()-1][4] = rSet.getString(5);
                tableData[rSet.getRow()-1][5] = WeekDay(rSet.getString(5));
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
        Shifts.setModel(new DefaultTableModel(tableData, tableHeaders)
        {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });

        Shifts.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Shifts.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Shifts.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Shifts.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

        Shifts.setAutoCreateRowSorter(true);
        Shifts.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        Shifts.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private boolean Keres()
    {
        boolean felvitel = false;
        int i = 0;
        while (i < Workers.getRowCount())
        {
            if (Boolean.parseBoolean(Workers.getValueAt(i, 3).toString()) == true)
            {
                felvitel = true;
                break;
            }
            i++;
        }
        return felvitel;
    }

    private boolean Datacheck()
    {
        boolean adatbevitel = true;
        try
        {
            Configuration cfg = new Configuration();
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            int i = 0;
            while (i < Workers.getRowCount())
            {
                if (Boolean.parseBoolean(Workers.getValueAt(i, 3).toString()) == true)
                {
                    for (int j = 0;j < 7;j++)
                    {
                        if (WeekCheck[j].isSelected())
                        {
                            sqlolvas("SELECT muszakbeosztas.azon FROM muszakbeosztas "
                                   + "WHERE dszam = '" + Workers.getValueAt(i, 1) + "' AND "
                                   + "datum = '" + Dates[j] + "' AND muszak = '"
                                   + ComboID(Shift)  + "'");
                            if (rSet.next())
                            {
                                adatbevitel = false;
                                break;
                            }
                        }
                    }
                }
                i++;
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
        return adatbevitel;
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

    private String WeekDay(String date)
    {
        Configuration cfg = new Configuration();
        Calendar cal = Calendar.getInstance();
        cal.setTime(cfg.sqlToDate(date));
        return WeekNamesCal[cal.get(Calendar.DAY_OF_WEEK) - 1];
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

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 16;
    private JMenu mainMenu;
    private JMenuItem addMenu, torolMenu, kilepMenu;
    private JMenuBar menuBar;
    private JPanel inputPane, tblPane, weekPane, stblPane;
    private JComboBox Week, Shift, Workplace;
    private JTable Workers, Shifts;
    private JScrollPane wScroll,sScroll;
    private JTextField startDate, endDate;
    private JCheckBox[] WeekCheck;
    private String[] Dates;
    private String[] WeekNames = {"Hétfő","Kedd","Szerda","Csütörtök","Péntek","Szombat","Vasárnap"};
    private String[] WeekNamesCal = {"Vasárnap","Hétfő","Kedd","Szerda","Csütörtök","Péntek","Szombat"};
}
