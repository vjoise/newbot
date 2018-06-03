package com.stampbot.service.symphony;

import com.stampbot.config.StampBotConfig;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.SymphonyClientFactory;
import org.symphonyoss.client.model.SymAuth;
import org.symphonyoss.symphony.clients.AuthenticationClient;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

@Component
public class SymphonyService {

    private SymphonyClient symphonyClient;

    @Autowired
    private StampBotConfig config;

    @Bean
    public SymphonyClient symphonyClient() throws Exception {
        symphonyClient = SymphonyClientFactory.getClient(SymphonyClientFactory.TYPE.V4);
        init();
        return symphonyClient;
    }

    private void init() throws Exception {
        KeyStore cks = KeyStore.getInstance("pkcs12");
        loadKeyStore(cks, new ClassPathResource("cert_key.p12").getInputStream(), config.getBotCertPassword());
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(cks, config.getBotCertPassword().toCharArray())
                .loadTrustMaterial(null, (a, b) -> true)
                .build();
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        Client client = clientBuilder.sslContext(sslContext).build();
        AuthenticationClient authClient = new AuthenticationClient(config.getSessionAuthURL(), config.getKeyAuthUrl(), client);
        SymAuth symAuth = authClient.authenticate();
        symphonyClient.init(
                symAuth,
                config.getBotEmailAddress(),
                config.getAgentAPIEndpoint(),
                config.getPodAPIEndpoint()
        );
    }

    private static void loadKeyStore(KeyStore ks, InputStream ksInputStream, String ksPass) throws Exception {
        ks.load(ksInputStream, ksPass.toCharArray());
    }
}

