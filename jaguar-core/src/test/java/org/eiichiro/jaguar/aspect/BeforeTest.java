package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BeforeTest {

	@Test
	public void test() {
		bootstrap();
		install(BeforeObject.class, BeforeInterceptor1.class);
		BeforeObject beforeObject = component(BeforeObject.class);
		beforeObject.method1();
		assertThat(beforeObject.order.size(), is(3));
		assertThat(beforeObject.order.get(0), is("before1"));
		assertThat(beforeObject.order.get(1), is("before2"));
		assertThat(beforeObject.order.get(2), is("method1"));
		shutdown();
		
		bootstrap();
		install(BeforeObject.class, BeforeInterceptor1.class);
		beforeObject = component(BeforeObject.class);
		beforeObject.method2("before-");
		shutdown();
		
		bootstrap();
		install(BeforeObject.class, BeforeInterceptor2.class);
		beforeObject = component(BeforeObject.class);
		beforeObject.method2("before-");
		assertThat(beforeObject.order.size(), is(2));
		assertThat(beforeObject.order.get(0), is("before-before1"));
		assertThat(beforeObject.order.get(1), is("before-method2"));
		shutdown();
		
		bootstrap();
		install(BeforeObject.class, BeforeInterceptor3.class);
		beforeObject = component(BeforeObject.class);
		
		try {
			beforeObject.method2("before-");
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThat(beforeObject.order.size(), is(0));
		shutdown();
	}
	
}
