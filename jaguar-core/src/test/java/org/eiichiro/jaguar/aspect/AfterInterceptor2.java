package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterIntercept
@Aspect
public class AfterInterceptor2 {

	@Inject AfterComponent afterComponent;
	
	@After
	public void after1(String string) {
		afterComponent.order.add(string + "after1");
	}
	
}
