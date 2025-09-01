package com.jantsa.task.manager.TaskManager.service;

import com.jantsa.task.manager.TaskManager.dto.BugReportRequestDto;
import com.jantsa.task.manager.TaskManager.dto.BugReportResponseDTO;
import com.jantsa.task.manager.TaskManager.dto.JobReportCreate;
import com.jantsa.task.manager.TaskManager.entity.JobReport;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public interface PersonalDashBoard {
    List<BugReportResponseDTO> getMyReports(Integer personalId);
    Report accept(Integer reportId, Integer companyId);
    Report reject(Integer reportId,Integer companyId);
    Report complete(Integer reportId, Integer companyId);
    public Report deactivate(Integer reportId);
    public JobReport save(Integer reportId, JobReportCreate jobReportCreate);


}