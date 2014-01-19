package org.eiichiro.jaguar.lifecycle;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class ActivatedTest {

	@Test
	public void test() {
		bootstrap();
		install(Object1.class);
		Object1 object1 = component(Object1.class);
		assertThat(object1.lifecycles.size(), is(4));
		assertTrue(object1.lifecycles.contains("Activated"));
		assertTrue(object1.lifecycles.contains("Activated2"));
		shutdown();
	}
	
}
