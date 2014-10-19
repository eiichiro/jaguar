package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AfterTest {

	@Test
	public void test() {
		bootstrap();
		install(AfterComponent.class, AfterAspect1.class);
		AfterComponent afterComponent = component(AfterComponent.class);
		afterComponent.method1();
		assertThat(afterComponent.order.size(), is(3));
		assertThat(afterComponent.order.get(0), is("method1"));
		
		if (afterComponent.order.get(1).equals("after1")) {
			assertThat(afterComponent.order.get(2), is("after2"));
		} else if (afterComponent.order.get(1).equals("after2")) {
			assertThat(afterComponent.order.get(2), is("after1"));
		} else {
			fail();
		}
		
		shutdown();
		
		bootstrap();
		install(AfterComponent.class, AfterAspect2.class);
		afterComponent = component(AfterComponent.class);
		afterComponent.method2();
		assertThat(afterComponent.order.size(), is(2));
		assertThat(afterComponent.order.get(0), is("method2"));
		assertThat(afterComponent.order.get(1), is("after-after1"));
		shutdown();
		
		bootstrap();
		install(AfterComponent.class, AfterAspect3.class);
		afterComponent = component(AfterComponent.class);
		
		try {
			afterComponent.method2();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThat(afterComponent.order.size(), is(1));
		assertThat(afterComponent.order.get(0), is("method2"));
		shutdown();
	}
	
}
