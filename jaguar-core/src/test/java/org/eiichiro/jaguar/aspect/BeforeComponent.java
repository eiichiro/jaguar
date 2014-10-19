package org.eiichiro.jaguar.aspect;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class BeforeComponent {

	List<String> order = new ArrayList<String>();
	
	@BeforeIntercept
	public void method1() {
		order.add("method1");
	}
	
	@BeforeIntercept
	public void method2(String string) {
		order.add(string + "method2");
	}
	
}
