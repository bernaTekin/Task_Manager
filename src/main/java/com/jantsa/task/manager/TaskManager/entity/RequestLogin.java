package com.jantsa.task.manager.TaskManager.entity;

import com.jantsa.task.manager.TaskManager.enums.UserRole;
import jakarta.persistence.*;


public class RequestLogin {

    private Integer id;
    private Integer company_id;
    private String password;
    private UserRole userRole;

    public RequestLogin(Integer company_id, String password, Integer id, UserRole userRole) {
        this.company_id = company_id;
        this.password = password;
        this.id = id;
        this.userRole = userRole;
    }

    public RequestLogin() {
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer company_id) {
        this.company_id = company_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
