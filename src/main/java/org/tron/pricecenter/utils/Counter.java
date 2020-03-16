package org.tron.pricecenter.utils;

public class Counter {

  private int size;
  private int i;

  public Counter(int size) {
    this.size = size;
  }

  public synchronized int read() {
    int ret = i;
    if (++i >= size) {
      i -= size;
    }
    return ret;
  }
}
