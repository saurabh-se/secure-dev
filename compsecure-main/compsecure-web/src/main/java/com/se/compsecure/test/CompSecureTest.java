package com.se.compsecure.test;

public class CompSecureTest {
	
	public static void main(String[] args) {
		CompSecureTest compSecureTest = new CompSecureTest();
		compSecureTest.test();
	}

	private void test() {
		String testing = "BearerABCD";
		
		System.out.println(testing.startsWith("Bearer"));
		
	}

}
