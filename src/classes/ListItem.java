/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package classes;

/**
 *
 * @author Forcek László
 */
public class ListItem {
    private int value;
    private String label;

    public ListItem(int id, String label){
        this.value = id;
        this.label = label;
    }

    public int getValue(){
        return value;
    }

    @Override
    public String toString(){
        return label;
    }
}