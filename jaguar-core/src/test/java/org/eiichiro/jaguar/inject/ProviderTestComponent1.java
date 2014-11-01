package org.eiichiro.jaguar.inject;

public class ProviderTestComponent1 {

	@Inject ProviderTestComponent component;
	
	@Inject @ProviderTestQualifier("value1")
	ProviderTestComponent component2;
	
}
