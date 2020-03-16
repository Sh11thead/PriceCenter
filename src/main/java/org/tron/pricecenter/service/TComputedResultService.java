package org.tron.pricecenter.service;

import java.util.List;
import org.tron.pricecenter.core.model.TComputedResult;


/**
 * Created by CodeGenerator on 2020/03/15.
 */
public interface TComputedResultService {

  public void cacheResults(List<TComputedResult> tComputedResultList);

  public TComputedResult getLastestResultByPairId(Integer pairId);

  public List<TComputedResult> getLastestResultByPairIdList(List<Integer> pairId);

  public void save(List<TComputedResult> models);

}
