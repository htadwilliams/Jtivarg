package org.tsoft.jtivarg.common.application;

import java.awt.event.ActionListener;

import javax.swing.*;

public class MainMenu extends JMenuBar
{
	public MainMenu(ActionListener actionListener)
	{
		FileMenu fileMenu = new FileMenu(actionListener);
		add(fileMenu);

//		AboutMenu aboutMenu = new AboutMenu(actionListener);
	}
}
