package com.my_solution.sorftaria_test_task.configurations;

import com.my_solution.sorftaria_test_task.services.MainApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Configuration
public class ApplicationConfig {

    private final MainApplicationService mainService;

    @Autowired
    public ApplicationConfig(@Lazy MainApplicationService mainApplicationService) {
        mainService = mainApplicationService;
    }

    @Bean
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startRoutine() {
        mainService.startExecution();
    }
}
