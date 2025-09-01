package com.jantsa.task.manager.TaskManager.service;

import com.jantsa.task.manager.TaskManager.dto.BugReportRequestDto;
import com.jantsa.task.manager.TaskManager.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserDashBoard {

    boolean changePassword(Integer companyId, String oldPassword, String newPassword);
    public void save(BugReportRequestDto bugReportRequestDto);
    public List<User> findByUserRole();


    User findByUserId(Integer userId);
}
