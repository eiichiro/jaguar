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
package org.eiichiro.jaguar.scope;

/**
 * Web context (e.g. {@code ServletContext}, {@code HttpSession} and 
 * {@code HttpServletRequest}) backed component store.
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public interface WebContext<C> extends Context {

	/** The attribute name to get descriptor-component map from Web context. */
	public static String COMPONENTS = "org.eiichiro.jaguar.components";
	
	/**
	 * Associates Web context object to this {@link Context}.
	 * 
	 * @param context Web context object associated to this {@link Context}.
	 */
	public void associate(C context);
	
	/**
	 * Deassociate Web context object from this {@link Context}.
	 * 
	 * @param context Web context object deassociated from this {@link Context}.
	 */
	public void deassociate(C context);
	
}
