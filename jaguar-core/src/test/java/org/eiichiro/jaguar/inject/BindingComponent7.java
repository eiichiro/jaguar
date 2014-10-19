package org.eiichiro.jaguar.inject;

public class BindingComponent7 {

	@Inject public BindingComponent7(
			BindingComponent1 bindingComponent1, 
			@Binding1 BindingComponent1 bindingObject12, 
			@Binding2 BindingComponent1 bindingObject13, 
			@Binding1 BindingComponent2 bindingComponent2, 
			@Binding1 @Binding2 BindingComponent2 bindingObject22, 
			BindingComponent5 bindingComponent5) {
		this.bindingComponent1 = bindingComponent1;
		this.bindingObject12 = bindingObject12;
		this.bindingObject13 = bindingObject13;
		this.bindingComponent2 = bindingComponent2;
		this.bindingObject22 = bindingObject22;
		this.bindingComponent5 = bindingComponent5;
	}
	
	// Duplicated.
	final BindingComponent1 bindingComponent1;
	
	// BindingComponent4
	final @Binding1 BindingComponent1 bindingObject12;

	// Incorrect.
	final @Binding2 BindingComponent1 bindingObject13;
	
	// Duplicated.
	final @Binding1 BindingComponent2 bindingComponent2;
	
	// BindingComponent6
	final @Binding1 @Binding2 BindingComponent2 bindingObject22;
	
	final BindingComponent5 bindingComponent5;
	
}
