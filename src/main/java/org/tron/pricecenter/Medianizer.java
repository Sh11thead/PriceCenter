package org.tron.pricecenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.pricecenter.configurer.TRONDefiConfigurer;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.core.model.TComputedResult;
import org.tron.pricecenter.core.model.TTokenPair;
import org.tron.pricecenter.service.TClawResultService;
import org.tron.pricecenter.service.TComputedResultService;

@Component
@Slf4j
public class Medianizer {

  @Autowired
  TRONDefiConfigurer tronDefiConfigurer;
  @Autowired
  TComputedResultService tComputedResultService;
  @Autowired
  TClawResultService tClawResultService;

  @Scheduled(fixedRate = 60 * 1000)
  public List<TComputedResult> compute() throws Exception {
    List<TComputedResult> ret = new ArrayList<>();
    List<TTokenPair> pairs = tronDefiConfigurer.getGlobalTokenPairList();
    for (TTokenPair tTokenPair : pairs) {
      Calendar beforeTime = Calendar.getInstance();
      beforeTime.add(Calendar.MINUTE, -5);// 5分钟之前的时间
      Date beforeD = beforeTime.getTime();
      List<TClawResult> source = tClawResultService
          .getLastesFullClawResultByPairId(tTokenPair.getId(), beforeD);
      if (source.size() > 0) {
        TComputedResult computedResult = compute(source);
        ret.add(computedResult);
      }
    }
    if (ret.size() > 0) {
      tComputedResultService.save(ret);
      //cache newest result
      tComputedResultService.cacheResults(ret);
      log.info("Medianizer succeed process {} token Pair", ret.size());
    }
    return ret;
  }

  public TComputedResult compute(List<TClawResult> sources) throws Exception {
    //All result suppose to be valid and in same token pair
    BigDecimal computed = null;
    if (sources.size() == 0) {
      throw new Exception("nah result");
    } else if (sources.size() == 1) {
      computed = sources.get(0).getPrice();
    } else if (sources.size() == 3) {
      List<BigDecimal> bigDecimalsList = sources.stream().map(s -> s.getPrice())
          .collect(Collectors.toList());
      Collections.sort(bigDecimalsList);
      if (bigDecimalsList.get(1).subtract(bigDecimalsList.get(0))
          .compareTo(bigDecimalsList.get(2).subtract(bigDecimalsList.get(1)))
          > 0) {
        computed = bigDecimalsList.get(1).add(bigDecimalsList.get(2)).divide(new BigDecimal(2));
      } else {
        computed = bigDecimalsList.get(1).add(bigDecimalsList.get(0)).divide(new BigDecimal(2));
      }
    } else {
      if (sources.size() % 2 == 0) {
        BigDecimal val1 = sources.get(sources.size() / 2).getPrice();
        BigDecimal val2 = sources.get(sources.size() / 2 - 1).getPrice();
        computed = val1.add(val2).divide(new BigDecimal(2));
      } else {
        computed = sources.get((sources.size() - 1) / 2).getPrice();
      }
    }
    return new TComputedResult(sources.get(0).getPairId(), computed, new Date());

  }
}
