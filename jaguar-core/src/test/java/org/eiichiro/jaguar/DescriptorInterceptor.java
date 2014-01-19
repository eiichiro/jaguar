package org.eiichiro.jaguar;

import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;

@DescriptorIntercept
@Interceptor
public class DescriptorInterceptor {

	@Before
	public void advice() {}
	
}
