package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class ThrowingTest {

	@Test
	public void test() {
		bootstrap();
		install(ThrowingObject.class, ThrowingInterceptor1.class);
		ThrowingObject throwingObject = component(ThrowingObject.class);
		throwingObject.method1();
		assertThat(throwingObject.order.size(), is(3));
		assertThat(throwingObject.order.get(0), is("method1"));
		assertThat(throwingObject.order.get(1), is("throwing1"));
		assertThat(throwingObject.order.get(2), is("throwing2"));
		shutdown();
		
		bootstrap();
		install(ThrowingObject.class, ThrowingInterceptor2.class);
		throwingObject = component(ThrowingObject.class);
		throwingObject.method1();
		assertThat(throwingObject.order.size(), is(2));
		assertThat(throwingObject.order.get(0), is("method1"));
		assertThat(throwingObject.order.get(1), is("exception-throwing1"));
		shutdown();
		
		bootstrap();
		install(ThrowingObject.class, ThrowingInterceptor3.class);
		throwingObject = component(ThrowingObject.class);
		
		try {
			throwingObject.method1();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThat(throwingObject.order.size(), is(1));
		assertThat(throwingObject.order.get(0), is("method1"));
		shutdown();
	}
	
}
