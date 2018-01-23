# libinfracheck

**Description**: This project demonstrates using network infrastructure as part of a healthcheck for an application.  The implementation here is a a Java library that can check the health of infrastructure using APIC-EM Path Trace.

## Dependencies

* Java 1.6+
* Maven

# Spring Boot Usage Example

A [spring-boot example](https://wwwin-github.cisco.com/asroach/infracheck-spring-boot) is available.


# High level flow

After authenticating and receiving a "service ticket", the library will determine network health using the `/api/v1/flow-analysis/` API from APIC-EM 1.3.


# Installation

From the terminal:

* Navigate to `libinfracheck/`
* Execute `mvn clean install`


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

InfraCheck exposes two APIs:

`getTicket()` which returns a String object token from APIC-EM

`pathCheck(token)` which returns an `HttpResponse<JsonNode>`. Use the [Unirest](http://unirest.io) functions to navigate the JSON object that is returned.  For example, `.getBody().getObject().getJSONObject("response").getJSONObject("request).getString("status");`.

Example:
```java

        HttpResponse<JsonNode> healthObject;
        InfraCheck health = new InfraCheck();

        try {

            String token = health.getTicket();

            healthObject = health.pathCheck(token);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
```

## Known issues

* None

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## License
[LICENSE](LICENSE)

## Getting involved

* If you'd like to make contributions, feel free to make a request in the issue tracker.  If you're interested in creating a Cisco DevNet Learning Lab, contact Ashley Roach (asroach at cisco.com).

## Credits and references

* APIC-EM API:  https://developer.cisco.com/site/apic-em/
* Cisco DNA Center API: https://developer.cisco.com/site/dna-center/
* DevNet Sandbox: https://devnetsandbox.cisco.com