package com.flyjava.data;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class TestDataController {
	@Value("${TEMP_UPPER_LIMIT}")
	private float TEMP_UPPER_LIMIT;
	
	@Value("${TEMP_LOWER_LIMIT}")
	private float TEMP_LOWER_LIMIT;
	
	@Test
	public void testTemp(){
		
	}
}
