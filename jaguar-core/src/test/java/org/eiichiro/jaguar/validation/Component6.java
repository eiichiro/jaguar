package org.eiichiro.jaguar.validation;

public class Component6 {

	// The other way around. @Constraint2 indicates that @Constraint2 should be 
	// validated by Constraint2Validator, but Constraint2Validator indicates 
	// that Constraint2Validator validates @Constraint1.
	@Constraint2 String string = "";

}
