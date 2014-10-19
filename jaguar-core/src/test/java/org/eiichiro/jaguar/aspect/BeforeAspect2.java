package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@BeforePointcut
@Aspect
public class BeforeAspect2 {

	@Inject BeforeComponent beforeComponent;
	
	@Before
	public void before1(String string) {
		beforeComponent.order.add(string + "before1");
	}
	
}
