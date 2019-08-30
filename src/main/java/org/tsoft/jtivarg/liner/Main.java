package org.tsoft.jtivarg.liner;

import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        // Fixes a lot of repaint bugs when redrawing menus or dragging windows over each other.
        System.setProperty("swing.bufferPerWindow", "false");

        Application app = Application.getInstance();
        EventQueue.invokeLater(() -> javax.swing.SwingUtilities.invokeLater(app));
    }
}
