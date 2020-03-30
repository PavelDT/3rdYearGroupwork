package group6.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class JTableUtil {

    /**
     * Sort of works, but cell cant be focused.
     * @param table
     * @param color
     */
    public static void renderColourCell(JTable table, Color color) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(color);
                c.setForeground(Color.BLACK);
                c.setFocusable(true);
                return c;
            }
        });
    }
}
