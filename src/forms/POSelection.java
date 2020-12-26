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
import java.awt.Color;
import java.awt.Component;
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
import java.util.Locale;
import javax.swing.ButtonGroup;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Forcek László
 */
public class POSelection extends JExtendedFrame implements TableModelListener{

    public POSelection()
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
        setTitle("Gyártási rendelések");
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

        Filter= new JTextField(10);
        mainMenu = new JMenu();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        menuBar = new JMenuBar();
        Munkahely = new JComboBox();
        grScroll = new JScrollPane();
        GRlista = new JTable();
        GrFilter = new JRadioButton("Gyártási rendelés");
        CikkFilter = new JRadioButton("Cikkszám");
        bGroup = new ButtonGroup();

        bGroup.add(GrFilter);
        bGroup.add(CikkFilter);

        GrFilter.setSelected(true);

        mainMenu.setText("Menü");

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png"))); // NOI18N
        torolMenu.setText("Töröl");
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

        Filter.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                FilterKeyReleased(evt);
            }
        });

        Comboboxtolt(Munkahely,"SELECT azon,megnevezes FROM kosp WHERE tervez = 1 ORDER BY megnevezes");

        Munkahely.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GRtablatolt();
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

        Combokeres(Munkahely,14);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Munkahely");
        JLabel Label2 = new JLabel("Szűrőfeltétel");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        inputPane.add(Label1);
        Label1.setLabelFor(Munkahely);
        inputPane.add(Munkahely);

        inputPane.add(Label2);
        Label2.setLabelFor(Filter);
        inputPane.add(Filter);

        inputPane.add(GrFilter);
        inputPane.add(CikkFilter);

        SpringUtilities.makeCompactGrid(inputPane,
                                3, 2, //rows, cols
                                10, 10,//initX, initY
                                5, 5);//xPad, yPad

        tablePane = new JPanel(new BorderLayout());
        tablePane.setBorder(new EtchedBorder());
        grScroll.setViewportView(GRlista);
        tablePane.add(grScroll);

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

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {
        int valaszt = JOptionPane.showConfirmDialog(null, "Törli az összes kijelölést?", "Kijelölés törlése", 
                                                    JOptionPane.YES_NO_OPTION);
        if (valaszt == 0)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement();
                sqlupdate("UPDATE gr_muveletterv SET kijelolt = 0");
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
            GRtablatolt();
        }
    }

    private void GRtablatolt()
    {
        String[] tableHeaders = {"Gyártási rendelés","Műveletsorszám","Műveletterv azon","Cikkszám","Ütemszám",
                                 "Művelet","Muhelyszam","Mennyiség","Befejezett mennyiség","Kezdés dátuma",
                                 "Kijelöl","Szerszám","Műhelyszam"};
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
                sqlCmd = "SELECT gyartasirend.rendszam,gr_muveletterv.muveletkapcsolo,gr_muveletterv.azon AS muvazon,"
                       + "gyartasirend.cikkszam,cikktorzs.utem,gr_muveletterv.megnevezes,muhelyek.azon,"
                       + "gyartasirend.mennyiseg,gr_muveletterv.befejezettmennyiseg,gyartasirend.kezddatum,"
                       + "gr_muveletterv.kijelolt,seged.szam,seged.muhelyszam  FROM gyartasirend "
                       + "INNER JOIN gr_muveletterv ON (gyartasirend.rendszam = gr_muveletterv.grszam) "
                       + "INNER JOIN muhelyek ON (gr_muveletterv.muhelykod = muhelyek.szam) "
                       + "INNER JOIN cikktorzs ON (gyartasirend.cikkszam = cikktorzs.cikkszam) "
                       + "LEFT OUTER JOIN (SELECT szerszamok.szam,muhelyek.szam AS muhelyszam FROM szerszamok "
                       + "INNER JOIN szerszamokmuhelyben ON (szerszamok.azon = szerszamokmuhelyben.szerszamazon) "
                       + "INNER JOIN muhelyek ON (szerszamokmuhelyben.muhelyazon = muhelyek.azon) "
                       + "GROUP BY szerszamok.szam,muhelyek.szam) seged ON (gr_muveletterv.gepszam = seged.szam) "
                       + "WHERE gyartasirend.nyitott = 1 AND muhelyek.ktghely = '"
                       + ComboID(Munkahely) + "' AND "
                       + "LEFT(gyartasirend.rendszam," + hosz + ") = '" + Filter.getText() + "' AND "
                       + "gyartasirend.mennyiseg > gr_muveletterv.befejezettmennyiseg ";
            }
            else if (CikkFilter.isSelected())
            {
                sqlCmd = "SELECT gyartasirend.rendszam,gr_muveletterv.muveletkapcsolo,gr_muveletterv.azon AS muvazon,"
                       + "gyartasirend.cikkszam,cikktorzs.utem,gr_muveletterv.megnevezes,muhelyek.azon,"
                       + "gyartasirend.mennyiseg,gr_muveletterv.befejezettmennyiseg,gyartasirend.kezddatum,"
                       + "gr_muveletterv.kijelolt,seged.szam,seged.muhelyszam FROM gyartasirend "
                       + "INNER JOIN gr_muveletterv ON (gyartasirend.rendszam = gr_muveletterv.grszam) "
                       + "INNER JOIN muhelyek ON (gr_muveletterv.muhelykod = muhelyek.szam) "
                       + "INNER JOIN cikktorzs ON (gyartasirend.cikkszam = cikktorzs.cikkszam) "
                       + "LEFT OUTER JOIN (SELECT szerszamok.szam,muhelyek.szam AS muhelyszam FROM szerszamok "
                       + "INNER JOIN szerszamokmuhelyben ON (szerszamok.azon = szerszamokmuhelyben.szerszamazon) "
                       + "INNER JOIN muhelyek ON (szerszamokmuhelyben.muhelyazon = muhelyek.azon) "
                       + "GROUP BY szerszamok.szam,muhelyek.szam) seged ON (gr_muveletterv.gepszam = seged.szam) "
                       + "WHERE gyartasirend.nyitott = 1 AND muhelyek.ktghely = '"
                       + ComboID(Munkahely) + "' AND "
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
                    tableData[rSet.getRow()-1][6] = rSet.getString(7);
                    int value = (int)rSet.getDouble(8);
                    tableData[rSet.getRow()-1][7] = value;
                    value = (int)rSet.getDouble(9);
                    tableData[rSet.getRow()-1][8] = value;
                    tableData[rSet.getRow()-1][9] = rSet.getString(10);
                    boolean atad = false;
                    if (rSet.getInt(11) == 1) atad = true;
                    tableData[rSet.getRow()-1][10] = atad;
                    value = 0;
                    tableData[rSet.getRow()-1][12] = "";
                    if (rSet.getString(12) != null)
                    {
                        tableData[rSet.getRow()-1][12] = rSet.getString(13);
                        value = 3;
                    }
                    tableData[rSet.getRow()-1][11] = value;
                }
            }
            conn.close();
            DefaultTableModel model = new DefaultTableModel(tableData, tableHeaders)
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
                    java.lang.Object.class,
                    java.lang.Object.class,
                    java.lang.Boolean.class,
                    java.lang.Integer.class,
                    java.lang.Object.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false, true, false, false
                };
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
                @Override
                public Class getColumnClass(int c) {
                    return getValueAt(0, c).getClass();
                }
            };

            GRlista = null;
            GRlista = new JTable(model)
            {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
                    {
                            Component c = super.prepareRenderer(renderer, row, column);
                            int modelRow = convertRowIndexToModel(row);
                            int Szerszam = (Integer)getModel().getValueAt(modelRow, 11);
                            if (isRowSelected(row))
                            {
                                c.setBackground(new Color(239,199,0));
                            }
                            else if(Szerszam == 0)
                            {
                                c.setBackground(Color.RED);
                            }
                            else if (Szerszam == 1)
                            {
                                c.setBackground(Color.CYAN);
                            }
                            else
                            {
                                c.setBackground(Color.WHITE);
                            }
                            return c;
                    }
            };
            grScroll.setViewportView(GRlista);

            Grtablafrissit();
            GRlista.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(1).setResizable(false);

            GRlista.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(2).setMinWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(2).setResizable(false);

            GRlista.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(6).setPreferredWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(6).setResizable(false);

            GRlista.getTableHeader().getColumnModel().getColumn(11).setMaxWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(11).setMinWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(11).setPreferredWidth(0);
            GRlista.getTableHeader().getColumnModel().getColumn(11).setResizable(false);

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
            render = new FormatRenderer(formatter);
            render.setHorizontalAlignment(SwingConstants.RIGHT);
            tcm.getColumn(4).setCellRenderer(render);
            tcm.getColumn(6).setCellRenderer(render);
            tcm.getColumn(7).setCellRenderer(render);
            tcm.getColumn(8).setCellRenderer(render);
            tcm.getColumn(12).setCellRenderer(render);
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

    private void Grtablafrissit()
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            for (int i = 0;i < GRlista.getRowCount();i++)
            {
                sqlolvas("SELECT gr_muveletterv.gepszam,muhelyek.szam FROM gr_muveletterv "
                       + "INNER JOIN szerszamok ON (gr_muveletterv.gepszam = szerszamok.szam) "
                       + "INNER JOIN szerszamokmuhelyben ON (szerszamok.azon = szerszamokmuhelyben.kovszerszam) "
                       + "INNER JOIN muhelyek ON (szerszamokmuhelyben.muhelyazon = muhelyek.azon) "
                       + "WHERE gr_muveletterv.azon = '" + GRlista.getValueAt(i, 2).toString() + "'");
                if (rSet.next())
                {
                    GRlista.setValueAt(1, i, 11);
                    GRlista.setValueAt(rSet.getString(2), i, 12);
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

    public void tableChanged(TableModelEvent e) {
        Object data = GRlista.getValueAt(GRlista.getSelectedRow(),10);
        boolean value = Boolean.parseBoolean(data.toString());
        int valueInInt = (value == true ? 1 : 0);
        UpdateChanges(GRlista.getValueAt(GRlista.getSelectedRow(), 2).toString(),valueInInt);
    }

    private void UpdateChanges(String index, int value)
    {
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            sqlupdate("UPDATE gr_muveletterv SET kijelolt = '" + value + "' " +
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
    
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 19;
    private JPanel inputPane, tablePane;
    private JComboBox Munkahely;
    private JTextField Filter;
    private JRadioButton GrFilter, CikkFilter;
    private ButtonGroup bGroup;
    private JTable GRlista;
    private JMenu mainMenu;
    private JMenuItem torolMenu, kilepMenu;
    private JMenuBar menuBar;
    private JScrollPane grScroll;
}
