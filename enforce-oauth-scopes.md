# enforce-oauth-scopes

The last step is to configure the required scopes for all your API calls and to enable scopes validation when an endpoint is accessed.

## Provided Annotations

The library provides two annotations which should be used to configure the scopes required for each API call. These annotations are: [AllOAuthScopes][AllOAuthScopes] and [AnyOAuthScopes][AnyOAuthScopes].

[AllOAuthScopes][AllOAuthScopes] should be used when you want to secify that a Token should have granted all the scopes from the specified list.

[AnyOAuthScopes][AnyOAuthScopes] should be used when you want to secify that a Token should have granted at least one of the scopes from the specified list.

## Annotate API calls

The annotations can be used in the following way:

When one scope is required for an api call:

```java
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@AllOAuthScopes({OAuthConfiguration.Scopes.BASIC_ACCESS})
	public Response retrievePrices(
```

When two scopea are required for an api call:

```java
	@AllOAuthScopes({OAuthConfiguration.Scopes.BASIC_ACCESS, OAuthConfiguration.Scopes.ADVANCED_ACCESS})
```

When one of two scopea is required for an api call:

```java
	@AnyOAuthScopes({OAuthConfiguration.Scopes.BASIC_ACCESS, OAuthConfiguration.Scopes.ADVANCED_ACCESS})
```

When no scope is required for an api call (public access):

```java
	@AllOAuthScopes({})
```

## Enable scopes validation 

In order to enable scopes validation for your API calls you need to register [OAuthScopeDynamicFeature][OAuthScopeDynamicFeature] provider in your rest application.

```java
import javax.ws.rs.core.Application;

public abstract class AbstractRestApplication extends Application {

	@Override
	public Set<Object> getSingletons() {
		final Set<Object> singletons = new HashSet<>(1);
		// Add other providers if needed
		singletons.add(new OAuthScopeDynamicFeature());
		return singletons;
	}

}
```


[AllOAuthScopes]: src/main/java/com/forbesdigital/jee/oauth/AllOAuthScopes.java
[AnyOAuthScopes]: src/main/java/com/forbesdigital/jee/oauth/AnyOAuthScopes.java
[OAuthScopeDynamicFeature]: src/main/java/com/forbesdigital/jee/oauth/OAuthScopeDynamicFeature.java