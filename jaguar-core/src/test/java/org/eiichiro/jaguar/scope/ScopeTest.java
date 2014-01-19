package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeTest {

	@Test
	public void test() {
		bootstrap();
		install(Scope1Object.class);
		Scope1Object scope1Object = component(Scope1Object.class);
		Scope1Object scope1Object2 = component(Scope1Object.class);
		assertNotSame(scope1Object, scope1Object2);
		shutdown();
	}
	
}
