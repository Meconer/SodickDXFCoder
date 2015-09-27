package SodickDXFCoder;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DxfCoder implements ListSelectionListener {

    private String workingDir = "E:\\Dropbox\\Mecona\\Gängfräs";
    private String fileName;
    private JFrame frame;
    private JTextArea textArea;

    private PlotPanel plotPanel;
    private final Action openFileAction = new OpenFileAction();
    private final Action exitAction = new ExitAction();
    private final Action codeStraightAction = new CodeStraightAction();
    private final Action codeAngularAction = new CodeAngularAction();
    private final Action codeTBAction = new CodeTBAction();
    private final Action reverseChainAction = new ReverseChainAction();
    private JList chainListBox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    DxfCoder window = new DxfCoder();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public DxfCoder() {
        initialize();
    }

    DefaultListModel chainListBoxModel = new DefaultListModel();

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1012, 663);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel toolPanel = new JPanel();
        frame.getContentPane().add(toolPanel, BorderLayout.WEST);

        chainListBox = new JList();
        chainListBox.setVisibleRowCount(5);
        chainListBox.setModel(chainListBoxModel);
        chainListBox.addListSelectionListener(this);
        toolPanel.add(chainListBox);

        JMenuBar menuBar = new JMenuBar();
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);

        JMenu mnArkiv = new JMenu("Arkiv");
        menuBar.add(mnArkiv);

        JMenuItem mntmOpen = mnArkiv.add(openFileAction);
        mntmOpen.setAction(openFileAction);
        mntmOpen.setText("Öppna");

        JSeparator separator = new JSeparator();
        mnArkiv.add(separator);

        JMenuItem mntmExit = new JMenuItem("Avsluta");
        mntmExit.setAction(exitAction);
        mnArkiv.add(mntmExit);

        JMenu mnCode = new JMenu("Kod");
        menuBar.add(mnCode);

        JMenuItem mntmReverseChain = new JMenuItem("Reversera kedja");
        mntmReverseChain.setAction(reverseChainAction);
        mnCode.add(mntmReverseChain);

        JMenuItem mntmStraight = new JMenuItem("Rakt snitt");
        mntmStraight.setAction(codeStraightAction);
        mnCode.add(mntmStraight);

        JMenuItem mntmAngle = new JMenuItem("Vinkelsnitt");
        mntmAngle.setAction(codeAngularAction);
        mnCode.add(mntmAngle);

        JMenuItem mntmTB = new JMenuItem("T/B-snitt");
        mntmTB.setAction(codeTBAction);
        mnCode.add(mntmTB);

        plotPanel = new PlotPanel();
        frame.getContentPane().add(plotPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        frame.getContentPane().add(scrollPane, BorderLayout.EAST);

        textArea = new JTextArea();
        textArea.setColumns(30);
        scrollPane.setViewportView(textArea);
    }

    private Arc dxfReadArc(BufferedReader inFile) throws IOException {
        String inLine1;
        String inLine2;

        double xC = 0,
                yC = 0,
                r = 0,
                stA = 0,
                endA = 0;
        do {
            // Read 2 lines from dxf file
            inLine1 = inFile.readLine();
            inLine2 = inFile.readLine();
            if (inLine1.matches(" 10")) {
                xC = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 20")) {
                yC = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 40")) {
                r = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 50")) {
                stA = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 51")) {
                endA = Double.parseDouble(inLine2);
            }
        } while (!inLine1.matches(" 51"));
        if (endA < stA) {
            endA += 360.0;
        }
        Arc a = new Arc(xC, yC, r, stA, endA - stA);
        NumberFormat nf = new DecimalFormat("#.###");
        System.out.println("Arc " + nf.format(a.getX1()) + ":"
                + nf.format(a.getY1()) + " ; "
                + nf.format(a.getX2()) + ":"
                + nf.format(a.getY2()) + " - "
                + nf.format(a.stAng) + " . "
                + nf.format(a.angExt));

        return a;
    }

    private Line dxfReadLine(BufferedReader inFile) throws IOException {
        String inLine1;
        String inLine2;
        double x1 = 0, x2 = 0, y1 = 0, y2 = 0;

        do {
            // Read 2 lines from dxf file
            inLine1 = inFile.readLine();
            inLine2 = inFile.readLine();
            if (inLine1.matches(" 10")) {
                x1 = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 20")) {
                y1 = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 11")) {
                x2 = Double.parseDouble(inLine2);
            }
            if (inLine1.matches(" 21")) {
                y2 = Double.parseDouble(inLine2);
            }
        } while (!inLine1.matches(" 21"));
        Line l = new Line(x1, y1, x2, y2);
        NumberFormat nf = new DecimalFormat("#.###");
        System.out.println("Line " + nf.format(l.getX1()) + ":"
                + nf.format(l.getY1()) + " ; "
                + nf.format(l.getX2()) + ":"
                + nf.format(l.getY2()));
        return l;
    }

    @SuppressWarnings("serial")
    private class OpenFileAction extends AbstractAction {

        public OpenFileAction() {
            putValue(NAME, "Öppna fil");
            putValue(SHORT_DESCRIPTION, "Opens a dxf files and reads entities from it");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fileOpen();
        }
    }

    @SuppressWarnings("serial")
    private class ExitAction extends AbstractAction {

        public ExitAction() {
            putValue(NAME, "Avsluta");
            putValue(SHORT_DESCRIPTION, "Exits applicatin");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    @SuppressWarnings("serial")
    private class CodeStraightAction extends AbstractAction {

        public CodeStraightAction() {
            putValue(NAME, "Rakt snitt");
            putValue(SHORT_DESCRIPTION, "Builds code for straight edm");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buildCodeStraight();
        }
    }

    @SuppressWarnings("serial")
    private class CodeAngularAction extends AbstractAction {

        public CodeAngularAction() {
            putValue(NAME, "Vinkelsnitt");
            putValue(SHORT_DESCRIPTION, "Builds code for angular edm");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buildCodeAngular();
        }
    }

    @SuppressWarnings("serial")
    private class CodeTBAction extends AbstractAction {

        public CodeTBAction() {
            putValue(NAME, "T/B-snitt");
            putValue(SHORT_DESCRIPTION, "Builds code for T/B edm");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            buildCodeTB();
        }
    }

    @SuppressWarnings("serial")
    private class ReverseChainAction extends AbstractAction {

        public ReverseChainAction() {
            putValue(NAME, "Reversera kedja");
            putValue(SHORT_DESCRIPTION, "Reverse selected chains");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            reverseChain();
        }
    }

    public void fileOpen() {
        plotPanel.chainList.clear();
        BufferedReader inFile;
        File dir = new File(workingDir);
        JFileChooser fc = new JFileChooser(dir);
        fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
        fc.addChoosableFileFilter(new FileNameExtensionFilter("DXF-filer", "dxf"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                inFile = new BufferedReader(new FileReader(fc.getSelectedFile()));
                workingDir = fc.getSelectedFile().getPath();
                fileName = fc.getSelectedFile().getName();

                boolean inEntitiesSection = false;
                String inLine2 = null;
                while ((inFile.readLine()) != null) {
                    inLine2 = inFile.readLine();
                    if (inEntitiesSection) {
                        if (inLine2.matches("LINE")) {
                            Line l = dxfReadLine(inFile);
                            plotPanel.chainList.addGeo(l);
                        }
                        if (inLine2.matches("ARC")) {
                            Arc arc = dxfReadArc(inFile);
                            plotPanel.chainList.addGeo(arc);
                        }
                        if (inLine2.matches("ENDSEC")) {
                            inEntitiesSection = false;
                        }
                    } else {
                        if (inLine2.matches("ENTITIES")) {
                            inEntitiesSection = true;
                        }
                    }
                }
                plotPanel.repaint();
                chainListBoxModel.clear();
                int noOfChains = plotPanel.chainList.listOfChains.size();
                textArea.append("Antal kedjor :" + noOfChains + "\n");
                for (int i = 1; i <= noOfChains; i++) {
                    String s = "Kedja " + i;
                    chainListBoxModel.addElement(s);
                }
                if (noOfChains > 0) {
                    chainListBox.setSelectedIndex(0);
                }
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "Unable to find file");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error opening file");
            }
        }
    }

    public void reverseChain() {
        int[] selectedChains = chainListBox.getSelectedIndices();
        for (int i = 0; i < selectedChains.length; i++) {
            plotPanel.chainList.listOfChains.get(selectedChains[i]).reverseChain();
        }
        plotPanel.repaint();
    }

    public void buildCodeStraight() {
        // First check if one link is selected
        int[] selectedChains = chainListBox.getSelectedIndices();
        if (selectedChains.length != 1) {
            JOptionPane.showMessageDialog(frame, "Välj först en kedja som skall kodas");
        } else {
            CodeStraightDialog cd;
            cd = new CodeStraightDialog(plotPanel.chainList.listOfChains.get(selectedChains[0]), workingDir, fileName);
            cd.showDialog();
        }

    }

    public void buildCodeAngular() {
        // First check if one link is selected
        int[] selectedChains = chainListBox.getSelectedIndices();
        if (selectedChains.length != 1) {
            JOptionPane.showMessageDialog(frame, "Välj först en kedja som skall kodas");
        } else {
            CodeAngularDialog cd = new CodeAngularDialog(plotPanel.chainList.listOfChains.get(selectedChains[0]), workingDir, fileName);
            cd.showDialog();
        }

    }

    public void buildCodeTB() {
        // First check if two links is selected
        int[] selectedChains = chainListBox.getSelectedIndices();
        if (selectedChains.length != 2) {
            JOptionPane.showMessageDialog(frame, "Välj först två kedjor som skall kodas");
        } else {
            CodeTBDialog cd = new CodeTBDialog(plotPanel.chainList.listOfChains.get(selectedChains[0]),
                    plotPanel.chainList.listOfChains.get(selectedChains[1]),
                    selectedChains[0], selectedChains[1],
                    workingDir,
                    fileName);
            cd.showDialog();
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent ev) {
        if (ev.getValueIsAdjusting() == false) {
            // First clear selection of all chains
            for (int i = 0; i < plotPanel.chainList.listOfChains.size(); i++) {
                plotPanel.chainList.listOfChains.get(i).setSelected(false);
            }
            int[] selectedChains = chainListBox.getSelectedIndices();
            for (int i = 0; i < selectedChains.length; i++) {
                plotPanel.chainList.listOfChains.get(selectedChains[i]).setSelected(true);
            }
            plotPanel.repaint();
        }

    }

}
