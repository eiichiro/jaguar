package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class DestroyedTest {

	@Test
	public void test() {
		bootstrap();
		install(Component2.class);
		Component2 component2 = component(Component2.class);
		shutdown();
		assertThat(component2.lifecycles.size(), is(6));
		assertTrue(component2.lifecycles.contains("Destroyed"));
		assertTrue(component2.lifecycles.contains("Destroyed2"));
	}
	
}
