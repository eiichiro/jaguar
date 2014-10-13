package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
@Intercept1
@Intercept2
public class Interceptor4 {

	@Inject Object1 object1;
	
	@Before
	public void before4() {
		object1.order.add("before4");
	}
	
}
