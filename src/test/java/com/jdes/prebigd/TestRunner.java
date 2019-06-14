package com.jdes.prebigd;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;



public class TestRunner {
	
	public static void main(String[] args) {
		
		Result mapResult = JUnitCore.runClasses(TestMapper.class);
		for (Failure failure : mapResult.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("mapResult=="+mapResult.wasSuccessful());
	
	
	Result reduceResult = JUnitCore.runClasses(TestReducer.class);
	for (Failure failure : reduceResult.getFailures()) {
		System.out.println(failure.toString());
	}
	System.out.println("reduceResult=="+reduceResult.wasSuccessful());
}
}



