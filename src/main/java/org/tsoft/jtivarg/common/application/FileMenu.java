package org.tsoft.jtivarg.common.application;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
 *
 */
public class FileMenu extends JMenu
{
	public static final String FILE_NEW			= "New";
	public static final String FILE_OPEN		= "Open...";
	public static final String FILE_REOPEN		= "Open last";
	public static final String FILE_SAVE		= "Save";
	public static final String FILE_SAVE_AS		= "Save as...";
	public static final String FILE_EXIT		= "Exit";

    public static final MenuRow[] MENUITEMS =
    {
        new MenuRow(FILE_NEW,      KeyEvent.VK_N),
        new MenuRow(FILE_OPEN,     KeyEvent.VK_O),
        new MenuRow(FILE_REOPEN,   KeyEvent.VK_L),
        new MenuRow(FILE_SAVE,     KeyEvent.VK_N),
        new MenuRow(FILE_SAVE_AS,  KeyEvent.VK_N),
        new MenuRow(FILE_EXIT,     KeyEvent.VK_N),
    };

    public FileMenu(ActionListener actionListener)
    {
        super("File");
        setMnemonic(KeyEvent.VK_F);
        getAccessibleContext().setAccessibleDescription("Generic File Menu");

        for(MenuRow menuRow: MENUITEMS)
        {
            JMenuItem menuItem = new JMenuItem(menuRow.getText(), menuRow.getKeyEvent());
            menuItem.addActionListener(actionListener);
            add(menuItem);
        }
    }

    public static class MenuRow
    {
        private String text;
        private int keyEvent;

        public MenuRow(String text, int keyEvent)
        {
            this.text = text;
            this.keyEvent = keyEvent;
        }

        public String getText()
        {
            return text;
        }

        public int getKeyEvent()
        {
            return keyEvent;
        }
    }
}
