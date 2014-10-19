package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
@Intercept1
public class Interceptor3 {

	@Inject Component1 component1;
	
	@Before
	public void before3() {
		component1.order.add("before3");
	}
	
}
