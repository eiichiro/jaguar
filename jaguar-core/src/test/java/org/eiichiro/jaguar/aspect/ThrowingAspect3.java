package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.aspect.Throwing;
import org.eiichiro.jaguar.inject.Inject;

@ThrowingPointcut
@Aspect
public class ThrowingAspect3 {

	@Inject ThrowingComponent throwingComponent;
	
	@Throwing
	public void throwing1(ThrowingComponent afterObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		afterObject.order.add(afterObject + "throwing1");
	}
	
}
