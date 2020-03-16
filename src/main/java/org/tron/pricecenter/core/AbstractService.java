package org.tron.pricecenter.core;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

public abstract class AbstractService<T> implements Service<T> {

  @Autowired
  protected Mapper<T> mapper;

  private Class<T> modelClass;

  public AbstractService() {
    ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    modelClass = (Class<T>) pt.getActualTypeArguments()[0];
  }

  public void save(T model) {
    mapper.insertSelective(model);
  }

  public void save(List<T> models) {
    if (models != null && models.size() > 0) {
      mapper.insertList(models);
    }
  }

  public void deleteById(Integer id) {
    mapper.deleteByPrimaryKey(id);
  }

  public void update(T model) {
    mapper.updateByPrimaryKeySelective(model);
  }

  public T findById(Integer id) {
    return mapper.selectByPrimaryKey(id);
  }

  @Override
  public T findBy(String fieldName, Object value) throws TooManyResultsException {
    try {
      T model = modelClass.newInstance();
      Field field = modelClass.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(model, value);
      return mapper.selectOne(model);
    } catch (ReflectiveOperationException e) {
      throw new ServiceException(e.getMessage(), e);
    }
  }

  public List<T> findByCondition(Condition condition) {
    return mapper.selectByCondition(condition);
  }

  public List<T> findByModel(T model) {
    return mapper.select(model);
  }

  public List<T> findAll() {
    return mapper.selectAll();
  }
}
