package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Around;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.reverb.reflection.Invocation;
import org.eiichiro.reverb.reflection.MethodInvocation;

@AroundIntercept
@Aspect
public class AroundInterceptor1 {

	@Inject AroundObject aroundObject;
	
	@Around
	public void around1(Invocation<Void> invocation) throws Throwable {
		aroundObject.order.add("before-around1");
		invocation.proceed();
		aroundObject.order.add("after-around1");
	}
	
	@Around
	public void around2(MethodInvocation<Void> invocation) throws Throwable {
		aroundObject.order.add("before-around2");
		invocation.proceed();
		aroundObject.order.add("after-around2");
	}
	
}
