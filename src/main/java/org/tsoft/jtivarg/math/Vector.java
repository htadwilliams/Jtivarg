/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.math;

/**
 * The math and layout of this class go back to very old C and then C++ projects...
 * <p>
 * The C version was just a collection of functions that could operate on a vector struct
 */
public class Vector extends Segment
{
    public static final double CIRCLE_RADIANS = Math.PI * 2;
    private double m_distance;
    private double m_angle;

    public Vector(double xBase, double yBase, double xEnd, double yEnd)
    {
        setCartesian(xBase, yBase, xEnd, yEnd);
    }

    public Vector(Vector copyVector)
    {
        setCartesian(copyVector.m_xBase, copyVector.m_yBase, copyVector.m_xEnd, copyVector.m_yEnd);
    }

    public Vector()
    {
        super();

        m_angle = 0.0;
        m_distance = 0.0;
    }

    public double getDistance()
    {
        return m_distance;
    }

    public double getAngle()
    {
        return m_angle;
    }

    public static double clampAngle(double angle)
    {
        if (angle > CIRCLE_RADIANS || angle < -CIRCLE_RADIANS)
        {
            return angle % CIRCLE_RADIANS;
        }

        return angle;
    }

    public void setCartesian(double xBase, double yBase, double xEnd, double yEnd)
    {
        set(xBase, yBase, xEnd, yEnd);
        m_distance = distance(xBase, yBase, xEnd, yEnd);

        if (m_distance != 0.0)
        {
            m_angle = Math.atan2(yEnd - yBase, xEnd - xBase);
            m_angle = clampAngle(m_angle);
        }
        else
        {
            m_angle = 0.0;
        }
    }

    public void setPolar(double xBase, double yBase, double angle, double distance)
    {
        m_xBase = xBase;
        m_yBase = yBase;

        m_angle = angle;
        m_distance = distance;

        m_xEnd = m_xBase + Math.cos(angle) * distance;
        m_yEnd = m_yBase + Math.sin(angle) * distance;

        m_angle = clampAngle(m_angle);
    }

    public void setAngle(double angle)
    {
        setPolar(m_xBase, m_yBase, angle, m_distance);
    }

    public void accumulate(Vector vectorAdd)
    {
        m_xEnd = m_xEnd + (vectorAdd.m_xEnd - vectorAdd.m_xBase);
        m_yEnd = m_yEnd + (vectorAdd.m_yEnd - vectorAdd.m_yBase);

        setCartesian(
            m_xBase,
            m_yBase,
            (m_xEnd + (vectorAdd.m_xEnd - vectorAdd.m_xBase)),
            (m_yEnd + (vectorAdd.m_yEnd - vectorAdd.m_yBase)));
    }

    public void flip()
    {
        m_angle += Math.PI;
        m_angle = clampAngle(m_angle);

        double xTemp = m_xBase;
        double yTemp = m_yBase;

        m_xBase = m_xEnd;
        m_yBase = m_yEnd;

        m_xEnd = xTemp;
        m_yEnd = yTemp;
    }

    public void setMagnitude(Double newLength)
    {
        setPolar(m_xBase, m_yBase, m_angle, newLength);
    }

    public void setDistance(Double distance)
    {
        m_distance = distance;
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(
            " (%,.2f, %,.2f), (%,.2f, %,.2f) angle=%,.2f distance=%,.2f",
            m_xBase, m_yBase,
            m_xEnd, m_yEnd,
            m_angle, m_distance);
    }
}