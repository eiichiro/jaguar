package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.ConfigurationException;
import org.junit.Test;

public class AroundTest {

	@Test
	public void test() {
		bootstrap();
		install(AroundComponent.class, AroundInterceptor1.class);
		AroundComponent aroundComponent = component(AroundComponent.class);
		aroundComponent.method1();
		assertThat(aroundComponent.order.size(), is(5));
		
		if (aroundComponent.order.get(0).equals("before-around1")) {
			assertThat(aroundComponent.order.get(1), is("before-around2"));
			assertThat(aroundComponent.order.get(2), is("method1"));
			assertThat(aroundComponent.order.get(3), is("after-around2"));
			assertThat(aroundComponent.order.get(4), is("after-around1"));
		} else if (aroundComponent.order.get(0).equals("before-around2")) {
			assertThat(aroundComponent.order.get(1), is("before-around1"));
			assertThat(aroundComponent.order.get(2), is("method1"));
			assertThat(aroundComponent.order.get(3), is("after-around1"));
			assertThat(aroundComponent.order.get(4), is("after-around2"));
		} else {
			fail();
		}
		
		shutdown();
		
		bootstrap();
		install(AroundComponent.class, AroundInterceptor2.class);
		aroundComponent = component(AroundComponent.class);
		String string = aroundComponent.method2("around-");
		assertThat(aroundComponent.order.size(), is(3));
		assertThat(aroundComponent.order.get(0), is("around-before-around1"));
		assertThat(aroundComponent.order.get(1), is("around-method2"));
		assertThat(aroundComponent.order.get(2), is("around-after-around1"));
		assertThat(string, is("around-around1"));
		shutdown();
		
		bootstrap();
		install(AroundComponent.class, AroundInterceptor3.class);
		aroundComponent = component(AroundComponent.class);
		string = aroundComponent.method2("around-");
		assertThat(aroundComponent.order.size(), is(1));
		assertThat(aroundComponent.order.get(0), is("around1"));
		assertThat(string, is("around1"));
		shutdown();
		
		bootstrap();
		install(AroundComponent.class, AroundInterceptor4.class);
		aroundComponent = component(AroundComponent.class);
		
		try {
			aroundComponent.method1();
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		shutdown();
		
		bootstrap();
		install(AroundComponent.class, AroundInterceptor5.class);
		aroundComponent = component(AroundComponent.class);
		
		try {
			aroundComponent.method1();
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		shutdown();
	}
	
}
