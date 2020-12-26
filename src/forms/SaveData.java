/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import classes.NoEditModel;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.WorkbookSettings;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.format.Border;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Forcek László
 */
public class SaveData implements Runnable{

    public SaveData(String ev, String honap, String nap, String saveloc)
    {
        Ev = ev;
        Honap = honap;
        Nap = nap;
        saveLoc = saveloc;
    }

    @Override
    public void run()
    {
        Configuration cfg = new Configuration();
        XLSMent();
        cfg.setthreadFinished(true);
    }

    private void XLSMent()
    {
        try
        {
            try
            {
                String filename = saveLoc + "/Furnérletár" + Ev;
                filename += (Honap.length() < 2 ? "0" : "") + Honap;
                filename += (Nap.length() < 10 ? "0" : "") + Nap + ".xls";
                WorkbookSettings ws = new WorkbookSettings();
                ws.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook = Workbook.createWorkbook(new File(filename), ws);
                for (int i = 0;i < ExcelSheets.length;i++)
                {
                    workbook.createSheet(ExcelSheets[i], i);
                    WritableSheet munkalap = workbook.getSheet(i);
                    if (i < ExcelSheets.length - 1) 
                    {
                        Fejlec(munkalap,Fejlecalap);
                        Tablafeltolt(Fejlecalap,slqComm[i]);
                    }
                    else 
                    {
                        Fejlec(munkalap,Fejlecmodositott);
                        Tablafeltolt(Fejlecmodositott,slqComm[i]);
                    }
                    writeData(munkalap,XLStabla);
                }
                workbook.write();
                workbook.close();
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null,"Hiba a fájl megnyitásakor: " + ex.getMessage());
            }
        }
        catch (WriteException ex)
        {
            JOptionPane.showMessageDialog(null,"Hiba az excel fájl létrehozásakor: " + ex.getMessage());
        }
    }

    private void Tablafeltolt(String[] fejlec,String sqlComm)
    {
        try
        {
            XLStabla = null;
            XLStabla = new JTable();
            Configuration cfg = new Configuration();
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            sqlolvas(sqlComm);
            if (rSet.next())
            {
                Vector<String> tableHeaders = new Vector<String>();
                Vector tableData = new Vector();
                for (int i = 0;i < fejlec.length;i++) tableHeaders.add(fejlec[i]);
                do
                {
                    Vector<Object> oneRow = new Vector<Object>();
                    for (int i = 0;i < fejlec.length;i++) oneRow.add(rSet.getString(i + 1));
                    tableData.add(oneRow);
                } while (rSet.next());
                conn.close();
                XLStabla.setModel(new NoEditModel(tableData, tableHeaders));
            }
            else
            {
                conn.close();
            }
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
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

    private static void writeDataSheet(WritableSheet munkalap, int oszlop1, int sor1, int oszlop2, int sor2,
                                       WritableFont font, BorderLineStyle top, BorderLineStyle bottom, BorderLineStyle left,
                                       BorderLineStyle right, Colour color, Alignment halign, VerticalAlignment valign, String text)
    throws WriteException
    {
        /* Format the Font */

        WritableCellFormat cf = new WritableCellFormat(font);
        cf.setAlignment(halign);
        cf.setVerticalAlignment(valign);
        cf.setBorder(Border.TOP, top, color);
        cf.setBorder(Border.BOTTOM, bottom, color);
        cf.setBorder(Border.LEFT, left, color);
        cf.setBorder(Border.RIGHT, right, color);
        cf.setWrap(false);
        Label fejlec = new Label(oszlop1, sor1, text, cf);
        munkalap.mergeCells(oszlop1, sor1, oszlop2, sor2);
        munkalap.addCell(fejlec);
    }

    private static void writeData(WritableSheet munkalap, JTable tabla)
    throws WriteException
    {
        for (int i = 0; i < tabla.getRowCount();i++)
        {
            for (int j = 0; j < tabla.getColumnCount();j++)
            {
                if ((j % 2) == 0 && j != 0)
                {
                    Number adat = new Number(j,i+1,Double.parseDouble(tabla.getValueAt(i, j).toString()),Cellformat[j]);
                    munkalap.addCell(adat);
                }
                else
                {
                    Label adat = new Label(j,i+1,tabla.getValueAt(i, j).toString(),Cellformat[j]);
                    munkalap.addCell(adat);
                }
            }
        }
    }

    private void Fejlec(WritableSheet munkalap,String[] fejlec)
    {
        try
        {
            WritableFont wf = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
            for (int i = 0;i < fejlec.length;i++)
            {
                writeDataSheet(munkalap,i,0,i,0,wf,BorderLineStyle.MEDIUM,BorderLineStyle.MEDIUM,
                               BorderLineStyle.MEDIUM,BorderLineStyle.MEDIUM,Colour.BLACK,
                               Alignment.CENTRE,VerticalAlignment.TOP,fejlec[i]);
            }
        }
        catch (WriteException ex)
        {
            JOptionPane.showMessageDialog(null,"Hiba az excel fájl létrehozásakor: " + ex.getMessage());
        }
    }

    private JTable XLStabla;
    private Connection conn = null;
    private Statement stmt;
    private String Ev, Honap, Nap, saveLoc;
    private ResultSet rSet;
    private String[] ExcelSheets = {"Leltárhiány","Leltártöbblet","Hibás rakatszámok"};
    private String[] Fejlecalap = {"Rakatszám","Cikkszám","Mennyiség"};
    private String[] Fejlecmodositott = {"Rakatszám","Eredeti cikkszám","Eredeti Mennyiség",
                                         "Leltározott cikkszám","Leltározott mennyiség"};
    private static WritableCellFormat[] Cellformat = {new WritableCellFormat(NumberFormats.TEXT),
                                                      new WritableCellFormat(NumberFormats.TEXT),
                                                      new WritableCellFormat(NumberFormats.FLOAT),
                                                      new WritableCellFormat(NumberFormats.TEXT),
                                                      new WritableCellFormat(NumberFormats.FLOAT)};

    private String[] slqComm = {"SELECT furnerraktar.rakatszam,furnerraktar.cikkszam," + //hiány
                                "furnerraktar.mennyiseg FROM furnerraktar " +
                                "LEFT OUTER JOIN furnerleltar ON " +
                                "(furnerraktar.rakatszam = furnerleltar.rakatszam) " +
                                "WHERE  isnull(furnerleltar.rakatszam)",
                                "SELECT furnerleltar.rakatszam,furnerleltar.cikkszam," + //többlet
                                "furnerleltar.mennyiseg FROM furnerleltar " +
                                "LEFT OUTER JOIN furnerraktar ON " +
                                "(furnerleltar.rakatszam = furnerraktar.rakatszam) " +
                                "WHERE isnull(furnerraktar.rakatszam)",
                                "SELECT furnerraktar.rakatszam,furnerraktar.cikkszam AS ered_cikkszam," +
                                "furnerraktar.mennyiseg AS ered_mennyiseg,furnerleltar.cikkszam," +
                                "furnerleltar.mennyiseg FROM furnerraktar " +
                                "INNER JOIN furnerleltar ON (furnerraktar.rakatszam = furnerleltar.rakatszam) " +
                                "WHERE furnerleltar.cikkszam <> furnerraktar.cikkszam OR " +
                                "furnerleltar.mennyiseg <> furnerraktar.mennyiseg"};
}
