import javax.swing.*;

public class FrameTest extends JFrame
{
	public FrameTest(JPanel surface, String testName)
	{
		super(testName);

		add(surface);
	}

    public void initialize()
    {
        pack();
        setVisible(true);
    }
}
