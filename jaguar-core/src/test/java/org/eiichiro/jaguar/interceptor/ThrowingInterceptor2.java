package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Interceptor;
import org.eiichiro.jaguar.interceptor.Throwing;

@ThrowingIntercept
@Interceptor
public class ThrowingInterceptor2 {

	@Inject ThrowingObject throwingObject;
	
	@Throwing
	public void throwing1(Exception exception) {
		throwingObject.order.add(exception.getMessage() + "throwing1");
	}
	
}
