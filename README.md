# libinfracheck

**Description**: This project demonstrates using network infrastructure as part of a healthcheck for an application.  The implementation here is a a Java library that can check the health of infrastructure using Path Trace.

This project is also an example of "infrastructure intent".  That is, the business requirement of application reliability can be assessed, in part, through DNA Center's information about network devices and routes.  This project can be extended and tailored to meet additional business requirements and interact with other systems to contribute to a perspective on overall system health.  

## Dependencies

* Java 1.6+
* Maven or Gradle

# Spring Boot Usage Example

A [spring-boot example](https://github.com/ciscodevnet/infraspringboot) is available.


# High level flow

After authenticating and receiving a "service ticket", the library will determine network health using the `/api/v1/flow-analysis/` API.

# Installation

From the terminal:

* Navigate to `libinfracheck/`
* Execute `mvn clean install`

## Creating a "flow analysis"

You must have an existing flow analysis ID to be used in the project.  

To create one, you may use the `createPathTrace` API in this project, or simply use cURL or another HTTP client like Postman.

```
curl -X POST \
  https://192.168.139.73/api/v1/flow-analysis \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -H 'x-auth-token: '<insert your auth token here>' \
  -d '{ "sourceIP": "10.10.22.98", "destIP": "10.10.22.114"}'
```


## Using libinfracheck in another project

* Navigate to `libinfracheck/`
* Execute `mvn clean install`

Add the dependency to *libinfracheck* in your `pom.xml`

```xml
    <dependency>
        <groupId>com.cisco.devnet</groupId>
        <artifactId>libinfracheck</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```

or in `build.gradle`:

dependencies {
	compile("com.cisco.devnet:libinfracheck:1.0-SNAPSHOT")
}

Instantiate a new InfraCheck object:

```java
    InfraCheck health = new InfraCheck();
```

InfraCheck exposes several APIs:

`setConfig()` which requires the URL for your API root

`getTicket()` which returns a String object token from the Path Trace API

`pathCheck(token)` which returns an `HttpResponse<JsonNode>`. Use the [Unirest](http://unirest.io) functions to navigate the JSON object that is returned.  For example, `.getBody().getObject().getJSONObject("response").getJSONObject("request).getString("status");`.

Example:
```java

        HttpResponse<JsonNode> healthObject;
        InfraCheck health = new InfraCheck();

        try {
            health.setConfig("https://sandboxapicem.cisco.com/api");
            String token = health.getTicket("devnetuser", "Cisco123!");

            healthObject = health.pathCheck(token);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
```

## Known issues

* If you are running against a server with a self-signed certificate, you will need to add that certificate to the Java truststore (see https://stackoverflow.com/questions/9619030/resolving-javax-net-ssl-sslhandshakeexception-sun-security-validator-validatore?rq=1)

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## License
[LICENSE](LICENSE)

## Getting involved

* If you'd like to make contributions, feel free to make a request in the issue tracker.  If you have other questions, contact Ashley Roach (asroach at cisco.com).

## Credits and references

* Cisco DNA Center API: https://developer.cisco.com/site/dna-center/
* DevNet Sandbox: https://devnetsandbox.cisco.com
