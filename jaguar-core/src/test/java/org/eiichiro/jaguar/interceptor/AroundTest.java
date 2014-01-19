package org.eiichiro.jaguar.interceptor;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.ConfigurationException;
import org.junit.Test;

public class AroundTest {

	@Test
	public void test() {
		bootstrap();
		install(AroundObject.class, AroundInterceptor1.class);
		AroundObject aroundObject = component(AroundObject.class);
		aroundObject.method1();
		assertThat(aroundObject.order.size(), is(5));
		
		if (aroundObject.order.get(0).equals("before-around1")) {
			assertThat(aroundObject.order.get(1), is("before-around2"));
			assertThat(aroundObject.order.get(2), is("method1"));
			assertThat(aroundObject.order.get(3), is("after-around2"));
			assertThat(aroundObject.order.get(4), is("after-around1"));
		} else if (aroundObject.order.get(0).equals("before-around2")) {
			assertThat(aroundObject.order.get(1), is("before-around1"));
			assertThat(aroundObject.order.get(2), is("method1"));
			assertThat(aroundObject.order.get(3), is("after-around1"));
			assertThat(aroundObject.order.get(4), is("after-around2"));
		} else {
			fail();
		}
		
		shutdown();
		
		bootstrap();
		install(AroundObject.class, AroundInterceptor2.class);
		aroundObject = component(AroundObject.class);
		String string = aroundObject.method2("around-");
		assertThat(aroundObject.order.size(), is(3));
		assertThat(aroundObject.order.get(0), is("around-before-around1"));
		assertThat(aroundObject.order.get(1), is("around-method2"));
		assertThat(aroundObject.order.get(2), is("around-after-around1"));
		assertThat(string, is("around-around1"));
		shutdown();
		
		bootstrap();
		install(AroundObject.class, AroundInterceptor3.class);
		aroundObject = component(AroundObject.class);
		string = aroundObject.method2("around-");
		assertThat(aroundObject.order.size(), is(1));
		assertThat(aroundObject.order.get(0), is("around1"));
		assertThat(string, is("around1"));
		shutdown();
		
		bootstrap();
		install(AroundObject.class, AroundInterceptor4.class);
		aroundObject = component(AroundObject.class);
		
		try {
			aroundObject.method1();
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		shutdown();
		
		bootstrap();
		install(AroundObject.class, AroundInterceptor5.class);
		aroundObject = component(AroundObject.class);
		
		try {
			aroundObject.method1();
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		
		shutdown();
	}
	
}
