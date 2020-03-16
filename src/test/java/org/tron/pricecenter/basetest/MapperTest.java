package org.tron.pricecenter.basetest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tron.pricecenter.Tester;
import org.tron.pricecenter.core.dao.TTokenPairMapper;
import org.tron.pricecenter.core.model.TTokenPair;

public class MapperTest extends Tester {

  @Autowired
  TTokenPairMapper tTokenPairMapper;

  @Test
  public void testMapperInsert() {
    TTokenPair tTokenPair = new TTokenPair();
    tTokenPair.setId(0);
    tTokenPair.setPairA("USDT");
    tTokenPair.setPairB("USD");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("BTC");
    tTokenPair.setPairB("USD");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("ETH");
    tTokenPair.setPairB("USD");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("MKR");
    tTokenPair.setPairB("USD");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("DAI");
    tTokenPair.setPairB("USD");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("TRX");
    tTokenPair.setPairB("USD");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("TRX");
    tTokenPair.setPairB("BTC");
    tTokenPairMapper.insert(tTokenPair);
    tTokenPair = new TTokenPair();
    tTokenPair.setPairA("TRX");
    tTokenPair.setPairB("ETH");
    tTokenPairMapper.insert(tTokenPair);

  }


}
