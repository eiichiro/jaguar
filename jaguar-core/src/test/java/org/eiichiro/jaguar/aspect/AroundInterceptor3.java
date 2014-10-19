package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Around;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.reverb.reflection.Invocation;

@AroundIntercept
@Aspect
public class AroundInterceptor3 {

	@Inject AroundComponent aroundComponent;
	
	@Around
	public String around1(Invocation<String> invocation) throws Throwable {
		aroundComponent.order.add("around1");
		return "around1";
	}
	
}
