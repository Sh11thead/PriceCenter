package org.tron.pricecenter.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ValueVault {


  private static final Map<String, Object> map;


  /**
   * 初始化
   */
  static {
    map = new ConcurrentHashMap<>();
  }

  /**
   * 私有构造函数,工具类不允许实例化
   */
  private ValueVault() {

  }

  /**
   * 增加缓存
   *
   * @param key
   * @param value
   */
  public static void put(String key, Object value) {
    if (value == null) {
      map.remove(key);
      return;
    }
    map.put(key, value);
  }


  /**
   * 获取缓存
   *
   * @param key
   * @return
   */
  public static Object get(String key) {
    return map.get(key);
  }

  /**
   * 查询缓存是否包含key
   *
   * @param key
   * @return
   */
  public static boolean containsKey(String key) {
    return map.containsKey(key);
  }

  /**
   * 删除缓存
   *
   * @param key
   */
  public static void remove(String key) {
    map.remove(key);
  }

  /**
   * 删除缓存
   *
   * @param o
   */
  public static void remove(Object o) {
    map.remove(o);
  }

  /**
   * 返回缓存大小
   *
   * @return
   */
  public static int size() {
    return map.size();
  }

  /**
   * 清除所有缓存
   *
   * @return
   */
  public static void clear() {
    if (size() > 0) {
      map.clear();
    }
  }

}
