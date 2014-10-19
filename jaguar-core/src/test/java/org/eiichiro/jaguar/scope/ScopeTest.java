package org.eiichiro.jaguar.scope;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ScopeTest {

	@Test
	public void test() {
		bootstrap();
		install(Scope1Component.class);
		Scope1Component scope1Component = component(Scope1Component.class);
		Scope1Component scope1Object2 = component(Scope1Component.class);
		assertNotSame(scope1Component, scope1Object2);
		shutdown();
	}
	
}
