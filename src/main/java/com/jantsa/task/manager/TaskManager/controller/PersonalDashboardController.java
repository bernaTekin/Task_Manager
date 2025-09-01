package com.jantsa.task.manager.TaskManager.controller;
import com.jantsa.task.manager.TaskManager.dto.BugReportRequestDto;
import com.jantsa.task.manager.TaskManager.dto.BugReportResponseDTO;
import com.jantsa.task.manager.TaskManager.dto.JobReportCreate;
import com.jantsa.task.manager.TaskManager.entity.JobReport;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.repository.JobReportRepository;
import com.jantsa.task.manager.TaskManager.service.PersonalDashBoardImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/personal-dashboard")
public class PersonalDashboardController {

    @Autowired
    private PersonalDashBoardImp personalDashboard;

    @Autowired
    JobReportRepository jobReportRepository;

    @GetMapping
    public String getPersonalDashboard() {
        return "PersonalDashBoard";
    }

    @GetMapping("/reports/{personalId}")
    @ResponseBody
    public List<BugReportResponseDTO> getMyReports(@PathVariable Integer personalId) {
        return personalDashboard.getMyReports(personalId);
    }

    @PostMapping("/reports/{reportId}/accept")
    @ResponseBody
    public Report accept(@PathVariable Integer reportId,
                                @RequestParam ("actorId") Integer companyId) {

        return personalDashboard.accept(reportId, companyId);
    }

    @PostMapping("/reports/{reportId}/reject")
    @ResponseBody
    public Report reject(@PathVariable Integer reportId,
                            @RequestParam("actorId") Integer companyId) {
        return personalDashboard.reject(reportId, companyId);
    }
    @PostMapping("/reports/{reportId}/complete")
    @ResponseBody
    public Report complete(@PathVariable Integer reportId,
                              @RequestParam("actorId") Integer companyId) {
        return personalDashboard.complete(reportId, companyId);
    }
    @PostMapping("/reports/{reportId}/deactivate")
    @ResponseBody
    public Report delete(@PathVariable Integer reportId){

        return personalDashboard.deactivate(reportId);
    }

    @PostMapping("/reports/{reportId}/job-report")
    @ResponseBody
    public JobReport save(@PathVariable Integer reportId, @RequestBody JobReportCreate dto) {

        return personalDashboard.save(reportId,dto);
    }

}


