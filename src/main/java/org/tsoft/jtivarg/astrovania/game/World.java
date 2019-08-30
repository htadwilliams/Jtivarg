/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.astrovania.game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.tsoft.jtivarg.graphics.Line;
import org.tsoft.jtivarg.math.Segment;

public class World
{
    private static final int MARGIN_WIDTH = 4;
    private static final double MARGIN_HEIGHT = 3;

    private static int COUNT_TESTLINES = 1;

    private Entity m_rootEntity = new Entity(this);
    private List<Line> m_testLines = new ArrayList<>();
    private Graphics2D m_graphics = null;

    private int m_viewportWidth;
    private int m_viewportHeight;
    private int m_viewportLeft;
    private int m_viewportTop;

    public Graphics2D getGraphics()
    {
        return m_graphics;
    }

    public void tick(long timeMS)
    {
        m_rootEntity.tick(timeMS);
    }

    public void draw(Graphics2D graphics)
    {
        // saved for debugging so that tick can access it
        m_graphics = graphics;

        m_rootEntity.draw(graphics);

        if (!m_testLines.isEmpty())
        {
            drawTestLines(graphics);
        }
    }

    private void drawTestLines(Graphics2D graphics)
    {
        for (int intersectCount = 0; intersectCount < COUNT_TESTLINES; intersectCount++)
        {
            int currentLineIndex = intersectCount * 2;

            Line line1 = m_testLines.get(currentLineIndex);
            Line line2 = m_testLines.get(currentLineIndex + 1);

            line1.draw(graphics);
            line2.draw(graphics);

            Segment.Intersection intersection = line1.intersectPoint(line2);
            graphics.drawOval(
                (int) intersection.getX() - 5, (int) intersection.getY() - 5,
                10, 10);
        }
    }

    public void addEntity(Entity entity)
    {
        m_rootEntity.addEntity(entity);
        entity.setWorld(this);
    }

    public void addTestLines()
    {
        List<Line> newList = new ArrayList<>();

        for (int intersectCount = 0; intersectCount < COUNT_TESTLINES; intersectCount++)
        {
            for (int lineCount = 0; lineCount < 2; lineCount++)
            {
                Line newLine = new Line(
                    Math.random() * 1024.0, Math.random() * 768.0,
                    Math.random() * 1024.0, Math.random() * 768.0);

                newList.add(newLine);
            }
        }

        m_testLines = newList;
    }

    public void mouseClicked(MouseEvent e)
    {
        m_rootEntity.moveTowardPoint(e.getX(), e.getY());
    }

    public void reset()
    {
        m_rootEntity.reset();
        m_testLines.clear();

        double left = m_viewportLeft + MARGIN_WIDTH;
        double top = m_viewportTop + MARGIN_HEIGHT;
        double right = m_viewportWidth - (MARGIN_WIDTH * 2);
        double bottom = m_viewportHeight - (MARGIN_HEIGHT * 2);
        double xMiddle = m_viewportWidth / 2.0;
        double yMiddle = m_viewportHeight / 2.0;

        /**
         * Hard-coded world creation objects
         *
         */

        addEntity(new Wall(left, top, right, top));
        addEntity(new Wall(right, top, right, bottom));
        addEntity(new Wall(right, bottom, left, bottom));
        addEntity(new Wall(left, bottom, left, top));

//        int countAnts = 360;
//        for (double antAngle = 0.0; antAngle < Math.PI * 2; antAngle += ((Math.PI * 2) / countAnts))
//        {
//            antAngle = Math.toRadians(45);
//            this.addEntity(new Ant(
//                xMiddle, yMiddle,           // base
//                antAngle,                   // angle
//                2));                        // speed
//        }

        this.addEntity(new Laser(
//          right - 100, bottom - 100,  // starting point
            xMiddle, yMiddle + 10,      // starting point
            Math.toRadians(45  ),       // angle
            125,                        // length
            1 * 1000));                 // speed in units/second
    }

    public void setViewport(int left, int top, int width, int height)
    {
        m_viewportLeft = left;
        m_viewportTop = top;
        m_viewportWidth = width;
        m_viewportHeight = height;
    }

    public void clear()
    {
        m_rootEntity.reset();
        m_testLines.clear();
    }

    public Entity getRootEntity()
    {
        return m_rootEntity;
    }

    public void setRootEntity(Entity rootEntity)
    {
        m_rootEntity = rootEntity;
        rootEntity.setWorld(this);
    }

    public static class CollisionInformation
    {
        private final Entity m_entity;
        private final Line m_line;

        public CollisionInformation(Entity entity, Line line)
        {
            m_entity = entity;
            m_line = line;
        }

        public Entity getEntity()
        {
            return m_entity;
        }

        public Line getLine()
        {
            return m_line;
        }
    }

    public List<CollisionInformation> getCollision(Entity requestingEntity, Line line)
    {
        List<CollisionInformation> collisionList = new ArrayList<>();
        m_rootEntity.updateCollisionList(collisionList, requestingEntity, line);
        return collisionList;
    }
}
