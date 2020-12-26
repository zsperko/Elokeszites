/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.swing.JOptionPane;

public class Start {

    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        try 
        {
            ServerSocket serverSocket = new ServerSocket(PORT, 10, InetAddress.getLocalHost());
            new MainFrame().setVisible(true);   
        }
        catch (BindException e) {
            JOptionPane.showMessageDialog(null, "Az alkalmazás egy példánya már meg van nyitva!");
            System.exit(1);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Nem várt hiba: " + e.getMessage());
            System.exit(2);
        }
    }
    
    private static final int PORT = 9997;

}