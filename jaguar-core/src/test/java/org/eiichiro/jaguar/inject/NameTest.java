package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class NameTest {

	@Test
	public void test() {
		bootstrap();
		install(NameObject.class, NameObject2.class, NameObject3.class);
		NameObject nameObject = component(NameObject.class);
		assertThat(nameObject.nameObject1, is(NameObject2.class));
		assertNull(nameObject.nameObject12);
		shutdown();
	}
	
}
