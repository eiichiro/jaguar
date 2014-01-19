package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@BeforeIntercept
@Interceptor
public class BeforeInterceptor3 {

	@Inject BeforeObject beforeObject;
	
	@Before
	public void before1(BeforeObject beforeObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		beforeObject.order.add(beforeObject + "before1");
	}
	
}
