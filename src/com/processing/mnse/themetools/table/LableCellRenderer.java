package com.processing.mnse.themetools.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import com.processing.mnse.themetools.common.MainContext;
import com.processing.mnse.themetools.common.ThemeToolsHelper;

/**
 * The Class LableCellRenderer.
 * cell renderer for label cells
 * 
 * @author mnse  
 */
@SuppressWarnings("serial")
public final class LableCellRenderer extends JLabel implements TableCellRenderer {
   
   /** The insets. */
   private Insets insets = new Insets(0, 10, 0, 0);

   /**
    * Instantiates a new lable cell renderer.
    *
    * @param table the table
    */
   public LableCellRenderer(JTable table) {
      setFont(table.getFont());
      setBorder(new EmptyBorder(insets));
      setOpaque(true);
   }

   /**
    * Gets the table cell renderer component.
    *
    * @param table the table
    * @param value the value
    * @param isSelected the is selected
    * @param hasFocus the has focus
    * @param row the row
    * @param column the column
    * @return the table cell renderer component
    */
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      if (value==null) {
         setBackground(MainContext.instance().getPropertyColor(ThemeToolsHelper.JSCROLLPANE_BORDER_COLOR_ATTR));
         table.setRowHeight(row, 4);
         setText("");
         return this;
      }      
      String lbl = (String) value;
      setText(lbl);
      Color color = MainContext.instance().getPropertyColor(ThemeToolsHelper.LABEL_CELL_BGCOLOR_ATTR);      
      setBackground(color);
      setForeground(ThemeToolsHelper.getContrastColor(color));
      setBorder(isSelected ? BorderFactory.createMatteBorder(2, 0, 2, 0, MainContext.instance().getPropertyColor(ThemeToolsHelper.TABLE_ROW_SELECTED_COLOR_ATTR)) : BorderFactory.createEmptyBorder());
      return this;
   }
}