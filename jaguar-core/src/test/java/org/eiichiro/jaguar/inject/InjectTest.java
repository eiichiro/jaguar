package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InjectTest {

	@Test
	public void test() {
		bootstrap();
		install(InjectObject.class, InjectObject2.class, InjectObject3.class,
				InjectInterceptor.class);
		InjectObject injectObject = component(InjectObject.class);
		assertNull(injectObject.injectObject2);
		assertNotNull(injectObject.injectObject22);
		assertNull(injectObject.string);
		InjectObject3 injectObject3 = component(InjectObject3.class);
		assertNotNull(injectObject3.injectObject2);
		assertNull(injectObject3.string);
		InjectInterceptor injectInterceptor = component(InjectInterceptor.class);
		assertNotNull(injectInterceptor.injectObject2);
		assertNull(injectInterceptor.string);
		shutdown();
	}
	
}
