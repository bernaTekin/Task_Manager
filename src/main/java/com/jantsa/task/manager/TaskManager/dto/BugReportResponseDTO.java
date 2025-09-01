package com.jantsa.task.manager.TaskManager.dto;

import com.jantsa.task.manager.TaskManager.enums.Department;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public class BugReportResponseDTO {


    private Integer id;
    private Integer personalId;
    private String header;

    private String body;

    private Integer companyId;

    private TaskStatus status;

    private LocalDateTime talep_date;


    private LocalDateTime atanma_date;

    private LocalDateTime iptal_date;

    private LocalDateTime kabul_date;


    private LocalDateTime bitis_date;


    private String gonderen_isim;



    private String personal_name;

    private boolean isActiveAdmin = true;

    private boolean isActivePersonel = true;

    private Department department;

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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getTalep_date() {
        return talep_date;
    }

    public void setTalep_date(LocalDateTime talep_date) {
        this.talep_date = talep_date;
    }

    public LocalDateTime getAtanma_date() {
        return atanma_date;
    }

    public void setAtanma_date(LocalDateTime atanma_date) {
        this.atanma_date = atanma_date;
    }

    public LocalDateTime getIptal_date() {
        return iptal_date;
    }

    public void setIptal_date(LocalDateTime iptal_date) {
        this.iptal_date = iptal_date;
    }

    public LocalDateTime getKabul_date() {
        return kabul_date;
    }

    public void setKabul_date(LocalDateTime kabul_date) {
        this.kabul_date = kabul_date;
    }

    public LocalDateTime getBitis_date() {
        return bitis_date;
    }

    public void setBitis_date(LocalDateTime bitis_date) {
        this.bitis_date = bitis_date;
    }

    public String getGonderen_isim() {
        return gonderen_isim;
    }

    public void setGonderen_isim(String gonderen_isim) {
        this.gonderen_isim = gonderen_isim;
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

    public boolean isActiveAdmin() {
        return isActiveAdmin;
    }

    public void setActiveAdmin(boolean activeAdmin) {
        isActiveAdmin = activeAdmin;
    }

    public boolean isActivePersonel() {
        return isActivePersonel;
    }

    public void setActivePersonel(boolean activePersonel) {
        isActivePersonel = activePersonel;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


    public BugReportResponseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BugReportResponseDTO(Integer id, String header, String body, Integer companyId, TaskStatus status, LocalDateTime talep_date, LocalDateTime atanma_date, LocalDateTime iptal_date, LocalDateTime kabul_date, LocalDateTime bitis_date, String gonderen_isim, Integer personalId, String personal_name, boolean isActiveAdmin, boolean isActivePersonel, Department department) {
        this.id = id;
        this.header = header;
        this.body = body;
        this.companyId = companyId;
        this.status = status;
        this.talep_date = talep_date;
        this.atanma_date = atanma_date;
        this.iptal_date = iptal_date;
        this.kabul_date = kabul_date;
        this.bitis_date = bitis_date;
        this.gonderen_isim = gonderen_isim;
        this.personalId = personalId;
        this.personal_name = personal_name;
        this.isActiveAdmin = isActiveAdmin;
        this.isActivePersonel = isActivePersonel;
        this.department = department;
    }
}
