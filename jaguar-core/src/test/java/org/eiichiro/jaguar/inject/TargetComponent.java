package org.eiichiro.jaguar.inject;

public class TargetComponent {

	String string;
	
	Object object = "object";
	
	@TargetBinding @TargetQualifier Object object2;
	
}
