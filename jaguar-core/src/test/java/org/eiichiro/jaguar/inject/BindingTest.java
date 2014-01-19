package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BindingTest {

	@Test
	public void test() {
		bootstrap();
		install(BindingObject.class, BindingObject3.class,
				BindingObject4.class, BindingObject5.class,
				BindingObject6.class, BindingObject7.class);
		BindingObject bindingObject = component(BindingObject.class);
		assertNull(bindingObject.bindingObject1);
		assertThat(bindingObject.bindingObject12, is(BindingObject4.class));
		assertNull(bindingObject.bindingObject13);
		assertNull(bindingObject.bindingObject2);
		assertThat(bindingObject.bindingObject22, is(BindingObject6.class));
		assertThat(bindingObject.bindingObject5, is(BindingObject5.class));
		BindingObject7 bindingObject7 = component(BindingObject7.class);
		assertNull(bindingObject7.bindingObject1);
		assertThat(bindingObject7.bindingObject12, is(BindingObject4.class));
		assertNull(bindingObject7.bindingObject13);
		assertNull(bindingObject7.bindingObject2);
		assertThat(bindingObject7.bindingObject22, is(BindingObject6.class));
		assertThat(bindingObject7.bindingObject5, is(BindingObject5.class));
		shutdown();
	}
	
}
