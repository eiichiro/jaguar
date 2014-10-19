package org.eiichiro.jaguar;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;

@DescriptorPointcut
@Aspect
public class DescriptorAspect {

	@Before
	public void advice() {}
	
}
