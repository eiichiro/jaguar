package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;

public class BindingObject {

	// Duplicated.
	@Inject BindingObject1 bindingObject1;
	
	// BindingObject4
	@Inject @Binding1 BindingObject1 bindingObject12;

	// Incorrect.
	@Inject @Binding2 BindingObject1 bindingObject13;
	
	// Duplicated.
	@Inject @Binding1 BindingObject2 bindingObject2;
	
	// BindingObject6
	@Inject @Binding1 @Binding2 BindingObject2 bindingObject22;
	
	@Inject BindingObject5 bindingObject5;
	
}
