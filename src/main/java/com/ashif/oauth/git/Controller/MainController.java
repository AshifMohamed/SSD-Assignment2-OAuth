package com.ashif.oauth.git.Controller;

import com.ashif.oauth.git.Model.Repository;
import com.ashif.oauth.git.Model.RepositoryWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class MainController {

    @GetMapping("/")
    public String showHomePage()  {

        return "index";
    }

    @PostMapping("/code-request")
    public void requestCode(HttpServletResponse response) throws IOException {

        response.sendRedirect("/auth/git");
    }
}
