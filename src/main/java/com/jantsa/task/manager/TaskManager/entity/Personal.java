package com.jantsa.task.manager.TaskManager.entity;


import jakarta.persistence.*;

@Table(name = "Personal")
@Entity
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "company_id")
    private Integer companyId;


    public Personal(Integer id, String name, String surname, Integer companyId) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.companyId = companyId;
    }

    public Personal() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsurname() {
        return surname;
    }

    public void setsurname(String surname) {
        this.surname = surname;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
