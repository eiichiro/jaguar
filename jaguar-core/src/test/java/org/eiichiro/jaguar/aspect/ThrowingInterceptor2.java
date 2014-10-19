package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.aspect.Throwing;
import org.eiichiro.jaguar.inject.Inject;

@ThrowingIntercept
@Aspect
public class ThrowingInterceptor2 {

	@Inject ThrowingComponent throwingComponent;
	
	@Throwing
	public void throwing1(Exception exception) {
		throwingComponent.order.add(exception.getMessage() + "throwing1");
	}
	
}
