package org.eiichiro.jaguar;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.deployment.Development;
import org.eiichiro.jaguar.deployment.Emergency;
import org.eiichiro.jaguar.deployment.Testing;
import org.junit.Test;

public class JaguarTest {

	@Test
	public void testDevelopmentClassOfQ() {
		deployment(Development.class);
		bootstrap();
		deployment(Testing.class);
		shutdown();
		deployment(Emergency.class);
	}

	@Test
	public void testDevelopment() {
		deployment(Testing.class);
		assertThat(deployment(), is((Object) Testing.class));
	}

	@Test
	public void testBootstrap() {
		bootstrap();
		bootstrap();	// Just ignored.
		shutdown();
	}

	@Test
	public void testShutdown() {
		shutdown();		// Just ignored.
		bootstrap();
		shutdown();
	}

	@Test
	public void testInstallSetOfClassOfQ() {
		bootstrap();
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(Object1.class);
		classes.add(Object2.class);
		install(classes);
		shutdown();
	}

	@Test
	public void testInstallClassOfQArray() {
		bootstrap();
		install(Object1.class, Object2.class);
		shutdown();
	}

	@Test
	public void testInstallClassOfQ() {
		try {
			install(Object1.class);
			fail();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		bootstrap();
		install(Object1.class);
		install(Object2.class);
		shutdown();
		
		bootstrap();
		install(Object4.class);
		Object4 object4 = component(Object4.class);
		install(Object4.class);
		assertSame(object4, component(Object4.class));
		shutdown();
	}

	@Test
	public void testInstalled() {
		try {
			installed(Object1.class);
			fail();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		bootstrap();
		assertFalse(installed(Object1.class));
		install(Object1.class);
		assertTrue(installed(Object1.class));
		shutdown();
	}

	@Test
	public void testComponent() {
		bootstrap();
		install(Object1.class);
		install(Object2.class);
		Object2 object2 = component(Object2.class);
		assertNotNull(object2.object1);
		shutdown();
	}

	@Test
	public void testAssemble() {
		bootstrap();
		install(Object1.class);
		install(Object2.class);
		Object3 object3 = Object3.object3();
		Object3 object32 = assemble(object3);
		assertSame(object3, object32);
		assertNotNull(object32.object2);
		assertNotNull(object32.object2.object1);
		shutdown();
	}
	
	@Test
	public void testDump() {
		dump();
		bootstrap();
		dump();
		install(Object1.class);
		install(Object2.class);
		dump();
		shutdown();
		dump();
	}

}
