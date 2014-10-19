package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterPointcut
@Aspect
public class AfterAspect2 {

	@Inject AfterComponent afterComponent;
	
	@After
	public void after1(String string) {
		afterComponent.order.add(string + "after1");
	}
	
}
