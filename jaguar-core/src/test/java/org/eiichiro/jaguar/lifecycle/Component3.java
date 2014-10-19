package org.eiichiro.jaguar.lifecycle;

import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.lifecycle.Constructed;
import org.eiichiro.jaguar.lifecycle.Failed;

public class Component3 {

	Set<String> lifecycles = new HashSet<String>();
	
	@Constructed
	public void constructed() {
		throw new IllegalStateException();
	}
	
	@Failed
	public void failed(IllegalArgumentException exception) {}
	
}
