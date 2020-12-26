/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import javax.swing.JInternalFrame;

/**
 *
 * @author Forcek László
 */
public class JExtendedFrame extends JInternalFrame{
    int ID;

    public void setformID(int ertek)
    {
        ID = ertek;
    }

    public int getformID()
    {
        return ID;
    }
}
