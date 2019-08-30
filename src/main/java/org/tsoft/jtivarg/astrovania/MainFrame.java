package org.tsoft.jtivarg.astrovania;

import javax.swing.*;

/**
 *
 */
public class MainFrame extends JFrame
{
	private Application m_application;

	public MainFrame(Application application, JPanel surface)
	{
		super("Astrovania - where the lines live");

		m_application = application;

		add(surface);
	}

	public void init()
	{
		MainMenu menuBar = new MainMenu(m_application);

		setJMenuBar(menuBar);
	}
}
