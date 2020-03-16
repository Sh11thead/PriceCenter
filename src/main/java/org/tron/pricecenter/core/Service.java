package org.tron.pricecenter.core;

import java.util.List;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;


public interface Service<T> {

  void save(T model);

  void save(List<T> models);

  void deleteById(Integer id);

  void update(T model);

  T findById(Integer id);

  T findBy(String fieldName, Object value) throws TooManyResultsException;

  List<T> findByCondition(Condition condition);

  List<T> findAll();

  List<T> findByModel(T model);
}
