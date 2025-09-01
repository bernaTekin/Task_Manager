package com.jantsa.task.manager.TaskManager.dto;

import jakarta.persistence.Column;


public class BugReportRequestDto {


    private String header;

    private String body;

    private Integer companyId;


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

    public BugReportRequestDto(String header, String body, Integer companyId) {
        this.header = header;
        this.body = body;
        this.companyId = companyId;
    }

    public BugReportRequestDto() {
    }
}
