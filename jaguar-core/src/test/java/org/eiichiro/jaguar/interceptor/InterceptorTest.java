package org.eiichiro.jaguar.interceptor;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class InterceptorTest {

	@Test
	public void test() {
		bootstrap();
		install(Object1.class, Interceptor4.class);
		Object1 object1 = component(Object1.class);
		object1.method1();
		object1.method2();
		assertThat(object1.order.size(), is(4));
		assertThat(object1.order.get(0), is("before4"));
		assertThat(object1.order.get(1), is("method1"));
		assertThat(object1.order.get(2), is("before4"));
		assertThat(object1.order.get(3), is("method2"));
		shutdown();
	}
	
}
