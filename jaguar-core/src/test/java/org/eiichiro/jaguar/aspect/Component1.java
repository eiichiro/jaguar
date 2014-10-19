package org.eiichiro.jaguar.aspect;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class Component1 {

	List<String> order = new ArrayList<String>();
	
	@Pointcut1
	public void method1() {
		order.add("method1");
	}
	
	@Pointcut2
	public void method2() {
		order.add("method2");
	}
	
	@Pointcut1 @Pointcut2
	public void method3() {
		order.add("method3");
	}
	
}
