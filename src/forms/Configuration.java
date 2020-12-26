/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

/**
 *
 * @author Forcek László
 */

import java.awt.Image;
import java.io.*;
import java.util.Random;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;


public class Configuration {

    public void setArray(String array[])
    {
        Array = array;
    }

    public String[] getArray()
    {
        return Array;
    }
    
    public static void initParameters()
    {
        ParamName = new String[20];
        ParamValue = new Object[20];
        for (int i = 0;i < 10;i++)
        {
            Configuration.setParamName("",i);
            Configuration.setParamValue("", i);
        }
    }
    
    public ImageIcon resizeImageIcon(ImageIcon imageIcon, int width, int height)
    {
        Image image = imageIcon.getImage();
        Image newImage  = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon newImageIcon = new ImageIcon(newImage);
        return newImageIcon;
    }

    private void kapcsolatStr()
    {
        //létrehozza a kapcsolódáshoz használt szövegváltozót
        sqlConn = "jdbc:mysql://" + Array[2] + ":" + Array[3] +
                  "/elokeszites_db?user=" + Array[0] + "&password=" + Array[1];
    }

    public void ment()
    {
        //A kapcsolat adatainak elmentését végzi
        try
        {
            for (int i = 0;i < 4;i++)
            {
                bekodol(Array[i],i);
            }
            try (ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream("config.dat"))) {
                objOut.writeObject(Array);
            }
        }
        catch (Exception ex)
        {
            Message = ex.getMessage();
            JOptionPane.showMessageDialog(null,Message);
        }
    }

    public void beolvas()
    {
        //Kapcsolat adatainak visszaolvasása az elmentett config.dat állományból
        try
        {
            Array = null;
            try (ObjectInputStream objIn = new ObjectInputStream(new FileInputStream("config.dat"))) {
                Array = (String[]) objIn.readObject();
            }
            for (int i = 0;i < 4;i++)
            {
                visszakodol(Array[i],i);
            }
            kapcsolatStr();
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Message = ex.getMessage();
            JOptionPane.showMessageDialog(null,Message);
        }
    }

    private void bekodol(String str, int idx)
    {
        //kapcsolat adatainak lekódolása
        String kodStr = "";
        for (int i = 0;i < str.length();i++)
        {
            int sub = (int)str.substring(i, i + 1).charAt(0) + 5;
            showRandomInteger(32,100,random);
            kodStr += (char)sub;
            kodStr += (char)ertek;
        }
        Array[idx] = kodStr;
    }

    private void visszakodol(String str, int idx)
    {
        //kapcsolat adatainak visszakódolása
        String kodStr = "";
        for (int i = 0;i < str.length();i++)
        {
            if (i == 0 || ((i % 2)) == 0)
            {
                int sub = (int)str.substring(i, i + 1).charAt(0) - 5;
                kodStr += (char)sub;
            }
        }
        Array[idx] = kodStr;
    }

    private void showRandomInteger(int aStart, int aEnd, Random aRandom)
    {
        //random értéket generál a titkosításhoz
        if ( aStart > aEnd )
        {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        long range = (long)aEnd - (long)aStart + 1;
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        ertek = randomNumber;
    }

    public void setMessage(String ertek)
    {
        Message = ertek;
    }

    public void setchangeEnable(boolean ertek)
    {
        changeEnable = ertek;
    }

    public boolean getchangeEnable()
    {
        return changeEnable;
    }

    public void sethalfEnable(boolean ertek)
    {
        halfEnable = ertek;
    }

    public boolean gethalfEnable()
    {
        return halfEnable;
    }

    public String getsqlConn()
    {
        return sqlConn;
    }

    public void setsqlConn(String ertek)
    {
        sqlConn = ertek;
    }

    public String getcurrentUserName()
    {
        return currentUserName;
    }

    public void setcurrentUserName(String ertek)
    {
        currentUserName = ertek;
    }

    public int getcurrentUser()
    {
         return currentUser;
    }

    public void setcurrentUser(int ertek)
    {
        currentUser = ertek;
    }

    public String getdefaultUser()
    {
         return defaultUser;
    }

    public String getdefaultPass()
    {
         return defaultPass;
    }

    public boolean getconfExist()
    {
        return confExist;
    }

    public void setconfExist(boolean ertek)
    {
        confExist = ertek;
    }

    public boolean getconnOK()
    {
        return connOK;
    }

    public void setconnOK(boolean ertek)
    {
        connOK = ertek;
    }

    public String getchoosenDate()
    {
        return choosenDate;
    }

    public void setchoosenDate(String ertek)
    {
        choosenDate = ertek;
    }

    public boolean getopenForms(int index)
    {
        return openForms[index];
    }

    public void setopenForm(boolean bool, int index)
    {
        openForms[index] = bool;
    }
    
    public String getrefreshDir()
    {
        return refreshDir;
    }

    public void setrefreshDir(String ertek)
    {
        refreshDir = ertek;
    }

    public String getdoneMessage()
    {
        return doneMessage;
    }

    public void setdoneMessage(String ertek)
    {
        doneMessage = ertek;
    }
    
    public JInternalFrame getForm()
    {
        return forms;
    }
    
    public static String getSqlConn() {
        return sqlConn;
    }

    public static void setSqlConn(String sqlConn) {
        Configuration.sqlConn = sqlConn;
    }

    public void setForm(JInternalFrame value)
    {
        forms = value;
    }

    public boolean getthreadFinished()
    {
        return threadFinished;
    }

    public void setthreadFinished(boolean value)
    {
        threadFinished = value;
    }

    public boolean getrefreshTables(int index)
    {
        return refreshTables[index];
    }

    public boolean getReportFilled()
    {
        return ReportFilled;
    }

    public void setReportFilled(boolean ertek)
    {
        ReportFilled = ertek;
    }

    public void setrefreshTables(int index, boolean value)
    {
        refreshTables[index] = value;
    }
    
    public static boolean isThreadFinished() {
        return threadFinished;
    }

    public static void setThreadFinished(boolean threadFinished) {
        Configuration.threadFinished = threadFinished;
    }
    
    public static String getReportName() {
        return reportName;
    }

    public static void setReportName(String reportName) {
        Configuration.reportName = reportName;
    }

    public static String getParamName(int index) {
        return ParamName[index];
    }

    public static void setParamName(String ParamName, int index) {
        Configuration.ParamName[index] = ParamName;
    }

    public static Object getParamValue(int index) {
        return ParamValue[index];
    }

    public static void setParamValue(Object ParamValue, int index) {
        Configuration.ParamValue[index] = ParamValue;
    }

    public String sqlDate(Date date)
    {
        SimpleDateFormat yearData = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthData = new SimpleDateFormat("MM");
        SimpleDateFormat dateData = new SimpleDateFormat("dd");
        return yearData.format(date) + "-"
             + monthData.format(date) + "-"
             + dateData.format(date);
    }

    public Date sqlToDate(String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("yyyy-MM-dd");
        Date processDate = new Date();
        try
        {
           processDate = dateFormat.parse(date);
        }
        catch (ParseException ex){}
        return processDate;
    }

    private final String defaultUser = "admin";
    private final String defaultPass = "admin";

    private static String ParamName[];
    private static Object ParamValue[];
    
    private Random random = new Random();
    private static String currentUserName ,Message, sqlConn, choosenDate, reportName, doneMessage, refreshDir;
    private static int currentUser;
    private static boolean changeEnable, confExist, connOK, halfEnable, threadFinished, ReportFilled;
    private int ertek;
    private static JInternalFrame forms;
    private static boolean refreshTables[] = new boolean[8];
    //index 0 = Dolgozói törzs
    //index 1 = Gyártái rendelések
    //index 2 = Furnérraktár készlet
    //index 3 = Cikktörzs
    //index 4 = Darabjegyzékek
    //index 5 = Művelettervek

    private JDialog AniMSGBox, ReportBox;
    private Timer timer;

    private static String Pname[] = new String[10];
    private static Object Pvalue[] = new Object[10];

    private static boolean openForms[] = new boolean[40];

    private static String Array[] = new String[4];
    // Array[0] -> felhasználói név
    // Array[1] -> jelszó
    // Array[2] -> host
    // Array[3] -> port
}
