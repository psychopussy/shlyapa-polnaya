package org.shlyapa.controller;

import jakarta.servlet.http.HttpSession;
import org.shlyapa.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/students";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/students";
        }
        return "login-form";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        boolean isAuthenticated = userService.authenticate(username, password);
        if (isAuthenticated) {
            session.setAttribute("user", username);
            return "redirect:/students";
        }
        model.addAttribute("error", "Неверное имя пользователя или пароль");
        return "login-form";
    }

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/students";
        }
        return "register-form";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        boolean isRegistered = userService.register(username, password);
        if (isRegistered) {
            return "redirect:/login?registered=true";
        }
        model.addAttribute("error", "Пользователь с таким логином уже существует или данные некорректны");
        return "register-form";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
