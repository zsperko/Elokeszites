/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

/**
 *
 * @author Forcek László
 */

import dialogs.ReportDialog;
import classes.ListItem;
import classes.NoEditModel;
import classes.ProgressRenderer;
import classes.SpringUtilities;
import classes.FormatRenderer;
import classes.JExtendedFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

public class DailyPlan extends JExtendedFrame implements TableModelListener{


    public DailyPlan()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/plan.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() - 150),
                                       (int)(defaultTool.getScreenSize().getHeight() - 150)));
        setTitle("Napiterv");
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

        Dolgozok = new JComboBox();
        Terhelestabla = new JTable();
        Gepszam = new JTable();
        Napiterv = new JTable();
        grScroll = new JScrollPane();
        tScroll = new JScrollPane();
        gjScroll = new JScrollPane();
        nScroll = new JScrollPane();
        Muszak = new JComboBox();
        Munkahely = new JComboBox();
        Datum = new JCalendarCombo();
        mainMenu = new JMenu();
        hozzaadMenu = new JMenuItem();
        modositMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        riportMenu = new JMenuItem();
        muszakTeljesit = new JMenuItem();
        menuBar = new JMenuBar();

        mainMenu.setText("Menü");

        hozzaadMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        hozzaadMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
        hozzaadMenu.setText("Hozzáad");
        hozzaadMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            hozzaadMenuActionPerformed(evt);
        });
        mainMenu.add(hozzaadMenu);

        modositMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        modositMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/change.png"))); // NOI18N
        modositMenu.setText("Módosít");
        modositMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            //modositMenuActionPerformed(evt);
        });
        mainMenu.add(modositMenu);
        mainMenu.add(new JSeparator());

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png"))); // NOI18N
        torolMenu.setText("Töröl");
        torolMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            torolMenuActionPerformed(evt);
        });
        mainMenu.add(torolMenu);
        mainMenu.add(new JSeparator());

        riportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        riportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report.png"))); // NOI18N
        riportMenu.setText("Napiterv");
        riportMenu.addActionListener((java.awt.event.ActionEvent evt) -> {
            riportMenuActionPerformed(evt);
        });
        mainMenu.add(riportMenu);
        mainMenu.add(new JSeparator());

        muszakTeljesit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        muszakTeljesit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report.png"))); // NOI18N
        muszakTeljesit.setText("Napi jelentés");
        muszakTeljesit.addActionListener((java.awt.event.ActionEvent evt) -> {
            muszakTeljesitActionPerformed(evt);
        });
        mainMenu.add(muszakTeljesit);
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

        Gepszam.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        gjScroll.setViewportView(Gepszam);

        Terhelestabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tScroll.setViewportView(Terhelestabla);

        Napiterv.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Napiterv.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        Napiterv.getTableHeader().setResizingAllowed(false);
        Napiterv.getTableHeader().setReorderingAllowed(false);

        nScroll.setViewportView(Napiterv);

        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp WHERE tervez = 1 ORDER BY megnevezes");
        Comboboxtolt(Muszak,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");
        Gepektolt();
        
        Munkahely.addActionListener((ActionEvent e) -> {
            Gepektolt();
            GRtablatolt();
            Dolgozotolt();
            Terhelestolt();
            Napitervtolt();
            Terhelestolt();
            Gepekfrissit();
        });

        Muszak.addActionListener((ActionEvent e) -> {
            Gepektolt();
            GRtablatolt();
            Napitervtolt();
            Terhelestolt();
            Dolgozotolt();
            Gepekfrissit();
        });

        Combokeres(Munkahely,14);

        listener = new MyDateListener();
        Listeners(1);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Dátum");
        JLabel Label2 = new JLabel("Műszak");
        JLabel Label3 = new JLabel("Munkahely");
        JLabel Label4 = new JLabel("Dolgozó");

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

        SpringUtilities.makeCompactGrid(inputPane,
                                4, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        grPane = new JPanel(new BorderLayout());
        grPane.setBorder(new EtchedBorder());
        grScroll.setViewportView(GRlista);
        grPane.add(grScroll);

        dbjPane = new JPanel(new BorderLayout());
        dbjPane.setBorder(new EtchedBorder());
        gjScroll.setViewportView(Gepszam);
        dbjPane.add(gjScroll);

        tPane = new JPanel(new BorderLayout());
        tPane.setBorder(new EtchedBorder());
        tScroll.setViewportView(Terhelestabla);
        tPane.add(tScroll);

        nPane = new JPanel(new BorderLayout());
        nPane.setBorder(new EtchedBorder());
        nScroll.setViewportView(Napiterv);
        nPane.add(nScroll);

        contentPane.add(inputPane);
        SplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,dbjPane,tPane);
        SplitPane1.setResizeWeight(0.5);

        SplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,grPane,nPane);
        SplitPane2.setResizeWeight(0.5);

        contentPane.add(SplitPane1);
        contentPane.add(SplitPane2);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             0,
                             SpringLayout.NORTH, contentPane);

        //SplitPane1
        layout.putConstraint(SpringLayout.WEST, SplitPane1,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, SplitPane1,
                             0,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.EAST, SplitPane1,
                             0,
                             SpringLayout.EAST, inputPane);
        layout.putConstraint(SpringLayout.SOUTH, SplitPane1,
                             0,
                             SpringLayout.SOUTH, contentPane);

        //SplitPane2
        layout.putConstraint(SpringLayout.WEST, SplitPane2,
                             0,
                             SpringLayout.EAST, inputPane);
        layout.putConstraint(SpringLayout.NORTH, SplitPane2,
                             0,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, SplitPane2,
                             0,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, SplitPane2,
                             0,
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

    private void Dolgozotolt()
    {
        Configuration cfg = new Configuration();
        Comboboxtolt(Dolgozok,"SELECT dtorzs.dszam,dtorzs.nev FROM dtorzs "
                   + "INNER JOIN dolgozok ON (dtorzs.dszam = dolgozok.dszam) "
                   + "INNER JOIN kosp ON (dolgozok.munkahely = kosp.azon) "
                   + "INNER JOIN muszakbeosztas ON (dtorzs.dszam = muszakbeosztas.dszam) "
                   + "WHERE kosp.azon = '"
                   + ComboID(Munkahely) + "' AND "
                   + "dtorzs.elment = 0 AND muszakbeosztas.datum = '"
                   + cfg.sqlDate(Datum.getDate()) + "' AND muszakbeosztas.muszak = '"
                   + ComboID(Muszak) + "' "
                   + "ORDER BY dtorzs.nev");
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

    private void hozzaadMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (Gepszam.getSelectedRow() != -1)
        {
            if (!Gepszamkeres())
            {
                Configuration cfg = new Configuration();
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    sqlupdate("INSERT INTO napiterv (napiterv.datum,napiterv.muszak,napiterv.dszam,"
                            + "napiterv.grmuvazon,napiterv.muhelyazon,napiterv.mennyiseg) "
                            + "SELECT tervezotabla.datum,tervezotabla.muszak,'"
                            + ComboID(Dolgozok) + "',tervezotabla.grmuvazon,"
                            + "tervezotabla.gepazon,tervezotabla.mennyiseg FROM tervezotabla "
                            + "WHERE tervezotabla.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND "
                            + "tervezotabla.muszak = '" + ComboID(Muszak)
                            + "' AND tervezotabla.gepazon = '"
                            + Gepszam.getValueAt(Gepszam.getSelectedRow(), 0).toString() + "'");
                    conn.close();
                    Napitervtolt();
                    Terhelestolt();
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
                JOptionPane.showMessageDialog(null,"A megadott gépszám már szerepel a napiterv listában!");
            }
        }
    }

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlupdate("DELETE napiterv.* FROM napiterv WHERE napiterv.datum = '"
                    + cfg.sqlDate(Datum.getDate()) + "' AND napiterv.muszak = '"
                    + ComboID(Muszak)
                    + "' AND napiterv.muhelyazon = '"
                    + Gepszam.getValueAt(Gepszam.getSelectedRow(), 0).toString() + "'");
            conn.close();
            Napitervtolt();
            Terhelestolt();
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
        Configuration cfg = new Configuration();
        Date date = Datum.getDate();
        String nextdate;
        try
        {
            String tableName = "temp_napiterv";
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT min(napiterv.datum) AS nextdate FROM napiterv WHERE napiterv.datum > '"
                   + cfg.sqlDate(date) + "' ");
            if (rSet.next())
            {
                rSet.first();
                nextdate = rSet.getString(1);
                sqlolvas("SHOW TABLES LIKE '" + tableName + "'");
                if (rSet.next())
                {
                    sqlupdate("DROP TABLE " + tableName + "");
                }
                sqlupdate("CREATE TABLE " + tableName + " ("
                        + "grmuvazon int(11) NOT NULL,"
                        + "muhelyazon int(11) NOT NULL,"
                        + "de double(15,3) NOT NULL DEFAULT '0.000',"
                        + "du double(15,3) NOT NULL DEFAULT '0.000',"
                        + "ej double(15,3) NOT NULL DEFAULT '0.000') "
                        + "ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;");
                sqlupdate("INSERT INTO " + tableName + " (grmuvazon,muhelyazon) SELECT "
                        + "napiterv.grmuvazon,napiterv.muhelyazon FROM napiterv "
                        + "WHERE napiterv.datum = '" + cfg.sqlDate(date) + "' AND napiterv.muszak <> 1 "
                        + "GROUP BY grmuvazon,muhelyazon");
                sqlupdate("INSERT INTO " + tableName + " (grmuvazon,muhelyazon) SELECT napiterv.grmuvazon,"
                        + "napiterv.muhelyazon FROM napiterv "
                        + "LEFT OUTER JOIN temp_napiterv ON (napiterv.grmuvazon = temp_napiterv.grmuvazon) AND "
                        + "(napiterv.muhelyazon = temp_napiterv.muhelyazon) WHERE napiterv.datum = '"
                        + nextdate + "' AND napiterv.muszak = 1 AND isnull(temp_napiterv.grmuvazon) "
                        + "GROUP BY napiterv.grmuvazon,napiterv.muhelyazon");
                sqlupdate("UPDATE " + tableName + " INNER JOIN (SELECT sum(napiterv.mennyiseg) AS mennyiseg,"
                        + "napiterv.grmuvazon,napiterv.muhelyazon FROM napiterv WHERE napiterv.datum = '"
                        + cfg.sqlDate(date) + "' AND napiterv.muszak = 2 GROUP BY napiterv.grmuvazon,"
                        + "napiterv.muhelyazon) seged ON (" + tableName + ".grmuvazon = seged.grmuvazon) "
                        + "AND (" + tableName + ".muhelyazon = seged.muhelyazon) SET "
                        + tableName + ".du = seged.mennyiseg");
                sqlupdate("UPDATE " + tableName + " INNER JOIN (SELECT sum(napiterv.mennyiseg) AS mennyiseg,"
                        + "napiterv.grmuvazon,napiterv.muhelyazon FROM napiterv WHERE napiterv.datum = '"
                        + cfg.sqlDate(date) + "' AND napiterv.muszak = 3 GROUP BY napiterv.grmuvazon,"
                        + "napiterv.muhelyazon) seged ON (" + tableName + ".grmuvazon = seged.grmuvazon) "
                        + "AND (" + tableName + ".muhelyazon = seged.muhelyazon) SET "
                        + tableName + ".ej = seged.mennyiseg");
                sqlupdate("UPDATE " + tableName + " INNER JOIN (SELECT sum(napiterv.mennyiseg) AS mennyiseg,"
                        + "napiterv.grmuvazon,napiterv.muhelyazon FROM napiterv WHERE napiterv.datum = '"
                        + nextdate + "' AND napiterv.muszak = 1 GROUP BY napiterv.grmuvazon,"
                        + "napiterv.muhelyazon) seged ON (" + tableName + ".grmuvazon = seged.grmuvazon) "
                        + "AND (" + tableName + ".muhelyazon = seged.muhelyazon) SET "
                        + tableName + ".de = seged.mennyiseg");
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
        Report("reports/napiterv.jasper",cfg.sqlDate(Datum.getDate()));
    }

    private void muszakTeljesitActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        Date date = Datum.getDate();
        try
        {
            String tableName = "muszakteljesit";
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SHOW TABLES LIKE '" + tableName + "'");
            if (rSet.next())
            {
                sqlupdate("DROP TABLE " + tableName + "");
            }
            sqlupdate("CREATE TABLE " + tableName + " ("
                    + "gr_muvazon int(11) NOT NULL DEFAULT '0',"
                    + "grszam char(10) COLLATE utf8_hungarian_ci NOT NULL,"
                    + "cikkszam char(40) COLLATE utf8_hungarian_ci NOT NULL,"
                    + "terv double(10,2) NOT NULL DEFAULT '0.00',"
                    + "teny double(10,2) NOT NULL DEFAULT '0.00',"
                    + "selejt double(10,2) NOT NULL DEFAULT '0.00',"
                    + "PRIMARY KEY (gr_muvazon),"
                    + "UNIQUE KEY grszam (grszam),"
                    + "UNIQUE KEY gr_muvazon (gr_muvazon)) "
                    + "ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;");
            sqlupdate("INSERT INTO " + tableName + " (gr_muvazon,grszam,cikkszam,terv) SELECT "
                    + "gr_muveletterv.azon,gr_muveletterv.grszam,"
                    + "gyartasirend.cikkszam,sum(napiterv.mennyiseg) AS mennyiseg "
                    + "FROM napiterv INNER JOIN gr_muveletterv ON (napiterv.grmuvazon = gr_muveletterv.azon) "
                    + "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) "
                    + "WHERE napiterv.datum = '" + cfg.sqlDate(date) + "' "
                    + "GROUP BY gr_muveletterv.grszam,gr_muveletterv.muveletkapcsolo,gyartasirend.cikkszam");
            sqlupdate("UPDATE " + tableName + " "
                    + "INNER JOIN gr_muveletterv ON (muszakteljesit.gr_muvazon = gr_muveletterv.azon) "
                    + "INNER JOIN (SELECT visszajelent.grszam,visszajelent.muvszam,sum(visszajelent.mennyiseg) AS teny,"
                    + "sum(visszajelent.selejt) AS selejt FROM visszajelent WHERE visszajelent.datum = '"
                    + cfg.sqlDate(date) + "' GROUP BY visszajelent.grszam,visszajelent.muvszam) seged "
                    + "ON (gr_muveletterv.grszam = seged.grszam) AND (gr_muveletterv.muveletkapcsolo = seged.muvszam) "
                    + "SET " + tableName + ".teny = seged.teny," + tableName + ".selejt = seged.selejt");
            sqlupdate("INSERT INTO " + tableName + " (gr_muvazon,grszam,cikkszam,teny,selejt) SELECT "
                    + "gr_muveletterv.azon,gr_muveletterv.grszam,gyartasirend.cikkszam,"
                    + "sum(visszajelent.mennyiseg) AS teny,sum(visszajelent.selejt) AS selejt "
                    + "FROM visszajelent INNER JOIN gr_muveletterv ON (visszajelent.grszam = gr_muveletterv.grszam) "
                    + "AND (visszajelent.muvszam = gr_muveletterv.muveletkapcsolo) "
                    + "INNER JOIN muhelyek ON (gr_muveletterv.muhelykod = muhelyek.szam) "
                    + "LEFT OUTER JOIN muszakteljesit ON (gr_muveletterv.azon = muszakteljesit.gr_muvazon) "
                    + "INNER JOIN gyartasirend ON (visszajelent.grszam = gyartasirend.rendszam) "
                    + "WHERE isnull(muszakteljesit.gr_muvazon) AND muhelyek.ktghely = '"
                    + ComboID(Munkahely) + "' AND "
                    + "visszajelent.datum = '" + cfg.sqlDate(date) + "' GROUP BY "
                    + "gr_muveletterv.azon,gr_muveletterv.grszam,gyartasirend.cikkszam");
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
        Report("reports/muszakteljesit.jasper",cfg.sqlDate(date));
    }


    private boolean Gepszamkeres()
    {
        if (Napiterv.getRowCount() > 0)
        {
            int i = 0;
            while (i < Napiterv.getRowCount())
            {
                if (Napiterv.getValueAt(i, 6).toString().compareTo(
                    Gepszam.getValueAt(Gepszam.getSelectedRow(), 1).toString()) == 0) return true;
                i++;
            }
        }
        return false;
    }

    private void Gepektolt()
    {
        Gepszam = null;
        Gepszam = new JTable();
        gjScroll.setViewportView(Gepszam);
        String[] tableHeaders = {"Azon","Gépszám","Név","Hozzárendelt"};
        Object[][] tableData = new Object[0][0];
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT muhelyek.azon,muhelyek.szam,muhelyek.megnevezes,0 FROM tervezotabla "
                   + "INNER JOIN muhelyek ON (tervezotabla.gepazon = muhelyek.azon) WHERE "
                   + "muhelyek.ktghely = '"
                   + ComboID(Munkahely) + "' AND "
                   + "tervezotabla.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND "
                   + "tervezotabla.muszak = '"
                   + ComboID(Muszak) + "' "
                   + "GROUP BY muhelyek.azon,muhelyek.szam ORDER BY muhelyek.szam");
            rSet.last();
            int count = rSet.getRow();
            rSet.beforeFirst();
            tableData = new Object[count][tableHeaders.length];
            while(rSet.next())
            {
                tableData[rSet.getRow()-1][0] = rSet.getString(1);
                tableData[rSet.getRow()-1][1] = rSet.getString(2);
                tableData[rSet.getRow()-1][2] = rSet.getString(3);
                tableData[rSet.getRow()-1][3] = rSet.getInt(4);
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
        Gepszam.setModel(new DefaultTableModel(tableData, tableHeaders)
        {
            Class[] types = new Class [] {
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class
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
        Gepszam.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        GepekTablelistener = new GepekSelectionListener(Gepszam);
        Gepszam.getSelectionModel().addListSelectionListener(GepekTablelistener);

        TableColumnModel tcm = Gepszam.getColumnModel();
        for (int i = 0;i < 4;i++)
        {
            tcm.getColumn(i).setCellRenderer(new ColoredCellRenderer());
        }
        
        Gepszam.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Gepszam.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Gepszam.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Gepszam.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

        Gepszam.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
        Gepszam.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
        Gepszam.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(0);
        Gepszam.getTableHeader().getColumnModel().getColumn(3).setResizable(false);

        Gepszam.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private void Napitervtolt()
    {
        Napiterv = null;
        Napiterv = new JTable();
        nScroll.setViewportView(Napiterv);
        String[] tableHeaders = {"Azon","Dolgozószám","Név","Gyártási rendelés",
                                 "Cikkszám","Művelet","Gépszám","Gépnév","Mennyiség"};
        Object[][] tableData = new Object[0][0];
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT napiterv.azon,dtorzs.dszam,dtorzs.nev,gyartasirend.rendszam," +
                     "gyartasirend.cikkszam,gr_muveletterv.megnevezes AS muvelet," +
                     "muhelyek.szam,muhelyek.megnevezes AS muhelynev,napiterv.mennyiseg " +
                     "FROM napiterv INNER JOIN dtorzs ON (napiterv.dszam = dtorzs.dszam) " +
                     "INNER JOIN gr_muveletterv ON (napiterv.grmuvazon = gr_muveletterv.azon) " +
                     "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) " +
                     "INNER JOIN muhelyek ON (napiterv.muhelyazon = muhelyek.azon) " +
                     "WHERE napiterv.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND " +
                     "napiterv.muszak = '" + ComboID(Muszak) + "' AND " +
                     "muhelyek.ktghely = '" + ComboID(Munkahely) + "' " +
                     "ORDER BY dtorzs.nev ASC");
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
                tableData[rSet.getRow()-1][5] = rSet.getString(6);
                tableData[rSet.getRow()-1][6] = rSet.getString(7);
                tableData[rSet.getRow()-1][7] = rSet.getString(8);
                int value = (int)rSet.getDouble(9);
                tableData[rSet.getRow()-1][8] = value;
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
        Napiterv.setModel(new DefaultTableModel(tableData, tableHeaders)
        {
            Class[] types = new Class [] {
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
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
        Napiterv.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(0).setResizable(false);

        Napiterv.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(1).setResizable(false);

        Napiterv.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(5).setPreferredWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(5).setResizable(false);

        Napiterv.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(7).setPreferredWidth(0);
        Napiterv.getTableHeader().getColumnModel().getColumn(7).setResizable(false);

        Napiterv.setAutoCreateRowSorter(true);

        Napiterv.getModel().addTableModelListener(this);

        TableColumnModel tcm = Napiterv.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.RIGHT);
        tcm.getColumn(7).setCellRenderer(render);
    }

    private void Terhelestolt()
    {
        Vector<String> tableHeaders = new Vector<>();
        Vector tableData = new Vector();
        tableHeaders.add("Dolgozószám");
        tableHeaders.add("Név");
        tableHeaders.add("Százalék");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT dtorzs.dszam,dtorzs.nev," +
                     "(sum((gr_muveletterv.szemelyido * napiterv.mennyiseg)) / 480) AS terheles " +
                     "FROM napiterv INNER JOIN dtorzs ON (napiterv.dszam = dtorzs.dszam) " +
                     "INNER JOIN gr_muveletterv ON (napiterv.grmuvazon = gr_muveletterv.azon) " +
                     "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) " +
                     "INNER JOIN muhelyek ON (napiterv.muhelyazon = muhelyek.azon) " +
                     "WHERE napiterv.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND " +
                     "napiterv.muszak = '" + ComboID(Muszak) + "' AND " +
                     "muhelyek.ktghely = '" + ComboID(Munkahely) + "' " +
                     "GROUP BY dtorzs.dszam,dtorzs.nev");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                double szazalek = rSet.getDouble(3) * 100;
                int szazalekint = (int)szazalek;
                ProgressRenderer progressBar = new ProgressRenderer();
                progressBar.setMaximum(200);
                progressBar.setBorderPainted(true);
                progressBar.setValue(szazalekint);
                if (szazalekint > 100)
                {
                    progressBar.setForeground(Color.RED);
                }
                else
                {
                    progressBar.setForeground(Color.GREEN);
                }
                progressBar.setBackground(Color.WHITE);
                progressBar.setStringPainted(true);
                progressBar.setString(Integer.toString(szazalekint) + " %");
                BasicProgressBarUI bui = new BasicProgressBarUI() {
                    @Override
                    protected Color getSelectionBackground() {
                        return Color.black; // string color over the background
                    }
                    @Override
                    protected Color getSelectionForeground() {
                        return Color.black; // string color over the foreground
                    }
                };
                progressBar.setUI(bui);
                oneRow.add(progressBar);
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
        Terhelestabla.setModel(new NoEditModel(tableData, tableHeaders));
        Terhelestabla.setAutoCreateRowSorter(true);
        Terhelestabla.getColumnModel().getColumn(2).setCellRenderer(new ProgressRenderer());
        Terhelestabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private void Comboboxtolt(JComboBox combo, String sqlcmd)
    {
        Configuration cfg = new Configuration();
        try
        {
            combo.removeAllItems();
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
        while (i < combo.getItemCount() - 1 &&
               ((ListItem)combo.getItemAt(i)).getValue() != ertek)
        {
            i++;
        }
        combo.setSelectedIndex(i);
    }

    private void GRtablatolt()
    {
        GRlista = null;
        GRlista = new JTable();
        grScroll.setViewportView(GRlista);
        Configuration cfg = new Configuration();
        if (Gepszam.getSelectedRow() != -1)
        {
            String[] tableHeaders = {"Gyártási rendelés","Műveletsorszám","Műveletterv azon","Cikkszám",
                                     "Művelet","Muhelyszam","Mennyiség"};
            Object[][] tableData = new Object[0][0];
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                String sqlCmd;
                sqlCmd = "SELECT gr_muveletterv.grszam,gr_muveletterv.muveletkapcsolo,gr_muveletterv.azon,"
                       + "gyartasirend.cikkszam,gr_muveletterv.megnevezes,gr_muveletterv.muhelykod,"
                       + "tervezotabla.mennyiseg FROM gr_muveletterv "
                       + "INNER JOIN tervezotabla ON (gr_muveletterv.azon = tervezotabla.grmuvazon) "
                       + "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) "
                       + "WHERE tervezotabla.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND "
                       + "tervezotabla.muszak = '" + ComboID(Muszak) + "' AND "
                       + "tervezotabla.gepazon = '" + Gepszam.getValueAt(Gepszam.getSelectedRow(), 0).toString() + "'";
                if (sqlCmd.compareTo("") != 0)
                {
                    sqlCmd += "ORDER BY gyartasirend.rendszam,gr_muveletterv.muveletkapcsolo";
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
                        tableData[rSet.getRow()-1][3] = rSet.getString(4);
                        tableData[rSet.getRow()-1][4] = rSet.getString(5);
                        tableData[rSet.getRow()-1][5] = rSet.getString(6);
                        int value = (int)rSet.getDouble(7);
                        tableData[rSet.getRow()-1][6] = value;
                    }
                }
                conn.close();
                GRlista.setModel(new DefaultTableModel(tableData, tableHeaders)
                {
                    Class[] types = new Class [] {
                        java.lang.Object.class,
                        java.lang.Object.class,
                        java.lang.Object.class,
                        java.lang.Object.class,
                        java.lang.Object.class,
                        java.lang.Object.class,
                        java.lang.Object.class
                    };
                    boolean[] canEdit = new boolean [] {
                        false, false, false, false, false, false, false
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

                GRlista.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(1).setResizable(false);

                GRlista.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(2).setResizable(false);

                GRlista.getTableHeader().getColumnModel().getColumn(5).setMaxWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(5).setMinWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(5).setPreferredWidth(0);
                GRlista.getTableHeader().getColumnModel().getColumn(5).setResizable(false);

                GRTablelistener = new GRSelectionListener(GRlista);
                GRlista.getSelectionModel().addListSelectionListener(GRTablelistener);
                GRlista.getModel().addTableModelListener(this);

                GRlista.setAutoCreateRowSorter(true);

                GRlista.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

                TableColumnModel tcm = GRlista.getColumnModel();
                NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
                FormatRenderer render = new FormatRenderer(formatter);
                render.setHorizontalAlignment(SwingConstants.CENTER);
                tcm.getColumn(1).setCellRenderer(render);
                render = new FormatRenderer(formatter);
                render.setHorizontalAlignment(SwingConstants.LEFT);
                tcm.getColumn(2).setCellRenderer(render);
                GRlista.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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
            String[] tableHeaders = {"Gyártási rendelés","Cikkszám",
                                     "Művelet","Mennyiség"};
            Object[][] tableData = new Object[0][0];
            GRlista.setModel(new DefaultTableModel(tableData, tableHeaders));
        }
    }

    private void Gepekfrissit()
    {
        for (int i = 0;i < Gepszam.getRowCount();i++)
        {
            String value = Gepszam.getValueAt(i, 1).toString();
            int j = 0;
            while (j < Napiterv.getRowCount())
            {
                if(value.compareTo(Napiterv.getValueAt(j, 6).toString()) == 0)
                {
                    Gepszam.setValueAt(1, i, 3);
                    break;
                }
                j++;
            }
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

    private void Report(String reportName, String datum)
    {
        Configuration cfg = new Configuration();
        ParametersClear();
        if (reportName.compareTo("reports/napiterv.jasper") == 0)
        {
//            cfg.setParamName("datum", 0);
//            cfg.setParamValue(datum,0);
//            cfg.setParamName("muszakfilter", 1);
//            cfg.setParamValue(mszLista.elementAt(Muszak), 1);
//            cfg.setParamName("muszaknev", 2);
//            cfg.setParamValue(mszLista.elementAt(Muszak.getSelectedIndex()).getValue(), 2);
//            cfg.setParamName("uzemfilter", 3);
//            cfg.setParamValue(mLista.elementAt(Munkahely), 3);
//            cfg.setParamName("uzemnev", 4);
//            cfg.setParamValue(mLista.elementAt(Munkahely.getSelectedIndex()).getValue(), 4);
            Configuration.setParamName("datum", 0);
            Configuration.setParamValue(datum,0);
            Configuration.setParamName("uzemfilter", 1);
            Configuration.setParamValue(Integer.toString(ComboID(Munkahely)), 1);
            Configuration.setParamName("uzemnev", 2);
            Configuration.setParamValue(ComboVAL(Munkahely), 2);
        }
        else
        {
            Configuration.setParamName("datum", 0);
            Configuration.setParamValue(datum,0);
            Configuration.setParamName("user", 1);
            Configuration.setParamValue(cfg.getcurrentUserName(),1);
        }
        Configuration.setReportName(reportName);
        ReportDialog rdialog = new ReportDialog();
        rdialog.setVisible(true);
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

        @Override
        public void dateChanged(DateEvent e)
        {
            Calendar c = e.getSelectedDate();
            if (c != null) {
                Gepektolt();
                Napitervtolt();
                Terhelestolt();
                Dolgozotolt();
                GRtablatolt();
                Gepekfrissit();
            }
            else
            {
                //System.out.println("No time selected.");
            }
        }
    }

    class GRSelectionListener implements ListSelectionListener {
        JTable table;

        GRSelectionListener(JTable table) {
            this.table = table;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == table.getSelectionModel()
                  && table.getRowSelectionAllowed()) {

            }
            else if (e.getSource() == table.getColumnModel().getSelectionModel()
                   && table.getColumnSelectionAllowed() ){
            }
            if (e.getValueIsAdjusting()) {
            }
        }
    }

    class GepekSelectionListener implements ListSelectionListener {
        JTable table;

        GepekSelectionListener(JTable table) {
            this.table = table;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == table.getSelectionModel()
                  && table.getRowSelectionAllowed()) {
                GRtablatolt();
            }
            else if (e.getSource() == table.getColumnModel().getSelectionModel()
                   && table.getColumnSelectionAllowed() ){
            }
            if (e.getValueIsAdjusting()) {
            }
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        Object data = Napiterv.getValueAt(Napiterv.getSelectedRow(),8);
        double value = Double.parseDouble(data.toString());
        UpdateChanges(Napiterv.getValueAt(Napiterv.getSelectedRow(), 0).toString(),value);
    }

    private void UpdateChanges(String index, double value)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlupdate("UPDATE napiterv SET mennyiseg = '" + value + "' " +
                      "WHERE azon = '" + index + "'");
            conn.commit();
            conn.close();
            Terhelestolt();
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

    public class ColoredCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                                              isSelected, hasFocus,
                                              row, column);
            int Hozzarendelt = (Integer)table.getValueAt(row, 3);

            if (isSelected)
            {
                c.setBackground(new Color(239,199,0));
            }
            else if(Hozzarendelt == 1)
            {
                c.setBackground(Color.GREEN);
            }
            else
            {
                c.setBackground(Color.WHITE);
            }
            return c;
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private final int formID = 6;
    private JPanel inputPane, grPane, dbjPane, tPane, nPane;
    private JComboBox Muszak, Munkahely, Dolgozok;
    private JTable GRlista, Terhelestabla, Gepszam, Napiterv;
    private JScrollPane grScroll, tScroll, gjScroll, nScroll;
    private JCalendarCombo Datum;
    private JMenu mainMenu;
    private JMenuItem hozzaadMenu, modositMenu, torolMenu, kilepMenu, riportMenu, muszakTeljesit;
    private JMenuBar menuBar;
    private JSplitPane SplitPane1,SplitPane2;
    private MyDateListener listener;
    private GRSelectionListener GRTablelistener;
    private GepekSelectionListener GepekTablelistener;
}