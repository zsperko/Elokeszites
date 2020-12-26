/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package elokeszites;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Forcek László
 */

public class NoEditModel extends DefaultTableModel {

    public NoEditModel(Vector cols, Vector rows)
    {
        super(cols,rows);
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
