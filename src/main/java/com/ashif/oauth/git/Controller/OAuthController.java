package com.ashif.oauth.git.Controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@RestController
public class OAuthController {

    AuthorizationCodeFlow flow;

    @PostConstruct
    public void init()  {

        System.out.println("Came");
        HttpExecuteInterceptor clientAuthenticator = new ClientParametersAuthentication("5aaf2a84c40fc789ec6f", "f1f808f3b2323a0873d75e880e7817640bb939cb");

        JacksonFactory jsonFactory = new JacksonFactory();
        HttpTransport httpTransport = new NetHttpTransport();
        flow = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                httpTransport, JacksonFactory.getDefaultInstance(),
                new GenericUrl("https://github.com/login/oauth/access_token"),
                clientAuthenticator, "5aaf2a84c40fc789ec6f",
                "https://github.com/login/oauth/authorize").build();

    }
    @GetMapping("/git")
    public void testRes(HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("Cameeeeeeeeeeeeee");

        String url = flow.newAuthorizationUrl().setState("xyz5-adhu4")
                .setRedirectUri("http://localhost:8080/home").build();
        response.setHeader("Retry-After","100");
        response.sendRedirect(url);

    }

    @GetMapping("/home")
    public void getCode(HttpServletRequest request) throws IOException{

        System.out.println("Came 2");

       String code= request.getParameter("code");

        System.out.println("Code :"+ code);
        TokenResponse tokenResponse = flow
                .newTokenRequest(code)
                .setScopes(new ArrayList<String>(Arrays.asList("user")))
                .setRequestInitializer(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) throws IOException {
                        request.getHeaders().setAccept("application/json");
                    }
                }).execute();

        tokenResponse.getAccessToken();
        System.out.println("Token :"+ tokenResponse.getAccessToken());
    }

}
