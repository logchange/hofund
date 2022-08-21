# 🗡️💥 hofund 💥🗡️

<p align="center">
    <a href="https://github.com/logchange/hofund/graphs/contributors" alt="Contributors">
        <img src="https://img.shields.io/github/contributors/logchange/hofund" /></a>
    <a href="https://github.com/logchange/hofund/pulse" alt="Activity">
        <img src="https://img.shields.io/github/commit-activity/m/logchange/hofund" /></a>
    <a href="https://search.maven.org/search?q=g:%22dev.logchange.hofund%22%20AND%20a:%22hofund-spring-boot-starter%22" alt="Maven Central">
        <img src="https://img.shields.io/maven-central/v/dev.logchange.hofund/hofund-spring-boot-starter.svg?label=Maven%20Central" /></a>
</p>

- [pronunciation in Old Norse](https://forvo.com/word/h%C7%ABfu%C3%B0/) also you can pronunce it as`ho` `fund`
- is a tool set to monitor applications, connections and discover current state of components of the system


```
Hǫfuð ("man-head," Norwegian hoved, Danish hoved, Swedish huvud and Icelandic höfuð) 
is the sword of Heimdallr.

"Where’s the sword? That sword is the key to opening the Bifrost." ― Hela

Hofund, often simply referred as the Bifrost Sword, was an Asgardian sword used 
by Heimdall and, during his exile, Skurge. 
It also served as a key to activate the switch that opens the Bifrost Bridge.
```

Compatible with Spring Boot >= 2.2.0 and Spring

### Requirements

You can check following requiremnts by running `mvn dependency:tree`, but if you are using `spring-boot` in version at
least `2.2.0` everthing should be alright.

Your project has to contain:

- spring-framework in version at least 5.2.12.RELEASE
- spring-boot in version at least 2.2.0
- micrometer-io in version at least 1.3.0
- slf4j in version at least 1.7.28

### Usage

#### SpringBoot based projects

1. Add to your pom.xml:

```xml
<dependencies>
    <dependency>
        <groupId>dev.logchange.hofund</groupId>
        <artifactId>hofund-spring-boot-starter</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

2. Your project already contains SpringBoot Actuator with Micrometer:

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-registry-prometheus</artifactId>
    </dependency>
</dependencies>
```

And your application has the following configuration:

```properties
management.endpoints.web.exposure.include=prometheus
```

To define basic information about our application in hofund metric we need some configuration.
For maven project add to your `application.properties` following entries, but you can define it as you wish:

```properties
hofund.info.application.name=@project.name@
hofund.info.application.version=@project.version@
```

3. Now you can start your application and verify exposed prometheus metric, it should include (example for postgres
   datasource):

```text
# HELP hofund_info_status Basic information about application
# TYPE hofund_info_status gauge
hofund_info_status{application_name="cart",application_version="1.0.4-SNAPSHOT",id="cart",} 1.0
# HELP hofund_connection_status Current status of given connection
# TYPE hofund_connection_status gauge
hofund_connection_status{id="cart-cart-database",source="cart",target="cart",type="database",} 1.0
```

4. Currently supported spring datasource's for auto-detection and providing `hofund_connection_status`:
   - PostgreSQL
   - Oracle
   
## Contribution

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/logchange/hofund)
