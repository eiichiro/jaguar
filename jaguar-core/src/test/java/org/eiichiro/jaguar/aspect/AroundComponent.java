package org.eiichiro.jaguar.aspect;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class AroundComponent {

	List<String> order = new ArrayList<String>();
	
	@AroundIntercept
	public void method1() {
		order.add("method1");
	}
	
	@AroundIntercept
	public String method2(String string) {
		order.add(string + "method2");
		return string;
	}
	
}
