/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import dialogs.ReportDialog;
import classes.ListItem;
import classes.SpringUtilities;
import classes.FormatRenderer;
import classes.JExtendedFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
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
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class AMaterialRequest extends JExtendedFrame implements TableModelListener{
    
    public AMaterialRequest()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/calculator.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 2),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2)));
        setTitle("Anyagigény napiterv alapján");
        setformID(formID);
        
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            @Override
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            @Override
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        
        Datum = new JCalendarCombo();
        tScroll = new JScrollPane();
        anyagigenyTabla = new JTable();
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        szamolMenu = new JMenuItem();
        osszetettriportMenu = new JMenuItem();
        reszletesriportMenu = new JMenuItem();
        teruletMenu = new JMenuItem();
        visszaadMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        Munkahely = new JComboBox();
        Muszak = new JComboBox();
        pMuszak = new JComboBox();
        Periods = new JCheckBox();
        endDatum = new JCalendarCombo();

        endDatum.setEnabled(false);
        pMuszak.setEnabled(false);

        rowHeight = anyagigenyTabla.getRowHeight();
        rowHeight += 10;

        mainMenu.setText("Menü");

        szamolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        szamolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/calculator.png"))); // NOI18N
        szamolMenu.setText("Anyagigény számítás");
        szamolMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            szamolMenuActionPerformed(evt);
        });
        mainMenu.add(szamolMenu);
        mainMenu.add(new JSeparator());

        osszetettriportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        osszetettriportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report_simple.png"))); // NOI18N
        osszetettriportMenu.setText("Üzemi anyagigénylési lap");
        osszetettriportMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            osszetettriportMenuActionPerformed(evt);
        });
        mainMenu.add(osszetettriportMenu);

        reszletesriportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        reszletesriportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report_dif.png"))); // NOI18N
        reszletesriportMenu.setText("Részletes riport");
        reszletesriportMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            reszletesriportMenuActionPerformed(evt);
        });
        mainMenu.add(reszletesriportMenu);

        teruletMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        teruletMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/reportarea.png"))); // NOI18N
        teruletMenu.setText("Raktári anyagigénylési lap");
        teruletMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            teruletMenuMenuActionPerformed(evt);
        });
        mainMenu.add(teruletMenu);

        visszaadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        visszaadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/transfer.png"))); // NOI18N
        visszaadMenu.setText("Visszaadandó anyagok");
        visszaadMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            visszaadMenuMenuActionPerformed(evt);
        });
        mainMenu.add(visszaadMenu);
        mainMenu.add(new JSeparator());

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png"))); // NOI18N
        torolMenu.setText("Töröl");
        torolMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            torolMenuActionPerformed(evt);
        });
        mainMenu.add(torolMenu);
        mainMenu.add(new JSeparator());

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/close.png"))); // NOI18N
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            kilepMenuActionPerformed(evt);
        });
        mainMenu.add(kilepMenu);
        menuBar.add(mainMenu);
        setJMenuBar(menuBar);

        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp ORDER BY megnevezes");
        Comboboxtolt(Muszak,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");
        Comboboxtolt(pMuszak,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");

        Munkahely.addActionListener((ActionEvent e) -> {
            Anyagigenytabla();
        });

        Muszak.addActionListener((ActionEvent e) -> {
            Anyagigenytabla();
        });

        Periods.addItemListener((java.awt.event.ItemEvent evt) -> {
            if (((JCheckBox)evt.getSource()).isSelected())
            {
                endDatum.setEnabled(true);
                pMuszak.setEnabled(true);
                endDatum.setDate(Datum.getDate());
            }
            else
            {
                endDatum.setEnabled(false);
                pMuszak.setEnabled(false);
            }
        });

        Combokeres(Munkahely,14);
        
        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Dátum");
        JLabel Label2 = new JLabel("Munkahely");
        JLabel Label3 = new JLabel("Műszak");
        JLabel Label4 = new JLabel("Periódus nyomtatása");
        JLabel Label5 = new JLabel("Végdátum");
        JLabel Label6 = new JLabel("Műszak(ig)");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());
        
        inputPane.add(Label1);
        Label1.setLabelFor(Datum);
        inputPane.add(Datum);

        listener = new MyDateListener();
        Datum.addDateListener(listener);
        
        inputPane.add(Label2);
        Label2.setLabelFor(Munkahely);
        inputPane.add(Munkahely);

        inputPane.add(Label3);
        Label3.setLabelFor(Muszak);
        inputPane.add(Muszak);

        inputPane.add(Label4);
        Label4.setLabelFor(Periods);
        inputPane.add(Periods);

        inputPane.add(Label5);
        Label5.setLabelFor(endDatum);
        inputPane.add(endDatum);

        inputPane.add(Label6);
        Label6.setLabelFor(pMuszak);
        inputPane.add(pMuszak);
        
        SpringUtilities.makeCompactGrid(inputPane,
                                6, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        tScroll.setViewportView(anyagigenyTabla);
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
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             1,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);

        pack();
    }
    
    
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void szamolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        int valaszt = JOptionPane.showConfirmDialog(null,"Biztosan készít anyagigény listát? A meglévő lista adatai törlődnek!", "Anyagigény számítás",
                                                    JOptionPane.YES_NO_OPTION);
        if (valaszt == 0)
        {
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement();
                sqlupdate("DELETE anyagigenyek.* FROM anyagigenyek WHERE datum = '" + 
                          cfg.sqlDate(Datum.getDate()) + "' AND munkahely = '" +
                          ComboID(Munkahely) + "' AND muszak >= '" +
                          ComboID(Muszak) + "'");
                sqlupdate("DELETE anyagigenyek.* FROM anyagigenyek WHERE datum > '" +
                          cfg.sqlDate(Datum.getDate()) + "' AND munkahely = '" +
                          ComboID(Munkahely) + "'");
                sqlolvas("SELECT max(napiterv.datum) FROM napiterv");
                if (rSet.next())
                {
                    rSet.first();
                    Date date = cfg.sqlToDate(rSet.getString(1));
                    Date fromDate = DateCalc(date);
                    int firstShift = ComboID(Muszak);
                    int cDates = fromDate.compareTo(fromDate);
                    while (cDates >= 0)
                    {
                        for (int k = 0;k < Muszak.getItemCount();k++)
                        {
                            if (fromDate.compareTo(Datum.getDate()) == 0 &&
                                ((ListItem)Muszak.getItemAt(k)).getValue() < firstShift)
                            {
                            }
                            else
                            {
                                sqlupdate("INSERT INTO anyagigenyek (anyagigenyek.cikkszam,anyagigenyek.mennyiseg," 
                                        + "anyagigenyek.tipus,anyagigenyek.megjegyzes,anyagigenyek.datum,anyagigenyek.munkahely,"
                                        + "anyagigenyek.muszak,anyagigenyek.gr_muvazon,anyagigenyek.raktar,anyagigenyek.muhelyazon) "
                                        + "SELECT cikktorzs.cikkszam,"
                                        + "(napiterv.mennyiseg * gr_dbj.alkatresz) AS mennyi,1,gr_muveletterv.grszam,'" 
                                        + cfg.sqlDate(fromDate) + "','" 
                                        + ComboID(Munkahely) + "','"
                                        + ((ListItem)Muszak.getItemAt(k)).getValue() + "',gr_muveletterv.azon,gr_dbj.raktar,"
                                        + "napiterv.muhelyazon FROM "
                                        + "anyagtipus "
                                        + "INNER JOIN anyagigenybeallitas ON (anyagtipus.azon = anyagigenybeallitas.anyagtipus) "
                                        + "INNER JOIN cikktorzs ON (anyagtipus.megnevezes = cikktorzs.anyagtipus) "
                                        + "INNER JOIN gr_dbj ON (cikktorzs.cikkszam = gr_dbj.cikkszam) "
                                        + "INNER JOIN gr_muveletterv ON (gr_muveletterv.grszam = gr_dbj.grszam) "
                                        + "INNER JOIN napiterv ON (gr_muveletterv.azon = napiterv.grmuvazon) "
                                        + "WHERE napiterv.datum = '" + cfg.sqlDate(fromDate) + "' AND napiterv.muszak = '"
                                        + ((ListItem)Muszak.getItemAt(k)).getValue() + "' AND anyagigenybeallitas.anyagigeny = 1");
                            }
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(fromDate);
                        cal.add(Calendar.DATE,1);
                        fromDate = cal.getTime();
                        firstShift = 0;
                        cDates = date.compareTo(fromDate);
                    }
                }
                conn.close();
                Anyagigenytabla();
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
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void osszetettriportMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report(1);
    }
    
    private void reszletesriportMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report(2);
    }

    private void teruletMenuMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report(3);
    }

    private void visszaadMenuMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report(4);
    }

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void Anyagigenytabla()
    {
        tScroll.setViewportView(null);
        anyagigenyTabla = null;
        anyagigenyTabla = new JTable();
        tScroll.setViewportView(anyagigenyTabla);
        Vector<String> tableHeaders = new Vector<>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Cikkszám");
        tableHeaders.add("Mennyiség");
        tableHeaders.add("Megjegyzés");
        tableHeaders.add("Dátum");
        tableHeaders.add("Műszak");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT anyagigenyek.azon,anyagigenyek.cikkszam,anyagigenyek.mennyiseg,"
                   + "anyagigenyek.megjegyzes,anyagigenyek.datum,muszakok.megnevezes FROM "
                   + "anyagigenyek INNER JOIN muszakok ON (anyagigenyek.muszak = muszakok.azon) "
                   + "WHERE anyagigenyek.datum = '" + cfg.sqlDate(Datum.getDate())
                   + "' AND anyagigenyek.munkahely = '"
                   + ComboID(Munkahely)
                   + "' AND anyagigenyek.muszak = '"
                   + ComboID(Muszak) + "'");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getDouble(3));
                oneRow.add(rSet.getString(4));
                oneRow.add(rSet.getString(5));
                oneRow.add(rSet.getString(6));
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
        anyagigenyTabla.setModel(new DefaultTableModel(tableData, tableHeaders)
        {
            Class[] types = new Class [] {
                java.lang.Object.class, 
                java.lang.Object.class, 
                java.lang.Double.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false
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
        //anyagigenyTabla.getColumnModel().getColumn(2).setCellEditor(new DoubleFormatter(0,35000));
        anyagigenyTabla.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        anyagigenyTabla.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        anyagigenyTabla.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        anyagigenyTabla.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

        TableColumnModel tcm = anyagigenyTabla.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.RIGHT);
        tcm.getColumn(2).setCellRenderer(render);
        anyagigenyTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        anyagigenyTabla.setAutoCreateRowSorter(true);
        anyagigenyTabla.setRowHeight(rowHeight);
        anyagigenyTabla.getModel().addTableModelListener(this);
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

    @Override
    public void tableChanged(TableModelEvent e) {
        Object data = anyagigenyTabla.getValueAt(anyagigenyTabla.getSelectedRow(),2);
        UpdateChanges(anyagigenyTabla.getValueAt(anyagigenyTabla.getSelectedRow(), 0).toString(),
                      data.toString());
    }

    private void ParametersClear()
    {
        for (int i = 0;i < 10;i++)
        {
            Configuration.setParamName("", i);
            Configuration.setParamValue("", i);
        }
    }

    private void TableMake(int valaszt)
    {
        Configuration cfg = new Configuration();
        try
        {
            String tableName = "temp_anyagigeny";
            String startdate,enddate;
            startdate = cfg.sqlDate(Datum.getDate());
            enddate = cfg.sqlDate(Datum.getDate());
            if (Periods.isSelected())
            {
                enddate = cfg.sqlDate(endDatum.getDate());
            }
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SHOW TABLES LIKE '" + tableName + "'");
            if (rSet.next())
            {
                sqlupdate("DROP TABLE " + tableName + "");
            }
            switch (valaszt)
            {
                case 1:
                    sqlupdate("CREATE TABLE " +  tableName + "("
                            + "azon int(11) NOT NULL AUTO_INCREMENT,"
                            + "cikkszam char(20) COLLATE utf8_hungarian_ci NOT NULL,"
                            + "mennyiseg double(15,3) NOT NULL,"
                            + "tipus tinyint(3) NOT NULL,"
                            + "gyartasirend char(50) COLLATE utf8_hungarian_ci DEFAULT NULL,"
                            + "muhelyszam char(20) COLLATE utf8_hungarian_ci NOT NULL,"
                            + "datum date NOT NULL,"
                            + "munkahely int(11) NOT NULL,"
                            + "muszak tinyint(2) NOT NULL,"
                            + "terulet tinyint(4) NOT NULL,"
                            + "raktar char(20) COLLATE utf8_hungarian_ci DEFAULT NULL,"
                            + "keszlet double(10,2) NOT NULL DEFAULT '0.00',"
                            + "uzemikeszlet double(10,2) NOT NULL DEFAULT '0.00',"
                            + "PRIMARY KEY (azon),"
                            + "UNIQUE KEY azon (azon)) "
                            + "ENGINE=MyISAM AUTO_INCREMENT=256 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;");
                    sqlupdate("INSERT INTO " + tableName + " (cikkszam,mennyiseg,tipus,gyartasirend,muhelyszam,datum,"
                            + "munkahely,muszak,terulet,raktar) SELECT anyagigenyek.cikkszam,anyagigenyek.mennyiseg,"
                            + "anyagigenyek.tipus,anyagigenyek.megjegyzes,muhelyek.szam,anyagigenyek.datum,"
                            + "anyagigenyek.munkahely,anyagigenyek.muszak,muhelyek.terulet,anyagigenyek.raktar FROM "
                            + "anyagigenyek INNER JOIN muhelyek ON (anyagigenyek.muhelyazon = muhelyek.azon) WHERE "
                            + "anyagigenyek.datum BETWEEN '" + startdate + "' AND '" + enddate + "'");
                    sqlupdate("UPDATE temp_anyagigeny INNER JOIN (SELECT keszlet.cikkszam,sum(keszlet.mennyiseg) AS mennyiseg "
                            + "FROM keszlet WHERE (keszlet.raktar = 'FURNER KOZ') OR (keszlet.raktar = 'SVARTNI') GROUP BY "
                            + "keszlet.cikkszam) seged ON (temp_anyagigeny.cikkszam = seged.cikkszam) "
                            + "SET temp_anyagigeny.keszlet = seged.mennyiseg");
                    sqlupdate("UPDATE temp_anyagigeny INNER JOIN keszlet ON (temp_anyagigeny.cikkszam = keszlet.cikkszam) AND "
                            + "(temp_anyagigeny.raktar = keszlet.raktar) SET temp_anyagigeny.uzemikeszlet = keszlet.mennyiseg");
                    break;
                case 2:
                    sqlupdate("CREATE TABLE " +  tableName + "("
                            + "azon int(11) NOT NULL AUTO_INCREMENT,"
                            + "cikkszam char(20) COLLATE utf8_hungarian_ci NOT NULL,"
                            + "mennyiseg double(15,3) NOT NULL,"
                            + "tipus tinyint(3) NOT NULL,"
                            + "megjegyzes char(50) COLLATE utf8_hungarian_ci DEFAULT NULL,"
                            + "datum date NOT NULL,"
                            + "munkahely int(11) NOT NULL,"
                            + "muszak tinyint(2) NOT NULL,"
                            + "terulet tinyint(4) NOT NULL,"
                            + "raktar char(20) COLLATE utf8_hungarian_ci DEFAULT NULL,"
                            + "PRIMARY KEY (azon),"
                            + "UNIQUE KEY azon (azon))"
                            + "ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci");
                    sqlupdate("INSERT INTO " + tableName + " (cikkszam,mennyiseg,tipus,megjegyzes,datum,"
                            + "munkahely,muszak,terulet,raktar) SELECT anyagigenyek.cikkszam,anyagigenyek.mennyiseg,"
                            + "anyagigenyek.tipus,anyagigenyek.megjegyzes,anyagigenyek.datum,anyagigenyek.munkahely,"
                            + "anyagigenyek.muszak,muhelyek.terulet,anyagigenyek.raktar FROM anyagigenyek "
                            + "INNER JOIN muhelyek ON (anyagigenyek.muhelyazon = muhelyek.azon) WHERE "
                            + "anyagigenyek.datum BETWEEN '" + startdate + "' AND '" + enddate + "'");
                        break;
            }
            sqlupdate("DELETE " + tableName + ".* FROM " + tableName + " "
                    + "WHERE datum = '" + startdate
                    + "' AND muszak < '" + ComboID(Muszak) + "'");
            sqlupdate("DELETE " + tableName + ".* FROM " + tableName + " "
                    + "WHERE datum = '" + enddate
                    + "' AND muszak > '" + ComboID(pMuszak) + "'");
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        finally
        {
            try
            {conn.close();}
            catch (SQLException ex){}
        }
    }

    private void VisszaadTabla()
    {
        Configuration cfg = new Configuration();
        try
        {
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            Calendar cal = Calendar.getInstance();
            Date datum = cal.getTime();
            sqlupdate("DELETE visszadando.* FROM visszadando");
            sqlupdate("INSERT INTO visszadando (visszadando.cikkszam,visszadando.mennyiseg,visszadando.keszlet) "
                    + "SELECT keszlet.cikkszam,keszlet.mennyiseg,keszlet.mennyiseg FROM keszlet "
                    + "INNER JOIN raktarak ON (keszlet.raktar = raktarak.raktarnev) "
                    + "WHERE raktarak.uzemkod = '"
                    + ComboID(Munkahely) + "'");
            int muszak = ComboID(Muszak);
            for (int i = 0;i < 3;i++)
            {
                sqlupdate("UPDATE visszadando INNER JOIN (SELECT anyagigenyek.cikkszam,"
                        + "sum(anyagigenyek.mennyiseg) AS mennyiseg FROM anyagigenyek WHERE "
                        + "anyagigenyek.datum = '" + cfg.sqlDate(datum) + "' AND anyagigenyek.muszak = '"
                        + muszak + "' AND anyagigenyek.munkahely = '"
                        + ComboID(Munkahely) + "' GROUP BY "
                        + "anyagigenyek.cikkszam ) seged ON (visszadando.cikkszam = seged.cikkszam) "
                        + "SET visszadando.mennyiseg = visszadando.mennyiseg - seged.mennyiseg, "
                        + "visszadando.anyagigeny = visszadando.anyagigeny + seged.mennyiseg");
                muszak++;
                if (muszak == 4)
                {
                    muszak = 1;
                    cal.add(Calendar.DATE, 1);
                    datum = cal.getTime();
                }
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
        finally
        {
            try
            {conn.close();}
            catch (SQLException ex){}
        }
    }

    private void Report(int valaszt)
    {
        boolean DoReport = false;
        if (Periods.isSelected()) 
        {
            int results = Datum.getDate().compareTo(endDatum.getDate());
            if(results <= 0) DoReport = true;
        }
        else DoReport = true;
        if (DoReport)
        {
            ParametersClear();
            Configuration cfg = new Configuration();
            String startdate,enddate,startmuszak,endmuszak;
            startdate = cfg.sqlDate(Datum.getDate());
            enddate = cfg.sqlDate(Datum.getDate());
            startmuszak = ComboVAL(Muszak);
            endmuszak = ComboVAL(Muszak);
            if (Periods.isSelected())
            {
                enddate = cfg.sqlDate(endDatum.getDate());
                endmuszak = ComboVAL(pMuszak);
            }
            String riportName = "";
            switch (valaszt) {
                case 1:
                    TableMake(1);
                    riportName = "reports/osszetettanyagigeny.jasper";
                    break;
                case 2:
                    TableMake(2);
                    riportName = "reports/reszletesanyagigeny.jasper";
                    break;
                case 3:
                    TableMake(1);
                    riportName = "reports/anyagigenyterulet.jasper";
                    break;
                case 4:
                    VisszaadTabla();
                    riportName = "reports/visszaadando.jasper";
                    Calendar cal = Calendar.getInstance();
                    Date datum = cal.getTime();
                    startdate = cfg.sqlDate(datum);
                    break;
                default:
                    break;
            }
            Configuration.setParamName("user", 0);
            Configuration.setParamValue("admin", 0);
            Configuration.setParamName("startdate", 1);
            Configuration.setParamValue(startdate, 1);
            Configuration.setParamName("enddate", 2);
            Configuration.setParamValue(enddate, 2);
            Configuration.setParamName("startmuszak", 3);
            Configuration.setParamValue(startmuszak, 3);
            Configuration.setParamName("endmuszak", 4);
            Configuration.setParamValue(endmuszak, 4);
            Configuration.setReportName(riportName);
            ReportDialog rdialog = new ReportDialog();
            rdialog.setVisible(true);
        }
    }

    private void UpdateChanges(String index, String value)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            sqlupdate("UPDATE anyagigenyek SET mennyiseg = '" + value + "' " +
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

    class MyDateListener implements DateListener
    {

        @Override
        public void dateChanged(DateEvent e)
        {
            Calendar c = e.getSelectedDate();
            if (c != null) {
                Anyagigenytabla();
            }
            else {
                //System.out.println("No time selected.");
            }
        }
    }

    private Date DateCalc(Date datum)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Datum.getDate());
        int ev = cal.get(Calendar.YEAR);
        int honap = cal.get(Calendar.MONTH);
        int nap = cal.get(Calendar.DATE);
        cal = Calendar.getInstance();
        cal.setTime(datum);
        cal.set(Calendar.YEAR, ev);
        cal.set(Calendar.MONTH, honap);
        cal.set(Calendar.DATE, nap);
        datum = cal.getTime();
        return datum;
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private final int formID = 11;
    private JCalendarCombo Datum, endDatum;
    private JScrollPane tScroll;
    private JTable anyagigenyTabla;
    private int rowHeight;
    private JCheckBox Periods;
    private JMenuBar menuBar;
    private JMenu mainMenu;
    private JMenuItem szamolMenu, torolMenu, osszetettriportMenu, reszletesriportMenu, teruletMenu,
                      visszaadMenu, kilepMenu;
    private JComboBox Munkahely, Muszak, pMuszak;
    private JPanel inputPane, tblPane;
    private MyDateListener listener;
}
