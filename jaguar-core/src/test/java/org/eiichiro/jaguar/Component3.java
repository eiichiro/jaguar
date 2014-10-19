package org.eiichiro.jaguar;

import org.eiichiro.jaguar.inject.Inject;

// Instantiated outside of the container.
public class Component3 {

	private Component3() {}
	
	@Inject Component2 component2;
	
	static Component3 component3() {
		return new Component3();
	}
	
}
