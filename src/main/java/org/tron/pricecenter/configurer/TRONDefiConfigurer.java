package org.tron.pricecenter.configurer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tron.pricecenter.core.model.TTokenPair;
import org.tron.pricecenter.service.TTokenPairService;

@Data
@Component
public class TRONDefiConfigurer {

  public static final int USDT_USD_PAIR_ID = 1;


  @Value("${spring.profiles.active}")
  String env;

  @Value("${sun.sdk.config}")
  String sdkConfigFile;
  @Value("${cmcQueryUrl}")
  String cmcQueryUrl;
  @Value("${fxhQueryUrl}")
  String fxhQueryUrl;
  @Value("${hbQueryUrl}")
  String hbQueryUrl;
  @Value("${baQueryUrl}")
  String baQueryUrl;
  @Value("${cmcapiKeys}")
  String cmcapiKeys;

  @Autowired
  TTokenPairService tTokenPairService;


  private List<TTokenPair> globalTokenPairList;

  private Map<Integer, TTokenPair> globalTokenPairMap;

  private Map<String, TTokenPair> globalTokenPairNameMap;

  @PostConstruct
  public void init() {
    globalTokenPairList = tTokenPairService.findAll();

    globalTokenPairMap = globalTokenPairList.stream()
        .collect(Collectors.toConcurrentMap(m -> m.getId(), m -> m));
    globalTokenPairNameMap = globalTokenPairList.stream()
        .collect(Collectors.toConcurrentMap(m -> m.getPairA() + m.getPairB(), m -> m));
  }
}