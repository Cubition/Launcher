package net.cubition.launcher;

import java.io.File;

/**
 * A basic set of PlatformUtils to help make Cubition platform-independent.
 */
public class PlatformUtils {
    public static File getApplicationData() {
        String os = System.getProperty("os.name").toUpperCase();

        if (os.contains("WIN")) {
            return new File(System.getenv("AppData"));
        } else if (os.contains("OS X")) {
            return new File(System.getProperty("user.home") + "/Library/Application Support/");
        } else if (os.contains("UNIX"));
            return new File(System.getProperty("user.home")); + "/usr/bin/"
        } else {
            return new File(System.getProperty("user.home"));
        }   
    }
}
