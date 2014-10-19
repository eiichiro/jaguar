package org.eiichiro.jaguar.validation;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.validation.ViolationException;
import org.junit.Test;

public class RequiredTest {

	@Test
	public void test() {
		bootstrap();
		install(Component1.class, Component2.class, Component3.class);
		component(Component1.class);
		
		try {
			component(Component2.class);
			fail();
		} catch (ViolationException e) {
			e.printStackTrace();
			assertThat(e.getMessage(), is("Constraint violation: Field [" 
					+ "org.eiichiro.jaguar.validation.Component3 " 
					+ "org.eiichiro.jaguar.validation.Component2.component3]" 
					+ "'s value [null] is invalid for " 
					+ "[@org.eiichiro.jaguar.validation.Required()] constraint"));
		}
		
		shutdown();
	}
	
}
