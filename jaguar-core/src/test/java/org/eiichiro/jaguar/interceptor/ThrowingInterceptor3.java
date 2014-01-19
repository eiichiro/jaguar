package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Interceptor;
import org.eiichiro.jaguar.interceptor.Throwing;

@ThrowingIntercept
@Interceptor
public class ThrowingInterceptor3 {

	@Inject ThrowingObject throwingObject;
	
	@Throwing
	public void throwing1(ThrowingObject afterObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		afterObject.order.add(afterObject + "throwing1");
	}
	
}
