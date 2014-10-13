package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
@Intercept1
public class Interceptor3 {

	@Inject Object1 object1;
	
	@Before
	public void before3() {
		object1.order.add("before3");
	}
	
}
