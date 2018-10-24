package com.ashif.oauth.git.Utils;

import com.ashif.oauth.git.Constants.Properties;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuth_Configuration {

    @Autowired
    Properties properties;

    private AuthorizationCodeFlow flow;

    private final String authorization_EndPoint="https://github.com/login/oauth/authorize";

    private final String token_EndPoint="https://github.com/login/oauth/access_token";

    public static final String callBack_URL="http://localhost:8080/home";

    private static OAuth_Configuration OAuthInstance;

    private OAuth_Configuration(){}

    public static OAuth_Configuration getOAuthInstance() {

        if(OAuthInstance==null){

            synchronized (OAuth_Configuration.class){

                if (OAuthInstance==null)
                    OAuthInstance=new OAuth_Configuration();
            }
        }
        return OAuthInstance;
    }

    public AuthorizationCodeFlow getAuthorizationCodeFlow(){

        if(flow == null) {
            HttpExecuteInterceptor clientAuthenticator = new ClientParametersAuthentication(properties.clientId, properties.clientSecret);

            HttpTransport httpTransport = new NetHttpTransport();
            flow = new AuthorizationCodeFlow.Builder(
                    BearerToken.authorizationHeaderAccessMethod(),
                    httpTransport, JacksonFactory.getDefaultInstance(),
                    new GenericUrl(this.token_EndPoint),
                    clientAuthenticator, properties.clientId,
                    this.authorization_EndPoint).setScopes(OAuth_Scope.getScopes()).build();
            System.out.println("Creating New Flow");
        }
       return flow;
    }


}
