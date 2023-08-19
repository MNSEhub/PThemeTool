package com.processing.mnse.themetools;

/**
 * ##tool.name## - ##tool.sentence##
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 *
 * @author   ##author##
 * @modified ##date##
 * @version  ##tool.prettyVersion##
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.processing.mnse.themetools.common.PThemeMainContext;
import com.processing.mnse.themetools.gui.PThemePanel;
import com.processing.mnse.themetools.logging.Log;

import processing.app.Base;
import processing.app.tools.Tool;
import processing.app.ui.Theme;

/**
 * The Class PThemeTool.
 * init point for processing tool
 * 
 * @author mnse
 */
public class PThemeTool implements Tool {

   /** The base. */
   private Base base;

   /**
    * Gets the menu title.
    *
    * @return the menu title
    */
   public String getMenuTitle() {
      return "##tool.name##";
   }

   /**
    * Inits the Tool.
    *
    * @param base the base processing object
    */
   public void init(Base base) {
      this.base = base;
   }

   /**
    * activates the Tool.
    */
   public void run() {
      Log.info("Processing Version: " + Base.getVersionName() + " Rev: " + Base.getRevision());
      Dimension od = base.getActiveEditor().getSize();
      Log.debug("Editor: height=" + base.getActiveEditor().getHeight());
      final String[] version = Base.getVersionName().split("\\.");
      try {
         EventQueue.invokeLater(() -> {
            try {
               if (Integer.parseInt(version[0]) != 4 || Integer.parseInt(version[1]) < 2) {
                  JOptionPane.showMessageDialog(null, "Sorry! ##tool.name## will not be loaded!\n##tool.name## needs Processing Version 4.2 or above!", "Error", JOptionPane.ERROR_MESSAGE);
                  return;
               }
               PThemeMainContext ctx;
               if (PThemeMainContext.isActive()) {
                  ctx = PThemeMainContext.instance();
                  if (base.getActiveEditor().getMode().equals(ctx.getCurrentMode())) {
                     Log.debug("##tool.name## already active: " + base.getActiveEditor().getMode());
                     return;
                  }
                  if (ctx.getMainPanel() != null) {
                     ctx.getMainPanel().dispose();
                  }
                  PThemeMainContext.destroy();
               }
               ctx = PThemeMainContext.instance();
               ctx.init(base, Theme.getSketchbookFile().getAbsolutePath());
               Log.debug("*** loading ##tool.name##!");
               ctx.setMainPanel(new PThemePanel(ctx));
               ctx.getEditor().add(ctx.getMainPanel(), BorderLayout.EAST);
               ctx.getEditor().pack();
               int w = (int) Math.min(od.getWidth()+ctx.getMainPanel().getWidth(),Toolkit.getDefaultToolkit().getScreenSize().getWidth());
               od.setSize(w, od.getHeight());
               ctx.getEditor().setSize(od);
               if ((ctx.getEditor().getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
                  ctx.getEditor().setExtendedState(JFrame.MAXIMIZED_BOTH);
               }                              
               ctx.getEditor().setLocationRelativeTo(null);               
               Log.debug("Editor: height=" + base.getActiveEditor().getHeight());
               Log.info("*** success initialized ##tool.name##!");
            } catch (Exception e) {
               Log.error("Error on initialize ##tool.name##: " + e.getMessage());
               PThemeMainContext.destroy();
            }
         });
      } catch (Exception e) {
         Log.error("Error on invoking ##tool.name##: " + e.getMessage());
      }
   }
}
