package org.tsoft.jtivarg.liner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.tsoft.jtivarg.graphics.Line;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *
 */
public class Application implements Runnable, ActionListener, ListSelectionListener
{
    public static final String FORMAT_DOUBLE = "%,.2f";
    public static final String FORMAT_DOUBLE_ANGLE = "%,f";

    private static volatile Application s_instance = null;

    private MainFrame m_mainFrame = null;
    private LinesFrame m_linesFrame = null;
    private Surface m_surface = new Surface();

    private Model m_linesModel = new Model();
    private LineDocument m_lineDocument = new LineDocument();
    private int m_selectedLineIndex = -1;

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    private static String[] COLUMN_NAMES = {
        "index",
        "X1",
        "Y1",
        "X2",
        "Y2",
        "angle",
        "dist"
    };

    public static Application getInstance()
    {
        if (null == s_instance)
        {
            s_instance = new Application();
        }
        return s_instance;
    }

    private Application()
    {
        m_lineDocument = readDocument();
        if (null == m_lineDocument)
        {
            m_lineDocument = new LineDocument();
        }
    }

    private void initGUI()
    {
        m_mainFrame = new MainFrame();
        m_mainFrame.add(m_surface);

        m_mainFrame.init();

        m_linesFrame = new LinesFrame();
        m_linesFrame.init();

        m_linesFrame.getTable().getSelectionModel().addListSelectionListener(this);

//        addLine(new Line(200, 50,  600, 360));
//        addLine(new Line(820, 100, 180, 520));
//        addLine(new Line(620, 300, 190, 510));
//        addLine(new Line(720, 400, 200, 500));
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
            case Menu.FILE_PASTE_LINES:
                String inputString = MultiLineInputDialog.show("Paste lines here", 23, 80);
                m_lineDocument.getLines().addAll(parseLines(inputString));
                repaintAll();
                break;

            case Menu.FILE_CLEAR_LINES:
                clearLines();
                repaintAll();
                break;

            case Menu.FILE_DELETE_LINE:
                deleteCurrentLine();
                repaintAll();
                break;

            case Menu.FILE_EXIT:
                writeDocument();
                System.exit(1);
                break;

            default:
                // do nothing
                break;
        }
    }

    private void deleteCurrentLine()
    {
        List<Line> lineList = m_lineDocument.getLines();
        int lastSelectableIndex = lineList.size() - 1;

        if ((m_selectedLineIndex != -1) && (m_selectedLineIndex <= lastSelectableIndex))
        {
            lineList.remove(m_selectedLineIndex);
            lastSelectableIndex = lineList.size() - 1;

            if (m_selectedLineIndex > lastSelectableIndex)
            {
                m_selectedLineIndex = lastSelectableIndex;
            }
        }
    }

    private LineDocument readDocument()
    {
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        File file = new File("liner.yml");
        LineDocument lineDocument = new LineDocument();

        try
        {
            lineDocument = objectMapper.readValue(file, LineDocument.class);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return lineDocument;
    }

    private void writeDocument()
    {
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        File file = new File("liner.yml");

        try
        {
            objectMapper.writeValue(file, m_lineDocument);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void repaintAll()
    {
        m_mainFrame.repaint(0);
        m_linesFrame.repaint(0);
    }

    private List<Line> parseLines(String inputString)
    {
//         org.tsoft.jtivarg.graphics.Line@50446ff1 (0.00, 0.00), (0.00, 0.00) angle=0.00 distance=0.00
//         org.tsoft.jtivarg.graphics.Line@50446ff1 (1,024.50, 517.50), (1,077.53, 464.47) angle=-0.79 distance=75.00
//         org.tsoft.jtivarg.graphics.Line@50446ff1 (1,077.53, 464.47), (1,095.21, 446.79) angle=-0.79 distance=25.00

        List<Line> lineList = new ArrayList<>();
        String[] lines = inputString.split("\n");
        String error = "";

        for (String line : lines)
        {
            String cleaned = line.trim();
            cleaned = cleaned.replaceAll("\\(", "");
            cleaned = cleaned.replaceAll("\\)", "");
            cleaned = cleaned.replaceAll(",", "");

            String[] tokens = cleaned.split("\\s*(, |\\s)\\s*");

            try
            {
                if (tokens.length >= 5)
                {
                    lineList.add(new Line(
                        Double.parseDouble(tokens[1]),
                        Double.parseDouble(tokens[2]),
                        Double.parseDouble(tokens[3]),
                        Double.parseDouble(tokens[4])));
                }
                else
                {
                    String message = String.format(
                        "Error processing line [%s]: Not enough tokens to parse (%d of 5 required)",
                        line,
                        tokens.length);
                    error += (message + "\n");
                }
            }
            catch (Exception exception)
            {
                String message = String.format("Error processing line [%s]: ", line) + exception;
                LOGGER.warning(message);
                error += (message + "\n");
            }

        }

        if (!error.isEmpty())
        {
            error = "Encountered the following errors while parsing line(s):\n" + error;
            JOptionPane.showMessageDialog(null, error);
        }

        String message = String.format("Added %d new line objects!", lineList.size());
        LOGGER.info(message);
        JOptionPane.showMessageDialog(null, message);

        return lineList;
    }

    public void addLine(Line line)
    {
        m_lineDocument.getLines().add(line);
    }

    public void clearLines()
    {
        m_lineDocument.getLines().clear();
    }

    public List<Line> getLines()
    {
        return Collections.unmodifiableList(m_lineDocument.getLines());
    }

    public Model getLinesModel()
    {
        return m_linesModel;
    }

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        LOGGER.info("Application->valueChanged ListSelectionEvent=[" + e.toString() + "]");
        int selectedIndex = m_linesFrame.getTable().getSelectedRow();

        if (m_selectedLineIndex != selectedIndex)
        {
            LOGGER.info("Selected row set from " + m_selectedLineIndex + " to " + selectedIndex);
            m_selectedLineIndex = selectedIndex;
            repaintAll();
        }
    }

    public int getSelectedLineIndex()
    {
        return m_selectedLineIndex;
    }

    public class Model implements TableModel, TableModelListener
    {
        @Override
        public void tableChanged(TableModelEvent e)
        {
            LOGGER.info("Application->tableChanged event=[" + e.toString() + "]");
        }

        @Override
        public int getRowCount()
        {
            return m_lineDocument.getLines().size();
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int columnIndex)
        {
            return COLUMN_NAMES[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex)
        {
            return "".getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return 0 != columnIndex;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            Line line = m_lineDocument.getLines().get(rowIndex);
            Object value = "Unknown";

            switch (columnIndex)
            {
                case 0:
                    value = "Line " + rowIndex;
                    break;

                case 1:
                    value = String.format(FORMAT_DOUBLE, line.getxBase());
                    break;

                case 2:
                    value = String.format(FORMAT_DOUBLE, line.getyBase());
                    break;

                case 3:
                    value = String.format(FORMAT_DOUBLE, line.getxEnd());
                    break;

                case 4:
                    value = String.format(FORMAT_DOUBLE, line.getyEnd());
                    break;

                // angle
                case 5:
                    value = String.format(FORMAT_DOUBLE_ANGLE, line.getAngle());
                    break;

                // distance
                case 6:
                    value = String.format(FORMAT_DOUBLE, line.getDistance());
                    break;

                default:
                    value = "Unknown";
                    break;
            }

            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            Double value = Double.parseDouble(aValue.toString());

            LOGGER.info(
                String.format(
                    "Application->setValueAt: aValue.toString=[%s], rowIndex=[%d], columnIndex=[%d]",
                    aValue.toString(),
                    rowIndex,
                    columnIndex));

            Line line = m_lineDocument.getLines().get(rowIndex);

            double x1 = line.getxBase();
            double y1 = line.getyBase();
            double x2 = line.getxEnd();
            double y2 = line.getyEnd();
            double angle = line.getAngle();
            double distance = line.getDistance();

            switch(columnIndex)
            {
                // X base
                case 1:
                    x1 = value;
                    line.setCartesian(x1, y1, x2, y2);
                    break;

                // Y base
                case 2:
                    y1 = value;
                    line.setCartesian(x1, y1, x2, y2);
                    break;

                // X end
                case 3:
                    x2 = value;
                    line.setCartesian(x1, y1, x2, y2);
                    break;

                // Y end
                case 4:
                    y2 = value;
                    line.setCartesian(x1, y1, x2, y2);
                    break;

                // angle
                case 5:
                    line.setPolar(x1, y1, value, distance);
                    break;

                // distance
                case 6:
                    line.setPolar(x1, y1, angle, value);
                    break;
            }

            repaintAll();
        }

        @Override
        public void addTableModelListener(TableModelListener l)
        {
            LOGGER.info("Application->addTableModelListener listener = [" + l.toString() + "]");
        }

        @Override
        public void removeTableModelListener(TableModelListener l)
        {
            LOGGER.info("Application->removeTableModelListener listener = [" + l.toString() + "]");
        }
    }
}
