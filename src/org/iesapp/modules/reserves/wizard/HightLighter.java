/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.modules.reserves.wizard;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author Josep
 */
public class HightLighter extends JLabel implements javax.swing.table.TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.setText(value.toString());
        this.setOpaque(true);
        if(table.isCellEditable(row, 0))
        {
            this.setBackground(new Color(150,255,150));
        }
        else
        {
            this.setBackground(new Color(255,150,150));
        }
        
        if(isSelected)
        {
            this.setBackground(this.getBackground().darker());
        }
        return this;
    }
    
}
