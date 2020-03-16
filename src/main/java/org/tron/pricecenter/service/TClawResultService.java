package org.tron.pricecenter.service;

import java.util.Date;
import java.util.List;
import org.tron.pricecenter.core.Service;
import org.tron.pricecenter.core.model.TClawResult;


/**
 * Created by CodeGenerator on 2020/03/15.
 */
public interface TClawResultService extends Service<TClawResult> {

  public List<TClawResult> getLastesFullClawResultByPairId(Integer pairId, Date mustGreaterThan);
}
