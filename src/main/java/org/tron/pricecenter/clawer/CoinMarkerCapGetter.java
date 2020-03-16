package org.tron.pricecenter.clawer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.tron.pricecenter.clawer.httpget.HttpGetter;
import org.tron.pricecenter.configurer.TRONDefiConfigurer;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.core.model.TTokenPair;
import org.tron.pricecenter.utils.Counter;

@Slf4j
@Component
public class CoinMarkerCapGetter extends HttpGetter {

  @Getter
  protected Integer clawerId = ClawerEnums.CMC.getClawerId();
  @Autowired
  TRONDefiConfigurer tronDefiConfigurer;

  List<String> apiKeys = null;
  Counter counter;

  @PostConstruct
  private void init() {
    apiKeys = Arrays
        .asList(tronDefiConfigurer.getCmcapiKeys().split(","));
    counter = new Counter(apiKeys.size());
  }

  private LinkedHashMap<String, Integer> getConfig() {
    //Customize Here
    LinkedHashMap<String, Integer> config = new LinkedHashMap<>();
    for (TTokenPair tokenPair : tronDefiConfigurer.getGlobalTokenPairList()) {
      String pairName = tokenPair.getPairA() + "-" + tokenPair.getPairB();
      config.put(pairName, tokenPair.getId());
    }
    return config;
  }

  private String getRounedKey() {
    return apiKeys.get(counter.read());
  }

  protected HttpHeaders getHttpHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("User-Agent",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
    return httpHeaders;
  }

  public String getResStr(String symbol) {
    HttpHeaders httpHeaders = getHttpHeaders();
    httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    httpHeaders.add("X-CMC_PRO_API_KEY", getRounedKey());
    httpHeaders.add(HttpHeaders.ACCEPT, "application/json");
    HashMap<String, String> requestBody = Maps.newHashMap();
    requestBody.put("symbol", symbol);
    org.springframework.http.HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(
        httpHeaders);
    ResponseEntity<String> responseEntity = restTemplate
        .exchange(
            tronDefiConfigurer.getCmcQueryUrl(),
            HttpMethod.GET, requestEntity, String.class,
            requestBody);
    return responseEntity.getBody();
  }

  @Override
  public List<TClawResult> getClawResult() throws Exception {
    String retStr = getResStr("BTC,ETH,MKR,TRX,USDT,DAI");
    List<TClawResult> ret = new ArrayList<>();
    Map<String, BigDecimal> priceVault = new HashMap<>();
    priceVault.put("USD", new BigDecimal(1));
    LinkedHashMap<String, Integer> config = getConfig();

    try {
      JSONObject object = JSONObject.parseObject(retStr);
      Integer code = object.getJSONObject("status")
          .getInteger("error_code");
      if (code != 0) {
        throw new Exception("get CMC status error");
      }
      JSONObject fullCoins = object.getJSONObject("data");
      for (String pairA : fullCoins.keySet()) {
        JSONObject which = fullCoins.getJSONObject(pairA);
        String symbol = which.getString("symbol");
        BigDecimal price = which.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price");
        priceVault.put(symbol.toUpperCase(), price);
      }

      //query from vault
      for (String pairAB : config.keySet()) {
        Integer id = config.get(pairAB);
        String pairA = pairAB.split("-")[0];
        String pairB = pairAB.split("-")[1];
        BigDecimal price_A = priceVault.get(pairA);
        BigDecimal price_B = priceVault.get(pairB);
        if (price_A != null && price_B != null && price_B.compareTo(BigDecimal.ZERO) != 0) {
          BigDecimal price_fin = price_A.divide(price_B, 10, BigDecimal.ROUND_HALF_UP);
          ret.add(new TClawResult(id, clawerId, price_fin, priceVault.get("USDT"), new Date()));
        }
      }

    } catch (Exception e) {
      log.error("BinanceClwaer getResult Exception", e);
    }
    return ret;
  }
}
