package elokeszites;


import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Forcek László
 */

public class ProgressRenderer extends JProgressBar implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value instanceof JComponent)
        {
            return (JComponent)value;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setValue(int n )
    {
        super.setValue(n);
    }
}
