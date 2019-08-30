package org.tsoft.jtivarg.astrovania;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 *
 */
public class TestMenu extends JMenu
{
	public static final String TEST_LINES = "Test random lines";
	public static final String TEST_BADBOUNCE = "Test bad bounce";

	public TestMenu(ActionListener actionListener)
	{
		super("Test");
		setMnemonic(KeyEvent.VK_T);
		getAccessibleContext().setAccessibleDescription("Developer tests");

		JMenuItem menuItem = new JMenuItem(TEST_LINES, KeyEvent.VK_L);
		menuItem.addActionListener(actionListener);
		add(menuItem);

		menuItem = new JMenuItem(TEST_BADBOUNCE, KeyEvent.VK_B);
		menuItem.addActionListener(actionListener);
		add(menuItem);
	}
}
