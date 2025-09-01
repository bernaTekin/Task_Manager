package com.jantsa.task.manager.TaskManager.controller;


import com.jantsa.task.manager.TaskManager.dto.AdminMonthlyJobReportDTO;
import com.jantsa.task.manager.TaskManager.dto.CreateUserRequestDto;
import com.jantsa.task.manager.TaskManager.dto.UserUpdateDto;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.Department;
import com.jantsa.task.manager.TaskManager.repository.BugReportRepository;
import com.jantsa.task.manager.TaskManager.service.AdminDashBoardImpl;
import com.jantsa.task.manager.TaskManager.service.UserDashBoardImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping(path = "/admin-dashboard")
public class AdminDashboardController {

    @Autowired
    AdminDashBoardImpl adminDashBoard;

    @Autowired
    UserDashBoardImpl userDashBoard;

    @GetMapping
    public String getAdminDashboard() {

        return "/AdminDashBoard";
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userDashBoard.findByUserId(userId);
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/all-users")
    @ResponseBody
    public List<User> getAllPersonal(){
        return adminDashBoard.getAllPersonal();
    }

    @GetMapping(path = "/all-reports")
    @ResponseBody
    public List<Report> findAll(){
        return adminDashBoard.findAll();
    }

    @PostMapping("/reports/{id}/reject")
    @ResponseBody
    public Report rejectReport(@PathVariable Integer id) {
        return adminDashBoard.rejectReport(id);
    }

    @PostMapping("/reports/{id}/complete")
    @ResponseBody
    public Report completeReport(
            @PathVariable Integer id,
            @RequestParam("actorId") Integer actorId) {
        return adminDashBoard.completeReport(id, actorId);
    }

    @PostMapping("/reports/{id}/assign")
    @ResponseBody
    public Report assignReport(
            @PathVariable Integer id,
            @RequestParam("personalId") Integer companyId,
            @RequestParam("department")Department department) {
            return adminDashBoard.assignReport(id, companyId, department);
    }

    @PostMapping("/reports/{reportId}/deactivate")
    @ResponseBody
    public Report delete(@PathVariable Integer reportId){
        return adminDashBoard.deactivate(reportId);
    }

    @GetMapping("/job-reports/last-month")
    public ResponseEntity<List<AdminMonthlyJobReportDTO>> getLastMonthJobReports() {
        return ResponseEntity.ok(adminDashBoard.getLastMonthJobReports());
    }

    @PostMapping("/reports/{id}/approve-cancellation")
    @ResponseBody
    public Report approveCancellation(@PathVariable Integer id) {
        return adminDashBoard.approveCancellationRequest(id);
    }

    @PostMapping("/reports/{id}/reject-cancellation")
    @ResponseBody
    public Report rejectCancellation(@PathVariable Integer id) {
        return adminDashBoard.rejectCancellationRequest(id);
    }


    @PostMapping("/users/add")
    @ResponseBody
    public ResponseEntity<?> addNewUser(@RequestBody CreateUserRequestDto userDto) {
        try {
            User createdUser = userDashBoard.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bilinmeyen bir hata oluştu.");
        }
    }


    @GetMapping("/searchable-users")
    @ResponseBody
    public ResponseEntity<List<User>> getSearchableUsers() {
        return ResponseEntity.ok(adminDashBoard.getSearchableUsers());
    }

    @PutMapping("/users/update/{companyId}")
    @ResponseBody
    public ResponseEntity<User> updateUserDetails(@PathVariable Integer companyId, @RequestBody UserUpdateDto userDto) {
        userDto.setCompanyId(companyId);
        try {
            User updatedUser = adminDashBoard.updateUser(userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/delete/{companyId}")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(@PathVariable Integer companyId) {
        try {
            adminDashBoard.deleteUser(companyId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
