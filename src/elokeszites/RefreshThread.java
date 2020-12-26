/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Forcek László
 */
public class RefreshThread implements Runnable{

    @Override
    public void run()
    {
        Configuration cfg = new Configuration();
        if (cfg.getrefreshTables(0))
        {
            Dtorzsfrissit();
        }
        if (cfg.getrefreshTables(1))
        {
            Grfrissit();
            Grdbjfrissit();
            Grmuvelettervfrissit();
        }
        if (cfg.getrefreshTables(2))
        {
            Keszletfrissit();
        }
        if (cfg.getrefreshTables(3))
        {
            Cikktorzsfrissit();
        }
        if (cfg.getrefreshTables(4))
        {
            Darabjegyzekfrissit();
        }
        if (cfg.getrefreshTables(5))
        {
            Muvelettervfrissit();
        }
        if (cfg.getrefreshTables(6))
        {
            Visszajelentfrissit();
        }
        if (cfg.getrefreshTables(7))
        {
            Furnerraktarfrissit();
        }
        cfg.setthreadFinished(true);
    }

    private void MultiSQLCommand(String sqlComm[],String TableName)
    {
        Configuration cfg = new Configuration();
        try
        {
            rSet = null;
            conn = DriverManager.getConnection(cfg.getsqlConn());
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rSet = stmt.executeQuery("SHOW TABLES LIKE '" + TableName + "'");
            if (rSet.next())
            {
                stmt.executeUpdate("DROP TABLE " + TableName + "");
            }
            for (int i = 0;i < sqlComm.length;i++)
            {
                sqlupdate(sqlComm[i]);
            }
            conn.commit();
            conn.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,ex.getMessage());
        }
    }

    private void Grfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[8];
        TableHeaders[0] = "rendszam";
        TableHeaders[1] = "cikkszam";
        TableHeaders[2] = "mennyiseg";
        TableHeaders[3] = "befejezett";
        TableHeaders[4] = "maradek";
        TableHeaders[5] = "selejt";
        TableHeaders[6] = "kezddatum";
        TableHeaders[7] = "letrehozasdatum";
        String TableName = "gyartasirend";
        String TempTable = "gyartasirend1";
        String sqlComm[] = new String[9];
        
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] += "UPDATE gyartasirend SET gyartasirend.nyitott = 1";
        sqlComm[3] = "UPDATE gyartasirend INNER JOIN gyartasirend1 ON " +
                     "(gyartasirend.rendszam = gyartasirend1.rendszam) SET " +
                     "gyartasirend.cikkszam = gyartasirend1.cikkszam," +
                     "gyartasirend.mennyiseg = gyartasirend1.mennyiseg," +
                     "gyartasirend.befejezett = gyartasirend1.befejezett," +
                     "gyartasirend.maradek = gyartasirend1.maradek," +
                     "gyartasirend.selejt = gyartasirend1.selejt " +
                     "WHERE gyartasirend.modositott = 1";
        sqlComm[4] = "UPDATE gyartasirend INNER JOIN gyartasirend1 ON " +
                     "(gyartasirend.rendszam = gyartasirend1.rendszam) SET " +
                     "gyartasirend.cikkszam = gyartasirend1.cikkszam," +
                     "gyartasirend.mennyiseg = gyartasirend1.mennyiseg," +
                     "gyartasirend.befejezett = gyartasirend1.befejezett," +
                     "gyartasirend.maradek = gyartasirend1.maradek," +
                     "gyartasirend.selejt = gyartasirend1.selejt," +
                     "gyartasirend.kezddatum = gyartasirend1.kezddatum " +
                     "WHERE gyartasirend.modositott = 0";
        sqlComm[5] = "INSERT INTO gyartasirend (gyartasirend.rendszam," +
                     "gyartasirend.cikkszam,gyartasirend.mennyiseg,gyartasirend.befejezett," +
                     "gyartasirend.maradek,gyartasirend.selejt,gyartasirend.kezddatum,gyartasirend.letrehozasdatum) " +
                     "SELECT gyartasirend1.rendszam," +
                     "gyartasirend1.cikkszam,gyartasirend1.mennyiseg,gyartasirend1.befejezett," +
                     "gyartasirend1.maradek,gyartasirend1.selejt,gyartasirend1.kezddatum,gyartasirend.letrehozasdatum " +
                     "FROM gyartasirend1 " +
                     "LEFT OUTER JOIN gyartasirend ON (gyartasirend.rendszam = gyartasirend1.rendszam) " +
                     "WHERE isnull(gyartasirend.rendszam)";
        sqlComm[6] = "UPDATE gyartasirend LEFT OUTER JOIN gyartasirend1 ON " +
                     "(gyartasirend.rendszam = gyartasirend1.rendszam) " +
                     "SET gyartasirend.nyitott = 0 WHERE " +
                     "isnull(gyartasirend1.rendszam) AND gyartasirend.nyitott = 1";
        Date date = new Date();
        sqlComm[7] = "UPDATE gyartasirend SET letrehozasdatum = '" + cfg.sqlDate(date) + "' WHERE " +
                     "isnull(letrehozasdatum)";
        sqlComm[8] = "DROP TABLE " + TempTable;
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Grdbjfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[8];
        TableHeaders[0] = "grszam";
        TableHeaders[1] = "sorszam";
        TableHeaders[2] = "cikkszam";
        TableHeaders[3] = "alkatresz";
        TableHeaders[4] = "osszmennyiseg";
        TableHeaders[5] = "maradek";
        TableHeaders[6] = "muveletkapcsolo";
        TableHeaders[7] = "raktar";
        String TableName = "gr_dbj";
        String TempTable = "gr_dbj1";
        String sqlComm[] = new String[5];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "CHARACTER SET latin2 " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        //2020.04.27 PZs
        //Módosítva lett a NAVISON-ban történt üzem összevonás miatt Megszünt a PRES
        //Helyette minden OWI_ZALA raktárba jön át
        //Mivel a NAVISION állomány nem hoz át raktár megnevezést ezért itt kerül bele
        //Közvetlenül bedrótozva
        /*sqlComm[2] = "UPDATE gr_dbj INNER JOIN gr_dbj1 ON (gr_dbj.grszam = gr_dbj1.grszam) " +
                     "AND (gr_dbj.sorszam = gr_dbj1.sorszam) SET " +
                     "gr_dbj.cikkszam = gr_dbj1.cikkszam,gr_dbj.alkatresz = gr_dbj1.alkatresz," +
                     "gr_dbj.osszmennyiseg = gr_dbj1.osszmennyiseg,gr_dbj.maradek = gr_dbj1.maradek," +
                     "gr_dbj.muveletkapcsolo = gr_dbj1.muveletkapcsolo,gr_dbj.raktar = gr_dbj1.raktar";
        */
        sqlComm[2] = "UPDATE gr_dbj INNER JOIN gr_dbj1 ON (gr_dbj.grszam = gr_dbj1.grszam) " +
                     "AND (gr_dbj.sorszam = gr_dbj1.sorszam) SET " +
                     "gr_dbj.cikkszam = gr_dbj1.cikkszam,gr_dbj.alkatresz = gr_dbj1.alkatresz," +
                     "gr_dbj.osszmennyiseg = gr_dbj1.osszmennyiseg,gr_dbj.maradek = gr_dbj1.maradek," +
                     "gr_dbj.muveletkapcsolo = gr_dbj1.muveletkapcsolo,gr_dbj.raktar = 'OWI_ZALA'";
        //2020.04.27 PZs. Lásd fentebb az indokot
        /*sqlComm[3] = "INSERT INTO gr_dbj (gr_dbj.grszam,gr_dbj.sorszam,gr_dbj.cikkszam," +
                     "gr_dbj.alkatresz,gr_dbj.osszmennyiseg,gr_dbj.maradek,gr_dbj.muveletkapcsolo,gr_dbj.raktar) " +
                     "SELECT gr_dbj1.grszam,gr_dbj1.sorszam,gr_dbj1.cikkszam,gr_dbj1.alkatresz," +
                     "gr_dbj1.osszmennyiseg,gr_dbj1.maradek,gr_dbj1.muveletkapcsolo,gr_dbj1.raktar " +
                     "FROM gr_dbj1 LEFT OUTER JOIN gr_dbj ON (gr_dbj1.grszam = gr_dbj.grszam) " +
                     "AND (gr_dbj1.sorszam = gr_dbj.sorszam) WHERE " +
                     "isnull(gr_dbj.sorszam) AND isnull(gr_dbj.grszam)";
        */
        sqlComm[3] = "INSERT INTO gr_dbj (gr_dbj.grszam,gr_dbj.sorszam,gr_dbj.cikkszam," +
                     "gr_dbj.alkatresz,gr_dbj.osszmennyiseg,gr_dbj.maradek,gr_dbj.muveletkapcsolo,gr_dbj.raktar) " +
                     "SELECT gr_dbj1.grszam,gr_dbj1.sorszam,gr_dbj1.cikkszam,gr_dbj1.alkatresz," +
                     "gr_dbj1.osszmennyiseg,gr_dbj1.maradek,gr_dbj1.muveletkapcsolo,'OWI_ZALA' AS raktar " +
                     "FROM gr_dbj1 LEFT OUTER JOIN gr_dbj ON (gr_dbj1.grszam = gr_dbj.grszam) " +
                     "AND (gr_dbj1.sorszam = gr_dbj.sorszam) WHERE " +
                     "isnull(gr_dbj.sorszam) AND isnull(gr_dbj.grszam)";

        sqlComm[4] = "DROP TABLE " + TempTable;
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Grmuvelettervfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[11];
        TableHeaders[0] = "grszam";
        TableHeaders[1] = "muveletkapcsolo";
        TableHeaders[2] = "tipus";
        TableHeaders[3] = "gepszam";
        TableHeaders[4] = "muhelykod";
        TableHeaders[5] = "megnevezes";
        TableHeaders[6] = "gepido";
        TableHeaders[7] = "szemelyido";
        TableHeaders[8] = "befejezettmennyiseg";
        TableHeaders[9] = "selejt";
        TableHeaders[10] = "nyitott";
        String TableName = "gr_muveletterv";
        String TempTable = "gr_muveletterv1";
        String sqlComm[] = new String[6];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "CHARACTER SET latin2 " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ");";
        sqlComm[2] = "UPDATE gr_muveletterv INNER JOIN gr_muveletterv1 ON " +
                     "(gr_muveletterv1.grszam = gr_muveletterv.grszam) AND " +
                     "(gr_muveletterv1.muveletkapcsolo = gr_muveletterv.muveletkapcsolo) " +
                     "SET gr_muveletterv.gepido = gr_muveletterv1.gepido," +
                     "gr_muveletterv.tipus = gr_muveletterv1.tipus," +
                     "gr_muveletterv.megnevezes = gr_muveletterv1.megnevezes," +
                     "gr_muveletterv.szemelyido = gr_muveletterv1.szemelyido," +
                     "gr_muveletterv.befejezettmennyiseg = gr_muveletterv1.befejezettmennyiseg," +
                     "gr_muveletterv.selejt = gr_muveletterv1.selejt," +
                     "gr_muveletterv.nyitott = gr_muveletterv1.nyitott";
        sqlComm[3] = "UPDATE gr_muveletterv LEFT OUTER JOIN gr_muveletterv1 ON " +
                     "(gr_muveletterv.grszam = gr_muveletterv1.grszam)AND " +
                     "(gr_muveletterv.muveletkapcsolo = gr_muveletterv1.muveletkapcsolo) " +
                     "SET gr_muveletterv.nyitott = 0 WHERE isnull(gr_muveletterv1.grszam) AND " +
                     "isnull(gr_muveletterv1.muveletkapcsolo)";
        sqlComm[4] = "INSERT INTO gr_muveletterv (gr_muveletterv.grszam,gr_muveletterv.tipus," +
                     "gr_muveletterv.gepszam,gr_muveletterv.muhelykod," +
                     "gr_muveletterv.muveletkapcsolo,gr_muveletterv.megnevezes," +
                     "gr_muveletterv.gepido,gr_muveletterv.szemelyido,gr_muveletterv.befejezettmennyiseg," +
                     "gr_muveletterv.selejt) SELECT gr_muveletterv1.grszam,gr_muveletterv1.tipus," +
                     "gr_muveletterv1.gepszam,gr_muveletterv1.muhelykod," +
                     "gr_muveletterv1.muveletkapcsolo,gr_muveletterv1.megnevezes," +
                     "gr_muveletterv1.gepido,gr_muveletterv1.szemelyido,gr_muveletterv1.befejezettmennyiseg," +
                     "gr_muveletterv1.selejt FROM gr_muveletterv1 LEFT OUTER JOIN gr_muveletterv ON " +
                     "(gr_muveletterv1.grszam = gr_muveletterv.grszam) AND " +
                     "(gr_muveletterv1.muveletkapcsolo = gr_muveletterv.muveletkapcsolo) WHERE " +
                     "isnull(gr_muveletterv.grszam) AND isnull(gr_muveletterv.muveletkapcsolo)";
        sqlComm[5] = "DROP TABLE " + TempTable;
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Dtorzsfrissit()
    {
        String TempTable = "dtorzs";
        String sqlComm[] = new String[2];
        sqlComm[0] = "CREATE TABLE dtorzs LIKE jelenlet.dtorzs;";
        sqlComm[1] = "INSERT INTO dtorzs SELECT * FROM jelenlet.dtorzs";
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Cikktorzsfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[11];
        TableHeaders[0] = "cikkszam";
        TableHeaders[1] = "megnevezes";
        TableHeaders[2] = "cikkcsoportkod";
        TableHeaders[3] = "minoseg";
        TableHeaders[4] = "hossz";
        TableHeaders[5] = "szelesseg";
        TableHeaders[6] = "vastagsag1";
        TableHeaders[7] = "vastagsag2";
        TableHeaders[8] = "anyagtipus";
        TableHeaders[9] = "darabjegyzek";
        TableHeaders[10] = "muveletterv";
        String TableName = "cikktorzs";
        String TempTable = "cikktorzs1";
        String sqlComm[] = new String[4];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "CHARACTER SET latin2 " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] = "INSERT INTO cikktorzs (cikktorzs.cikkszam," +
                     "cikktorzs.megnevezes,cikktorzs.cikkcsoportkod," +
                     "cikktorzs.minoseg,cikktorzs.hossz,cikktorzs.szelesseg," +
                     "cikktorzs.vastagsag1,cikktorzs.vastagsag2,cikktorzs.anyagtipus," +
                     "cikktorzs.darabjegyzek,cikktorzs.muveletterv) SELECT " +
                     "cikktorzs1.cikkszam,cikktorzs1.megnevezes,cikktorzs1.cikkcsoportkod," +
                     "cikktorzs1.minoseg,cikktorzs1.hossz,cikktorzs1.szelesseg," +
                     "cikktorzs1.vastagsag1,cikktorzs1.vastagsag2,cikktorzs1.anyagtipus," +
                     "cikktorzs1.darabjegyzek,cikktorzs1.muveletterv FROM cikktorzs1 " +
                     "LEFT OUTER JOIN cikktorzs ON (cikktorzs.cikkszam = cikktorzs1.cikkszam) " +
                     "WHERE isnull(cikktorzs.cikkszam)";
        sqlComm[3] = "DROP TABLE " + TempTable;
        MultiSQLCommand(sqlComm,TempTable);
//        File f = new File(cfg.getrefreshDir() + "/" + TableName + ".csv");
//        f.delete();
    }

    private void Keszletfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[3];
        TableHeaders[0] = "cikkszam";
        TableHeaders[1] = "raktar";
        TableHeaders[2] = "mennyiseg";
        String TableName = "keszlet";
        String TempTable = "keszlet1";
        String sqlComm[] = new String[4];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "CHARACTER SET latin2 " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] = "DROP TABLE " + TableName;
        sqlComm[3] = "RENAME TABLE " + TempTable + " TO " + TableName;
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Muvelettervfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[6];
        TableHeaders[0] = "muvelettervszam";
        TableHeaders[1] = "sorszam";
        TableHeaders[2] = "megnevezes";
        TableHeaders[3] = "beallido";
        TableHeaders[4] = "gepido";
        TableHeaders[5] = "szemelyido";
        String TableName = "muveletterv";
        String TempTable = "muveletterv1";
        String sqlComm[] = new String[5];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] = "DROP TABLE " + TableName;
        sqlComm[3] = "RENAME TABLE " + TempTable + " TO " + TableName;
        sqlComm[4] = "UPDATE muveletterv SET muveletterv.muvelettervszam = " +
                     "RIGHT(muveletterv.muvelettervszam,LENGTH(muveletterv.muvelettervszam)-1)";
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Darabjegyzekfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[6];
        TableHeaders[0] = "dbjszam";
        TableHeaders[1] = "sorszam";
        TableHeaders[2] = "tipus";
        TableHeaders[3] = "cikkszam";
        TableHeaders[4] = "alkatresz";
        TableHeaders[5] = "kapcsolo";
        String TableName = "darabjegyzek";
        String TempTable = "darabjegyzek1";
        String sqlComm[] = new String[5];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] = "DROP TABLE " + TableName;
        sqlComm[3] = "RENAME TABLE " + TempTable + " TO " + TableName;
        sqlComm[4] = "UPDATE darabjegyzek SET darabjegyzek.dbjszam = " +
                     "RIGHT(darabjegyzek.dbjszam,LENGTH(darabjegyzek.dbjszam)-1)," +
                     "darabjegyzek.cikkszam = " +
                     "RIGHT(darabjegyzek.cikkszam,LENGTH(darabjegyzek.cikkszam)-1)";
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Visszajelentfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[7];
        TableHeaders[0] = "tetelszam";
        TableHeaders[1] = "dszam";
        TableHeaders[2] = "grszam";
        TableHeaders[3] = "muvszam";
        TableHeaders[4] = "mennyiseg";
        TableHeaders[5] = "selejt";
        TableHeaders[6] = "datum";
        String TableName = "visszajelent";
        String TempTable = "visszajelent1";
        String sqlComm[] = new String[7];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] = "INSERT INTO visszajelent (visszajelent.tetelszam,visszajelent.dszam,"
                   + "visszajelent.grszam,visszajelent.muvszam,visszajelent.mennyiseg,"
                   + "visszajelent.selejt,visszajelent.datum) SELECT "
                   + "visszajelent1.tetelszam,visszajelent1.dszam,visszajelent1.grszam,"
                   + "visszajelent1.muvszam,visszajelent1.mennyiseg,visszajelent1.selejt,"
                   + "visszajelent1.datum FROM visszajelent1 "
                   + "LEFT OUTER JOIN visszajelent ON (visszajelent1.tetelszam = visszajelent.tetelszam) "
                   + "WHERE isnull(visszajelent.tetelszam)";
        sqlComm[3] = "INSERT INTO teljesitmenyek (teljesitmenyek.tetelszam,teljesitmenyek.dszam,"
                   + "teljesitmenyek.grszam,teljesitmenyek.muvszam,teljesitmenyek.mennyiseg,"
                   + "teljesitmenyek.selejt,teljesitmenyek.datum,teljesitmenyek.tipus) SELECT "
                   + "visszajelent1.tetelszam,visszajelent1.dszam,visszajelent1.grszam,"
                   + "visszajelent1.muvszam,visszajelent1.mennyiseg,visszajelent1.selejt,"
                   + "visszajelent1.datum,2 FROM visszajelent1 "
                   + "LEFT OUTER JOIN teljesitmenyek ON (visszajelent1.tetelszam = teljesitmenyek.tetelszam) "
                   + "WHERE ISNULL(teljesitmenyek.tetelszam)";
        sqlComm[4] = "INSERT INTO teljesitmenyek (teljesitmenyek.tetelszam,teljesitmenyek.dszam,"
                   + "teljesitmenyek.grszam,teljesitmenyek.muvszam,teljesitmenyek.tervmennyiseg,"
                   + "teljesitmenyek.mennyiseg,teljesitmenyek.selejt,teljesitmenyek.datum,"
                   + "teljesitmenyek.tipus) SELECT napiterv.azon,napiterv.dszam,gr_muveletterv.grszam,"
                   + "gr_muveletterv.muveletkapcsolo,napiterv.mennyiseg,0,0,napiterv.datum,1 FROM napiterv "
                   + "LEFT OUTER JOIN teljesitmenyek ON (napiterv.azon = teljesitmenyek.tetelszam) "
                   + "INNER JOIN gr_muveletterv ON (napiterv.grmuvazon = gr_muveletterv.azon) WHERE "
                   + "isnull(teljesitmenyek.tetelszam)";
        sqlComm[5] = "DELETE teljesitmenyek.* FROM teljesitmenyek "
                   + "LEFT OUTER JOIN napiterv ON (teljesitmenyek.tetelszam = napiterv.azon) WHERE "
                   + "teljesitmenyek.tipus = 1 AND isnull(napiterv.azon)";
        sqlComm[6] = "DROP TABLE " + TempTable;
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void Furnerraktarfrissit()
    {
        Configuration cfg = new Configuration();
        String TableHeaders[] = new String[3];
        TableHeaders[0] = "rakatszam";
        TableHeaders[1] = "cikkszam";
        TableHeaders[2] = "mennyiseg";
        String TableName = "furnerraktar";
        String TempTable = "furnerraktar1";
        String sqlComm[] = new String[5];
        sqlComm[0] = "CREATE TABLE " + TempTable + " LIKE " + TableName;
        sqlComm[1] = "LOAD DATA LOCAL INFILE '" +
                     cfg.getrefreshDir() + "/" + TableName + ".csv' " +
                     "INTO TABLE " + TempTable + " " +
                     "FIELDS TERMINATED BY ';' " +
                     "LINES TERMINATED BY '\n' " +
                     "IGNORE 1 LINES (";
        for (int i = 0;i < TableHeaders.length;i++)
        {
            sqlComm[1] += TableHeaders[i];
            if (i < TableHeaders.length - 1)
            {
                sqlComm[1] += ",";
            }
        }
        sqlComm[1] += ")";
        sqlComm[2] = "DROP TABLE " + TableName;
        sqlComm[3] = "RENAME TABLE " + TempTable + " TO " + TableName;
        sqlComm[4] = "UPDATE furnerraktar SET furnerraktar.cikkszam = " +
                     "RIGHT(furnerraktar.cikkszam,LENGTH(furnerraktar.cikkszam)-1)";
        MultiSQLCommand(sqlComm,TempTable);
    }

    private void sqlupdate(String sqlcmd)
    {
        try
        {
            stmt.executeUpdate(sqlcmd);
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(null,"Kapcsolódási hiba: " + ex.getMessage());
        }
    }

    public boolean stopped = false;
    private Connection conn = null;
    private Statement stmt;
    private ResultSet rSet;
}

