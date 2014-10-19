package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class BeforeTest {

	@Test
	public void test() {
		bootstrap();
		install(BeforeComponent.class, BeforeInterceptor1.class);
		BeforeComponent beforeComponent = component(BeforeComponent.class);
		beforeComponent.method1();
		assertThat(beforeComponent.order.size(), is(3));
		assertThat(beforeComponent.order.get(0), is("before1"));
		assertThat(beforeComponent.order.get(1), is("before2"));
		assertThat(beforeComponent.order.get(2), is("method1"));
		shutdown();
		
		bootstrap();
		install(BeforeComponent.class, BeforeInterceptor1.class);
		beforeComponent = component(BeforeComponent.class);
		beforeComponent.method2("before-");
		shutdown();
		
		bootstrap();
		install(BeforeComponent.class, BeforeInterceptor2.class);
		beforeComponent = component(BeforeComponent.class);
		beforeComponent.method2("before-");
		assertThat(beforeComponent.order.size(), is(2));
		assertThat(beforeComponent.order.get(0), is("before-before1"));
		assertThat(beforeComponent.order.get(1), is("before-method2"));
		shutdown();
		
		bootstrap();
		install(BeforeComponent.class, BeforeInterceptor3.class);
		beforeComponent = component(BeforeComponent.class);
		
		try {
			beforeComponent.method2("before-");
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThat(beforeComponent.order.size(), is(0));
		shutdown();
	}
	
}
