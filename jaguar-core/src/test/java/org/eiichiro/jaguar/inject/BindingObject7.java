package org.eiichiro.jaguar.inject;

public class BindingObject7 {

	@Inject public BindingObject7(
			BindingObject1 bindingObject1, 
			@Binding1 BindingObject1 bindingObject12, 
			@Binding2 BindingObject1 bindingObject13, 
			@Binding1 BindingObject2 bindingObject2, 
			@Binding1 @Binding2 BindingObject2 bindingObject22, 
			BindingObject5 bindingObject5) {
		this.bindingObject1 = bindingObject1;
		this.bindingObject12 = bindingObject12;
		this.bindingObject13 = bindingObject13;
		this.bindingObject2 = bindingObject2;
		this.bindingObject22 = bindingObject22;
		this.bindingObject5 = bindingObject5;
	}
	
	// Duplicated.
	final BindingObject1 bindingObject1;
	
	// BindingObject4
	final @Binding1 BindingObject1 bindingObject12;

	// Incorrect.
	final @Binding2 BindingObject1 bindingObject13;
	
	// Duplicated.
	final @Binding1 BindingObject2 bindingObject2;
	
	// BindingObject6
	final @Binding1 @Binding2 BindingObject2 bindingObject22;
	
	final BindingObject5 bindingObject5;
	
}
