package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Interceptor;

@Interceptor
public class InjectInterceptor {

	@Inject public InjectInterceptor(InjectObject2 injectObject2, String string) {
		this.injectObject2 = injectObject2;
		this.string = string;
	}
	
	final InjectObject2 injectObject2;
	
	final String string;
	
}
