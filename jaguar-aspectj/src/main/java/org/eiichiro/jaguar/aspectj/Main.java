/*
 * Copyright (C) 2011 Eiichiro Uchiumi. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eiichiro.jaguar.aspectj;

import static org.eiichiro.jaguar.aspectj.Version.*;

/**
 * Command line interface to print the version &amp; copyright information.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public final class Main {

	/**
	 * Prints the version &amp; copyright information.
	 * 
	 * @param args Ignored.
	 */
	public static void main(String[] args) {
		System.out.println("Jaguar AspectJ integration module " + MAJOR + "." + MINER + BUILD);
		System.out.println("Copyright (C) 2009-2011 Eiichiro Uchiumi. All Rights Reserved.");
	}
	
}
