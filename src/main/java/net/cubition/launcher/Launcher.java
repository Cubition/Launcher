package net.cubition.launcher;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

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
        LauncherApplication.start();
    }

    public void error(String err) {
        error(err, null);
    }

    public void error(String err, Exception exception) {
        String fullMessage = err + (exception != null ? (": " + exception.getLocalizedMessage()) : "");

        JOptionPane.showMessageDialog(null, fullMessage, "Cubition Launcher", JOptionPane.ERROR_MESSAGE);
    }


    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.start();
    }
}
