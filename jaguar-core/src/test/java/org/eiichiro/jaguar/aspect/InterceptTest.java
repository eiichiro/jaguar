package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InterceptTest {

	@Test
	public void test() {
		bootstrap();
		install(Component1.class, Interceptor1.class, Interceptor3.class);
		Component1 component1 = component(Component1.class);
		component1.method1();
		assertThat(component1.order.size(), is(3));
		assertThat(component1.order.get(0), is("before1"));
		assertThat(component1.order.get(1), is("before3"));
		assertThat(component1.order.get(2), is("method1"));
		shutdown();
		
		bootstrap();
		install(Component1.class, Interceptor1.class, Interceptor2.class);
		component1 = component(Component1.class);
		component1.method3();
		assertThat(component1.order.size(), is(3));
		assertThat(component1.order.get(0), is("before1"));
		assertThat(component1.order.get(1), is("before2"));
		assertThat(component1.order.get(2), is("method3"));
		shutdown();
	}
	
}
