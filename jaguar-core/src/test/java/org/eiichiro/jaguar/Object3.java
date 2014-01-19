package org.eiichiro.jaguar;

import org.eiichiro.jaguar.inject.Inject;

// Instantiated outside of the container.
public class Object3 {

	private Object3() {}
	
	@Inject Object2 object2;
	
	static Object3 object3() {
		return new Object3();
	}
	
}
