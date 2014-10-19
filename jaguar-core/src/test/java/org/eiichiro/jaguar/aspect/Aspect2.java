package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
@Pointcut2
public class Aspect2 {

	@Inject Component1 component1;
	
	@Before
	public void before2() {
		component1.order.add("before2");
	}
	
}
