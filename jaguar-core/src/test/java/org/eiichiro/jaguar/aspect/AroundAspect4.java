package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Around;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AroundPointcut
@Aspect
public class AroundAspect4 {

	@Inject AroundComponent aroundComponent;
	
	@Around
	public void around1() {}
	
}
