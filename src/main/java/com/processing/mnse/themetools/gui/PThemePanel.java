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

import com.processing.mnse.themetools.common.PThemeMainContext;
import com.processing.mnse.themetools.common.PThemeToolsHelper;
import com.processing.mnse.themetools.logging.Log;
import com.processing.mnse.themetools.table.PThemeTable;

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
   private PThemeMainContext ctx;

   /**
    * Instantiates a new PThemePanel object.
    *
    * @param ctx the main context
    * @throws Exception the exception
    */
   public PThemePanel(PThemeMainContext ctx) throws Exception {
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
      setBackground(Theme.getColor(PThemeToolsHelper.JPANEL_BGCOLOR_ATTR));
      super.paintComponent(g);
   }

   @Override
   protected void paintBorder(Graphics g) {
      setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Theme.getColor(PThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
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
            Font watermarkFont = g.getFont().deriveFont(Font.BOLD, 14f);
            g.setFont(watermarkFont);
            FontMetrics metrics = g.getFontMetrics(watermarkFont);
            int x = (getWidth() - metrics.stringWidth(watermark));
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
            g.setColor(PThemeToolsHelper.getLuminance(color) > 0.5 ? color.darker() : color.brighter());
            g.drawString(watermark, x, y);
         }
      };
      headerPanel.setName("headerPanel");
      headerPanel.setBorder(BorderFactory.createEmptyBorder());
      JLabel header = new JLabel("Processing Theme Editor") {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("editor.gradient.top"));
            setForeground(PThemeToolsHelper.getContrastColor(getBackground()));
            if (ctx.getIsDirty()) {
               setText("Processing Theme Editor *");               
            } else {
               setText("Processing Theme Editor");   
            }            
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
            ctx.setIsDirty(false);
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
            Log.debug("Close dirty Status: " + ctx.getIsDirty());
            if (ctx.getIsDirty()) {
               PThemeExitDialog whattodo = new PThemeExitDialog(PThemePanel.this);
               if (whattodo.wantsToSave()) {
                  Log.debug("save on close");
                  ctx.getFileWatcher().stop();
                  Theme.save();
               } else {
                  Log.debug("revert on close");
                  Theme.reload();
                  PThemeMainContext.instance().getBase().updateTheme();
                  PThemeMainContext.instance().getEditor().invalidate();                  
               }
            }
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
            setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Theme.getColor(PThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
            super.paintBorder(g);
         }
      };
      PThemeTabbedPane tabbedPane = new PThemeTabbedPane();
      tabbedPane.setBorder(BorderFactory.createEmptyBorder());

      PThemeToolsHelper.GROUP_ITEMS.forEach((k, v) -> {
         JScrollPane scroll = new JScrollPane(new PThemeTable(ctx, k, v)) {
            @Override
            public void paint(Graphics g) {
               setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Theme.getColor(PThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
               if (getVerticalScrollBar().getUI() instanceof PdeScrollBarUI)
                  ((PdeScrollBarUI) getVerticalScrollBar().getUI()).updateTheme();
               super.paint(g);
            }
         };
         scroll.getVerticalScrollBar().setUI(new PdeScrollBarUI(PThemeToolsHelper.PDE_SCROLLBAR_UI_ATTR));
         tabbedPane.addTab(k, scroll);
      });
      tablePanel.add(tabbedPane, BorderLayout.CENTER);
      return tablePanel;
   }

//   public void updateHeader() {
//      if (headerPanel != null)
//         headerPanel.repaint();
//   }
   
   /**
    * Dispose dialog panel.
    */
   public void dispose() {
      getParent().remove(this);
      ctx.getEditor().pack();
      PThemeMainContext.destroy();
   }
}
