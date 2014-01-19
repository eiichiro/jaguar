package org.eiichiro.jaguar.interceptor;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InterceptTest {

	@Test
	public void test() {
		bootstrap();
		install(Object1.class, Interceptor1.class, Interceptor3.class);
		Object1 object1 = component(Object1.class);
		object1.method1();
		assertThat(object1.order.size(), is(3));
		assertThat(object1.order.get(0), is("before1"));
		assertThat(object1.order.get(1), is("before3"));
		assertThat(object1.order.get(2), is("method1"));
		shutdown();
		
		bootstrap();
		install(Object1.class, Interceptor1.class, Interceptor2.class);
		object1 = component(Object1.class);
		object1.method3();
		assertThat(object1.order.size(), is(3));
		assertThat(object1.order.get(0), is("before1"));
		assertThat(object1.order.get(1), is("before2"));
		assertThat(object1.order.get(2), is("method3"));
		shutdown();
	}
	
}
