package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PrototypeTest {

	@Test
	public void test() {
		bootstrap();
		install(PrototypeObject.class);
		PrototypeObject prototypeObject = component(PrototypeObject.class);
		PrototypeObject prototypeObject2 = component(PrototypeObject.class);
		assertNotSame(prototypeObject, prototypeObject2);
		shutdown();
	}
	
}
