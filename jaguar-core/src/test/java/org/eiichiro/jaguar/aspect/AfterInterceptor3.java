package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterIntercept
@Aspect
public class AfterInterceptor3 {

	@Inject AfterObject afterObject;
	
	@After
	public void after1(AfterObject afterObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		afterObject.order.add(afterObject + "after1");
	}
	
}
