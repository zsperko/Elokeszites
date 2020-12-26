/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.ListItem;
import classes.SpringUtilities;
import classes.JExtendedFrame;
import dialogs.ReportDialog;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import org.freixas.jcalendar.JCalendarCombo;

/**
 *
 * @author Forcek Laszlo
 */
public class ToolsInMachines extends JExtendedFrame{

    public ToolsInMachines()
    {
        initComponents();
    }

    private void initComponents()
    {
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/machines.png"));
        setFrameIcon(icon);
        Toolkit defaultTool = Toolkit.getDefaultToolkit();
        setPreferredSize(new Dimension((int)(defaultTool.getScreenSize().getWidth() - 100),
                                       (int)(defaultTool.getScreenSize().getHeight() - 150)));
        setTitle("Aktuális szerszámok");
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

        menuBar = new JMenuBar();
        mainMenu = new JMenu();
        mentesMenu = new JMenuItem();
        szerszamlistaMenu = new JMenuItem();
        szerszamcsereMenu = new JMenuItem();
        kilepMenu = new JMenuItem();
        Megjegyzes = new JTextArea(7,20);
        Szerszamok = new JComboBox();
        Zarolt = new JCheckBox();
        mScroll = new JScrollPane();
        KovSzerszam = new JComboBox();
        Muszak = new JComboBox();
        Datum = new JCalendarCombo();

        mainMenu.setText("Menü");

        mScroll.setViewportView(Megjegyzes);

        mentesMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        mentesMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/disk.png"))); // NOI18N
        mentesMenu.setText("Mentés");
        mentesMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mentesMenuActionPerformed(evt);
            }
        });
        mainMenu.add(mentesMenu);
        mainMenu.add(new JSeparator());

        szerszamlistaMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        szerszamlistaMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/report_simple.png"))); // NOI18N
        szerszamlistaMenu.setText("Szerszámlista");
        szerszamlistaMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                szerszamlistaMenuActionPerformed(evt);
            }
        });
        mainMenu.add(szerszamlistaMenu);

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

        DataRead();

        toolsPane = new DrawPane();
        toolsPane.setLayout(null);
        toolsPane.setPreferredSize(new Dimension(width * 3 + 20, SquaresNest.length * height + 1));
        ScrollPane = new JScrollPane(toolsPane);
        ScrollPane.setBorder(new EtchedBorder());
        ScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Comboboxtolt(Szerszamok,"SELECT szerszamok.azon,szerszamok.szam FROM szerszamok ORDER BY szerszamok.szam");
        Comboboxtolt(KovSzerszam,"SELECT szerszamok.azon,szerszamok.szam FROM szerszamok ORDER BY szerszamok.szam");
        Comboboxtolt(Muszak,"SELECT azon,megnevezes FROM muszakok ORDER BY megnevezes");

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());

        JLabel Label1 = new JLabel("Szerszámlista");
        JLabel Label2 = new JLabel("Megjegyzés");
        JLabel Label3 = new JLabel("Zárolva");
        JLabel Label4 = new JLabel("Következő szerszám");
        JLabel Label5 = new JLabel("Bekerülés dátuma");
        JLabel Label6 = new JLabel("Műszak");

        inputPane.add(Label1);
        Label1.setLabelFor(Szerszamok);
        inputPane.add(Szerszamok);

        inputPane.add(Label2);
        Label2.setLabelFor(mScroll);
        inputPane.add(mScroll);

        inputPane.add(Label3);
        Label3.setLabelFor(Zarolt);
        inputPane.add(Zarolt);

        inputPane.add(Label4);
        Label4.setLabelFor(KovSzerszam);
        inputPane.add(KovSzerszam);

        inputPane.add(Label5);
        Label5.setLabelFor(Datum);
        inputPane.add(Datum);

        inputPane.add(Label6);
        Label6.setLabelFor(Muszak);
        inputPane.add(Muszak);

        SpringUtilities.makeCompactGrid(inputPane,
                                        6, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad
        
        contentPane.add(ScrollPane);
        contentPane.add(inputPane);

        //ScrollPane
        layout.putConstraint(SpringLayout.WEST, ScrollPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, ScrollPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, ScrollPane,
                             1,
                             SpringLayout.SOUTH, contentPane);

        //ScrollPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.EAST, ScrollPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);

        pack();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
    }

    private void mentesMenuActionPerformed(java.awt.event.ActionEvent evt) {
        if (currentSquareIndex != -1)
        {
            try
            {
                Configuration cfg = new Configuration();
                int i = (Zarolt.isSelected() ? 1 : 0);
                conn = DriverManager.getConnection(cfg.getsqlConn());
                stmt = conn.createStatement();
                sqlupdate("UPDATE szerszamokmuhelyben SET szerszamazon = '"
                        + ComboID(Szerszamok) + "',"
                        + "megjegyzes = '" + Megjegyzes.getText() + "',"
                        + "zarolt = '" + i + "',kovszerszam = '" 
                        + ComboID(KovSzerszam) + "', "
                        + "bdatum = '" + cfg.sqlDate(Datum.getDate()) + "',muszak = '"
                        + ComboID(Muszak) + "' "
                        + "WHERE azon = '"
                        + SquaresNest[currentSquareIndex].getAzon() + "'");
                NestRectangle rec = new NestRectangle(width,
                                                      currentSquareIndex * height,
                                                      width,
                                                      height,
                                                      SquaresNest[currentSquareIndex].getAzon(),
                                                      SquaresNest[currentSquareIndex].getMachine(),
                                                      ComboVAL(Szerszamok),
                                                      Megjegyzes.getText(),i,
                                                      ComboVAL(KovSzerszam),
                                                      cfg.sqlDate(Datum.getDate()),
                                                      ComboID(Muszak));
                NextRectangle rec2 = new NextRectangle(width * 2,currentSquareIndex * height,
                                                       width, height,
                                                       ComboVAL(KovSzerszam));
                SquaresNest[currentSquareIndex] = rec;
                SquaresNext[currentSquareIndex] = rec2;
                repaint();
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
    }

    private void szerszamlistaMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Report();
    }
    
    private void kilepMenuActionPerformed(java.awt.event.ActionEvent evt) {
        Configuration cfg = new Configuration();
        cfg.setopenForm(false, formID);
        dispose();
    }

    private void DataRead()
    {
        try
        {
            Configuration cfg = new Configuration();
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas("SELECT azon,szam,feszek FROM muhelyek WHERE tervezes = 1 ORDER BY szam");
            rSet.last();
            Squares = new MachinesRectangle[rSet.getRow()];
            rSet.beforeFirst();
            int i = 0;
            int yPoz = 0;
            int nests = 0;
            while(rSet.next())
            {
                Squares[i] = new MachinesRectangle(0, yPoz, width, height * rSet.getInt(3),rSet.getInt(1),rSet.getString(2));
                nests += rSet.getInt(3);
                yPoz += height * rSet.getInt(3);
                i++;
            }
            sqlolvas("SELECT szerszamokmuhelyben.azon,szerszamokmuhelyben.muhelyazon,szerszamok.szam,"
                   + "szerszamokmuhelyben.megjegyzes,szerszamokmuhelyben.zarolt,szerszamok1.szam,"
                   + "szerszamokmuhelyben.bdatum,szerszamokmuhelyben.muszak FROM szerszamokmuhelyben "
                   + "INNER JOIN muhelyek ON (szerszamokmuhelyben.muhelyazon = muhelyek.azon) "
                   + "INNER JOIN szerszamok ON (szerszamokmuhelyben.szerszamazon = szerszamok.azon) "
                   + "INNER JOIN szerszamok szerszamok1 ON (szerszamokmuhelyben.kovszerszam = szerszamok1.azon) "
                   + "ORDER BY muhelyek.szam,szerszamokmuhelyben.feszek");
            SquaresNest = new NestRectangle[nests];
            i = 0;
            while(rSet.next())
            {
                SquaresNest[i] = new NestRectangle(width, i * height, width, height,
                                                   rSet.getInt(1),rSet.getInt(2),
                                                   rSet.getString(3),rSet.getString(4),
                                                   rSet.getInt(5),rSet.getString(6),
                                                   rSet.getString(7),rSet.getInt(8));
                i++;
            }
            sqlolvas("SELECT szerszamok.szam FROM szerszamokmuhelyben "
                   + "INNER JOIN muhelyek ON (szerszamokmuhelyben.muhelyazon = muhelyek.azon) "
                   + "INNER JOIN szerszamok ON (szerszamokmuhelyben.kovszerszam = szerszamok.azon) "
                   + "ORDER BY muhelyek.szam,szerszamokmuhelyben.feszek");
            SquaresNext = new NextRectangle[nests];
            i = 0;
            while(rSet.next())
            {
                SquaresNext[i] = new NextRectangle(width + width, i * height, width, height,
                                                   rSet.getString(1));
                i++;
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

    class DrawPane extends JPanel{

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(sanSerifFont);
            for (int i = 0; i < Squares.length; i++){
                g.setColor(Color.BLACK);
                ((Graphics2D)g).draw(Squares[i]);
                g.setColor(Color.GREEN);
                g.fillRect((int)Squares[i].getX() + 1,
                           (int)Squares[i].getY() + 1,
                           (int)Squares[i].getWidth() - 1,
                           (int)Squares[i].getHeight() - 1);
                FontMetrics fm = g.getFontMetrics();
                int w = fm.stringWidth(Squares[i].getName());
                g.setColor(Color.RED);
                ((Graphics2D)g).drawString(Squares[i].getName(),
                                          (int)(width / 2 - (w / 2)),
                                          (int)(Squares[i].getY() + Squares[i].getHeight() / 2));
            }
            for (int i = 0; i < SquaresNest.length; i++){
                g.setColor(Color.BLACK);
                ((Graphics2D)g).draw(SquaresNest[i]);
                g.setColor(Color.YELLOW);
                g.fillRect((int)SquaresNest[i].getX() + 1,
                           (int)SquaresNest[i].getY() + 1,
                           (int)SquaresNest[i].getWidth() - 1,
                           (int)SquaresNest[i].getHeight() - 1);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(Color.BLACK);
                int w = fm.stringWidth(SquaresNest[i].getToolName());
                ((Graphics2D)g).drawString(SquaresNest[i].getToolName(),
                                          (int)(width + width / 2 - (w / 2)),
                                          (int)(SquaresNest[i].getY() + SquaresNest[i].getHeight() / 2));
            }
            for (int i = 0; i < SquaresNext.length; i++){
                g.setColor(Color.BLACK);
                ((Graphics2D)g).draw(SquaresNext[i]);
                g.setColor(Color.GRAY);
                g.fillRect((int)SquaresNext[i].getX() + 1,
                           (int)SquaresNext[i].getY() + 1,
                           (int)SquaresNext[i].getWidth() - 1,
                           (int)SquaresNext[i].getHeight() - 1);
                FontMetrics fm = g.getFontMetrics();
                g.setColor(Color.BLACK);
                int w = fm.stringWidth(SquaresNext[i].getToolName());
                ((Graphics2D)g).drawString(SquaresNext[i].getToolName(),
                                          (int)(2 * width + width / 2 - (w / 2)),
                                          (int)(SquaresNext[i].getY() + SquaresNext[i].getHeight() / 2));
            }
        }
        
        public DrawPane() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent evt) {
                    
                }

                @Override
                public void mouseClicked(MouseEvent evt) {
                    int x = evt.getX();
                    int y = evt.getY();
                    Configuration cfg = new Configuration();
                    currentSquareIndex = getSquare(x, y);
                    if (currentSquareIndex != -1)
                    {
                        Combokeres(Szerszamok,SquaresNest[currentSquareIndex].getToolName(),1);
                        Combokeres(KovSzerszam,SquaresNest[currentSquareIndex].getNextTool(),1);
                        if (SquaresNest[currentSquareIndex].getShift() != -1)
                            Combokeres(Muszak,Integer.toString(SquaresNest[currentSquareIndex].getShift()),2);
                        Megjegyzes.setText(SquaresNest[currentSquareIndex].getMegjegyzes());
                        Zarolt.setSelected(false);
                        if (SquaresNest[currentSquareIndex].getDate() != null)
                            Datum.setDate(cfg.sqlToDate(SquaresNest[currentSquareIndex].getDate()));
                        if (SquaresNest[currentSquareIndex].getZarolva() == 1) Zarolt.setSelected(true);
                    }
                }
            });
        }

        public int getSquare(int x, int y) {
            for (int i = 0; i < SquaresNest.length; i++)
                if(SquaresNest[i].contains(x,y))
                    return i;
            return -1;
        }
    }

    class MachinesRectangle extends Rectangle{
        public MachinesRectangle(int x, int y, int width, int height, int Azon, String Name)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.Azon = Azon;
            this.Name = Name;
        }

        public int getAzon()
        {
            return Azon;
        }

        public String getName()
        {
            return Name;
        }

        private String Name;
        private int Azon;
    }

    class NestRectangle extends Rectangle{

        public NestRectangle(int x, int y, int width, int height, int Azon, int Machine, String ToolName,
                             String Megjegyzes, int Zarolva, String NextTool, String Date, int Shift)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.Azon = Azon;
            this.Machine = Machine;
            this.ToolName = ToolName;
            this.Megjegyzes = Megjegyzes;
            this.Zarolva = Zarolva;
            this.NextTool = NextTool;
            this.Date = Date;
            this.Shift = Shift;
        }

        public int getAzon()
        {
            return Azon;
        }

        public int getMachine()
        {
            return Machine;
        }

        public String getToolName()
        {
            return ToolName;
        }

        public String getMegjegyzes()
        {
            return Megjegyzes;
        }

        public int getZarolva()
        {
            return Zarolva;
        }

        public String getNextTool()
        {
            return NextTool;
        }

        public String getDate()
        {
            return Date;
        }

        public int getShift()
        {
            return Shift;
        }

        private int Azon, Machine, Zarolva, Shift;
        private String ToolName, Megjegyzes, NextTool, Date;
    }

    class NextRectangle extends Rectangle{

        public NextRectangle(int x, int y, int width, int height, String ToolName)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.ToolName = ToolName;
        }

        public String getToolName()
        {
            return ToolName;
        }

        private String ToolName;
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
        String riportName = "reports/szerszamlista.jasper";
        if (riportName.compareTo("") != 0)
        {
            Configuration.setParamName("user", 0);
            Configuration.setParamValue("admin", 0);
            Configuration.setReportName(riportName);
            ReportDialog rdialog = new ReportDialog();
            rdialog.setVisible(true);
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
    private int width = 70, height = 50;
    private int formID = 18;
    private int currentSquareIndex;
    private DrawPane toolsPane;
    private MachinesRectangle[] Squares;
    private NestRectangle[] SquaresNest;
    private NextRectangle[] SquaresNext;
    private JScrollPane ScrollPane, mScroll;
    private JMenuBar menuBar;
    private JMenu mainMenu;
    private JMenuItem mentesMenu, szerszamlistaMenu, szerszamcsereMenu, kilepMenu;
    private JPanel inputPane;
    private JComboBox Szerszamok, KovSzerszam, Muszak;
    private JCheckBox Zarolt;
    private JCalendarCombo Datum;
    private JTextArea Megjegyzes;
    private static Font sanSerifFont = new Font("SanSerif", Font.BOLD, 12);
}