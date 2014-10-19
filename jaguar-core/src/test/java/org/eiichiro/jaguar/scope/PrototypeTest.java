package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class PrototypeTest {

	@Test
	public void test() {
		bootstrap();
		install(PrototypeComponent.class);
		PrototypeComponent prototypeComponent = component(PrototypeComponent.class);
		PrototypeComponent prototypeObject2 = component(PrototypeComponent.class);
		assertNotSame(prototypeComponent, prototypeObject2);
		shutdown();
	}
	
}
