package com.processing.mnse.themetools.gui;

import javax.swing.JTabbedPane;

import com.processing.mnse.themetools.common.PThemeMainContext;

/**
 * The Class PThemeTabbedPane.
 */
@SuppressWarnings("serial")
public class PThemeTabbedPane extends JTabbedPane {
   
   /**
    * Instantiates a new PThemeTabbedPane.
    */
   public PThemeTabbedPane() {
      super(JTabbedPane.TOP);
      setFont(PThemeMainContext.instance().getGlobalFont().deriveFont(12f));
      setUI(new PThemeTabbedPaneCustomUI(this));
   }
}
