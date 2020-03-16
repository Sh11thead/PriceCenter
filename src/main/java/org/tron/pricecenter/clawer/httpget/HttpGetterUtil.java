package org.tron.pricecenter.clawer.httpget;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpGetterUtil {

  private static Logger logger = LoggerFactory.getLogger(HttpGetterUtil.class);

  private static final int TIME_OUT = 10 * 1000;
  private static PoolingHttpClientConnectionManager cm = null;

  static {
    LayeredConnectionSocketFactory sslsf = null;
    try {
      sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
    } catch (NoSuchAlgorithmException e) {
      logger.error("SSL Connection failed...");
    }
    Registry<ConnectionSocketFactory> sRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("https", sslsf)
        .register("http", new PlainConnectionSocketFactory())
        .build();
    cm = new PoolingHttpClientConnectionManager(sRegistry);
    // max clients
    cm.setMaxTotal(200);
    cm.setDefaultMaxPerRoute(20);
  }

  private static CloseableHttpClient getHttpClient() {
    CloseableHttpClient httpClient = HttpClients.custom()
        .setConnectionManager(cm).build();
    return httpClient;
  }

  public static JSONArray httpGetArray(String url) {
    JSONArray jsonResult = null;
    CloseableHttpClient httpClient = getHttpClient();

    CloseableHttpResponse response = null;
    try {

      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT)
          .setSocketTimeout(TIME_OUT).build();
      HttpGet request = new HttpGet(url);
      request.setConfig(requestConfig);
      response = httpClient.execute(request);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        String strResult = EntityUtils.toString(response.getEntity());
        jsonResult = JSON.parseArray(strResult);
        url = URLDecoder.decode(url, "UTF-8");
      } else {
        logger.error("get failed:" + url);
      }
    } catch (IOException e) {
      logger.error("get failed:" + url, e);
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
          response.close();
        } catch (IOException e) {
          logger.error("close response failed", e);
        }
      }
    }
    return jsonResult;
  }

  public static JSONObject httpGet(String url) {
    JSONObject jsonResult = null;
    CloseableHttpClient httpClient = getHttpClient();

    CloseableHttpResponse response = null;
    try {

      RequestConfig requestConfig = RequestConfig.custom()
          .setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT)
          .setSocketTimeout(TIME_OUT).build();
      HttpGet request = new HttpGet(url);
      request.setConfig(requestConfig);
      response = httpClient.execute(request);
      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        String strResult = EntityUtils.toString(response.getEntity());
        jsonResult = JSON.parseObject(strResult);
        url = URLDecoder.decode(url, "UTF-8");
      } else {
        logger.error("get failed:" + url);
      }
    } catch (IOException e) {
      logger.error("get failed:" + url, e);
    } finally {
      if (response != null) {
        try {
          EntityUtils.consume(response.getEntity());
          response.close();
        } catch (IOException e) {
          logger.error("close response failed", e);
        }
      }
    }
    return jsonResult;
  }


}
