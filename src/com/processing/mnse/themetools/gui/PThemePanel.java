package com.processing.mnse.themetools.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.processing.mnse.themetools.common.MainContext;
import com.processing.mnse.themetools.common.ThemeToolsHelper;
import com.processing.mnse.themetools.table.ThemeTable;

import processing.app.laf.PdeScrollBarUI;

/**
 * The Class PThemePanel.
 * the main panel for the tool
 * 
 * @author mnse 
 */
@SuppressWarnings("serial")
public final class PThemePanel extends JPanel {
   
   /** The main context object. */
   private MainContext ctx;

   /**
    * Instantiates a new PThemePanel object.
    *
    * @param ctx the main context
    * @throws Exception the exception
    */
   public PThemePanel(MainContext ctx) throws Exception {
      this.ctx = ctx;
      setBorder(BorderFactory.createEmptyBorder());
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      ctx.addThemeComponent(this);
      add(createLabel());
      add(createTable());
      add(createButtons());
      ctx.applyTheme();
      ctx.startFileWatcher();
   }

   /**
    * Creates the label.
    *
    * @return the JPanel
    */
   public JPanel createLabel() {
      JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
      headerPanel.setName("headerPanel");
      JLabel header = new JLabel("Processing Theme Editor");
      header.setSize(new Dimension(this.getWidth(), 40));
      headerPanel.add(header);
      ctx.addThemeComponent(headerPanel);
      ctx.addThemeComponent(header);
      return headerPanel;
   }

   /**
    * Creates the buttons.
    *
    * @return the JPanel
    */
   public JPanel createButtons() {
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
      buttonPanel.setName("buttonPanel");
      ctx.addThemeComponent(buttonPanel);
      JButton btntest = new JButton("Test");
      btntest.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ctx.getProperties().updateFile();
         }
      });
      ctx.addThemeComponent(btntest);
      buttonPanel.add(btntest);

      JButton btnRevert = new JButton("Revert");
      btnRevert.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ctx.getProperties().revertFile();
         }
      });
      ctx.addThemeComponent(btnRevert);
      buttonPanel.add(btnRevert);

      JButton btnClose = new JButton("Close");
      btnClose.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
      ctx.addThemeComponent(btnClose);
      buttonPanel.add(btnClose);

      return buttonPanel;
   }

   /**
    * Creates the table.
    *
    * @return the JPanel
    */
   public JPanel createTable() {
      JPanel tablePanel = new JPanel(new BorderLayout());
      ctx.addThemeComponent(tablePanel);
      JScrollPane scroll = new JScrollPane(new ThemeTable(ctx));
      scroll.getVerticalScrollBar().setUI(new PdeScrollBarUI(ThemeToolsHelper.PDE_SCROLLBAR_UI_ATTR));
      ctx.addThemeComponent(scroll);
      tablePanel.add(scroll, BorderLayout.CENTER);
      return tablePanel;
   }

   /**
    * Dispose dialog panel.
    */
   public void dispose() {
      getParent().remove(this);
      ctx.getEditor().pack();
      MainContext.destroy();
   }
}
