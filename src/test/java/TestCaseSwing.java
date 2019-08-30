import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.swing.*;

import junit.framework.TestCase;

/**
 * Test framework prototype for integration tests with GUI (javax.swing)
 *
 */
public abstract class TestCaseSwing extends TestCase
{
    private static final Logger LOGGER = Logger.getLogger(TestCaseSwing.class.getName());
    private FrameTest m_frame;
    CountDownLatch m_latch = new CountDownLatch(1);

    protected void tearDown() throws Exception
    {
        if (this.m_frame != null)
        {
            this.m_frame.dispose();
            this.m_frame = null;
        }

    }

    public FrameTest getFrame(JPanel surface)
    {
        if (this.m_frame == null)
        {
            this.m_frame = new FrameTest(surface, "Test");
        }

        return this.m_frame;
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        TestCaseRunnable testRunnable = new TestCaseRunnable();
        javax.swing.SwingUtilities.invokeLater(testRunnable);

        // wait until we're invoked by the runnable
        m_latch.await();
    }

    private class TestCaseRunnable implements Runnable
    {
        @Override
        public void run()
        {
            m_latch.countDown();
        }
    }
}
