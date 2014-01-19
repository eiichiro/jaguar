package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;

public class InjectObject3 {

	@Inject public InjectObject3(InjectObject2 injectObject2, String string) {
		this.injectObject2 = injectObject2;
		this.string = string;
	}
	
	final InjectObject2 injectObject2;
	
	final String string;
	
}
