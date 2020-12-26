/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
import classes.SpringUtilities;
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
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Forcek László
 */
public class Users extends JExtendedFrame{

    public Users()
    {
        initComponents();
    }
    
    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/user.png"));
        setFrameIcon(icon);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        setTitle("Felhasználók & Jogosultságok");
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() / 1.5),
                                       (int)(defaultTool.getScreenSize().getHeight() / 1.5)));

        setformID(formID);
        
        userList = new JTable();
        userPass = new JPasswordField();
        fullName = new JTextField(20);
        nickName = new JTextField(20);
        formNames = new JComboBox();
        RoleEnable = new JCheckBox();
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        newMenu = new JMenuItem();
        mentMenu = new JMenuItem();
        torolMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        uScroll = new JScrollPane();
        
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
                formInternalFrameOpened(evt);
            }
        });

        RoleEnable.setText("Engedély");
        RoleEnable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RoleEnableItemStateChanged(evt);
            }
        });

        mainMenu.setText("Menü");
        mainMenu.setName("mainMenu");

        newMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        newMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/adduser.png")));
        newMenu.setText("Új felhasználó");
        newMenu.setName("newMenu");
        newMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuActionPerformed(evt);
            }
        });
        mainMenu.add(newMenu);

        mentMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        mentMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png")));
        mentMenu.setText("Mentés");
        mentMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentMenu);

        mainMenu.add(new JSeparator());

        torolMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        torolMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eraseall.png")));
        torolMenu.setText("Törlés");
        torolMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                torolMenuActionPerformed(evt);
            }
        });
        mainMenu.add(torolMenu);

        mainMenu.add(new JSeparator());

        kilepMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/close.png")));
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);
        menuBar.add(mainMenu);
        setJMenuBar(menuBar);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        JLabel Label1 = new JLabel("Azonosító");
        JLabel Label2 = new JLabel("Név");
        JLabel Label3 = new JLabel("Jelszó");
        JLabel Label4 = new JLabel("Űrlap");
        JLabel Label5 = new JLabel("");
        JLabel Label6 = new JLabel("");
        JLabel Label7 = new JLabel("");

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        tblPane = new JPanel(new BorderLayout());

        inputPane.add(Label1);
        Label1.setLabelFor(nickName);
        inputPane.add(nickName);

        inputPane.add(Label2);
        Label2.setLabelFor(fullName);
        inputPane.add(fullName);

        inputPane.add(Label3);
        Label3.setLabelFor(userPass);
        inputPane.add(userPass);
        
        inputPane.add(Label4);
        Label4.setLabelFor(formNames);
        inputPane.add(formNames);

        inputPane.add(Label5);
        inputPane.add(RoleEnable);

        inputPane.add(Label6);
        inputPane.add(Label7);

        SpringUtilities.makeCompactGrid(inputPane,
                                        3, 4, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad
        tblPane.add(uScroll);

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
        layout.putConstraint(SpringLayout.SOUTH, tblPane,
                             1,
                             SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             1,
                             SpringLayout.EAST, tblPane);

        pack();

    }
    
    private void userListMousePressed(java.awt.event.MouseEvent evt) {                                      
        Configuration cfg = new Configuration();
        int row = userList.getSelectedRow();
        Object value = userList.getValueAt(row, 0);
        if (Integer.parseInt(value.toString()) != UID)
        {
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                sqlolvas("SELECT username, nev, password FROM users WHERE azon ='" +
                         value.toString() + "'");
                rSet.first();
                nickName.setText(rSet.getString(1));
                fullName.setText(rSet.getString(2));
                userPass.setText(rSet.getString(3));
                sqlolvas("SELECT role FROM roles WHERE urlapazon = '" +
                         ComboID(formNames) + "' AND " +
                         "userazon = '" + userList.getValueAt(userList.getSelectedRow(), 0).toString() + "'");
                if (rSet.next())
                {
                    rSet.first();
                    if (rSet.getString(1).compareTo("1") == 0)
                    {
                        RoleEnable.setSelected(Boolean.TRUE);
                    }
                }
                else
                {
                    RoleEnable.setSelected(Boolean.FALSE);
                }
                conn.close();
                UID = Integer.parseInt(value.toString());
                userPass.setEditable(false);
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
            finally
            {
                try {conn.close();}
                catch (SQLException ex){}
            }
        }
    }                                     

    private void mentMenuActionPerformed(java.awt.event.ActionEvent evt) {                                         
        Configuration cfg = new Configuration();
        String passStr = PassToString(userPass.getPassword());
        if (nickName.getText().compareTo("") != 0 &&
            passStr.compareTo("") != 0 &&
            fullName.getText().compareTo("") != 0)
        {
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                if (userList.getSelectedRow() != -1)
                {
                    sqlupdate("UPDATE users SET username = '" + nickName.getText() + "'," +
                              "nev = '" + fullName.getText() + "' WHERE azon = '" +
                              userList.getValueAt(userList.getSelectedRow(), 0).toString() + "'");
                }
                else
                {
                    sqlolvas("SELECT username FROM users WHERE username = '" +
                             nickName.getText() + "'");
                    if (rSet.next())
                    {
                        JOptionPane.showMessageDialog(null,"Már létezik ilyen felhasználó!");
                        nickName.setText("");
                    }
                    else
                    {
                        sqlupdate("INSERT INTO users (nev, username, password) VALUES ('" +
                                  fullName.getText() + "','" + nickName.getText() + "',MD5('" +
                                  PassToString(userPass.getPassword()) + "'))");
                        
                        userPass.setEditable(false);
                    }
                }
                conn.commit();
                conn.close();
                Tablafeltolt();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
            finally
            {
                try {conn.close();}
                catch (SQLException ex){}
            }
        }
    }                                        

    private void RoleEnableItemStateChanged(java.awt.event.ItemEvent evt) {                                            
        if (userList.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            try
            {
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.setAutoCommit(false);
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                if (RoleEnable.isSelected())
                {
                    sqlupdate("INSERT INTO roles (urlapazon, userazon, role) VALUES ('" +
                              ComboID(formNames) + "','" +
                              userList.getValueAt(userList.getSelectedRow(), 0).toString() + "',1)");
                }
                else
                {
                    sqlupdate("DELETE roles.* FROM roles WHERE urlapazon = '" +
                              ComboID(formNames) + "' " +
                              "AND userazon = '" + 
                              userList.getValueAt(userList.getSelectedRow(), 0).toString() + "'");
                }
                conn.commit();
                conn.close();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
            finally
            {
                try {conn.close();}
                catch (SQLException ex){}
            }
        }
    }                                           

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }                                         

    private void torolMenuActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (userList.getSelectedRow() != -1)
        {
            Configuration cfg = new Configuration();
            int valaszt = JOptionPane.showConfirmDialog(null,"Biztosan törli a megadott felhasználót?","Törlés jóváhagyása",JOptionPane.YES_NO_OPTION);
            if (valaszt == 0)
            {
                if (userList.getValueAt(userList.getSelectedRow(), 1).toString().compareTo("admin") != 0)
                {
                    try
                    {
                        conn = DriverManager.getConnection(cfg.getsqlConn());
                        conn.setAutoCommit(false);
                        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        sqlupdate("INSERT INTO deletedusers (deletedusers.azon,deletedusers.nev,deletedusers.username) " +
                                  "SELECT users.azon,users.nev,users.username FROM users WHERE users.azon = '" +
                                  userList.getValueAt(userList.getSelectedRow(), 0).toString() + "'");
                        sqlupdate("DELETE users.* FROM users WHERE azon = '" +
                                  userList.getValueAt(userList.getSelectedRow(), 0).toString() + "'");
                        conn.commit();
                        conn.close();
                        Tablafeltolt();
                    }
                    catch (SQLException ex)
                    {
                        JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
                    }
                    finally
                    {
                        try {conn.close();}
                        catch (SQLException ex){}
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"A kijelölt felhasználó a beépített adminisztrátor és nem törölhető!");
                }
            }
        }
    }                                         

    private void newMenuActionPerformed(java.awt.event.ActionEvent evt) {                                        
        alaphelyzet();
    }                                       

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {                                         
        Configuration cfg = new Configuration();
        Tablafeltolt();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT azon, cim FROM forms ORDER BY cim");
            if (rSet.next())
            {
                do
                {
                    formNames.addItem(new ListItem(rSet.getInt(1),rSet.getString(2).trim()));
                } while (rSet.next());
                formNames.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent event) {
                        Configuration cfg = new Configuration();
                        JComboBox comboBox = (JComboBox) event.getSource();
                        if (userList.getSelectedRow() != -1)
                        {
                            try
                            {
                                conn = DriverManager.getConnection(cfg.getsqlConn());
                                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                                sqlolvas("SELECT role FROM roles WHERE urlapazon = '" +
                                ComboID(comboBox) + "' AND " +
                                "userazon = '" + userList.getValueAt(userList.getSelectedRow(), 0).toString() + "'");
                                if (rSet.next())
                                {
                                    rSet.first();
                                    if (rSet.getString(1).compareTo("1") == 0)
                                    {
                                        RoleEnable.setSelected(Boolean.TRUE);
                                    }
                                }
                                else
                                {
                                    RoleEnable.setSelected(Boolean.FALSE);
                                }
                                conn.close();
                            }
                            catch (SQLException ex)
                            {
                                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
                            }
                            finally
                            {
                                try {conn.close();}
                                catch (SQLException ex){}
                            }
                        }
                    }
                });
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
        }
        finally
        {
            try {conn.close();}
            catch (SQLException ex){}
        }
    }                                        

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {                                          
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }                                         

    private void alaphelyzet()
    {
        UID = -1;
        userList.clearSelection();
        nickName.setText("");
        fullName.setText("");
        userPass.setText("");
        userPass.setEditable(Boolean.TRUE);
        RoleEnable.setSelected(Boolean.FALSE);
        nickName.grabFocus();
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
        rSet = null;
        try
        {
            stmt.executeUpdate(sqlcmd);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
        }
    }

    private void Tablafeltolt()
    {
        alaphelyzet();
        userList = null;
        userList = new JTable();
        uScroll.setViewportView(userList);
        String[] tableHeaders = {"Azon","Azonosító","Név"};
        Object[][] tableData = new Object[0][0];
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas("SELECT azon, username, nev FROM users ORDER BY nev");
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
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
        }
        finally
        {
            try {conn.close();}
            catch (SQLException ex){}
        }
        userList.setModel(new DefaultTableModel(tableData, tableHeaders)
        {
            Class[] types = new Class [] {
                java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
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

        userList.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        userList.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        userList.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        userList.getTableHeader().getColumnModel().getColumn(0).setResizable(false);
        userList.setAutoCreateRowSorter(true);
        userList.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        userList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        userList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                userListMousePressed(evt);
            }
        });
        uScroll.setViewportView(userList);
    }

    private String PassToString(char[] pass)
    {
        String passStr = "";
        for (int i = 0;i < pass.length;i++) {
            passStr += pass[i];
        }
        return passStr;

    }

    private int ComboID(JComboBox combo)
    {
        ListItem selectedType = (ListItem)combo.getSelectedItem();
        return selectedType.getValue();
    }

    private JCheckBox RoleEnable;
    private JComboBox formNames;
    private JTextField fullName, nickName;
    private JMenuBar menuBar;
    private JPanel inputPane, tblPane;
    private JScrollPane uScroll;
    private JMenuItem kilepMenu, mentMenu, newMenu, torolMenu;
    private JMenu mainMenu;
    private JTable userList;
    private JPasswordField userPass;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 2, UID = -1;
}
