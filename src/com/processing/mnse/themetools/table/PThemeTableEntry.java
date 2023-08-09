package com.processing.mnse.themetools.table;

import java.awt.Color;

import javax.swing.table.DefaultTableModel;

import com.processing.mnse.themetools.common.PThemeToolsHelper;

/**
 * The Class PThemeTableEntry.
 * represents a table row entries
 * 
 * @author mnse
 */
public final class PThemeTableEntry {

   /** The label. */
   private String label;

   /** The color. */
   private Color color;

   /** The style. */
   private String style;

   /**
    * Instantiates a new theme table entry.
    *
    * @param label the label
    * @param color the color
    * @param style the style
    */
   public PThemeTableEntry(String label, Color color, String style) {
      this.label = label;
      this.color = color;
      this.style = style;
   }

   /**
    * Gets the label.
    *
    * @return the label
    */
   public String getLabel() {
      return label;
   }

   /**
    * Sets the label.
    *
    * @param label the new label
    */
   public void setLabel(String label) {
      this.label = label;
   }

   /**
    * Gets the color.
    *
    * @return the color
    */
   public Color getColor() {
      return color;
   }

   /**
    * Sets the color.
    *
    * @param color the new color
    */
   public void setColor(Color color) {
      this.color = color;
   }

   /**
    * Gets the style.
    *
    * @return the style
    */
   public String getStyle() {
      return style;
   }

   /**
    * Sets the style.
    *
    * @param style the new style
    */
   public void setStyle(String style) {
      this.style = style;
   }

   /**
    * Empty entry.
    *
    * @return the theme table entry
    */
   public static PThemeTableEntry emptyEntry() {
      return new PThemeTableEntry(null, null, null);
   }

   /**
    * From string.
    *
    * @param key   the key
    * @param value the value
    * @return the theme table entry
    */
   public static PThemeTableEntry fromString(String key, String value) {
      String[] parts = value.split(",");
      Color c = Color.decode(parts[0].trim());
      String s = PThemeToolsHelper.VALUE_NOT_AVAILABLE;
      if (parts.length == 2) {
         s = parts[1].trim();
      }
      return new PThemeTableEntry(key, c, s);
   }

   /**
    * From table.
    *
    * @param m the m
    * @param r the r
    * @return the theme table entry
    */
   public static PThemeTableEntry fromTable(DefaultTableModel m, int r) {
      return new PThemeTableEntry((String) m.getValueAt(r, 0), (Color) m.getValueAt(r, 1), (String) m.getValueAt(r, 2));
   }

   /**
    * To table element.
    *
    * @return the object[]
    */
   public Object[] toTableElement() {
      return new Object[] { label, color, style
      };
   }

   /**
    * Creates the property value.
    *
    * @return the string
    */
   public String createPropertyValue() {
      if (PThemeToolsHelper.VALUE_NOT_AVAILABLE.equals(style)) {
         return PThemeToolsHelper.toHexColorString(color);
      }
      return PThemeToolsHelper.toHexColorString(color) + "," + style;
   }
}
