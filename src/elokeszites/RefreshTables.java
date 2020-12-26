/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Container;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Forcek László
 */
public class RefreshTables extends JExtendedFrame{

    public RefreshTables()
    {
        initComponents();
    }

    private void initComponents()
    {
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/refresh.png"));
        setFrameIcon(icon);
        setResizable(false);
        setClosable(true);
        setIconifiable(true);
        setTitle("Frissítések");
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

        raktarkeszlet = new JCheckBox("Raktárkészlet");
        dolgozok = new JCheckBox("Dolgozói törzs");
        muveletterv = new JCheckBox("Művelettervek");
        cikktorzs = new JCheckBox("Cikktörzs");
        gyartasirendelesek = new JCheckBox("Gyártási rendelések");
        darabjegyzek = new JCheckBox("Darabjegyzékek");
        visszajelent = new JCheckBox("Visszajelentések");
        furnerraktar = new JCheckBox("Furnérraktár");
        menuBar = new JMenuBar();
        mainMenu = new JMenu("Menü");
        frissitMenu = new JMenuItem();
        kilepMenu = new JMenuItem();

        frissitMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        frissitMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/refresh.png"))); // NOI18N
        frissitMenu.setText("Frissítés");
        frissitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frissitMenuActionPerformed(evt);
            }
        });
        mainMenu.add(frissitMenu);
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

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        rPane = new JPanel(new SpringLayout());

        rPane.add(dolgozok);
        rPane.add(cikktorzs);
        rPane.add(raktarkeszlet);
        rPane.add(gyartasirendelesek);
        //rPane.add(darabjegyzek);
        //rPane.add(muveletterv);
        rPane.add(visszajelent);
        //rPane.add(furnerraktar);
        contentPane.add(rPane);

        SpringUtilities.makeCompactGrid(rPane,
                                        5, 1, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad
        //inputPane
        layout.putConstraint(SpringLayout.WEST, rPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, rPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             1,
                             SpringLayout.SOUTH, rPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             70,
                             SpringLayout.EAST, rPane);

        FileSystemView filesys = FileSystemView.getFileSystemView();
        DefaultDir = filesys.getHomeDirectory().getAbsolutePath();
        Konyvtarkeres();
        pack();
    }
    

    private void frissitMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if ((cikktorzs.isSelected()) || (dolgozok.isSelected()) ||
            (darabjegyzek.isSelected()) || (muveletterv.isSelected()) ||
            (raktarkeszlet.isSelected()) || gyartasirendelesek.isSelected() ||
            (visszajelent.isSelected()) || (furnerraktar.isSelected()))
        {
            Configuration cfg = new Configuration();
            cfg.setthreadFinished(false);
            if ((cikktorzs.isSelected()) || (darabjegyzek.isSelected()) ||
                (muveletterv.isSelected()) || (raktarkeszlet.isSelected()) ||
                (gyartasirendelesek.isSelected()) || (visszajelent.isSelected()) ||
                (furnerraktar.isSelected()))
            {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(DefaultDir));
                fileChooser.setDialogTitle("Válassza ki a könyvtárat");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                int status = fileChooser.showOpenDialog(null);
                if (status == JFileChooser.APPROVE_OPTION)
                {
                    cfg.setrefreshDir(fileChooser.getSelectedFile().getAbsolutePath());
                    System.out.println(cfg.getrefreshDir());
                }
                else
                {
                    cfg.setrefreshDir("");
                }
            }
            for (int i = 0;i < 8;i++) cfg.setrefreshTables(i, false);
            boolean enableRefresh = false;
            if (dolgozok.isSelected())
            {
                cfg.setrefreshTables(0, true);
                enableRefresh = true;
            }
            if (cfg.getrefreshDir() != null)
            {
                if (cfg.getrefreshDir().compareTo("") != 0)
                {
                    if (gyartasirendelesek.isSelected())
                    {
                        cfg.setrefreshTables(1, true);
                        enableRefresh = true;
                    }
                    if (raktarkeszlet.isSelected())
                    {
                        cfg.setrefreshTables(2, true);
                        enableRefresh = true;
                    }
                    if (cikktorzs.isSelected())
                    {
                        cfg.setrefreshTables(3, true);
                        enableRefresh = true;
                    }

                    if (darabjegyzek.isSelected())
                    {
                        cfg.setrefreshTables(4, true);
                        enableRefresh = true;
                    }

                    if (muveletterv.isSelected())
                    {
                       cfg.setrefreshTables(5, true);
                       enableRefresh = true;
                    }

                    if (visszajelent.isSelected())
                    {
                       cfg.setrefreshTables(6, true);
                       enableRefresh = true;
                    }
                    if (furnerraktar.isSelected())
                    {
                       cfg.setrefreshTables(7, true);
                       enableRefresh = true;
                    }
                }
            }
            if (enableRefresh)
            {
                AniMsgBox form = new AniMsgBox("Frissítés folyamatban...","Frissítés kész!");
                RefreshThread th;
                Thread thread;
                th = new RefreshThread();
                thread = new Thread(th);
                thread.start();
                form.setVisible(true);
            }
            cikktorzs.setSelected(false);
            dolgozok.setSelected(false);
            darabjegyzek.setSelected(false);
            muveletterv.setSelected(false);
            raktarkeszlet.setSelected(false);
            gyartasirendelesek.setSelected(false);
            visszajelent.setSelected(false);
            furnerraktar.setSelected(false);
            cfg.setrefreshDir("");
        }
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void Konyvtarkeres()
    {
        Configuration cfg = new Configuration();
        try
        {
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT ertek FROM opciok WHERE opszam = 1");
            if (rSet.next())
            {
                rSet.first();
                DefaultDir = rSet.getString(1);
            }
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
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
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
    }

    private JCheckBox cikktorzs, darabjegyzek, dolgozok, raktarkeszlet, gyartasirendelesek, 
                      muveletterv, visszajelent, furnerraktar;
    private JMenuItem frissitMenu,kilepMenu;
    private JPanel rPane;
    private JMenu mainMenu;
    private JMenuBar menuBar;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int formID = 3;
    private String DefaultDir;
}
