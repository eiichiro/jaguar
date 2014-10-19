package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InjectTest {

	@Test
	public void test() {
		bootstrap();
		install(InjectComponent.class, InjectComponent2.class, InjectComponent3.class,
				InjectAspect.class);
		InjectComponent injectComponent = component(InjectComponent.class);
		assertNull(injectComponent.injectComponent2);
		assertNotNull(injectComponent.injectObject22);
		assertNull(injectComponent.string);
		InjectComponent3 injectComponent3 = component(InjectComponent3.class);
		assertNotNull(injectComponent3.injectComponent2);
		assertNull(injectComponent3.string);
		InjectAspect injectAspect = component(InjectAspect.class);
		assertNotNull(injectAspect.injectComponent2);
		assertNull(injectAspect.string);
		shutdown();
	}
	
}
