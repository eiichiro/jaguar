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
		classes.add(Component1.class);
		classes.add(Component2.class);
		install(classes);
		shutdown();
	}

	@Test
	public void testInstallClassOfQArray() {
		bootstrap();
		install(Component1.class, Component2.class);
		shutdown();
	}

	@Test
	public void testInstallClassOfQ() {
		try {
			install(Component1.class);
			fail();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		bootstrap();
		install(Component1.class);
		install(Component2.class);
		shutdown();
		
		bootstrap();
		install(Component4.class);
		Component4 component4 = component(Component4.class);
		install(Component4.class);
		assertSame(component4, component(Component4.class));
		shutdown();
	}

	@Test
	public void testInstalled() {
		try {
			installed(Component1.class);
			fail();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		
		bootstrap();
		assertFalse(installed(Component1.class));
		install(Component1.class);
		assertTrue(installed(Component1.class));
		shutdown();
	}

	@Test
	public void testComponent() {
		bootstrap();
		install(Component1.class);
		install(Component2.class);
		Component2 component2 = component(Component2.class);
		assertNotNull(component2.component1);
		shutdown();
	}

	@Test
	public void testAssemble() {
		bootstrap();
		install(Component1.class);
		install(Component2.class);
		Component3 component3 = Component3.component3();
		Component3 object32 = assemble(component3);
		assertSame(component3, object32);
		assertNotNull(object32.component2);
		assertNotNull(object32.component2.component1);
		shutdown();
	}
	
	@Test
	public void testDump() {
		dump();
		bootstrap();
		dump();
		install(Component1.class);
		install(Component2.class);
		dump();
		shutdown();
		dump();
	}

}
