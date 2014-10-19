package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.aspect.Throwing;
import org.eiichiro.jaguar.inject.Inject;

@ThrowingIntercept
@Aspect
public class ThrowingInterceptor1 {

	@Inject ThrowingComponent throwingComponent;
	
	@Throwing
	public void throwing1() {
		throwingComponent.order.add("throwing1");
	}
	
	@Throwing
	public void throwing2() {
		throwingComponent.order.add("throwing2");
	}
	
}
