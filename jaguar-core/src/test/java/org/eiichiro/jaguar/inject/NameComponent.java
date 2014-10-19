package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.inject.Name;

public class NameComponent {

	@Inject @Name("NameComponent2") NameComponent1 nameComponent1;
	
	@Inject @Name("not_found") NameComponent1 nameObject12;
	
}
