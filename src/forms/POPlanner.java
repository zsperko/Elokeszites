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
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class POPlanner extends JExtendedFrame{

    public POPlanner()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/polist.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() - 150),
                                       (int)(defaultTool.getScreenSize().getHeight() - 150)));
        setTitle("Gyártási rendelés tervezés");
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

        WorkPlace = new JComboBox();
        Shift = new JComboBox();
        Datum = new JCalendarCombo();
        mainMenu = new JMenu();
        szamolMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();
        ptScroll = new JScrollPane();

        mainMenu.setText("Menü");

        szamolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        szamolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/calculator.png"))); // NOI18N
        szamolMenu.setText("Tervezés");
        szamolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szamolMenuActionPerformed(evt);
            }
        });
        mainMenu.add(szamolMenu);
        mainMenu.add(new JSeparator());

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png"))); // NOI18N
        torolMenu.setText("Törlés");
        torolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torolMenuActionPerformed(evt);
            }
        });
        //mainMenu.add(torolMenu);
        //mainMenu.add(new JSeparator());

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

        Comboboxtolt(WorkPlace,"SELECT azon,megnevezes FROM kosp WHERE tervez = 1 ORDER BY megnevezes");
        Comboboxtolt(Shift,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");

        WorkPlace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tervtablatolt();
            }
        });

        Shift.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Tervtablatolt();
            }
        });

        listener = new MyDateListener();
        Datum.addDateListener(listener);

        Combokeres(WorkPlace,14);

        Tervtablatolt();

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Munkahely");
        JLabel Label2 = new JLabel("Dátum");
        JLabel Label3 = new JLabel("Műszak");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(WorkPlace);
        inputPane.add(WorkPlace);

        inputPane.add(Label2);
        Label2.setLabelFor(Datum);
        inputPane.add(Datum);

        inputPane.add(Label3);
        Label3.setLabelFor(Shift);
        inputPane.add(Shift);

        SpringUtilities.makeCompactGrid(inputPane,
                                3, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        tablePane = new JPanel(new BorderLayout());
        tablePane.setBorder(new EtchedBorder());
        ptScroll.setViewportView(PlannerTable);
        tablePane.add(ptScroll);

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

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        try
        {
            Configuration cfg = new Configuration();
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlupdate("DELETE tervezotabla.* FROM tervezotabla WHERE datum = '"
                    + cfg.sqlDate(Datum.getDate()) + "' AND muszak >= '"
                    + ComboID(Shift) + "'");
            sqlupdate("DELETE tervezotabla.* FROM tervezotabla WHERE datum > '"
                    + cfg.sqlDate(Datum.getDate()) + "'");
            conn.close();
            Tervtablatolt();
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

    private void szamolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        int valaszt = JOptionPane.showConfirmDialog(null,"Kiszámolja a műszaktervet? A meglévő terv törlődik!","Műszakterv számítása",
                                                    JOptionPane.YES_NO_OPTION);
        if (valaszt == 0)
        {
            Tervtorol();
            POListfeltolt();
            Szamol(1);
            Szamol(2);
            GepekDolgozok();
            Tervtablatolt();
        }
    }

    private void Szamol(int Tipus)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Dátum");
            model.addColumn("Műszak");
            model.addColumn("GRművazon");
            model.addColumn("Mennyiség");
            model.addColumn("Gépazon");
            model.addColumn("Szerszám");
            model.addColumn("Fészek");
            model.addColumn("Kapacitás");
            int myval = POlist.getRowCount();
            int i = 0;
            while (i < POlist.getRowCount())
            {
                Date date = Datum.getDate();
                Date POdate = cfg.sqlToDate(POlist.getValueAt(i, 4).toString());
                int Datumok = POdate.compareTo(date);
                if (Datumok > 0) {
                    date = POdate;
                }
                int firstShift = ComboID(Shift);
                if (Tipus == 1) {
                    WCenterfeltolt(POlist.getValueAt(i, 1).toString(),Tipus);
                }
                else if (Tipus == 2) {
                    WCenterfeltolt(POlist.getValueAt(i, 1).toString(),Tipus);
                }
                if (WCenter.getRowCount() > 0)
                {
                    while (POlist.getValueAt(i, 2).toString().compareTo("0.0") != 0)
                    {
                        for (int k = 0;k < Shift.getItemCount();k++)
                        {
                            if (date.compareTo(Datum.getDate()) == 0 &&
                                ((ListItem)Shift.getItemAt(k)).getValue() < firstShift)
                            {}
                            else
                            {
                                for (int j = 0;j < WCenter.getRowCount();j++)
                                {
                                    String sqlutasitas1 ="SELECT termeles FROM uzeminaptar WHERE datum = '"
                                           + cfg.sqlDate(date) + "' AND muszak = '"
                                           + ((ListItem)Shift.getItemAt(k)).getValue() + "' AND gepazon = '"
                                           + WCenter.getValueAt(j, 0).toString() + "'";
                                    
                                    sqlolvas("SELECT termeles FROM uzeminaptar WHERE datum = '"
                                           + cfg.sqlDate(date) + "' AND muszak = '"
                                           + ((ListItem)Shift.getItemAt(k)).getValue() + "' AND gepazon = '"
                                           + WCenter.getValueAt(j, 0).toString() + "'");
                                    if (rSet.next())
                                    {
                                        rSet.first();
                                        if (rSet.getInt(1) == 1)
                                        {
                                            String sqlutasitas="SELECT azon,kapacitas FROM kapacitasok WHERE datum = '"
                                                    + cfg.sqlDate(date) + "' AND muszak = '"
                                                    + ((ListItem)Shift.getItemAt(k)).getValue() + "' AND gepazon = "
                                                    + WCenter.getValueAt(j, 0).toString() + " AND feszek = "
                                                    + WCenter.getValueAt(j, 1).toString();
                                            
                                            sqlolvas("SELECT azon,kapacitas FROM kapacitasok WHERE datum = '"
                                                    + cfg.sqlDate(date) + "' AND muszak = '"
                                                    + ((ListItem)Shift.getItemAt(k)).getValue() + "' AND gepazon = "
                                                    + WCenter.getValueAt(j, 0).toString() + " AND feszek = "
                                                    + WCenter.getValueAt(j, 1).toString());
                                            double maradek = Double.parseDouble(POlist.getValueAt(i, 2).toString());
                                            double utem = Double.parseDouble(POlist.getValueAt(i, 3).toString());
                                            double mennyiseg;
                                            if (rSet.next())
                                            {
                                                rSet.first();
                                                int azon = rSet.getInt(1);
                                                double kapacitas = (double)rSet.getInt(2);
                                                if (kapacitas > 0)
                                                {
                                                    mennyiseg = utem * (kapacitas / 100);
                                                    if (mennyiseg > maradek) {
                                                        mennyiseg = maradek;
                                                    }
                                                    double valt = (mennyiseg / utem) * 100;
                                                    kapacitas -= (int)valt;

                                                    if (mennyiseg > 0)
                                                    {
                                                        POlist.setValueAt(maradek - mennyiseg, i, 2);
                                                        System.out.println(POlist.getValueAt(i, 0).toString() + " - " + WCenter.getValueAt(j, 0).toString());
                                                        sqlupdate("INSERT INTO tervezotabla (datum,muszak,grmuvazon,mennyiseg,"
                                                                + "gepazon,szerszamazon,feszek) VALUES ('"
                                                                + cfg.sqlDate(date) + "','"
                                                                + ((ListItem)Shift.getItemAt(k)).getValue() + "','"
                                                                + POlist.getValueAt(i, 0).toString() + "','"
                                                                + (int)mennyiseg + "','"
                                                                + WCenter.getValueAt(j, 0).toString() + "','"
                                                                + WCenter.getValueAt(j, 2).toString() + "','"
                                                                + WCenter.getValueAt(j, 1).toString() + "')");
                                                        sqlupdate("UPDATE kapacitasok SET kapacitas = '"
                                                                + kapacitas + "' WHERE azon = '" + azon + "'");

                                                    }
                                                }
                                            }
                                            else
                                            {
                                                int kapacitas;
                                                if (maradek != 0)
                                                {
                                                    if (maradek > utem)
                                                    {
                                                        mennyiseg = utem;
                                                        kapacitas = 0;
                                                    }
                                                    else
                                                    {
                                                        mennyiseg = maradek;
                                                        double valt = (mennyiseg / utem) * 100;
                                                        kapacitas = 100 - (int)valt;
                                                    }
                                                    POlist.setValueAt(maradek - mennyiseg, i, 2);
                                                    System.out.println(POlist.getValueAt(i, 0).toString() + " - " + WCenter.getValueAt(j, 0).toString());
                                                    sqlupdate("INSERT INTO tervezotabla (datum,muszak,grmuvazon,mennyiseg,"
                                                            + "gepazon,szerszamazon,feszek) VALUES ('"
                                                            + cfg.sqlDate(date) + "','"
                                                            + ((ListItem)Shift.getItemAt(k)).getValue() + "','"
                                                            + POlist.getValueAt(i, 0).toString() + "','"
                                                            + (int)mennyiseg + "','"
                                                            + WCenter.getValueAt(j, 0).toString() + "','"
                                                            + WCenter.getValueAt(j, 2).toString() + "','"
                                                            + WCenter.getValueAt(j, 1).toString() + "')");
                                                    sqlupdate("INSERT INTO kapacitasok (datum,muszak,gepazon,feszek,kapacitas) "
                                                            + "VALUES ('" + cfg.sqlDate(date) + "','"
                                                            + ((ListItem)Shift.getItemAt(k)).getValue() + "','"
                                                            + WCenter.getValueAt(j, 0).toString() + "','"
                                                            + WCenter.getValueAt(j, 1).toString() + "','"
                                                            + kapacitas + "')");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.add(Calendar.DATE,1);
                        date = cal.getTime();
                    }
                    int myval2 = 1;
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
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void GepekDolgozok()
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlupdate("DELETE gepekdolgozok.* FROM gepekdolgozok");
            sqlupdate("INSERT INTO gepekdolgozok (gepekdolgozok.datum,gepekdolgozok.muszak,"
                    + "gepekdolgozok.dszam,gepekdolgozok.muhelyazon) SELECT napiterv.datum,"
                    + "napiterv.muszak,napiterv.dszam,napiterv.muhelyazon FROM napiterv WHERE "
                    + "napiterv.datum >= '" + cfg.sqlDate(Datum.getDate()) + "' "
                    + "GROUP BY napiterv.datum,napiterv.muszak,napiterv.dszam,napiterv.muhelyazon");
            sqlupdate("DELETE gepekdolgozok.* FROM gepekdolgozok WHERE datum = '"
                    + cfg.sqlDate(Datum.getDate()) + "' AND muszak < '"
                    + ComboID(Shift) + "'");
            sqlupdate("DELETE napiterv.* FROM napiterv WHERE datum > '"
                    + cfg.sqlDate(Datum.getDate()) + "'");
            sqlupdate("DELETE napiterv.* FROM napiterv WHERE datum = '"
                    + cfg.sqlDate(Datum.getDate()) + "' AND muszak >= '"
                    + ComboID(Shift) + "'");
            sqlupdate("INSERT INTO napiterv (napiterv.datum,napiterv.muszak,napiterv.dszam,"
                    + "napiterv.grmuvazon,napiterv.muhelyazon,napiterv.mennyiseg) "
                    + "SELECT tervezotabla.datum,tervezotabla.muszak,gepekdolgozok.dszam,"
                    + "tervezotabla.grmuvazon,gepekdolgozok.muhelyazon,tervezotabla.mennyiseg "
                    + "FROM tervezotabla "
                    + "INNER JOIN gepekdolgozok ON (tervezotabla.datum = gepekdolgozok.datum) AND "
                    + "(tervezotabla.muszak = gepekdolgozok.muszak) AND "
                    + "(tervezotabla.gepazon = gepekdolgozok.muhelyazon)");
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

    private void POListfeltolt()
    {
        POlist = null;
        POlist = new JTable();
        String[] tableHeaders = {"Azon","Gépszám","Maradék","Ütem","Kezdés dátuma"};
        Object[][] tableData;
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            String sqlCmd = "SELECT gr_muveletterv.azon,szerszamok.azon AS szerszamazon,"
                          + "(gyartasirend.mennyiseg - gr_muveletterv.befejezettmennyiseg) AS maradek,"
                          + "cikktorzs.utem,gyartasirend.kezddatum FROM gr_muveletterv "
                          + "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) "
                          + "INNER JOIN muhelyek ON (gr_muveletterv.muhelykod = muhelyek.szam) "
                          + "INNER JOIN cikktorzs ON (gyartasirend.cikkszam = cikktorzs.cikkszam) "
                          + "INNER JOIN szerszamok ON (gr_muveletterv.gepszam = szerszamok.szam) "
                          + "WHERE gr_muveletterv.kijelolt = 1 AND "
                          + "gyartasirend.mennyiseg > gr_muveletterv.befejezettmennyiseg AND "
                          + "gr_muveletterv.nyitott = 1 "
                          + "ORDER BY gyartasirend.kezddatum";
            sqlolvas(sqlCmd);
            rSet.last();
            int count = rSet.getRow();
            rSet.beforeFirst();
            tableData = new Object[count][tableHeaders.length];
            while(rSet.next())
            {
                tableData[rSet.getRow()-1][0] = rSet.getString(1);
                tableData[rSet.getRow()-1][1] = rSet.getString(2);
                tableData[rSet.getRow()-1][2] = rSet.getDouble(3);
                tableData[rSet.getRow()-1][3] = rSet.getDouble(4);
                tableData[rSet.getRow()-1][4] = rSet.getString(5);
            }
            conn.close();

            POlist.setModel(new DefaultTableModel(tableData, tableHeaders)
            {
                Class[] types = new Class [] {
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
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

    private void WCenterfeltolt(String azon, int Tipus)
    {
        WCenter = null;
        WCenter = new JTable();
        String[] tableHeaders = {"Műhely","Fészek","Szerszám"};
        Object[][] tableData;
        try
        {
            String sqlCmd = "";
            if (Tipus == 1)
            {
                sqlCmd = "SELECT muhelyazon,feszek,szerszamazon FROM szerszamokmuhelyben WHERE "
                       + "szerszamazon = '" + azon + "' AND zarolt = 0";
            }
            else if (Tipus == 2)
            {
                sqlCmd = "SELECT muhelyazon,feszek,kovszerszam FROM szerszamokmuhelyben WHERE "
                       + "kovszerszam = '" + azon + "' AND zarolt = 0";
            }
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
            }

            WCenter.setModel(new DefaultTableModel(tableData, tableHeaders)
            {
                Class[] types = new Class [] {
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
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
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
        }
    }

    private void Tervtablatolt()
    {
        PlannerTable = null;
        PlannerTable = new JTable();
        ptScroll.setViewportView(PlannerTable);
        String[] tableHeaders = {"Azon","Gyártási rendelés","Cikkszám","Mennyiség","Ütem","Műhely","Szerszám","Fészek"};
        Object[][] tableData;
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            String sqlCmd = "SELECT tervezotabla.azon,gr_muveletterv.grszam,gyartasirend.cikkszam,tervezotabla.mennyiseg,"
                          + "cikktorzs.utem,muhelyek.szam,szerszamok.szam,tervezotabla.feszek "
                          + "FROM tervezotabla INNER JOIN muszakok ON (tervezotabla.muszak = muszakok.azon) "
                          + "INNER JOIN gr_muveletterv ON (tervezotabla.grmuvazon = gr_muveletterv.azon) "
                          + "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) "
                          + "INNER JOIN cikktorzs ON (gyartasirend.cikkszam = cikktorzs.cikkszam) "
                          + "INNER JOIN muhelyek ON (tervezotabla.gepazon = muhelyek.azon) "
                          + "INNER JOIN szerszamok ON (tervezotabla.szerszamazon = szerszamok.azon) "
                          + "WHERE tervezotabla.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND "
                          + "tervezotabla.muszak = '" + ComboID(Shift) + "' "
                          + "AND muhelyek.ktghely = '" + ComboID(WorkPlace) + "' "
                          + "ORDER BY gr_muveletterv.grszam";
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
                int value = (int)rSet.getDouble(4);
                tableData[rSet.getRow()-1][3] = value;
                tableData[rSet.getRow()-1][4] = rSet.getString(5);
                tableData[rSet.getRow()-1][5] = rSet.getString(6);
                tableData[rSet.getRow()-1][6] = rSet.getString(7);
                tableData[rSet.getRow()-1][7] = rSet.getString(8);
            }
            conn.close();

            PlannerTable.setModel(new DefaultTableModel(tableData, tableHeaders)
            {
                Class[] types = new Class [] {
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false
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
            PlannerTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            PlannerTable.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
            PlannerTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
            PlannerTable.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
            PlannerTable.setAutoCreateRowSorter(true);

            PlannerTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            TableColumnModel tcm = PlannerTable.getColumnModel();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
            FormatRenderer render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.RIGHT);
            tcm.getColumn(3).setCellRenderer(render);
            tcm.getColumn(4).setCellRenderer(render);
            tcm.getColumn(5).setCellRenderer(render);
            tcm.getColumn(6).setCellRenderer(render);
            tcm.getColumn(7).setCellRenderer(render);
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

    private void Tervtorol()
    {
        Configuration cfg = new Configuration();
        try
        {            
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlupdate("DELETE tervezotabla.* FROM tervezotabla WHERE datum = '"
                    + cfg.sqlDate(Datum.getDate()) + "' AND muszak >= '"
                    + ComboID(Shift) + "'");
            sqlupdate("DELETE tervezotabla.* FROM tervezotabla WHERE datum > '"
                    + cfg.sqlDate(Datum.getDate()) + "'");
            sqlupdate("DELETE kapacitasok.* FROM kapacitasok WHERE kapacitasok.datum = '"
                    + cfg.sqlDate(Datum.getDate()) + "' AND muszak >= '"
                    + ComboID(Shift) + "'");
            sqlupdate("DELETE kapacitasok.* FROM kapacitasok WHERE kapacitasok.datum > '"
                    + cfg.sqlDate(Datum.getDate()) + "'");
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

    class MyDateListener implements DateListener
    {

        public void dateChanged(DateEvent e)
        {
            Calendar c = e.getSelectedDate();
            if (c != null) {
                Tervtablatolt();
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
    private int formID = 20;
    private JTable POlist, WCenter, PlannerTable;
    private JScrollPane ptScroll;
    private JPanel inputPane, tablePane;
    private JCalendarCombo Datum;
    private JComboBox Shift, WorkPlace;
    private JMenu mainMenu;
    private JMenuItem szamolMenu, torolMenu, kilepMenu;
    private JMenuBar menuBar;
    private MyDateListener listener;
}
