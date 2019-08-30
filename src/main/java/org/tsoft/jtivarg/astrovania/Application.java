package org.tsoft.jtivarg.astrovania;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.tsoft.jtivarg.astrovania.game.Entity;
import org.tsoft.jtivarg.astrovania.game.World;
import org.tsoft.jtivarg.common.application.FileMenu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *
 */
public class Application extends WindowAdapter implements Runnable, ActionListener
{
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    public static final String CONFIG_FILE_NAME = "astrovania.config";

    private MainFrame m_mainFrame = null;
    private World m_world = null;
    private SurfaceGame m_surfaceGame;
    private Config m_config = null;

    private void initGUI()
    {
        m_config = readConfig();
        m_world = new World();
        m_surfaceGame = new SurfaceGame(m_world);
        m_mainFrame = new MainFrame(this, m_surfaceGame);

        m_mainFrame.setState(JFrame.MAXIMIZED_BOTH);
        m_mainFrame.init();
        m_mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        m_mainFrame.setState(JFrame.MAXIMIZED_BOTH);
        m_mainFrame.pack();
        m_mainFrame.setVisible(true);
        m_mainFrame.setExtendedState(m_mainFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        m_mainFrame.addWindowListener(this);

        m_world.reset();

        readWorld();
    }

    @Override
    public void windowClosing(WindowEvent windowEvent)
    {
        writeFiles();
    }

    @Override
    public void run()
    {
        initGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.out.println("actionPerformed: " + e.toString());

        String command = e.getActionCommand();

        switch (command)
        {
            case FileMenu.FILE_NEW:
                m_world.clear();
                m_config.setWorldFile(null);
                m_surfaceGame.repaint(0);
                break;

            case FileMenu.FILE_OPEN:
                doFileOpen();
                m_surfaceGame.repaint(0);
                break;

            case FileMenu.FILE_REOPEN:
                readWorld();
                m_surfaceGame.repaint(0);
                break;

            case FileMenu.FILE_SAVE:
                if (m_config.getWorldFile() != null)
                {
                    writeWorld();
                }
                else
                {
                    doFileSaveAs();
                }
                break;

            case FileMenu.FILE_SAVE_AS:
                doFileSaveAs();
                break;

            case FileMenu.FILE_EXIT:
                writeFiles();
                System.exit(1);
                break;

            case TestMenu.TEST_LINES:
                m_world.addTestLines();
                m_surfaceGame.repaint(0);
                break;

            case WorldMenu.TICK:
                m_surfaceGame.tick();
                break;

            case WorldMenu.RESET_DEMO:
                m_world.reset();
                m_surfaceGame.repaint(0);
                break;

            case WorldMenu.RESET:
                m_world.clear();
                m_surfaceGame.repaint(0);
                break;

            case WorldMenu.RUN:
                m_surfaceGame.setCranking(true);
                break;

            case WorldMenu.STOP:
                m_surfaceGame.setCranking(false);
                break;

            default:
                // do nothing
                break;
        }
    }

    private void doFileSaveAs()
    {
        File fileChosen = chooseFile(m_mainFrame, "Save As");
        if (m_config.setFileInfo(fileChosen))
        {
            writeWorld();
        }
    }

    private void writeFiles()
    {
        writeConfig(m_config);
        writeWorld();
    }

    private void doFileOpen()
    {
        File fileChosen = chooseFile(m_mainFrame, "Open");
        if (m_config.setFileInfo(fileChosen))
        {
            readWorld();
        }
    }

    public File chooseFile(Component parent, String title)
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Astrovania files", "ast");

        chooser.setDialogTitle(title);
        if (null != m_config.getLastDirectory())
        {
            chooser.setCurrentDirectory(new File(m_config.getLastDirectory()));
        }
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(false);
        chooser.setApproveButtonText(title);

        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            return chooser.getSelectedFile();
        }

        return null;
    }

    private Config readConfig()
    {
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        File file = new File(CONFIG_FILE_NAME);
        Config config = new Config();

        try
        {
            config = objectMapper.readValue(file, Config.class);
            config.setConfigFile(file.getAbsolutePath());
            config.report();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return config;
    }

    private void writeWorld()
    {
        String worldFile = m_config.getWorldFile();

        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        File file = new File(worldFile);

        try
        {
            objectMapper.writeValue(file, m_world.getRootEntity());
        }
        catch (IOException e)
        {
            LOGGER.severe("Error writing world to file: " + e);
            e.printStackTrace();
        }
    }

    private void writeConfig(Config config)
    {
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        File file = new File(CONFIG_FILE_NAME);

        try
        {
            objectMapper.writeValue(file, config);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void readWorld()
    {
        if (null != m_config.getWorldFile())
        {
            YAMLFactory yamlFactory = new YAMLFactory();
            ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
            File file = new File(m_config.getWorldFile());

            try
            {
                Entity rootEntity = objectMapper.readValue(file, Entity.class);
                m_world.setRootEntity(rootEntity);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
