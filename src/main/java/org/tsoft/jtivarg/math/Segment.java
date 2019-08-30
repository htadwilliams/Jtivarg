/**
 * Copyright 2016 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 *
 */

package org.tsoft.jtivarg.math;

import java.awt.*;

public class Segment
{
	protected double m_xBase;
	protected double m_yBase;
	protected double m_xEnd;
	protected double m_yEnd;

	public Segment(double xBase, double yBase, double xEnd, double yEnd)
	{
		set(xBase, yBase, xEnd, yEnd);
	}

	public Segment()
	{
		set(0.0, 0.0, 0.0, 0.0);
	}

	public double getxBase()
	{
		return m_xBase;
	}

	public double getyBase()
	{
		return m_yBase;
	}

	public double getxEnd()
	{
		return m_xEnd;
	}

	public double getyEnd()
	{
		return m_yEnd;
	}

	public void setxBase(double xBase)
	{
		m_xBase = xBase;
	}

	public void setyBase(double yBase)
	{
		m_yBase = yBase;
	}

	public void setxEnd(double xEnd)
	{
		m_xEnd = xEnd;
	}

	public void setyEnd(double yEnd)
	{
		m_yEnd = yEnd;
	}

	public void set(double xBase, double yBase, double xEnd, double yEnd)
	{
		m_xBase = xBase;
		m_yBase = yBase;
		m_xEnd = xEnd;
		m_yEnd = yEnd;
	}

	public static double distance(double x1, double y1, double x2, double y2)
	{
		double distance = 0.0;
		double check = 0.0;
		// double fTemp1 = 0.0, fTemp2 = 0.0;

		// Calculate distance using distance formula in two steps --
		// check for sqrt -1...

		// replace pow call with 2 multiplications for speed

		// fTemp1 = pow(fy2 - fy1, 2);
		// fTemp2 = pow(fx2 - fx1, 2);
		// fCheck = fTemp1 + fTemp2;
		double xFactor = x2 - x1;
		double yFactor = y2 - y1;

		check = xFactor * xFactor + yFactor * yFactor;

		if (check >= 0)
		{
			// Perform second step of distance formula.
			distance = Math.sqrt(check);
		}

		return distance;
	}

	public static int counterClockwise(double x0, double y0, double x1, double y1, double x2, double y2)
	{
		double dx1, dy1, dx2, dy2;

		// calculate deltas
		dx1 = x1 - x0;
		dy1 = y1 - y0;
		dx2 = x2 - x0;
		dy2 = y2 - y0;

		// compare deltas (slopes) and decide...
		if (dx1 * dy2 > dy1 * dx2)
		{
			return 1;
		}
		if (dx1 * dy2 < dy1 * dx2)
		{
			return -1;
		}
		if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0))
		{
			return -1;
		}
		if ((dx1 * dx1 + dy1 * dy1) < (dx2 * dx2 + dy2 * dy2))
		{
			return -1;
		}

		return 0;
	}

	public static boolean checkIntersect(
		double x1, double y1,
		double x2, double y2,
		double x3, double y3,
		double x4, double y4)
	{
		// given the endpoints of two line segments, based on this truth table
		// we can quickly determine if an intersection point exists or not
		return ((counterClockwise(x1, y1, x2, y2, x3, y3)
				 * counterClockwise(x1, y1, x2, y2, x4, y4)) <= 0)
			   && ((counterClockwise(x3, y3, x4, y4, x1, y1)
					* counterClockwise(x3, y3, x4, y4, x2, y2)) <= 0);
	}

	public enum Slope
	{
		HORIZONTAL,
		VERTICAL,
		NORMAL
	}

	public static class SlopeContext
	{
		private final Slope m_slope;
		private final double m_result;

		public SlopeContext(Slope slope, double result)
		{
			m_slope = slope;
			m_result = result;
		}

		public Slope getSlope()
		{
			return m_slope;
		}

		public double getResult()
		{
			return m_result;
		}
	}

	public static SlopeContext slope(double fx1, double fy1, double fx2, double fy2)
	{
		Slope slopeReturn = Slope.NORMAL;
		double result = 0.0;

		// check for horizontal line
		if (0 == (fy2 - fy1))
		{
			return new SlopeContext(Slope.HORIZONTAL, 0.0);
		}

		// vertical line (and divide by zero avoidance!)
		if (0 == (fx2 - fx1))
		{
			return new SlopeContext(Slope.VERTICAL, 0.0);
		}

		return new SlopeContext(Slope.NORMAL, (fy2 - fy1) / (fx2 - fx1));
	}

	public static class Intersection
	{
		private final double m_x;
		private final double m_y;

		public Intersection(double x, double y)
		{
			m_x = x;
			m_y = y;
		}

		public double getX()
		{
			return m_x;
		}

		public double getY()
		{
			return m_y;
		}
	}

public static Intersection getLineLineIntersection(
	double x1, double y1, double x2, double y2,
	double x3, double y3, double x4, double y4)
{
      double det1And2 = det(x1, y1, x2, y2);
      double det3And4 = det(x3, y3, x4, y4);
      double x1LessX2 = x1 - x2;
      double y1LessY2 = y1 - y2;
      double x3LessX4 = x3 - x4;
      double y3LessY4 = y3 - y4;

      double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);

      if (det1Less2And3Less4 == 0)
	  {
         // the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
         return null;
      }
      double x = (det(det1And2, x1LessX2, det3And4, x3LessX4) / det1Less2And3Less4);
      double y = (det(det1And2, y1LessY2, det3And4, y3LessY4) / det1Less2And3Less4);

      return new Intersection(x, y);
   }

   private static double det(double a, double b, double c, double d)
   {
      return a * d - b * c;
   }

	public static Intersection intersectPoint(
		double x1, double y1,
		double x2, double y2,
		double x3, double y3,
		double x4, double y4)
	{
		boolean bIntersect = false;
		double c1 = 0.0;
		double c2 = 0.0;

		//
		// Use general intersection formula
		//
		// Given two linear equations in the form(s)
		//
		//       a1x + b1y = c1
		// and   a2x + b2y = c2
		//
		// Intersection point can be calculated by the following
		// general formula for x and y.
		//
		//     b2 c1 - b1 c2           a1 c2 - a2 c1
		// x = -------------       y = -------------
		//     a1 b2 - a2 b1           a1 b2 - a2 b1
		//
		// Start by stating each vector as a linear equation
		// in the form ax + by = c.  In y-intercept terms
		// this would be mx + by = c.  When starting with y-intercept
		// form and moving to general linear form, b always
		// drops out to a value of 1, which we can drop out of our
		// general solution equation
		//
		// This gives us a simpler form to solve; mx + y = c.
		//
		//     c1 - c2        a1 c2 - a2 c1
		// x = -------    y = -------------
		//     a1 - a2           a1 - a2
		//

		// calculate slope (a) for both line segments
		SlopeContext a1Context = slope(x1, y1, x2, y2);
		SlopeContext a2Context = slope(x3, y3, x4, y4);

		// TODO handle vertical lines
		if (a1Context.getSlope() == Slope.VERTICAL || a2Context.getSlope() == Slope.VERTICAL)
		{
			return null;
		}

		double a1 = a1Context.getResult();
		double a2 = a2Context.getResult();

		//
		// use any x,y pair from each vector with
		// slope to calculate y-intercept (c)
		//

		// b = y-mx... or b = y-ax
		c1 = y2 - a1 * x2;
		c2 = y4 - a2 * x4;

		// if (a1 - a2) evaluate to 0, then lines don't intersect
		// (we'd get a divide by 0 error)
		if (0 != (a1 - a2))
		{
			// intersect point
			return new Intersection(
				-(c1 - c2) / (a1 - a2),
				(a1 * c2 - a2 * c1) / (a1 - a2)
			);
		}

		return null;
	}

	public Intersection intersectPoint(double x1, double y1, double x2, double y2)
	{
		return getLineLineIntersection(m_xBase, m_yBase, m_xEnd, m_yEnd, x1, y1, x2, y2);
	}

	public Intersection intersectPoint(Segment segment)
	{
		return getLineLineIntersection(
			m_xBase, m_yBase, m_xEnd, m_yEnd,
			segment.getxBase(), segment.getyBase(), segment.getxEnd(), segment.getyEnd());
	}

	public boolean compareTo(double xBase, double yBase, double xEnd, double yEnd)
	{
		return (m_xBase == xBase && m_yBase == yBase && m_xEnd == xEnd && m_yEnd == yEnd);
	}

	public boolean compareTo(Segment segment)
	{
		return (
			null != segment
			&& compareTo(segment.getxBase(), segment.getyBase(), segment.getxEnd(), segment.getyEnd()));
	}
}
