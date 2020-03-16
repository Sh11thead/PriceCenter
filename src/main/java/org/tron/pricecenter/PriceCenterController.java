package org.tron.pricecenter;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tron.pricecenter.configurer.TRONDefiConfigurer;
import org.tron.pricecenter.core.Result;
import org.tron.pricecenter.core.model.TComputedResult;
import org.tron.pricecenter.core.model.TTokenPair;
import org.tron.pricecenter.service.TComputedResultService;

@RestController
@Slf4j
public class PriceCenterController {

  @Autowired
  TRONDefiConfigurer tronDefiConfigurer;
  @Autowired
  TComputedResultService tComputedResultService;

  @GetMapping("favicon.ico")
  void returnNoFavicon() {
  }

  @GetMapping("/v1/cryptocurrency/getprice")
  public Result getPrice(
      @RequestParam String symbol,
      @RequestParam(value = "convert", defaultValue = "USD", required = false) String convert
  ) {
    long from = System.currentTimeMillis();
    String[] symbols = toUpperCase(symbol.split(","));
    String[] converts = toUpperCase(convert.split(","));
    List<String> queryStr = Lists.newArrayList();
    for (String s : symbols) {
      for (String c : converts) {
        queryStr.add(s + c);
      }
    }

    List<Integer> pairIdList = new ArrayList<>();

    for (String s : queryStr) {
      TTokenPair tTokenPair = tronDefiConfigurer.getGlobalTokenPairNameMap().get(s);
      if (tTokenPair != null) {
        pairIdList.add(tTokenPair.getId());
      }
    }
    List<TComputedResult> computedResultsret = tComputedResultService
        .getLastestResultByPairIdList(pairIdList);

    Map<String, TComputedResult> computedResultsMap = computedResultsret.stream().collect(
        Collectors.toMap(
            tr -> tronDefiConfigurer.getGlobalTokenPairMap().get(tr.getPairId()).combineNames(),
            tr -> tr));

    Map<String, Object> result = new HashMap<>();

    for (String mainSymbol : symbols) {
      Map<String, Object> data = new HashMap<>();
      Map<String, Object> quote = new HashMap<>();

      result.put(mainSymbol, data);
      data.put("quote", quote);

      for (String sideSymbol : converts) {
        TComputedResult find = computedResultsMap.get(mainSymbol + sideSymbol);
        if (find != null) {
          Map<String, Object> priceInfo = new HashMap<>();
          priceInfo.put("price", find.getPrice().toPlainString());
          priceInfo.put("last_updated", find.getCreateTime().getTime());
          quote.put(sideSymbol, priceInfo);
        }
      }
    }
    long end = System.currentTimeMillis();
    log.info("query strs:{} use time :{}", queryStr, end - from);

    return new Result(result);

  }


  public static String[] toUpperCase(String[] source) {
    String[] ret = new String[source.length];
    for (int i = 0; i < source.length; i++) {
      ret[i] = source[i].toUpperCase();
    }
    return ret;
  }
}
