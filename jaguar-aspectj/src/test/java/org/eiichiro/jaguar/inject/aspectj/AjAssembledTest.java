package org.eiichiro.jaguar.inject.aspectj;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AjAssembledTest {

	@Test
	public void test() {
		bootstrap();
		install(AjAssembledTestObject2.class);
		AjAssembledTestObject1 ajAssembledTestObject1 = new AjAssembledTestObject1();
		assertNotNull(ajAssembledTestObject1.ajAssembledTestObject2);
		shutdown();
	}
	
}
