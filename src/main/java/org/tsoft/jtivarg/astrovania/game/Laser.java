/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.astrovania.game;

import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.tsoft.jtivarg.graphics.Line;
import org.tsoft.jtivarg.graphics.LineUtils;
import org.tsoft.jtivarg.math.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Laser extends Entity
{
    private double m_length;        // laser at full length
    private double m_speed;         // pixels / second
    private double m_x;
    private double m_y;

    private double m_angle;         // starting / current angle
    private double m_lengthCurrent; // current length (young lasers are short)

    private Line m_lastLineCollided = null;
    private final HeadLineVisitor m_headLineVisitor = new HeadLineVisitor(this);

    private static final Logger LOGGER = Logger.getLogger(Laser.class.getName());

    @JsonCreator
    public Laser(
        @JsonProperty("x") double x,
        @JsonProperty("y") double y,
        @JsonProperty("angle") double angle,
        @JsonProperty("length") double length,
        @JsonProperty("speed") double speed)
    {
        super();

        m_length = length;
        m_speed = speed;
        m_angle = angle;
        m_x = x;
        m_y = y;
    }

    public void setLength(double length)
    {
        m_length = length;
    }

    public double getSpeed()
    {
        return m_speed;
    }

    public void setSpeed(double speed)
    {
        m_speed = speed;
    }

    public double getX()
    {
        return m_x;
    }

    public void setX(double x)
    {
        m_x = x;
    }

    public double getY()
    {
        return m_y;
    }

    public void setY(double y)
    {
        m_y = y;
    }

    public double getAngle()
    {
        return m_angle;
    }

    public void setAngle(double angle)
    {
        m_angle = angle;
    }

    //    @Override
//    public void tick(long timeMS)
//    {
//        super.tick(timeMS);
//
//        double moveDistance = timeMS * (m_speed / 1000);
//        double baseMoveDistance = 0.0;
//        double lengthNew = m_lengthCurrent;
//
//        // this tick moves base and end - already at full length
//        if (m_lengthCurrent == m_length)
//        {
//            baseMoveDistance = moveDistance;
//            lengthNew = m_lengthCurrent;
//        }
//        // this tick moves base less than end - will make laser more than full length if the base doesn't move
//        else if (m_lengthCurrent + moveDistance > m_length)
//        {
//            baseMoveDistance = m_lengthCurrent + moveDistance - m_length;
//            lengthNew = m_length;
//        }
//        else
//        // this tick will lengthen laser by tick distance
//        {
//            lengthNew = moveDistance;
//        }
//
//        double tickDistanceTraveled = 0.0;
//        double lengthIncreased = 0.0;
//
//        List<Line> m_newLines = new ArrayList<>();
//        boolean drawingNewLines = false;
//        Line newLine = null;
//        Line lastOldLine = null;
//
//        for (Line oldLine : m_lines)
//        {
//            if (!drawingNewLines)
//            {
//                // If true, need to start new line somewhere along oldLine
//                if (baseMoveDistance > tickDistanceTraveled
//                    && baseMoveDistance <= (tickDistanceTraveled + oldLine.getDistance()))
//                {
//                    drawingNewLines = true;
//
//                    newLine = new Line();
//
//                    // This line segment will overlap baseMoveDistance along last old line, and loop iterations
//                    // after this should copy the rest of the old lines
//                    newLine.setPolar(
//                        oldLine.getxBase(), oldLine.getyBase(),
//                        oldLine.getAngle(),
//                        baseMoveDistance);
//                    newLine.setCartesian(newLine.getxEnd(), newLine.getyEnd(), oldLine.getxEnd(), oldLine.getyEnd());
//
//                    m_newLines.add(newLine);
//                    LOGGER.info(newLine.toString());
//
//                    tickDistanceTraveled += newLine.getDistance();
//                    lengthIncreased += newLine.getDistance();
//                }
//                else
//                {
//                    // keep old line and record distance
//                    m_newLines.add(oldLine);
//                    tickDistanceTraveled += oldLine.getDistance();
//                    lengthIncreased += oldLine.getDistance();
//                }
//            }
//            else
//            {
//                // discarding old line but still recording distance
//                tickDistanceTraveled += oldLine.getDistance();
//                lengthIncreased += oldLine.getDistance();
//            }
//            lastOldLine = oldLine;
//        }
//
//        double newXBase = m_x;
//        double newYBase = m_y;
//        double newAngle = m_angle;
//
//        if (null != lastOldLine)
//        {
//            newXBase = lastOldLine.getxEnd();
//            newYBase = lastOldLine.getyEnd();
//            newAngle = lastOldLine.getAngle();
//        }
//
//        double lengthLeft = lengthNew - lengthIncreased;
//        while (lengthLeft > 0)
//        {
//            if (null != newLine)
//            {
//                newXBase = newLine.getxEnd();
//                newYBase = newLine.getyEnd();
//            }
//
//            newLine = new Line();
//            newLine.setPolar(newXBase, newYBase, newAngle, lengthLeft);
//
//            Line lineCollided = getLineCollided(newLine);
//
//            if (null != lineCollided && !lineCollided.compareTo(m_lastLineCollided))
//            {
//                // Calculate collision point
//                Segment.Intersection intersection = lineCollided.intersectPoint(newLine);
//                assert (null != intersection);
//
//                // Cut line off at intersection point and add it, starting next line at end of this one and at
//                // reflection angle
//                newLine.setCartesian(newLine.getxBase(), newLine.getyBase(), intersection.getX(), intersection.getY());
//                m_newLines.add(newLine);
//
//                // Calculate reflection angle
//                newAngle = lineCollided.getAngle() * 2 - newLine.getAngle();
//
//                // Prevent double collision which *WILL* happen if we begin and end our line segments
//                // *ON* the lines we collide with!
//                m_lastLineCollided = lineCollided;
//
//                newXBase = intersection.getX();
//                newYBase = intersection.getY();
//
//                lengthLeft -= newLine.getDistance();
//            }
//            else // no collision means we're done - we can just add our new line and length is all used up!
//            {
//                m_newLines.add(newLine);
//                lengthLeft = 0.0;
//            }
//        }
//
//        m_lengthCurrent = lengthNew;
//        m_lines = m_newLines;
//    }
    @Override
    public void tick(long timeMS)
    {
        super.tick(timeMS);

        double headMoveDistance = timeMS * (m_speed / 1000);
        double baseMoveDistance = 0.0;
        double lengthNew = m_lengthCurrent;

        // already at full length
        if (m_lengthCurrent == m_length)
        {
            baseMoveDistance = headMoveDistance;
//            lengthNew = m_lengthCurrent;  // implied
        }
        // this tick moves base less than end - will make laser more than full length if the base doesn't move
        else if (m_lengthCurrent + headMoveDistance > m_length)
        {
            baseMoveDistance = m_lengthCurrent + headMoveDistance - m_length;
            lengthNew = m_length;
        }
        else
        // only head moves
        {
            lengthNew = headMoveDistance;
        }

        updateHead(headMoveDistance);
        updateBase();

        m_lengthCurrent = lengthNew;

        Line lastLine = getLastLine();
        if (null != lastLine)
        {
            m_x = lastLine.getxEnd();
            m_y = lastLine.getyEnd();
            m_angle = lastLine.getAngle();
        }
    }

    @Override
    public void moveTowardPoint(double x, double y)
    {
        super.moveTowardPoint(x, y);

        // Could use new Vector but rather avoid creating new object
        double distance = Vector.distance(m_x, m_y, x, y);
        if (distance != 0.0)
        {
            m_angle = Math.atan2(y - m_y, x - m_x);
            m_angle = Vector.clampAngle(m_angle);
        }
        else
        {
            m_angle = 0.0;
        }
    }

    private void updateBase()
    {
        double length = 0.0f;
        boolean removeRemaining = false;
        ListIterator<Line> it = m_lines.listIterator(m_lines.size());

        while (it.hasPrevious())
        {
            Line line = it.previous();

            if (removeRemaining)
            {
                it.remove();
            }
            else
            {
                double lineLength = line.getDistance();

                if (length + lineLength > m_length)
                {
                    line.flip();
                    line.setDistance((length + lineLength) - m_length);
                    line.flip();
                    removeRemaining = true;
                }

                length += line.getDistance();
            }
        }
    }

    private double getLength()
    {
        double length = 0.0f;

        for (Line line: m_lines)
        {
            length += line.getDistance();
        }

        return length;
    }

    private void updateHead(final double headMoveDistance)
    {
        LineUtils.bounce(m_x, m_y, m_angle, headMoveDistance, m_headLineVisitor);
        m_lastLineCollided = null;
    }

    private void removeLastLine()
    {
        if (!m_lines.isEmpty())
        {
            m_lines.remove(m_lines.size() - 1);
        }
    }

    private Line getLastLine()
    {
        if (m_lines.isEmpty())
        {
            return null;
        }

        return m_lines.get(m_lines.size() - 1);
    }

    private World.CollisionInformation getFirstCollision(List<World.CollisionInformation> collisionList)
    {
        if (null != collisionList && !collisionList.isEmpty())
        {
            return collisionList.get(0);
        }

        return null;
    }

    @Override
    public boolean shouldCheckCollisions()
    {
        return true;
    }

    /**
     * HEAD LINE VISITOR
     *
     */
    private class HeadLineVisitor implements LineUtils.ILineVisitor
    {
        private final Entity m_owningEntity;

        private HeadLineVisitor(Entity owningEntity)
        {
            m_owningEntity = owningEntity;
            reset();
        }

        @Override
        public boolean shouldCollide(Line lineCollided)
        {
            boolean shouldCollide = false;

            if (!lineCollided.compareTo(m_lastLineCollided))
            {
                shouldCollide = true;
                m_lastLineCollided = lineCollided;
            }
            else
            {
                m_lastLineCollided = null;
            }

            return shouldCollide;
        }

        @Override
        public void newLine(Line line, boolean didCollide)
        {
            if (didCollide)
            {
                m_lines.add(line);
            }
            else
            {
                if (m_lines.isEmpty())
                {
                    m_lines.add(line);
                }
                else
                {
                    Line lastLine = getLastLine();

                    // If angle has changed for some other reason, just add new line to end
                    if (m_angle != lastLine.getAngle())
                    {
                        m_lines.add(line);
                    }
                    // Modify existing line by growing it to end of new line
                    else
                    {
                        lastLine.setCartesian(
                            lastLine.getxBase(), lastLine.getyBase(),
                            line.getxEnd(), line.getyEnd());
                    }
                }
            }
        }

        @Override
        public Line getLineCollided(Line newLine)
        {
            Line lineCollided = null;
            List<World.CollisionInformation> collisionList = m_world.getCollision(m_owningEntity, newLine);

            // TODO getting first collision may not be suitable for multi-line objects: test it
            World.CollisionInformation collision = getFirstCollision(collisionList);
            if (null != collision)
            {
                lineCollided = collision.getLine();
            }

            return lineCollided;
        }
    }
}
