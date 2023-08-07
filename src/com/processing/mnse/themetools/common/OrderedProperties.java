package com.processing.mnse.themetools.common;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;

import com.processing.mnse.themetools.logging.Log;
import com.processing.mnse.themetools.table.ThemeTableEntry;

/**
 * The Class OrderedProperties.
 * keeps and handle the theme properties
 * 
 * @author mnse 
 */
public final class OrderedProperties extends Properties {
   
   /** The Constant serialVersionUID. */
   private static final long           serialVersionUID = 6143210832128003462L;
   
   /** The keys. */
   private final LinkedHashSet<Object> keys             = new LinkedHashSet<>();
   
   /** The filename. */
   private String                      filename;
   
   /** The backupfilename. */
   private String                      backupfilename;

   /**
    * Instantiates a new ordered properties.
    *
    * @param filename the filename
    * @throws Exception the exception
    */
   public OrderedProperties(String filename) throws Exception {
      super();
      this.filename = filename;
      loadFile();
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      backupfilename = filename.replaceAll("theme.txt$", now.format(formatter) + "_" + "theme.txt");
      backupFile();
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
    * @param key the key
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
      Log.info("properties loaded from: " + filename);
   }

   /**
    * Update property entry by key/value.
    *
    * @param key the key
    * @param v1 the first value
    * @param v2 the second value
    */
   public void updateEntry(String key, String v1, String v2) {
      if (ThemeToolsHelper.VALUE_NOT_AVAILABLE.equals(v2)) {
         Log.debug("update property: " + key + "=" + v1);
         setProperty(key, v1);
      } else {
         Log.debug("update property: " + key + "=" + v1 + "," + v2);
         setProperty(key, v1 + "," + v2);
      }
   }

   /**
    * Update entry.
    *
    * @param tableEntry the tableEntry by tableEntry
    */
   public void updateEntry(ThemeTableEntry tableEntry) {
      setProperty(tableEntry.getLabel(), tableEntry.createPropertyValue());
   }

   /**
    * Update theme file.
    */
   public void updateFile() {
      try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
         for (Object key : orderedKeys()) {
            String k = (String) key;
            String v = getProperty(k).trim();
            writer.write(k + "=" + v);
            writer.newLine();
         }
      } catch (IOException e) {
         Log.warning("Failed to apply theme.txt to " + filename);
      }
   }

   /**
    * Revert theme file from backup.
    */
   public void revertFile() {
      try {
         Files.copy(Paths.get(backupfilename), Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
         try (FileInputStream fis = new FileInputStream(this.filename)) {
            load(fis);
         }
         Log.debug("revert theme.txt from backup: " + backupfilename);
      } catch (IOException e) {
         Log.warning("can't revert file: " + backupfilename + "\n" + e.getMessage());
      }
   }

   /**
    * Backup theme file.
    */
   public void backupFile() {
      Log.debug("creating backup of theme.txt to " + backupfilename);

      try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(backupfilename))) {
         for (Object key : orderedKeys()) {
            String k = (String) key;
            String v = getProperty(k);
            writer.write(k + "=" + v);
            writer.newLine();
         }
      } catch (IOException e) {
         Log.warning("Failed to backup theme.txt to " + backupfilename);
      }
   }
}
