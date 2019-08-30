package org.tsoft.jtivarg.liner;

import java.awt.event.ActionListener;

import javax.swing.*;

public class MainMenu extends JMenuBar
{
	public MainMenu(ActionListener actionListener)
	{
		Menu fileMenu = new Menu(actionListener);
		add(fileMenu);
	}
}
