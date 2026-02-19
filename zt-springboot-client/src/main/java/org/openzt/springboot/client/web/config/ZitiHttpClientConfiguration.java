/*
 * Copyright (c) 2018-2022 NetFoundry Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hanzozt.springboot.client.web.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.SchemePortResolver;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionOperator;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HeaderElements;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.message.MessageSupport;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.hanzozt.Ziti;
import org.hanzozt.ZitiContext;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ZitiHttpClientConfiguration {

  // The default timeout when requesting a connection from the connection manager.
  private static final long DEFAULT_CONNECTION_REQUEST_TIMEOUT = 30 * 1000L;

  // The default time to wait for data, the maximum time between two consecutive packets of data.
  private static final int DEFAULT_RESPONSE_TIMEOUT = 60 * 1000;

  // The default time to keep a connection alive.
  private static final long DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000L;

  @ConditionalOnProperty(value = "spring.zt.client.rest-template.enabled", havingValue = "true", matchIfMissing = true)
  @Bean
  public RestTemplate ztRestTemplate(@Qualifier("ztRestTemplateBuilder") RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

  @ConditionalOnProperty(value = "spring.zt.client.rest-template-builder.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztRestTemplateBuilder")
  public RestTemplateBuilder restTemplateBuilder(@Qualifier("ztHttpClient") HttpClient httpClient) {
    return new RestTemplateBuilder()
        .requestFactory(() -> clientHttpRequestFactory(httpClient));
  }

  @ConditionalOnProperty(value = "spring.zt.client.rest-client.enabled", havingValue = "true", matchIfMissing = true)
  @Bean
  public RestClient ztRestClient(@Qualifier("ztRestClientBuilder") RestClient.Builder RestClientBuilder) {
    return RestClientBuilder.build();
  }

  @ConditionalOnProperty(value = "spring.zt.client.rest-client-builder.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztRestClientBuilder")
  public RestClient.Builder RestClientBuilder(@Qualifier("ztHttpClient") HttpClient httpClient) {
    return RestClient.builder()
        .requestFactory(clientHttpRequestFactory(httpClient));
  }

  @ConditionalOnProperty(value = "spring.zt.client.request-factory.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztClientHttpRequestFactory")
  public ClientHttpRequestFactory clientHttpRequestFactory(@Qualifier("ztHttpClient") HttpClient httpClient) {
    final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setHttpClient(httpClient);
    return clientHttpRequestFactory;
  }

  @ConditionalOnProperty(value = "spring.zt.client.context.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztContext")
  public ZitiContext context(@Value("${spring.zt.client.identity.file:}") Resource identityFile,
      @Value("${spring.zt.client.identity.password:}") String password) throws IOException {

    if (identityFile == null) {
      throw new BeanCreationException("No identity file specified. To use the default zt setup please add a property named "
                                      + "`spring.zt.client.identity.file` pointing to a zt identity file.");
    }

    return Ziti.newContext(identityFile.getInputStream(), password.toCharArray());
  }

  @ConditionalOnProperty(value = "spring.zt.client.tls-socket-strategy.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztTlsSocketStrategy")
  public TlsSocketStrategy ztTlsSocketStrategy() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    return new ZitiTlsSocketStrategy(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build());
  }

  @ConditionalOnProperty(value = "spring.zt.client.connection-manager.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztPoolingConnectionManager")
  public PoolingHttpClientConnectionManager poolingConnectionManager(
      @Qualifier("ztDnsResolver") DnsResolver ztDnsResolver,
      @Qualifier("ztTlsSocketStrategy") TlsSocketStrategy ztTlsSocketStrategy,
      @Value("${spring.zt.client.httpclient.max-total:}") Integer maxTotal,
      @Value("${spring.zt.client.httpclient.max-per-route:}") Integer maxPerRoute) {

    final PoolingHttpClientConnectionManagerBuilder connectionManagerBuilder = new PoolingHttpClientConnectionManagerBuilder() {
      @Override
      protected HttpClientConnectionOperator createConnectionOperator(
          SchemePortResolver schemePortResolver, DnsResolver dnsResolver, TlsSocketStrategy tlsSocketStrategy) {
        return new DefaultHttpClientConnectionOperator(
            proxy -> Ziti.getSocketFactory().createSocket(),
            schemePortResolver,
            dnsResolver,
            RegistryBuilder.<TlsSocketStrategy>create()
                .register(URIScheme.HTTPS.id, tlsSocketStrategy)
                .build());
      }
    };
    final PoolingHttpClientConnectionManager poolingConnectionManager = connectionManagerBuilder
        .setDnsResolver(ztDnsResolver)
        .setTlsSocketStrategy(ztTlsSocketStrategy)
        .setConnPoolPolicy(PoolReusePolicy.LIFO)
        .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
        .build();
    Optional.ofNullable(maxTotal).ifPresent(poolingConnectionManager::setMaxTotal);
    Optional.ofNullable(maxPerRoute).ifPresent(poolingConnectionManager::setDefaultMaxPerRoute);
    return poolingConnectionManager;
  }

  @ConditionalOnProperty(value = "spring.zt.client.connection-keep-alive-strategy.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztConnectionKeepAliveStrategy")
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(
      @Value("${spring.zt.client.httpclient.keep-alive-time:}") Long keepAliveTime) {
    return (response, context) -> {
      final Iterator<HeaderElement> it = MessageSupport.iterate(response, HeaderElements.KEEP_ALIVE);
      while (it.hasNext()) {
        final HeaderElement he = it.next();
        final String param = he.getName();
        final String value = he.getValue();

        if (value != null && param.equalsIgnoreCase("timeout")) {
          return TimeValue.of(Long.parseLong(value), TimeUnit.SECONDS);
        }
      }
      return TimeValue.of(Optional.ofNullable(keepAliveTime).orElse(DEFAULT_KEEP_ALIVE_TIME_MILLIS), TimeUnit.MILLISECONDS);
    };
  }

  @ConditionalOnProperty(value = "spring.zt.client.http-client.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztHttpClient")
  public CloseableHttpClient httpClient(
      @Qualifier("ztPoolingConnectionManager") PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
      @Qualifier("ztConnectionKeepAliveStrategy") ConnectionKeepAliveStrategy connectionKeepAliveStrategy,
      @Value("${spring.zt.client.httpclient.connection-request-timeout:}") Long connectionRequestTimeout,
      @Value("${spring.zt.client.httpclient.response-timeout:}") Integer responseTimeout) {

    return HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.custom()
            .setConnectionRequestTimeout(Optional.ofNullable(connectionRequestTimeout).orElse(DEFAULT_CONNECTION_REQUEST_TIMEOUT), TimeUnit.MILLISECONDS)
            .setResponseTimeout(Optional.ofNullable(responseTimeout).orElse(DEFAULT_RESPONSE_TIMEOUT), TimeUnit.MILLISECONDS)
            .build())
        .setConnectionManager(poolingHttpClientConnectionManager)
        .setKeepAliveStrategy(connectionKeepAliveStrategy)
        .build();
  }

  @ConditionalOnProperty(value = "spring.zt.client.dns-resolver.enabled", havingValue = "true", matchIfMissing = true)
  @Bean("ztDnsResolver")
  public static DnsResolver dnsResolver() {
    return new DnsResolver() {
      @Override
      public InetAddress[] resolve(String hostname) throws UnknownHostException {
        log.debug("Resolving {} using Ziti DNS Resolver", hostname);
        InetAddress address = Ziti.getDNSResolver().resolve(hostname);
        if (address != null) {
          log.debug("Resolved {} using Ziti to address {}", hostname, address);
          return new InetAddress[]{address};
        }

        log.debug("Unable to resolve {} using Ziti DNS Resolver, trying regular DNS resolution", hostname);
        return new InetAddress[]{InetAddress.getByName(hostname)};
      }

      @Override
      public String resolveCanonicalHostname(String hostname) throws UnknownHostException {
        log.debug("Resolving {} using Ziti DNS Resolver", hostname);
        InetAddress address = Ziti.getDNSResolver().resolve(hostname);
        if (address != null) {
          log.debug("Resolved {} using Ziti to address {}", hostname, address);
          return address.getCanonicalHostName();
        }

        log.debug("Unable to resolve {} using Ziti DNS Resolver, trying regular DNS resolution", hostname);
        return InetAddress.getByName(hostname).getCanonicalHostName();
      }
    };
  }

}
