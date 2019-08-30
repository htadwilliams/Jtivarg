package org.tsoft.jtivarg.yticlock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.tsoft.jtivarg.common.application.FileMenu;

/**
 *
 */
public class Application implements Runnable, ActionListener
{
	private MainFrame m_mainFrame = null;
	private File[] m_filesChosen = null;

	private void initGUI()
	{
		m_mainFrame = new MainFrame(this, new SurfaceClock());

		m_mainFrame.init();

		m_mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		m_mainFrame.pack();
		m_mainFrame.setVisible(true);
		m_mainFrame.setSize(1024, 768);
	}

	@Override
	public void run()
	{
		initGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("actionPerformed: " + e.toString());

		String command = e.getActionCommand();

		switch (command)
		{
			case FileMenu.FILE_OPEN:
				doFileOpen();
				break;

			case FileMenu.FILE_EXIT:
				System.exit(1);
				break;

			default:
				// do nothing
				break;
		}
	}

	private void doFileOpen()
	{
		m_filesChosen = chooseLogFile(m_mainFrame);
	}

	public File[] chooseLogFile(Component parent)
	{
		File[] chosenFileArray = null;

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("All files", "*");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(true);

		int returnVal = chooser.showOpenDialog(parent);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			chosenFileArray = chooser.getSelectedFiles();
		}

		return chosenFileArray;
	}
}
