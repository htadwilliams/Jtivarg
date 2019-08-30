package org.tsoft.jtivarg.yticlock;

import javax.swing.*;

import org.tsoft.jtivarg.astrovania.MainMenu;

/**
 *
 */
public class MainFrame extends JFrame
{
	Application m_application;

	public MainFrame(Application application, JPanel surface)
	{
		super("Jtivarg ~ Gravity -> ytivarG in Java!");

		m_application = application;

		add(surface);
	}

	public void init()
	{
		MainMenu menuBar = new MainMenu(m_application);

		setJMenuBar(menuBar);
	}
}
