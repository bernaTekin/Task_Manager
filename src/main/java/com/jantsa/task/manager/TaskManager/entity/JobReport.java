package com.jantsa.task.manager.TaskManager.entity;

import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_report")
public class JobReport {

    @Id
    private Integer id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id") // job_report.id = report.id
    private Report report;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "company_id")
    private Integer companyId;
    @Column(name = "header", nullable = false)
    private String header;
    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;
    @Column(name = "atanma_tarihi")
    private LocalDateTime atanma_date;
    @Column(name = "iptal_tarihi")
    private LocalDateTime iptal_date;
    @Column(name = "kabul_tarihi")
    private LocalDateTime kabul_date;
    @Column(name = "bitis_tarihi")
    private LocalDateTime bitis_date;
    @Column(name = "gonderen_isim")
    private String gonderen_isim;
    @Column(name = "hasJobReport")
    private boolean hasJobReport;

    public JobReport(Integer id,
                     Report report,
                     String name,
                     String surname,
                     Integer companyId,
                     String header,
                     String body,
                     LocalDateTime atanma_date,
                     LocalDateTime iptal_date,
                     LocalDateTime kabul_date,
                     LocalDateTime bitis_date,
                     String gonderen_isim,
                     boolean hasJobReport) {
        this.id = id;
        this.report = report;
        this.name = name;
        this.surname = surname;
        this.companyId = companyId;
        this.header = header;
        this.body = body;
        this.atanma_date = atanma_date;
        this.iptal_date = iptal_date;
        this.kabul_date = kabul_date;
        this.bitis_date = bitis_date;
        this.gonderen_isim = gonderen_isim;
        this.hasJobReport = hasJobReport;
    }

    public boolean isHasJobReport() {
        return hasJobReport;
    }

    public void setHasJobReport(boolean hasJobReport) {
        this.hasJobReport = hasJobReport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public JobReport() {
    }
}
