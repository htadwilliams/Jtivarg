package org.tsoft.jtivarg.liner;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.tsoft.jtivarg.graphics.Line;

class Surface extends JPanel
{
    public Surface()
    {
        setOpaque(true);
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        Graphics2D g2d = (Graphics2D) graphics;
        super.paintComponent(graphics);

        List<Line> lineList = Application.getInstance().getLines();

        graphics.setColor(Color.DARK_GRAY);
        for (Line line: lineList)
        {
            line.draw(g2d);
        }

        int selectedLineIndex = Application.getInstance().getSelectedLineIndex();
        if (selectedLineIndex >= 0 && selectedLineIndex < lineList.size())
        {
            Line selectedLine = lineList.get(selectedLineIndex);

            graphics.setColor(Color.WHITE);
            selectedLine.draw(g2d);

            graphics.setColor(Color.GREEN);
            graphics.drawLine(
                (int) selectedLine.getxBase(), (int) selectedLine.getyBase(),
                (int) selectedLine.getxBase(), (int) selectedLine.getyBase());
            graphics.setColor(Color.RED);
            graphics.drawLine(
                (int) selectedLine.getxEnd(), (int) selectedLine.getyEnd(),
                (int) selectedLine.getxEnd(), (int) selectedLine.getyEnd());
        }

        revalidate();
    }
}
