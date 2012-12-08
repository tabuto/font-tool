package com.ofgod.fonttool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FontTool extends JFrame implements ActionListener {
	private static final long serialVersionUID = 8257877457563760548L;
	
	static FontManager Fm;
	JPanel 	  Settings;
	JPanel	  InfoPanel;
	JMenuBar  MBar;
	JScrollPane hScroll;
	// Panel where characters will be drawn.
	DPanel    Preview;
	JComboBox FontChooser;
	JComboBox FSizeChooser;
	JComboBox CBox;
	JCheckBox showGrid;
	JComboBox useAA;
	SpinnerNumberModel SpinnerRange = null;
	SpinnerNumberModel SpinnerRange2 = null;
	JSpinner CSpinner;
	JSpinner CSpinner2;
	SpinnerNumberModel SCharRange = null;
	JSpinner SChar;
	JButton button5;
	JButton button6;
	JButton button7;
	JLabel loadedFont;
	String[] Image_Sizes = { "256x256", "512x512" };
	Float[]    Font_Progression = {12f,14f,16f,18f,21f,24f};
	String[] AA_Modes = {"Off","On","AA Gasp","AA LCD-HRGB"};

	public FontTool() {
		
		Setup_Panels();
		Setup_Widgets();
		Setup_InfoPanel();
		Setup_MBar();

		setTitle("Font Tool");
		setBackground(Color.gray);
		setSize(640, 480);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		//setResizable(false);
		setVisible(true);
        setJMenuBar(MBar);
	}

	private void Setup_Panels() {
		// Left settings panel.
		Settings = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Settings.setPreferredSize(new Dimension(200, 350));
		Settings.setBorder(new CompoundBorder(new TitledBorder("Options"),
				new EmptyBorder(4, 4, 4, 4)));
		pack();
		add(Settings, BorderLayout.EAST);
		// Add a drawing panel.
		Preview = new DPanel("arial.ttf", 12);
		add(Preview, BorderLayout.CENTER);
		MBar = new JMenuBar();
		// Add a status panel (Font info,x/y mouse position on the grid etc...).
		InfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		InfoPanel.setPreferredSize(new Dimension(100,110));
		InfoPanel.setBorder(new CompoundBorder(new TitledBorder("Info"),
				new EmptyBorder(4, 4, 4, 4)));
		pack();
		add(InfoPanel,BorderLayout.SOUTH);
	}

	private void Setup_Widgets() {

	    GraphicsEnvironment gEnv = GraphicsEnvironment
	        .getLocalGraphicsEnvironment();
	    
	    String envfonts[] = gEnv.getAvailableFontFamilyNames();
	    
		JLabel Label0 = new JLabel("Font:");
		Settings.add(Label0);
		
		FontChooser = new JComboBox(envfonts);
		FontChooser.setPreferredSize(new Dimension(180, 25));
		FontChooser.setEnabled(false);
		FontChooser.setSelectedItem("Arial");
		FontChooser.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				String Selection;
				
				Selection = (String) FontChooser.getSelectedItem();
				
				if( FontChooser.getSelectedIndex() == -1 ){
					//We are using a custom font...
					return;
				}
				
				Preview.setCFont(Selection,false);
			    loadedFont.setText("<html>Using Font: " + FontManager.GetFont().getName()
						   + "<br>" + "Font Size: "+ FontManager.GetFont().getSize() + "<br>" 
						   + "Custom TTF Font: No"+ "<html>");
			}
		});
		Settings.add(FontChooser);
		
		JLabel FSize = new JLabel("Font Size:");
		Settings.add(FSize);
		FSizeChooser = new JComboBox(Font_Progression);
		FSizeChooser.setPreferredSize(new Dimension(180, 25));
		FSizeChooser.setSelectedIndex(0);
		FSizeChooser.setEnabled(false);
		FSizeChooser.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				Float Item = (Float)FSizeChooser.getSelectedItem();
				
				Preview.setFontSize(Item);
			}
		});
		Settings.add(FSizeChooser);
		
		JLabel Label = new JLabel("Image format:");
		Label.setHorizontalAlignment(JLabel.LEFT);
		Settings.add(Label);
		
		CBox = new JComboBox(Image_Sizes);
		CBox.setSelectedIndex(-1);
		CBox.setPreferredSize(new Dimension(180, 25));
		// CBox.setMaximumSize(new Dimension(140, 22));
		CBox.addActionListener(this);
		Settings.add(CBox);
		
		JLabel Label2 = new JLabel("Cells Width/Height:",JLabel.LEFT);
		Settings.add(Label2);
		
		SpinnerRange = new SpinnerNumberModel(16, 0, 40, 1);
		CSpinner = new JSpinner(SpinnerRange);
		CSpinner.setPreferredSize(new Dimension(40,20));
		CSpinner.setEnabled(false);
		ChangeListener JSpinChanged1 = new ChangeListener() {
			
			public void stateChanged(ChangeEvent ev) {
		         System.out.println("Source: " + ev.getSource());
		         System.out.println("Changed to: " + SpinnerRange.getNumber().intValue());
		         Preview.setCellWidth(SpinnerRange.getNumber().intValue());
			}
		};
		CSpinner.addChangeListener(JSpinChanged1);
		Settings.add(CSpinner);
		JLabel Label3 = new JLabel("x");
		Settings.add(Label3);
		SpinnerRange2 = new SpinnerNumberModel(16, 0, 40, 1);
		CSpinner2 = new JSpinner(SpinnerRange2);
		CSpinner2.setPreferredSize(new Dimension(40,20));
		CSpinner2.setEnabled(false);
		final ChangeListener JSpinChanged2 = new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
		         System.out.println("Source: " + ev.getSource());
		         System.out.println("Changed to: " + SpinnerRange.getNumber().intValue());
		         Preview.setCellHeight(SpinnerRange2.getNumber().intValue());
			}
		};
		CSpinner2.addChangeListener(JSpinChanged2);
		Settings.add(CSpinner2);
			
		JLabel LabelS = new JLabel("Starting Char:",JLabel.LEFT);
		Settings.add(LabelS);
		SCharRange = new SpinnerNumberModel(0, 0, 256, 1);
		SChar = new JSpinner(SCharRange);
		SChar.setPreferredSize(new Dimension(40,20));
		SChar.setEnabled(false);
		final ChangeListener JSCharChanged = new ChangeListener() {
			public void stateChanged(ChangeEvent ev) {
		         System.out.println("Source: " + ev.getSource());
		         System.out.println("Changed to: " + SCharRange.getNumber().intValue());
		         Preview.setStartingChar(SCharRange.getNumber().intValue());
			}
		};
		SChar.addChangeListener(JSCharChanged);
		Settings.add(SChar);
		
		JLabel	  label4 = new JLabel("Show grids?");
		Settings.add(label4);
		showGrid = new JCheckBox();
		showGrid.setEnabled(false);
		showGrid.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {

				if( showGrid.isSelected() ){
					//System.out.println("Checked.");
					Preview.setShowGrid(true);
				} else {
					Preview.setShowGrid(false);
				}
			}
		});

		Settings.add(showGrid);
		
		
		JLabel label5 = new JLabel("Background Color:");
		Settings.add(label5);

		button5 = new JButton("Background...");
		button5.setEnabled(false);
		button5.setBackground(Preview.getBGColor());
		button5.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent act) {
				 Color background;
				//FIXME:Get background from dpanel class
				background = JColorChooser.showDialog(Settings, "Choose Background Color", Color.black);
				if( background != null ){
					System.out.println("Background Color => " + background.toString());
					Preview.setBGColor(background);
					button5.setBackground(background);
				}
			}
		});
		Settings.add(button5);
		
		JLabel label6 = new JLabel("Grid Color:");
		Settings.add(label6);

		button6 = new JButton("Background...");
		button6.setEnabled(false);
		button6.setBackground(Preview.getGridColor());
		button6.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent act) {
				Color GridColor;
				//FIXME:Get background from dpanel class
				GridColor = JColorChooser.showDialog(Settings, "Choose Grid Color", Color.red);
				if( GridColor != null ){
					System.out.println("Grid Color => " + GridColor.toString());
					Preview.setGridColor(GridColor);
					button6.setBackground(GridColor);
				}
			}
		});
		Settings.add(button6);

		JLabel label7 = new JLabel("Characters Color:");
		Settings.add(label7);

		button7 = new JButton("Char Color...");
		button7.setEnabled(false);
		button7.setBackground(Preview.getCharsColor());
		button7.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent act) {
				Color CharsColor;
				//FIXME:Get background from dpanel class 
				CharsColor = JColorChooser.showDialog(Settings, "Choose Characters Color", Color.white);
				if( CharsColor != null ){
					System.out.println("Chars Color => " + CharsColor.toString());
					Preview.setCharsColor(CharsColor);
					button6.setBackground(CharsColor);
				}
			}
		});
		Settings.add(button7);
	
	}
	
	private void Setup_InfoPanel(){
		loadedFont = new JLabel("<html>Using Font: " + FontManager.GetFont().getName()
				   + "<br>" + "Font Size: "+ FontManager.GetFont().getSize() + "<br>" 
				   + "Custom TTF Font: No"+ "<html>",JLabel.LEFT);
		InfoPanel.add(loadedFont);
		return;
	}

	private void Setup_MBar(){
		JMenu File = new JMenu("File");

		JMenuItem ImportItem = new JMenuItem("Import...");
		ImportItem.setToolTipText("Import a .ttf font");
		ImportItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser FChooser1 = new JFileChooser();
				FChooser1.setAcceptAllFileFilterUsed(false);
				FChooser1.setFileFilter(new TTFFilter());
				int stats;
				stats = FChooser1.showOpenDialog(null);
				if( stats == JFileChooser.APPROVE_OPTION ){
				    File selectedFile = FChooser1.getSelectedFile();
				    System.out.println("You have selected " + FChooser1.getSelectedFile().getAbsoluteFile());
				    Preview.setCFont(selectedFile.getAbsoluteFile().toString(),true);
				    loadedFont.setText("<html>Using Font: " + FontManager.GetFont().getName()
							   + "<br>" + "Font Size: "+ FontManager.GetFont().getSize() + "<br>" 
							   + "Custom TTF Font: Yes"+ "<html>");
				    FontChooser.setSelectedIndex(-1);
				    System.out.println(selectedFile.getParent());
				    System.out.println(selectedFile.getName());
				} else {
				    System.out.println(JFileChooser.CANCEL_OPTION);
				}
			}
		});
		
		JMenuItem ExportMenu = new JMenu("Export");
		ExportMenu.setToolTipText("Export Font Atlas Texture.");
        JMenuItem ExpBin = new JMenuItem("To Binary Data...");
        ExpBin.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				JFileChooser FChooser = new JFileChooser();
				FChooser.setDialogTitle("Export to binary data...");
				FChooser.setAcceptAllFileFilterUsed(true);
				//FChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int stats;
				stats = FChooser.showSaveDialog(null);
				if( stats == JFileChooser.APPROVE_OPTION ){
					if( FChooser.getSelectedFile().exists() ){
						int Question;
						Question = JOptionPane.showConfirmDialog(FChooser.getParent(),
			                    "File exists, overwrite?", "File exists",
			                    JOptionPane.YES_NO_OPTION);
						
						if( Question == JOptionPane.YES_OPTION ){
						    System.out.println("Dir: " + FChooser.getSelectedFile().getAbsolutePath());
						    //if( !FChooser2.getSelectedFile().canWrite() )
						    Preview.savetoBin(FChooser.getSelectedFile().getAbsolutePath());
						} else {
							FChooser.showSaveDialog(null);
						}
					}
				    System.out.println("Dir: " + FChooser.getSelectedFile().getAbsolutePath());
				    Preview.savetoBin(FChooser.getSelectedFile().getAbsolutePath());
				} else {
				    System.out.println(JFileChooser.CANCEL_OPTION);
				}
			}
		});
        JMenuItem ExpPlain = new JMenuItem("To Plain Text...");
        ExpPlain.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				JFileChooser FChooser2 = new JFileChooser();
				FChooser2.setDialogTitle("");
				FChooser2.setAcceptAllFileFilterUsed(true);
				//FChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int stats;
				stats = FChooser2.showSaveDialog(null);
				if( stats == JFileChooser.APPROVE_OPTION ){
					if( FChooser2.getSelectedFile().exists() ){
						int Question;
						Question = JOptionPane.showConfirmDialog(FChooser2.getParent(),
			                    "File exists, overwrite?", "File exists",
			                    JOptionPane.YES_NO_OPTION);
						
						if( Question == JOptionPane.YES_OPTION ){
						    System.out.println("Dir: " + FChooser2.getSelectedFile().getAbsolutePath());
						    //if( !FChooser2.getSelectedFile().canWrite() )
						    Preview.savetoText(FChooser2.getSelectedFile().getAbsolutePath());
						} else {
							FChooser2.showSaveDialog(null);
						}
					}
				    System.out.println("Dir: " + FChooser2.getSelectedFile().getAbsolutePath());
				    Preview.savetoText(FChooser2.getSelectedFile().getAbsolutePath());
				} else {
				    System.out.println(JFileChooser.CANCEL_OPTION);
				}
			}
		});
        
        JMenuItem ExpImageMenu = new JMenu("To Image...");
        JMenuItem ExptoPng = new JMenuItem(".png");
        ExptoPng.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				JFileChooser FChooser3 = new JFileChooser();
				FChooser3.setDialogTitle("Export to png image file...");
				FChooser3.setAcceptAllFileFilterUsed(false);
				FChooser3.setFileFilter(new GenericFileFilter(".png", "png Image file"));

				int stats;
				stats = FChooser3.showSaveDialog(null);
				if( stats == JFileChooser.APPROVE_OPTION ){
					if( FChooser3.getSelectedFile().exists() ){
						int Question;
						Question = JOptionPane.showConfirmDialog(FChooser3.getParent(),
			                    "File exists, overwrite?", "File exists",
			                    JOptionPane.YES_NO_OPTION);
						
						if( Question == JOptionPane.YES_OPTION ){
						    System.out.println("Dir: " + FChooser3.getSelectedFile().getAbsolutePath());
						    //if( !FChooser2.getSelectedFile().canWrite() )
						    Preview.saveImage(FChooser3.getSelectedFile().getAbsolutePath(),"png");
						} else {
							FChooser3.showSaveDialog(null);
						}
					}
				    System.out.println("Dir: " + FChooser3.getSelectedFile().getAbsolutePath());
				    Preview.saveImage(FChooser3.getSelectedFile().getAbsolutePath(),"png");
				} else {
				    System.out.println(JFileChooser.CANCEL_OPTION);
				}
			}
		});
        /*
        JMenuItem ExptoTGA = new JMenuItem(".tga(24 bpp)");
        ExptoTGA.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				JFileChooser FChooser4 = new JFileChooser();
				FChooser4.setDialogTitle("Export to tga image file...");
				FChooser4.setAcceptAllFileFilterUsed(false);
				FChooser4.setFileFilter(new GenericFileFilter(".tga", "tga 24bpp Image file"));

				int stats;
				stats = FChooser4.showSaveDialog(null);
				if( stats == JFileChooser.APPROVE_OPTION ){
					if( FChooser4.getSelectedFile().exists() ){
						int Question;
						Question = JOptionPane.showConfirmDialog(FChooser4.getParent(),
			                    "File exists, overwrite?", "File exists",
			                    JOptionPane.YES_NO_OPTION);
						
						if( Question == JOptionPane.YES_OPTION ){
						    System.out.println("Dir: " + FChooser4.getSelectedFile().getAbsolutePath());
						    //if( !FChooser2.getSelectedFile().canWrite() )
						    Preview.saveImage(FChooser4.getSelectedFile().getAbsolutePath(),"tga");
						} else {
							FChooser4.showSaveDialog(null);
						}
					}
				    System.out.println("Dir: " + FChooser4.getSelectedFile().getAbsolutePath());
				    Preview.saveImage(FChooser4.getSelectedFile().getAbsolutePath(),"tga");
				} else {
				    System.out.println(JFileChooser.CANCEL_OPTION);
				}
			}
		});
        JMenuItem ExptoTGA2 = new JMenuItem(".tga(32 bpp)");
        
*/
        ExportMenu.add(ExpBin);
        ExportMenu.add(ExpPlain);
        ExportMenu.add(ExpImageMenu);
        ExpImageMenu.add(ExptoPng);
       // ExpImageMenu.add(ExptoTGA);
       // ExpImageMenu.add(ExptoTGA2);
		

		JMenuItem ExitItem = new JMenuItem("Exit");
		ExitItem.setToolTipText("Exit application");
		ExitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }

        });
        

		File.add(ImportItem);
		File.add(ExportMenu);
        File.add(ExitItem);
		MBar.add(File);
		
		//About.
		JMenu Info = new JMenu("?");
		JMenuItem AboutItem = new JMenuItem("About");
		
		Info.add(AboutItem);
		MBar.add(Info);
	}

	public void actionPerformed(ActionEvent e) {
		String te;

		te = (String) CBox.getSelectedItem();
		if (e.getSource() instanceof JComboBox) {
			if (!te.isEmpty()) {
				if (te.contains("256")) {
					// New dimension 256*256;
					System.out.println("256x256");
					//was 257
					Preview.setImage_Size(new Dimension(257, 257));
					Preview.setCellWidth(16);
					Preview.setCellHeight(16);
					FontChooser.setEnabled(true);
					FSizeChooser.setEnabled(true);
					SpinnerRange.setValue(16);
					SpinnerRange2.setValue(16);
					CSpinner.setEnabled(true);
					CSpinner2.setEnabled(true);
					SChar.setEnabled(true);
					showGrid.setEnabled(true);
					button5.setEnabled(true);
					button6.setEnabled(true);
					button7.setEnabled(true);
				} else if (te.contains("512")) {
					System.out.println("512x512");
					Preview.setImage_Size(new Dimension(513, 513));
					Preview.setCellWidth(32);
					Preview.setCellHeight(32);
					FontChooser.setEnabled(true);
					FSizeChooser.setEnabled(true);
					SpinnerRange.setValue(32);
					SpinnerRange2.setValue(32);
					CSpinner.setEnabled(true);
					CSpinner2.setEnabled(true);
					SChar.setEnabled(true);
					showGrid.setEnabled(true);
					button5.setEnabled(true);
					button6.setEnabled(true);
					button7.setEnabled(true);
				} else {

				}
			}
		}
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FontTool ft = new FontTool();
					System.out.println("X=> " + MouseInfo.getPointerInfo().getLocation().getX());
					System.out.println("Y=> " + MouseInfo.getPointerInfo().getLocation().getY());
				//FIXME:Add font chooser combo box according to the imported one.
				ft.setVisible(true);
			}
		});
	}

}
