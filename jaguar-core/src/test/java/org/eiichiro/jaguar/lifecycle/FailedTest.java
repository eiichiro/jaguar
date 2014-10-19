package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class FailedTest {

	@Test
	public void test() {
		bootstrap();
		install(Component2.class, Component3.class);
		Component2 component2 = component(Component2.class);
		component(Component3.class);
		shutdown();
		assertThat(component2.lifecycles.size(), is(6));
		assertTrue(component2.lifecycles.contains("Failed"));
		assertTrue(component2.lifecycles.contains("Failed2LifecycleException"));
	}
	
}
