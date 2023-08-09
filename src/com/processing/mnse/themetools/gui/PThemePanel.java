package com.processing.mnse.themetools.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicPanelUI;

import com.processing.mnse.themetools.common.MainContext;
import com.processing.mnse.themetools.common.ThemeToolsHelper;
import com.processing.mnse.themetools.table.ThemeTable;

import processing.app.laf.PdeScrollBarUI;
import processing.app.ui.Theme;

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
      this.setName("mainPanel");
      this.setUI(new BasicPanelUI());
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      add(createLabel());
      add(createTables());
      add(createButtons());
      ctx.startFileWatcher();
   }

   @Override
   protected void paintComponent(Graphics g) {
      setBackground(Theme.getColor(ThemeToolsHelper.JPANEL_BGCOLOR_ATTR));
      super.paintComponent(g);  
   }

   @Override
   protected void paintBorder(Graphics g) {
      setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Theme.getColor(ThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
      super.paintBorder(g);
   }

   /**
    * Creates the label.
    *
    * @return the JPanel
    */
   public JPanel createLabel() {
      JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2)) {
         @Override
         protected void paintComponent(Graphics g) {
            Color color = Theme.getColor("editor.gradient.top"); 
            setBackground(color);
            super.paintComponent(g);
            String watermark = "mnse  ";
            Font watermarkFont = g.getFont().deriveFont(Font.BOLD,14f);
            g.setFont(watermarkFont);
            FontMetrics metrics = g.getFontMetrics(watermarkFont);
            int x = (getWidth() - metrics.stringWidth(watermark));
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
            g.setColor(ThemeToolsHelper.getLuminance(color) > 0.5 ? color.darker() : color.brighter());            
            g.drawString(watermark, x, y);                         
         }
      };
      headerPanel.setName("headerPanel");
      headerPanel.setBorder(BorderFactory.createEmptyBorder());
      JLabel header = new JLabel("Processing Theme Editor") {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("editor.gradient.top"));
            setForeground(ThemeToolsHelper.getContrastColor(getBackground()));
            super.paintComponent(g);
         }
      };
      header.setSize(new Dimension(this.getWidth(), 40));      
      headerPanel.add(header);
      return headerPanel;
   }
   /**
    * Creates the buttons.
    *
    * @return the JPanel
    */
   public JPanel createButtons() {
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2)) {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("editor.gradient.bottom"));
            super.paintComponent(g);
         }
      };
      buttonPanel.setName("buttonPanel");
      buttonPanel.setBorder(BorderFactory.createEmptyBorder());

      JButton btnSave = new JButton("Save") {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("header.tab.selected.color"));
            setForeground(Theme.getColor("header.text.selected.color"));
            super.paintComponent(g);
         }
      };
      btnSave.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Theme.save();
         }
      });
      buttonPanel.add(btnSave);

      JButton btnRevert = new JButton("Revert") {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("header.tab.selected.color"));
            setForeground(Theme.getColor("header.text.selected.color"));
            super.paintComponent(g);
         }
      };
      btnRevert.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            ctx.getProperties().revert();
         }
      });

      buttonPanel.add(btnRevert);

      JButton btnClose = new JButton("Close") {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("header.tab.selected.color"));
            setForeground(Theme.getColor("header.text.selected.color"));
            super.paintComponent(g);
         }
      };
      btnClose.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            dispose();
         }
      });
      buttonPanel.add(btnClose);

      return buttonPanel;
   }

   /**
    * Creates the tables for tabbedPane.
    *
    * @return the JPanel
    */
   public JPanel createTables() {
      JPanel tablePanel = new JPanel(new BorderLayout()) {
         @Override
         protected void paintBorder(Graphics g) {
            setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Theme.getColor(ThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
            super.paintBorder(g);
         }
      };
      PThemeTabbedPane tabbedPane = new PThemeTabbedPane();
      tabbedPane.setBorder(BorderFactory.createEmptyBorder());

      ThemeToolsHelper.GROUP_ITEMS.forEach((k, v) -> {
         JScrollPane scroll = new JScrollPane(new ThemeTable(ctx, k, v)) {
            @Override
            public void paint(Graphics g) {
               setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Theme.getColor(ThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
               if (getVerticalScrollBar().getUI() instanceof PdeScrollBarUI)
                  ((PdeScrollBarUI) getVerticalScrollBar().getUI()).updateTheme();
               super.paint(g);
            }
         };
         scroll.getVerticalScrollBar().setUI(new PdeScrollBarUI(ThemeToolsHelper.PDE_SCROLLBAR_UI_ATTR));
         tabbedPane.addTab(k, scroll);
      });
      tablePanel.add(tabbedPane, BorderLayout.CENTER);
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
