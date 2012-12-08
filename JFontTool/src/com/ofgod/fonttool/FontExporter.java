package com.ofgod.fonttool;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FontExporter {
	
	File Out;
	FileWriter Fw;
	BufferedWriter BufferPlain;
	DataOutputStream BufferBin;
	boolean Initialized = true;
	boolean useBinary = false;
	
	public void FOpen(String InFont){
		FileOutputStream fw;
		try{
			Out = new File(InFont);
			
			if( Out.exists() ){
				//Ask to overwrite or specify a new name.
				Out.createNewFile();
			}
			
			fw = new FileOutputStream(Out.getAbsoluteFile());
			BufferBin = new DataOutputStream(fw);
			BufferPlain = new BufferedWriter(new FileWriter(Out.getAbsoluteFile()));
			Begin();
			
		} catch ( IOException e ){
			System.out.println(e);
			Initialized = false;
		}

	}
	
	public void Begin() throws IOException{
		
		Byte[] Magic = {'A','F','O','N','T' };
		short Version = 10;
		
		if( !Initialized ){
			System.out.println("Called FontExporter::Begin without initializing it...");
			return;
		}
		
		if( !useBinary ){
			//Plain text file...
			return;
		}
		
		//Write our standard header
		for( int i = 0; i < Magic.length; i++ ){
			BufferBin.writeByte(Magic[i]);
		}
		
		BufferBin.writeShort(Version);
		
		//Buffer.close();
	}
	
	public void writeInt(int Value) throws IOException{
		if( useBinary ){
			System.out.println("Using binary .");
			BufferBin.writeInt(Value);
			return;
		}
		
		BufferPlain.write(Integer.toString(Value));
	}
	
	public void End(boolean Reopen) throws IOException{
		//Amen.
		BufferBin.close();
		BufferPlain.close();
		
		if( Reopen ){
			FOpen(Out.getAbsoluteFile().toString());
		}
	}
	
	public FontExporter(String InFont,boolean Text){
		
		if( !Text ){
			useBinary = true;
		}
		FOpen(InFont);
	}
}
