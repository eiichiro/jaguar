package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;

public class InjectComponent3 {

	@Inject public InjectComponent3(InjectComponent2 injectComponent2, String string) {
		this.injectComponent2 = injectComponent2;
		this.string = string;
	}
	
	final InjectComponent2 injectComponent2;
	
	final String string;
	
}
