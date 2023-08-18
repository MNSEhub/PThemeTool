package com.processing.mnse.themetools.common;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.processing.mnse.themetools.PThemeTool;
import com.processing.mnse.themetools.gui.PThemePanel;
import com.processing.mnse.themetools.logging.Log;
import com.processing.mnse.themetools.table.PThemeTable;

import processing.app.Base;
import processing.app.Mode;
import processing.app.Preferences;
import processing.app.Settings;
import processing.app.ui.Editor;
import processing.app.ui.Theme;

/**
 * The PThemeMainContext class.
 * context holds main objects and grants access to it.
 * 
 * @author mnse
 */
public final class PThemeMainContext {

   /** The context instance. */
   private static PThemeMainContext context;

   /** The theme file. */
   private String themeFile;

   /** The file watcher. */
   private PThemeFileWatcher fileWatcher;

   /** The properties. */
   private PThemeProperties properties;

   /** The properties. */
   private Properties tooltipprops;

   /** The base. */
   private Base base;

   /** The Settings. */
   private Settings settings;

   /** The editor. */
   private Editor editor;

   /** The mainpanel. */
   private PThemePanel mainpanel;

   /** The tables. */
   private Map<String, PThemeTable> tables;

   /** The current mode. */
   private Mode currentMode;

   /** The kw map. */
   private Map<String, List<String>> kwMap;

   /** The global font. */
   private Font globalFont;

   /** is dirty flag. */
   private boolean isDirty;

   /**
    * Instantiates a new main context.
    */
   private PThemeMainContext() {
      tables = new HashMap<>();
   }

   /**
    * Instance.
    *
    * @return the main context instance
    */
   public static PThemeMainContext instance() {
      if (context == null)
         context = new PThemeMainContext();
      return context;
   }

   /**
    * Inits the.
    *
    * @param base      the base
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

      try {
         Field field = Theme.class.getDeclaredField("theme");
         field.setAccessible(true);
         settings = (Settings) field.get(null);
         Log.debug("Settings available!");
      } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
         throw new Exception("Settings not accessable! :/\n", e);
      }

      loadGlobalFont();

      properties = new PThemeProperties(themeFile);
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
         Log.debug("Unable to load font : ");
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
    * @param c    the c
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
      fileWatcher = new PThemeFileWatcher(themeFile);
      fileWatcher.start();
   }

   /**
    * Gets the file watcher.
    *
    * @return the file watcher
    */
   public PThemeFileWatcher getFileWatcher() {
      return fileWatcher;
   }

   /**
    * Sets the file watcher.
    *
    * @param fileWatcher the new file watcher
    */
   public void setFileWatcher(PThemeFileWatcher fileWatcher) {
      this.fileWatcher = fileWatcher;
   }

   /**
    * Gets the is dirty flag.
    *
    * @return the is dirty flag
    */
   public boolean getIsDirty() {
      return isDirty;
   }

   /**
    * Sets the is dirty flag.
    *
    * @param isdirty the new is dirty state
    */
   public void setIsDirty(boolean isdirty) {
      Log.debug("setIsDirty from " + isDirty + " to " + isdirty);
      isDirty = isdirty;
      //mainpanel.updateHeader();
   }
   
   /**
    * Gets the properties.
    *
    * @return the theme properties
    */
   public PThemeProperties getProperties() {
      return properties;
   }

   /**
    * Sets the properties.
    *
    * @param properties the new properties
    */
   public void setProperties(PThemeProperties properties) {
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
    * Get the processing theme settings
    *
    * @return the current theme settings
    */
   public Settings getSettings() {
      return settings;
   }

   /**
    * Gets the current editor.
    *
    * @return the current editor
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
    * @param name of the table
    *
    * @return the table for corresponding name
    */
   public PThemeTable getTable(String name) {
      return tables.get(name);
   }

   /**
    * adds a table from table name.
    *
    * @param table to be added
    */
   public void addTable(PThemeTable table) {
      tables.put(table.getName(), table);
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
    * Reload file.
    *
    * @throws Exception the exception
    */
   public void reloadFile() throws Exception {
      properties.loadFile();
      Theme.loadSketchbookFile();
      updateTables();
   }

   /**
    * updates tables by corresponding properties.
    *
    * @throws Exception the exception
    */
   public void updateTables() throws Exception {
      EventQueue.invokeLater(() -> {
         for (PThemeTable t : tables.values()) {
            t.setEnabled(false);
            t.populateData();
            t.setEnabled(true);
         }
         base.updateTheme();
         editor.invalidate();
      });
   }   
   
   /**
    * Load key words.
    */
   public void loadKeyWords() {
      List<String> search = Arrays.asList("comment1", "comment2", "function1", "function2", "function3", "function4", "invalid", "keyword1", "keyword2", "keyword3", "keyword4", "keyword5", "keyword6",
            "label", "literal1", "literal2", "operator");
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
         Log.debug("errors reading keywords: " + kwfile);
      }
      loadHelp();
   }

   /**
    * Load key words.
    */
   public void loadHelp() {
      tooltipprops = new Properties();
      try (InputStream is = getClass().getResourceAsStream("/help.properties")) {
         tooltipprops.load(is);
         tooltipprops.entrySet().removeIf(entry -> {
            String value = (String) entry.getValue();
            return value == null || value.trim().isEmpty();
        });
        Log.debug("help properties loaded");
      } catch(Exception e) {
         Log.debug("unable to load help properties");
      }
   }
   
   /**
    * Checks for info.
    *
    * @param v the v
    * @return true, if successful
    */
   public boolean hasInfo(String v) {
      if (v == null)
         return false;
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
            ret = String.join("\n", kwMap.get(id));
         }
      } else if (tooltipprops != null) {
         ret=tooltipprops.getProperty(v,v);
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
