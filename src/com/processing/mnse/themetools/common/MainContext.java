package com.processing.mnse.themetools.common;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.processing.mnse.themetools.gui.PThemePanel;
import com.processing.mnse.themetools.logging.Log;
import com.processing.mnse.themetools.table.ThemeTable;

import processing.app.Base;
import processing.app.Mode;
import processing.app.Preferences;
import processing.app.laf.PdeScrollBarUI;
import processing.app.ui.Editor;
import processing.app.ui.Theme;

/**
 * The MainContext class.
 * context holds main objects and grants access to it.
 * 
 * @author mnse          
 */
public final class MainContext {
   
   /** The context instance. */
   private static MainContext        context;
   
   /** The components. */
   private List<Component>           components;
   
   /** The theme file. */
   private String                    themeFile;
   
   /** The file watcher. */
   private FileWatcher               fileWatcher;
   
   /** The properties. */
   private OrderedProperties         properties;
   
   /** The base. */
   private Base                      base;
   
   /** The editor. */
   private Editor                    editor;
   
   /** The mainpanel. */
   private PThemePanel               mainpanel;
   
   /** The maintable. */
   private ThemeTable                 maintable;
   
   /** The current mode. */
   private Mode                      currentMode;
   
   /** The kw map. */
   private Map<String, List<String>> kwMap;
   
   /** The global font. */
   private Font globalFont;

   /**
    * Instantiates a new main context.
    */
   private MainContext() {
      components = new ArrayList<>();
   }

   /**
    * Instance.
    *
    * @return the main context instance
    */
   public static MainContext instance() {
      if (context == null)
         context = new MainContext();
      return context;
   }

   /**
    * Inits the.
    *
    * @param base the base
    * @param themeFile the theme file
    * @throws Exception the exception
    */
   public void init(Base base, String themeFile) throws Exception {
      setBase(base);
      this.themeFile = themeFile;

      if (!Files.exists(Paths.get(themeFile))) {
         Log.info("create sketchbook theme.txt from current theme: " + Preferences.get("theme"));
         Theme.save();
      }
      if (!Files.exists(Paths.get(themeFile))) {
         throw new Exception("Unable to initialize PThemeTools: not theme.txt");
      }
      
      loadGlobalFont();
      
      properties = new OrderedProperties(themeFile);
      currentMode = editor.getMode();
      loadKeyWords();
   }

   /**
    * Load global font.
    */
   private void loadGlobalFont() {
      try (InputStream is = getClass().getResourceAsStream("/SourceCodePro-Medium.ttf")) {
         globalFont = Font.createFont(Font.TRUETYPE_FONT, is);
      } catch (FontFormatException | IOException e) {
         Log.warning("Unable to load font : ");
      }      
   }
   
   /**
    * Gets the global font.
    *
    * @return the global font
    */
   public Font getGlobalFont() {
      return globalFont;
   }

   /**
    * Gets the font height.
    *
    * @param c the c
    * @param font the font
    * @return the font height
    */
   public int getFontHeight(Component c, Font font) {
      FontMetrics metrics = c.getFontMetrics(font);
      return metrics.getHeight();
   }
   
   /**
    * Start file watcher.
    */
   public void startFileWatcher() {
      fileWatcher = new FileWatcher(themeFile);
      fileWatcher.start();
   }

   /**
    * Adds the theme component.
    *
    * @param c the component to add
    */
   public void addThemeComponent(Component c) {
      components.add(c);
   }

   /**
    * Apply theme.
    */
   public void applyTheme() {
      components.forEach(c -> {
         if (c instanceof JPanel) {
            if ("headerPanel".equals(c.getName())) {
               ((JPanel) c).setBackground(getPropertyColor(ThemeToolsHelper.JPANEL_BGCOLOR_ATTR));
               ((JPanel) c).setBorder(BorderFactory.createMatteBorder(2, 2, 0, 0, getPropertyColor(ThemeToolsHelper.JPANEL_BORDER_COLOR_ATTR)));
            } else if ("buttonPanel".equals(c.getName())) {
               ((JPanel) c).setBackground(getPropertyColor(ThemeToolsHelper.JPANEL_BGCOLOR_ATTR));
               ((JPanel) c).setBorder(null);
            } else {
               c.setBackground(getPropertyColor(ThemeToolsHelper.JPANEL_BGCOLOR_ATTR));
            }
         } else if (c instanceof JButton) {
            c.setBackground(getPropertyColor(ThemeToolsHelper.JBUTTON_BGCOLOR_ATTR));
            c.setForeground(getPropertyColor(ThemeToolsHelper.JBUTTON_FGCOLOR_ATTR));
         } else if (c instanceof JLabel) {
            c.setForeground(getPropertyColor(ThemeToolsHelper.JLABEL_FGCOLOR_ATTR));
         } else if (c instanceof JScrollPane) {
            ((JScrollPane) c).setBorder(BorderFactory.createMatteBorder(2, 2, 2, 0, getPropertyColor(ThemeToolsHelper.JSCROLLPANE_BORDER_COLOR_ATTR)));
            ((PdeScrollBarUI) ((JScrollPane) c).getVerticalScrollBar().getUI()).updateTheme();
         }
      });
   }

   /**
    * Gets the file watcher.
    *
    * @return the file watcher
    */
   public FileWatcher getFileWatcher() {
      return fileWatcher;
   }

   /**
    * Sets the file watcher.
    *
    * @param fileWatcher the new file watcher
    */
   public void setFileWatcher(FileWatcher fileWatcher) {
      this.fileWatcher = fileWatcher;
   }

   /**
    * Gets the properties.
    *
    * @return the theme properties
    */
   public OrderedProperties getProperties() {
      return properties;
   }

   /**
    * Sets the properties.
    *
    * @param properties the new properties
    */
   public void setProperties(OrderedProperties properties) {
      this.properties = properties;
   }

   /**
    * Gets the base.
    *
    * @return the processing base object 
    */
   public Base getBase() {
      return base;
   }

   /**
    * Sets the base.
    *
    * @param base the new processing base object
    */
   public void setBase(Base base) {
      this.base = base;
      this.editor = base.getActiveEditor();
   }

   /**
    * Gets the editor.
    *
    * @return the editor
    */
   public Editor getEditor() {
      return editor;
   }

   /**
    * Sets the editor.
    *
    * @param editor the new editor
    */
   public void setEditor(Editor editor) {
      this.editor = editor;
   }

   /**
    * Gets the main table.
    *
    * @return the main table
    */
   public ThemeTable getMainTable() {
      return maintable;
   }

   /**
    * Sets the main table.
    *
    * @param maintable the new main table
    */
   public void setMainTable(ThemeTable maintable) {
      this.maintable = maintable;
   }

   /**
    * Gets the main panel.
    *
    * @return the main panel
    */
   public PThemePanel getMainPanel() {
      return mainpanel;
   }

   /**
    * Sets the main panel.
    *
    * @param mainpanel the new main panel
    */
   public void setMainPanel(PThemePanel mainpanel) {
      this.mainpanel = mainpanel;
   }

   /**
    * Gets the theme file.
    *
    * @return the theme file
    */
   public String getThemeFile() {
      return themeFile;
   }

   /**
    * Gets the current mode.
    *
    * @return the current mode
    */
   public Mode getCurrentMode() {
      return currentMode;
   }

   /**
    * Gets the property color.
    *
    * @param p the p
    * @return the property color
    */
   public Color getPropertyColor(String p) {
      return Color.decode((String) properties.get(p));
   }

   /**
    * Reload file.
    *
    * @throws Exception the exception
    */
   public void reloadFile() throws Exception {
      properties.loadFile();
      Theme.loadSketchbookFile();
      EventQueue.invokeLater(() -> { 
         maintable.setEnabled(false);
         maintable.populateData();
         maintable.setEnabled(true);
         applyTheme();
         base.updateTheme();
      });
   }

   /**
    * Load key words.
    */
   public void loadKeyWords() {
      List<String> search = Arrays.asList("comment1", "comment2", "function1", "function2", "function3", "function4", "invalid", "keyword1", "keyword2", "keyword3", "keyword4","keyword5","keyword6","label","literal1","literal2","operator");
      kwMap = new HashMap<>();
      String kwfile = editor.getMode().getFolder().getAbsolutePath() + "/keywords.txt";
      Log.debug("Try loading keywords form: " + kwfile);
      try (BufferedReader br = new BufferedReader(new FileReader(kwfile))) {
         String line;         
         while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("//") || line.startsWith("#"))
               continue;            
            String[] parts = line.split("\\s+");
            if (parts.length > 1 && search.contains(parts[1].trim().toLowerCase())) {
               kwMap.computeIfAbsent(parts[1].trim().toLowerCase(), k -> new ArrayList<>()).add(parts[0].trim());
            }
         }
      } catch (IOException e) {
         Log.info("errors reading: " + kwfile);
      }
   }

   /**
    * Checks for info.
    *
    * @param v the v
    * @return true, if successful
    */
   public boolean hasInfo(String v) {
      if (v.contains(".token.")) {
         String id = v.trim().replaceAll("editor\\.token\\.([^\\.]+)\\.style", "$1");      
         if (kwMap.get(id) != null) {
            return true;
         }
      }
      return false;
   }

   /**
    * Find tool tip.
    *
    * @param v the v
    * @return the string
    */
   public String findToolTip(String v) {
      String ret = v;
      if (v.startsWith("editor.token")) {
         String id = v.trim().replaceAll("editor\\.token\\.([^\\.]+)\\.style", "$1");      
         if (kwMap.get(id) != null) {
            ret = String.join("\n",kwMap.get(id));
         }
      }
      return ret;
   }
   
   
   /**
    * Checks if is active.
    *
    * @return true, if is active
    */
   public static boolean isActive() {
      return context != null;
   }

   /**
    * Destroy.
    */
   public static void destroy() {
      if (context != null) {
         if (context.getFileWatcher() != null)
            context.getFileWatcher().stop();
         context = null;
      }
   }
}
