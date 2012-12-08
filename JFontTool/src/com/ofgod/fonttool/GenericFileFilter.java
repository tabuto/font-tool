package com.ofgod.fonttool;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class GenericFileFilter extends FileFilter {
	private String Ext;
	private String Description;


	public boolean accept(File f) {
		if( f.isDirectory() ){
			return true;
		} else { 
			if ( f.getName().endsWith(Ext) ) {
				System.out.println(f.getName() + " Should be good...");
				return true;
			}
		}
		
		return false;
	}

	public String getDescription() {
		return Description;
	}
	
	public GenericFileFilter(String Ext,String Description) {
		this.Ext = Ext;
		this.Description = Description;
	}

}
