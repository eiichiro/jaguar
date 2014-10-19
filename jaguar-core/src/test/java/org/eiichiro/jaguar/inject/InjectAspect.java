package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
public class InjectAspect {

	@Inject public InjectAspect(InjectComponent2 injectComponent2, String string) {
		this.injectComponent2 = injectComponent2;
		this.string = string;
	}
	
	final InjectComponent2 injectComponent2;
	
	final String string;
	
}
