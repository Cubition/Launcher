package net.cubition.launcher;

import javax.swing.*;
import java.awt.*;

/**
 * The LauncherFrame is the main GUI portion of the Cubition Launcher.
 */
public class LauncherFrame extends JFrame {
    private Launcher parent;

    public LauncherFrame(Launcher parent) {
        this.parent = parent;

        setUndecorated(true);

        // Set preferred width
        setPreferredSize(new Dimension(640, 480));
        setSize(getPreferredSize());

        // Make sure it sits in the middle of the screen
        setLocationRelativeTo(null);
    }
}
