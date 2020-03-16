package org.tron.pricecenter.service.impl;


import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tron.pricecenter.core.dao.TComputedResultMapper;
import org.tron.pricecenter.core.model.TComputedResult;
import org.tron.pricecenter.service.TComputedResultService;
import org.tron.pricecenter.utils.ValueVault;
import tk.mybatis.mapper.entity.Condition;


/**
 * Created by CodeGenerator on 2020/03/15.
 */
@Slf4j
@Service

public class TComputedResultServiceImpl implements
    TComputedResultService {

  @Autowired
  private TComputedResultMapper tComputedResultMapper;


  @Override
  public void cacheResults(List<TComputedResult> tComputedResultList) {
    for (TComputedResult tComputedResult : tComputedResultList) {
      ValueVault.put(tComputedResult.getPairId().toString(), tComputedResult);
    }
  }


  @Override
  public TComputedResult getLastestResultByPairId(Integer pairId) {

    TComputedResult result = (TComputedResult) ValueVault.get(pairId.toString());
    if (result == null) {
      PageHelper.startPage(0, 1);
      Condition condition = new Condition(TComputedResult.class);
      condition.createCriteria().andEqualTo("pairId", pairId);
      condition.orderBy("createTime").desc();
      List<TComputedResult> lastest = tComputedResultMapper.selectByCondition(condition);
      if (lastest.size() > 0) {
        ValueVault.put(lastest.get(0).getPairId().toString(), lastest.get(0));
        result = lastest.get(0);
      }
    }
    return result;
  }

  @Override
  public List<TComputedResult> getLastestResultByPairIdList(List<Integer> pairIds) {

    List<TComputedResult> ret = new ArrayList<>();
    for (Integer pairId : pairIds) {
      TComputedResult result = getLastestResultByPairId(pairId);
      if (result != null) {
        ret.add(result);
      }
    }

    return ret;
  }

  @Override
  public void save(List<TComputedResult> models) {
    if (models != null && models.size() > 0) {
      tComputedResultMapper.insertList(models);
    }
  }
}
