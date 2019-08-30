package org.tsoft.jtivarg.astrovania;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import javax.swing.*;

import org.tsoft.jtivarg.astrovania.game.World;

/**
 *
 */
//class SurfaceGame extends JPanel implements ActionListener
class SurfaceGame extends JPanel implements MouseListener, KeyListener, ComponentListener
{
    private static final Logger LOGGER = Logger.getLogger(SurfaceGame.class.getName());

    public static final String GAME_EXECUTIVE_LOOP = "game-executive-loop";
    private static long TICK_INTERVAL_MS = 10;

    // TODO ownership of world should be application / model
    private final World m_world;

    // TODO ownership of game state should be application
    private boolean m_isCranking = false;

    // TODO ownership of main game loop should be application
    private long m_lastTickMS = System.currentTimeMillis();
    private final GameLoopRunnable m_gameLoopRunnable = new GameLoopRunnable();

    // TODO World or game should be processor for keyboard and mouse input

    public SurfaceGame(World world)
    {
        m_world = world;

        Thread gameLoopThread = new Thread(m_gameLoopRunnable);
        gameLoopThread.setDaemon(true);
        gameLoopThread.setName(GAME_EXECUTIVE_LOOP);
        gameLoopThread.start();

        setBackground(Color.BLACK);

        addMouseListener(this);
        addComponentListener(this);
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D) graphics;

        // TODO do rendering hints need to be set with each paint or can they be set once?
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        m_world.draw(g2d);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // ignore
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        switch (e.getButton())
        {
            case MouseEvent.BUTTON1:
                m_world.mouseClicked(e);
                break;

            case MouseEvent.BUTTON2:
                m_world.reset();
                repaint(0);
                break;

            case MouseEvent.BUTTON3:
                setCranking(!isCranking());
                break;

            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // ignore
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // ignore
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // ignore
    }

    @Override
    public void componentResized(ComponentEvent e)
    {
        System.out.println("componentResized: " + e);

        m_world.setViewport(0, 0, getWidth(), getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
        // TODO Auto generated method stub
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
        // TODO Auto generated method stub
    }

    @Override
    public void componentHidden(ComponentEvent e)
    {
        // TODO Auto generated method stub
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        // TODO Auto generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_SPACE:
                if (isCranking())
                {
                    setCranking(false);
                }
                tick();
                break;

            case KeyEvent.VK_F12:
            {
                if (e.isControlDown())
                {
                    LOGGER.info("Resetting world entities to start state!");
                    m_world.reset();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto generated method stub
    }

    public void setCranking(boolean isCranking)
    {
        m_isCranking = isCranking;
        m_gameLoopRunnable.setCranking(m_isCranking);
    }

    public boolean isCranking()
    {
        return m_isCranking;
    }

    public void tick()
    {
        m_world.tick(TICK_INTERVAL_MS);
        repaint(0);
    }

    private class GameLoopRunnable implements Runnable
    {
        private final AtomicBoolean m_isCranking = new AtomicBoolean(false);

        @Override
        public void run()
        {
            while (true)
            {
                if (m_isCranking.get())
                {
                    long timeNow = System.currentTimeMillis();

                    if ((timeNow - m_lastTickMS) >= TICK_INTERVAL_MS)
                    {
//                        m_world.tick(timeNow - m_lastTickMS);
                        m_world.tick(TICK_INTERVAL_MS);
                        repaint(0);
                        m_lastTickMS = timeNow;
                    }
                }
                try
                {
                    Thread.sleep(TICK_INTERVAL_MS / 2);
                }
                catch (InterruptedException e)
                {
                    return;
                }
            }
        }

        public void setCranking(boolean cranking)
        {
            m_isCranking.set(cranking);
        }
    }
}
