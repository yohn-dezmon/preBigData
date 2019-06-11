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
import java.util.concurrent.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;




import java.util.ArrayList;
import java.util.Collections;




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
	
	// queue for adding the words to be counted...
	LinkedBlockingQueue<String[]> wordsQueue = new LinkedBlockingQueue<String[]>();
	
	
	// this keeps track of when the reading is done! :D 
	AtomicBoolean readingFinished = new AtomicBoolean(false);
	
	private static String line;
	private static String path = "/Users/HomeFolder/Desktop/DevPortfolio/UltimateGuideDataEng/ch6.txt";
	
//	static HashMap<Integer, String> mappedLines = new HashMap<>();
	
	public static synchronized String[] mapMethod(String line) {
		// map splits each line into its words... 
		// the number of the line is stored in the while loop...
		line = line.toLowerCase();
		
		String [] words = line.split("\\W");
		
		return words;
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
	private void reduce(String[] words) {
		
		
		// reduce does the COUNTING
		
		for(String word : words) {
			// a get on something that didn't exist in the hashmap previously
			// returns a value of null! (that's why we need the if statements below)
			AtomicInteger count = wordToCounts.putIfAbsent(word, new AtomicInteger(0));
			
			if (count == null) {
				// no previous count 
				count = wordToCounts.get(word);
			} 
			
			count.incrementAndGet();
		}
//		wordsFromQueue.clear();
	}
	
	private void createCounterThreads(ReadIn uhhh) {
		
		Runnable counterRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					while(readingFinished.get() == false || wordsQueue.isEmpty() == false) {
						
						String[] words = wordsQueue.poll(1, TimeUnit.SECONDS);
						
						if (words != null) {
							reduce(words);
						} // if 
					} // while 
						 
					
				} // try
				catch (Exception e) {
					e.printStackTrace();
				}
				
			} // run
		};// Runnable
		
		for (int i = 0; i < COUNTER_THREADS; i++) {
			uhhh.countersPool.submit(counterRunnable);
			
		} // for 
		
	}// createCounterThreads
	
	
	public void mapFile(final BufferedReader reader, ReadIn uhhh) {
		Runnable mapTask = () -> {
				String line = null;
				try {
				while ((line = reader.readLine()) != null) {
					String[] words = mapMethod(line);
					
					wordsQueue.add(words);
				} // while
				reader.close(); 
				} // try
				catch (IOException e) {
					e.printStackTrace();
				} // catch
				
			
		}; // Runnable
		uhhh.fileReadersPool.submit(mapTask);
		
	} // mapFile 
	
//		fileReadersPool.submit(new Runnable() {
//			@Override
//			public void run() {
//			String line = null;	
//			
////			System.out.println("mapTask inside: " + Thread.currentThread().getName());
//			try {
//				while ((line = reader.readLine()) != null) {
//				    
//				
//				mapMethod(line); }
////				placeInQueue(wordsInLine);
//			
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			}
//			
//		};) 
//	}
	
	
	public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException {
		
		
		System.out.println("Inside : " + Thread.currentThread().getName());
		
		ReadIn uhhh = new ReadIn();
		
		
//		List<String> wordsList = new ArrayList<String>();
//		List<String> wordsFromQueue = Collections.synchronizedList(wordsList);
		
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
//		int lineNum;
//			
//			lineNum = 0;
		System.out.println("Counter threads shutdown.");
		
		System.out.println("finalPrint inside : " + Thread.currentThread().getName());
		
		for (Entry<String, AtomicInteger> entry : wordToCounts.entrySet()) {
			System.out.println("Word: "+ entry.getKey() + " Count: " + entry.getValue());
		}
		
		System.out.println("There are "+ wordToCounts.size() + " unique words!");
		
		
	
			
			
//			fileReadersPool.submit(mapTask);
//			executorService.submit(reduceTask);
//			scheduledService.schedule(finalPrint, 5, TimeUnit.SECONDS);
			
				
//				reader.close();
//		
	
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

