package org.tron.pricecenter;

import java.util.List;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tron.pricecenter.clawer.BinanceGetter;
import org.tron.pricecenter.clawer.CoinMarkerCapGetter;
import org.tron.pricecenter.clawer.FeixiaoHaoGetter;
import org.tron.pricecenter.clawer.HuobiGlobalGetter;
import org.tron.pricecenter.clawer.httpget.HttpGetter;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.service.TClawResultService;

@Component
public class PriceFeedManager {

  @Autowired
  TClawResultService tClawResultService;

  @Autowired
  BinanceGetter binanceGetter;
  @Autowired
  CoinMarkerCapGetter coinMarkerCapGetter;
  @Autowired
  FeixiaoHaoGetter feixiaoHaoGetter;
  @Autowired
  HuobiGlobalGetter huobiGlobalGetter;

  Gather binanceGather;
  Gather coinMarkerCapGather;
  Gather feixiaoHaoGather;
  Gather huobiGlobalGather;

  @PostConstruct
  public void init() {
    binanceGather = new Gather(binanceGetter, tClawResultService);
    coinMarkerCapGather = new Gather(coinMarkerCapGetter, tClawResultService);
    feixiaoHaoGather = new Gather(feixiaoHaoGetter, tClawResultService);
    huobiGlobalGather = new Gather(huobiGlobalGetter, tClawResultService);
  }

  @Scheduled(fixedRate = 60 * 1000)
  public void start() throws Exception {
    new Thread(binanceGather).start();
    new Thread(coinMarkerCapGather).start();
    new Thread(feixiaoHaoGather).start();
    new Thread(huobiGlobalGather).start();
  }

  @Slf4j
  @AllArgsConstructor
  public static class Gather implements Runnable {

    HttpGetter httpGetter;
    TClawResultService tClawResultService;

    @Override
    public void run() {
      try {
        List<TClawResult> list = httpGetter.getClawResult();
        if (list.size() > 0) {
          log.info("success save {} result from clawer id {}", list.size(),
              httpGetter.getClawerId());
          tClawResultService.save(list);
        }

      } catch (Exception e) {
        log.error("httpGetter encountered error", e);
      }

    }
  }


}
