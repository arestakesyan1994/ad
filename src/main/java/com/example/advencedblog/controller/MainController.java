package com.example.advencedblog.controller;

import com.example.advencedblog.model.User;
import com.example.advencedblog.model.UserType;
import com.example.advencedblog.repository.AdRepository;
import com.example.advencedblog.repository.CategoryRepository;
import com.example.advencedblog.repository.UserRepository;
import com.example.advencedblog.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String main(ModelMap map, @AuthenticationPrincipal UserDetails userDetails) {
        map.addAttribute("allCategory", categoryRepository.findAll());
        map.addAttribute("allAd", adRepository.findAll());
        map.addAttribute("isLoggedIn", userDetails != null);
        return "index";
    }

    @GetMapping("/signIn")
    public String signIn(ModelMap modelMap) {
        modelMap.addAttribute("user", new User());
        return "signIn";
    }

    @GetMapping("/history")
    public String history() {
        return "history";
    }

    @GetMapping("/classification")
    public String classification() {
        return "classification";
    }

    @GetMapping("/technology")
    public String technology() {
        return "technology";
    }

    @GetMapping("/advertising")
    public String advertising() {
        return "advertising";
    }


    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/loginUser")
    public String loginSuccess(@AuthenticationPrincipal UserDetails userDetails) {
        CurrentUser currentUser = (CurrentUser) userDetails;
        if (currentUser != null) {
            return "admin";
        }
        return "redirect:/signIn";
    }

}
