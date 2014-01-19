package org.eiichiro.jaguar.lifecycle;

import java.util.HashSet;
import java.util.Set;

import org.eiichiro.jaguar.lifecycle.Destroyed;
import org.eiichiro.jaguar.lifecycle.Failed;
import org.eiichiro.jaguar.lifecycle.Passivated;
import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class Object2 {

	Set<String> lifecycles = new HashSet<String>();
	
	@Passivated
	public void passivated() {
		lifecycles.add("Passivated");
	}
	
	@Passivated
	public void passivated2() {
		lifecycles.add("Passivated2");
	}
	
	@Destroyed
	public void destroyed() {
		lifecycles.add("Destroyed");
	}
	
	@Destroyed
	public void destroyed2() {
		lifecycles.add("Destroyed2");
	}
	
	@Destroyed
	public void destroyed3() {
		throw new RuntimeException();
	}
	
	@Failed
	public void failed() {
		lifecycles.add("Failed");
	}
	
	@Failed
	public void failed2(Exception exception) {
		lifecycles.add("Failed2" + exception.getClass().getSimpleName());
	}
	
}
