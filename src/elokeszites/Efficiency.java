/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Container;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class Efficiency extends JExtendedFrame{


    public Efficiency()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/efficiency.png"));
        setFrameIcon(icon);
        setPreferredSize(new Dimension(300,200));
        setTitle("Műszakjelentés");
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

        ShiftFrom = new JComboBox();
        ShiftTo = new JComboBox();
        StartDate = new JCalendarCombo();
        EndDate = new JCalendarCombo();
        mainMenu = new JMenu();
        reportMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();

        mainMenu.setText("Menü");

        reportMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        reportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/report.png")));
        reportMenu.setText("Riport");
        reportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportMenuActionPerformed(evt);
            }
        });
        mainMenu.add(reportMenu);
        mainMenu.add(new JSeparator());

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/close.png"))); // NOI18N
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);
        menuBar.add(mainMenu);
        setJMenuBar(menuBar);

        Comboboxtolt(ShiftFrom,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");
        Comboboxtolt(ShiftTo,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Start dátum");
        JLabel Label2 = new JLabel("Végdátum");
        JLabel Label3 = new JLabel("Műszaktól");
        JLabel Label4 = new JLabel("Műszakig");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(StartDate);
        inputPane.add(StartDate);

        inputPane.add(Label2);
        Label2.setLabelFor(EndDate);
        inputPane.add(EndDate);

        inputPane.add(Label3);
        Label3.setLabelFor(ShiftFrom);
        inputPane.add(ShiftFrom);

        inputPane.add(Label4);
        Label4.setLabelFor(ShiftTo);
        inputPane.add(ShiftTo);

        SpringUtilities.makeCompactGrid(inputPane,
                                4, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        contentPane.add(inputPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             0,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             0,
                             SpringLayout.EAST, inputPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             0,
                             SpringLayout.SOUTH, inputPane);

        pack();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void reportMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report();
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }
    
    private void TableMake()
    {
        Configuration cfg = new Configuration();
        try
        {
            String tableName = "temp_hatekonysag";
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SHOW TABLES LIKE '" + tableName + "'");
            if (rSet.next())
            {
                sqlupdate("DROP TABLE " + tableName + "");
            }
            sqlupdate("CREATE TABLE " +  tableName +"("
                    + "cikkszam char(40) COLLATE utf8_hungarian_ci NOT NULL,"
                    + "datum date NOT NULL,"
                    + "muszak tinyint(4) NOT NULL,"
                    + "tervmennyiseg double(15,3) NOT NULL DEFAULT '0.000',"
                    + "tenymennyiseg double(15,3) NOT NULL DEFAULT '0.000',"
                    + "selejt double(15,3) NOT NULL DEFAULT '0.000') "
                    + "ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;");
            sqlupdate("INSERT INTO " + tableName + " (cikkszam,datum,muszak,"
                    + "tervmennyiseg,tenymennyiseg,selejt) SELECT "
                    + "gyartasirend.cikkszam,teljesitmenyek.datum,muszakbeosztas.muszak,"
                    + "teljesitmenyek.tervmennyiseg,teljesitmenyek.mennyiseg,teljesitmenyek.selejt "
                    + "FROM teljesitmenyek "
                    + "INNER JOIN gyartasirend ON (teljesitmenyek.grszam = gyartasirend.rendszam) "
                    + "INNER JOIN muszakbeosztas ON (teljesitmenyek.dszam = muszakbeosztas.dszam) AND "
                    + "(teljesitmenyek.datum = muszakbeosztas.datum) "
                    + "WHERE teljesitmenyek.datum BETWEEN '" + cfg.sqlDate(StartDate.getDate()) + "' AND '"
                    + cfg.sqlDate(EndDate.getDate()) + "'");
            sqlupdate("DELETE " + tableName + ".* FROM " + tableName + " "
                    + "WHERE datum = '" + cfg.sqlDate(StartDate.getDate())
                    + "' AND muszak < '" + ComboID(ShiftFrom) + "'");
            sqlupdate("DELETE " + tableName + ".* FROM " + tableName + " "
                    + "WHERE datum = '" + cfg.sqlDate(EndDate.getDate())
                    + "' AND muszak > '" + ComboID(ShiftTo) + "'");
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
    
    public static long daysBetween(Date d1, Date d2){
    return ( (d2.getTime() - d1.getTime() + ONE_HOUR) / 
                  (ONE_HOUR * 24));
  }

    private void Muszakszamol()
    {
        shiftsNr = daysBetween(StartDate.getDate(), EndDate.getDate()) + 1;
        shiftsNr = shiftsNr * ShiftFrom.getItemCount();
        shiftsNr = shiftsNr - ShiftFrom.getSelectedIndex();
        shiftsNr = shiftsNr - (ShiftFrom.getItemCount() - (ShiftTo.getSelectedIndex() + 1));
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

    private void ParametersClear()
    {
        Configuration cfg = new Configuration();
        for (int i = 0;i < 10;i++)
        {
            cfg.setParamName("", i);
            cfg.setParamValue("", i);
        }
    }

    private void Report()
    {
        int results = StartDate.getDate().compareTo(EndDate.getDate());
        if(results <= 0)
        {
            ParametersClear();
            TableMake();
            Muszakszamol();
            String riportName = "reports/muszakjelentes.jrxml";
            Configuration cfg = new Configuration();
            cfg.setParamName("user", 0);
            cfg.setParamValue(cfg.getcurrentUserName(), 0);
            cfg.setParamName("shiftfrom", 1);
            cfg.setParamValue(cfg.sqlDate(StartDate.getDate()) + " - " +
                              ComboVAL(ShiftFrom), 1);
            cfg.setParamName("shiftto", 2);
            cfg.setParamValue(cfg.sqlDate(EndDate.getDate()) + " - " +
                              ComboVAL(ShiftTo), 2);
            cfg.setParamName("shiftsNr", 3);
            cfg.setParamValue(shiftsNr, 3);
            cfg.setreportName(riportName);
            //cfg.AniMSGBox(new javax.swing.ImageIcon(getClass().getResource("icons/aniicon.gif")));
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
    
    private JComboBox ShiftFrom, ShiftTo;
    private JCalendarCombo StartDate, EndDate;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private JMenuItem reportMenu, kilepMenu;
    private JPanel inputPane;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 6;
    private long shiftsNr;
    static final long ONE_HOUR = 60 * 60 * 1000L;
}
