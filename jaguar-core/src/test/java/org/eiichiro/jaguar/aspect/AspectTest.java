package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AspectTest {

	@Test
	public void test() {
		bootstrap();
		install(Component1.class, Aspect4.class);
		Component1 component1 = component(Component1.class);
		component1.method1();
		component1.method2();
		assertThat(component1.order.size(), is(4));
		assertThat(component1.order.get(0), is("before4"));
		assertThat(component1.order.get(1), is("method1"));
		assertThat(component1.order.get(2), is("before4"));
		assertThat(component1.order.get(3), is("method2"));
		shutdown();
	}
	
}
