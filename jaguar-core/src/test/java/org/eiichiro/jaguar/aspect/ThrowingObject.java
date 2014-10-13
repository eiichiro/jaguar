package org.eiichiro.jaguar.aspect;

import java.util.ArrayList;
import java.util.List;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class ThrowingObject {

	List<String> order = new ArrayList<String>();
	
	@ThrowingIntercept
	public void method1() {
		order.add("method1");
		throw new RuntimeException("exception-");
	}
	
}
