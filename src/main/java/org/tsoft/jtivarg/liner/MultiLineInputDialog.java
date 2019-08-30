package org.tsoft.jtivarg.liner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MultiLineInputDialog
{
    public static String show(final String message, int rows, int columns)
    {
        String data = null;
        class GetData extends JDialog implements ActionListener
        {
            JTextArea ta = new JTextArea(rows, columns);
            JButton btnOK = new JButton("   OK   ");
            JButton btnCancel = new JButton("Cancel");
            String str = null;

            public GetData()
            {
                setModal(true);
                getContentPane().setLayout(new BorderLayout());
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                // TODO get rid of hard coded location
                setLocation(400, 300);

                getContentPane().add(new JLabel(message), BorderLayout.NORTH);
                getContentPane().add(ta, BorderLayout.CENTER);

                JPanel jp = new JPanel();

                btnOK.addActionListener(this);
                btnCancel.addActionListener(this);
                jp.add(btnOK);
                jp.add(btnCancel);

                getContentPane().add(jp, BorderLayout.SOUTH);
                pack();
                setVisible(true);
            }

            public void actionPerformed(ActionEvent ae)
            {
                if (ae.getSource() == btnOK)
                {
                    str = ta.getText();
                }
                dispose();
            }

            public String getData()
            {
                return str;
            }
        }

        data = new GetData().getData();

        return data;
    }
}
