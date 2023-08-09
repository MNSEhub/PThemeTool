package com.processing.mnse.themetools.gui;

import javax.swing.JTabbedPane;

import com.processing.mnse.themetools.common.MainContext;

@SuppressWarnings("serial")
public class PThemeTabbedPane extends JTabbedPane {
   public PThemeTabbedPane() {
    super(JTabbedPane.TOP);
    setFont(MainContext.instance().getGlobalFont().deriveFont(12f));
    setUI(new PThemeTabbedPaneCustomUI(this));
   }    
}
