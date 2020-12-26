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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Forcek László
 */
public class ExcelTable extends JExtendedFrame{

    public ExcelTable()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/printtoexcel.png"));
        this.setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        setTitle("Leltáradatok");
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 3),
                                       (int)(defaultTool.getScreenSize().getHeight() / 2)));
        setformID(formID);

        Year = new JComboBox();
        Month = new JComboBox();
        LScroll = new JScrollPane();
        Leltarlista = new JTable();
        menuBar = new JMenuBar();
        mainMenu = new javax.swing.JMenu();
        xlsMenu = new javax.swing.JMenuItem();
        navMenu = new javax.swing.JMenuItem();
        kilepMenu = new javax.swing.JMenuItem();
        
        Leltarlista.getTableHeader().setResizingAllowed(false);
        Leltarlista.getTableHeader().setReorderingAllowed(false);
        LScroll.setViewportView(Leltarlista);

        mainMenu.setText("Menü");

        xlsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        xlsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png"))); // NOI18N
        xlsMenu.setText("Excel lista mentés");
        xlsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xlsMenuActionPerformed(evt);
            }
        });
        mainMenu.add(xlsMenu);
        mainMenu.add(new JSeparator());

        navMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        navMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loaddata.png"))); // NOI18N
        navMenu.setText("Navison leltárlista");
        navMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                navMenuActionPerformed(evt);
            }
        });
        mainMenu.add(navMenu);
        mainMenu.add(new JSeparator());

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/close.png"))); // NOI18N
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);
        menuBar.add(mainMenu);
        setJMenuBar(menuBar);
        
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0;i < 25;i++)
        {
            Year.addItem(year -10 + i);
        }
        for (int i = 1;i < 13;i++)
        {
            Month.addItem(i);
        }
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        int i = 0;
        while (Year.getItemAt(i).toString().compareTo(Integer.toString(year)) != 0)
        {
            i++;
        }
        Year.setSelectedIndex(i);
        i = 0;
        while (Month.getItemAt(i).toString().compareTo(Integer.toString(month)) != 0)
        {
            i++;
        }
        Month.setSelectedIndex(i);

        Year.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leltarnaplo();
            }
        });
        Month.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Leltarnaplo();
            }
        });

        Leltarnaplo();

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Év");
        JLabel Label2 = new JLabel("Hónap");

        datePane = new JPanel(new SpringLayout());
        datePane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());

        datePane.add(Label1);
        Label1.setLabelFor(Year);
        datePane.add(Year);

        datePane.add(Label2);
        Label2.setLabelFor(Month);
        datePane.add(Month);

        tblPane.add(LScroll);

        SpringUtilities.makeCompactGrid(datePane,
                                2, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        contentPane.add(datePane);
        contentPane.add(tblPane);

        //datePane
        layout.putConstraint(SpringLayout.WEST, datePane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, datePane,
                             1,
                             SpringLayout.NORTH, contentPane);

        //tblPane
        layout.putConstraint(SpringLayout.WEST, tblPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, tblPane,
                             1,
                             SpringLayout.SOUTH, datePane);
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             1,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);

        pack();
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void xlsMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (Leltarlista.getRowCount() > 0)
        {
            //SaveData save = new SaveData();
            //save.XLSMent(Year.getSelectedItem().toString(),Month.getSelectedItem().toString());
        }
    }

    private void navMenuActionPerformed(java.awt.event.ActionEvent evt) {
        LoadData ld = new LoadData();
        ld.XlsToSql(Year.getSelectedItem().toString(),Month.getSelectedItem().toString());
    }

    private void Leltarnaplo()
    {
        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("Azon");
        tableHeaders.add("Cikkszám");
        tableHeaders.add("Mennyiség");
        tableHeaders.add("Raktár");
        rSet = null;
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT leltarnaplo.azon,leltarnaplo.cikkszam,leltarnaplo.mennyiseg," +
                     "raktarak.raktarnev FROM leltarnaplo " +
                     "INNER JOIN raktarak ON (leltarnaplo.raktarkod = raktarak.azon) " +
                     "WHERE leltarnaplo.ev = '" + Year.getSelectedItem().toString() + "' AND " +
                     "leltarnaplo.honap = '" + Month.getSelectedItem().toString() + "'");
            if (rSet.next())
            {
                do
                {
                    Vector<Object> oneRow = new Vector<Object>();
                    oneRow.add(rSet.getString(1));
                    oneRow.add(rSet.getString(2));
                    oneRow.add(rSet.getString(3));
                    oneRow.add(rSet.getString(4));
                    tableData.add(oneRow);
                } while (rSet.next());
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            System.out.println("Kapcsolódási hiba: " + ex.getMessage());
        }
        Leltarlista.setModel(new NoEditModel(tableData, tableHeaders));
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        Leltarlista.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
        TableColumnModel tcm = Leltarlista.getColumnModel();
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
        FormatRenderer render = new FormatRenderer(formatter);
        render.setHorizontalAlignment(SwingConstants.RIGHT);
        tcm.getColumn(2).setCellRenderer(render);
        Leltarlista.setAutoCreateRowSorter(true);
        Leltarlista.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
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
            System.out.println("kapcsolódási hiba: " + ex.getMessage());
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 10;
    private Vector<ListItem> rLista = new Vector<ListItem>();
    private JComboBox Year, Month;
    private JPanel datePane, tblPane;
    private JTable Leltarlista;
    private JScrollPane LScroll;
    private JMenuBar menuBar;
    private JMenuItem xlsMenu, navMenu, kilepMenu;
    private JMenu mainMenu;
}
