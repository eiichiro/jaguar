package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@BeforeIntercept
@Aspect
public class BeforeInterceptor2 {

	@Inject BeforeObject beforeObject;
	
	@Before
	public void before1(String string) {
		beforeObject.order.add(string + "before1");
	}
	
}
