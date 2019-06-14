package com.jdes.prebigd;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.BeforeClass;


public class TestJunit {
	
	@BeforeClass
	public static void onlyOnce() {
		
	}

	@Test
	public void test() {
		String str = "I am done with Junit setup";
		assertEquals("I am done with Junit setup", str);
	}
	
	@Test
	public void shouldHandleCapitals() {
		String[] testArray = new String[2];
		
		testArray[0] = "borg";
		testArray[1] = "worg";
		
		
		assertArrayEquals(testArray, ReadIn.mapMethod("BORG wOrg"));
		
		
	}
	
	@Test
	public void shouldHandleNull() {
		
		String[] testArray = {"bob"};
		
		assertArrayEquals(testArray, ReadIn.mapMethod("   bob   "));
		
	}

}
