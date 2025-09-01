package com.jantsa.task.manager.TaskManager.dto;

import com.jantsa.task.manager.TaskManager.enums.Department;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;

import java.time.LocalDateTime;

public class AdminMonthlyJobReportDTO {


    private Integer reportId;
    private String header; // talep başlığı (Report.header)
    private String body;
    private String personelRaporBaslik; // jr.header
    private String personelRaporIcerik; // jr.body
    private String gonderenIsmi;

    private LocalDateTime atanmaTarihi;
    private LocalDateTime kabulTarihi;
    private LocalDateTime iptalTarihi;
    private LocalDateTime bitisTarihi;

    private String raporuYazanAdSoyad;
    private TaskStatus status;
    private Department department;


    public AdminMonthlyJobReportDTO(
            Integer reportId,
            String header,                // Report.header (talep başlığı)
            String body,                  // Report.body   (talep içeriği)
            String personelRaporBaslik,   // JobReport.header
            String personelRaporIcerik,   // JobReport.body
            String gonderenIsmi,
            LocalDateTime atanmaTarihi,
            LocalDateTime kabulTarihi,
            LocalDateTime iptalTarihi,
            LocalDateTime bitisTarihi,
            String raporuYazanAdSoyad,
            TaskStatus status,
            Department department
    ) {
        this.reportId = reportId;
        this.header = header;
        this.body = body;
        this.personelRaporBaslik = personelRaporBaslik;
        this.personelRaporIcerik = personelRaporIcerik;
        this.gonderenIsmi = gonderenIsmi;
        this.atanmaTarihi = atanmaTarihi;
        this.kabulTarihi = kabulTarihi;
        this.iptalTarihi = iptalTarihi;
        this.bitisTarihi = bitisTarihi;
        this.raporuYazanAdSoyad = raporuYazanAdSoyad;
        this.status = status;
        this.department = department;
    }


    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getHeader() { return header; }
    public void setHeader(String header) { this.header = header; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }


    public String getPersonelRaporBaslik() { return personelRaporBaslik; }
    public void setPersonelRaporBaslik(String v) { this.personelRaporBaslik = v; }

    public String getPersonelRaporIcerik() { return personelRaporIcerik; }
    public void setPersonelRaporIcerik(String v) { this.personelRaporIcerik = v; }

    public String getGonderenIsmi() {
        return gonderenIsmi;
    }

    public void setGonderenIsmi(String gonderenIsmi) {
        this.gonderenIsmi = gonderenIsmi;
    }

    public LocalDateTime getAtanmaTarihi() {
        return atanmaTarihi;
    }

    public void setAtanmaTarihi(LocalDateTime atanmaTarihi) {
        this.atanmaTarihi = atanmaTarihi;
    }

    public LocalDateTime getKabulTarihi() {
        return kabulTarihi;
    }

    public void setKabulTarihi(LocalDateTime kabulTarihi) {
        this.kabulTarihi = kabulTarihi;
    }

    public LocalDateTime getIptalTarihi() {
        return iptalTarihi;
    }

    public void setIptalTarihi(LocalDateTime iptalTarihi) {
        this.iptalTarihi = iptalTarihi;
    }

    public LocalDateTime getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(LocalDateTime bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    public String getRaporuYazanAdSoyad() {
        return raporuYazanAdSoyad;
    }

    public void setRaporuYazanAdSoyad(String raporuYazanAdSoyad) {
        this.raporuYazanAdSoyad = raporuYazanAdSoyad;
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

    public AdminMonthlyJobReportDTO() {
    }
}
