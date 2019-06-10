package com.jdes.prebigd.jesse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SimpleV1 {
	public void readFile(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		

		
		String line = null;
		int lineNum;
		
		
		// interesting, the while loop is set up in a way I haven't seen before
		while ((line = reader.readLine()) != null) {
			// wait what is substring?
			String lineNumberStr = line.substring(0, 6);
			lineNum =Integer.parseInt(lineNumberStr.trim());
			
			String lineString = line.substring(8);
			
			System.out.print("Line Num:" + lineNum + " Line:"+lineString+"\n");
			
		}
		reader.close();
	}

	public static void main(String[] args) {
		
		try {
			new SimpleV1().readFile("/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt");
			
		} catch (IOException e) {
			e.printStackTrace();
			// finally is used when you want to proceed after your 
			// try block has finished, regardless of whether it threw an 
			// exception! :D 
		} 

	}

}
