package org.eiichiro.jaguar;

import org.eiichiro.jaguar.validation.Validator;

public class DescriptorConstraintValidator implements Validator<DescriptorConstraint> {

	public boolean validate(DescriptorConstraint constraint, Object object) {
		return true;
	}

}
