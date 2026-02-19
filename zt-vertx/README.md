Ziti Module for Vert.x
---------------------

This module allows using Ziti with vert.x.

It provides a Transport implementation to connect or bind over
Hanzo ZT overlay network.

## How to use it
- add dependency
```groovy
dependencies {
    implementation "org.hanzozt:zt-vertx:+" // use latest version
}
```
- configure vertx instance with [ZitiTransport](src/main/kotlin/org/hanzozt/vertx/ZitiTransport.kt) 
then use vertx instance as usual
```kotlin
val ztx = Ziti.newContext(identityFile, charArrayOf())
val binding = mapOf(8080 to ZitiAddress.Bind(service = serviceName))

val vertx = VertxBuilder()
    .transport(ZitiTransport(ztx, binding))
    .init()
    .vertx()
```

See sample [server](src/samples/kotlin/org/hanzozt/vertx/sample/EchoServer.kt)