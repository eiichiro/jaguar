package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@BeforePointcut
@Aspect
public class BeforeAspect3 {

	@Inject BeforeComponent beforeComponent;
	
	@Before
	public void before1(BeforeComponent beforeComponent) {
		// Always fail -> parameter type & number is different from intercepted method.
		beforeComponent.order.add(beforeComponent + "before1");
	}
	
}
