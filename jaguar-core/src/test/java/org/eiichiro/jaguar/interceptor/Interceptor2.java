package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@Interceptor
@Intercept2
public class Interceptor2 {

	@Inject Object1 object1;
	
	@Before
	public void before2() {
		object1.order.add("before2");
	}
	
}
