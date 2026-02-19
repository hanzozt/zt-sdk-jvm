This component allows to Zitify a spring boot application.

Project goals:
* make spring boot rest template able to connect to a Ziti service


## Configuration
To enable auto configuration of the Ziti beans add `@EnableZitiHttpClient` to either a @Configuration class or your main application.
This configuration will add both a RestTemplate based bean and a RestClient based bean.
```java
@SpringBootApplication
@EnableZitiHttpClient
public class SampleApplication {

  public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}
	
}
```
All zt related beans are created with a prefix of `zt`. Overriding any of them is done by adding a @Configuration with a @Bean("zt...") and that will be used instead of the auto configured one.

### Simple Ziti file based context
To connect using a zt connection add a zt section to your config:
```yaml
spring:
  zt:
    client:
      identity:
        file: classpath:client.zid
```
the file `client.zid` must be an enrolled token.
Then to use the zt rest template you autowire it as usual but with a qualifier.
```java
@Configuration
public class RestTemplateCaller {
  
  @Autowired
  public RestTemplateCaller(@Qualifier("ztRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

}
```
The zt rest template is now ready to use just like any other rest template but it will resolve addresses and connect to services through your zt controller.
Highly recommended not to use it for other public/external services even if it works to do so.
If you prefer the to use the RestClient use the following configuration.
```java
@Configuration
public class RestClientCaller {
  
  @Autowired
  public RestClientCaller(@Qualifier("ztRestClient") RestClient restClient) {
    this.restClient = restClient;
  }

}
```
To read more about zt services please see [zt service](https://hanzozt.github.io/zt/services/overview.html).

### Ziti identity from other sources
To override the socket factory in order to provide your own identity you can do the following:
```java
@Configuration
public class SampleConfiguration {

  private ZitiConnectionSocketFactory ztConnectionSocketFactory;

  @Bean("ztConnectionSocketFactory")
  public ZitiConnectionSocketFactory connectionSocketFactory() throws IOException {
    if (ztConnectionSocketFactory == null) {
      try (InputStream is = /* insert input stream creation here */ ) {
        ztConnectionSocketFactory = new ZitiConnectionSocketFactory(is);
      }
    }
    return ztConnectionSocketFactory;
  }

  @PreDestroy
  public void destroy() {
    Optional.ofNullable(ztConnectionSocketFactory).ifPresent(ZitiConnectionSocketFactory::shutdown);
  }

}
```
The placeholder above should be replaced with your own input stream for an identity.
One such example would be to read from a secrets manager or a vault where the identity is stored securely.

### Overriding client or builder features
The configuration can also include a RestTemplate, RestTemplateBuilder, RestClient, or RestClient.Builder bean(s) in order to customize them.
```java
@Configuration
public class SampleConfiguration {

  @Bean("ztRestTemplate")
  public RestTemplate restTemplate(@Qualifier("ztRestTemplateBuilder") RestTemplateBuilder restTemplateBuilder) {
    // customizations
    return restTemplateBuilder.build();
  }

}
```
or
```java
@Configuration
public class SampleConfiguration {

  @Bean("ztRestTemplateBuilder")
  public RestTemplateBuilder restTemplateBuilder(@Qualifier("ztHttpClient") HttpClient httpClient) {
    return new RestTemplateBuilder()
        // customizations
        .requestFactory(() -> clientHttpRequestFactory(httpClient));
  }

}
```
or
```java
@Configuration
public class SampleConfiguration {

@Bean("ztRestClient")
public RestTemplate restTemplate(@Qualifier("ztRestClientBuilder") RestTemplateBuilder restTemplateBuilder) {
// customizations
return restClientBuilder.build();
}

}
```
or
```java
@Configuration
public class SampleConfiguration {

  @Bean("ztRestClientBuilder")
  public RestClient.Builder restClientBuilder(@Qualifier("ztHttpClient") HttpClient httpClient) {
    return new RestClient.Builder()
        // customizations
        .requestFactory(() -> clientHttpRequestFactory(httpClient));
  }

}
```

## Application code change
Add `zt-springboot-client` dependency
```groovy
implementation "org.hanzozt:zt-springboot-client:+"
```
Autowire the 'ztRestTemplate' into your application and use it to connect to zt services.

Try the [complete sample](../samples/zt-spring-boot-client)

### Reference Documentation
* [zt service](https://hanzozt.github.io/zt/services/overview.html)

For further reference, please consider the following sections:
* [REST Clients](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html)

An example of running a service is in our samples
* Ziti Spring Server [sample](../samples/zt-spring-boot)
