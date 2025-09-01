package com.jantsa.task.manager.TaskManager.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jantsa.task.manager.TaskManager.enums.Department;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "report")
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "header")
    private String header;

    @Column(name = "body")
    private String body;

    @Column(name = "talep_tarihi")
    private LocalDateTime talep_date;

    @Column(name = "atanma_tarihi")
    private LocalDateTime atanma_date;

    @Column(name = "iptal_tarihi")
    private LocalDateTime iptal_date;

    @Column(name = "kabul_tarihi")
    private LocalDateTime kabul_date;

    @Column(name = "bitis_tarihi")
    private LocalDateTime bitis_date;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "gonderen_isim")
    private String gonderen_isim;

    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "personal_id")
    private Integer personalId;

    @Column(name = "personal_name")
    private String personal_name;

    @Column(name = "isActiveAdmin")
    private boolean isActiveAdmin = true;

    @Column(name = "isActivePersonel")
    private boolean isActivePersonel = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "department")
    private Department department;


    public Report(Integer id,
                  String header,
                  String body,
                  LocalDateTime talep_date,
                  LocalDateTime atanma_date,
                  LocalDateTime iptal_date,
                  LocalDateTime kabul_date,
                  LocalDateTime bitis_date,
                  Integer companyId,
                  TaskStatus status,
                  Integer personalId,
                  String personal_name,
                  String gonderen_isim,
                  boolean isActiveAdmin,
                  boolean isActivePersonel,
                  Department department)
    {
        this.id = id;
        this.header = header;
        this.body = body;
        this.talep_date = talep_date;
        this.atanma_date = atanma_date;
        this.iptal_date = iptal_date;
        this.kabul_date = kabul_date;
        this.bitis_date = bitis_date;
        this.companyId = companyId;
        this.status = status;
        this.personalId = personalId;
        this.personal_name = personal_name;
        this.gonderen_isim = gonderen_isim;
        this.isActiveAdmin = isActiveAdmin;
        this.isActivePersonel = isActivePersonel;
        this.department = department;
    }



    public Report() {
    }

    @com.fasterxml.jackson.annotation.JsonProperty("is_active_admin")
    public boolean isActiveAdmin() {
        return isActiveAdmin;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("is_active_admin")
    public void setActiveAdmin(boolean activeAdmin) {
        isActiveAdmin = activeAdmin;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("is_active_personel")
    public boolean isActivePersonel() {
        return isActivePersonel;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("is_active_personel")
    public void setActivePersonel(boolean activePersonel) {
        isActivePersonel = activePersonel;
    }

    public Integer getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Integer personalId) {
        this.personalId = personalId;
    }


    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("createdAt")
    public LocalDateTime getTalep_date() {
        return talep_date;
    }

    public void setTalep_date(LocalDateTime talep_date) {
        this.talep_date = talep_date;
    }

    @JsonProperty("assignedAt")
    public LocalDateTime getAtanma_date() {
        return atanma_date;
    }

    public void setAtanma_date(LocalDateTime atanma_date) {
        this.atanma_date = atanma_date;
    }

    @JsonProperty("rejectedAt")
    public LocalDateTime getIptal_date() {
        return iptal_date;
    }

    public void setIptal_date(LocalDateTime iptal_date) {
        this.iptal_date = iptal_date;
    }

    @JsonProperty("acceptedAt")
    public LocalDateTime getKabul_date() {
        return kabul_date;
    }

    public void setKabul_date(LocalDateTime kabul_date) {
        this.kabul_date = kabul_date;
    }

    @JsonProperty("completedAt")
    public LocalDateTime getBitis_date() {
        return bitis_date;
    }

    public void setBitis_date(LocalDateTime bitis_date) {
        this.bitis_date = bitis_date;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getGonderen_isim() {
        return gonderen_isim;
    }

    public void setGonderen_isim(String gonderen_isim) {
        this.gonderen_isim = gonderen_isim;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }




}

