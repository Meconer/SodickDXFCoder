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
public class CodeTBDialog extends JDialog {
	Chain chainToCode1;
	Chain chainToCode2;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton btnSkapaProgram;

	private static final String STANDARD_DIR = "E:\\Mats\\My Dropbox\\Ironcad";
	private static final String START_SECTION_FILE_NAME = "tb.txt";
	private final ButtonGroup compButtonGrp = new ButtonGroup();
	private JRadioButton rdbtnG141;
	private JRadioButton rdbtnG142;
	private JRadioButton rdbtnG40;
	private final ButtonGroup chainButtonGroup = new ButtonGroup();
	private JRadioButton rdbtnChain2Top;
	private JRadioButton rdbtnChain1Top;
	private double chainTopStartPointx;
	private double chainTopStartPointy;
	private double chain2ndPointTopx;
	private double chain2ndPointTopy;
	private double chainNLPointTopx;
	private double chainNLPointTopy;
	private double chainBottomStartPointx;
	private double chainBottomStartPointy;
	private double chain2ndPointBottomx;
	private double chain2ndPointBottomy;
	private double chainNLPointBottomx;
	private double chainNLPointBottomy;
	private int lastG010203Top;
	private int lastG010203Bottom;
	private JCheckBox cbM199;
	private double lastYTop;
	private double lastXTop;
	private double lastXBottom;
	private double lastYBottom;
	private JTextField tfZLevelProgram;
	private JTextField tfZLevelNext;
	private int chainNo1;
	private int chainNo2;
	private Chain topChain;
	private Chain bottomChain;
	private Double deltax;
	private Double deltay;
	/**
	 * Create the dialog.
	 * @param chainToCode1
	 * @param chainToCode2 
	 * @param chainNo1 
	 * @param chainNo2 
	 */
	public CodeTBDialog(Chain chainToCode1, Chain chainToCode2, int chainNo1, int chainNo2) {
		super();
		this.chainToCode1 = chainToCode1;
		this.chainToCode2 = chainToCode2;
		this.chainNo1 = chainNo1;
		this.chainNo2 = chainNo2;
		setBounds(100, 100, 450, 300);
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
		panel.setBounds(344, 10, 66, 102);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		rdbtnG141 = new JRadioButton("G141");
		rdbtnG141.setBounds(6, 20, 54, 23);
		panel.add(rdbtnG141);
		compButtonGrp.add(rdbtnG141);
		rdbtnG141.setSelected(true);
		
		rdbtnG142 = new JRadioButton("G142");
		rdbtnG142.setBounds(6, 46, 54, 23);
		panel.add(rdbtnG142);
		compButtonGrp.add(rdbtnG142);
		
		rdbtnG40 = new JRadioButton("G140");
		rdbtnG40.setBounds(6, 72, 54, 23);
		panel.add(rdbtnG40);
		compButtonGrp.add(rdbtnG40);
		
		cbM199 = new JCheckBox("M199");
		cbM199.setSelected(true);
		cbM199.setBounds(182, 103, 76, 23);
		contentPanel.add(cbM199);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Toppkedja", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(147, 10, 148, 76);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);
		
		rdbtnChain1Top = new JRadioButton("Kedja " +  Integer.toString(this.chainNo1+1));
		rdbtnChain1Top.setBounds(6, 20, 136, 23);
		panel_1.add(rdbtnChain1Top);
		chainButtonGroup.add(rdbtnChain1Top);
		rdbtnChain1Top.setSelected(true);
		
		rdbtnChain2Top = new JRadioButton("Kedja " +  Integer.toString(this.chainNo2+1));
		rdbtnChain2Top.setBounds(6, 46, 136, 23);
		panel_1.add(rdbtnChain2Top);
		chainButtonGroup.add(rdbtnChain2Top);
		
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
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			okButton = new JButton("Stång");
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
	}

	public void showDialog() {
		this.setVisible(true);
		
	}


	private void code() {
		// First check if both chains have same start and endpoint and also same number of elements.
		if (chainToCode1.entityList.size() != chainToCode2.entityList.size()) {
			JOptionPane.showMessageDialog(contentPanel, "Kedjorna har inte samma antal element");
			return;
		}
		
		Double delta = chainToCode1.getEndPoint().x - 
				       chainToCode1.getStartPoint().x - 
				       chainToCode2.getEndPoint().x + 
				       chainToCode2.getStartPoint().x;
		if (Math.abs(delta) > 0.00001) {
			JOptionPane.showMessageDialog(contentPanel, "Kedjornas start och slutpunkt stämmer inte överens i x");
			return;
		}

		delta = chainToCode1.getEndPoint().y - 
			    chainToCode1.getStartPoint().y - 
			    chainToCode2.getEndPoint().y + 
			    chainToCode2.getStartPoint().y;
		if (Math.abs(delta) > 0.00001) {
			JOptionPane.showMessageDialog(contentPanel, "Kedjornas start och slutpunkt stämmer inte överens i y");
			return;
		}
		
		// Check is ok. Now start to code
		File f = new File(STANDARD_DIR);
		JFileChooser fc = new JFileChooser(f);
		Boolean cancel = false;
		int returnVal = fc.showSaveDialog(contentPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
			if (f.exists()) {
				returnVal = JOptionPane.showConfirmDialog(contentPanel, "Filen finns, skriva över","OBS!",JOptionPane.YES_NO_OPTION);
				if (returnVal != JOptionPane.YES_OPTION ) {
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
					chainToCode1.reverseChain();
					addSubSection(bw, "N0002");
					bw.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(contentPanel, "Kan inte skapa filen " + f.getName());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(contentPanel, e.getMessage() );
				}
				
			}
			
		}
		this.setVisible(false);
	}

	private String buildG010203Top(int g010203) {
		String s = "";
		if (g010203 != lastG010203Top) {
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
			lastG010203Top = g010203;
		}
		return s;
	}
	
	private String buildG010203Bottom(int g010203) {
		String s = "";
		if (g010203 != lastG010203Bottom) {
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
			lastG010203Bottom = g010203;
		}
		return s;
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

		if (rdbtnChain1Top.isSelected()) {
			topChain = chainToCode1;
			bottomChain = chainToCode2;
		} else {
			topChain = chainToCode2;
			bottomChain = chainToCode1;
		}
		
		// First chain
		chainTopStartPointx = topChain.getStartPoint().x;
		chainTopStartPointy = topChain.getStartPoint().y;

		GeometricEntity ent = topChain.entityList.get(0);
		if (ent.geometryType != GeometryType.LINE ) throw new Exception("Måste starta med linje");
		chain2ndPointTopx = ent.getX2();
		chain2ndPointTopy = ent.getY2();

		ent = topChain.entityList.get(topChain.entityList.size()-1);
		chainNLPointTopx = ent.getX1();
		chainNLPointTopy = ent.getY1();
		
		// Second chain
		chainBottomStartPointx = bottomChain.getStartPoint().x;
		chainBottomStartPointy = bottomChain.getStartPoint().y;

		ent = bottomChain.entityList.get(0);
		if (ent.geometryType != GeometryType.LINE ) throw new Exception("Måste starta med linje");
		chain2ndPointBottomx = ent.getX2();
		chain2ndPointBottomy = ent.getY2();

		ent = bottomChain.entityList.get(bottomChain.entityList.size()-1);
		chainNLPointBottomx = ent.getX1();
		chainNLPointBottomy = ent.getY1();
		
		deltax = chainTopStartPointx - chainBottomStartPointx;
		deltay = chainTopStartPointy - chainBottomStartPointy;
		
	}

	private String buildCoordTop(double x, double y, Boolean forceOut) {
		String s = "";
		if ( (Math.abs(x - lastXTop) > 0.00001d ) || forceOut ) { // X changed, output X
			s = "X" + nForm(x);
		}
		
		if ( (Math.abs(y - lastYTop)) > 0.00001d || forceOut) { // Y changed, output Y
			if ( !s.isEmpty() ) s = s + " "; 
			s = s + "Y" + nForm(y);
		}
		
		lastXTop = x;
		lastYTop = y;
		return s;
	}

	private String buildCoordBottom(double x, double y, Boolean forceOut) {
		String s = "";
		if ( (Math.abs(x - lastXBottom) > 0.00001d ) || forceOut ) { // X changed, output X
			s = "X" + nForm(x);
		}
		
		if ( (Math.abs(y - lastYBottom)) > 0.00001d || forceOut) { // Y changed, output Y
			if ( !s.isEmpty() ) s = s + " "; 
			s = s + "Y" + nForm(y);
		}
		
		lastXBottom = x;
		lastYBottom = y;
		return s;
	}

	private void addStartSection(BufferedWriter bw) {
		try {
			// Write start section part to bw.
			String path = DxfCoder.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = new File(path).getParentFile().getPath()+ File.separator + START_SECTION_FILE_NAME;
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
	
	
			
			String s = "G92 "+buildCoordTop(chainBottomStartPointx,chainBottomStartPointy, true) + " U0 V0 Z0;\n";
			bw.write(s);
			bw.write("G29;\n");
			bw.write("T94;\n");
			bw.write("T84;\n");
			bw.write("C000;\n");
			
			String comp = "G140";
			String revComp ="G140";
			if (rdbtnG141.isSelected())  {
				comp = "G141";
				revComp = "G142";
			}
			if (rdbtnG142.isSelected()) {
				comp = "G142";
				revComp = "G141";
			}
			
			bw.write(comp + " H000;\n");
			
			s = "G01 "+buildCoordTop(chain2ndPointTopx - deltax,chain2ndPointTopy - deltay, true) + " : " +
				"G01 "+buildCoordBottom(chain2ndPointBottomx,chain2ndPointBottomy, true) + ";\n";
			bw.write(s);
			bw.write("H001 C001;\n");
			bw.write("M98 P0001;\n");
			bw.write("T85;\n");
			bw.write("G149 G249;\n");
			
			bw.write("C002;\n");
			bw.write(revComp + " H000;\n");
			s = "G01 " + buildCoordTop(chainNLPointTopx - deltax, chainNLPointTopy - deltay, true) + " : " +
				"G01 "+ buildCoordBottom(chainNLPointBottomx,chainNLPointBottomy, true) + ";\n";
			bw.write(s);
			bw.write("H002;\n");
			bw.write("M98 P0002;\n");
			
			bw.write("C900;\n");
			bw.write(comp + " H000;\n");
			s = "G01 "+buildCoordTop(chain2ndPointTopx - deltax,chain2ndPointTopy - deltay, true) + " : " +
				"G01 "+buildCoordBottom(chain2ndPointBottomx,chain2ndPointBottomy, true) + ";\n";
			bw.write(s);
			bw.write("H003;\n");
			bw.write("M98 P0001;\n");
			
			bw.write("C901;\n");
			bw.write(revComp + " H000;\n");
			s = "G01 " + buildCoordTop(chainNLPointTopx - deltax, chainNLPointTopy - deltay, true) + " : " +
				"G01 "+ buildCoordBottom(chainNLPointBottomx,chainNLPointBottomy, true) + ";\n";
			bw.write(s);
			bw.write("H004;\n");
			bw.write("M98 P0002;\n");
			
			bw.write("C902;\n");
			bw.write(comp + " H000;\n");
			s = "G01 "+buildCoordTop(chain2ndPointTopx - deltax,chain2ndPointTopy - deltay, true) + " : " +
				"G01 "+buildCoordBottom(chain2ndPointBottomx,chain2ndPointBottomy, true) + ";\n";
			bw.write(s);
			bw.write("H005;\n");
			bw.write("M98 P0001;\n");
			
			bw.write("C903;\n");
			bw.write(revComp + " H000;\n");
			s = "G01 " + buildCoordTop(chainNLPointTopx - deltax, chainNLPointTopy - deltay, true) + " : " +
				"G01 "+ buildCoordBottom(chainNLPointBottomx,chainNLPointBottomy, true) + ";\n";
			bw.write(s);
			bw.write("H006;\n");
			bw.write("M98 P0002;\n");
			
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

	private void addSubSection(BufferedWriter bw, String subname) {
		GeometricEntity geo;
		lastG010203Top = -1;
		lastG010203Bottom = -1;
		lastXTop = -99999.88;
		lastYTop = -99999.88;
		lastXBottom = -99999.88;
		lastYBottom = -99999.88;
		try {
			bw.write(";\n");
			bw.write(subname + ";\n");
			for (int i = 1 ; i <= topChain.entityList.size()-2 ; i++) {
				geo = topChain.entityList.get(i);
				if (geo.geometryType == GeometryType.LINE) {
					bw.write(buildG010203Top(1) + buildCoordTop(geo.getX2() - deltax, geo.getY2() - deltay, false)+ " : ");
				}
				if (geo.geometryType == GeometryType.ARC) {
					Arc a = (Arc) geo;
					String s;
					if (a.angExt < 0) { // cw arc
						s = buildG010203Top(2);
					} else {
						s = buildG010203Top(3);
					}
					s = s + buildCoordTop(a.getX2() - deltax, a.getY2() - deltay, false) + " " + buildIJ(a) +  " : ";
					bw.write(s);
				}
				geo = bottomChain.entityList.get(i);
				if (geo.geometryType == GeometryType.LINE) {
					bw.write(buildG010203Bottom(1) + buildCoordBottom(geo.getX2(), geo.getY2(), false)+ ";\n");
				}
				if (geo.geometryType == GeometryType.ARC) {
					Arc a = (Arc) geo;
					String s;
					if (a.angExt < 0) { // cw arc
						s = buildG010203Bottom(2);
					} else {
						s = buildG010203Bottom(3);
					}
					s = s + buildCoordBottom(a.getX2(), a.getY2() , false) + " " + buildIJ(a) +  ";\n";
					bw.write(s);
				}
			}
			geo = bottomChain.entityList.get(bottomChain.entityList.size()-1);
			if (geo.geometryType != GeometryType.LINE) {
				throw new Exception("Måste avslutas med linje");
			}
			bw.write( "G140;\n");
			bw.write(buildG010203Top(1) + buildCoordTop(geo.getX2(), geo.getY2() , false) + ";\n");
			bw.write("M99;\n");
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPanel, "Fel vid skapande av subsektion!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(contentPanel, e.getMessage());
		}
		
	}
}


