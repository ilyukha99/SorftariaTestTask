package com.my_solution.sorftaria_test_task.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties("algorithm")
@Component
public class AlgorithmConfiguration {
    private String algorithmName;
}
