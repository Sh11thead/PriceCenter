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
@Table(name = "t_computed_result")
public class TComputedResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "pair_id")
  private Integer pairId;

  private BigDecimal price;

  @Column(name = "create_time")
  private Date createTime;

  public TComputedResult(Integer pairId, BigDecimal price, Date createTime) {
    this.pairId = pairId;
    this.price = price;
    this.createTime = createTime;
  }
}