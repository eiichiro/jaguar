package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Around;
import org.eiichiro.jaguar.interceptor.Interceptor;

@AroundIntercept
@Interceptor
public class AroundInterceptor4 {

	@Inject AroundObject aroundObject;
	
	@Around
	public void around1() {}
	
}
