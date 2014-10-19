package org.eiichiro.jaguar;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;

@DescriptorIntercept
@Aspect
public class DescriptorInterceptor {

	@Before
	public void advice() {}
	
}
