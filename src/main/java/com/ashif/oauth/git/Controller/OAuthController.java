package com.ashif.oauth.git.Controller;

import com.ashif.oauth.git.Model.Git_User;
import com.ashif.oauth.git.Service.OAuthService;
import com.ashif.oauth.git.Utils.OAuth_Configuration;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class OAuthController {

    @Autowired
    OAuthService oauthService;

    OAuth_Configuration oauthConfig=OAuth_Configuration.getOAuthInstance();

    private Git_User git_user ;
    private AuthorizationCodeFlow flow;
    private String token;
    private Credential credential;

    @GetMapping("/auth/git")
    public void authorizeFlow(HttpServletResponse response) throws IOException{

        flow= oauthConfig.getAuthorizationCodeFlow();

        String token2;
        if((token2=oauthService.getCredential())==null){

            System.out.println("No Credentials");
            String url = oauthService.getAuthorizationCode(flow);
            response.sendRedirect(url);
        }else{
            System.out.println("Already Have Access:" + token2);
            response.sendRedirect("redirect:/user");

        }
//        if((token=oauthService.getExistingToken(flow,"user"))==null) {
//            String url = oauthService.getAuthorizationCode(flow);
//            response.sendRedirect(url);
//
//        }else{
//            System.out.println("Already Have Access");
//            response.sendRedirect("redirect:/user");
//
//        }

    }

    @GetMapping("/home")
    public String callBack(@RequestParam("code") String code) throws IOException{

        TokenResponse tokenResponse =oauthService.getAccessToken(flow, code);
        oauthService.saveCredential(flow,tokenResponse,"user");
        token=tokenResponse.getAccessToken();
        System.out.println("Token is :"+ token);
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String showGitPage(Model model )  {

        git_user=oauthService.getUserDetails(token);
        model.addAttribute("user", git_user );
        return "user_profile";
    }

}
