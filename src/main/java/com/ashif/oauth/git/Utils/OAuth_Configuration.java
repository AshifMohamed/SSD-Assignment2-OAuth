package com.ashif.oauth.git.Utils;

import com.ashif.oauth.git.Constants.Properties;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth_Configuration {

    // GitHub ClientId & ClientSecret retrieved from the properties class
    @Autowired
    Properties properties;

    private AuthorizationCodeFlow flow;

    // This is the URL we'll send the user to first
    // to get their authorization
    private final String authorization_EndPoint="https://github.com/login/oauth/authorize";

    // This is the endpoint we'll request an access token from
    private final String token_EndPoint="https://github.com/login/oauth/access_token";

    // This is the URL the Authorization Server sends the response
    public static final String callBack_URL="http://localhost:8080/home";

    private final HttpTransport httpTransport = new NetHttpTransport();

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


    /**Use the authorization code flow to allow the end user to
     * grant your application access to their protected data.
     * @return
     */
    public AuthorizationCodeFlow getAuthorizationCodeFlow() throws IOException {

        if(flow == null) {

            // in memory datastore for access tokens, ensures lookups are very quick
            DataStore<StoredCredential> dataStore = MemoryDataStoreFactory.getDefaultInstance()
                    .getDataStore("flow");

            HttpExecuteInterceptor clientAuthenticator = new ClientParametersAuthentication(properties.clientId, properties.clientSecret);

            // authorization code flow
            flow = new AuthorizationCodeFlow.Builder(
                    BearerToken.authorizationHeaderAccessMethod(),
                    httpTransport, JacksonFactory.getDefaultInstance(),
                    new GenericUrl(this.token_EndPoint),
                    clientAuthenticator, properties.clientId,
                    this.authorization_EndPoint).setCredentialDataStore(dataStore).setScopes(OAuth_Scope.getScopes()).build();
            System.out.println("Creating New Flow");
        }
       return flow;
    }
}
