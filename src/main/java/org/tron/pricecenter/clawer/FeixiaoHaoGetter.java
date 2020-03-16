package org.tron.pricecenter.clawer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.tron.pricecenter.clawer.httpget.HttpGetter;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.core.model.TTokenPair;

@Slf4j
@Component
public class FeixiaoHaoGetter extends HttpGetter {

  @Getter
  protected Integer clawerId = ClawerEnums.FXH.getClawerId();

  private LinkedHashMap<String, Integer> getConfig() {
    //Customize Here
    LinkedHashMap<String, Integer> config = new LinkedHashMap<>();
    for (TTokenPair tokenPair : tronDefiConfigurer.getGlobalTokenPairList()) {
      String pairName = tokenPair.getPairA() + "-" + tokenPair.getPairB();
      config.put(pairName, tokenPair.getId());
    }
    return config;
  }

  public String getResStr() {
/*    HashMap<String, String> requestMap = Maps.newHashMap();
    requestMap.put("symbol", symbolA.toUpperCase() + symbolB.toUpperCase());*/
    ResponseEntity<String> responseEntity = restTemplate
        .getForEntity(tronDefiConfigurer.getFxhQueryUrl(), String.class);
    return responseEntity.getBody();
  }

  @Override
  public List<TClawResult> getClawResult() throws Exception {
    String retStr = getResStr();
    List<TClawResult> ret = new ArrayList<>();
    Map<String, BigDecimal> priceVault = new HashMap<>();
    priceVault.put("USD", new BigDecimal(1));
    LinkedHashMap<String, Integer> config = getConfig();

    try {
      JSONArray jsonArray = JSONArray.parseArray(retStr);
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject which = jsonArray.getJSONObject(i);
        String symbol = which.getString("symbol");
        BigDecimal price = which.getBigDecimal("price_usd");
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
