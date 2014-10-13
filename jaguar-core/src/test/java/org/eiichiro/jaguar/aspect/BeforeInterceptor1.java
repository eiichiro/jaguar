package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@BeforeIntercept
@Aspect
public class BeforeInterceptor1 {

	@Inject BeforeObject beforeObject;
	
	@Before
	public void before1() {
		beforeObject.order.add("before1");
	}
	
	@Before
	public void before2() {
		beforeObject.order.add("before2");
	}
	
}
