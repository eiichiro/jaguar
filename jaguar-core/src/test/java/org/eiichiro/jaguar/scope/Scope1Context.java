package org.eiichiro.jaguar.scope;

import java.util.HashMap;
import java.util.Map;

import org.eiichiro.jaguar.Descriptor;
import org.eiichiro.jaguar.scope.Context;

// The same as @Prototype.
public class Scope1Context implements Context {

	private Map<Descriptor<?>, Object> map = new HashMap<Descriptor<?>, Object>();
	
	@SuppressWarnings("unchecked")
	public <T> T get(Descriptor<T> descriptor) {
		return (T) map.get(descriptor);
	}

	public <T> void put(Descriptor<T> descriptor, T component) {
		map.put(descriptor, component);
	}

	@Override
	public Object store() {
		return map;
	}

}
