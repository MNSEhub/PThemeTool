package com.processing.mnse.themetools.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.processing.mnse.themetools.common.PThemeToolsHelper;

/**
 * The Class Log.
 * logging class
 * 
 * @author mnse
 */
public final class Log {

   /** The Constant logger. */
   private static final Logger logger = Logger.getLogger("##project.name##");

   static {
      ConsoleHandler handler = new ConsoleHandler();
      handler.setFormatter(new CustomFormatter());
      logger.addHandler(handler);
      logger.setUseParentHandlers(false);
   }

   /**
    * Debug.
    *
    * @param msg the message
    */
   public static void debug(String msg) {
      logger.log(Level.FINE, msg);
   }

   /**
    * Info.
    *
    * @param msg the message
    */
   public static void info(String msg) {
      logger.log(Level.INFO, msg);
   }

   /**
    * Warning.
    *
    * @param msg the message
    */
   public static void warning(String msg) {
      logger.log(Level.WARNING, msg);
   }

   /**
    * Error.
    *
    * @param msg the message
    */
   public static void error(String msg) {
      logger.log(Level.SEVERE, msg);
   }

   /**
    * The Class CustomFormatter.
    */
   static class CustomFormatter extends Formatter {

      /** The Constant DateTimeFormat. */
      private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PThemeToolsHelper.DATE_TIME_FORMAT);

      /**
       * Format.
       *
       * @param record the record
       * @return the formatted string
       */
      @Override
      public String format(LogRecord record) {
         return String.format(PThemeToolsHelper.LOG_MSG_FORMAT, dtf.format(LocalDateTime.now()), record.getLevel(), record.getLoggerName(), record.getMessage());
      }
   }
}
