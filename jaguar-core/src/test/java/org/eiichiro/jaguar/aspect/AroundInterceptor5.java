package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Around;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AroundIntercept
@Aspect
public class AroundInterceptor5 {

	@Inject AroundObject aroundObject;
	
	@Around
	public void around1(AroundObject aroundObject) {}
	
}
