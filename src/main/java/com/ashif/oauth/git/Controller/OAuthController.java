package com.ashif.oauth.git.Controller;

import com.ashif.oauth.git.Model.Git_User;
import com.ashif.oauth.git.Model.Repository;
import com.ashif.oauth.git.Model.RepositoryWrapper;
import com.ashif.oauth.git.Service.OAuthService;
import com.ashif.oauth.git.Utils.OAuth_Scope;
import com.ashif.oauth.git.Utils.Random_Number;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
//import org.springframework.stereotype.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import sun.net.www.http.HttpClient;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Base64;
//import java.util.Collections;

@Controller
public class OAuthController {

    @Autowired
    OAuthService oauthService;

    private static final Logger log = LoggerFactory.getLogger(OAuthController.class);
    private Git_User git_user ;

    AuthorizationCodeFlow flow;
    String token;

    @PostConstruct
    public void init()  {

        HttpExecuteInterceptor clientAuthenticator = new ClientParametersAuthentication("5aaf2a84c40fc789ec6f", "privateId");

        JacksonFactory jsonFactory = new JacksonFactory();
        HttpTransport httpTransport = new NetHttpTransport();
        flow = new AuthorizationCodeFlow.Builder(
                BearerToken.authorizationHeaderAccessMethod(),
                httpTransport, JacksonFactory.getDefaultInstance(),
                new GenericUrl("https://github.com/login/oauth/access_token"),
                clientAuthenticator, "5aaf2a84c40fc789ec6f",
                "https://github.com/login/oauth/authorize").setScopes(new ArrayList<>(Arrays.asList("repo","user","user:email"))).build();

    }


    @GetMapping("/auth/git")
    public void testRes(HttpServletResponse response) throws IOException{

        String url = flow.newAuthorizationUrl().setState(Random_Number.generateUniqueNumber())
                .setScopes(OAuth_Scope.getScopes())
                .setRedirectUri("http://localhost:8080/home").build();
        response.sendRedirect(url);

    }

    @GetMapping("/home")
    public String getAccessToken(HttpServletRequest request) throws IOException{

       String code= request.getParameter("code");
       TokenResponse tokenResponse = flow
                .newTokenRequest(code)
                .setScopes(flow.getScopes())
                .setRequestInitializer(request1 -> request1.getHeaders().setAccept("application/json")).execute();

        token=tokenResponse.getAccessToken();
        testt();
        return "redirect:/user";

    }

    public void testt() throws IOException {

        git_user=oauthService.getUserDetails(token);
    }

    @GetMapping("/user")
    public String showGitPage(Model model )  {

        model.addAttribute("user", git_user );
        return "repository";
    }

}
