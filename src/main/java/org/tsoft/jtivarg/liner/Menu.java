package org.tsoft.jtivarg.liner;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 *
 */
public class Menu extends JMenu
{
	public static final String FILE_PASTE_LINES = "Add Lines...";
	public static final String FILE_CLEAR_LINES = "Clear line list";
	public static final String FILE_DELETE_LINE = "Delete current line";

	public static final String FILE_EXIT		= "Exit";

	public Menu(ActionListener actionListener)
	{
		super("Menu");
		setMnemonic(KeyEvent.VK_F);
		getAccessibleContext().setAccessibleDescription("Menu");

		JMenuItem menuItem = new JMenuItem(FILE_PASTE_LINES, KeyEvent.VK_O);
		menuItem.addActionListener(actionListener);
		add(menuItem);

		menuItem = new JMenuItem(FILE_DELETE_LINE, KeyEvent.VK_DELETE);
		menuItem.addActionListener(actionListener);
		add(menuItem);

		menuItem = new JMenuItem(FILE_CLEAR_LINES);
		menuItem.addActionListener(actionListener);
		add(menuItem);

		menuItem = new JMenuItem(FILE_EXIT, KeyEvent.VK_X);
		menuItem.addActionListener(actionListener);
		add(menuItem);
	}
}
