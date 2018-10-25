package com.ashif.oauth.git.Controller;

import com.ashif.oauth.git.Model.Git_User;
import com.ashif.oauth.git.Service.OAuthService;
import com.ashif.oauth.git.Utils.OAuth_Configuration;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
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

    /** Check for existing token of the given userId.
     *  If returned null, get a Authorization code
     *  Else get the Access Token of the user
     *  Note : UserId is hard coded here.
     * @param response
     * @throws IOException
     */
    @GetMapping("/auth/git")
    public void authorizeFlow(HttpServletResponse response) throws IOException{

        flow= oauthConfig.getAuthorizationCodeFlow();

        if((token=oauthService.getExistingCredentials("user"))==null){

            System.out.println("No Credentials");
            String url = oauthService.getAuthorizationCode();
            response.sendRedirect(url);

        }else{
            System.out.println("Already Have Access:" + token);
            response.sendRedirect("/user");
        }

    }

    /**Get the Authorization Code from the response and
     * Exchange that to get an Access Token.
     * Store the Token Response in the in memory Datastore.
     * @param code
     * @return
     * @throws IOException
     */
    @GetMapping("/home")
    public String callBack(@RequestParam("code") String code) throws IOException{

        TokenResponse tokenResponse =oauthService.getAccessToken( code);
        oauthService.storeCredentials(tokenResponse,"user");
        token=tokenResponse.getAccessToken();
        System.out.println("Token is :"+ token);
        return "redirect:/user";
    }

    /**Get the git_user model and
     * bind it for the view
     * @param model
     * @return
     */
    @GetMapping("/user")
    public String showGitPage(Model model )  {

        git_user=oauthService.getUserDetails(token);
        model.addAttribute("user", git_user );
        return "user_profile";
    }

}
