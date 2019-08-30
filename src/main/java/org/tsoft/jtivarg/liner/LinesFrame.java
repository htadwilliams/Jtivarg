package org.tsoft.jtivarg.liner;

import javax.swing.*;

/**
 *
 */
public class LinesFrame extends JFrame
{
    private JTable m_table = null;
    private JScrollPane m_scrollPane = null;

    public LinesFrame()
    {
        super("Lines");
    }

    public void init()
    {
        m_table = new JTable(Application.getInstance().getLinesModel());
        m_table.setSelectionModel(new DefaultListSelectionModel());
        m_table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        m_table.setFillsViewportHeight(true);

        m_scrollPane = new JScrollPane(m_table);

        add(m_scrollPane);

		// TODO hard-coded size
        setSize(800, 300);
        pack();
        setVisible(true);
    }

    public JTable getTable()
    {
        return m_table;
    }
}
