package com.forbesdigital.jee.oauth.spring;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Abstract class for handles any AccessDeniedException and AuthenticationException thrown within the filter chain.
 * To be extended by specific classes for each type of authentication.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public abstract class AbstractExceptionTranslationFilter extends GenericFilterBean {

	protected ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		try {
			chain.doFilter(request, response);
			logger.debug("Chain processed normally");
		
		} catch (IOException ex) {
			throw ex;
			
		} catch (ServletException|RuntimeException ex) {
			
			// Try to extract a SpringSecurityException from the stacktrace
			Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
			RuntimeException ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
			if (ase == null) {
				ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
			}
			if (ase != null) {
				handleSpringSecurityException(request, response, chain, ase);
			
			// Rethrow ServletExceptions and RuntimeExceptions as-is
			} else {
				
				if (ex instanceof ServletException) {
					throw (ServletException) ex;
				}
				else if (ex instanceof RuntimeException) {
					throw (RuntimeException) ex;
				}

				// Wrap other Exceptions. This shouldn't actually happen
				// as we've already covered all the possibilities for doFilter
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * Performs custom handling for caught Spring Security exceptions.
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @param exception
	 * @throws IOException
	 * @throws ServletException 
	 */
	protected abstract void handleSpringSecurityException(HttpServletRequest request, 
			  HttpServletResponse response, FilterChain chain, RuntimeException exception) 
			  throws IOException, ServletException;

	/**
	 * Default implementation of
	 * <code>ThrowableAnalyzer</code> which is capable of also unwrapping
	 * <code>ServletException</code>s.
	 */
	private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {

		/**
		 * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
		 */
		@Override
		protected void initExtractorMap() {
			super.initExtractorMap();

			registerExtractor(ServletException.class, new ThrowableCauseExtractor() {
				@Override
				public Throwable extractCause(Throwable throwable) {
					ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
					return ((ServletException) throwable).getRootCause();
				}
			});
		}
	}
}
