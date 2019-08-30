import java.awt.*;

import org.junit.Test;
import org.tsoft.jtivarg.astrovania.Application;
import org.tsoft.jtivarg.astrovania.game.World;

/**
 * Use TestCaseSwing to accomplish GUI integration tests
 *
 */
public class WorldIntegrationTests extends TestCaseSwing
{
    @Test
    public void testWorldWithSurface() throws InterruptedException
    {
        // Fixes a lot of repaint bugs when redrawing menus or dragging windows over each other.
        System.setProperty("swing.bufferPerWindow", "false");

        World world = new World();
        SurfaceTest surface = new SurfaceTest(world);
        FrameTest frameTest = getFrame(surface);
        frameTest.initialize();

        world.reset();
        world.tick(100);
        world.tick(100);

        System.out.println("");
    }
}
