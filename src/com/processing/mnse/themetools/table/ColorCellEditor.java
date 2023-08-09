package com.processing.mnse.themetools.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * The Class ColorCellEditor.
 * cell editor for Color selector
 * 
 * @author mnse 
 */
@SuppressWarnings("serial")
public final class ColorCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
   
   /** The current color. */
   private Color         currentColor;
   
   /** The button. */
   private JButton       button;
   
   /** The color chooser. */
   private JColorChooser colorChooser;
   
   /** The dialog. */
   private JDialog       dialog;
   
   /** The parent table. */
   private ThemeTable parentTable;

   /** The Constant EDIT. */
   private static final String EDIT = "edit";

   /**
    * Instantiates a new color cell editor.
    *
    * @param parent the parent
    */
   public ColorCellEditor(ThemeTable parent) {
      parentTable = parent;
      button = new JButton();
      button.setActionCommand(EDIT);
      button.addActionListener(this);
      button.setBorderPainted(false);
      colorChooser = new JColorChooser();
      dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this, null);
   }

   /**
    * Action performed.
    *
    * @param e the e
    */
   @Override
   public void actionPerformed(ActionEvent e) {
      if (EDIT.equals(e.getActionCommand())) {
         button.setBackground(currentColor);
         colorChooser.setColor(currentColor);
         dialog.setVisible(true);
         fireEditingStopped();
      } else {
         Color selectedColor = colorChooser.getColor();
         if (!selectedColor.equals(currentColor)) {
            currentColor = selectedColor;
            final int row = parentTable.getEditingRow();            
            EventQueue.invokeLater(() -> parentTable.updatePropertyFromTable(row));
         }
      }
   }

   /**
    * Gets the cell editor value.
    *
    * @return the cell editor value
    */
   @Override
   public Object getCellEditorValue() {
      return currentColor;
   }


   /**
    * Gets the table cell editor component.
    *
    * @param table the table
    * @param value the value
    * @param isSelected the is selected
    * @param row the row
    * @param column the column
    * @return the table cell editor component
    */
   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      currentColor = (Color) value;
      return button;
   }
}