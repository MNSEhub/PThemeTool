package com.processing.mnse.themetools.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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
 * The Class PThemeExitDialog.
 * simple exit dialog, call if state is dirty
 * 
 * @author mnse
 */
@SuppressWarnings("serial")
public class PThemeExitDialog extends JDialog {
   boolean doSave = false;
   
   /**
    * Instantiates a new Ptheme exit dialog.
    *
    * @param parent Panel
    */
   public PThemeExitDialog(PThemePanel parent) {
      super(PThemeMainContext.instance().getEditor() ,true);
      setUndecorated(true);

      String lbl = " Action required!";
      
      JPanel exitPanel = new JPanel(new BorderLayout());

      // header
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
            PThemeExitDialog.this.setLocation(point.x - clickPoint[0].x, point.y - clickPoint[0].y);
         }
      });
      headerPanel.setPreferredSize(new Dimension(300, 30));
      headerPanel.setMaximumSize(new Dimension(300, 30));
      headerPanel.setSize(new Dimension(300, 30));
      headerPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Theme.getColor("header.text.selected.color")));
      headerPanel.setBackground(Theme.getColor(PThemeToolsHelper.JPANEL_BGCOLOR_ATTR));

      JLabel header = new JLabel(lbl);
      header.setForeground(Theme.getColor(PThemeToolsHelper.JLABEL_FGCOLOR_ATTR));
      headerPanel.add(header);
      exitPanel.add(headerPanel, BorderLayout.NORTH);
      
      // content
      JPanel contentPanel = new JPanel();
      contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
      contentPanel.setPreferredSize(new Dimension(300, 100));
      contentPanel.setMaximumSize(new Dimension(300, 100));
      contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
      contentPanel.setSize(new Dimension(300, 100));
      contentPanel.setBackground(Theme.getColor(PThemeToolsHelper.JPANEL_BGCOLOR_ATTR));

      JLabel content = new JLabel(" You've unsaved changes, what to do ?");
      content.setForeground(Theme.getColor(PThemeToolsHelper.JLABEL_FGCOLOR_ATTR));
      contentPanel.add(content);
      exitPanel.add(contentPanel, BorderLayout.CENTER);      

      // buttons      
      JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3)) {
         @Override
         protected void paintComponent(Graphics g) {
            setBackground(Theme.getColor("editor.gradient.bottom"));
            super.paintComponent(g);
         }
      };
      buttonPanel.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Theme.getColor("header.text.selected.color")));

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
            doSave=true;
            dispose();
         }
      });
      buttonPanel.add(btnSave);

      JButton btnRevert = new JButton("Don't Save") {
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
            dispose();
         }
      });

      buttonPanel.add(btnRevert);
      exitPanel.add(buttonPanel, BorderLayout.SOUTH);
      add(exitPanel);
      setSize(300,300);
      pack();
      setLocationRelativeTo(PThemeMainContext.instance().getEditor());
      setVisible(true);
   }
   
   public boolean wantsToSave() {
      return doSave;
   }
}
