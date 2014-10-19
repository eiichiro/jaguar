package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class ConstructedTest {

	@Test
	public void test() {
		bootstrap();
		install(Component1.class);
		Component1 component1 = component(Component1.class);
		assertThat(component1.lifecycles.size(), is(4));
		assertTrue(component1.lifecycles.contains("Constructed"));
		assertTrue(component1.lifecycles.contains("Constructed2"));
		shutdown();
	}

}
