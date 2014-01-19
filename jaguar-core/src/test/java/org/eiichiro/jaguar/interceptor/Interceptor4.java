package org.eiichiro.jaguar.interceptor;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@Interceptor
@Intercept1
@Intercept2
public class Interceptor4 {

	@Inject Object1 object1;
	
	@Before
	public void before4() {
		object1.order.add("before4");
	}
	
}
