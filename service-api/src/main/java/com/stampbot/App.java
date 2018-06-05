package com.stampbot;

import com.stampbot.repository.WorkflowDataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
@EnableWebMvc
@Slf4j
public class App {

    @Autowired
    private WorkflowDataStore workflowDataStore;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void initializeWorkflow() {
        log.info("Initializing workflow...");
        workflowDataStore.build();
        workflowDataStore.buildForDev();
        log.info("The number of workflows created are :: " + workflowDataStore.count());
    }

}
