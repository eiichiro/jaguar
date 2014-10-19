package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterIntercept
@Aspect
public class AfterInterceptor3 {

	@Inject AfterComponent afterComponent;
	
	@After
	public void after1(AfterComponent afterComponent) {
		// Always fail -> parameter type & number is different from intercepted method.
		afterComponent.order.add(afterComponent + "after1");
	}
	
}
