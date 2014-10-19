package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.After;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@AfterIntercept
@Aspect
public class AfterInterceptor1 {

	@Inject AfterComponent afterComponent;
	
	@After
	public void after1() {
		afterComponent.order.add("after1");
	}
	
	@After
	public void after2() {
		afterComponent.order.add("after2");
	}
	
}
