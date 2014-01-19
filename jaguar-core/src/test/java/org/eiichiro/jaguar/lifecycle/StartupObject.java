package org.eiichiro.jaguar.lifecycle;

import org.eiichiro.jaguar.scope.Singleton;

@Singleton
public class StartupObject {

	boolean started = false;
	
	@Startup
	public void startup() {
		started = true;
	}
	
}
