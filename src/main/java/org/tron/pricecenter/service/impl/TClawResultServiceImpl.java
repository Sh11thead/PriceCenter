package org.tron.pricecenter.service.impl;


import com.github.pagehelper.PageHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tron.pricecenter.clawer.ClawerEnums;
import org.tron.pricecenter.configurer.TRONDefiConfigurer;
import org.tron.pricecenter.core.AbstractService;
import org.tron.pricecenter.core.dao.TClawResultMapper;
import org.tron.pricecenter.core.model.TClawResult;
import org.tron.pricecenter.service.TClawResultService;
import tk.mybatis.mapper.entity.Condition;


/**
 * Created by CodeGenerator on 2020/03/15.
 */
@Service
public class TClawResultServiceImpl extends AbstractService<TClawResult> implements
    TClawResultService {

  @Autowired
  TRONDefiConfigurer tronDefiConfigurer;

  @Resource
  private TClawResultMapper tClawResultMapper;

  @Override
  public List<TClawResult> getLastesFullClawResultByPairId(Integer pairId, Date mustGreaterThan) {
    List<TClawResult> ret = new ArrayList<>();

    for (ClawerEnums clawer : ClawerEnums.values()) {
      Integer clawerId = clawer.getClawerId();
      PageHelper.startPage(0, 1);
      Condition condition = new Condition(TClawResult.class);
      condition.createCriteria().andEqualTo("pairId", pairId).andEqualTo("clawerId", clawerId)
          .andGreaterThan("createTime", mustGreaterThan);
      condition.orderBy("createTime").desc();
      List<TClawResult> res = tClawResultMapper.selectByCondition(condition);
      if (res.size() == 1) {
        ret.add(res.get(0));
      }
    }
    return ret;
  }
}
