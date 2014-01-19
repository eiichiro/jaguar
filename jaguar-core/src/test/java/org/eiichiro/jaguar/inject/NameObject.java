package org.eiichiro.jaguar.inject;

import org.eiichiro.jaguar.inject.Inject;
import org.eiichiro.jaguar.inject.Name;

public class NameObject {

	@Inject @Name("NameObject2") NameObject1 nameObject1;
	
	@Inject @Name("not_found") NameObject1 nameObject12;
	
}
