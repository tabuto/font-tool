package com.ofgod.fonttool;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TTFFilter extends FileFilter {

	
	public TTFFilter() {
		//Costruttore vuoto
	}
	public boolean accept(File f) {
		
		if( f.isDirectory() ){
			return true;
		} else {
			if( f.getName().endsWith("ttf") ){
				System.out.println(f.getName() + " Should be good...");
				return true;
			}
		}
		
		return false;
	}

	public String getDescription() {
		return "Open a .ttf font file";
	}


}
