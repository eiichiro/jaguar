package org.eiichiro.jaguar.aspect;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class ThrowingTest {

	@Test
	public void test() {
		bootstrap();
		install(ThrowingComponent.class, ThrowingAspect1.class);
		ThrowingComponent throwingComponent = component(ThrowingComponent.class);
		throwingComponent.method1();
		assertThat(throwingComponent.order.size(), is(3));
		assertThat(throwingComponent.order.get(0), is("method1"));
		assertThat(throwingComponent.order.get(1), is("throwing1"));
		assertThat(throwingComponent.order.get(2), is("throwing2"));
		shutdown();
		
		bootstrap();
		install(ThrowingComponent.class, ThrowingAspect2.class);
		throwingComponent = component(ThrowingComponent.class);
		throwingComponent.method1();
		assertThat(throwingComponent.order.size(), is(2));
		assertThat(throwingComponent.order.get(0), is("method1"));
		assertThat(throwingComponent.order.get(1), is("exception-throwing1"));
		shutdown();
		
		bootstrap();
		install(ThrowingComponent.class, ThrowingAspect3.class);
		throwingComponent = component(ThrowingComponent.class);
		
		try {
			throwingComponent.method1();
			fail();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThat(throwingComponent.order.size(), is(1));
		assertThat(throwingComponent.order.get(0), is("method1"));
		shutdown();
	}
	
}
