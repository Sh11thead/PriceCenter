package org.tron.pricecenter.configurer;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "app.scheduling", name = "enable", havingValue = "true")
public class SchedulingConfigurer {

}
