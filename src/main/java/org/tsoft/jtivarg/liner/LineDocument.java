package org.tsoft.jtivarg.liner;

import java.util.ArrayList;
import java.util.List;

import org.tsoft.jtivarg.graphics.Line;

public class LineDocument
{
    private List<Line> m_lines = new ArrayList<>();

    public List<Line> getLines()
    {
        return m_lines;
    }

    public void setLines(List<Line> lines)
    {
        m_lines = lines;
    }
}
