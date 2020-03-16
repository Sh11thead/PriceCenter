package org.tron.pricecenter.clawer.httpget;

import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.tron.pricecenter.configurer.TRONDefiConfigurer;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.core.model.TComputedResult;
import org.tron.pricecenter.service.TComputedResultService;

@Slf4j
public abstract class HttpGetter {


  @Autowired
  protected RestTemplate restTemplate;
  @Autowired
  protected TComputedResultService tComputedResultService;

  @Autowired
  protected TRONDefiConfigurer tronDefiConfigurer;


  public abstract Integer getClawerId() throws Exception;

  protected BigDecimal getUSDTPrice() {
    TComputedResult usdtResult = tComputedResultService
        .getLastestResultByPairId(TRONDefiConfigurer.USDT_USD_PAIR_ID);
    if (usdtResult != null) {
      return usdtResult.getPrice();
    }
    return new BigDecimal(1);
  }


  public abstract List<TClawResult> getClawResult() throws Exception;


}
