package com.jdes.prebigd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyReadV2 {
	
	public static void main( String[] args ) {
	BufferedReader br = null;
	try {
		// can BufferedReader/FileReader read in .doc files?
		br = new BufferedReader(new FileReader("/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt"));
		String line = null;
		// I was using this to output the given line, but this is not intrinsic to the file reader...
		int linenumb;
		
		// interesting, the while loop is set up in a way I haven't seen before
		while ((line = br.readLine()) != null) {
			String lineNumberStr = line.substring(0, 6);
			try {
			linenumb = Integer.parseInt(lineNumberStr.trim());
			String lineString = line.substring(8);
			
			System.out.print("Line Num:" + linenumb + " Line:"+lineString+"\n");
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
			
		}
		
	} catch (IOException e) {
		System.out.println(e);
		// finally is used when you want to proceed after your 
		// try block has finished, regardless of whether it threw an 
		// exception! :D 
	} finally {
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
}
