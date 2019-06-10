package com.jdes.prebigd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class CountWords {

//	public static void main(String[] args) throws FileNotFoundException {
		public static int countWords(String path) throws FileNotFoundException {
		int count = 0;
		File file = new File(path);
		FileInputStream inputStream = new FileInputStream(file);
		byte[] bytesArray = new byte[(int)file.length()];
		try {
		inputStream.read(bytesArray);
		String s = new String(bytesArray);
		String [] data = s.split(" ");
		for (int i=0; i<data.length; i++) {
			count++;
		}
		
		System.out.println("Number of words in the file are: "+count);
		inputStream.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return count;
		}
//	}

}
