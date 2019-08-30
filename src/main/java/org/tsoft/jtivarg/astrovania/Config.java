/**
 * Copyright 2017 H. Tad Williams All rights reserved.
 * Use is subject to license terms.
 */

package org.tsoft.jtivarg.astrovania;

import java.io.File;
import java.util.logging.Logger;

public class Config
{
    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private String m_worldFile = null;
    private String m_lastDirectory = null;
    private String m_configFile = null;

    public String getWorldFile()
    {
        return m_worldFile;
    }

    public void setWorldFile(String lastFile)
    {
        m_worldFile = lastFile;
    }

    public String getLastDirectory()
    {
        return m_lastDirectory;
    }

    public void setLastDirectory(String lastDirectory)
    {
        m_lastDirectory = lastDirectory;
    }

    public String getConfigFile()
    {
        return m_configFile;
    }

    public void setConfigFile(String configFile)
    {
        m_configFile = configFile;
    }

    public boolean setFileInfo(File file)
    {
        if (null != file && !file.isDirectory())
        {
            setWorldFile(file.getAbsolutePath());
            setLastDirectory(file.getParent());
            return true;
        }

        return false;
    }

    public void report()
    {
        LOGGER.info("World file  " + m_worldFile);
        LOGGER.info("Last dir    " + m_lastDirectory);
        LOGGER.info("Config file " + m_configFile);
    }
}
