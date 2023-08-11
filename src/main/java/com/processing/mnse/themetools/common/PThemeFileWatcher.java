package com.processing.mnse.themetools.common;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.processing.mnse.themetools.logging.Log;

/**
 * The Class PThemeFileWatcher.
 * 
 * @author mnse
 */
public final class PThemeFileWatcher implements Runnable {

   /** The path. */
   private final Path path;

   /** The tracked filename. */
   private final Path trackedFilename;

   /** The running. */
   private boolean running = false;

   /** The paused. */
   private boolean paused = false;

   /** The pause lock. */
   private final Object pauseLock = new Object();

   /** The scheduler. */
   private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

   /** The future. */
   private ScheduledFuture<?> future = null;

   /**
    * Instantiates a new file watcher.
    *
    * @param fname the filename being tracked for changes
    */
   public PThemeFileWatcher(String fname) {
      trackedFilename = Paths.get(fname).getFileName();
      path = Paths.get(fname).getParent();
      Log.info("tracking: " + trackedFilename + " in path " + path);
   }

   /**
    * Run the filewatcher service.
    */
   public void run() {
      try {
         WatchService watchService = FileSystems.getDefault().newWatchService();
         path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

         while (running) {
            synchronized (pauseLock) {
               if (paused) {
                  try {
                     pauseLock.wait();
                  } catch (InterruptedException e) {
                     Thread.currentThread().interrupt();
                     return;
                  }
               }
            }

            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
               WatchEvent.Kind<?> kind = event.kind();

               if (kind == StandardWatchEventKinds.OVERFLOW) {
                  continue;
               }

               @SuppressWarnings("unchecked")
               WatchEvent<Path> ev = (WatchEvent<Path>) event;
               Path filename = ev.context();
               if (filename.equals(trackedFilename)) {
                  if (future != null && !future.isDone()) {
                     future.cancel(false);
                  }
                  future = scheduler.schedule(this::reloadFile, 500, TimeUnit.MILLISECONDS);
               }
            }
            boolean valid = key.reset();
            if (!valid) {
               break;
            }
         }
      } catch (IOException | InterruptedException e) {
         e.printStackTrace();
      }
   }

   /**
    * Reload trigger theme file.
    */
   private void reloadFile() {
      try {
         Log.debug("Reloading file");
         PThemeMainContext.instance().reloadFile();
      } catch (Exception e) {
         Log.error("Issues loading file !?");
      }
   }

   /**
    * Start file watching.
    */
   public void start() {
      running = true;
      new Thread(this).start();
   }

   /**
    * Pause file watching.
    */
   public void pause() {
      paused = true;
   }

   /**
    * Resume file watching.
    */
   public void resume() {
      synchronized (pauseLock) {
         paused = false;
         pauseLock.notify();
      }
   }

   /**
    * Stop file watching.
    */
   public void stop() {
      running = false;
   }
}
