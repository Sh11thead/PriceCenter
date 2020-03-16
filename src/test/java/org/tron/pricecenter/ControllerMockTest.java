package org.tron.pricecenter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class ControllerMockTest extends Tester {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testProxy() throws Exception {
    String ret = mockMvc
        .perform(MockMvcRequestBuilders.get("/v1/cryptocurrency/getprice")
            .param("symbol", "BTC,eth,TrX")
            .param("convert", "USD,BTC,ETH"))
        .andDo(print())
        .andReturn().getResponse().getContentAsString();
    logger.info(ret);


  }


}
