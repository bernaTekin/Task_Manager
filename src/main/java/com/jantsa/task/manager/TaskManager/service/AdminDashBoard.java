package com.jantsa.task.manager.TaskManager.service;

import com.jantsa.task.manager.TaskManager.dto.AdminMonthlyJobReportDTO;
import com.jantsa.task.manager.TaskManager.dto.UserUpdateDto;
import com.jantsa.task.manager.TaskManager.entity.JobReport;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.Department;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AdminDashBoard {

    public List<User> getAllPersonal();
    public List <Report> findAll();
    Report rejectReport(Integer reportId);
    Optional<Report> findById(Integer id);
    Report completeReport(Integer reportId, Integer personalId);
    Report assignReport(Integer reportId, Integer companyId, Department department);
    public Report deactivate(Integer reportId);
    List<AdminMonthlyJobReportDTO> getLastMonthJobReports();
    User updateUser(UserUpdateDto userDto);
    List<User> getSearchableUsers();
    void deleteUser(Integer companyId);

}
