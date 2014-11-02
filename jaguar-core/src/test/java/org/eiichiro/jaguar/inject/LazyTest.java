package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LazyTest {

	@Before
	public void setUp() throws Exception {
		bootstrap();
	}

	@After
	public void tearDown() throws Exception {
		shutdown();
	}

	@Test
	public void testGet() {
		install(LazyTestComponent.class, LazyTestComponent1.class);
		LazyTestComponent1 component = component(LazyTestComponent1.class);
		assertNotNull(component.lazy);
		LazyTestComponent lazyTestComponent = component.lazy.get();
		assertSame(lazyTestComponent, component.lazy.get());
	}

}
