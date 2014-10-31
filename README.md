# jee-oauth

> OAuth library based on the mechanism from Spring Security

## Building

1. Clone the repository.

2. Run the following command

```bash
cd <projectFolder>
mvn clean install
```

## Introduction

This documentation presents all you need to configure/implement in order to integrate the OAuth library in your application. Throughtout the documentation we assume that the reader is familiar with OAuth 2.0 specification and terms. To learn more about OAuth 2.0, please refer to the <a href="http://tools.ietf.org/html/rfc6749" target="_blank">reference documentation</a>.

## Bootstrapping the Library in a Maven Project

In a standard Maven multi-module project like we have (EAR / EJB / WAR / JAR), you'll need to setup the dependency as
follows.

The first thing to do is to add the dependency in the `dependencyManagement` section in the `<artifactIdPrefix>/pom.xml`.
You can copy/paste the following dependency definition:

```xml
<!-- Rest -->
<dependency>
	<groupId>com.forbesdigital.jee</groupId>
	<artifactId>jee-oauth</artifactId>
	<version>[[ version ]]</version>
</dependency>
```

**Note:** Replace `[[ version ]]` by the correct version you need in your project. At each version update, you can then
bump the version in here. This avoids tricky issues where different versions are defined for a same dependency.

Secondly, you'll need to put the dependency in your EJB and EJB-Test modules. (`<artifactIdPrefix>/<artifactIdPrefix>-ejb/pom.xml`
and `<artifactIdPrefix>/<artifactIdPrefix>-ejb-test/pom.xml`). This time, you will add the dependency under
`dependencies`:

```xml
<dependency>
	<groupId>com.forbesdigital.jee</groupId>
	<artifactId>jee-oauth</artifactId>
	<scope>provided</scope>
</dependency>
```

**Note:** You will not specify the version because this already done in the parent `pom.xml` file. This means that the
version is inherited. The `<scope>` is there to manage properly the packaging and the dependencies packaged in the
different jar/war/ear files.

Finally, you need to put the dependency in your WAR and WAR-Test modules. (`<artifactIdPrefix>/<artifactIdPrefix>-war/pom.xml`
and `<artifactIdPrefix>/<artifactIdPrefix>-war-test/pom.xml`). Again, dependency goes under `dependencies`:

```xml
<dependency>
	<groupId>com.forbesdigital.jee</groupId>
	<artifactId>jee-oauth</artifactId>
</dependency>
```

**Note:** No `<version>` for the same reason than before. No `<scope>` because we need to package the dependency in the
war.

## Bootstrapping the Library in the Code

Next, we present step by step what needs to be done in order to integrate the library in a new or existing application. Each step is a link to a separate page where the step will be explained in details. These pages will also contain code examples to ease the integration process.


[1. Define OAuth configuration](doc/define-configuration.md)

[2. Define OAuth entities](doc/define-entities.md)

[3. Request OAuth tokens](doc/request-tokens.md)

[4. Use OAuth tokens](doc/use-tokens.md)

[5. Enforce OAuth scopes](doc/enforce-scopes.md)

## Contributing

* [Fork](https://help.github.com/articles/fork-a-repo)
* Create a topic branch - `git checkout -b feature`
* Push to your branch - `git push origin feature`
* Create a [pull request](http://help.github.com/pull-requests/) from your branch

Please add a changelog entry with your name for new features and bug fixes.

## License

**jee-oauth** is licensed under the [MIT License](http://opensource.org/licenses/MIT).
See [LICENSE.txt](LICENSE.txt) for the full text.
