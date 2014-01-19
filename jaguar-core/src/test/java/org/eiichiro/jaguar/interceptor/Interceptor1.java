package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@Interceptor
@Intercept1
public class Interceptor1 {

	@Inject Object1 object1;
	
	@Before
	public void before1() {
		object1.order.add("before1");
	}
	
}
