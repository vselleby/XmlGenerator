import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class XmlGeneratorView extends JFrame {
    private static final Dimension INPUT_OUTPUT_DIMENSION = new Dimension(400, 600);
    private final XmlGenerator generator;
    private final JTextArea inputTextArea;
    private final JTextArea outputTextArea;

    public XmlGeneratorView(XmlGenerator generator) {
        super("XML Generator");
        this.generator = generator;
        inputTextArea = new JTextArea();
        outputTextArea = new JTextArea();
        initialiseGui();
    }

    private void initialiseGui() {
        createMenu();
        createMainView();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem fromFileItem = new JMenuItem("Read");
        fromFileItem.setToolTipText("Read input from file.");
        fromFileItem.addActionListener(event -> readInputFile());

        JMenuItem toFileItem = new JMenuItem("Write");
        toFileItem.setToolTipText("Write output to file.");
        toFileItem.addActionListener(event -> writeOutputFile());

        fileMenu.add(fromFileItem);
        fileMenu.add(toFileItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createMainView() {
        setLayout(new BorderLayout());
        JPanel inputOutputPanel = new JPanel(new BorderLayout());
        inputOutputPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        inputTextArea.setEditable(true);
        inputTextArea.setAutoscrolls(true);
        outputTextArea.setEditable(false);
        outputTextArea.setAutoscrolls(true);

        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputScrollPane.setAutoscrolls(true);
        inputScrollPane.setPreferredSize(INPUT_OUTPUT_DIMENSION);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder("Input"));

        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setAutoscrolls(true);
        outputScrollPane.setPreferredSize(INPUT_OUTPUT_DIMENSION);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        inputOutputPanel.add(inputScrollPane, BorderLayout.WEST);
        inputOutputPanel.add(outputScrollPane, BorderLayout.EAST);
        inputOutputPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(inputOutputPanel, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton generateButton = new JButton("Generate");
        generateButton.setToolTipText("Generates XML output from the given input.");
        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clears input, output and debug text areas.");

        generateButton.addActionListener(event -> outputTextArea.setText(generator.generate(inputTextArea.getText())));
        clearButton.addActionListener(event -> {
            outputTextArea.setText("");
            inputTextArea.setText("");
        });

        buttonPanel.add(clearButton);
        buttonPanel.add(generateButton);

        return buttonPanel;
    }

    private void readInputFile() {
        FileDialog fileDialog = new FileDialog(this);
        fileDialog.setMode(FileDialog.LOAD);
        fileDialog.setVisible(true);
        File file = new File(fileDialog.getDirectory() + fileDialog.getFile());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                inputTextArea.append(line);
                inputTextArea.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeOutputFile() {
        FileDialog fileDialog = new FileDialog(this);
        fileDialog.setMode(FileDialog.SAVE);
        fileDialog.setVisible(true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileDialog.getDirectory() + fileDialog.getFile()))) {
            outputTextArea.write(writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to write output to file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
