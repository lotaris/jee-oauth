package com.forbesdigital.jee.oauth.spring;

import com.lotaris.rox.annotations.RoxableTest;
import com.lotaris.rox.annotations.RoxableTestClass;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;

/**
 * @see AbstractExceptionTranslationFilter
 * @author Adrian Ungureanu <adrian.ungureanu@fortech.ro>
 */
@RoxableTestClass(tags = {"abstractExceptionTranslationFilter"})
public class AbstractExceptionTranslationFilterTest {
	
	//<editor-fold defaultstate="collapsed" desc="Constants">
	@Mock
	private HttpServletRequest request;
	@Mock
	private FilterChain chain;
	@Mock
	private IOException ioException;
	@Mock 
	private ServletException servletException;
	@Mock
	private RuntimeException runtimeException;
	@InjectMocks
	private AbstractExceptionTranslationFilterImpl abstractExceptionTranslationFilterImpl;
	//</editor-fold>
	
	//<editor-fold defaultstate="collapsed" desc="Mocks">
	
	//</editor-fold>
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@RoxableTest(key = "9ee9380ee729")
	public void doFilterThrowsIOException() throws Exception {
		doThrow(ioException).when(chain).doFilter(request,null);
		try{
			abstractExceptionTranslationFilterImpl.doFilter(request, null, chain);
			fail("IOException should have been thrown");
		} catch(IOException ex){
			
		}
	}
	
	@Test
	@RoxableTest(key = "214b09a41fb5")
	public void doFilterThrowsRuntimeException() throws Exception {
		doThrow(runtimeException).when(chain).doFilter(request,null);
		try{
			abstractExceptionTranslationFilterImpl.doFilter(request, null, chain);
			fail("IOException should have been thrown");
		} catch(RuntimeException ex){
			
		}
	}
	
	@Test
	@RoxableTest(key = "abafbd5925ff")
	public void doFilterThrowsServletException() throws Exception {
		doThrow(servletException).when(chain).doFilter(request,null);
		try{
			abstractExceptionTranslationFilterImpl.doFilter(request, null, chain);
			fail("IOException should have been thrown");
		} catch(ServletException ex){
			
		}
	}

	public static class AbstractExceptionTranslationFilterImpl extends AbstractExceptionTranslationFilter {

		@Override
		public void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response, FilterChain chain, RuntimeException exception) throws IOException, ServletException {
		}
	}
	
}
