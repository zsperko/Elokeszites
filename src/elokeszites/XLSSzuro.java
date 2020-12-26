/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.io.File;

/**
 *
 * @author Forcek László
 */
public class XLSSzuro extends javax.swing.filechooser.FileFilter
{
     public boolean accept(File file)
     {
         String filename = file.getName();

         if(file.isDirectory())
             return true;
         else if(filename.toLowerCase().endsWith(".xls"))
             return true;
         else
             return false;
     }

     public String getDescription()
     {
         return "XLS (Microsoft Office Excel)";
     }
}
