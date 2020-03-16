package org.tron.pricecenter;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.pricecenter.utils.Counter;

public class PlainTest extends Tester {

  @Autowired
  Medianizer medianizer;
  @Autowired
  PriceFeedManager priceFeedManager;

  @Test
  public void counterTest() {
    Counter counter = new Counter(2);
    logger.info("counter:{}", counter.read());
    logger.info("counter:{}", counter.read());
  }

  @Test
  public void testpriceFeedManagerFlow() throws Exception {
    priceFeedManager.start();
  }

  @Test
  public void testmedianizer() throws Exception {
    priceFeedManager.start();
    logger.info("{}", JList(medianizer.compute()));
  }


}
