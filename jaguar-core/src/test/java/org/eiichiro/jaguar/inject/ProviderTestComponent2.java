package org.eiichiro.jaguar.inject;

public class ProviderTestComponent2 {

	final ProviderTestComponent component;
	
	final ProviderTestComponent component2;
	
	@Inject
	public ProviderTestComponent2(ProviderTestComponent component,
			@ProviderTestQualifier("value2") ProviderTestComponent component2) {
		this.component = component;
		this.component2 = component2;
	}
	
}
