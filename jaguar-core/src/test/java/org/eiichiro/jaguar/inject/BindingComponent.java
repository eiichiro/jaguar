package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;

public class BindingComponent {

	// Duplicated.
	@Inject BindingComponent1 bindingComponent1;
	
	// BindingComponent4
	@Inject @Binding1 BindingComponent1 bindingObject12;

	// Incorrect.
	@Inject @Binding2 BindingComponent1 bindingObject13;
	
	// Duplicated.
	@Inject @Binding1 BindingComponent2 bindingComponent2;
	
	// BindingComponent6
	@Inject @Binding1 @Binding2 BindingComponent2 bindingObject22;
	
	@Inject BindingComponent5 bindingComponent5;
	
}
