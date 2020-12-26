/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

import java.awt.Component;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;

/**
 *
 * @author Forcek László
 */
public class JExtendedDesktop extends JDesktopPane{

    public JExtendedFrame[] getExtFrames()
    {
        Component[] components = getComponents();
        int Counter = 0;
        int i = 0;
        while (i < components.length)
        {
            if (components[i] instanceof JExtendedFrame)
            {
                Counter++;
            }
            else if (components[i] instanceof JDesktopIcon)
            {
                Counter++;
            }
            i++;
        }
        JExtendedFrame[] frames = new JExtendedFrame[Counter];
        i = 0;
        Counter = 0;
        while (i < components.length)
        {
            if (components[i] instanceof JExtendedFrame)
            {
                frames[Counter] = (JExtendedFrame)components[i];
                Counter++;
            }
            if (components[i] instanceof JDesktopIcon)
            {
                JInternalFrame iFrame =((JDesktopIcon)components[i]).getInternalFrame();
                frames[Counter] = (JExtendedFrame)iFrame;
                Counter++;
            }
            i++;
        }
        return frames;
    }
}
