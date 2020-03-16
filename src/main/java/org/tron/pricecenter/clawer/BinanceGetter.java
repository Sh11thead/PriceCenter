package org.tron.pricecenter.clawer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.tron.pricecenter.clawer.httpget.HttpGetter;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.core.model.TTokenPair;

@Slf4j
@Component
public class BinanceGetter extends HttpGetter {

  @Getter
  protected Integer clawerId = ClawerEnums.BINANCE.getClawerId();

  private LinkedHashMap<String, Integer> getConfig() {
    //Customize Here
    LinkedHashMap<String, Integer> config = new LinkedHashMap<>();
    for (TTokenPair tokenPair : tronDefiConfigurer.getGlobalTokenPairList()) {
      String pairName = tokenPair.combineNames();
      if (!tronDefiConfigurer.getBaExclude().contains(pairName)) {
        pairName = pairName.replace("USD", "USDT");
        config.put(pairName, tokenPair.getId());
      }
    }
    return config;
  }

  public String getResStr() {
/*    HashMap<String, String> requestMap = Maps.newHashMap();
    requestMap.put("symbol", symbolA.toUpperCase() + symbolB.toUpperCase());*/
    ResponseEntity<String> responseEntity = restTemplate
        .getForEntity(tronDefiConfigurer.getBaQueryUrl(), String.class);
    return responseEntity.getBody();
  }

  @Override
  public List<TClawResult> getClawResult() throws Exception {
    String retStr = getResStr();
    List<TClawResult> ret = new ArrayList<>();
    //symbol,id
    LinkedHashMap<String, Integer> config = getConfig();

    BigDecimal usdtPrice = getUSDTPrice();

    Set<String> configSet = config.keySet();
    try {
      JSONArray jsonArray = JSONArray.parseArray(retStr);
      for (int i = 0; i < jsonArray.size(); i++) {
        JSONObject which = jsonArray.getJSONObject(i);
        String symbol = which.getString("symbol");
        if (configSet.contains(symbol)) {
          BigDecimal price = which.getBigDecimal("price");
          if (symbol.endsWith("USDT")) {
            price = price.multiply(usdtPrice);
          }
          Integer id = config.get(symbol);
          ret.add(
              new TClawResult(id, clawerId, price, usdtPrice, new Date()));
        }
      }
    } catch (Exception e) {
      log.error("BinanceClwaer getResult Exception", e);
    }
    return ret;
  }
}
