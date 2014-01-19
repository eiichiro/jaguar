package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@BeforeIntercept
@Interceptor
public class BeforeInterceptor2 {

	@Inject BeforeObject beforeObject;
	
	@Before
	public void before1(String string) {
		beforeObject.order.add(string + "before1");
	}
	
}
