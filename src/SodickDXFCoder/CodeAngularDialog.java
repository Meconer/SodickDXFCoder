package SodickDXFCoder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import SodickDXFCoder.GeometricEntity.GeometryType;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class CodeAngularDialog extends JDialog {

    Chain chainToCode;
    private final JPanel contentPanel = new JPanel();
    private JButton okButton;
    private JButton btnSkapaProgram;

    private String workingDir;
    private static final String START_SECTION_FILE_NAME_6 = "angle6.txt";
    private static final String START_SECTION_FILE_NAME_1 = "angle1.txt";

    private final ButtonGroup compButtonGrp = new ButtonGroup();
    private JRadioButton rdbtnG41;
    private JRadioButton rdbtnG42;
    private JRadioButton rdbtnG40;

    private final ButtonGroup angleDirButtonGroup = new ButtonGroup();
    private JRadioButton rdbtnAngleDirRight;
    private JRadioButton rdbtnAngleDirLeft;

    private final ButtonGroup noOfCutsButtonGrp = new ButtonGroup();
    private JRadioButton rdbtn1Cuts;
    private JRadioButton rdbtn6Cuts;

    private double chainStartPointx;
    private double chainStartPointy;
    private double chain2ndPointx;
    private double chain2ndPointy;
    private double chainNLPointx;
    private double chainNLPointy;
    private int lastG010203;
    private JCheckBox cbM199;
    private double lastY;
    private double lastX;
    private JTextField tfZLevelProgram;
    private JTextField tfZLevelNext;
    private JLabel lblLutningsvinkel;
    private JTextField tfAngle;

    /**
     * Create the dialog.
     */
    public CodeAngularDialog(Chain chainToCode, String workingDir) {
        super();
        this.workingDir = workingDir;
        this.chainToCode = chainToCode;
        setBounds(100, 100, 500, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        btnSkapaProgram = new JButton("Skapa program");
        btnSkapaProgram.setBounds(138, 180, 157, 23);
        btnSkapaProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                code();
            }
        });
        contentPanel.setLayout(null);
        contentPanel.add(btnSkapaProgram);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Komp", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(106, 10, 66, 102);
        contentPanel.add(panel);
        panel.setLayout(null);

        rdbtnG41 = new JRadioButton("G41");
        rdbtnG41.setBounds(6, 20, 54, 23);
        panel.add(rdbtnG41);
        compButtonGrp.add(rdbtnG41);
        rdbtnG41.setSelected(true);

        rdbtnG42 = new JRadioButton("G42");
        rdbtnG42.setBounds(6, 46, 54, 23);
        panel.add(rdbtnG42);
        compButtonGrp.add(rdbtnG42);

        rdbtnG40 = new JRadioButton("G40");
        rdbtnG40.setBounds(6, 72, 54, 23);
        panel.add(rdbtnG40);
        compButtonGrp.add(rdbtnG40);

        cbM199 = new JCheckBox("M199");
        cbM199.setSelected(true);
        cbM199.setBounds(182, 103, 76, 23);
        contentPanel.add(cbM199);

        panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Lutning", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(182, 10, 148, 76);
        contentPanel.add(panel);
        panel.setLayout(null);

        rdbtnAngleDirLeft = new JRadioButton("Lutning åt vänster");
        rdbtnAngleDirLeft.setBounds(6, 20, 136, 23);
        panel.add(rdbtnAngleDirLeft);
        angleDirButtonGroup.add(rdbtnAngleDirLeft);
        rdbtnAngleDirLeft.setSelected(true);

        rdbtnAngleDirRight = new JRadioButton("Lutning åt höger");
        rdbtnAngleDirRight.setBounds(6, 46, 136, 23);
        panel.add(rdbtnAngleDirRight);
        angleDirButtonGroup.add(rdbtnAngleDirRight);

        panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Antal snitt", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(336, 10, 100, 76);
        contentPanel.add(panel);
        panel.setLayout(null);

        rdbtn1Cuts = new JRadioButton("1");
        rdbtn1Cuts.setBounds(6, 20, 36, 23);
        panel.add(rdbtn1Cuts);
        noOfCutsButtonGrp.add(rdbtn1Cuts);

        rdbtn6Cuts = new JRadioButton("6");
        rdbtn6Cuts.setBounds(6, 46, 36, 23);
        panel.add(rdbtn6Cuts);
        noOfCutsButtonGrp.add(rdbtn6Cuts);
        rdbtn6Cuts.setSelected(true);

        tfZLevelProgram = new JTextField();
        tfZLevelProgram.setText("29.8");
        tfZLevelProgram.setBounds(10, 29, 86, 20);
        contentPanel.add(tfZLevelProgram);
        tfZLevelProgram.setColumns(10);

        JLabel lblZLevProg = new JLabel("Z-nivå program");
        lblZLevProg.setBounds(10, 11, 86, 14);
        contentPanel.add(lblZLevProg);

        JLabel lblZLevelNext = new JLabel("Z-nivå nedre");
        lblZLevelNext.setBounds(10, 60, 86, 14);
        contentPanel.add(lblZLevelNext);

        tfZLevelNext = new JTextField();
        tfZLevelNext.setText("27.8");
        tfZLevelNext.setColumns(10);
        tfZLevelNext.setBounds(10, 78, 86, 20);
        contentPanel.add(tfZLevelNext);

        lblLutningsvinkel = new JLabel("Lutningsvinkel");
        lblLutningsvinkel.setBounds(10, 112, 86, 14);
        contentPanel.add(lblLutningsvinkel);

        tfAngle = new JTextField();
        tfAngle.setText("10.0");
        tfAngle.setBounds(10, 126, 86, 20);
        contentPanel.add(tfAngle);
        tfAngle.setColumns(10);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("Stäng");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);

            }
        });

    }

    public void showDialog() {
        this.setVisible(true);

    }

    private void code() {
        File f = new File(workingDir);
        JFileChooser fc = new JFileChooser(f);
        Boolean cancel = false;
        int returnVal = fc.showSaveDialog(contentPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            f = fc.getSelectedFile();
            if (f.exists()) {
                returnVal = JOptionPane.showConfirmDialog(contentPanel, "Filen finns, skriva över", "OBS!", JOptionPane.YES_NO_OPTION);
                if (returnVal != JOptionPane.YES_OPTION) {
                    // cancel overwrite
                    cancel = true;
                }
            }
            if (!cancel) {
                // Start coding
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    // Add start section
                    setInitialPoints();
                    addStartSection(bw);
                    addSubSection(bw, "N0001");
                    chainToCode.reverseChain();
                    addSubSection(bw, "N0002");
                    bw.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(contentPanel, "Kan inte skapa filen " + f.getName());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(contentPanel, e.getMessage());
                }

            }

        }
        this.setVisible(false);
    }

    private String buildG010203(int g010203) {
        String s = "";
        if (g010203 != lastG010203) {
            switch (g010203) {
                case 1:
                    s = "G01 ";
                    break;

                case 2:
                    s = "G02 ";
                    break;

                case 3:
                    s = "G03 ";
                    break;

                default:
                    break;
            }
            lastG010203 = g010203;
        }
        return s;
    }

    private void addSubSection(BufferedWriter bw, String subname) {
        GeometricEntity geo;
        lastG010203 = -1;
        lastX = -99999.88;
        lastY = -99999.88;
        try {
            bw.write(";\n");
            bw.write(subname + ";\n");
            for (int i = 1; i <= chainToCode.entityList.size() - 2; i++) {
                geo = chainToCode.entityList.get(i);
                if (geo.geometryType == GeometryType.LINE) {
                    bw.write(buildG010203(1) + buildCoord(geo.getX2(), geo.getY2(), false) + ";\n");
                }
                if (geo.geometryType == GeometryType.ARC) {
                    Arc a = (Arc) geo;
                    String s;
                    if (a.angExt < 0) { // cw arc
                        s = buildG010203(2);
                    } else {
                        s = buildG010203(3);
                    }
                    s = s + buildCoord(a.getX2(), a.getY2(), false) + " " + buildIJ(a) + ";\n";
                    bw.write(s);
                }
                if (i==1) bw.write("A" + tfAngle.getText() + ";\n");
            }
            geo = chainToCode.entityList.get(chainToCode.entityList.size() - 1);
            if (geo.geometryType != GeometryType.LINE) {
                throw new Exception("Måste avslutas med linje");
            }
            bw.write("A0;\n");
            bw.write(buildG010203(1) + "G50 G40 H000 " + buildCoord(geo.getX2(), geo.getY2(), false) + ";\n");
            bw.write("M99;\n");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(contentPanel, "Fel vid skapande av subsektion!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPanel, e.getMessage());
        }

    }

    // Number formatting
    private String nForm(double number) {
        String s;
        DecimalFormat df = new DecimalFormat("0.0###");
        s = df.format(number);
        return s.replaceAll(",", ".");

    }

    // Calculate I and J numbers for arc
    private String buildIJ(Arc a) {
        return "I" + nForm(a.xCenter - a.getX1()) + " J" + nForm(a.yCenter - a.getY1());
    }

    // Finds start and end points of first and last element of chain. Has to be a line.
    private void setInitialPoints() throws Exception {

        chainStartPointx = chainToCode.getStartPoint().x;
        chainStartPointy = chainToCode.getStartPoint().y;

        GeometricEntity ent = chainToCode.entityList.get(0);
        if (ent.geometryType != GeometryType.LINE) {
            throw new Exception("Måste starta med linje");
        }
        chain2ndPointx = ent.getX2();
        chain2ndPointy = ent.getY2();

        ent = chainToCode.entityList.get(chainToCode.entityList.size() - 1);
        chainNLPointx = ent.getX1();
        chainNLPointy = ent.getY1();
    }

    private String buildCoord(double x, double y, Boolean forceOut) {
        String s = "";
        if ((Math.abs(x - lastX) > 0.00001d) || forceOut) { // X changed, output X
            s = "X" + nForm(x);
        }

        if ((Math.abs(y - lastY)) > 0.00001d || forceOut) { // Y changed, output Y
            if (!s.isEmpty()) {
                s = s + " ";
            }
            s = s + "Y" + nForm(y);
        }

        lastX = x;
        lastY = y;
        return s;
    }

    private void addStartSection(BufferedWriter bw) {
        try {
            // Write start section part to bw.
            String path = System.getProperty("user.dir");
            if (rdbtn1Cuts.isSelected()) {
                path = path + File.separator + START_SECTION_FILE_NAME_1;
            } else {
                path = path + File.separator + START_SECTION_FILE_NAME_6;
            }
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                line = line + "\n";
                bw.write(line);
            }
            bw.write("TP" + tfZLevelProgram.getText() + ";\n");
            bw.write("TN" + tfZLevelNext.getText() + ";\n");

            // Next write start point info and G92
            bw.write("G55;\n");
            bw.write("G90;\n");

            String s = "G92 " + buildCoord(chainStartPointx, chainStartPointy, true) + " Z0;\n";
            bw.write(s);
            bw.write("G29;\n");
            bw.write("T94;\n");
            bw.write("T84;\n");
            bw.write("C000;\n");

            String comp = "G40";
            String revComp = "G40";
            if (rdbtnG41.isSelected()) {
                comp = "G41";
                revComp = "G42";
            }
            if (rdbtnG42.isSelected()) {
                comp = "G42";
                revComp = "G41";
            }

            String angularDir = "G51";
            String angularRevDir = "G52";
            if (rdbtnAngleDirRight.isSelected()) {
                angularDir = "G52";
                angularRevDir = "G51";
            }

            s = angularDir + " A0 " + comp + " H000 G01 " + buildCoord(chain2ndPointx, chain2ndPointy, true) + ";\n";
            bw.write(s);
            bw.write("A" + tfAngle.getText() + ";\n");
            bw.write("H001 C001;\n");
            bw.write("M98 P0001;\n");
            bw.write("T85;\n");
            bw.write("G149 G249;\n");

            if (rdbtn6Cuts.isSelected()) {
                bw.write("C002;\n");
                bw.write(angularRevDir + " A0 " + revComp + " H000 G01 " + buildCoord(chainNLPointx, chainNLPointy, true) + ";\n");
                bw.write("A" + tfAngle.getText() + ";\n");
                bw.write("H002;\n");
                bw.write("M98 P0002;\n");

                bw.write("C900;\n");
                bw.write(angularDir + " A0 " + comp + " H000 G01 " + buildCoord(chain2ndPointx, chain2ndPointy, true) + ";\n");
                bw.write("A" + tfAngle.getText() + ";\n");
                bw.write("H003;\n");
                bw.write("M98 P0001;\n");

                bw.write("C901;\n");
                bw.write(angularRevDir + " A0 " + revComp + " H000 G01 " + buildCoord(chainNLPointx, chainNLPointy, true) + ";\n");
                bw.write("A" + tfAngle.getText() + ";\n");
                bw.write("H004;\n");
                bw.write("M98 P0002;\n");

                bw.write("C902;\n");
                bw.write(angularDir + " A0 " + comp + " H000 G01 " + buildCoord(chain2ndPointx, chain2ndPointy, true) + ";\n");
                bw.write("A" + tfAngle.getText() + ";\n");
                bw.write("H005;\n");
                bw.write("M98 P0001;\n");

                bw.write("C903;\n");
                bw.write(angularRevDir + " A0 " + revComp + " H000 G01 " + buildCoord(chainNLPointx, chainNLPointy, true) + ";\n");
                bw.write("A" + tfAngle.getText() + ";\n");
                bw.write("H006;\n");
                bw.write("M98 P0002;\n");
            }

            if (cbM199.isSelected()) {
                bw.write("M199;\n");
            } else {
                bw.write("M02;\n");
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(contentPanel, "Kan inte hitta filen ");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(contentPanel, "Fel vid skapande av startsektion!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(contentPanel, e.getMessage());
        }

    }
}
