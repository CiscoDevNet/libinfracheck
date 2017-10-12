# InfraCheck Example

This project demonstrates using network infrastructure as part of a healthcheck for an application.

In this project, there are two separate modules:

* backend: A Jersey-based web service
* libinfracheck: A Java library that can will check the health of infrastructure using APIC-EM Path Trace

# libinfracheck

After authenticating and receiving a "service ticket", the library will determine network health using the `/api/v1/flow-analysis/` API from APIC-EM 1.3.


# Usage

From the terminal:

* Navigate to `libinfracheck/`
* Execute `mvn clean install`
* Within the `backend`, execute `mvn clean tomcat7:run`

From IntelliJ:

* Open the Maven Projects control tab
* root > > Install 
* backend > Plugins > tomcat7 > tomcat:run

From your browser:

* `http://localhost:8080/health`
* Response should respond with a `500` error and body contains a health error message.
