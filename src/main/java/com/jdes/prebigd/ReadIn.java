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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
	LinkedBlockingQueue<String[]> wordsQueueAE = new LinkedBlockingQueue<String[]>();
	LinkedBlockingQueue<String[]> wordsQueueFJ = new LinkedBlockingQueue<String[]>();
	LinkedBlockingQueue<String[]> wordsQueueKO = new LinkedBlockingQueue<String[]>();
	LinkedBlockingQueue<String[]> wordsQueuePT = new LinkedBlockingQueue<String[]>();
	LinkedBlockingQueue<String[]> wordsQueueUZ = new LinkedBlockingQueue<String[]>();
	
	private static List<String> listAE = new ArrayList<String>();
	private static List<String> synlistAE = Collections.synchronizedList(listAE);
	private static List<String> listFJ = new ArrayList<String>();
	private static List<String> synlistFJ = Collections.synchronizedList(listFJ);
	private static List<String> listKO = new ArrayList<String>();
	private static List<String> synlistKO = Collections.synchronizedList(listKO);
	private static List<String> listPT = new ArrayList<String>();
	private static List<String> synlistPT = Collections.synchronizedList(listPT);
	private static List<String> listUZ = new ArrayList<String>();
	private static List<String> synlistUZ = Collections.synchronizedList(listUZ);
	
	
	
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

	}
	
	private void createCounterThreads(ReadIn uhhh) {
		
		Runnable counterRunnableAE = new Runnable() {
			@Override
			public void run() {
				try {
					while(readingFinished.get() == false || wordsQueueAE.isEmpty() == false) {
						
						String[] words = wordsQueueAE.poll(1, TimeUnit.SECONDS);
						
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
		
		Runnable counterRunnableFJ = new Runnable() {
			@Override
			public void run() {
				try {
					while(readingFinished.get() == false || wordsQueueFJ.isEmpty() == false) {
						
						String[] words = wordsQueueFJ.poll(1, TimeUnit.SECONDS);
						
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
		
		Runnable counterRunnableKO = new Runnable() {
			@Override
			public void run() {
				try {
					while(readingFinished.get() == false || wordsQueueKO.isEmpty() == false) {
						
						String[] words = wordsQueueKO.poll(1, TimeUnit.SECONDS);
						
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
		
		Runnable counterRunnablePT = new Runnable() {
			@Override
			public void run() {
				try {
					while(readingFinished.get() == false || wordsQueuePT.isEmpty() == false) {
						
						String[] words = wordsQueuePT.poll(1, TimeUnit.SECONDS);
						
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
		
		Runnable counterRunnableUZ = new Runnable() {
			@Override
			public void run() {
				try {
					while(readingFinished.get() == false || wordsQueueUZ.isEmpty() == false) {
						
						String[] words = wordsQueueUZ.poll(1, TimeUnit.SECONDS);
						
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
		
		
		
		
		uhhh.countersPool.submit(counterRunnableAE);
		uhhh.countersPool.submit(counterRunnableFJ);
		uhhh.countersPool.submit(counterRunnableKO);
		uhhh.countersPool.submit(counterRunnablePT);
		uhhh.countersPool.submit(counterRunnableUZ);
			
		
		
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
				while ((line = reader.readLine()) != null) {
					String[] words = mapMethod(line);
					
					String regexAE = "[a-e].*";
					String regexFJ = "[f-j].*";
					String regexKO = "[k-o].*";
					String regexPT = "[p-t].*";
					String regexUZ = "[u-z].*";
					
					
					
					for (String word: words) {
						 
						boolean matchesAE = Pattern.matches(regexAE, word);
						boolean matchesFJ = Pattern.matches(regexFJ, word);
						boolean matchesKO = Pattern.matches(regexKO, word);
						boolean matchesPT = Pattern.matches(regexPT, word);
						boolean matchesUZ = Pattern.matches(regexUZ, word);
						 
						 
						 if (matchesAE == true) {
							 synlistAE.add(word);
//							 wordsQueueAE.add(word);
						 }
						 
						 else if (matchesFJ == true) { 
							 synlistFJ.add(word);
						 }
						 
						 else if (matchesKO == true) { 
							 synlistFJ.add(word);
						 }
						 
						 else if (matchesPT == true) { 
							 synlistFJ.add(word);
						 }
						 else if (matchesUZ == true) { 
							 synlistFJ.add(word);
						 }
						
					}
					
					String[] wordsAE = createBucket(synlistAE);
					wordsQueueAE.add(wordsAE);
					
					String[] wordsFJ = createBucket(synlistFJ);
					wordsQueueFJ.add(wordsFJ);
					
					String[] wordsKO = createBucket(synlistKO);
					wordsQueueKO.add(wordsKO);
					
					String[] wordsPT = createBucket(synlistPT);
					wordsQueuePT.add(wordsPT);
					
					String[] wordsUZ = createBucket(synlistUZ);
					wordsQueueUZ.add(wordsUZ);
					
				} // while
				reader.close(); 
				} // try
				catch (IOException e) {
					e.printStackTrace();
				} // catch
				
			
		}; // Runnable
		uhhh.fileReadersPool.submit(mapTask);
		
	} // mapFile 
	

	
	public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException {
		
		
		System.out.println("Inside : " + Thread.currentThread().getName());
		
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
		
			
		
		 
	}
	
}

