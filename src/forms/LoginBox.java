/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.resizeImageIcon;
import classes.SpringUtilities;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Forcek László
 */
public final class LoginBox extends JDialog implements ActionListener{

    public LoginBox()
    {
        initComponents();
    }

    public void initComponents()
    {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/connect.png"));
        setIconImage(icon.getImage());

        addWindowListener( new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                Configuration cfg = new Configuration();
                if (cfg.getsqlConn().compareTo("") == 0)
                {
                    userName.setText("admin");
                    userName.setEditable(false);
                    passField.grabFocus();
                }
            }
        });

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dim.getSize().getWidth() / 2) - 180,
                           (int)(dim.getSize().getHeight() / 2) -90);
        setTitle("Belépés");
        setResizable(false);

        Container contentPane = getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        imgPane =  new JPanel();
        imgPane.setLayout(new BorderLayout());
        imgPane.setBorder(new EtchedBorder());
        inputPane = new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        btnPane = new JPanel(new BorderLayout());

        JLabel userlabel = new JLabel("Felhasználónév");
        JLabel passlabel = new JLabel("Jelszó");

        userName = new JTextField(10);
        passField = new JPasswordField(10);
        imageLabel = new JLabel();
        acceptButton = new JButton("Belépés");
        cancelButton = new JButton("Mégse");

        getRootPane().setDefaultButton(acceptButton);

        inputPane.add(userlabel);
        userlabel.setLabelFor(userName);
        inputPane.add(userName);

        inputPane.add(passlabel);
        passlabel.setLabelFor(passField);
        inputPane.add(passField);

        btnPane.add("West",acceptButton);
        btnPane.add("East",cancelButton);
        acceptButton.addActionListener(this);
        cancelButton.addActionListener(this);
        //btnPane.add(cancelButton);

        SpringUtilities.makeCompactGrid(inputPane,
                                        2, 2, //rows, cols
                                        10, 10,//initX, initY
                                        5, 5);//xPad, yPad

        icon = new javax.swing.ImageIcon(getClass().getResource("/icons/java.gif"));
        resizeImageIcon img = new resizeImageIcon(icon,75,130);
        imageLabel.setIcon(img.getImage());

        imgPane.add(imageLabel);
        contentPane.add(imgPane);
        contentPane.add(inputPane);
        contentPane.add(btnPane);

        //imgPane
        layout.putConstraint(SpringLayout.WEST, imgPane,
                             5,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, imgPane,
                             5,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             5,
                             SpringLayout.SOUTH, imgPane);

        //inputPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             5,
                             SpringLayout.EAST, imgPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             5,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             5,
                             SpringLayout.EAST, inputPane);

        //btnPane
        layout.putConstraint(SpringLayout.WEST, btnPane,
                             5,
                             SpringLayout.EAST, imgPane);
        layout.putConstraint(SpringLayout.NORTH, btnPane,
                             5,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.EAST, btnPane,
                             0,
                             SpringLayout.EAST, inputPane);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource().equals(acceptButton))
        {
            String passStr = PassToString(passField.getPassword());
            Configuration cfg = new Configuration();
            if (cfg.getconfExist() == Boolean.FALSE)
            {
                if (passStr.equals(cfg.getdefaultPass()))
                {
                    cfg.setcurrentUser(-1);
                    cfg.setcurrentUserName(userName.getText());
                    dispose();
                }
                else
                {
                    passField.setText("");
                }
            }
            else
            {
                try
                {
                    conn = DriverManager.getConnection(cfg.getsqlConn());
                    stmt = conn.createStatement();
                    rSet = null;
                    rSet = stmt.executeQuery("SELECT COUNT(username) as username FROM users WHERE username = '" +
                                             userName.getText() + "'");
                    if (rSet.next())
                    {
                        rSet = null;
                        rSet = stmt.executeQuery("SELECT azon, username " +
                                                 "FROM users WHERE username = '" +
                                                 userName.getText() + "' AND password = MD5('" +
                                                 passStr + "')");
                        if (rSet.next())
                        {
                            rSet.first();
                            cfg.setcurrentUser(rSet.getInt(1));
                            cfg.setcurrentUserName(rSet.getString(2));
                            dispose();
                        }
                        else
                        {
                            passField.grabFocus();
                            passField.setText("");
                        }
                    }
                    else
                    {
                        userName.setText("");
                        passField.setText("");
                    }
                    conn.close();
                }
                catch (SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " +ex.getMessage());
                }
            }
        }
        else if(ae.getSource().equals(cancelButton))
        {
            dispose();
        }
    }

    private String PassToString(char[] pass)
    {
        String passStr = "";
        for (int i = 0;i < pass.length;i++) passStr += pass[i];
        return passStr;
    }

    private JPanel imgPane, inputPane, btnPane;
    private JTextField userName;
    private JPasswordField passField;
    private JLabel imageLabel;
    private JButton acceptButton, cancelButton;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet = null;
}