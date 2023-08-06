package com.processing.mnse.themetools.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import com.processing.mnse.themetools.common.MainContext;
import com.processing.mnse.themetools.common.ThemeToolsHelper;

/**
 * The Class ColorCellRenderer.
 * cell renderer for Color cells 
 * 
 * @author mnse 
 */
@SuppressWarnings("serial")
public final class ColorCellRenderer extends JLabel implements TableCellRenderer {
   
   /**
    * Instantiates a new color cell renderer.
    *
    * @param table the table
    */
   public ColorCellRenderer(JTable table) {
      setFont(table.getFont());
      setOpaque(true);
      setHorizontalAlignment(SwingConstants.CENTER);
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
         setText("");
         return this;
      }      
      Color color = (Color) value;
      setText(ThemeToolsHelper.toHexColorString(color));
      setBackground(color);  
      setForeground(ThemeToolsHelper.getContrastColor(color));      
      setBorder(isSelected ? BorderFactory.createMatteBorder(2, 0, 2, 0,  MainContext.instance().getPropertyColor(ThemeToolsHelper.TABLE_ROW_SELECTED_COLOR_ATTR)) : BorderFactory.createEmptyBorder());
      return this;
   }
}