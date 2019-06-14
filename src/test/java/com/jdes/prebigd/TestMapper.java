package com.jdes.prebigd;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.BeforeClass;


public class TestMapper {
	
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
	public void shouldHandleNonWords() {
		// ok this failed, the reason being that right now the string is being
		// split off of white spaces! 
		// this is good for the most part, but isn't good for when there is > 1 white space
		
		String[] testArray = {"bob","jennifer"};
		
		// two spaces after jennifer...
		assertArrayEquals(testArray, ReadIn.mapMethod("bob jennifer - - "));
		assertArrayEquals(testArray, ReadIn.mapMethod("bob jennifer  "));
		assertArrayEquals(testArray, ReadIn.mapMethod("bob jennifer \n\n"));
		assertArrayEquals(testArray, ReadIn.mapMethod("bob jennifer *"));
		assertArrayEquals(testArray, ReadIn.mapMethod("* bob jennifer"));
	}
	
	
	

}
