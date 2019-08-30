/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.astrovania.game;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.tsoft.jtivarg.graphics.Line;
import org.tsoft.jtivarg.math.Vector;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
public class Entity implements Serializable
{
    protected Paint m_paint = Color.white;
    protected Stroke m_stroke = new BasicStroke(1);

    protected World m_world;

    protected List<Entity> m_children = new ArrayList<>();
    protected List<Line> m_lines = new ArrayList<>();

    public Entity()
    {
        m_world = null;
    }

    public Entity(World world)
    {
        setWorld(world);
    }

    public void setWorld(World world)
    {
        m_world = world;
        synchronized (m_children)
        {
            for (Entity child : m_children)
            {
                child.setWorld(world);
            }
        }
    }

    public void setChildren(List<Entity> children)
    {
        m_children = children;
    }

    public void setPaint(Paint paint)
    {
        m_paint = paint;
    }

    public void setStroke(Stroke stroke)
    {
        m_stroke = stroke;
    }

    public void tick(long timeMS)
    {
        synchronized (m_children)
        {
            for (Entity child : m_children)
            {
                child.tick(timeMS);
            }
        }
    }

    public void moveTowardPoint(double x, double y)
    {
        synchronized (m_children)
        {
            for (Entity child : m_children)
            {
                child.moveTowardPoint(x, y);
            }
        }
    }

    public void draw(Graphics2D graphics)
    {
        synchronized (m_children)
        {
            // Draw children
            for (Entity child : m_children)
            {
                child.draw(graphics);
            }

            prepareGraphics(graphics);

            // Draw self
            for (Line line : m_lines)
            {
                line.draw(graphics);
            }
        }
    }

    private void prepareGraphics(Graphics2D graphics)
    {
        if (null != m_paint)
        {
            graphics.setPaint(m_paint);
        }

        if (null != m_stroke)
        {
            graphics.setStroke(m_stroke);
        }
    }

    public void addEntity(Entity entity)
    {
        synchronized (m_children)
        {
            m_children.add(entity);
        }
    }

    public List<Line> getLines()
    {
        // For easier de-serialization
//        return Collections.unmodifiableList(m_lines);
        return m_lines;
    }

    public void setLines(List<Line> lines)
    {
        m_lines = lines;
    }

    public List<Entity> getChildren()
    {
        // For easier de-serialization
//        return Collections.unmodifiableList(m_children);
        return m_children;
    }

    public void addLine(Line line)
    {
        m_lines.add(line);
    }

    public void updateCollisionList(List<World.CollisionInformation> collisionList, Entity requestingEntity, Line line)
    {
        // Don't check against ourselves
        if (this == requestingEntity)
        {
            return;
        }

        for (Line checkLine : m_lines)
        {
            if (Vector.checkIntersect(
                line.getxBase(), line.getyBase(), line.getxEnd(), line.getyEnd(),
                checkLine.getxBase(), checkLine.getyBase(), checkLine.getxEnd(), checkLine.getyEnd()))
            {
                collisionList.add(new World.CollisionInformation(this, checkLine));
            }
        }

        for (Entity child : m_children)
        {
            if (child.shouldCheckCollisions())
            {
                child.updateCollisionList(collisionList, requestingEntity, line);
            }
        }
    }

    public boolean shouldCheckCollisions()
    {
        return false;
    }

    public void reset()
    {
        synchronized (m_children)
        {
            m_children.clear();
        }
    }
}
