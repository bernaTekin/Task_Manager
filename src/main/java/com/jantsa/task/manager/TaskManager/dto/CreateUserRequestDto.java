package com.jantsa.task.manager.TaskManager.dto;

public class CreateUserRequestDto {
    private Integer companyId;
    private String name;
    private String surName;
    private String password;
    private String userRole;


    public CreateUserRequestDto(Integer companyId,
                                String userRole,
                                String name,
                                String surName,
                                String password) {

        this.companyId = companyId;
        this.userRole = userRole;
        this.name = name;
        this.surName = surName;
        this.password = password;
    }

    public CreateUserRequestDto() {
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
