package com.stampbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@ConfigurationProperties
@Data
@Component
@Validated
public class StampBotConfig {

    @NotNull
    private String sessionAuthURL;

    @NotNull
    private String keyAuthUrl;

    private String localKeystorePath;

    private String localKeystorePassword;

    @NotNull
    private String botCertPath;

    @NotNull
    private String botCertPassword;

    private String botEmailAddress;

    private String userEmailAddress;

    @NotNull
    private String agentAPIEndpoint;

    @NotNull
    private String podAPIEndpoint;

    @NotNull
    private String taskReminderInterval;

}
