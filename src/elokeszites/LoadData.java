/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFileChooser;
import jxl.*;
import java.util.*;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.read.biff.*;

/**
 *
 * @author Forcek László
 */
public class LoadData {


    public void XlsToSql(String ev,String honap)
    {
        Ev = ev;
        Honap = honap;
        XlsOlvas();
    }

    private void XlsOlvas()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(defaultDir));
        fileChooser.setDialogTitle("Válassza ki az állományt");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(new XLSSzuro());
        int status = fileChooser.showOpenDialog(null);
        if (status == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fileChooser.getSelectedFile();
            try
            {
                String filename = selectedFile.getAbsolutePath();
                WorkbookSettings ws = new WorkbookSettings();
                ws.setLocale(new Locale("hu", "HU"));
                ws.setEncoding ("ISO8859_2");
                Workbook workbook = Workbook.getWorkbook(new File(filename),ws);
                Sheet sheet  = workbook.getSheet(0);
                findColumns(sheet);
                if (CikkIdx != 0 && MennyiIdx != 0 && RaktarIdx != 0)
                {
                    Vector<String> tableHeaders = new Vector<String>();
                    Vector tableData = new Vector();
                    tableHeaders.add("Cikkszám");
                    tableHeaders.add("Mennyiség");
                    tableHeaders.add("Raktár");
                    int rows = sheet.getRows();
                    Cell cell = null;
                    for (int i = 1;i < rows;i++)
                    {
                        Vector<Object> oneRow = new Vector<Object>();
                        cell = sheet.getCell(CikkIdx, i);
                        oneRow.add(cell.getContents());
                        cell = sheet.getCell(MennyiIdx, i);
                        oneRow.add(ConvertToNumber(cell.getContents()));
                        cell = sheet.getCell(RaktarIdx, i);
                        oneRow.add(cell.getContents());
                        tableData.add(oneRow);
                    }
                    xlsBeolvas = new JTable();
                    xlsBeolvas.setModel(new NoEditModel(tableData, tableHeaders));
                }
                workbook.close();
                tableToCSV();
                LoadToServer();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (BiffException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void findColumns(Sheet s)
    {
        int columns = s.getColumns();
        Cell cell = null;
        for (int i = 0;i < columns;i++)
        {
            cell = s.getCell(i, 0);
            if (cell.getContents().compareTo("Cikkszám") == 0) CikkIdx = i;
            else if (cell.getContents().compareTo("Raktárkód") == 0) RaktarIdx = i;
            else if (cell.getContents().compareTo("Mennyiség (leltár)") == 0) MennyiIdx = i;
        }
    }

    private double ConvertToNumber(String value)
    {
        char[] numbers = {'0','1','2','3','4','5','6','7','8','9','.',','};
        Arrays.sort(numbers);
        String temp = "";
        for (int i = 0;i < value.length();i++)
        {
            char ch = value.charAt(i);
            int intResult = Arrays.binarySearch(numbers,ch);
            if (intResult > -1) temp += ch;
        }
        double d = 0;
        try
        {
            d = Double.parseDouble(temp.replace(",","."));
        }
        catch (Exception ex){System.out.println(ex);}
        return d;
    }

    private void tableToCSV()
    {
        try
        {
            String filenev = "c:/leltár.csv";
            FileOutputStream outFile = new FileOutputStream(filenev);
            OutputStreamWriter outStream = new OutputStreamWriter(outFile,"ISO-8859-2");
            PrintWriter out = new PrintWriter(outStream);
            out.println("Cikkszam;Mennyiseg;Raktarkod;Ev;Honap");
            for (int i = 0;i < xlsBeolvas.getRowCount();i++)
            {
                String temp = xlsBeolvas.getValueAt(i, 0).toString() + ";" +
                              xlsBeolvas.getValueAt(i, 1).toString() + ";" +
                              xlsBeolvas.getValueAt(i, 2).toString() + ";" +
                              Ev + ";" + Honap;
                out.println(temp);
            }
            out.close();
        }
        catch (IOException iex)
        {
            System.out.println("Hiba a fájl mentésekor:" + iex.getMessage());
        }
    }

    private void LoadToServer()
    {
        String sqlComm = "LOAD DATA LOCAL INFILE '" +
                         "C:/leltár.csv' " +
                         "INTO TABLE leltarnaplo_nav " +
                         "CHARACTER SET latin2 " +
                         "FIELDS TERMINATED BY ';' " +
                         "LINES TERMINATED BY '\r\n' " +
                         "IGNORE 1 LINES (Cikkszam,Mennyiseg,Raktarkod,Ev,Honap)";
        Configuration cfg = new Configuration();
        try
        {
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            sqlupdate(sqlComm);
            conn.commit();
            conn.close();
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        finally
        {
            try{conn.close();}
            catch (SQLException ex){}
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
            System.out.println("Kapcsolódási hiba: " + ex.getMessage());
        }
    }

    private Connection conn = null;
    private Statement stmt;
    private JTable xlsBeolvas;
    private String defaultDir = "c:/";
    private String Ev,Honap;
    private int CikkIdx = 0,MennyiIdx = 0,RaktarIdx = 0;
}
