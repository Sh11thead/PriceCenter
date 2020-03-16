package org.tron.pricecenter.utils.bean;

import java.util.HashMap;
import java.util.Map;
import org.springframework.cglib.beans.BeanMap;

public class BeanMapUtils {

  /**
   * bean to Map
   */
  public static <T> Map<String, Object> bean2Map(T bean) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if (bean != null) {
        BeanMap beanMap = BeanMap.create(bean);
        map.putAll(beanMap);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return map;
  }

}
