package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@Aspect
@Pointcut1
public class Aspect1 {

	@Inject Component1 component1;
	
	@Before
	public void before1() {
		component1.order.add("before1");
	}
	
}
