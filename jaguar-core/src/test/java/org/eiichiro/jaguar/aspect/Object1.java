package org.eiichiro.jaguar.aspect;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class Object1 {

	List<String> order = new ArrayList<String>();
	
	@Intercept1
	public void method1() {
		order.add("method1");
	}
	
	@Intercept2
	public void method2() {
		order.add("method2");
	}
	
	@Intercept1 @Intercept2
	public void method3() {
		order.add("method3");
	}
	
}
