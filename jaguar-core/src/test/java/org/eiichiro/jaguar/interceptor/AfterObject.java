package org.eiichiro.jaguar.interceptor;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class AfterObject {

	List<String> order = new ArrayList<String>();
	
	@AfterIntercept
	public void method1() {
		order.add("method1");
	}
	
	@AfterIntercept
	public String method2() {
		order.add("method2");
		return "after-";
	}
	
}
