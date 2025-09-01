package com.jantsa.task.manager.TaskManager.dto;

import com.jantsa.task.manager.TaskManager.enums.UserRole;

public class UserUpdateDto {

    private Integer companyId;
    private String name;
    private String surName;
    private String password;
    private UserRole userRole;


    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer companyId) { this.companyId = companyId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurName() { return surName; }
    public void setSurName(String surName) { this.surName = surName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }

}
