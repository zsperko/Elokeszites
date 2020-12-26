/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek László
 */
public class POSetup extends JExtendedFrame{

    public POSetup()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/posetup.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() - 150),
                                       (int)(defaultTool.getScreenSize().getHeight() - 150)));
        setTitle("Gyártási rendelés beállítás");
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

        Szerszamok = new JComboBox();
        mainMenu = new JMenu();
        mentesMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();
        grScroll = new JScrollPane();
        Grszam = new JTextField(30);
        Cikkszam = new JTextField(30);
        Filter = new JTextField(30);
        GrFilter = new JRadioButton("Gyártási rendelés");
        CikkFilter = new JRadioButton("Cikkszám");
        bGroup = new ButtonGroup();
        Datum = new JCalendarCombo();

        Grszam.setEditable(false);
        Cikkszam.setEditable(false);

        bGroup.add(GrFilter);
        bGroup.add(CikkFilter);

        GrFilter.setSelected(true);

        mainMenu.setText("Menü");

        mentesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        mentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/calculator.png"))); // NOI18N
        mentesMenu.setText("Mentés");
        mentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentesMenu);
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

        Filter.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                FilterKeyReleased(evt);
            }
        });

        GrFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                GrFilterStateChanged(evt);
            }
        });

        CikkFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CikkFilterStateChanged(evt);
            }
        });

        GRtablatolt();
        Comboboxtolt(Szerszamok,"SELECT azon,szam FROM szerszamok ORDER BY szam");

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Gyártási rendelés");
        JLabel Label2 = new JLabel("Cikkszám");
        JLabel Label3 = new JLabel("Szerszám");
        JLabel Label4 = new JLabel("Dátum");
        JLabel Label5 = new JLabel("Szűrő");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Grszam);
        inputPane.add(Grszam);

        inputPane.add(Label2);
        Label2.setLabelFor(Cikkszam);
        inputPane.add(Cikkszam);

        inputPane.add(Label3);
        Label3.setLabelFor(Szerszamok);
        inputPane.add(Szerszamok);

        inputPane.add(Label4);
        Label4.setLabelFor(Datum);
        inputPane.add(Datum);

        inputPane.add(Label5);
        Label5.setLabelFor(Filter);
        inputPane.add(Filter);

        inputPane.add(GrFilter);
        inputPane.add(CikkFilter);

        SpringUtilities.makeCompactGrid(inputPane,
                                6, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        tblPane = new JPanel(new BorderLayout());
        tblPane.setBorder(new EtchedBorder());
        grScroll.setViewportView(GRlista);
        tblPane.add(grScroll);

        contentPane.add(inputPane);
        contentPane.add(tblPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             0,
                             SpringLayout.NORTH, contentPane);

        //tablePane
        layout.putConstraint(SpringLayout.WEST, tblPane,
                             0,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, tblPane,
                             0,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.EAST, tblPane,
                             0,
                             SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             0,
                             SpringLayout.SOUTH, contentPane);

        pack();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void mentesMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (GRlista.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement();
                sqlupdate("UPDATE gr_muveletterv SET gr_muveletterv.gepszam = '"
                        + ComboVAL(Szerszamok) + "' "
                        + "WHERE gr_muveletterv.azon = '"
                        + GRlista.getValueAt(GRlista.getSelectedRow(),2).toString() + "'");
                sqlupdate("UPDATE gyartasirend SET gyartasirend.kezddatum = '"
                        + cfg.sqlDate(Datum.getDate()) + "',gyartasirend.modositott = 1 "
                        + "WHERE gyartasirend.rendszam = '"
                        + GRlista.getValueAt(GRlista.getSelectedRow(),0).toString() + "'");
                conn.close();
                GRtablatolt();
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
    
    private void FilterKeyReleased(java.awt.event.KeyEvent evt) {
        GRtablatolt();
    }

    private void GrFilterStateChanged(java.awt.event.ItemEvent evt) {
        try
        {
            Filter.removeKeyListener(Filter.getKeyListeners()[0]);
            Filter.setText("");
            GRtablatolt();
            Filter.requestFocus();
            Filter.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    FilterKeyReleased(evt);
                }
            });
        }
        catch (Exception ex)
        {}
    }

    private void CikkFilterStateChanged(java.awt.event.ItemEvent evt) {
        try
        {
            Filter.removeKeyListener(Filter.getKeyListeners()[0]);
            Filter.setText("");
            GRtablatolt();
            Filter.requestFocus();
            Filter.addKeyListener(new java.awt.event.KeyAdapter() {
                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    FilterKeyReleased(evt);
                }
            });
        }
        catch (Exception ex)
        {}
    }

    private void GRtablatolt()
    {
        GRlista = null;
        GRlista = new JTable();
        grScroll.setViewportView(GRlista);
        String[] tableHeaders = {"Gyártási rendelés","Műveletsorszám","Műveletterv azon","Cikkszám",
                                 "Művelet","Gépszám","Mennyiség","Befejezett mennyiség","Kezdés dátuma"};
        Object[][] tableData = new Object[0][0];
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            int hosz = Filter.getText().length();
            String sqlCmd = "";
            if (GrFilter.isSelected())
            {
                sqlCmd = "SELECT gr_muveletterv.grszam,gr_muveletterv.muveletkapcsolo,gr_muveletterv.azon,"
                       + "gyartasirend.cikkszam,gr_muveletterv.megnevezes,gr_muveletterv.gepszam,"
                       + "gyartasirend.mennyiseg,gr_muveletterv.befejezettmennyiseg,gyartasirend.kezddatum "
                       + "FROM gr_muveletterv "
                       + "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) "
                       + "WHERE gyartasirend.nyitott = 1 AND "
                       + "LEFT(gyartasirend.rendszam," + hosz + ") = '" + Filter.getText() + "' AND "
                       + "gyartasirend.mennyiseg > gr_muveletterv.befejezettmennyiseg ";
            }
            else if (CikkFilter.isSelected())
            {
               sqlCmd = "SELECT gr_muveletterv.grszam,gr_muveletterv.muveletkapcsolo,gr_muveletterv.azon,"
                       + "gyartasirend.cikkszam,gr_muveletterv.megnevezes,gr_muveletterv.gepszam,"
                       + "gyartasirend.mennyiseg,gr_muveletterv.befejezettmennyiseg,gyartasirend.kezddatum "
                       + "FROM gr_muveletterv "
                       + "INNER JOIN gyartasirend ON (gr_muveletterv.grszam = gyartasirend.rendszam) "
                       + "WHERE gyartasirend.nyitott = 1 AND "
                       + "LEFT(gyartasirend.cikkszam," + hosz + ") = '" + Filter.getText() + "' AND "
                       + "gyartasirend.mennyiseg > gr_muveletterv.befejezettmennyiseg ";
            }
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
                    value = (int)rSet.getDouble(8);
                    tableData[rSet.getRow()-1][7] = value;
                    tableData[rSet.getRow()-1][8] = rSet.getString(9);
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
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false
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
            GRlista.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(2).setResizable(false);

            GRlista.setAutoCreateRowSorter(true);

            GRlista.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            GRlistaTablelistener = new SelectionListener(GRlista);
            GRlista.getSelectionModel().addListSelectionListener(GRlistaTablelistener);

            TableColumnModel tcm = GRlista.getColumnModel();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.UK);
            FormatRenderer render = new FormatRenderer(formatter);
            render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.CENTER);
            tcm.getColumn(1).setCellRenderer(render);
            render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.RIGHT);
            tcm.getColumn(5).setCellRenderer(render);
            tcm.getColumn(6).setCellRenderer(render);
            tcm.getColumn(7).setCellRenderer(render);
            tcm.getColumn(8).setCellRenderer(render);
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
    
    private void Gradatok()
    {
        Configuration cfg = new Configuration();
        if (GRlista.getSelectedRow() != -1)
        {
            Grszam.setText(GRlista.getValueAt(GRlista.getSelectedRow(),0).toString());
            Cikkszam.setText(GRlista.getValueAt(GRlista.getSelectedRow(),3).toString());
            Datum.setDate(cfg.sqlToDate(GRlista.getValueAt(GRlista.getSelectedRow(),8).toString()));
            Datum.repaint();
            Combokeres(Szerszamok,GRlista.getValueAt(GRlista.getSelectedRow(),5).toString(),1);
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            Grszam.setText("");
            Cikkszam.setText("");
            Datum.setDate(cal.getTime());
            Datum.repaint();
        }
    }

    class SelectionListener implements ListSelectionListener {
        JTable table;

        SelectionListener(JTable table) {
            this.table = table;
        }

        public void valueChanged(ListSelectionEvent e) {
            if (e.getSource() == table.getSelectionModel()
                  && table.getRowSelectionAllowed()) {
                Gradatok();
            }
            else if (e.getSource() == table.getColumnModel().getSelectionModel()
                   && table.getColumnSelectionAllowed() ){
            }
            if (e.getValueIsAdjusting()) {
            }
        }
    }

    private void Combokeres(JComboBox combo, String ertek,int valaszt)
    {
        int i = 0;
        switch (valaszt)
        {
            case 1:
                    while (combo.getItemCount() - 1 > i &&
                           ((ListItem)combo.getItemAt(i)).toString().compareTo(ertek) != 0)
                    {
                        i++;
                    }
                break;
            case 2:
                    while (combo.getItemCount() - 1 > i &&
                           Integer.toString(((ListItem)combo.getItemAt(i)).getValue()).compareTo(ertek) != 0)
                    {
                        i++;
                    }
                break;
        }
        combo.setSelectedIndex(i);
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

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 22;
    private JScrollPane grScroll;
    private JTable GRlista;
    private JComboBox Szerszamok;
    private JPanel inputPane, tblPane;
    private JMenu mainMenu;
    private JMenuItem mentesMenu, kilepMenu;
    private JMenuBar menuBar;
    private JCalendarCombo Datum;
    private JTextField Grszam, Cikkszam, Filter;
    private JRadioButton GrFilter, CikkFilter;
    private ButtonGroup bGroup;
    private SelectionListener GRlistaTablelistener;
}