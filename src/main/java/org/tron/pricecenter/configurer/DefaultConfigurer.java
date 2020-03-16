package org.tron.pricecenter.configurer;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class DefaultConfigurer {

  @Autowired
  private TRONDefiConfigurer tronDefiConfigurer;

/*  @Bean
  public SunNetwork getSunNetwork() {
    SunNetwork sdk = new SunNetwork();
    sdk.init(new ServerConfigImpl(tronDefiConfigurer.getSdkConfigFile()), null);
    sdk.setPrivateKey(
        "000000000000000000000000000000000000000000000000000000000000001"); //default address
    return sdk;
  }*/

  @Bean
  public RestTemplate getRestTemplate() {
    CloseableHttpClient httpClient = HttpClients.custom()
        .setSSLHostnameVerifier(new NoopHostnameVerifier())
        .build();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);
    requestFactory.setConnectTimeout(6 * 1000);
    requestFactory.setReadTimeout(6 * 1000);
    return new RestTemplate(requestFactory);
  }
}
