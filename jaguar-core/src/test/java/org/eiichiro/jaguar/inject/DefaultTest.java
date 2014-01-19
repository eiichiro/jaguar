package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class DefaultTest {

	@Test
	public void test() {
		bootstrap();
		install(DefaultObject.class, DefaultObject1.class, DefaultObject2.class, DefaultObject3.class);
		DefaultObject3 defaultObject3 = component(DefaultObject3.class);
		assertThat(defaultObject3.defaultObject.getClass(), is((Object) DefaultObject1.class));
		shutdown();
	}
	
}
