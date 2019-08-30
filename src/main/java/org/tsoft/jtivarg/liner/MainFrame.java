package org.tsoft.jtivarg.liner;

import javax.swing.*;

/**
 *
 */
public class MainFrame extends JFrame
{
	public MainFrame()
	{
		super("Liner - draw lines for debugging");
	}

	public void init()
	{
		MainMenu menuBar = new MainMenu(Application.getInstance());

		setJMenuBar(menuBar);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

		// TODO hard-coded size
        setSize(1024, 768);
	}
}
