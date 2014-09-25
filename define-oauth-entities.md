#Define OAuth entities

Based on the defined OAuth configuration, in this step you should define what OAuth entities you want to use in your application. The solution for the storage of the entities is left to you to decide (hardoced, in memory, in DB, ...). 


## Define Client entity

The Oauth Client entity will be used for authorization when requestiong an OAuth Token. It has to implement the [IOAuthClient][IOAuthClient] interface and to provide implementation for `getClientRole()` and `getTokenLifetime()` methods.

A client also needs to have one or more sets of OAuth credentials (client_id & client_secret pairs). These can be either properties of the Client entity.

```java
public class Client implements IOAuthClient {
	private String clientId;
	private String clientSecret;
}	
```

Or they can be put in a separate entity, lets say `OAUthClientCredentials` and the Client entity can have a list of such credentials entities. This will be usefull in case you want to so some versioning on your clients.

```java

public class OAuthCredentials {
	private String clientId;
	private String clientSecret;
}

public class Client implements IOAuthClient {
	private List<OAuthCredentials> oAuthCredentials;
}
```

The Client entity can have other properties, like `name` , `description`, whatever your application business requires.

## Define User entity - optional

A User entity is required only if you want to use the `password` grant type. In this case a set of User credentials (username & password) need to be sent when requesting a token. This entity has to inplement the marker interface [IOAuthUser][IOAuthUser]. No specific method needs to be implemented.

## Define Token entity

The Token entity will be used to store information about an OAuth Token generated for a Client. It has to implement the [IOAuthToken][IOAuthToken] interface and to provide implementation for `getAccessToken()`, `getTokenType()`, `getExpiresIn()`, `getExpirationDate()` and `getScopes()` methods.  



[IOAuthClient]: src/main/java/com/forbesdigital/jee/oauth/model/IOAuthClient.java
[IOAuthUser]: src/main/java/com/forbesdigital/jee/oauth/model/IOAuthUser.java
[IOAuthToken]: src/main/java/com/forbesdigital/jee/oauth/model/IOAuthToken.java