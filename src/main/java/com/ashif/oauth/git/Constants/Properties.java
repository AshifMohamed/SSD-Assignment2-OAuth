package com.ashif.oauth.git.Constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Properties {

    public static String clientSecret;

    public static  String clientId;

    @Value("${application.clientId}")
    public void setClientId(String client_id) {
        clientId = client_id;
    }

    @Value("${application.clientSecret}")
    public void setClientSecret(String client_secret) {
        clientSecret = client_secret;
    }
}
