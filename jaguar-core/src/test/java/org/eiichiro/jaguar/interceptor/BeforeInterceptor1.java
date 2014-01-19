package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@BeforeIntercept
@Interceptor
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
