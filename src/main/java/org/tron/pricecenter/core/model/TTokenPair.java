package org.tron.pricecenter.core.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_token_pair")
public class TTokenPair {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "pair_a")
  private String pairA;

  @Column(name = "pair_b")
  private String pairB;

  /**
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return pair_a
   */
  public String getPairA() {
    return pairA;
  }

  /**
   * @param pairA
   */
  public void setPairA(String pairA) {
    this.pairA = pairA;
  }

  /**
   * @return pair_b
   */
  public String getPairB() {
    return pairB;
  }

  /**
   * @param pairB
   */
  public void setPairB(String pairB) {
    this.pairB = pairB;
  }

  public String combineNames() {
    return pairA + pairB;
  }
}