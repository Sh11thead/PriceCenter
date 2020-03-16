package org.tron.pricecenter.clawer;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.tron.pricecenter.clawer.httpget.HttpGetter;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.core.model.TTokenPair;

@Component
public class HuobiGlobalGetter extends HttpGetter {

  @Getter
  protected Integer clawerId = ClawerEnums.HBG.getClawerId();

  private LinkedHashMap<String, Integer> getConfig() {
    //Customize Here
    LinkedHashMap<String, Integer> config = new LinkedHashMap<>();
    for (TTokenPair tokenPair : tronDefiConfigurer.getGlobalTokenPairList()) {
      String pairName = tokenPair.combineNames();
      if (!tronDefiConfigurer.getHbExclude().contains(pairName)) {
        pairName = pairName.replace("USD", "USDT");
        config.put(pairName, tokenPair.getId());
      }
    }

    return config;
  }


  public String getResStr(String symbol) {
    HashMap<String, String> requestMap = Maps.newHashMap();
    requestMap.put("symbol", symbol.toLowerCase());
    ResponseEntity<String> responseEntity = restTemplate
        .getForEntity(tronDefiConfigurer.getHbQueryUrl(), String.class,
            requestMap);
    return responseEntity.getBody();
  }


  @Override
  public List<TClawResult> getClawResult() throws Exception {
    List<TClawResult> ret = new ArrayList<>();
    LinkedHashMap<String, Integer> config = getConfig();
    BigDecimal usdtPrice = getUSDTPrice();
    for (String symbol : config.keySet()) {
      JSONObject jsonObject = JSONObject.parseObject(getResStr(symbol));
      String status = jsonObject.getString("status");
      if (!status.equalsIgnoreCase("ok")) {
        throw new Exception("get " + symbol + " status error");
      }
      BigDecimal price = jsonObject.getJSONObject("tick").getJSONArray("data")
          .getJSONObject(0).getBigDecimal("price");
      if (symbol.endsWith("USDT")) {
        price = price.multiply(usdtPrice);
      }
      Integer id = config.get(symbol);
      ret.add(
          new TClawResult(id, clawerId, price, usdtPrice, new Date()));
    }

    return ret;
  }
}
