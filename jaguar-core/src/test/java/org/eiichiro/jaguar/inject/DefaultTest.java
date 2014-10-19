package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultTest {

	@Test
	public void test() {
		bootstrap();
		install(DefaultComponent.class, DefaultComponent1.class, DefaultComponent2.class, DefaultComponent3.class);
		DefaultComponent3 defaultComponent3 = component(DefaultComponent3.class);
		assertThat(defaultComponent3.defaultComponent.getClass(), is((Object) DefaultComponent1.class));
		shutdown();
	}
	
}
