package com.ofgod.fonttool;

import java.io.*;  
import java.nio.*;  

/*
 * Helper class for creating binary file.
*/
public class DataFile extends FilterOutputStream {
      protected int Wrote = 0;
  
      public int Length(){
    	  return Wrote;
      }
      
      public void write(Byte value) throws IOException {
          ByteBuffer buffer = ByteBuffer.allocate(1).order(ByteOrder.nativeOrder());  
          buffer.put(value);  
          out.write(buffer.array());
          Wrote += 1; 
      }
      public void writeShort(short value) throws IOException {  
        ByteBuffer buffer = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder());  
        buffer.putShort(value);  
        out.write(buffer.array());
        Wrote += 2;
      }  
      
      public void writeInt(int value) throws IOException {  
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder());  
        buffer.putInt(value);  
        out.write(buffer.array()); 
        Wrote += 4;
      }  
      
      public void writeLong(long value) throws IOException {  
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder());  
        buffer.putLong(value);  
        out.write(buffer.array());
        Wrote += 8;
      }
      
      public DataFile(OutputStream out) {  
          super(out);  
        }  
}
