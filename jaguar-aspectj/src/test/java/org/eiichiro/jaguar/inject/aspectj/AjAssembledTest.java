package org.eiichiro.jaguar.inject.aspectj;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AjAssembledTest {

	@Test
	public void test() {
		bootstrap();
		install(AjAssembledTestComponent2.class);
		AjAssembledTestComponent1 ajAssembledTestComponent1 = new AjAssembledTestComponent1();
		assertNotNull(ajAssembledTestComponent1.ajAssembledTestComponent2);
		shutdown();
	}
	
}
