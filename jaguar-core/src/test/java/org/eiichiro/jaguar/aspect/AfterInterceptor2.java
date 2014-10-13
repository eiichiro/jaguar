package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterIntercept
@Aspect
public class AfterInterceptor2 {

	@Inject AfterObject afterObject;
	
	@After
	public void after1(String string) {
		afterObject.order.add(string + "after1");
	}
	
}
