package org.eiichiro.jaguar.inject.aspectj;

import org.eiichiro.jaguar.aspectj.AjAssembled;
import org.eiichiro.jaguar.inject.Inject;

@AjAssembled
public class AjAssembledTestComponent1 {

	@Inject AjAssembledTestComponent2 ajAssembledTestComponent2;
	
}
