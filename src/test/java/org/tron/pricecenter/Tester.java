package org.tron.pricecenter;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 单元测试继承该类即可
 */
@RunWith(SpringRunner.class)
@TestPropertySource(properties = "app.scheduling.enable=false")
@SpringBootTest(classes = Application.class)
@ActiveProfiles("dev")
public abstract class Tester {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected String J(Object object) {
    return JSONObject.toJSONString(object);
  }

  protected String JList(Object object) {
    return JSONArray.toJSONString(object);
  }


}



