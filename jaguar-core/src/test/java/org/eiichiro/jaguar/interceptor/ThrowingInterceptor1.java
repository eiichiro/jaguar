package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Interceptor;
import org.eiichiro.jaguar.interceptor.Throwing;

@ThrowingIntercept
@Interceptor
public class ThrowingInterceptor1 {

	@Inject ThrowingObject throwingObject;
	
	@Throwing
	public void throwing1() {
		throwingObject.order.add("throwing1");
	}
	
	@Throwing
	public void throwing2() {
		throwingObject.order.add("throwing2");
	}
	
}
