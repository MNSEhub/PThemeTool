package com.processing.mnse.themetools.common;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;

import com.processing.mnse.themetools.logging.Log;
import com.processing.mnse.themetools.table.PThemeTableEntry;

/**
 * The Class PThemeProperties.
 * keeps and handle the theme properties
 * 
 * @author mnse
 */
public final class PThemeProperties extends Properties {

   /** The Constant serialVersionUID. */
   private static final long serialVersionUID = 6143210832128003462L;

   /** The keys. */
   private final LinkedHashSet<Object> keys = new LinkedHashSet<>();

   /** The filename. */
   private String filename;

   /**
    * Instantiates a new ordered properties.
    *
    * @param filename the filename
    * @throws Exception the exception
    */
   public PThemeProperties(String filename) throws Exception {
      super();
      this.filename = filename;
      loadFile();
   }

   /**
    * Ordered keys.
    *
    * @return the iterable
    */
   public Iterable<Object> orderedKeys() {
      return Collections.list(keys());
   }

   /**
    * Keys.
    *
    * @return the enumeration
    */
   public Enumeration<Object> keys() {
      return Collections.<Object>enumeration(keys);
   }

   /**
    * Put.
    *
    * @param key   the key
    * @param value the value
    * @return the object
    */
   public Object put(Object key, Object value) {
      keys.add(key);
      return super.put(key, value);
   }

   /**
    * Prints the.
    */
   public void print() {
      for (Object key : orderedKeys()) {
         Log.debug(key + "=" + getProperty((String) key));
      }
   }

   /**
    * Load file.
    *
    * @throws Exception the exception
    */
   public void loadFile() throws Exception {
      Log.debug("Try loading style properties of: " + filename);
      try (FileInputStream fis = new FileInputStream(this.filename)) {
         load(fis);
      }
      print();
      Log.debug("properties loaded from: " + filename);
   }

   /**
    * Update entry.
    *
    * @param tableEntry the tableEntry by tableEntry
    */
   public void updateEntry(PThemeTableEntry tableEntry) {
      PThemeMainContext.instance().getSettings().set(tableEntry.getLabel(), tableEntry.createPropertyValue());
      PThemeMainContext.instance().getBase().updateTheme();
      PThemeMainContext.instance().getEditor().invalidate();
   }

   /**
    * Reverts the theme settings which are loaded on startup
    *
    */
   public void revert() {
      for (Object key : orderedKeys()) {
         String k = (String) key;
         String v = getProperty(k);
         PThemeMainContext.instance().getSettings().set(k, v);
      }
      PThemeMainContext.instance().getBase().updateTheme();
      PThemeMainContext.instance().getEditor().invalidate();
      try {
         PThemeMainContext.instance().updateTables();
      } catch(Exception e) {
         Log.warning("issues on revert tables!?");
      }
   }
}
