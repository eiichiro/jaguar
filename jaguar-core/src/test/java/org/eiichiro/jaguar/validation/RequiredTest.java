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
		install(Object1.class, Object2.class, Object3.class);
		component(Object1.class);
		
		try {
			component(Object2.class);
			fail();
		} catch (ViolationException e) {
			e.printStackTrace();
			assertThat(e.getMessage(), is("Constraint violation: Field [" 
					+ "org.eiichiro.jaguar.validation.Object3 " 
					+ "org.eiichiro.jaguar.validation.Object2.object3]" 
					+ "'s value [null] is invalid for " 
					+ "[@org.eiichiro.jaguar.validation.Required()] constraint"));
		}
		
		shutdown();
	}
	
}
