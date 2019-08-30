/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.graphics;

import org.tsoft.jtivarg.math.Segment;
import org.tsoft.jtivarg.math.Vector;

public class LineUtils
{
    public interface ILineVisitor
    {
        /**
         * Allow caller to filter the lines that are collided while bouncing occurs.
         * Caller should track last line collided for example, as well as any other
         * lines that should be ignored.  Be careful of object identity rules.
         *
         * @param lineCollided
         * @return
         */
        boolean shouldCollide(Line lineCollided);

        /**
         * Called for each new line "bounced" until distance is exhausted
         *
         * @param line
         */
        void newLine(Line line, boolean didCollide);

        /**
         * Request first Line out of set of target lines that would be collided
         * by the given Line.
         *
         * @param line
         * @return Line collided by input Line
         */
        Line getLineCollided(Line line);
    }

    /**
     * Calculates set of lines when bouncing from a starting position and angle over the given distance.
     * When newly calculated lines collide with lines supplied by the caller, the angle of reflection
     * is calculated and a new line is started.
     *
     * @param xStart
     * @param yStart
     * @param angleStart
     * @param distance
     * @param lineVisitor Used to customize behavior and capture output of new lines.
     */
    public static void bounce(
        final double xStart,
        final double yStart,
        final double angleStart,
        final double distance,
        final ILineVisitor lineVisitor)
    {
        double remainDistance = distance;
        double x = xStart;
        double y = yStart;
        double angle = angleStart;

        while (remainDistance > 0)
        {
            boolean didCollide = false;
            Line newLine = new Line();
            newLine.setPolar(x, y, angle, remainDistance);

            Line lineCollided = lineVisitor.getLineCollided(newLine);

            if (null != lineCollided && lineVisitor.shouldCollide(lineCollided))
            {
                didCollide = true;

                // Calculate collision point
                Segment.Intersection intersection = lineCollided.intersectPoint(newLine);
                assert (null != intersection);

                // Cut line off at intersection point and add it, starting next line at end of this one and at
                // reflection angle
                newLine.setCartesian(newLine.getxBase(), newLine.getyBase(), intersection.getX(), intersection.getY());

                // calculate reflection angle, new x and y
                x = newLine.getxEnd();
                y = newLine.getyEnd();

                angle = Vector.clampAngle((lineCollided.getAngle() * 2) - newLine.getAngle());
            }

            lineVisitor.newLine(newLine, didCollide);
            remainDistance -= newLine.getDistance();
        }
    }
}
