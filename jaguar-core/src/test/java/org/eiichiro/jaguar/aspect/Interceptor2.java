package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
@Intercept2
public class Interceptor2 {

	@Inject Object1 object1;
	
	@Before
	public void before2() {
		object1.order.add("before2");
	}
	
}
