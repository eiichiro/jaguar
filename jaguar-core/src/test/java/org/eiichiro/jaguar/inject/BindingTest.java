package org.eiichiro.jaguar.inject;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BindingTest {

	@Test
	public void test() {
		bootstrap();
		install(BindingComponent.class, BindingComponent3.class,
				BindingComponent4.class, BindingComponent5.class,
				BindingComponent6.class, BindingComponent7.class);
		BindingComponent bindingComponent = component(BindingComponent.class);
		assertNull(bindingComponent.bindingComponent1);
		assertThat(bindingComponent.bindingObject12, is(BindingComponent4.class));
		assertNull(bindingComponent.bindingObject13);
		assertNull(bindingComponent.bindingComponent2);
		assertThat(bindingComponent.bindingObject22, is(BindingComponent6.class));
		assertThat(bindingComponent.bindingComponent5, is(BindingComponent5.class));
		BindingComponent7 bindingComponent7 = component(BindingComponent7.class);
		assertNull(bindingComponent7.bindingComponent1);
		assertThat(bindingComponent7.bindingObject12, is(BindingComponent4.class));
		assertNull(bindingComponent7.bindingObject13);
		assertNull(bindingComponent7.bindingComponent2);
		assertThat(bindingComponent7.bindingObject22, is(BindingComponent6.class));
		assertThat(bindingComponent7.bindingComponent5, is(BindingComponent5.class));
		shutdown();
	}
	
}
