package com.jantsa.task.manager.TaskManager.entity;

import jakarta.persistence.*;

@Table
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "company_id")
    private Integer companyId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Admin(String name, String surname, Integer id, Integer companyId) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.companyId = companyId;
    }

    public Admin() {
    }
}
