package org.tron.pricecenter.core.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table(name = "t_claw_result")
public class TClawResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "pair_id")
  private Integer pairId;

  @Column(name = "clawer_id")
  private Integer clawerId;

  private BigDecimal price;

  @Column(name = "usdt_price")
  private BigDecimal usdtPrice;

  @Column(name = "create_time")
  private Date createTime;

  public TClawResult(Integer pairId, Integer clawerId, BigDecimal price, BigDecimal usdtPrice,
      Date createTime) {
    this.pairId = pairId;
    this.clawerId = clawerId;
    this.price = price;
    this.usdtPrice = usdtPrice;
    this.createTime = createTime;
  }
}