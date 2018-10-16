package com.ashif.oauth.git.Service;

import com.ashif.oauth.git.Model.Git_User;
import com.ashif.oauth.git.Model.Repository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    private final String GIT_REPO_URL= "https://api.github.com/user/repos";
    private final String GIT_USER_URL= "https://api.github.com/user";
    private RestTemplate restTemplate;
    private HttpHeaders headers;

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
}
