package com.unicom.oauth2.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "secure.ignored")
public class IgnoreUrlsConfig {
  private List<String> urls = new ArrayList<>();
}
