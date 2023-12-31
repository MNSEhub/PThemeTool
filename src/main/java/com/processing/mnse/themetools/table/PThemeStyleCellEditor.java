package com.processing.mnse.themetools.table;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.processing.mnse.themetools.common.PThemeToolsHelper;

/**
 * The Class PThemeStyleCellEditor.
 * cell editor for style cells
 * 
 * @author mnse
 */
@SuppressWarnings("serial")
public final class PThemeStyleCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

   /** The Constant PLAIN. */
   public static final String PLAIN = "plain";

   /** The Constant BOLD. */
   public static final String BOLD = "bold";

   /** The switch button. */
   private JButton switchButton;

   /** The parent table. */
   private PThemeTable parentTable;

   /**
    * Instantiates a new style cell editor.
    *
    * @param parent the parent
    */
   public PThemeStyleCellEditor(PThemeTable parent) {
      parentTable = parent;
      switchButton = new JButton();
      switchButton.addActionListener(this);
   }

   /**
    * Action performed.
    *
    * @param e the e
    */
   @Override
   public void actionPerformed(ActionEvent e) {
      switchButton.setText(PLAIN.equals(switchButton.getText()) ? BOLD : PLAIN);
      final int row = parentTable.getEditingRow();
      fireEditingStopped();
      EventQueue.invokeLater(() -> parentTable.updatePropertyFromTable(row));
   }

   /**
    * Gets the cell editor value.
    *
    * @return the cell editor value
    */
   @Override
   public Object getCellEditorValue() {
      return switchButton.getText();
   }

   /**
    * Gets the table cell editor component.
    *
    * @param table      the table
    * @param value      the value
    * @param isSelected the is selected
    * @param row        the row
    * @param column     the column
    * @return the table cell editor component
    */
   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      if (PThemeToolsHelper.VALUE_NOT_AVAILABLE.equals(value)) {
         switchButton.setText(PThemeToolsHelper.VALUE_NOT_AVAILABLE);
      } else {
         switchButton.setText(PLAIN.equals(value) ? PLAIN : BOLD);
      }
      return switchButton;
   }
}