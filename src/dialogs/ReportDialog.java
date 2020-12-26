/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dialogs;

import forms.Configuration;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author Forcek László1
 */
public class ReportDialog extends JDialog{
    
    public ReportDialog()
    {
        FillReport();
        initComponents();
    }
    
    private void initComponents()
    {
        setVisible(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);
        ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/report.png"));
        setIconImage(icon.getImage());
        Dimension dim = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
        dim.setSize(dim.getWidth() - 50, dim.getHeight() - 50);
        
        viewer = new JRViewer(jasperPrint);
        viewer.setSize(dim);
        viewer.setVisible(true);
        viewer.setEnabled(true);
        
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
            @Override
            public void windowClosed(WindowEvent e) {
                dispose();
            }
        });

        getContentPane().add(viewer);
        setLocation(10, 10);
        setPreferredSize(dim);
        setTitle("Riport előnézet");
        setResizable(false);
        pack();
        if (!hasPages)
        {
            dispose();
        }
        else
        {
            setVisible(true);
        }
    }
    
    private void FillReport()
    {
        Configuration.setThreadFinished(false);
        ArrayList<String> MessageList = new ArrayList<>();
        MessageList.add("Riport megnyitása...");
        AnimatedDialog form = new AnimatedDialog(MessageList);
        ReportThread th;
        Thread thread;
        th = new ReportThread();
        thread = new Thread(th);
        thread.start();
        form.setVisible(true);
    }
    
    class ReportThread implements Runnable {

        @Override
        public void run()
        {
            InputStream reportSource = this.getClass().getClassLoader().
                                       getResourceAsStream(Configuration.getReportName());
            ResultSet sqlresults = null;
            Statement stmt = null;
            Connection connection = null;
            try
            {
                connection = DriverManager.getConnection(Configuration.getSqlConn());
                Map<String, Object> params = new HashMap<>();
                try
                {
                    int i = 0;
                    while (Configuration.getParamName(i).compareTo("") != 0 && i < 10)
                    {
                        params.put(Configuration.getParamName(i), Configuration.getParamValue(i));
                        i++;
                    }
                    jasperPrint = JasperFillManager.fillReport(reportSource, params, connection); 
                    hasPages = !jasperPrint.getPages().isEmpty();
                }
                catch (JRException ex)
                {
                    JOptionPane.showMessageDialog(null, "Hiba a riport megjelenítésekor: " + ex.getMessage());
                }
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(null, "Kapcsolódási hiba: " + ex.getMessage());
            }
            finally
            {
                try
                {connection.close();}
                catch (SQLException ex){}
            }
            Configuration.setThreadFinished(true);
        }
    }
    
    private JRViewer viewer;    
    private JasperPrint jasperPrint;
    private boolean hasPages;
}
