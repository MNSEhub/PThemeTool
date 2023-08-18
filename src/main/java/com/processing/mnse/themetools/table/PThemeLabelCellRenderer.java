package com.processing.mnse.themetools.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import com.processing.mnse.themetools.common.PThemeMainContext;
import com.processing.mnse.themetools.common.PThemeToolsHelper;

import processing.app.ui.Theme;

/**
 * The Class PThemeLabelCellRenderer.
 * cell renderer for label cells
 * 
 * @author mnse
 */
@SuppressWarnings("serial")
public final class PThemeLabelCellRenderer extends JLabel implements TableCellRenderer {

   /** The insets. */
   private Insets insets = new Insets(0, 10, 0, 0);

   /**
    * Instantiates a new lable cell renderer.
    *
    * @param table the table
    */
   public PThemeLabelCellRenderer(JTable table) {
      setFont(table.getFont());
      setBorder(new EmptyBorder(insets));
      setOpaque(true);
   }

   /**
    * Gets the table cell renderer component.
    *
    * @param table      the table
    * @param value      the value
    * @param isSelected the is selected
    * @param hasFocus   the has focus
    * @param row        the row
    * @param column     the column
    * @return the table cell renderer component
    */
   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      String lbl = (String) value;
      setText(lbl);
      Color color = (row % 2 == 0) ? Theme.getColor(PThemeToolsHelper.TABLE_CELL_EVEN) : Theme.getColor(PThemeToolsHelper.TABLE_CELL_ODD);
      setBackground(color);
      setForeground(PThemeToolsHelper.getContrastColor(color));
      setBorder(isSelected ? BorderFactory.createMatteBorder(2, 0, 2, 0, Theme.getColor(PThemeToolsHelper.TABLE_ROW_SELECTED_COLOR_ATTR)) : BorderFactory.createEmptyBorder());
      return this;
   }
}