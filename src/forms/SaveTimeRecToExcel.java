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
public class SaveTimeRecToExcel implements Runnable{

    public SaveTimeRecToExcel(String ev, String honap, String nap, String munkahely, String saveloc)
    {
        Ev = ev;
        Honap = honap;
        Nap = nap;
        saveLoc = saveloc;
        Datum = Ev + "-" + (Honap.length() < 2 ? "0" : "") + Honap + "-" + (Nap.length() < 2 ? "0" : "") + Nap;
        Munkahely = munkahely;
        sqlComm = "SELECT dtorzs.nev,muszakok.megnevezes,'Terv' as A2,"
                + "sum(napiterv.mennyiseg) AS mennyiseg,'Jó' as A3,"
                + "seged.mennyiseg AS teny,'Selejt' as A4,seged.selejt,"
                + "'Terv %' as A5,"
                + "sum(((gr_muveletterv.szemelyido * napiterv.mennyiseg) / 480)) AS teljesitmeny,"
                + "'Tény %' as A6,seged.teljesitmeny AS tenyszazalek,"
                + "'Eltérés Terv %' as A7,"
                + "sum(((gr_muveletterv.szemelyido * napiterv.mennyiseg) / 480)) - "
                + "seged.teljesitmeny as elterterv,"
                + "'Eltérés Tény %' as A8,1 - seged.teljesitmeny as elterteny "
                + "FROM napiterv INNER JOIN dtorzs ON (napiterv.dszam = dtorzs.dszam) "
                + "INNER JOIN muszakok ON (napiterv.muszak = muszakok.azon) "
                + "INNER JOIN gr_muveletterv ON (napiterv.grmuvazon = gr_muveletterv.azon) "
                + "INNER JOIN muhelyek ON (napiterv.muhelyazon = muhelyek.azon) "
                + "LEFT OUTER JOIN (SELECT visszajelent.dszam,sum(visszajelent.mennyiseg) AS mennyiseg,"
                + "sum(visszajelent.selejt) AS selejt,"
                + "sum(((gr_muveletterv.szemelyido * visszajelent.mennyiseg) / 480)) AS teljesitmeny "
                + "FROM visszajelent INNER JOIN gr_muveletterv ON (visszajelent.grszam = gr_muveletterv.grszam)"
                + " AND (visszajelent.muvszam = gr_muveletterv.muveletkapcsolo) "
                + "WHERE  visszajelent.datum = '" + Datum + "' "
                + "GROUP BY visszajelent.dszam) seged ON (napiterv.dszam = seged.dszam) "
                + "WHERE napiterv.datum = '" + Datum + "' AND muhelyek.ktghely = '"
                + Munkahely + "' GROUP BY dtorzs.nev,muszakok.megnevezes,seged.mennyiseg,"
                + "seged.selejt,seged.teljesitmeny ORDER BY muszakok.megnevezes,dtorzs.nev";
        Cellformats = new WritableCellFormat[16];
        WritableFont fontBold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        Cellformats[0] = BoldTextformat;
        Cellformats[0].setFont(fontBold);
        Cellformats[1] = BoldTextformat;
        Cellformats[1].setFont(fontBold);
        int i = 2;
        while (i < 16)
        {
            Cellformats[i] = NoBoldTextformat;
            i += 2;
        }
        Cellformats[3] = Numberformat;
        Cellformats[5] = Numberformat;
        Cellformats[7] = Numberformat;
        Cellformats[9] = Percentformat;
        Cellformats[11] = Percentformat;
        Cellformats[13] = Percentformat;
        Cellformats[15] = Percentformat;
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
                String filename = saveLoc + "/Visszajelentések" + Ev;
                filename += (Honap.length() < 2 ? "0" : "") + Honap;
                filename += (Nap.length() < 10 ? "0" : "") + Nap + ".xls";
                WorkbookSettings ws = new WorkbookSettings();
                ws.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook = Workbook.createWorkbook(new File(filename), ws);
                workbook.createSheet("Teljesítmények",0);
                WritableSheet munkalap = workbook.getSheet(0);
                Tablafeltolt();
                Fejlec(munkalap,Datum.replace("-", "."));
                writeData(munkalap,XLStabla);
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

    private void Tablafeltolt()
    {
        try
        {
            XLStabla = null;
            XLStabla = new JTable();
            Vector tableData = new Vector();
            Vector<String> tableHeaders = new Vector<String>();
            Vector<Object> oneRow = null;
            tableHeaders.add("Oszlop1");
            tableHeaders.add("Oszlop2");
            Configuration cfg = new Configuration();
            conn = DriverManager.getConnection(cfg.getsqlConn());
            stmt = conn.createStatement();
            sqlolvas(sqlComm);
            while (rSet.next())
            {
                for(int i = 1;i < 17;i++)
                {
                    if (oneRow != null && i % 2 == 0)
                    {
                        tableData.add(oneRow);
                    }
                    if (i % 2 != 0)
                    {
                        oneRow= new Vector<Object>();
                    }
                    if (rSet.getString(i) != null) oneRow.add(rSet.getString(i));
                    else oneRow.add("0");
                }
            }
            conn.close();
            XLStabla.setModel(new NoEditModel(tableData, tableHeaders));
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
        int formatInt = 0;
        int i = 0;
        while (i < tabla.getRowCount())
        {
            Label szoveg = new Label(0,i+1,tabla.getValueAt(i, 0).toString(),Cellformats[formatInt]);
            munkalap.addCell(szoveg);
            formatInt++;
            if (formatInt == 1)
            {
                Label muszak = new Label(1,i+1,tabla.getValueAt(i, 1).toString(),Cellformats[formatInt]);
                munkalap.addCell(muszak);
            }
            else
            {
                Number szam = new Number(1,i+1,Double.parseDouble(tabla.getValueAt(i, 1).toString()),Cellformats[formatInt]);
                munkalap.addCell(szam);
            }
            i++;
            formatInt++;
            if (formatInt == 16) formatInt = 0;
        }
    }

    private void Fejlec(WritableSheet munkalap,String fejlec)
    {
        try
        {
            WritableFont wf = new WritableFont(WritableFont.ARIAL,10, WritableFont.BOLD);
            writeDataSheet(munkalap,1,0,1,0,wf,BorderLineStyle.NONE,BorderLineStyle.NONE,
                           BorderLineStyle.NONE,BorderLineStyle.NONE,Colour.BLACK,
                           Alignment.CENTRE,VerticalAlignment.TOP,fejlec);
        }
        catch (WriteException ex)
        {
            JOptionPane.showMessageDialog(null,"Hiba az excel fájl létrehozásakor: " + ex.getMessage());
        }
    }

    private JTable XLStabla;
    private Connection conn = null;
    private Statement stmt;
    private String Ev, Honap, Nap, saveLoc, Datum, Munkahely;
    private ResultSet rSet;
    private static WritableCellFormat BoldTextformat = new WritableCellFormat(NumberFormats.TEXT);
    private static WritableCellFormat NoBoldTextformat = new WritableCellFormat(NumberFormats.TEXT);
    private static WritableCellFormat Numberformat = new WritableCellFormat(NumberFormats.INTEGER);
    private static WritableCellFormat Percentformat = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
    private static WritableCellFormat[] Cellformats;
    private String sqlComm;
}
