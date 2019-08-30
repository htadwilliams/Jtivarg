/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.yticlock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;

import org.tsoft.jtivarg.graphics.Line;

/**
 *
 */
class SurfaceClock extends JPanel implements ActionListener
{
	public static final double PI_RAD = Math.PI * 2;

	private BufferedImage m_buffer;

	private static final int DELAY = 0;
	private static final long REPORT_FRAMES = 100;
	private Timer timer;

	private int m_width = 0;
	private int m_height = 0;

	private long m_lastTimeMS = System.currentTimeMillis();

	private long m_countFrames = 0;
	private long m_fps = 0;
	private long m_reportFrameMS;
	private String m_message = "FPS not yet calculated";

	public SurfaceClock()
	{
		initTimer();
	}

	private void initTimer()
	{
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public Timer getTimer()
	{
		return timer;
	}

	private BufferedImage createBufferedImage(int width, int height)
	{
		GraphicsEnvironment		graphicsEnvironment		= GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice			graphicsDevice			= graphicsEnvironment.getDefaultScreenDevice();
		GraphicsConfiguration	graphicsConfiguration	= graphicsDevice.getDefaultConfiguration();

		return graphicsConfiguration.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
    }

	private void paintClock(Graphics graphics)
	{
		calcFPS();

		Graphics2D g2d = (Graphics2D) graphics;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth();
		int h = getHeight();

		double x = (w / 2.0);
		double y = (h / 2.0);

		int sizeAvail = (h > w) ? w : h;

		double lenFace		= (sizeAvail / 2.0) * (9.0 / 10.0);

		g2d.setStroke(new BasicStroke(sizeAvail / 120, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		g2d.setPaint(Color.BLACK);
		g2d.drawOval(
			(int) (x - lenFace),
			(int) (y - lenFace),
			(int) (lenFace * 2),
			(int) (lenFace * 2));

		drawTicks(g2d, (int) x, (int) y, lenFace, sizeAvail);

		drawHands(g2d, (int) x, (int) y, sizeAvail);

		drawFPS(g2d);
	}

	private int m_drawX = 0;

	private void drawFPS(Graphics2D g2d)
	{
		m_drawX++;
		if (m_drawX > getWidth())
		{
			m_drawX = 0;
		}

//		g2d.drawLine(m_drawX, 0, m_drawX, getHeight());

		g2d.drawString(m_message, 0, getHeight());
	}

	private void calcFPS()
	{
		long timeNowMS = System.currentTimeMillis();

//		try
//		{
//			Thread.sleep(5);
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}

		if (++m_countFrames % REPORT_FRAMES == 0)
		{
			m_reportFrameMS = timeNowMS - m_lastTimeMS;

			if (m_reportFrameMS != 0)
			{
				m_fps = 1000 / m_reportFrameMS;
			}
			else
			{
				m_fps = -1;
			}


			m_message = String.format("%,5d frames, %,5dMS = %,d FPS", m_countFrames, m_reportFrameMS, m_fps);
			System.out.println(m_message);
		}

		m_lastTimeMS = timeNowMS;
	}

	private void drawTicks(Graphics2D g2d, int x, int y, double lenFace, int sizeAvail)
	{
		Line vector = new Line();

		g2d.setStroke(new BasicStroke(sizeAvail / 120, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		for (double faceAngle = 0.0; faceAngle < PI_RAD; faceAngle += PI_RAD / 60.0)
		{
			vector.setPolar(x, y, faceAngle, lenFace);
			vector.flip();
			vector.setMagnitude(lenFace / 50.0);
			vector.draw(g2d);
		}

		g2d.setStroke(new BasicStroke(sizeAvail / 80, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		for (double faceAngle = 0.0; faceAngle < PI_RAD; faceAngle += PI_RAD / 12.0)
		{
			vector.setPolar(x, y, faceAngle, lenFace);
			vector.flip();
			vector.setMagnitude(lenFace / 20.0);
			vector.draw(g2d);
		}
	}

	private void drawHands(Graphics2D graphics, int x, int y, int sizeAvail)
	{
		double lenHour		= (sizeAvail / 2.0) * (1.0 / 2.0);
		double lenSecond	= (sizeAvail / 2.0) * (7.0 / 8.0);
		double lenFract		= (sizeAvail / 2.0) * (5.0 / 8.0);
		double lenMinute	= (sizeAvail / 2.0) * (3.0 / 4.0);

		Date date = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);

		double angleFract = ((double) calendar.get(Calendar.MILLISECOND) / 1000) * PI_RAD;
		angleFract -= PI_RAD / 4;

		double angleSecond = ((double) calendar.get(Calendar.SECOND) / 60) * PI_RAD;
		angleSecond -= PI_RAD / 4;

		double angleMinute = ((double) calendar.get(Calendar.MINUTE) / 60) * PI_RAD;
		angleSecond += (angleFract / 60);			// Comment out for "ticky" movement
		angleMinute -= PI_RAD / 4;

		int hourMinutes = calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE);
		double angleHour = ((double) hourMinutes / (12 * 60)) * PI_RAD;
		angleHour -= PI_RAD / 4;

		Line handHour = new Line();
		Line handMinute = new Line();
		Line handSecond = new Line();
		Line handFract = new Line();

		handHour.setPolar(x, y, angleHour, lenHour);
		handMinute.setPolar(x, y, angleMinute, lenMinute);
		handSecond.setPolar(x, y, angleSecond, lenSecond);
		handFract.setPolar(x, y, angleFract, lenFract);

		// Make hands extend *backwards* a little bit from their center anchor point
		handHour.flip();
		handMinute.flip();
		handSecond.flip();
//		handFract.flip();
		handHour.setMagnitude(lenHour * 1.25);
		handMinute.setMagnitude(lenMinute * 1.25);
		handSecond.setMagnitude(lenSecond * 1.33);

//		handFract.setMagnitude(lenFract * 1.15);
//		handHour.flip();
//		handMinute.flip();
//		handSecond.flip();
//		handFract.flip();

//		graphics.setPaint(Color.DARK_GRAY);
		graphics.setPaint(Color.BLACK);

		graphics.setStroke(new BasicStroke(sizeAvail / 25, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		handHour.draw(graphics);

		graphics.setPaint(Color.BLACK);
		graphics.setStroke(new BasicStroke(sizeAvail / 50, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		handMinute.draw(graphics);

		graphics.setStroke(new BasicStroke(sizeAvail / 75, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		graphics.setPaint(new Color(255, 0, 0, 192));
		handSecond.draw(graphics);

		graphics.setStroke(new BasicStroke(sizeAvail / 225, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		handFract.draw(graphics);

		graphics.setPaint(Color.RED);
		graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		double sizePin = sizeAvail / 40.0;
		graphics.fillOval((int) (x - sizePin / 2), (int) (y - sizePin / 2), (int) (sizePin), (int) (sizePin));
	}

	private void paintToBuffer(Graphics graphics)
	{
		Graphics graphicsBuffer = m_buffer.createGraphics();
		paintClock(graphicsBuffer);
	}

	@Override
	public void paintComponent(Graphics graphics)
	{
		super.paintComponent(graphics);

		doPaint(graphics, false);
	}

	private void doPaint(Graphics graphics, boolean paintBuffered)
	{
		if (paintBuffered)
		{
			updateBuffer();
			Graphics graphicsBuffer = m_buffer.createGraphics();
			paintClock(graphicsBuffer);

			graphics.drawImage(m_buffer, 1, 0, null);
		}
		else
		{
			paintClock(graphics);
		}
	}

	private void updateBuffer()
	{
		int width = getWidth();
		int height = getHeight();

		if (m_width != width || m_height != height)
		{
			m_buffer = createBufferedImage(width, height);
			m_width = width;
			m_height = height;
		}

		m_buffer.getGraphics().clearRect(0, 0, width, height);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		repaint();
	}
}
