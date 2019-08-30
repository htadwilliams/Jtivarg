package org.tsoft.jtivarg.astrovania;

import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        String newString = String.valueOf(true);

        // Fixes a lot of repaint bugs when redrawing menus or dragging windows over each other.
        System.setProperty("swing.bufferPerWindow", "false");

        EventQueue.invokeLater(() ->
        {
            Application application = new Application();
            javax.swing.SwingUtilities.invokeLater(application);
        });
    }
}
