
OAuth Library Documentation
---

The Digital Commerce API uses OAuth 2.0 to manage access rights. 
This documentation presents all you need to configure/implement in order to integrate the OAuth library in your application. Throughtout the documentation we assume that the reader is familiar with OAuth 2.0 specification and terms. To learn more about OAuth 2.0, please refer to the <a href="http://tools.ietf.org/html/rfc6749" target="_blank">reference documentation</a>.

Prerequisits
-------------

// TODO - here should be listed all dependencies to other libs from the OAuth lib. And also the maven dependency that needed to be added for the lib:

```
	<dependency>
		<groupId>com.forbesdigital.jee</groupId>
		<artifactId>oauth</artifactId>
		<version>0.1.0</version>
	</dependency>	 
```

Integrate OAuth library
-------------------------

Next, we present step by step what needs to be done in order to integrate the library in a new or existing application. Each step is a link to a separate page where it will be explained in details. These pages will also contain code examples to ease the integration process.
 

[1. Define OAuth configuration](#define-oauth-configuration)

[2. Define OAuth entities](/define-oauth-entities)

[3. Request OAuth tokens](/request-oauth-tokens)

[4. Use OAuth tokens](/use-oath-tokens)

[5. Enforce OAuth scopes](/enforce-oauth-scopes)


1. Define OAuth configuration
--------------------

//TODO