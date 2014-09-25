# Define OAuth configuration

In this step you need to define the OAuth behaviour of your app, more exactlty the lists of accepted roles, scopes and grant types, and also some relations between them. This should be done in a class which implements [IOAuthConfiguration][IOAuthConfiguration] interface.

## Select OAuth grant types

Choose from the grant types offered by the library which ones do you want to use in your application. The grant types supported by the OAuth library can be found in [EOAuthGrantType][EOAuthGrantType] enum.

```java
	// define list of supported grant types
	grantTypes = new TreeSet<>();
	grantTypes.add(EOAuthGrantType.CLIENT_CREDENTIALS);
	grantTypes.add(EOAuthGrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS);
```

The selected list of grant types need to be returned as set of [EOAuthGrantType][EOAuthGrantType] in the method `getAllGrantTypes()`.

```java
	@Override
	public Set<EOAuthGrantType> getAllGrantTypes() {
		return grantTypes;
	}
```

## Define OAuth scopes

Define the complete list of OAuth scopes that you want to use in your application. A scope is used to grant access to a certain set of resources on your application. These can be a set of string constants put in a separate class.

```java
public static final class Scopes {
	
	/**
	 * Represents the trusted access. This scope offers access to all resources which can
	 * be accessed from a trusted client.
	 */
	public static final String TRUSTED_ACCESS = "TRUSTED_ACCESS";
	
	/**
	 * Represents the secure client access. This scope offers access to all resources
	 * accessible for a secure client.
	 */
	public static final String ADVANCED_ACCESS = "ADVANCED_ACCESS";
	
	/**
	 * Represents the insecure client access. This scope offers access to all GET resources.
	 */
	public static final String BASIC_ACCESS = "BASIC_ACCESS";
	
	/**
	 * Represents the authenticated user access. This scope offers access to all
	 * resources which can be accessed by an authenticated user.
	 */
	public static final String AUTHENTICATED_ACCESS = "AUTHENTICATED_ACCESS";
}
```

Then create a set with the allowed scopes:

```java
	// define allowed scopes
	scopes = new TreeSet<>();
	scopes.add(BASIC_ACCESS);
	scopes.add(ADVANCED_ACCESS);
	scopes.add(AUTHENTICATED_ACCESS);
	scopes.add(TRUSTED_ACCESS);
```
And return the set in the method `getAllScopes()`.

```java
	@Override
	public Set<String> getAllScopes() {
		return scopes;
	}
```
At this step you can also configure the allowed scopes for each grant type and to return them in the `getAllowedScopes(EOAuthGrantType grantType)` method.

```java
		// define allowed scopes per grant type
		scopesPerGrantType = new TreeMap<>();
		Set<String> clientCredentialsGrantTypeScopes = new TreeSet<>();
		clientCredentialsGrantTypeScopes.add(BASIC_ACCESS);
		clientCredentialsGrantTypeScopes.add(ADVANCED_ACCESS);
		clientCredentialsGrantTypeScopes.add(TRUSTED_ACCESS);
		scopesPerGrantType.put(EOAuthGrantType.CLIENT_CREDENTIALS, clientCredentialsGrantTypeScopes);
		Set<String> passwordGrantTypeScopes = new TreeSet<>();
		passwordGrantTypeScopes.add(BASIC_ACCESS);
		passwordGrantTypeScopes.add(ADVANCED_ACCESS);
		passwordGrantTypeScopes.add(AUTHENTICATED_ACCESS);
		passwordGrantTypeScopes.add(TRUSTED_ACCESS);
		scopesPerGrantType.put(EOAuthGrantType.RESOURCE_OWNER_PASSWORD_CREDENTIALS, passwordGrantTypeScopes);
```

```java
	@Override
	public Set<String> getAllowedScopes(EOAuthGrantType grantType) {
		return scopesPerGrantType.get(grantType);
	}
```

## Define OAuth client roles

Define first the names for OAuth client role that you want to use in you application. This can to be a set of String constants.

```java
	public static final String UNSECURE_CLIENT_ROLE = "UNSECURE_CLIENT";
	public static final String SECURE_CLIENT_ROLE   = "SECURE_CLIENT";
	public static final String TRUSTED_CLIENT_ROLE  = "TRUSTED_CLIENT";
```

For each client role create a class which implements [IOAuthClientRole][IOAuthClientRole] and configure the name, the allowed scopes and grant types and the default token lifetime.

```java
private static final class RoleSecure implements IOAuthClientRole {
	
	private final Set<String> oauthScopes;
	private final Set<EOAuthGrantType> grantTypes;
	
	public RoleSecure() {
	
		oauthScopes = new TreeSet<>();
		oauthScopes.add(BASIC_ACCESS);
		oauthScopes.add(ADVANCED_ACCESS);
		
		grantTypes = new TreeSet<>();
		grantTypes.add(EOAuthGrantType.CLIENT_CREDENTIALS);
	}
	
	@Override
	public String getName() {
		return SECURE_CLIENT_ROLE;
	}
	
	@Override
	public Set<String> getAllowedScopes() {
		return oauthScopes;
	}
	
	@Override
	public Set<EOAuthGrantType> getAllowedOAuthGrantTypes() {
		return grantTypes;
	}
	
	@Override
	public Integer getTokenLifetime() {
		return 7 * 24 * 60 * 60; // Token valid for 7 days.
	}
}
```

Then implement the `getClientRoleNames()` method to return the full set of client role names and `getClientRole(String name)` method to return a Client Role by its name.

```java
		// define the client roles
		clientRoles = new TreeMap<>();
		clientRoles.put(UNSECURE_CLIENT_ROLE, new RoleInsecure());
		clientRoles.put(SECURE_CLIENT_ROLE, new RoleSecure());
		clientRoles.put(TRUSTED_CLIENT_ROLE, new RoleTrusted());
```

```java
	@Override
	public Set<String> getClientRoleNames() {
		return clientRoles.keySet();
	}

	@Override
	public IOAuthClientRole getClientRole(String name) {
		return clientRoles.get(name);
	}		
```

## Register Oauth configuration

Now that we have the Oauth configuration done, the next step is to register it in the OAuth context so that it can be available both for OAuth lib classes but also in your code.


You have to provide an implementation for [AbstractOAuthConfigurationListener][AbstractOAuthConfigurationListener] class in your application and register it as a Web listener. To do this, create a class in your WAR project and annotate it with `WebListener` as follows:

```java
@WebListener
public class OAuthListener extends AbstractOAuthConfigurationListener {

	@Override
	public IOAuthConfiguration getOAuthConfiguration() {
		return new OAuthConfiguration();
	}
}
```

## Access the OAuth configuration

Now you should be able to access the Oauth configuration everywhere in you application by using the [OAuthContext][OAuthContext] class.

```java
	OAuthContext.getConfig().getAllGrantTypes()
	OAuthContext.getConfig().getAllScopes()
	OAuthContext.getConfig().getAllowedScopes(grantType)
	OAuthContext.getConfig().getClientRole(clientRole)
```



[IOAuthConfiguration]: src/main/java/com/forbesdigital/jee/oauth/configuration/IOAuthConfiguration.java
[EOAuthGrantType]: src/main/java/com/forbesdigital/jee/oauth/configuration/EOAuthGrantType.java
[IOAuthClientRole]: src/main/java/com/forbesdigital/jee/oauth/configuration/IOAuthClientRole.java
[AbstractOAuthConfigurationListener]: src/main/java/com/forbesdigital/jee/oauth/configuration/AbstractOAuthConfigurationListener.java
[OAuthContext]: src/main/java/com/forbesdigital/jee/oauth/configuration/OAuthContext.java