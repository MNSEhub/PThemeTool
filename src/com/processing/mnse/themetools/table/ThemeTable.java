package com.processing.mnse.themetools.table;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.processing.mnse.themetools.common.MainContext;
import com.processing.mnse.themetools.common.ThemeToolsHelper;
import com.processing.mnse.themetools.gui.PThemeInfoDialog;

/**
 * The Class ThemeTable.
 * the main table to handle theme properties
 * 
 * @author mnse 
 */
@SuppressWarnings("serial")
public final class ThemeTable extends JTable {
   
   /** The ctx. */
   private MainContext         ctx;

   /**
    * Instantiates a new theme table.
    *
    * @param ctx the ctx
    */
   public ThemeTable(MainContext ctx) {
      super();
      this.ctx = ctx;

      setModel(new ThemeTableDataModel(new String[] { "Label", "Color", "Style"
      }));
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      
      Font font = ctx.getGlobalFont().deriveFont(11f);
      setFont(font);
      setRowHeight(ctx.getFontHeight(this, font));
      
      getColumnModel().getColumn(0).setCellRenderer(new LableCellRenderer(this));

      getColumnModel().getColumn(1).setCellEditor(new ColorCellEditor(this));
      getColumnModel().getColumn(1).setCellRenderer(new ColorCellRenderer(this));

      getColumnModel().getColumn(2).setCellEditor(new StyleCellEditor(this));
      getColumnModel().getColumn(2).setCellRenderer(new StyleCellRenderer(this));

      addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            new PThemeInfoDialog((ThemeTable) e.getSource(), e);
         }
      });

      setShowGrid(false);
      setShowHorizontalLines(false);
      setShowVerticalLines(false);
      setTableHeader(null);
      setBorder(null);
      ctx.setMainTable(this);
      ctx.addThemeComponent(this);
      populateData();
   }

   /**
    * Gets the tool tip text.
    *
    * @param event the event
    * @return the tool tip text
    */
   @Override
   public String getToolTipText(MouseEvent event) {
      java.awt.Point p = event.getPoint();
      int rowIndex = rowAtPoint(p);
      int colIndex = columnAtPoint(p);
      String lbl = (String) getModel().getValueAt(rowIndex, 0);
      if (colIndex == 0 && MainContext.instance().hasInfo(lbl)) {
         lbl += "\n" + "click for more info";
      }
      return lbl;
   }

   /**
    * Populate data.
    */
   public void populateData() {
      DefaultTableModel model = (DefaultTableModel) getModel();
      model.setRowCount(0);
      List<ThemeTableEntry> top = new ArrayList<>();
      List<ThemeTableEntry> bottom = new ArrayList<>();
      StreamSupport.stream(MainContext.instance().getProperties().orderedKeys().spliterator(), false).map(key -> (String) key)
            .filter(k -> List.of(ThemeToolsHelper.visibleItems).stream().anyMatch(pattern -> k.matches(pattern))).forEachOrdered(k -> {
               ThemeTableEntry entry = ThemeTableEntry.fromString(k, MainContext.instance().getProperties().getProperty(k));
               if (entry.getLabel().contains(".token."))
                  top.add(entry);
               else
                  bottom.add(entry);
            });
      top.add(ThemeTableEntry.emptyEntry());
      Stream.concat(top.stream(), bottom.stream()).forEachOrdered(e -> {
         model.addRow(e.toTableElement());
      });

      adjustColumnWidths(0);
      adjustColumnWidths(1);
      adjustColumnWidths(2);
   }

   /**
    * Update property from table.
    *
    * @param row the row
    */
   public void updatePropertyFromTable(int row) {
      ctx.getProperties().updateEntry(ThemeTableEntry.fromTable((DefaultTableModel) getModel(), row));
   }

   /**
    * Adjust column widths.
    *
    * @param column the column
    * @return the int
    */
   public int adjustColumnWidths(int column) {
      TableColumn tableColumn = getColumnModel().getColumn(column);
      int preferredWidth = tableColumn.getMinWidth();
      int maxWidth = tableColumn.getMaxWidth();

      for (int row = 0; row < getRowCount(); row++) {
         TableCellRenderer cellRenderer = getCellRenderer(row, column);
         Component c = prepareRenderer(cellRenderer, row, column);
         int width = c.getPreferredSize().width + getIntercellSpacing().width;
         preferredWidth = Math.max(preferredWidth, width);

         if (preferredWidth >= maxWidth) {
            preferredWidth = maxWidth;
            break;
         }
      }

      tableColumn.setPreferredWidth(preferredWidth);
      return preferredWidth;
   }
}
