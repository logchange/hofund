# 🗡️💥 hofund 💥🗡️

<p align="center">
    <a href="https://github.com/logchange/hofund/graphs/contributors" alt="Contributors">
        <img src="https://img.shields.io/github/contributors/logchange/hofund" /></a>
    <a href="https://github.com/logchange/hofund/pulse" alt="Activity">
        <img src="https://img.shields.io/github/commit-activity/m/logchange/hofund" /></a>
    <a href="https://search.maven.org/search?q=g:%22dev.logchange.hofund%22%20AND%20a:%22hofund-spring-boot-starter%22" alt="Maven Central">
        <img src="https://img.shields.io/maven-central/v/dev.logchange.hofund/hofund-spring-boot-starter.svg?label=Maven%20Central" /></a>
</p>


<div align="center">
  <img src="https://github.com/logchange/hofund/raw/master/hofund.gif" />
</div>

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

# Compatibility

|  Version  |           SpringBoot Version            |
|:---------:|:---------------------------------------:|
| **0.X.0** | from **2.2.0** (including 3.0.0 and up) |

# Requirements

You can check following requirements by running `mvn dependency:tree`, but if you are using `spring-boot` in version at
least `2.2.0` everything should be alright.

Your project has to contain:

- spring-framework in version at least 5.2.12.RELEASE
- spring-boot in version at least 2.2.0
- micrometer-io in version at least 1.3.0
- slf4j in version at least 1.7.28

# Usage

## SpringBoot based projects

### 1. Add to your pom.xml:

```xml
<project>
   ...
   <dependencies>
      ...
       <dependency>
           <groupId>dev.logchange.hofund</groupId>
           <artifactId>hofund-spring-boot-starter</artifactId>
           <version>0.3.0</version>
       </dependency>
      ...
   </dependencies>
   
   <build>
       ...
       <plugins>
          ...
          <plugin>
             <!-- For multi-module projects this plugin should be in module which produce final package (.jar/.war/.ear)  -->
             <groupId>pl.project13.maven</groupId>
             <artifactId>git-commit-id-plugin</artifactId>
             <version>4.9.10</version> <!-- for java 11 you can use 5.0.0 (https://github.com/git-commit-id/git-commit-id-maven-plugin#relocation-of-the-project) -->
             <configuration>
                <failOnNoGitDirectory>false</failOnNoGitDirectory>
                <injectAllReactorProjects>true</injectAllReactorProjects>
             </configuration>
          </plugin>
          ...
       </plugins>
       ...
   </build>
   ...
</project>
```

### 2. Check if your project already contains SpringBoot Actuator with Micrometer or add it:

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

And your application should have the following configuration:

```properties
management.endpoints.web.exposure.include=prometheus
```

or

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "prometheus"
```

To define basic information about our application in hofund metric we need some configuration.
For maven project add to your `application.properties` following entries, but you can define it as you wish:

```properties
hofund.info.application.name=@project.name@
hofund.info.application.version=@project.version@

hofund.git-info.commit.id=@git.commit.id@
hofund.git-info.commit.id-abbrev=@git.commit.id.abbrev@
hofund.git-info.dirty=@git.dirty@
hofund.git-info.branch=@git.branch@
hofund.git-info.build.host=@git.build.host@
hofund.git-info.build.time=@git.build.time@
```

or

```yaml
hofund:
  info:
    application:
      name: @project.name@
      version: @project.version@
  git-info:
    commit:
      id: @git.commit.id@
      id-abbrev: @git.commit.id.abbrev@
    dirty: @git.dirty@
    branch: @git.branch@
    build:
      host: @git.build.host@
      time: @git.build.time@
```

### 3. Now you can start your application and verify exposed prometheus metric:

```text
# HELP hofund_info Basic information about application
# TYPE hofund_info gauge
hofund_info{application_name="cart",application_version="1.0.4-SNAPSHOT",id="cart",} 1.0
# HELP hofund_connection Current status of given connection
# TYPE hofund_connection gauge
hofund_connection{id="cart-cart_database",source="cart",target="cart_database",type="database",} 1.0
# HELP hofund_git_info Basic information about application based on git
# TYPE hofund_git_info gauge
hofund_git_info{branch="master",build_host="DESKTOP-AAAAA",build_time="2023-02-19T11:22:34+0100",commit_id="0d32d0f",dirty="true",} 1.0
```

### 4. Metrics description

   - `hofund_info` - used to detect if application is running and what version is used. Application name and id
     in the metric is always lowercase to make easier creation of connection graph.

   - `hofund_connection` - used to examine connection status to given services such as databases, rest apis etc.
     Source is a name of the current application (lowercase) and target is a name of service that we want to connect
     to joint with target type(also lowercase). Id is created by joining this two properties.

   - `hofund_git_info` - used to inform about build and git-based information such as: commit id(short),
     dirtiness(dirty - uncommitted changes), build machine name and time, branch. This information is useful
     for sandbox environments, where everything is changing really fast.

### 5. Currently supported spring datasource's for auto-detection and providing `hofund_connection`:
    - PostgreSQL
    - Oracle

# Contribution

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/logchange/hofund)
