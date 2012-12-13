package com.ofgod.fonttool;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Add comment for javadoc to this class
 * @author user
 *
 */
public class TTFFilter extends FileFilter {

	
	public TTFFilter() {
		//questo è un costruttore vuoto
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
