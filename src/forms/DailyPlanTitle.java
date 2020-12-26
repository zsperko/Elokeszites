/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import dialogs.ReportDialog;
import classes.RectangleButton;
import classes.ListItem;
import classes.NoEditModel;
import classes.SpringUtilities;
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
import java.util.Calendar;
import java.util.Vector;
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
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class DailyPlanTitle extends JExtendedFrame{

    public DailyPlanTitle()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/header.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 2),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2)));
        setformID(formID);
        
        setTitle("Napiterv fejléc");
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

        Munkahely = new JComboBox();
        Muszak = new JComboBox();
        Dolgozok = new JComboBox();
        Beosztas = new JComboBox();
        mainMenu = new JMenu();
        hozzaadMenu = new JMenuItem();
        riportMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();
        Datum = new JCalendarCombo();
        Fejlec = new JTable();
        tScroll = new JScrollPane();

        mainMenu.setText("Menü");

        hozzaadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        hozzaadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        hozzaadMenu.setText("Hozzáad");
        hozzaadMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            hozzaadMenuActionPerformed(evt);
        });
        mainMenu.add(hozzaadMenu);
        mainMenu.add(new JSeparator());

        riportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        riportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report.png"))); // NOI18N
        riportMenu.setText("Riport");
        riportMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            riportMenuActionPerformed(evt);
        });
        mainMenu.add(riportMenu);
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

        tScroll.setViewportView(Fejlec);

        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp ORDER BY megnevezes");
        Comboboxtolt(Muszak,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");
        Comboboxtolt(Beosztas,"SELECT azon,megnevezes FROM beosztas ORDER BY megnevezes");
        Combokeres(Munkahely,14);

        Munkahely.addActionListener((ActionEvent e) -> {
            Comboboxtolt(Dolgozok,"SELECT dtorzs.dszam,dtorzs.nev FROM dtorzs " +
                    "INNER JOIN dolgozok ON (dtorzs.dszam = dolgozok.dszam) " +
                    "INNER JOIN kosp ON (dolgozok.munkahely = kosp.azon) " +
                    "WHERE kosp.azon = '" +
                    ComboID(Munkahely) + "' AND " +
                    "dtorzs.elment = 0 ORDER BY dtorzs.nev");
            Fejlectolt();
        });

        Muszak.addActionListener((ActionEvent e) -> {
            Fejlectolt();
        });

        listener = new MyDateListener();
        Listeners(1);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Dátum");
        JLabel Label2 = new JLabel("Műszak");
        JLabel Label3 = new JLabel("Munkahely");
        JLabel Label4 = new JLabel("Dolgozó");
        JLabel Label5 = new JLabel("Beosztás");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Datum);
        inputPane.add(Datum);

        inputPane.add(Label2);
        Label2.setLabelFor(Muszak);
        inputPane.add(Muszak);

        inputPane.add(Label3);
        Label3.setLabelFor(Munkahely);
        inputPane.add(Munkahely);

        inputPane.add(Label4);
        Label4.setLabelFor(Dolgozok);
        inputPane.add(Dolgozok);

        inputPane.add(Label5);
        Label5.setLabelFor(Beosztas);
        inputPane.add(Beosztas);

        SpringUtilities.makeCompactGrid(inputPane,
                                        5, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());
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

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void hozzaadMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            sqlupdate("INSERT INTO napitervfejlec (datum,muszak,munkahely,dszam,beosztas) VALUES ('" +
                      cfg.sqlDate(Datum.getDate()) + "','" +
                      ComboID(Muszak) + "','" +
                      ComboID(Munkahely) + "','" +
                      ComboID(Dolgozok) + "','" +
                      ComboID(Beosztas) + "')");
            conn.commit();
            conn.close();
            Fejlectolt();
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

    private void riportMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report();
    }

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (Fejlec.getSelectedRows().length > 0)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                int i = 0;
                while (i < Fejlec.getSelectedRowCount())
                {
                    sqlupdate("DELETE napitervfejlec.* FROM napitervfejlec WHERE napitervfejlec.azon = '" +
                              Fejlec.getValueAt(Fejlec.getSelectedRows()[i], 0).toString() + "'");
                    i++;
                }
                conn.commit();
                conn.close();
                Fejlectolt();
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

    private void Listeners(int valaszt)
    {
        switch (valaszt)
        {
            case 1:
                    Datum.addDateListener(listener);
                break;
            case 2:
                    try
                    {
                        Datum.removeDateListener(listener);
                    }
                    catch (Exception ex)
                    {}
                break;
        }
    }

    private void Fejlectolt()
    {
        Vector<String> tableHeaders = new Vector<>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Dátum");
        tableHeaders.add("Műszak");
        tableHeaders.add("Munkahely");
        tableHeaders.add("Dolgozószám");
        tableHeaders.add("Név");
        tableHeaders.add("Beosztás");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT napitervfejlec.azon,napitervfejlec.datum,muszakok.megnevezes," +
                     "kosp.megnevezes,napitervfejlec.dszam,dtorzs.nev,beosztas.megnevezes FROM " +
                     "napitervfejlec INNER JOIN muszakok ON (napitervfejlec.muszak = muszakok.azon) " +
                     "INNER JOIN kosp ON (napitervfejlec.munkahely = kosp.azon) " +
                     "INNER JOIN dtorzs ON (napitervfejlec.dszam = dtorzs.dszam) " +
                     "INNER JOIN beosztas ON (napitervfejlec.beosztas = beosztas.azon) " +
                     "WHERE napitervfejlec.datum = '" + cfg.sqlDate(Datum.getDate()) + "' " +
                     "AND napitervfejlec.muszak = '" +
                     ComboID(Muszak) + "' AND napitervfejlec.munkahely = '" +
                     ComboID(Munkahely) + "'");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                oneRow.add(rSet.getString(4));
                oneRow.add(rSet.getString(5));
                oneRow.add(rSet.getString(6));
                oneRow.add(rSet.getString(7));
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
        Fejlec.setModel(new NoEditModel(tableData, tableHeaders));
        Fejlec.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Fejlec.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Fejlec.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Fejlec.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
        Fejlec.setAutoCreateRowSorter(true);
        Fejlec.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        Fejlec.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
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
        while (i < combo.getItemCount() - 1 &&
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

    private void ParametersClear()
    {
        for (int i = 0;i < 10;i++)
        {
            Configuration.setParamName("", i);
            Configuration.setParamValue("", i);
        }
    }

    private void Report()
    {
        ParametersClear();
        String riportName = "reports/napitervfejlec.jasper";
        Configuration cfg = new Configuration();
        Configuration.setParamName("datum", 0);
        Configuration.setParamValue(cfg.sqlDate(Datum.getDate()),0);
        Configuration.setParamName("muszakfilter", 1);
        Configuration.setParamValue(ComboID(Muszak), 1);
        Configuration.setParamName("muszaknev", 2);
        Configuration.setParamValue(ComboVAL(Muszak), 2);
        Configuration.setParamName("uzemfilter", 3);
        Configuration.setParamValue(ComboID(Munkahely), 3);
        Configuration.setParamName("uzemnev", 4);
        Configuration.setParamValue(ComboVAL(Munkahely), 4);
        Configuration.setReportName(riportName);
        ReportDialog rdialog = new ReportDialog();
        rdialog.setVisible(true);
    }

    class MyDateListener implements DateListener
    {

        @Override
        public void dateChanged(DateEvent e)
        {
            Calendar c = e.getSelectedDate();
            if (c != null) {
                Fejlectolt();
            }
            else {
                //System.out.println("No time selected.");
            }
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private final int formID = 13;
    private JComboBox Muszak, Munkahely, Dolgozok, Beosztas;
    private JPanel inputPane, tblPane;
    private JTable Fejlec;
    private JScrollPane tScroll;
    private JCalendarCombo Datum;
    private JMenu mainMenu;
    private JMenuItem hozzaadMenu, riportMenu, torolMenu, kilepMenu;
    private JMenuBar menuBar;
    private MyDateListener listener;
}
