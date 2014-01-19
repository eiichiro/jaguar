package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Around;
import org.eiichiro.jaguar.interceptor.Interceptor;
import org.eiichiro.reverb.reflection.Invocation;
import org.eiichiro.reverb.reflection.MethodInvocation;

@AroundIntercept
@Interceptor
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
