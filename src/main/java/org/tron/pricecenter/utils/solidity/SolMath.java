package org.tron.pricecenter.utils.solidity;

import java.math.BigInteger;

public class SolMath {

  public static final BigInteger WAD = new BigInteger("1000000000000000000");

  public static final BigInteger RAY = new BigInteger("1000000000000000000000000000");


  public static final BigInteger WDIV(BigInteger a, BigInteger b) {
    if (b.compareTo(BigInteger.ZERO) == 0) {
      return BigInteger.ZERO;
    } else {
      return a.multiply(WAD).add(b.divide(new BigInteger("2"))).divide(b);
    }

  }

  public static final BigInteger RDIV(BigInteger a, BigInteger b) {
    if (b.compareTo(BigInteger.ZERO) == 0) {
      return BigInteger.ZERO;
    } else {
      return a.multiply(RAY).add(b.divide(new BigInteger("2"))).divide(b);
    }
  }


  public static final BigInteger WMUL(BigInteger a, BigInteger b) {
    return a.multiply(b).add(WAD.divide(new BigInteger("2"))).divide(WAD);
  }

  public static final BigInteger RMUL(BigInteger a, BigInteger b) {
    return a.multiply(b).add(RAY.divide(new BigInteger("2"))).divide(RAY);
  }


}
