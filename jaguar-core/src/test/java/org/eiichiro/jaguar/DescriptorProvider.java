package org.eiichiro.jaguar;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.inject.Target;
import org.eiichiro.jaguar.inject.Provider;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.scope.Singleton;
import org.eiichiro.jaguar.validation.Required;

@Production
@Singleton
@DescriptorBinding
@DescriptorConstraint
@DescriptorPointcut
public class DescriptorProvider implements Provider<Object> {

	private final Object instance;
	
	public DescriptorProvider() {
		instance = new Object();
	}
	
	@Inject Object object;
	
	@Required Object object2;
	
	// Ignored.
	@Before
	public void advice() {}
	
	// Ignored.
	@DescriptorPointcut
	public void method() {}
	
	@Constructed
	public void lifecycle() {}

	@Override
	public Object provide(Target target) {
		return instance;
	}
	
}
