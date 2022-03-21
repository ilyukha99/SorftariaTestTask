package com.my_solution.sorftaria_test_task.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("path")
@Component
public class PathsConfiguration {
    private String todayDirectory;
    private String yesterdayDirectory;
    private String todayUrlsPath;
    private String yesterdayUrlsPath;
}
