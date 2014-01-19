package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.After;
import org.eiichiro.jaguar.interceptor.Interceptor;

@AfterIntercept
@Interceptor
public class AfterInterceptor1 {

	@Inject AfterObject afterObject;
	
	@After
	public void after1() {
		afterObject.order.add("after1");
	}
	
	@After
	public void after2() {
		afterObject.order.add("after2");
	}
	
}
