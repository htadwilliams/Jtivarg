package org.tsoft.jtivarg.astrovania;

import java.awt.event.ActionListener;

import javax.swing.*;

public class MainMenu extends JMenuBar
{
    public MainMenu(ActionListener actionListener)
    {
        add(new GameFileMenu(actionListener));
        add(new TestMenu(actionListener));
        add(new WorldMenu(actionListener));
    }
}
