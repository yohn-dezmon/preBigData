package com.jdes.prebigd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadInV2 {
	
	public void readFile(String path) throws IOException {
		BufferedReader br = null;
		// can BufferedReader/FileReader read in .doc files?
		br = new BufferedReader(new FileReader(path));
		String line = null;
		int linenumb;
		
		
		// interesting, the while loop is set up in a way I haven't seen before
		while ((line = br.readLine()) != null) {
			// wait what is substring?
			String lineNumberStr = line.substring(0, 6);
			linenumb =Integer.parseInt(lineNumberStr.trim());
			
			String lineString = line.substring(8);
			
			System.out.print("Line Num:" + linenumb + " Line:"+lineString+"\n");
			
		}
		br.close();
	}

	public static void main(String[] args) {
		
		try {
			new ReadInV2().readFile("/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt");
			
		} catch (IOException e) {
			e.printStackTrace();
			// finally is used when you want to proceed after your 
			// try block has finished, regardless of whether it threw an 
			// exception! :D 
		} 

	}

}
