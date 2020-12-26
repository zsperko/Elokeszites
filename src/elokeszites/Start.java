/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import com.nilo.plaf.nimrod.NimRODTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Forcek Laszlo
 */
public class Start extends JFrame{

    public Start() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Configuration cfg = new Configuration();
        cfg.setsqlConn("");
        cfg.setconnOK(false);
        boolean exists = (new File("config.dat")).exists();
        if (exists)
        {
            try
            {
                cfg.beolvas();
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.close();
                cfg.setconnOK(true);
            }
            catch (SQLException ex){}
            finally
            {
                if (cfg.getconnOK())
                {
                    try {conn.close();}
                    catch (SQLException ex){}
                }
            }
        }
        for (int i = 0;i<40;i++)
        {
            cfg.setopenForm(false, i);
        }
    }

    private void initComponents()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Előkészítés");
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());

        ((JPanel)cont).setBorder(BorderFactory.createRaisedBevelBorder());

        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        maximumWindowBounds = graphicsEnvironment.getMaximumWindowBounds();
        setMaximizedBounds(maximumWindowBounds);

        Desktop = new JExtendedDesktop();
        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        belepMenu = new JMenuItem();
        jelszoMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        tervezesMenu = new JMenu();
        napitervMenu = new JMenuItem();
        anyagigenyaMenu = new JMenuItem();
        anyagigenymMenu = new JMenuItem();
        leltarMenu = new JMenu();
        leltarurlapMenu = new JMenuItem();
        excelMenu = new JMenuItem();
        opciokMenu = new JMenu();
        kapcsolatMenu = new JMenuItem();
        frissitMenu = new JMenuItem();
        felhasznalokMenu = new JMenuItem();
        munkahelyMenu = new JMenuItem();
        muhelyMenu = new JMenuItem();
        gepcsoportMenu = new JMenuItem();
        anyagigenyopciokMenu = new JMenuItem();
        napitervfejlecMenu = new JMenuItem();
        visszajelentesMenu = new JMenuItem();
        muszakbeosztMenu = new JMenuItem();
        hatekonysagMenu = new JMenuItem();
        szerszamokMenu = new JMenuItem();
        gyartasirendMenu = new JMenuItem();
        rendelestervMenu = new JMenuItem();
        uzeminaptarMenu = new JMenuItem();
        gyartasirendbeallMenu = new JMenuItem();
        utemekMenu = new JMenuItem();

        Desktop.setOpaque(false);
        mainMenu.setText("Főmenü");

        belepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/connect.png"))); 
        belepMenu.setText("Belépés");
        belepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                belepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(belepMenu);

        jelszoMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/key.png"))); 
        jelszoMenu.setText("Jelszó váltás");
        jelszoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jelszoMenuActionPerformed(evt);
            }
        });
        mainMenu.add(jelszoMenu);
        mainMenu.add(new JSeparator());

        kilepMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/close.png"))); 
        kilepMenu.setText("Kilépés");
        kilepMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kilepMenuActionPerformed(evt);
            }
        });
        mainMenu.add(kilepMenu);

        menuBar.add(mainMenu);

        tervezesMenu.setText("Tervezés");

        napitervMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/plan.png"))); 
        napitervMenu.setText("Napiterv");
        napitervMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                napitervMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(napitervMenu);

        napitervfejlecMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/header.png"))); 
        napitervfejlecMenu.setText("Napiterv fejléc");
        napitervfejlecMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                napitervfejlecMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(napitervfejlecMenu);

        anyagigenyaMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/amaterialrequest.png"))); 
        anyagigenyaMenu.setText("Anyagigény napiterv alapján");
        anyagigenyaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anyagigenyaMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(anyagigenyaMenu);

        anyagigenymMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/mmaterialrequest.png"))); 
        anyagigenymMenu.setText("Független anyagigénylés");
        //tervezesMenu.add(anyagigenymMenu);

        visszajelentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/timerec.png"))); 
        visszajelentesMenu.setText("Visszajelentések");
        visszajelentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visszajelentesMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(visszajelentesMenu);

        anyagigenyopciokMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/options.png"))); 
        anyagigenyopciokMenu.setText("Anyagigény beállítások");
        anyagigenyopciokMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anyagigenyopciokMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(anyagigenyopciokMenu);

        muszakbeosztMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/shiftplanner.png"))); 
        muszakbeosztMenu.setText("Műszakbeosztások");
        muszakbeosztMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muszakbeosztMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(muszakbeosztMenu);

        hatekonysagMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/efficiency.png"))); 
        hatekonysagMenu.setText("Műszakjelentés");
        hatekonysagMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hatekonysagMenuActionPerformed(evt);
            }
        });
        //tervezesMenu.add(hatekonysagMenu);

        szerszamokMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/machines.png")));
        szerszamokMenu.setText("Aktuális szerszámok");
        szerszamokMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szerszamokMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(szerszamokMenu);

        gyartasirendMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/polist.png")));
        gyartasirendMenu.setText("Gyártási rendelések");
        gyartasirendMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gyartasirendMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(gyartasirendMenu);


        gyartasirendbeallMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/posetup.png")));
        gyartasirendbeallMenu.setText("Gyártási rendelés beállítás");
        gyartasirendbeallMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gyartasirendbeallMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(gyartasirendbeallMenu);

        rendelestervMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/planner.png")));
        rendelestervMenu.setText("Gyártási rendelés tervezés");
        rendelestervMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rendelestervMenuActionPerformed(evt);
            }
        });
        tervezesMenu.add(rendelestervMenu);

        menuBar.add(tervezesMenu);

        leltarMenu.setText("Leltár");

        leltarurlapMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/invonhand.png"))); 
        leltarurlapMenu.setText("Furnér leltárűrlap");
        leltarurlapMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leltarurlapMenuActionPerformed(evt);
            }
        });
        leltarMenu.add(leltarurlapMenu);

        excelMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/printtoexcel.png"))); 
        excelMenu.setText("Leltáradatok");
        excelMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excelMenuActionPerformed(evt);
            }
        });
        //leltarMenu.add(excelMenu);

        
        //menuBar.add(leltarMenu);

        opciokMenu.setText("Beállítások");

        kapcsolatMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/cog.png"))); 
        kapcsolatMenu.setText("Kapcsolat beállítása");
        kapcsolatMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kapcsolatMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(kapcsolatMenu);

        frissitMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/refresh.png"))); 
        frissitMenu.setText("Frissítések");
        frissitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frissitMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(frissitMenu);

        felhasznalokMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/user.png"))); 
        felhasznalokMenu.setText("Felhasználók");
        felhasznalokMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                felhasznalokMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(felhasznalokMenu);

        munkahelyMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/workplace.png"))); 
        munkahelyMenu.setText("Munkahelyek");
        munkahelyMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                munkahelyMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(munkahelyMenu);

        muhelyMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/groupping.png"))); 
        muhelyMenu.setText("Műhelyek");
        muhelyMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muhelyMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(muhelyMenu);

        gepcsoportMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/tools.png"))); 
        gepcsoportMenu.setText("Gépcsoportok");
        gepcsoportMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gepcsoportMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(gepcsoportMenu);

        uzeminaptarMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/calendar.png")));
        uzeminaptarMenu.setText("Üzemi naptárak");
        uzeminaptarMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uzeminaptarMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(uzeminaptarMenu);

        utemekMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/elokeszites/icons/grid.png")));
        utemekMenu.setText("Cikkszámok");
        utemekMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                utemekMenuActionPerformed(evt);
            }
        });
        opciokMenu.add(utemekMenu);

        menuBar.add(opciokMenu);
        setJMenuBar(menuBar);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        cont.add(Desktop,BorderLayout.CENTER);
        Label1 = new JLabel("Felhasználó:");
        userLabel = new JLabel();
        userPane = new JPanel();
        SpringLayout layout = new SpringLayout();
        userPane.setLayout(layout);
        userPane.setBorder(new EtchedBorder());

        userPane.add(Label1);
        userPane.add(userLabel);
        SpringUtilities.makeCompactGrid(userPane,
                                        1, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad
        cont.add(userPane,BorderLayout.SOUTH);

        pack();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getconnOK())
        {
            try
            {
                cfg.beolvas();
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.close();
                cfg.setconfExist(true);
            }
            catch (SQLException ex){}
            finally
            {
                try {conn.close();}
                catch (SQLException ex){}
            }
        }
        else
        {
            String Array[] = new String[4];
            Array[0] = "";
            Array[1] = "";
            Array[2] = "";
            Array[3] = "";
            cfg.setArray(Array);
            cfg.ment();
            cfg.setsqlConn("");
            cfg.setconfExist(false);
        }
        LoginBox form = new LoginBox();
        form.setVisible(true);
        if (cfg.getcurrentUser() != 0)
        {
            userLabel.setText(cfg.getcurrentUserName());
        }
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }

    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void belepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getconnOK())
        {
            try
            {
                cfg.beolvas();
                conn = DriverManager.getConnection(cfg.getsqlConn());
                conn.close();
                cfg.setconfExist(true);
            }
            catch (SQLException ex){}
            finally
            {
                try {conn.close();}
                catch (SQLException ex){}
            }
        }
        else
        {
            String Array[] = new String[4];
            Array[0] = "";
            Array[1] = "";
            Array[2] = "";
            Array[3] = "";
            cfg.setArray(Array);
            cfg.ment();
            cfg.setsqlConn("");
            cfg.setconfExist(false);
        }
        closeAllForm();
        LoginBox form = new LoginBox();
        form.setVisible(true);
        if (cfg.getcurrentUser() != 0)
        {
            userLabel.setText(cfg.getcurrentUserName());
        }
    }

    private void kapcsolatMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        boolean canOpen = false;
        if (cfg.getcurrentUser() == -1)
        {
            canOpen = true;
        }
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            canOpen = true;
        }

        if (canOpen)
        {
            Connect form = new Connect();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                if (!cfg.getconnOK())
                {
                    role = true;
                }
                else
                {
                    RolesRead(form.getformID());
                }
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void jelszoMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        PasswordChange form = new PasswordChange();
        if (cfg.getcurrentUser() != 0 &&
            cfg.getconnOK() &&
            cfg.getopenForms(form.getformID()) == false)
        {
            Desktop.add(form);
            form.setVisible(true);
            cfg.setopenForm(true, form.getformID());
        }
    }

    private void frissitMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            RefreshTables form = new RefreshTables();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void felhasznalokMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            Users form = new Users();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void munkahelyMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            WorkPlaces form = new WorkPlaces();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void napitervMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            DailyPlan form = new DailyPlan();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void napitervfejlecMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            DailyPlanTitle form = new DailyPlanTitle();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void hatekonysagMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            Efficiency form = new Efficiency();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void muhelyMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            WorkCenters form = new WorkCenters();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void gepcsoportMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            Tools form = new Tools();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void leltarurlapMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            VeneerInventory form = new VeneerInventory();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void excelMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            ExcelTable form = new ExcelTable();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void szerszamokMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            ToolsInMachines form = new ToolsInMachines();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void anyagigenyaMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            AMaterialRequest form = new AMaterialRequest();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void anyagigenyopciokMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            MROptions form = new MROptions();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void visszajelentesMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            TimeRecording form = new TimeRecording();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void muszakbeosztMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            ShiftPlanner form = new ShiftPlanner();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void gyartasirendMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            POSelection form = new POSelection();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }


    private void gyartasirendbeallMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            POSetup form = new POSetup();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void rendelestervMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            POPlanner form = new POPlanner();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void uzeminaptarMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            ShopCalendar form = new ShopCalendar();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void utemekMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        if (cfg.getcurrentUser() > 0 && cfg.getconnOK())
        {
            Items form = new Items();
            JExtendedFrame formex = openedForm(form.getformID());
            if (formex != null)
            {
                try
                {
                    formex.setSelected(true);
                }
                catch (PropertyVetoException pve) {}
                form.dispose();
            }
            else
            {
                RolesRead(form.getformID());
                if (role && !cfg.getopenForms(form.getformID()))
                {
                    Desktop.add(form);
                    form.setVisible(true);
                    cfg.setopenForm(true, form.getformID());
                }
                else
                {
                    form.dispose();
                }
            }
        }
    }

    private void RolesRead(int formID)
    {
        Configuration cfg = new Configuration();
        role = false;
        if (cfg.getsqlConn().compareTo("") != 0)
        {
            try
            {
                rSet = null;
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement();
                rSet = stmt.executeQuery("SELECT role FROM roles WHERE userazon = '" +
                                         cfg.getcurrentUser() + "' AND urlapazon = '" +
                                         formID + "'");
                if (rSet.next())
                {
                    role = true;
                }
                conn.close();
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
            }
        }
    }

    private JExtendedFrame openedForm(int formId)
    {
        JExtendedFrame[] frames = Desktop.getExtFrames();
        for (int i = 0;i < frames.length;i++)
        {
            if (frames[i].getformID() == formId)
            {
                try
                {
                    frames[i].setIcon(false);
                }
                catch (PropertyVetoException pve) {}
                return frames[i];
            }
        }
        return null;
    }

    private void closeAllForm()
    {
        Configuration cfg = new Configuration();
        Component[] comp = Desktop.getComponents();
        for (int i = 0;i < comp.length;i++)
        {
            if (comp[i] instanceof JDesktopIcon)
            {
                ((JDesktopIcon)comp[i]).getInternalFrame().dispose();
            }
            else if (comp[i] instanceof JExtendedFrame)
            {
                ((JExtendedFrame)comp[i]).dispose();
            }
        }
        for (int i = 0;i<40;i++)
        {
            cfg.setopenForm(false, i);
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) throws Exception {
        NimRODTheme nt = new NimRODTheme();
        NimRODLookAndFeel NimRODLF = new NimRODLookAndFeel();
        NimRODLF.setCurrentTheme( nt);
        int style = Font.BOLD;
        Font font = new Font ("SansSerif", style , 12);
        nt.setPrimary1( new Color(229,189,0));
        nt.setPrimary2( new Color(239,199,0));
        nt.setPrimary3( new Color(249,209,0));
        nt.setSecondary1( new Color(217,215,173));
        nt.setSecondary2( new Color(227,225,183));
        nt.setSecondary3( new Color(237,235,193));
        nt.setBlack( new Color(0,0,0));
        nt.setWhite( new Color(255,255,255));
        nt.setFont(font);
        UIManager.setLookAndFeel( NimRODLF);
        lockFile = new File(DefaultDir + "lock");
        lockFile.createNewFile();
        lockFile.deleteOnExit();
        lockFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(lockFile);
        if (fos.getChannel().tryLock() == null) {
            return;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Start().setVisible(true);
            }
        });
    }

    private JExtendedDesktop Desktop;
    private JMenuItem anyagigenyaMenu, belepMenu, excelMenu, felhasznalokMenu, frissitMenu, gepcsoportMenu,
                      jelszoMenu, kapcsolatMenu, kilepMenu, leltarurlapMenu, muhelyMenu, munkahelyMenu,
                      napitervMenu, anyagigenymMenu, anyagigenyopciokMenu, napitervfejlecMenu,
                      visszajelentesMenu, muszakbeosztMenu, hatekonysagMenu, szerszamokMenu, gyartasirendMenu,
                      rendelestervMenu, uzeminaptarMenu, gyartasirendbeallMenu, utemekMenu;
    private JMenu mainMenu, opciokMenu, tervezesMenu, leltarMenu;
    private JMenuBar menuBar;
    private JLabel Label1,userLabel;
    private JPanel userPane;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet = null;
    private boolean role = Boolean.FALSE;
    private GraphicsEnvironment graphicsEnvironment;
    private Rectangle maximumWindowBounds;
    private static String DefaultDir = System.getProperty("java.io.tmpdir").replace("\\", "/");
    private static File lockFile;
}