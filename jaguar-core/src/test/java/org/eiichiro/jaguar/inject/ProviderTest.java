package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProviderTest {

	@Before
	public void setUp() throws Exception {
		bootstrap();
	}

	@After
	public void tearDown() throws Exception {
		shutdown();
	}

	@Test
	public void testProvide() {
		install(ProviderTestComponent1.class, ProviderTestComponent2.class, ProviderTestComponentProvider.class);
		ProviderTestComponent1 component1 = component(ProviderTestComponent1.class);
		ProviderTestComponent2 component2 = component(ProviderTestComponent2.class);
		assertThat(component1.component.value, is(""));
		assertThat(component1.component2.value, is("value1"));
		assertThat(component2.component.value, is(""));
		assertThat(component2.component2.value, is("value2"));
		assertSame(component1.component, component2.component);
	}

}
