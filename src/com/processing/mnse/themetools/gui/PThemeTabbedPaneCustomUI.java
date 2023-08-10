package com.processing.mnse.themetools.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

import com.processing.mnse.themetools.common.PThemeMainContext;

import processing.app.ui.Theme;

public class PThemeTabbedPaneCustomUI extends BasicTabbedPaneUI {
   PThemeTabbedPane parent;

   public PThemeTabbedPaneCustomUI(PThemeTabbedPane parent) {
      super();
      this.parent = parent;
   }

   @Override
   protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
      g.setColor(isSelected ? Theme.getColor("header.tab.selected.color") : Theme.getColor("header.tab.unselected.color"));
      g.fillRect(x, y, w, h);
   }

   @Override
   protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
      g.setColor(!isSelected ? Theme.getColor("header.tab.selected.color") : Theme.getColor("header.tab.unselected.color"));
      g.drawRect(x, y, w, h);
   }

   @Override
   protected Insets getContentBorderInsets(int tabPlacement) {
      return new Insets(2, 0, 0, 0);
   }

   @Override
   protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
   }

   @Override
   protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
      g.setColor(Theme.getColor("editor.gradient.top"));
      g.fillRect(0, 0, tabPane.getWidth(), calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight));
      super.paintTabArea(g, tabPlacement, selectedIndex);
   }

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
