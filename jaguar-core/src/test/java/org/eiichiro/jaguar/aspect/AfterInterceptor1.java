package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterIntercept
@Aspect
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
