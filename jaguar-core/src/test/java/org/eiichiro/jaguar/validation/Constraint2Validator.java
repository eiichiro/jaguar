package org.eiichiro.jaguar.validation;

import org.eiichiro.jaguar.validation.Validator;

public class Constraint2Validator implements Validator<Constraint1> {

	public boolean validate(Constraint1 constraint1, Object object) {
		return object instanceof Component3;
	}

}
