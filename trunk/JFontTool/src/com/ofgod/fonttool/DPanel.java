package com.ofgod.fonttool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


/*
 BufferedImage buf = new BufferedImage(Image_Size.width *2, Image_Size.width *2, BufferedImage.TYPE_INT_ARGB);
 //drawCharacters(buf.getGraphics(), Color.white);
 try {
 //ImageIO.write(Font_Image, "png", new File("/home/adriano/test.png"));
 } catch (IOException e) {
 // TODO Auto-generated catch block
 System.out.println("Failed.");
 e.printStackTrace();
 }
 */
/*
 DPanel:
 A simple JPanel on which you can draw basic shapes or load basic images.
 */
public class DPanel extends JPanel {
	private static final long serialVersionUID = -2023403245235877149L;

	private BufferedImage Font_Image;
	private Dimension Image_Size = new Dimension(257, 257);
	private int cellWidth = 16;
	private int cellHeight = 16;
	private Color BGColor = Color.black;
	private Color CharsColor = Color.white;
	private Color GridColor = Color.red;
	private FontManager  CFont = new FontManager();
	private boolean showGrid;
	private int startingChar = 0;




	public Color getBGColor() {
		return BGColor;
	}

	public void setBGColor(Color bGColor) {
		BGColor = bGColor;
		Update();
	}

	public Color getCharsColor() {
		return CharsColor;
	}

	public void setCharsColor(Color charsColor) {
		CharsColor = charsColor;
		Update();
	}

	public Color getGridColor() {
		return GridColor;
	}
	
	
	public void setGridColor(Color gridColor) {
		GridColor = gridColor;
		Update();
	}
	

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
		Update();
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
		Update();
	}

	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
		Update();
	}

	public Dimension getImage_Size() {
		return Image_Size;
	}

	public void setImage_Size(Dimension image_Size) {
		Image_Size = image_Size;
		Update();
	}

	public Font getCFont() {
		return FontManager.GetFont();
	}

	/*public void setCFont(File cFont) {
		CFont.SetFont(cFont.getAbsolutePath(),true);
		Update();
	}*/
	
	public void setCFont(String Font,boolean TTF){
		CFont.SetFont(Font,TTF);
		Update();
	}
	
	public void setFontSize(float Size){
		CFont.SetSize(Size);
		Update();
	}
	public int getStartingChar() {
		return startingChar;
	}

	public void setStartingChar(int startingChar) {
		this.startingChar = startingChar;
		Update();
	}

	public final void Update() {
		System.out.println("Refreshing grid");
		Draw_Chars();
	}


	public void Draw_Chars() {
		
		Font_Image = new BufferedImage(Image_Size.width - 1, Image_Size.height - 1,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics _g = Font_Image.getGraphics();
		Graphics2D g = (Graphics2D) _g;

		FontMetrics tes = getFontMetrics(FontManager.GetFont());

		g.setFont(FontManager.GetFont());

		System.out.println("using font " + g.getFont());
		
		int x;
		int y;
		int cx;
		int cy;
		int k = 0;
		int row = 16;

		x = y = 0;

		for (char i = (char)startingChar; i < 256; i++, k++) {

			if (!FontManager.GetFont().canDisplay(i)) {
				// FIXME:!
				// continue;
			}

			if (k == row) {
				y += cellHeight;
				x = 0;
				k = 0;
			}

			g.setColor(BGColor);
			// Draw a background rect in order to display white chars.
			g.fillRect(x, y, Image_Size.width, Image_Size.height);
			if (showGrid) {
				g.setColor(GridColor);
				g.drawRect(x, y, cellWidth, cellHeight);
				System.out.println("X;Y => ( " + x + ";" + y + "(" + tes.getHeight() + ")" + ")");
			}

			g.setColor(CharsColor);
			
			cx = (x + 4);
			cy = (y + tes.getHeight() - g.getFontMetrics().getDescent() );
			
			g.drawString(Character.toString(i), cx , cy/* +  tes.getHeight() - g.getFontMetrics().getDescent()*/);

			x += cellWidth;

		}
		g.dispose();
		// drawCharacters(buf.getGraphics(), Color.white);
		/*
		try {
			ImageIO.write(Font_Image, "png", new File("/home/adriano/test.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed.");
			e.printStackTrace();
		}
		*/
		repaint();

	}
	
	public void savetoBin(String OutFile){
		File Out;
		//DataOutputStream BufferBin = null;
        DataFile BufferBin = null;
		Byte[] Magic = {'A','F','O','N','T' };
		Byte Version = 26;
		short RealWidth = (short) (Image_Size.width);
		short RealHeight =(short) (Image_Size.height);

		int x = 0;
		int y = 0;
		int row = 16;
		int k = 0;
		int Char = 0;
		
		if( Font_Image == null ){
			return;
		}
		
		Graphics _g = Font_Image.getGraphics();
		Graphics2D g = (Graphics2D) _g;

		FontMetrics tes = getFontMetrics(FontManager.GetFont());
		
		try{
			Out = new File(OutFile);
			
			if( Out.exists() && !Out.canWrite() ){
				//FIXME:Ask to overwrite or specify a new name.
				System.out.println("Failed...");
				return;
			}
			
			BufferBin = new DataFile(new FileOutputStream(Out.getAbsoluteFile()));
			
			for( int i = 0; i < Magic.length; i++ ){
				BufferBin.write(Magic[i]);
			}
			
			System.out.println("Writing to binary file." + " " + Image_Size.width + Image_Size.height);
			BufferBin.write(Version);
			BufferBin.writeShort((short)(Image_Size.width - 1));
			BufferBin.writeShort((short)(Image_Size.height - 1));
			BufferBin.write(cellWidth);
			BufferBin.write(cellHeight);
			BufferBin.write(FontManager.GetFont().getSize());			
		} catch ( IOException e ){
			System.out.println(e);
		}
		
		for (char i = (char)startingChar; i < 256; i++, k++, Char++) {

			if (k == row) {
				y += cellHeight;
				x = 0;
				k = 0;
			}
			
			try {
				BufferBin.write(Char);
				BufferBin.writeShort((short)x);
				BufferBin.writeShort((short)(y + tes.getHeight() - g.getFontMetrics().getDescent() ) );
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x += cellWidth;

		}
		try {
			System.out.println("Wrote " + BufferBin.Length() + " bytes.");
			BufferBin.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void savetoText(String OutFile){
		int x = 0;
		int y = 0;
		int k = 0;
		int row = 16;
		File Out;
		BufferedWriter BufferPlain = null;

		if( Font_Image == null ){
			System.out.println("Image not ready yet...");
			return;
		}
		Graphics _g = Font_Image.getGraphics();
		Graphics2D g = (Graphics2D) _g;

		FontMetrics tes = getFontMetrics(FontManager.GetFont());
		
		try{
			Out = new File(OutFile);
			
			if( Out.exists() && !Out.canWrite() ){
				//FIXME:Ask to overwrite or specify a new name.
				System.out.println("Aborted...");
				return;//Dam it
			}
			
			BufferPlain = new BufferedWriter(new FileWriter(Out.getAbsoluteFile()));
			BufferPlain.write("imageWidth ");
			BufferPlain.write(Integer.toString(Image_Size.width - 1));
			BufferPlain.write(" imageHeight ");
			BufferPlain.write(Integer.toString(Image_Size.height - 1));
			BufferPlain.write(" cellWidth ");
			BufferPlain.write(Integer.toString(cellWidth));
			BufferPlain.write(" cellHeight ");
			BufferPlain.write(Integer.toString(tes.getHeight()));
			BufferPlain.newLine();
		} catch ( IOException e ){
			System.out.println(e);
		}

		
		for (char i = (char)startingChar; i < 256; i++, k++) {
			// Col count...
			// HACK:
			if (!FontManager.GetFont().canDisplay(i)) {
				// FIXME:!
				// continue;
			}

			if (k == row) {
				y += cellHeight;
				x = 0;
				k = 0;
			}
			
		//	g.drawString(Character.toString(i), x, y + 10);
			try {
				//FIXME: Add to each char his width.
				BufferPlain.write(Integer.toString(i));
				BufferPlain.write(" = ");
				BufferPlain.write(Integer.toString(x));
				BufferPlain.write(';');
				BufferPlain.write(Integer.toString(y));
				BufferPlain.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x += cellWidth;

		}
		try {
			BufferPlain.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//FIXME:Only pngs images works...
	//FIXME:Write TARGA loader/writer class...
	public void saveImage(String OutImage,String format){
		File f;
		
		if( Font_Image == null ){
			//Bump off.
			System.out.println("saveImage:Font Image not loaded yet.");
			return;
		}
		
		try {
			f = new File(OutImage);
			
			if( f.exists() && !f.canWrite() ){
				//FIXME:Ask to overwrite or specify a new name.
				System.out.println("saveImage:Woops...");
				return;//Dam it
			}

			ImageIO.write(Font_Image, format, f);
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed.");
			e.printStackTrace();
		}

	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Assure that we have an image.
		if (Font_Image == null) {
			return;
		}
	    int x = (this.getWidth() - Font_Image.getWidth()) / 2;
	    int y = (this.getHeight() - Font_Image.getHeight()) / 2;

	    g.drawImage(Font_Image, x, y, this);
	}

	public DPanel(String Font, int Size) {
		setPreferredSize(new Dimension(440, 480));
		setBorder(new CompoundBorder(new TitledBorder("Preview"),
				new EmptyBorder(4, 4, 4, 4)));
		// Draw_Grid();
		// setBackground(Color.gray);
	}

}
