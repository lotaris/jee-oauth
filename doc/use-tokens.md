# Use OAuth tokens

The OAuth tokens will be sent in the request in `Authorization` header in the follwing format `Authorization: Bearer <your_oauth_token>` 
In this step you need to implement the classes required for Authorization with OAuth tokens and to configure in Spring Security the required filters for all your endpoints.

## Spring Security Service

First you have to implement a service which will be used by Spring Security in authorization of the API calls requests.

This service will be used to build an [OAuthTokenDetails][OAuthTokenDetails] with information about the Token used in the request, based on the token value sent in the `Authorization` header.

For this you need to extend [IOAuthTokenDetailsBuilder][IOAuthTokenDetailsBuilder] interface to add the `Local` annotation and then to implement the new interface in a service.

```java
@Local
public interface ISpringTokenService extends IOAuthTokenDetailsBuilder{
	// Add other methods that you want to expose in this EJB 
}
```

```java
@Stateless
public class SpringTokenService implements ISpringTokenService {

	@Override
	public OAuthTokenDetails buildTokenDetails(String accessToken) {
		// get the Token entity by accessToken
		// build and return an OAuthTokenDetails instance with the OAuth Token information required for authentication
	}
}
```

## Authorization error handling

In order to catch the authentication exceptions and to return the error response in a desired format, you need to extend [AbstractExceptionTranslationFilter][AbstractExceptionTranslationFilter] and to override the `handleSpringSecurityException(...)` method.

```java
public class SpringTokenExceptionTranslationFilter extends AbstractExceptionTranslationFilter {

	@Override
	protected void handleSpringSecurityException(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			RuntimeException exception) throws IOException, ServletException {

        if (exception instanceof AuthenticationException) {
			sendUnauthorizedAccessResponse(response);
		}
	}

	protected void sendUnauthorizedAccessResponse(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.addHeader("WWW-Authenticate", "Bearer");
		response.getOutputStream().print(buildErrorResponse(
			// Some parameters here
			));
	}

	protected String buildErrorResponse(
			// Some parameters here
			) {
		// Build your custom response body here
	}	
}
```

## Configure Spring security filters

In `spring-security.xml` file configure the beans for the filters used for Token Authorization.

```xml
<beans:bean id="tokenService" class="org.springframework.jndi.JndiObjectFactoryBean">
	<beans:property name="jndiName" value="java:comp/env/ejb/SpringTokenService"/>
	<beans:property name="expectedType" value="<your_pachage_here>.ISpringTokenService"/>
</beans:bean>	
<!--
	TOKEN START
-->
<beans:bean id="tokenExceptionTranslatorFilter" class="<your_pachage_here>.SpringTokenExceptionTranslationFilter" />	

<beans:bean id="tokenFilterSecurityInterceptor" class="org.springframework.security.web.access.intercept.FilterSecurityInterceptor">
	<beans:property name="securityMetadataSource">
		<security:filter-security-metadata-source>
			<security:intercept-url pattern="/.*" access="ROLE_TEST" />
		</security:filter-security-metadata-source>
	</beans:property>
	<beans:property name="authenticationManager" ref="tokenAuthenticationManager" />
	<beans:property name="accessDecisionManager" ref="accessDecisionManager" />
</beans:bean>

<beans:bean id="tokenAuthenticationFilter" class="com.forbesdigital.jee.oauth.spring.token.TokenBearerAuthenticationFilter">
	<beans:constructor-arg ref="tokenAuthenticationManager" />
</beans:bean>

<beans:bean id="tokenAuthenticationManager" class="org.springframework.security.authentication.ProviderManager">
	<beans:property name="providers">
		<beans:list>
			<beans:ref bean="tokenAuthenticationProvider" />
		</beans:list>
	</beans:property>
</beans:bean>

<beans:bean id="tokenAuthenticationProvider" class="org.springframework.security.authentication.dao.DaoAuthenticationProvider">
	<beans:property name="userDetailsService" ref="tokenDetailsService" />
</beans:bean>

<beans:bean	id="tokenDetailsService" class="com.forbesdigital.jee.oauth.spring.token.OAuthTokenDetailsService">
	<beans:property name="tokenDetailsBuilder" ref="tokenService" />
</beans:bean>
<!--
	TOKEN END
-->
```

For the first bean to work you need to also add the following to your `web.xml` file:

```xml
<ejb-local-ref>
	<ejb-ref-name>ejb/SpringTokenService</ejb-ref-name>
	<ejb-ref-type>Session</ejb-ref-type>
	<local>com.lotaris.dcc.infra.security.token.ISpringTokenService</local>
	<ejb-link>SpringTokenService</ejb-link>
</ejb-local-ref>
```

Then configure the required filters for your API calls where you want to use Token Authorization

```xml
<security:filter-chain pattern="/api/.*" filters="securityContextFilter, tokenExceptionTranslatorFilter, tokenAuthenticationFilter, tokenFilterSecurityInterceptor" />
```

[Next step](enforce-scopes.md)

[Previous step](request-tokens.md)

[Back to parent](../README.md)


[OAuthTokenDetails]: src/main/java/com/forbesdigital/jee/oauth/spring/token/OAuthTokenDetails.java
[IOAuthTokenDetailsBuilder]: src/main/java/com/forbesdigital/jee/oauth/spring/token/IOAuthTokenDetailsBuilder.java
[AbstractExceptionTranslationFilter]: src/main/java/com/forbesdigital/jee/oauth/spring/AbstractExceptionTranslationFilter.java