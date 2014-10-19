package org.eiichiro.jaguar.lifecycle;

import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.lifecycle.Activated;
import org.eiichiro.jaguar.lifecycle.Constructed;

public class Component1 {

	Set<String> lifecycles = new HashSet<String>();
	
	@Constructed
	public void constructed() {
		lifecycles.add("Constructed");
	}
	
	@Constructed
	public void constructed2() {
		lifecycles.add("Constructed2");
	}
	
	@Activated
	public void activated() {
		lifecycles.add("Activated");
	}
	
	@Activated
	public void activated2() {
		lifecycles.add("Activated2");
	}
	
}
