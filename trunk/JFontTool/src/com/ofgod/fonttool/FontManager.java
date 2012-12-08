package com.ofgod.fonttool;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;

public class FontManager {

	// Our font file handler.
	private InputStream F_File;

	private String FName = null;
	private static Font DFont = null;
	private static Font TTFFont = null;
	private float Size = 12f;
	private static boolean IsLoadedasTTF;


	public static Font GetFont(){
		return IsLoadedasTTF ? TTFFont : DFont;
	}
	
	public void SetFont(String InFont,boolean TTF){
		this.FName = InFont;
		if( TTF ){
			System.out.println("Loading as TTF...");
			LoadTTFFont();
		} else {
			System.out.println("Loading as Plain...");
			LoadFont();
		}
	}
	
	public void SetSize(float Size){
		this.Size = Size;
		if( IsLoadedasTTF ){
			LoadTTFFont();
		} else {
			LoadFont();
		}
	}
	
	private void LoadTTFFont(){
		
		if (FName.isEmpty()) {
			System.out.println("Invalid font file.");
			//Invalid ttf...keep using standard one.
			Init();
			return;
		}
		try {
			//Import the font - As long as it exists and it is valid - .
			//F_File = this.getClass().getResourceAsStream(InFont);
				System.out.println("Trying to create font("+ Size + ")..." + FName);
				F_File = new FileInputStream(FName);
				DFont = Font.createFont(Font.TRUETYPE_FONT, F_File);
				TTFFont = DFont.deriveFont(Size);
				System.out.println("Loaded:" + TTFFont);
				IsLoadedasTTF = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't load " + FName +  " Using default...");
			Init();
			IsLoadedasTTF = false;
		}
	}
	
	private void LoadFont(){
		
		if (FName.isEmpty()) {
			System.out.println("Invalid font file.");
			Init();
			return;
		}
		try {
				DFont = new Font(FName,Font.PLAIN, (int)Size);
				DFont = DFont.deriveFont(Size);
				IsLoadedasTTF = false;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't load " + FName +  " Using default...");
			Init();
			IsLoadedasTTF = false;
		}
	}
	
	private void Init(){
		DFont = new Font("Arial",Font.PLAIN,(int)Size);
		FName = "Arial";
		IsLoadedasTTF = false;
	}
	
	public FontManager(){
		Init();
	}
	public FontManager(String InFont,int Size,boolean asTTF) {
		System.out.println("Loading font " + InFont);
		this.FName = InFont;
		this.Size = Size;
		SetFont(InFont, asTTF);
	}


}
