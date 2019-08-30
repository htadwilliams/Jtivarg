import java.awt.*;

import javax.swing.*;

import org.tsoft.jtivarg.astrovania.game.World;

public class SurfaceTest extends JPanel
{
    private final World m_world;

    public SurfaceTest(World world)
    {
        m_world = world;
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        Graphics2D g2d = (Graphics2D) graphics;

        // TODO do rendering hints need to be set with each paint or can they be set once?
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        m_world.draw(g2d);
    }
}
