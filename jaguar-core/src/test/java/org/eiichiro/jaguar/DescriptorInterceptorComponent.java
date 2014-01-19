package org.eiichiro.jaguar;

import org.eiichiro.jaguar.Component;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.interceptor.Interceptor;
import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.scope.Singleton;
import org.eiichiro.jaguar.validation.Required;

@Production
@Singleton
@DescriptorBinding
@DescriptorConstraint
@DescriptorIntercept
@Interceptor
public class DescriptorInterceptorComponent extends Component<Object> {

	private final Object instance;
	
	public DescriptorInterceptorComponent() {
		instance = new Object();
	}
	
	@Override
	public Object instance() {
		return instance;
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
	
}
