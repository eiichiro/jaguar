package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Around;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.reverb.reflection.Invocation;

@AroundPointcut
@Aspect
public class AroundAspect2 {

	@Inject AroundComponent aroundComponent;
	
	@Around
	public String around1(Invocation<String> invocation) throws Throwable {
		aroundComponent.order.add(invocation.args()[0] + "before-around1");
		String string = invocation.proceed();
		aroundComponent.order.add(string + "after-around1");
		return string + "around1";
	}
	
}
