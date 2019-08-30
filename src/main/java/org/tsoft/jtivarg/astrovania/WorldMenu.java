package org.tsoft.jtivarg.astrovania;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 *
 */
public class WorldMenu extends JMenu
{
    public static final String TICK = "Tick (space)";
    public static final String RESET = "Reset";
    public static final String RESET_DEMO = "Reset demo (mmouse)";
    public static final String RUN = "Run (rmouse toggle)";
    public static final String STOP = "Stop (rmouse toggle)";

    public WorldMenu(ActionListener actionListener)
    {
        super("World");
        setMnemonic(KeyEvent.VK_T);
        getAccessibleContext().setAccessibleDescription("Do things with the game world");

        JMenuItem menuItem = new JMenuItem(TICK, KeyEvent.VK_T);
        menuItem.addActionListener(actionListener);
        add(menuItem);

        menuItem = new JMenuItem(RESET, KeyEvent.VK_E);
        menuItem.addActionListener(actionListener);
        add(menuItem);

        menuItem = new JMenuItem(RESET_DEMO, KeyEvent.VK_D);
        menuItem.addActionListener(actionListener);
        add(menuItem);

        menuItem = new JMenuItem(RUN, KeyEvent.VK_R);
        menuItem.addActionListener(actionListener);
        add(menuItem);

        menuItem = new JMenuItem(STOP, KeyEvent.VK_S);
        menuItem.addActionListener(actionListener);
        add(menuItem);
    }
}
