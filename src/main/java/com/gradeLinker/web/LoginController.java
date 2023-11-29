package com.gradeLinker.web;


import com.gradeLinker.domain.user.LoginUser;
import com.gradeLinker.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class LoginController {
    @Autowired
    private HttpSession session;

    private final LoginService loginService;
    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @GetMapping("/login")
    public String loginPage() { return "pages/login.html"; }

    @PostMapping("/login")
    public String loginPost(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
        LoginUser user = loginService.login(username, password);

        /* Invalid login-password */
        if (user == null) {
            model.addAttribute("invalidCredentials", "Invalid Credentials");
            return "pages/login.html";
        }

        if (user.hasRole("admin")) {
            // TODO, redirect to admin's page
            return "redirect:pages/error.html";
        }

        /* Otherwise redirect to the ordinary user homepage page */
        session.setAttribute("username", username);
        return "redirect:/h";
    }

}