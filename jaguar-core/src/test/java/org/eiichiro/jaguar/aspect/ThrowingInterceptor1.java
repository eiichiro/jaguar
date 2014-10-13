package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.aspect.Throwing;
import org.eiichiro.jaguar.inject.Inject;

@ThrowingIntercept
@Aspect
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
