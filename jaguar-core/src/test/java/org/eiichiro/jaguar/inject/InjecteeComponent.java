package org.eiichiro.jaguar.inject;

public class InjecteeComponent {

	String string;
	
	Object object = "object";
	
	@InjecteeBinding @InjecteeQualifier Object object2;
	
}
