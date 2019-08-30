package org.tsoft.jtivarg.yticlock;

import java.awt.*;

import org.tsoft.jtivarg.yticlock.Application;

public class Main
{
	public static void main(String[] args)
	{
        // Fixes a lot of repaint bugs when redrawing menus or dragging windows over each other.
        System.setProperty("swing.bufferPerWindow", "false");

		EventQueue.invokeLater(() ->
		{
            Application application = new Application();
            javax.swing.SwingUtilities.invokeLater(application);
        });
	}
}
