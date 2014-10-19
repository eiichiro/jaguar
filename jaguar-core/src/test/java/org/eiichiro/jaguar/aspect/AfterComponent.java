package org.eiichiro.jaguar.aspect;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class AfterComponent {

	List<String> order = new ArrayList<String>();
	
	@AfterPointcut
	public void method1() {
		order.add("method1");
	}
	
	@AfterPointcut
	public String method2() {
		order.add("method2");
		return "after-";
	}
	
}
