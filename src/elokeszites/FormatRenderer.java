/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.text.Format;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Forcek László
 */
public class FormatRenderer extends DefaultTableCellRenderer {
    private Format formatter;
    public FormatRenderer(Format formatter) {
        if (formatter==null)
            throw new NullPointerException();
        this.formatter = formatter;
    }
}
