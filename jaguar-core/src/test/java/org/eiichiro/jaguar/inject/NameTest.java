package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class NameTest {

	@Test
	public void test() {
		bootstrap();
		install(NameComponent.class, NameComponent2.class, NameComponent3.class);
		NameComponent nameComponent = component(NameComponent.class);
		assertThat(nameComponent.nameComponent1, is(NameComponent2.class));
		assertNull(nameComponent.nameObject12);
		shutdown();
	}
	
}
