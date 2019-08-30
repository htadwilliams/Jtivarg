/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.graphics;

import java.awt.*;

import org.tsoft.jtivarg.math.Vector;

public class Line extends Vector
{
	public Line()
	{
		super();
	}

	public Line(double xBase, double yBase, double xEnd, double yEnd)
	{
		super(xBase, yBase, xEnd, yEnd);
	}

	public Line(Line copyLine)
	{
		super(copyLine);
	}

	public void draw(Graphics2D graphics)
	{
		graphics.drawLine((int) getxBase(), (int) getyBase(), (int) getxEnd(), (int) getyEnd());
	}
}
