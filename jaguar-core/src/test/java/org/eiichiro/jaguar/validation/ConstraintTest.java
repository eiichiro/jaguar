package org.eiichiro.jaguar.validation;

import static org.eiichiro.jaguar.Jaguar.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.eiichiro.jaguar.ConfigurationException;
import org.eiichiro.jaguar.validation.ViolationException;
import org.junit.Test;

public class ConstraintTest {

	@Test
	public void test() {
		bootstrap();
		install(Component4.class, Component5.class, Component6.class);
		component(Component4.class);
		
		try {
			component(Component5.class);
			fail();
		} catch (ViolationException e) {
			e.printStackTrace();
			assertThat(e.getMessage(), is(
					"Constraint violation: Field [java.lang.String " 
					+ "org.eiichiro.jaguar.validation.Component5.string]" 
					+ "'s value [] is invalid for " 
					+ "[@org.eiichiro.jaguar.validation.Constraint1()] constraint"));
		}
		
		try {
			component(Component6.class);
			fail();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			assertThat(e.getMessage(), is(
					"The other way around about Constraint and Validator: " 
					+ "Validator [class org.eiichiro.jaguar.validation.Constraint2Validator] " 
					+ "must validate [interface org.eiichiro.jaguar.validation.Constraint2]"));
		}
		
		shutdown();
	}

}
