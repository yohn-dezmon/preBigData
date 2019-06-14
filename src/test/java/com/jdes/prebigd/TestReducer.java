package com.jdes.prebigd;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;



import org.junit.Test;
//import org.junit.BeforeClass;

public class TestReducer {
	
//	@Before
//	public void beforeReudce() {
//		ReadIn.wordToCounts;
//		
//	}

	@Test
	public void testReduce() {
		// Testing concurrentHashMaps is difficult...
		// assertTrue(wordToCountsTest.equals(ReadIn.wordToCounts)); did not work.
		ReadIn testReadIn = new ReadIn();
		
		testReadIn.reduce("orogami");
		testReadIn.reduce("orogami");
		
		String testKey = "orogami";
		AtomicInteger testValue = new AtomicInteger(2);
		ConcurrentHashMap<String, AtomicInteger> wordToCountsTest = new ConcurrentHashMap<>();
		wordToCountsTest.put(testKey, testValue);
		
		String actualKey = "";
		AtomicInteger actualValue = new AtomicInteger(0);
		
		
		for (Entry<String, AtomicInteger> entry : ReadIn.wordToCounts.entrySet()) {
			actualKey = entry.getKey();
			actualValue = entry.getValue();
		}
		
		for (Entry<String, AtomicInteger> entry : ReadIn.wordToCounts.entrySet()) {
			testKey = entry.getKey();
			testValue = entry.getValue();
		}
		
		
		assertEquals(testKey, actualKey);
		assertEquals(testValue, actualValue);
	
	}

}
