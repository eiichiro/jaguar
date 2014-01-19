package org.eiichiro.jaguar.interceptor;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AfterTest {

	@Test
	public void test() {
		bootstrap();
		install(AfterObject.class, AfterInterceptor1.class);
		AfterObject afterObject = component(AfterObject.class);
		afterObject.method1();
		assertThat(afterObject.order.size(), is(3));
		assertThat(afterObject.order.get(0), is("method1"));
		
		if (afterObject.order.get(1).equals("after1")) {
			assertThat(afterObject.order.get(2), is("after2"));
		} else if (afterObject.order.get(1).equals("after2")) {
			assertThat(afterObject.order.get(2), is("after1"));
		} else {
			fail();
		}
		
		shutdown();
		
		bootstrap();
		install(AfterObject.class, AfterInterceptor2.class);
		afterObject = component(AfterObject.class);
		afterObject.method2();
		assertThat(afterObject.order.size(), is(2));
		assertThat(afterObject.order.get(0), is("method2"));
		assertThat(afterObject.order.get(1), is("after-after1"));
		shutdown();
		
		bootstrap();
		install(AfterObject.class, AfterInterceptor3.class);
		afterObject = component(AfterObject.class);
		
		try {
			afterObject.method2();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThat(afterObject.order.size(), is(1));
		assertThat(afterObject.order.get(0), is("method2"));
		shutdown();
	}
	
}
