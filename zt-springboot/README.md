This component allows to Zitify a spring boot application.

Project goals:
* make spring boot service available directly on a Ziti network
* simplify ztfication work -- one-line-ztify


## Configuration
In order to bind service to a Ziti network the following configuration is needed
```properties
zt.id = <path to zt identity file>
zt.serviceName = <zt service to bind to>
```

## Application code change
Add `zt-springboot` dependency
```groovy
implementation "org.hanzozt:zt-springboot:+"
```

Add Ziti customizer class to your application.
`ZitiTomcatCustomizer` bootstraps Tomcat configuration and 
binds to [zt service](https://hanzozt.github.io/zt/services/overview.html#service-termination).

```kotlin
@SpringBootApplication(
    scanBasePackageClasses = [
        ZitiTomcatCustomizer::class, // without this line tomcat runs on default(localhost:8080)
        HelloController::class
    ]
)
class ZitiSpringBootApplication


@RestController
class HelloController {
    @GetMapping("/")
    fun index(): String {
        return "Greetings from ZprIng booTI"
    }
}
```

Try the [complete sample](../samples/zt-spring-boot)
