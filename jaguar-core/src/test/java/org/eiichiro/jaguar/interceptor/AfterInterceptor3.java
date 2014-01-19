package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.After;
import org.eiichiro.jaguar.interceptor.Interceptor;

@AfterIntercept
@Interceptor
public class AfterInterceptor3 {

	@Inject AfterObject afterObject;
	
	@After
	public void after1(AfterObject afterObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		afterObject.order.add(afterObject + "after1");
	}
	
}
