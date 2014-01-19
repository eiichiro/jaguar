package org.eiichiro.jaguar.aspectj;

import org.eiichiro.jaguar.aspectj.AjAssembled;
import org.eiichiro.jaguar.inject.Inject;

@AjAssembled
public class AjAssembledTestObject1 {

	@Inject AjAssembledTestObject2 ajAssembledTestObject2;
	
}
