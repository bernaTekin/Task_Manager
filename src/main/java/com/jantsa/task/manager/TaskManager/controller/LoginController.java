package com.jantsa.task.manager.TaskManager.controller;

import com.jantsa.task.manager.TaskManager.entity.RequestLogin;
import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.UserRole;
import com.jantsa.task.manager.TaskManager.service.LoginServiceImpl;
import com.jantsa.task.manager.TaskManager.service.UserDashBoardImpl; // User nesnesini almak için bu servisi ekledim
import jakarta.servlet.http.HttpServletRequest; // Session için gerekli
import jakarta.servlet.http.HttpSession;      // Session için gerekli
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/task")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private UserDashBoardImpl userDashBoard;


    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin, HttpServletRequest request) {

        // Login servisini sadece bir kez çağırıyoruz.
        User validatedUser = loginService.login(requestLogin);

        if (validatedUser == null) {
            return ResponseEntity.status(401).body("Giriş başarısız: Kullanıcı bulunamadı veya şifre yanlış.");
        }


        HttpSession session = request.getSession(true);
        session.setAttribute("loggedInUser", validatedUser);
        session.setMaxInactiveInterval(30 * 60);


        UserRole userRole = validatedUser.getUserRole();
        String redirectUrl;
        if (userRole == UserRole.ADMIN) {
            redirectUrl = "/admin-dashboard";
        } else if (userRole == UserRole.PERSONEL) {
            redirectUrl = "/personal-dashboard";
        } else if (userRole == UserRole.USER) {
            redirectUrl = "/user-dashboard";
        } else {
            return ResponseEntity.status(500).body("Sunucu hatası: Tanımsız kullanıcı rolü.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", redirectUrl);
        response.put("companyId", validatedUser.getCompanyId());
        response.put("userId", validatedUser.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        // Spring'e bir yönlendirme komutu döndürüyoruz.
        return new ModelAndView("redirect:/task/login");
    }
}