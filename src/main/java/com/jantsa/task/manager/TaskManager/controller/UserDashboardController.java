package com.jantsa.task.manager.TaskManager.controller;


import com.jantsa.task.manager.TaskManager.dto.BugReportRequestDto;
import com.jantsa.task.manager.TaskManager.service.UserDashBoardImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path = "/user-dashboard")
public class UserDashboardController {

    @Autowired
    UserDashBoardImpl userDashBoard;


    @GetMapping
    public String getUserDashboard() {

        return "/UserDashBoard";
    }

    @PostMapping("/new")
    public ResponseEntity<String> createBugReport(@RequestBody BugReportRequestDto request) {
        userDashBoard.save(request);
        return ResponseEntity.ok("Talep başarıyla kaydedildi");
    }


    @GetMapping("/change-password")
    public String loadChangePasswordPage() {
        return "ChangePassword";
    }

    @PostMapping(path = "/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        Integer companyId = Integer.parseInt(body.get("companyId"));
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        boolean success = userDashBoard.changePassword(companyId, oldPassword, newPassword);

        if (success) {
            return ResponseEntity.ok("Şifre başarıyla değiştirildi.");
        } else {
            return ResponseEntity.status(400).body("Mevcut şifre yanlış.");
        }
    }

}
