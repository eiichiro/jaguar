package org.eiichiro.jaguar;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.aspect.Aspect;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.inject.Injectee;
import org.eiichiro.jaguar.inject.Provider;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.scope.Singleton;
import org.eiichiro.jaguar.validation.Required;

@Production
@Singleton
@DescriptorBinding
@DescriptorConstraint
@DescriptorIntercept
@Aspect
public class DescriptorInterceptorProvider implements Provider<Object> {

	private final Object instance;
	
	public DescriptorInterceptorProvider() {
		instance = new Object();
	}
	
	@Inject Object object;
	
	@Required Object object2;
	
	@Before
	public void advice() {}
	
	// Ignored.
	@DescriptorIntercept
	public void method() {}
	
	@Constructed
	public void lifecycle() {}

	@Override
	public Object provide(Injectee injectee) {
		return instance;
	}
	
}
