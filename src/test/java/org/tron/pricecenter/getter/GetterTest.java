package org.tron.pricecenter.getter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.pricecenter.Tester;
import org.tron.pricecenter.clawer.BinanceGetter;
import org.tron.pricecenter.clawer.CoinMarkerCapGetter;
import org.tron.pricecenter.clawer.FeixiaoHaoGetter;
import org.tron.pricecenter.clawer.HuobiGlobalGetter;

public class GetterTest extends Tester {

  @Autowired
  BinanceGetter binanceGetter;

  @Autowired
  HuobiGlobalGetter huobiGlobalGetter;

  @Autowired
  FeixiaoHaoGetter feixiaoHaoGetter;

  @Autowired
  CoinMarkerCapGetter coinMarkerCapGetter;

  @Test
  public void testBinanceGetter() throws Exception {
    logger.info(JList(binanceGetter.getClawResult()));
  }


  @Test
  public void testHBGetter() throws Exception {
    logger.info(JList(huobiGlobalGetter.getClawResult()));
  }

  @Test
  public void testFGetter() throws Exception {
    logger.info(JList(feixiaoHaoGetter.getClawResult()));
  }

  @Test
  public void testCMC() throws Exception {
    logger.info(JList(coinMarkerCapGetter.getClawResult()));
  }
}
