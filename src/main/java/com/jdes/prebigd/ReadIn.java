package com.jdes.prebigd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



import java.util.ArrayList;
import java.util.Collections;




// the main method here reads in and accounts for a number for each new line
public class ReadIn {
	private static volatile BlockingQueue<String> queue = new ArrayBlockingQueue<String>(468);
	// the word is the key (string), and the value is the number of times that word has appeared! 
	static volatile HashMap<String, Integer> wordToCounts = new HashMap<>();
	private static String line;
	private static String path = "/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt";
	
//	static HashMap<Integer, String> mappedLines = new HashMap<>();
	
	public static String [] mapMethod(String line) {
		// map splits each line into its words... 
		// the number of the line is stored in the while loop...
		line = line.toLowerCase();
		
		String [] words = line.split("\\W");
		
		
		return words;
	}
	
	public static synchronized void placeInQueue(String[] words) {
		// do I want to put the array in? or each element of the array? 
		for (String word: words) {
		try { queue.put(word);
	} catch (InterruptedException e) {
		System.out.println("Error putting words into queue: "+e);
	}
		}
		
	}
	
	public static synchronized List<String> takeFromQueue(BlockingQueue<String> queue) {
		
		List<String> wordsList = new ArrayList<String>();
		List<String> wordsFromQueue = Collections.synchronizedList(wordsList);
		String wordFromQueue;
		
		for (String word: queue) {
			try { wordFromQueue = queue.take();
			wordsFromQueue.add(wordFromQueue);
			
			} catch (InterruptedException e) {
				System.out.println("Error extracting words from queue: "+e);
			}
		}
		return wordsFromQueue;
	}
	
//	public static void reduce(String[] words) {
	public static void reduce(List<String> wordsFromQueue) {
		
		
		// reduce does the COUNTING
		
		for(String word : wordsFromQueue) {
			// a get on something that didn't exist in the hashmap previously
			// returns a value of null! (that's why we need the if statements below)
			Integer count = wordToCounts.get(word);
			
			if (count == null) {
				// no previous count 
				wordToCounts.put(word, 1);
			} else {
				// increment previous count 
				wordToCounts.put(word, count + 1);
			}
		}
	}
	
	
	public static void main( String[] args ) throws IOException {
		System.out.println("Inside : " + Thread.currentThread().getName());
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
//		int lineNum;
//			
//			lineNum = 0;
			
			Runnable mapTask = () -> {
				System.out.println("mapTask inside: " + Thread.currentThread().getName());
				try {
				while ((line = reader.readLine()) != null) {
					String[] wordsInLine = mapMethod(line);
					placeInQueue(wordsInLine);
					
				}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
			
			Runnable reduceTask = () -> {
				System.out.println("reduceTask inside : " + Thread.currentThread().getName());
				List<String> wordsFromQueue = takeFromQueue(queue);
				reduce(wordsFromQueue);
			
				
			};
			
			Runnable finalPrint = () -> {
				System.out.println("reduceTask inside : " + Thread.currentThread().getName());
				for (Entry<String, Integer> entry : wordToCounts.entrySet()) {
					System.out.println("Word: "+ entry.getKey() + " Count: " + entry.getValue());
				}
				
			};
			
			
			executorService.submit(mapTask);
			executorService.submit(reduceTask);
			executorService.submit(finalPrint);
			
			
			
			
			try {
	        executorService.awaitTermination(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			executorService.shutdown();
//				CountWords.countWords(path);		
		
	
		// I don't think reduce needs to be part of the words...?
				// I think It only needs to be called on the queue...
			
//				reduce(words);
//				
//				lineNum++;
//				System.out.print("Line Num: " + lineNum+" Line: "+line+"\n");
//				
//				
//			}
//			System.out.println("Using hashmap, count: "+ count);
			// count words...
			
			
	
			
			for (Entry<String, Integer> entry : wordToCounts.entrySet()) {
				System.out.println("Word: "+ entry.getKey() + " Count: " + entry.getValue());
			}
		
			reader.close();
		
		 
	}
	
}

