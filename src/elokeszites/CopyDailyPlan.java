/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import org.freixas.jcalendar.JCalendar;

/**
 *
 * @author Forcek László
 */
public class CopyDailyPlan extends JDialog implements ActionListener{

    public CopyDailyPlan()
    {
        initComponents();
    }

    public void initComponents()
    {
        Configuration cfg = new Configuration();
        cfg.setchangeEnable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Napiterv másolása");
        setResizable(false);
        setModal(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("icons/copy.png"));
        setIconImage(icon.getImage());
        contentPane = new JPanel();

        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        setContentPane(contentPane);

        inputPane =  new JPanel(new SpringLayout());
        inputPane.setBorder(new EtchedBorder());
        btnPane = new JPanel(new FlowLayout());
        btnPane.setBorder(new EtchedBorder());

        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE,-1);
        Datum = new JCalendar();

        Fejlec = new JCheckBox("Fejléc");
        Fejlec.setSelected(true);

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Mégsem");
        cancelButton.addActionListener(this);

        inputPane.add(Datum);
        inputPane.add(Fejlec);

        SpringUtilities.makeCompactGrid(inputPane,
                                        2, 1, //rows, cols
                                        0, 0,//initX, initY
                                        0, 5);//xPad, yPad

        getRootPane().setDefaultButton(okButton);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dim.getSize().getWidth() / 2) - 180,
                    (int)(dim.getSize().getHeight() / 2) - 90);

        btnPane.add(okButton);
        btnPane.add(cancelButton);

        contentPane.add(inputPane);
        contentPane.add(btnPane);

        //imgPane
        layout.putConstraint(SpringLayout.WEST, inputPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, inputPane,
                             1,
                             SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.EAST, contentPane,
                             1,
                             SpringLayout.EAST, inputPane);

        //btnPane
        layout.putConstraint(SpringLayout.WEST, btnPane,
                             1,
                             SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, btnPane,
                             1,
                             SpringLayout.SOUTH, inputPane);
        layout.putConstraint(SpringLayout.SOUTH, contentPane,
                             0,
                             SpringLayout.SOUTH, btnPane);
        layout.putConstraint(SpringLayout.EAST, btnPane,
                             0,
                             SpringLayout.EAST, inputPane);

        pack();
    }

    public void actionPerformed(ActionEvent ae)
    {
        if(ae.getSource().equals(cancelButton))
        {
            dispose();
        }
        else if(ae.getSource().equals(okButton))
        {
            Configuration cfg = new Configuration();
            cfg.setchangeEnable(true);
            cfg.setchoosenDate(cfg.sqlDate(Datum.getDate()));
            if (Fejlec.isSelected()) cfg.sethalfEnable(true);
            dispose();
        }
    }

    private JPanel inputPane, btnPane, contentPane;
    private JCheckBox Fejlec;
    private JButton okButton,cancelButton;
    private JCalendar Datum;
}
