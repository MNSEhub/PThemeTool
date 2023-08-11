package com.processing.mnse.themetools.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.processing.mnse.themetools.common.PThemeMainContext;

import processing.app.ui.Theme;

/**
 * The Class PThemeTabbedPaneCustomUI.
 */
public class PThemeTabbedPaneCustomUI extends BasicTabbedPaneUI {
   
   /** The parent PThemeTabbedPane. */
   PThemeTabbedPane parent;

   /**
    * Instantiates a new PThemeTabbedPaneCustomUI.
    *
    * @param parent the parent PThemeTabbedPane
    */
   public PThemeTabbedPaneCustomUI(PThemeTabbedPane parent) {
      super();
      this.parent = parent;
   }

   /**
    * Paints the tab background.
    *
    * @param g the Graphics context
    * @param tabPlacement the tab placement
    * @param tabIndex the tab index
    * @param x the x
    * @param y the y
    * @param w the w
    * @param h the h
    * @param isSelected the is selected
    */
   @Override
   protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
      g.setColor(isSelected ? Theme.getColor("header.tab.selected.color") : Theme.getColor("header.tab.unselected.color"));
      g.fillRect(x, y, w, h);
   }

   /**
    * Paints the tab border.
    *
    * @param g the the Graphics context
    * @param tabPlacement the tab placement
    * @param tabIndex the tab index
    * @param x the x
    * @param y the y
    * @param w the w
    * @param h the h
    * @param isSelected the selected flag
    */
   @Override
   protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
      g.setColor(!isSelected ? Theme.getColor("header.tab.selected.color") : Theme.getColor("header.tab.unselected.color"));
      g.drawRect(x, y, w, h);
   }

   /**
    * Gets the content border insets.
    *
    * @param tabPlacement the tab placement
    * @return the content border insets
    */
   @Override
   protected Insets getContentBorderInsets(int tabPlacement) {
      return new Insets(2, 0, 0, 0);
   }

   /**
    * Paint content border.
    *
    * @param g the g
    * @param tabPlacement the tab placement
    * @param selectedIndex the selected index
    */
   @Override
   protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
   }

   /**
    * Paint tab area.
    *
    * @param g the g
    * @param tabPlacement the tab placement
    * @param selectedIndex the selected index
    */
   @Override
   protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
      g.setColor(Theme.getColor("editor.gradient.top"));
      g.fillRect(0, 0, tabPane.getWidth(), calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight));
      super.paintTabArea(g, tabPlacement, selectedIndex);
   }

   /**
    * Paint text.
    *
    * @param g the g
    * @param tabPlacement the tab placement
    * @param font the font
    * @param metrics the metrics
    * @param tabIndex the tab index
    * @param title the title
    * @param textRect the text rect
    * @param isSelected the is selected
    */
   @Override
   protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
      g.setFont(PThemeMainContext.instance().getGlobalFont().deriveFont(24));
      if (isSelected) {
         parent.setForegroundAt(tabIndex, Theme.getColor("header.text.selected.color"));
      } else {
         parent.setForegroundAt(tabIndex, Theme.getColor("header.text.unselected.color"));
      }
      super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
   }

}
