package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.aspect.Throwing;
import org.eiichiro.jaguar.inject.Inject;

@ThrowingIntercept
@Aspect
public class ThrowingInterceptor3 {

	@Inject ThrowingObject throwingObject;
	
	@Throwing
	public void throwing1(ThrowingObject afterObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		afterObject.order.add(afterObject + "throwing1");
	}
	
}
