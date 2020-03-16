package org.tron.pricecenter.clawer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ClawerEnums {
  BINANCE(1), HBG(2), CMC(3), FXH(4);

  @Getter
  private Integer clawerId;
}
