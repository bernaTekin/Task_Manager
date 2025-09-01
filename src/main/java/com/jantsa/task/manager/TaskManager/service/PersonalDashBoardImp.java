package com.jantsa.task.manager.TaskManager.service;

import com.jantsa.task.manager.TaskManager.dto.BugReportResponseDTO;
import com.jantsa.task.manager.TaskManager.dto.JobReportCreate;
import com.jantsa.task.manager.TaskManager.entity.JobReport;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import com.jantsa.task.manager.TaskManager.repository.BugReportRepository;
import com.jantsa.task.manager.TaskManager.repository.JobReportRepository;
import com.jantsa.task.manager.TaskManager.repository.PersonalDashBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonalDashBoardImp implements PersonalDashBoard {

    @Autowired
    PersonalDashBoardRepository personalDashBoardRepository;

    @Autowired
    BugReportRepository bugReportRepository;

    @Autowired
    JobReportRepository jobReportRepository;



    public List<BugReportResponseDTO> getMyReports(Integer personalId) {
        List<TaskStatus> wantedStatuses = List.of(
                TaskStatus.ATANDI,
                TaskStatus.AKTIF,
                TaskStatus.REDDEDILDI,
                TaskStatus.TAMAMLANDI
        );

        List<Report> reports = bugReportRepository.findByPersonalIdAndIsActivePersonelTrue(personalId);


        List<BugReportResponseDTO> response = reports.stream()
                .map(r -> new BugReportResponseDTO(
                        r.getId(),
                        r.getHeader(),
                        r.getBody(),
                        r.getCompanyId(),
                        r.getStatus(),
                        r.getTalep_date(),
                        r.getAtanma_date(),
                        r.getIptal_date(),
                        r.getKabul_date(),
                        r.getBitis_date(),
                        r.getGonderen_isim(),
                        r.getPersonalId(),
                        r.getPersonal_name(),
                        r.isActiveAdmin(),
                        r.isActivePersonel(),
                        r.getDepartment()
                ))
                .collect(Collectors.toList());

        return response;
    }


    @Override
    @Transactional
    public Report accept(Integer reportId, Integer companyId) {
        Report report = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));

        report.setStatus(TaskStatus.AKTIF);
        report.setKabul_date(LocalDateTime.now());

        return bugReportRepository.save(report);
    }

    @Override
    @Transactional
    public Report reject(Integer reportId, Integer companyId) {
        Report report = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));

        report.setStatus(TaskStatus.REDDEDILDI);
        report.setIptal_date(LocalDateTime.now());


        return bugReportRepository.save(report);
    }

    @Override
    public Report complete(Integer reportId, Integer companyId) {

        Report report = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));
        report.setStatus(TaskStatus.TAMAMLANDI);
        report.setBitis_date(LocalDateTime.now());
        return bugReportRepository.save(report);


    }

    @Override
    public Report deactivate(Integer reportId) {

        Report report = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));

        report.setActivePersonel(false);
        return bugReportRepository.save(report);

    }

    @Override
    @Transactional
    public JobReport save(Integer reportId, JobReportCreate jobReportCreate) {
        Report report = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + reportId));

        if (jobReportRepository.existsById(reportId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "İş Raporu zaten var");
        }

        JobReport jr = new JobReport();
        jr.setReport(report);
        jr.setHeader(jobReportCreate.getHeader());
        jr.setBody(jobReportCreate.getBody());
        jr.setCompanyId(report.getCompanyId());
        jr.setGonderen_isim(report.getGonderen_isim());
        jr.setAtanma_date(report.getAtanma_date());
        jr.setIptal_date(report.getIptal_date());
        jr.setKabul_date(report.getKabul_date());
        jr.setBitis_date(report.getBitis_date());
        String full = report.getPersonal_name();
        if (full != null && !full.isBlank()) {
            String[] parts = full.trim().split("\\s+");
            if (parts.length == 1) {
                jr.setName(parts[0]);
            } else {
                jr.setSurname(parts[parts.length - 1]);
                jr.setName(String.join(" ", java.util.Arrays.copyOf(parts, parts.length - 1)));
            }
        }
        jr.setHasJobReport(true);
        return jobReportRepository.save(jr);
    }




}