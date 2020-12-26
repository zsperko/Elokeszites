/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import dialogs.ReportDialog;
import dialogs.AnimatedDialog;
import classes.ListItem;
import classes.NoEditModel;
import classes.ProgressRenderer;
import classes.SpringUtilities;
import classes.FormatRenderer;
import classes.JExtendedFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.TableColumnModel;
import org.freixas.jcalendar.DateEvent;
import org.freixas.jcalendar.DateListener;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class TimeRecording extends JExtendedFrame{

    public TimeRecording()
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
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() - 150),
                                       (int)(defaultTool.getScreenSize().getHeight() - 150)));
        setformID(formID);
        setTitle("Visszajelentések");
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

        Munkahely = new JComboBox();
        mainMenu = new JMenu();
        xlsMenu = new JMenuItem();
        riportMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();
        Datum = new JCalendarCombo();
        Visszajelent = new JTable();
        vScroll = new JScrollPane();
        Terhelestabla = new JTable();
        tScroll = new JScrollPane();

        mainMenu.setText("Menü");
        menuBar.setLayout(new BorderLayout());

        xlsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        xlsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png"))); // NOI18N
        xlsMenu.setText("Excel mentés");
        xlsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xlsMenuActionPerformed(evt);
            }
        });
        mainMenu.add(xlsMenu);
        mainMenu.add(new JSeparator());

        riportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        riportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report.png"))); // NOI18N
        riportMenu.setText("Riport");
        riportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                riportMenuActionPerformed(evt);
            }
        });
        mainMenu.add(riportMenu);
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
        Visszajelent.setSize(Visszajelent.getWidth(), Visszajelent.getHeight() - 100);
        vScroll.setViewportView(Visszajelent);
        tScroll.setViewportView(Terhelestabla);

        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp ORDER BY megnevezes");

        Munkahely.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        Combokeres(Munkahely,14);

        listener = new MyDateListener();
        Listeners(1);
        Teljesitmenytolt();
        Visszajelenttolt();

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Dátum");
        JLabel Label2 = new JLabel("Munkahely");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Datum);
        inputPane.add(Datum);

        inputPane.add(Label2);
        Label2.setLabelFor(Munkahely);
        inputPane.add(Munkahely);

        SpringUtilities.makeCompactGrid(inputPane,
                                        2, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());
        tblPane.add(vScroll);

        tPane = new JPanel(new BorderLayout());
        tPane.setBorder(new EtchedBorder());
        tPane.add(tScroll);

        SplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,tPane,tblPane);
        SplitPane.setResizeWeight(0.5);

        contentPane.add(inputPane);
        contentPane.add(SplitPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        //tPane
        layout.putConstraint(SpringLayout.WEST, SplitPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, SplitPane,
                             1,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.EAST, SplitPane,
                             1,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, SplitPane,
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

    private void Visszajelenttolt()
    {
        Configuration cfg = new Configuration();
        tableData = new Vector();
        Vector<String> tableHeaders = new Vector<String>();
        tableHeaders.add("Név");
        tableHeaders.add("Gyártási rendelés");
        tableHeaders.add("Cikkszám");
        tableHeaders.add("Megnevezés");
        tableHeaders.add("Terv mennyiség");
        tableHeaders.add("Jó mennyiség");
        tableHeaders.add("Selejt");
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT dtorzs.nev,teljesitmenyek.grszam,gyartasirend.cikkszam,"
                   + "gr_muveletterv.megnevezes,"
                   + "sum(teljesitmenyek.tervmennyiseg) AS terv,"
                   + "sum(teljesitmenyek.mennyiseg) AS teny,"
                   + "sum(teljesitmenyek.selejt) AS selejt FROM teljesitmenyek "
                   + "INNER JOIN dolgozok ON (teljesitmenyek.dszam = dolgozok.dszam) "
                   + "INNER JOIN dtorzs ON (teljesitmenyek.dszam = dtorzs.dszam) "
                   + "INNER JOIN gyartasirend ON (teljesitmenyek.grszam = gyartasirend.rendszam) "
                   + "INNER JOIN gr_muveletterv ON (teljesitmenyek.grszam = gr_muveletterv.grszam) AND "
                   + "(teljesitmenyek.muvszam = gr_muveletterv.muveletkapcsolo) WHERE "
                   + "teljesitmenyek.datum = '" + cfg.sqlDate(Datum.getDate()) + "' AND "
                   + "dolgozok.munkahely = '" + ComboID(Munkahely) + "' "
                   + "GROUP BY teljesitmenyek.dszam,teljesitmenyek.grszam,"
                   + "teljesitmenyek.datum,dtorzs.nev,gyartasirend.cikkszam,gr_muveletterv.megnevezes");
            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                oneRow.add(rSet.getString(4));
                int value = (int)rSet.getDouble(5);
                oneRow.add(value);
                value = (int)rSet.getDouble(6);
                oneRow.add(value);
                value = (int)rSet.getDouble(7);
                oneRow.add(value);
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
        Visszajelent.setModel(new NoEditModel(tableData, tableHeaders));

        TableColumnModel tcm = Visszajelent.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.RIGHT);
        tcm.getColumn(4).setCellRenderer(render);
        tcm.getColumn(5).setCellRenderer(render);
        tcm.getColumn(6).setCellRenderer(render);
        
        Visszajelent.setAutoCreateRowSorter(true);
        Visszajelent.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        Visszajelent.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void Teljesitmenytolt()
    {
        Vector<String> tableHeaders = new Vector<String>();
        tableDataP = new Vector();
        tableHeaders.add("Dolgozószám");
        tableHeaders.add("Név");
        tableHeaders.add("Műszak");
        tableHeaders.add("Terv százalék");
        tableHeaders.add("Tény százalék");
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT dszam,nev,muszak,terv,teny FROM ("
                   + "SELECT teljesitmenyek.dszam,dtorzs.nev,seged.megnevezes as muszak,"
                   + "(sum((teljesitmenyek.tervmennyiseg * gr_muveletterv.szemelyido)) / 480) AS terv,"
                   + "(sum((teljesitmenyek.mennyiseg * gr_muveletterv.szemelyido)) / 480) AS teny "
                   + "FROM teljesitmenyek INNER JOIN dtorzs ON (teljesitmenyek.dszam = dtorzs.dszam) "
                   + "INNER JOIN gr_muveletterv ON (teljesitmenyek.grszam = gr_muveletterv.grszam) AND "
                   + "(teljesitmenyek.muvszam = gr_muveletterv.muveletkapcsolo) INNER JOIN "
                   + "(SELECT muszakbeosztas.dszam,muszakok.megnevezes FROM muszakbeosztas "
                   + "INNER JOIN dolgozok ON (muszakbeosztas.dszam = dolgozok.dszam) "
                   + "INNER JOIN muszakok ON (muszakbeosztas.muszak = muszakok.azon) WHERE "
                   + "dolgozok.munkahely = '" + ComboID(Munkahely) + "' "
                   + "AND muszakbeosztas.datum = '" + cfg.sqlDate(Datum.getDate()) + "') "
                   + "seged ON (teljesitmenyek.dszam = seged.dszam) WHERE "
                   + "teljesitmenyek.datum = '" + cfg.sqlDate(Datum.getDate()) + "' "
                   + "GROUP BY "
                   + "teljesitmenyek.dszam,dtorzs.nev,seged.megnevezes ORDER BY "
                   + "seged.megnevezes,dtorzs.nev) seged");

            while(rSet.next())
            {
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(rSet.getString(1));
                oneRow.add(rSet.getString(2));
                oneRow.add(rSet.getString(3));
                double terv = rSet.getDouble(4) * 100;
                double teny = rSet.getDouble(5) * 100;
                int szazalekint = (int)terv;
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

                szazalekint = (int)teny;
                ProgressRenderer progressBar2 = new ProgressRenderer();
                progressBar2.setMaximum(200);
                progressBar2.setBorderPainted(true);
                progressBar2.setValue(szazalekint);
                if (terv > teny)
                {
                    progressBar2.setForeground(Color.RED);
                }
                else
                {
                    progressBar2.setForeground(Color.GREEN);
                }
                progressBar2.setBackground(Color.WHITE);
                progressBar2.setStringPainted(true);
                progressBar2.setString(Integer.toString(szazalekint) + " %");
                BasicProgressBarUI bui2 = new BasicProgressBarUI() {
                    @Override
                    protected Color getSelectionBackground() {
                        return Color.black; // string color over the background
                    }
                    @Override
                    protected Color getSelectionForeground() {
                        return Color.black; // string color over the foreground
                    }
                };
                progressBar2.setUI(bui2);
                oneRow.add(progressBar2);
                tableDataP.add(oneRow);
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
        Terhelestabla.setModel(new NoEditModel(tableDataP, tableHeaders));
        try
        {
            Terhelestabla.setAutoCreateRowSorter(true);
        }
        catch (Exception ex){}
        Terhelestabla.getColumnModel().getColumn(3).setCellRenderer(new ProgressRenderer());
        Terhelestabla.getColumnModel().getColumn(4).setCellRenderer(new ProgressRenderer());
        Terhelestabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }

    private void xlsMenuActionPerformed(java.awt.event.ActionEvent evt) {
        int valaszt = JOptionPane.showConfirmDialog(null, "Biztosan menti az adatokat Excel fájlba?", "Mentés megerősítés", JOptionPane.YES_NO_OPTION);
        if (valaszt == 0)
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
                MessageList.add("Mentés folyamatban...");
                AnimatedDialog form = new AnimatedDialog(MessageList);
                Calendar cal = Calendar.getInstance();
                int year = 0;
                int month = 0;
                int day = 0;
                try
                {
                    cal.setTime(Datum.getDate());
                    year = cal.get(Calendar.YEAR);
                    month = cal.get(Calendar.MONTH) + 1;
                    day = cal.get(Calendar.DATE);
                }
                catch (Exception e){}
                SaveTimeRecToExcel saveFile = new SaveTimeRecToExcel(Integer.toString(year),
                                              Integer.toString(month),Integer.toString(day),
                                              Integer.toString(ComboID(Munkahely)),
                                              fileChooser.getSelectedFile().getAbsolutePath());
                Thread thread;
                thread = new Thread(saveFile);
                thread.start();
                form.setVisible(true);
            }
        }
    }

    private void riportMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report();
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
        String riportName = "reports/teljesitmenyek.jasper";
        Configuration cfg = new Configuration();
        Configuration.setParamName("datum", 0);
        Configuration.setParamValue(cfg.sqlDate(Datum.getDate()),0);
        Configuration.setParamName("user", 1);
        Configuration.setParamValue(cfg.getcurrentUserName(), 1);
        Configuration.setReportName(riportName);
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

        public void dateChanged(DateEvent e)
        {
            Calendar c = e.getSelectedDate();
            if (c != null) {
                Teljesitmenytolt();
                Visszajelenttolt();
            }
            else {
                //System.out.println("No time selected.");
            }
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 14;
    private JComboBox Munkahely;
    private JPanel inputPane, tblPane, tPane;
    private JTable Visszajelent,Terhelestabla;
    private JScrollPane vScroll,tScroll;
    private JCalendarCombo Datum;
    private JMenu mainMenu;
    private JMenuItem xlsMenu,riportMenu,kilepMenu;
    private JMenuBar menuBar;
    private Vector tableData, tableDataP;
    private JSplitPane SplitPane;
    private MyDateListener listener;
}
