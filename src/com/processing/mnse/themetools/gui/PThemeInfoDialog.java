package com.processing.mnse.themetools.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.processing.mnse.themetools.common.PThemeMainContext;
import com.processing.mnse.themetools.common.PThemeToolsHelper;
import com.processing.mnse.themetools.table.PThemeTable;

import processing.app.laf.PdeScrollBarUI;
import processing.app.ui.Theme;

/**
 * The Class PThemeInfoDialog.
 * simple info dialog
 * 
 * @author mnse
 */
@SuppressWarnings("serial")
public class PThemeInfoDialog extends JDialog {

   /**
    * Instantiates a new p theme info dialog.
    *
    * @param table the table
    * @param e     the mouse event
    */
   public PThemeInfoDialog(PThemeTable table, MouseEvent e) {
      super();
      setUndecorated(true);

      int row = table.rowAtPoint(e.getPoint());
      int col = table.columnAtPoint(e.getPoint());
      if (col != 0 || table.isEditing()) {
         return;
      }

      String lbl = (String) table.getModel().getValueAt(row, col);
      if (lbl == null || lbl.isEmpty())
         return;

      String toolTipText = PThemeMainContext.instance().findToolTip(lbl);
      if (lbl.equals(toolTipText))
         return;

      JPanel helpPanel = new JPanel(new BorderLayout());
      JPanel headerPanel = new JPanel();
      headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
      final Point[] clickPoint = new Point[1];
      headerPanel.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            clickPoint[0] = e.getPoint();
         }
      });
      headerPanel.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent e) {
            Point point = e.getLocationOnScreen();
            PThemeInfoDialog.this.setLocation(point.x - clickPoint[0].x, point.y - clickPoint[0].y);
         }
      });
      headerPanel.setName("headerPanel");
      headerPanel.setPreferredSize(new Dimension(300, 30));
      headerPanel.setMaximumSize(new Dimension(300, 30));
      headerPanel.setSize(new Dimension(300, 30));
      headerPanel.setBackground(Theme.getColor(PThemeToolsHelper.JPANEL_BGCOLOR_ATTR));
      JLabel header = new JLabel(" affected by " + lbl.trim().replaceAll("editor\\.token\\.([^\\.]+)\\.style", "$1"));
      header.setForeground(Theme.getColor(PThemeToolsHelper.JLABEL_FGCOLOR_ATTR));
      headerPanel.add(header);
      headerPanel.add(Box.createHorizontalGlue());
      JLabel close = new JLabel("close ");
      close.setForeground(Theme.getColor(PThemeToolsHelper.JLABEL_FGCOLOR_ATTR));
      close.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            dispose();
         }
      });
      headerPanel.add(close);
      helpPanel.add(headerPanel, BorderLayout.NORTH);

      JTextArea txt = new JTextArea();
      txt.setFont(PThemeMainContext.instance().getGlobalFont().deriveFont("bold".equals(table.getModel().getValueAt(row, 2)) ? Font.BOLD : Font.PLAIN, 11f));
      txt.setBackground(Theme.getColor(PThemeToolsHelper.EDITOR_BGCOLOR_ATTR));
      txt.setForeground((Color) table.getModel().getValueAt(row, 1));
      txt.setEditable(false);
      txt.setText(toolTipText);
      txt.setCaretPosition(0);
      JScrollPane scroll = new JScrollPane(txt);
      scroll.getVerticalScrollBar().setUI(new PdeScrollBarUI(PThemeToolsHelper.PDE_SCROLLBAR_UI_ATTR));
      scroll.getHorizontalScrollBar().setUI(new PdeScrollBarUI(PThemeToolsHelper.PDE_SCROLLBAR_UI_ATTR));
      ((PdeScrollBarUI) scroll.getVerticalScrollBar().getUI()).updateTheme();
      ((PdeScrollBarUI) scroll.getHorizontalScrollBar().getUI()).updateTheme();
      helpPanel.add(scroll, BorderLayout.CENTER);

      add(helpPanel);
      helpPanel.setPreferredSize(new Dimension(300, 300));
      pack();
      setLocation(e.getLocationOnScreen());
      addWindowFocusListener(new WindowAdapter() {
         @Override
         public void windowLostFocus(WindowEvent e) {
            dispose();
         }
      });
      setVisible(true);
   }
}
