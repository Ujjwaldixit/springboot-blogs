package com.blogs.controller;

import com.blogs.model.User;
import com.blogs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    Logger log = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationForm";
    }

    @PostMapping("/saveUser")
    public String registerUser(@ModelAttribute("user") User user,
                               RedirectAttributes redirectAttributes) {

        System.out.println("user= "+user);
        boolean checkRegistered = userService.register(user);
        System.out.println("user2");
        if (checkRegistered) {
            System.out.println("user3");
            redirectAttributes.addFlashAttribute("success", "!!! Registered Successfully !!!");
        } else {
            System.out.println("user4");
            redirectAttributes.addFlashAttribute("error", "!!! Already Registered !!!");
        }
        System.out.println("user5");
        return "redirect:/register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "loginForm";
    }
}
