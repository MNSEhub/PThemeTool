package com.processing.mnse.themetools.table;

import javax.swing.table.DefaultTableModel;

import com.processing.mnse.themetools.common.ThemeToolsHelper;

/**
 * The Class ThemeTableDataModel.
 * the data model 
 *  
 * @author mnse 
 */
@SuppressWarnings("serial")
public class ThemeTableDataModel extends DefaultTableModel {
   
   /**
    * Instantiates a new theme table data model.
    *
    * @param columns the columns
    */
   public ThemeTableDataModel(String[] columns) {
      super(columns,0);
   }
   
   /**
    * Checks if is cell editable.
    *
    * @param row the row
    * @param col the col
    * @return true, if is cell editable
    */
   @Override
   public boolean isCellEditable(int row, int col) {
      Object cellValue = this.getValueAt(row, col);
      return col != 0 && cellValue != null && !ThemeToolsHelper.VALUE_NOT_AVAILABLE.equals(cellValue);
   }
}
