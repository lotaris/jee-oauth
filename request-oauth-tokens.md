# Request OAuth tokens

In this step you need to implement an endpoint for requesting Oauth Tokens. You will also need to implement some classes required for Client and User Authentication and to configure in Spring Security the filters for this endpoint.

## Extend AbstractAccessTokenResource

First step will be to create a Resource class which extends [AbstractAccessTokenResource][AbstractAccessTokenResource], to override `requestToken` method and to add the required annotations depending on what REST framework you want to use. For Jersey such a resource would look like this:

```java
@Path("oauth/token")
@Stateless
public class AccessTokenResource extends AbstractAccessTokenResource<Client, PlatformUser, OAuthToken>{

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@AllOAuthScopes({}) // public access
	@Override
	public Response requestToken(
			@FormParam("grant_type") String grantType,
			@FormParam("scope") String scope,
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("expires_in") String expiresIn) {

		return super.requestToken(grantType, scope, username, password, expiresIn);
	}
}
``` 

## Implement abstract methods

Next step will be to implement all the abstract methods from [AbstractAccessTokenResource][AbstractAccessTokenResource] to provide custom logic for some of the resource operations.

```java
@Override
protected Client getAuthenticatedClient() {
	// retrive and return the authenticated Client
}

@Override
protected OAuthToken createOAuthToken(Client client, Integer tokenLifetime, Set<String> grantedScopes, PlatformUser user) {
	// custom logic to create, persist and return an OAuth Token for the Authenticated Client and optionally the Authenticated User
}
```

When using `Spring Security`, the `client_id` of the authenticated client can be obtained using `SecurityContextHolder`:

```java
final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

if (authentication != null) {
	String clientId = authentication.getName();
}
```

Another method which is not abstract, but which needs to be overriden in case `password` grant type is used, is `getOAuthUser(String username)`.

```java
@Override
protected PlatformUser getOAuthUser(String username) {
	// custom logic to retrieve and return User by username
}
```

## Overwrite protected methods - optional

You can also override the rest of the protected methods from the abstract class if you need to add custom logic/processing.

```java
@Override
protected Set<String> afterOAuthScopesResolution(Set<String> grantedScopes) {
	// do some cusom processing of granted scopes after the requested scopes were validated by the libary 
	return grantedScopes;
}

@Override
protected Integer afterTokenLifetimeResolution(Client client, Integer tokenLifetime, PlatformUser user) {
	// do some cusom processing of token lifetime after the processing done by the libary 
	return tokenLifetime;
}

@Override
protected Map<String, Object> getAdditionalResponseParameters(OAuthToken token) {
	Map<String, Object> additionalInformation = new LinkedHashMap<>();
	// add custom parameters in additionalInformation map if you want more info to be returned in the request token response
	return additionalInformation;
}
```

You can override even the way successful and error responses are built.

```java
@Override
protected Response buildSuccessfulResponse(OAuthTokenResponse tokenResponse, Map<String, String> headers) {
	// build successfull responses in a custom way
}

@Override
protected Response buildErrorResponse(OAuthTokenError errorResponse, int statusCode) {
	// build error responses in a custom way
}
```

## Spring Security Services

In this step you have to implement some services which will be used by Spring Security for authorization of OAuth Token requests.

One of the services will be used to build an [OAuthClientDetails][OAuthClientDetails] with information about the client doing the request, based on the `client_id` sent in the Authorization header.

For this you need to extend [IOAuthClientDetailsBuilder][IOAuthClientDetailsBuilder] interface to add the `Local` annotation and then to implement the new interface in a service.

```java
@Local
public interface ISpringClientService extends IOAuthClientDetailsBuilder {
	// Add other methods that you want to expose in this EJB 
}
```

```java
@Stateless
public class SpringClientService implements ISpringClientService {

	@Override
	public OAuthClientDetails buildClientDetails(String clientId) {
		// get the Client entity by clientId
		// build and return an OAuthClientDetails instance with the Client information required for authentication
	}
}
```
In case `password` grant type is used, a second service is needed to build an [OAuthUserDetails][OAuthUserDetails] with information about the User doing the request, based on the `username` sent in the request parameters.

For this you need to extend [IOAuthUserDetailsBuilder][IOAuthUserDetailsBuilder] interface to add the `Local` annotation and then to implement the new interface in a service.

```java
@Local
public interface ISpringUserService extends IOAuthUserDetailsBuilder {
	// Add other methods that you want to expose in this EJB 
}
```

```java
@Stateless
public class SpringUserService implements ISpringPlatformUserService {

	@Override
	public OAuthUserDetails buildUserDetails(String username) {
		// get the User entity by username
		// build and return an OAuthUserDetails instance with the User information required for authentication
	}
}
```

## Configure Spring security filters 

In `spring-security.xml` file configure the beans for the filters used for Client and for User Authorization.

```xml
<beans:bean id="platformUserService" class="org.springframework.jndi.JndiObjectFactoryBean">
	<beans:property name="jndiName" value="java:comp/env/ejb/SpringPlatformUserService"/>
	<beans:property name="expectedType" value="<your_pachage_here>.ISpringPlatformUserService"/>
</beans:bean>

<beans:bean id="clientService" class="org.springframework.jndi.JndiObjectFactoryBean">
	<beans:property name="jndiName" value="java:comp/env/ejb/SpringClientService"/>
	<beans:property name="expectedType" value="<your_pachage_here>.ISpringClientService"/>
</beans:bean>	
<!--
	USER START
-->
<beans:bean id="platformUserFilterSecurityInterceptor" class="org.springframework.security.web.access.intercept.FilterSecurityInterceptor">
	<beans:property name="securityMetadataSource">
		<security:filter-security-metadata-source>
			<security:intercept-url pattern="/.*" access="ROLE_TEST" />
		</security:filter-security-metadata-source>
	</beans:property>
	<beans:property name="authenticationManager" ref="platformUserAuthenticationManager" />
	<beans:property name="accessDecisionManager" ref="accessDecisionManager" />
</beans:bean>

<beans:bean id="passwordGrantTypeUserAuthenticationFilter" class="com.forbesdigital.jee.oauth.spring.user.UserPasswordGrantTypeAuthenticationFilter">
	<beans:constructor-arg ref="platformUserAuthenticationManager" />
</beans:bean>

<beans:bean id="platformUserAuthenticationManager" class="org.springframework.security.authentication.ProviderManager">
	<beans:property name="providers">
		<beans:list>
			<beans:ref bean="platformUserAuthenticationProvider" />
		</beans:list>
	</beans:property>
</beans:bean>

<beans:bean id="platformUserAuthenticationProvider" class="org.springframework.security.authentication.dao.DaoAuthenticationProvider">
	<beans:property name="passwordEncoder" ref="platformUserPasswordEncoder" />
	<beans:property name="userDetailsService" ref="platformUserDetailsService" />
	<beans:property name="saltSource" ref="platformUserSaltSource" />
</beans:bean>

<beans:bean id="platformUserSaltSource" class="org.springframework.security.authentication.dao.ReflectionSaltSource">
	<beans:property name="userPropertyToUse" value="salt" />
</beans:bean>

<beans:bean	id="platformUserDetailsService"	class="com.forbesdigital.jee.oauth.spring.user.OAuthUserDetailsService">
	<beans:property name="userDetailsBuilder" ref="platformUserService" />
</beans:bean>

<beans:bean id="platformUserPasswordEncoder" class="org.springframework.security.authentication.encoding.ShaPasswordEncoder">
	<beans:constructor-arg name="strength" value="256" />
</beans:bean>
<!--
	USER END
-->

<!--
	CLIENT START
-->
<beans:bean id="clientExceptionTranslatorFilter" class="com.forbesdigital.jee.oauth.spring.client.ClientExceptionTranslationFilter" />	

<beans:bean id="grantTypeCheckFilter" class="com.forbesdigital.jee.oauth.spring.client.GrantTypeCheckFilter"/>

<beans:bean id="clientFilterSecurityInterceptor" class="org.springframework.security.web.access.intercept.FilterSecurityInterceptor">
	<beans:property name="securityMetadataSource">
		<security:filter-security-metadata-source>
			<security:intercept-url pattern="/.*" access="ROLE_TEST" />
		</security:filter-security-metadata-source>
	</beans:property>
	<beans:property name="authenticationManager" ref="clientAuthenticationManager" />
	<beans:property name="accessDecisionManager" ref="accessDecisionManager" />
</beans:bean>

<beans:bean id="clientAuthenticationFilter" class="com.forbesdigital.jee.oauth.spring.client.ClientBasicAuthenticationFilter">
	<beans:constructor-arg ref="clientAuthenticationManager" />
</beans:bean>

<beans:bean id="clientAuthenticationManager" class="org.springframework.security.authentication.ProviderManager">
	<beans:property name="providers">
		<beans:list>
			<beans:ref bean="clientAuthenticationProvider" />
		</beans:list>
	</beans:property>
</beans:bean>

<beans:bean id="clientAuthenticationProvider" class="org.springframework.security.authentication.dao.DaoAuthenticationProvider">
	<beans:property name="userDetailsService" ref="clientDetailsService" />
</beans:bean>

<beans:bean	id="clientDetailsService" class="com.forbesdigital.jee.oauth.spring.client.OAuthClientDetailsService">
	<beans:property name="clientDetailsBuilder" ref="clientService" />
</beans:bean>
<!--
	CLIENT END
-->
```

For the first two beans to work you need to also add the following to your `web.xml` file:

```xml
<ejb-local-ref>
	<ejb-ref-name>ejb/SpringPlatformUserService</ejb-ref-name>
	<ejb-ref-type>Session</ejb-ref-type>
	<local>com.lotaris.dcc.infra.security.basic.ISpringPlatformUserService</local>
	<ejb-link>SpringPlatformUserService</ejb-link>
</ejb-local-ref>
<ejb-local-ref>
	<ejb-ref-name>ejb/SpringClientService</ejb-ref-name>
	<ejb-ref-type>Session</ejb-ref-type>
	<local>com.lotaris.dcc.infra.security.basic.ISpringClientService</local>
	<ejb-link>SpringClientService</ejb-link>
</ejb-local-ref>
```

Then configure the required filters for the request token endpoint

```xml
<beans:bean id="filterChainProxy" class="org.springframework.security.web.FilterChainProxy">
	<security:filter-chain-map request-matcher="regex">
		<!-- Other patterns here - Please note that the resolution of the patterns is done top -> down -->
		<security:filter-chain pattern="/api/oauth/token.*" filters="securityContextFilter, clientExceptionTranslatorFilter, clientAuthenticationFilter, grantTypeCheckFilter, passwordGrantTypeUserAuthenticationFilter, clientFilterSecurityInterceptor" />
		<!-- Other patterns here -->
	</security:filter-chain-map>s
</beans:bean>
```

You should be ready to request OAuth tokens at this point.



[AbstractAccessTokenResource]: src/main/java/com/forbesdigital/jee/oauth/rest/api/AbstractAccessTokenResource.java
[OAuthClientDetails]:  src/main/java/com/forbesdigital/jee/oauth/spring/client/OAuthClientDetails.java
[OAuthUserDetails]: src/main/java/com/forbesdigital/jee/oauth/spring/user/OAuthUserDetails.java
[IOAuthClientDetailsBuilder]: src/main/java/com/forbesdigital/jee/oauth/spring/client/IOAuthClientDetailsBuilder.java
[IOAuthUserDetailsBuilder]: src/main/java/com/forbesdigital/jee/oauth/spring/user/IOAuthUserDetailsBuilder.java