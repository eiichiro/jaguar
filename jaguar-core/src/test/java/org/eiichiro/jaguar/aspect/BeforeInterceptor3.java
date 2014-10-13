package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@BeforeIntercept
@Aspect
public class BeforeInterceptor3 {

	@Inject BeforeObject beforeObject;
	
	@Before
	public void before1(BeforeObject beforeObject) {
		// Always fail -> parameter type & number is different from intercepted method.
		beforeObject.order.add(beforeObject + "before1");
	}
	
}
