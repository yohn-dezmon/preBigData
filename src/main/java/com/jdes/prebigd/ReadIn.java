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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;





import java.util.ArrayList;
import java.util.Collections;




// the main method here reads in and accounts for a number for each new line
public class ReadIn {
	private static volatile BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
	// the word is the key (string), and the value is the number of times that word has appeared! 
	static volatile HashMap<String, Integer> wordToCounts = new HashMap<>();
	private static String line;
	private static String path = "/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt";
	
//	static HashMap<Integer, String> mappedLines = new HashMap<>();
	
	public static synchronized void mapMethod(String line) {
		// map splits each line into its words... 
		// the number of the line is stored in the while loop...
		line = line.toLowerCase();
		
		String [] words = line.split("\\W");
		for (String word: words) {
			try { queue.offer(word);
		} catch (ClassCastException e) {
			System.out.println("Error putting words into queue: "+e);
		}
			}
	}
	
	
	
	public static synchronized List<String> takeFromQueue(BlockingQueue<String> queue, List<String> wordsFromQueue) {
		
		
		String wordFromQueue;
		if (queue.isEmpty() == false) {
		for (String word: queue) {
			try { wordFromQueue = queue.take();
			wordsFromQueue.add(wordFromQueue);
			} catch (InterruptedException e) {
				System.out.println("Error extracting words from queue: "+e);
			}
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
		wordsFromQueue.clear();
	}
	
	
	public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException {
		System.out.println("Inside : " + Thread.currentThread().getName());
		
		// I chose two b/c this allows time for the HashMap to be populated
		
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		
		
		
		List<String> wordsList = new ArrayList<String>();
		List<String> wordsFromQueue = Collections.synchronizedList(wordsList);
		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		String firstline = reader.readLine();
		System.out.println(firstline);
//		int lineNum;
//			
//			lineNum = 0;
			
			Runnable mapTask = () -> {
				System.out.println("mapTask inside: " + Thread.currentThread().getName());
				try {
					for (String line = reader.readLine(); line != null; line = reader.readLine()) {
					    
					
					mapMethod(line); }
//					placeInQueue(wordsInLine);
				
				System.out.println("Does it get past the while loop?");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
	
				
			};
			
			Runnable reduceTask = () -> {
				System.out.println("reduceTask inside : " + Thread.currentThread().getName());
				System.out.println("Does it...");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("... wait?");
				
				List<String> wordsFromQueue1 = takeFromQueue(queue, wordsFromQueue);
				reduce(wordsFromQueue1);
			
				
			};
			
			
			executorService.submit(mapTask);
			executorService.submit(reduceTask);
//			scheduledService.schedule(finalPrint, 5, TimeUnit.SECONDS);
			
			
			
			
			
			
			executorService.shutdown();
			executorService.awaitTermination(10, TimeUnit.SECONDS);
			
	        
			
			System.out.println("finalPrint inside : " + Thread.currentThread().getName());
			
			for (Entry<String, Integer> entry : wordToCounts.entrySet()) {
				System.out.println("Word: "+ entry.getKey() + " Count: " + entry.getValue());
			}
			
				CountWords.countWords(path);
				reader.close();
		
	
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
			
			
	
			
			
		
			
		
		 
	}
	
}

