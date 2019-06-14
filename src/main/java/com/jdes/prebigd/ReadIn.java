package com.jdes.prebigd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.ArrayList;
import java.util.Collections;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// the main method here reads in and accounts for a number for each new line
public class ReadIn {
	private static volatile BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);
	// the word is the key (string), and the value is the number of times that word has appeared! 
	static ConcurrentHashMap<String, AtomicInteger> wordToCounts = new ConcurrentHashMap<>();
	
	public static final int READER_THREADS = 5;
	
	
	// pool of threads for reading files 
	ExecutorService fileReadersPool = Executors.newFixedThreadPool(READER_THREADS);
	
	
	public static final int COUNTER_THREADS = 5;
	
	ExecutorService countersPool = Executors.newFixedThreadPool(COUNTER_THREADS);
	
	private static volatile ArrayList<LinkedBlockingQueue<String>> wordsQueues = new ArrayList<LinkedBlockingQueue<String>>(COUNTER_THREADS);

	
	// this keeps track of when the reading is done! :D 
	AtomicBoolean readingFinished = new AtomicBoolean(false);
	private static AtomicInteger finalCount = new AtomicInteger();
	
	
	private static String path = "/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt";

	
	public static synchronized String[] mapMethod(String line) {
		// map splits each line into its words... 
		// the number of the line is stored in the while loop...
		line = line.toLowerCase();
		String [] words = line.split("\\W");
		
		return words;
	}
	
	

	private void reduce(String words) {
	
			AtomicInteger count = wordToCounts.putIfAbsent(words, new AtomicInteger(0));
			
			if (count == null) {
				// no previous count 
				count = wordToCounts.get(words);
			} 
			
			count.incrementAndGet();
		}
	
	private void createCounterThreads(ReadIn uhhh) {
//		class counterRunnable1 implements Runnable {
//		https://stackoverflow.com/questions/5853167/runnable-with-a-parameter	
//		} 
		
		
		for (int i = 0; i < COUNTER_THREADS; i++) {
			final int counterQueueIndex = i;
			
			uhhh.countersPool.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("Does it get to run()?");
				int amountProcessed = 0;
				
				LinkedBlockingQueue<String> myQueue = wordsQueues.get(counterQueueIndex);
				try {
					while(readingFinished.get() == false || myQueue.isEmpty() == false) {
						
						// this is weird...  isn't it just pulling out one word at a time?
						String words = myQueue.poll(1, TimeUnit.SECONDS);
						
						if (words != null) {
							reduce(words);
							
							amountProcessed++;
						} // if 
					} // while 
						 
					
				} // try
				catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Counter thread "+ counterQueueIndex + " processed " + amountProcessed);
				finalCount.addAndGet(amountProcessed);
				
				
			} // run
			
			});// Runnable
		
		
		
		} // for loop 
	}// createCounterThreads
	
	public String[] createBucket(List<String> synlist) {
		// declaration and initialize String Array 
		String words[] = new String[synlist.size()]; 
		  
	    // ArrayList to Array Conversion 
	    for (int j = 0; j < synlist.size(); j++) { 

	        // Assign each value to String array 
	        words[j] = synlist.get(j); 
	    } 
	    
	    synlist.clear();

	    return words;
		
		
	}
	

	
	
	public void mapFile(final BufferedReader reader, ReadIn uhhh) {
		Runnable mapTask = () -> {
				String line = null;
				
				try {
					MessageDigest md = MessageDigest.getInstance("MD5");
					
				try {
				while (reader.ready()) {
					
					line = reader.readLine();
					String[] words = mapMethod(line);
							
					for (String word: words) {
						int index = -1;
						
						
						byte[] hashInBytes = md.digest(word.getBytes());

						
							if (hashInBytes[0] < -77) {
							index = 0;
							
							}
							else if (hashInBytes[0] < -26) {
							index = 1;
							}
						
							else if (hashInBytes[0] < 25) {
							index = 2;
							}
						
							else if (hashInBytes[0] < 76) {
							index = 3;
							}
						
							else if (hashInBytes[0] <= 127) {
							index = 4;
							}
							
						
						
						// this is genius, also look he is adding individual words! 
						
						wordsQueues.get(index).add(word);
						
					} // other for 
				}
				reader.close(); 
					} catch (IOException e) {
						e.printStackTrace();
					} // catch
				} catch (NoSuchAlgorithmException e) {
					System.out.println(e);
				}
				
				}; // try
				
				
				uhhh.fileReadersPool.submit(mapTask);
			
		} 
		
		
	 
	

	
	public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException {
		
		
		System.out.println("Inside : " + Thread.currentThread().getName());
		
		for (int i = 0; i < COUNTER_THREADS ; ++i) {
		    wordsQueues.add(new LinkedBlockingQueue<String>());
		}
		
		ReadIn uhhh = new ReadIn();
		

		
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		
		// this is where the multithreading shall begin! 
		uhhh.mapFile(reader, uhhh);
		
		uhhh.createCounterThreads(uhhh);
		
		uhhh.fileReadersPool.shutdown();
		uhhh.fileReadersPool.awaitTermination(60, TimeUnit.SECONDS);
		
		System.out.println("Reader threads shutdown.");
		
		uhhh.readingFinished.set(true);
		
		uhhh.countersPool.shutdown();
		uhhh.countersPool.awaitTermination(60, TimeUnit.SECONDS);

		System.out.println("Counter threads shutdown.");
		
		System.out.println("finalPrint inside : " + Thread.currentThread().getName());
		
		for (Entry<String, AtomicInteger> entry : wordToCounts.entrySet()) {
			System.out.println("Word: "+ entry.getKey() + " Count: " + entry.getValue());
		}
		
		System.out.println("There are "+ wordToCounts.size() + " unique words!");
		System.out.println("There were "+ finalCount + " words processed!");
			
		
		 
	}
	
}

