package org.eiichiro.jaguar;

import org.eiichiro.jaguar.aspect.Before;
import org.eiichiro.jaguar.deployment.Production;
import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.scope.Singleton;
import org.eiichiro.jaguar.validation.Required;

@Production
@Singleton
@DescriptorBinding
@DescriptorConstraint
@DescriptorIntercept
public class DescriptorComponent {

	@Inject Object object;
	
	@Required Object object2;
	
	// Ignored.
	@Before
	public void advice() {}
	
	@DescriptorIntercept
	public void method() {}
	
	@Constructed
	public void lifecycle() {}
	
}
