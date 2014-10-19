package org.eiichiro.jaguar.aspect;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.inject.Inject;

@BeforePointcut
@Aspect
public class BeforeAspect1 {

	@Inject BeforeComponent beforeComponent;
	
	@Before
	public void before1() {
		beforeComponent.order.add("before1");
	}
	
	@Before
	public void before2() {
		beforeComponent.order.add("before2");
	}
	
}
