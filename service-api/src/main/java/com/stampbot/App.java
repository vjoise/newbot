package com.stampbot;

import com.stampbot.workflow.repository.WorkflowDataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

@EnableAutoConfiguration
@SpringBootApplication(exclude = Neo4jDataAutoConfiguration.class)
@EnableScheduling
@EnableWebMvc
@Slf4j
@EnableJpaRepositories(value = {"com.stampbot.repository", "com.stampbot.workflow.repository"},  transactionManagerRef = "jpaTransactionManager")
@EnableNeo4jRepositories("com.stampbot.knowledge.repository")
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
//        log.info("The number of workflows created are :: " + workflowDataStore.count());
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
