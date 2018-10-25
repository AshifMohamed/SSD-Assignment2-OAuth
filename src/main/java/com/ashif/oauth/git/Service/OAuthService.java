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

@Service
public class OAuthService {

    //url to get GitHub repository details of a user
    private final String GIT_REPO_URL= "https://api.github.com/user/repos";

    //url to get GitHub profile information of a user
    private final String GIT_USER_URL= "https://api.github.com/user";

    private RestTemplate restTemplate;
    private HttpHeaders headers;
    private static AuthorizationCodeFlow authorizationFlow;

    {
        try {
              authorizationFlow=OAuth_Configuration.getOAuthInstance().getAuthorizationCodeFlow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Get GitHub profile details of a user.
     * @param token
     * @return
     */
    public Git_User getUserDetails(String token){

        restTemplate = new RestTemplate();
        headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+ token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Git_User> respEntity = restTemplate.exchange(GIT_USER_URL, HttpMethod.GET, entity, Git_User.class);

        return respEntity.getBody();
    }

    /** Get GitHub repository details of a user
     * @param token
     * @return
     */
    public Repository[] getRepositoryDetails(String token){

        restTemplate = new RestTemplate();
        headers=new HttpHeaders();
        headers.set("Authorization","Bearer "+ token);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Repository[]> respEntity = restTemplate.exchange(GIT_REPO_URL, HttpMethod.GET, entity, Repository[].class);

        return respEntity.getBody();
    }

    /** Get the authorization code using AuthorizationCodeFlow
     * @return
     */
    public String getAuthorizationCode() {

        return authorizationFlow.newAuthorizationUrl().setState(Random_Number.generateUniqueNumber())
                .setScopes(OAuth_Scope.getScopes())
                .setRedirectUri(OAuth_Configuration.callBack_URL).build();
    }

    /** Exchange authorization code for an Access Token.
     * @param autherizationCode
     * @return
     * @throws IOException
     */
    public TokenResponse getAccessToken( String autherizationCode) throws IOException {

       return authorizationFlow.newTokenRequest(autherizationCode)
                .setRequestInitializer(request1 -> request1.getHeaders().setAccept("application/json")).execute();
    }

    /** Save credential in the credential store with the given user ID
     * @param tokenResponse
     * @param userId
     * @throws IOException
     */
    public void storeCredentials( TokenResponse tokenResponse, String userId) throws IOException {

        authorizationFlow.createAndStoreCredential(tokenResponse, userId);
    }

    /** returns credential found in the credential store of the given user ID or
     *  null for none found
     * @param userId
     * @return
     * @throws IOException
     */
    public String getExistingCredentials(String userId) throws IOException {

        Credential credential;

        if (authorizationFlow != null && (credential=authorizationFlow.loadCredential(userId)) != null){

            System.out.println("Credential is :"+credential.toString());
            System.out.println("Have Existing Token");
            return credential.getAccessToken();

        }else{
            System.out.println("No Token");
            return null;
        }
    }

}
