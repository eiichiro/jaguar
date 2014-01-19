package org.eiichiro.jaguar;

import org.eiichiro.jaguar.Component;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.interceptor.Before;
import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.scope.Singleton;
import org.eiichiro.jaguar.validation.Required;

@Production
@Singleton
@DescriptorBinding
@DescriptorConstraint
@DescriptorIntercept
public class DescriptorComponent extends Component<Object> {

	private final Object instance;
	
	public DescriptorComponent() {
		instance = new Object();
	}
	
	@Override
	public Object instance() {
		return instance;
	}

	@Inject Object object;
	
	@Required Object object2;
	
	// Ignored.
	@Before
	public void advice() {}
	
	// Ignored.
	@DescriptorIntercept
	public void method() {}
	
	@Constructed
	public void lifecycle() {}
	
}
