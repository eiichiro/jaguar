package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Around;
import org.eiichiro.jaguar.interceptor.Interceptor;
import org.eiichiro.reverb.reflection.Invocation;

@AroundIntercept
@Interceptor
public class AroundInterceptor3 {

	@Inject AroundObject aroundObject;
	
	@Around
	public String around1(Invocation<String> invocation) throws Throwable {
		aroundObject.order.add("around1");
		return "around1";
	}
	
}
