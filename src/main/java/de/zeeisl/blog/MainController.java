package de.zeeisl.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/home")
    String home(){
        return "home";
    }

    @GetMapping("/login")
    String login(){
        return "login";
    }

    @GetMapping("/register")
    String register(){
        return "register";
    }

    @GetMapping("/article")
    String article(){
        return "article";
    }

}
