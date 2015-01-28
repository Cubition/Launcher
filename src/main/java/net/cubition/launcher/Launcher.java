package net.cubition.launcher;

import javafx.application.Platform;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Cubition Launcher eases the login and Bootstrap process for the Client. This class specifically
 * starts a Swing prompt, asking for user credentials, and gets Bootstrap ready.
 */
public class Launcher {
    private File destinationRoot = new File(PlatformUtils.getApplicationData(), "Cubition");

    private File bootstrapDest = new File(destinationRoot, "bootstrap.jar");
    private File modDirDest = new File(destinationRoot, "mods");

    /**
     * Starts the launcher.
     */
    public void start() {
        // Make sure we have a proper directory structure built
        if (!modDirDest.exists() && !modDirDest.mkdirs()) {
            error("Failed to create destination Cubition directory:\n" + modDirDest.getPath());
            System.exit(-1);
        }

        // Create and display the Launcher
        try {
            LauncherApplication.start();
        } catch (Exception e) {
            error("Error while launching Application", e);
        }
    }

    public void downloadBootstrap() {

    }

    public static void error(String err) {
        error(err, null);
    }

    public static void error(String err, Exception exception) {
        String fullMessage = err + (exception != null ? (": " + exception.getLocalizedMessage()) : "");

        JOptionPane.showMessageDialog(null, fullMessage, "Cubition Launcher", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Helper method to build a queue containing all things the Application should do during launch.
     */
    public static Queue<Task<String, Runnable>> buildQueue() {
        Queue<Task<String, Runnable>> taskQueue = new ConcurrentLinkedQueue<>();

        // TODO: Buff this out, really messy code
        taskQueue.add(new Task<>("Logging in...", () -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        taskQueue.add(new Task<>("Downloading bootstrap...", () -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        taskQueue.add(new Task<>("Building dependencies...", () -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        taskQueue.add(new Task<>("Downloading dependencies...", () -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        taskQueue.add(new Task<>("Launching...", () -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Platform.exit();
        }));

        return taskQueue;
    }


    public static void main(String[] args) {
        System.out.println("Cubition Launcher v0.1");
        Launcher launcher = new Launcher();
        launcher.start();
    }
}
