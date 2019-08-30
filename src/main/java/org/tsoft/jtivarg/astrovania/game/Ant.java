/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.astrovania.game;

import java.awt.*;
import java.util.List;

import org.tsoft.jtivarg.graphics.Line;
import org.tsoft.jtivarg.math.Vector;

public class Ant extends Entity
{
    private static final int MAX_BOUNCE = 12;

    public Ant(
        double x,
        double y,
        double angle,
        double speed)
    {
        super();
        setLineProperties();

        Line line = new Line();
        line.setPolar(x, y, angle, speed);

        addLine(line);
    }

    public Ant()
    {
        super();

        setLineProperties();
    }

    public void setLineProperties()
    {
        setPaint(Color.GREEN);
    }


    @Override
    public void tick(long timeMS)
    {
        super.tick(timeMS);

        Line line = m_lines.get(0);
        Line newLine = new Line();
        double newAngle = line.getAngle();

        for (int bounceCount = 0; bounceCount < MAX_BOUNCE; ++bounceCount)
        {
            newLine.setPolar(line.getxEnd(), line.getyEnd(), newAngle, line.getDistance());

            List<World.CollisionInformation> collisionList = m_world.getCollision(this, newLine);
            if (!collisionList.isEmpty())
            {
                // All we care about is the first collided line
                Line lineCollided = collisionList.get(0).getLine();
                double angleCollided = lineCollided.getAngle();

                // Calculate reflection angle
                newAngle = Vector.clampAngle(angleCollided * 2 - newLine.getAngle());
            }
            else
            {
                m_lines.set(0, newLine);
                break;
            }
        }
    }

    @Override
    public void moveTowardPoint(double x, double y)
    {
        Line line = m_lines.get(0);
        Vector constructionLine = new Vector();

        // draw line from our base to point to calculate movement angle
        constructionLine.setCartesian(line.getxBase(), line.getyBase(), x, y);

        line.setAngle(constructionLine.getAngle());
    }
}
