package com.processing.mnse.themetools.common;

import java.awt.Color;

/**
 * The Class ThemeToolsHelper.
 * helper class with constants and some needful methods
 * 
 * @author mnse 
 */
public final class ThemeToolsHelper {
   
   /** The Constant TABLE_ROW_SELECTED_COLOR_ATTR. */
   public static final String TABLE_ROW_SELECTED_COLOR_ATTR = "editor.scrollbar.thumb.enabled.color";
   
   /** The Constant JSCROLLPANE_BORDER_COLOR_ATTR. */
   public static final String JSCROLLPANE_BORDER_COLOR_ATTR = "editor.scrollbar.thumb.enabled.color";
   
   /** The Constant JPANEL_BORDER_COLOR_ATTR. */
   public static final String JPANEL_BORDER_COLOR_ATTR      = "editor.scrollbar.thumb.enabled.color";
   
   /** The Constant JLABEL_FGCOLOR_ATTR. */
   public static final String JLABEL_FGCOLOR_ATTR           = "manager.tab.text.selected.color";
   
   /** The Constant JBUTTON_FGCOLOR_ATTR. */
   public static final String JBUTTON_FGCOLOR_ATTR          = "manager.tab.text.selected.color";
   
   /** The Constant JBUTTON_BGCOLOR_ATTR. */
   public static final String JBUTTON_BGCOLOR_ATTR          = "manager.tab.selected.color";
   
   /** The Constant JPANEL_BGCOLOR_ATTR. */
   public static final String JPANEL_BGCOLOR_ATTR           = "editor.gradient.bottom";
   
   /** The Constant PDE_SCROLLBAR_UI_ATTR. */
   public static final String PDE_SCROLLBAR_UI_ATTR         = "editor.scrollbar";
   
   /** The Constant LABEL_CELL_BGCOLOR_ATTR. */
   public static final String LABEL_CELL_BGCOLOR_ATTR       = "editor.gradient.top";
   
   /** The Constant EDITOR_BGCOLOR_ATTR. */
   public static final String EDITOR_BGCOLOR_ATTR           = "editor.bgcolor";
   
   /** The Constant EDITOR_FGCOLOR_ATTR. */
   public static final String EDITOR_FGCOLOR_ATTR           = "editor.fgcolor";
   
   /** The Constant LOG_MSG_FORMAT. */
   public static final String LOG_MSG_FORMAT                = "%s [%s] %s: %s%n";
   
   /** The Constant DATE_TIME_FORMAT. */
   public static final String DATE_TIME_FORMAT              = "yyyy/MM/dd HH:mm:ss";
   
   /** The Constant VALUE_NOT_AVAILABLE. */
   public static final String VALUE_NOT_AVAILABLE           = "N/A";

   /** The Constant visibleItems. */
   public static final String[] visibleItems = {
         ".*\\.token\\..*",
         "^editor.bgcolor$",
         "^editor.fgcolor$",
         "^editor.warning.underline.color$",
         "^editor.error.underline.color$",
         "^editor.selection.color$",
         "^editor.line.highlight.color$",
         ".*color$",
         ".*style$"
   };

   /**
    * Gets the contrast color.
    *
    * @param color the color
    * @return the contrast color
    */
   public static Color getContrastColor(Color color) {
      float[] rgb = color.getRGBColorComponents(null);
      float luminance = 0.299f * rgb[0] + 0.587f * rgb[1] + 0.114f * rgb[2];
      return luminance > 0.5 ? Color.BLACK : Color.WHITE;
   }

   /**
    * To hex color string.
    *
    * @param color the color
    * @return the hex color string
    */
   public static String toHexColorString(Color color) {
      return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
   }
   
}
