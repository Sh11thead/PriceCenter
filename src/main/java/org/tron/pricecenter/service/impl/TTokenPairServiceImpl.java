package org.tron.pricecenter.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.tron.pricecenter.core.AbstractService;
import org.tron.pricecenter.core.dao.TTokenPairMapper;
import org.tron.pricecenter.core.model.TTokenPair;
import org.tron.pricecenter.service.TTokenPairService;


/**
 * Created by CodeGenerator on 2020/03/12.
 */
@Service
public class TTokenPairServiceImpl extends AbstractService<TTokenPair> implements
    TTokenPairService {

  @Resource
  private TTokenPairMapper tTokenPairMapper;

}
