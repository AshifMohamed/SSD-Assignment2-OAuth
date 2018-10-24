package com.ashif.oauth.git.Service;

import com.ashif.oauth.git.Model.Git_User;
import com.ashif.oauth.git.Model.Repository;
import com.ashif.oauth.git.Utils.OAuth_Configuration;
import com.ashif.oauth.git.Utils.OAuth_Scope;
import com.ashif.oauth.git.Utils.Random_Number;
import com.google.api.client.auth.oauth2.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.sql.SQLOutput;

@Service
public class OAuthService {

    private final String GIT_REPO_URL= "https://api.github.com/user/repos";
    private final String GIT_USER_URL= "https://api.github.com/user";
    private RestTemplate restTemplate;
    private HttpHeaders headers;
    StoredCredential store;

    public Git_User getUserDetails(String token){

        restTemplate = new RestTemplate();
        headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+ token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Git_User> respEntity = restTemplate.exchange(GIT_USER_URL, HttpMethod.GET, entity, Git_User.class);

        return respEntity.getBody();
    }

    public Repository[] getRepositoryDetails(String token){

        restTemplate = new RestTemplate();
        headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+ token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Repository[]> respEntity = restTemplate.exchange(GIT_REPO_URL, HttpMethod.GET, entity, Repository[].class);

        return respEntity.getBody();
    }

    public String getAuthorizationCode(AuthorizationCodeFlow flow) {

        return flow.newAuthorizationUrl().setState(Random_Number.generateUniqueNumber())
                .setScopes(OAuth_Scope.getScopes())
                .setRedirectUri(OAuth_Configuration.callBack_URL).build();
    }

    public TokenResponse getAccessToken(AuthorizationCodeFlow flow, String code) throws IOException {

       return flow.newTokenRequest(code)
                .setScopes(flow.getScopes())
                .setRequestInitializer(request1 -> request1.getHeaders().setAccept("application/json")).execute();

    }

    public void saveCredential(AuthorizationCodeFlow flow, TokenResponse tokenResponse, String userId) throws IOException {

        System.out.println("Saved Credential");
        Credential credential=flow.createAndStoreCredential(tokenResponse, userId);
        store = new StoredCredential(credential);

    }

    public String getExistingToken(AuthorizationCodeFlow flow, String userId) throws IOException {

        Credential credential = flow.loadCredential(userId);

        System.out.println(credential);
        if(credential !=null) {
            System.out.println("Have Existing Token");
            return credential.getAccessToken();
        }else{
            System.out.println("No Token");
            return null;
        }
    }

    public String getCredential() throws IOException {

        if(store != null){

            return store.getAccessToken();
        }else{

            return null;
        }

    }
}
