package com.example.ezyroulettehttpserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class GoogleController {

    @GetMapping("/")
    public String user(Principal principal) {
        System.out.println("username : " + principal.getName());
        return "redirect:/getGoogleLoginToken/token=" + principal.getName();
    }

    @GetMapping("/getGoogleLoginToken/*")
    @ResponseBody
    public String getGoogleLoginToken() {
        return "Login Successfully!! Returning app....";
    }

}
