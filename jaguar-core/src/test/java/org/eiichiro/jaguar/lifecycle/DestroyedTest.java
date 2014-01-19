package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class DestroyedTest {

	@Test
	public void test() {
		bootstrap();
		install(Object2.class);
		Object2 object2 = component(Object2.class);
		shutdown();
		assertThat(object2.lifecycles.size(), is(6));
		assertTrue(object2.lifecycles.contains("Destroyed"));
		assertTrue(object2.lifecycles.contains("Destroyed2"));
	}
	
}
