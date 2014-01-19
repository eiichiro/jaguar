package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.After;
import org.eiichiro.jaguar.interceptor.Interceptor;

@AfterIntercept
@Interceptor
public class AfterInterceptor2 {

	@Inject AfterObject afterObject;
	
	@After
	public void after1(String string) {
		afterObject.order.add(string + "after1");
	}
	
}
