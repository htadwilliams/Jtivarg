/**
 * Copyright 2016 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.astrovania.game;

import java.awt.*;

import org.tsoft.jtivarg.graphics.Line;

public class Wall extends Entity
{

    public Wall(
        double xBase,
        double yBase,
        double xEnd,
        double yEnd)
    {
        super();

        setLineProperties();
        addLine(new Line(xBase, yBase, xEnd, yEnd));
    }

    public Wall()
    {
        setLineProperties();
    }

    public void setLineProperties()
    {
        setPaint(Color.YELLOW);
        setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
    }

    @Override
    public boolean shouldCheckCollisions()
    {
        // everything collides with a wall
        return true;
    }
}
