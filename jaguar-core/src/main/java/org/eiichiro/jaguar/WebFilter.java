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
package org.eiichiro.jaguar;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A {@code Filter} to associate the current Web context with the current thread.
 * You need to setup this filter and {@link WebListener} in the <code>web.xml</code> 
 * as below if you use Jaguar in Web environment: 
 * <pre>
 * &lt;filter&gt;
 *     &lt;filter-name&gt;webFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;org.eiichiro.jaguar.WebFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;webFilter&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * 
 * @author <a href="mailto:eiichiro@eiichiro.org">Eiichiro Uchiumi</a>
 */
public class WebFilter implements Filter {

	private static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	
	/**
	 * no-op.
	 * 
	 * @param filterConfig {@code FilterConfig} to initialize this filter.
	 */
	public void init(FilterConfig filterConfig) throws ServletException {}

	/**
	 * Associates the current HTTP servlet request with the current thread until 
	 * filter chain ends.
	 * 
	 * @param request The current {@code ServletRequest}.
	 * @param response The current {@code ServletResponse}.
	 * @param chain The current {@code FilterChain}.
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = WebFilter.request.get();
		
		try {
			WebFilter.request.set((HttpServletRequest) request);
			chain.doFilter(request, response);
		} finally {
			WebFilter.request.set(httpServletRequest);
		}
	}
	
	/** no-op. */
	public void destroy() {}
	
	/**
	 * Returns the HTTP servlet request associated with the current thread.
	 * 
	 * @return The HTTP servlet request associated with the current thread.
	 */
	public static HttpServletRequest request() {
		return request.get();
	}
	
	/**
	 * Associates the specified HTTP servlet request to the current thread.
	 * 
	 * @param request The HTTP servlet request to be associated to the current 
	 * thread.
	 */
	public static void request(HttpServletRequest request) {
		WebFilter.request.set(request);
	}
	
	/**
	 * Returns the HTTP session associated with the current thread.
	 * 
	 * @return The HTTP session associated with the current thread.
	 */
	public static HttpSession session() {
		HttpServletRequest httpServletRequest = request.get();
		return (httpServletRequest == null) ? null : httpServletRequest.getSession();
	}

}
