package com.jantsa.task.manager.TaskManager.service;


import com.jantsa.task.manager.TaskManager.dto.AdminMonthlyJobReportDTO;
import com.jantsa.task.manager.TaskManager.dto.UserUpdateDto;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.Department;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import com.jantsa.task.manager.TaskManager.enums.UserRole;
import com.jantsa.task.manager.TaskManager.repository.AdminDashBoardRepository;
import com.jantsa.task.manager.TaskManager.repository.BugReportRepository;
import com.jantsa.task.manager.TaskManager.repository.JobReportRepository;
import com.jantsa.task.manager.TaskManager.repository.UserDashBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminDashBoardImpl implements AdminDashBoard {

    @Autowired
    AdminDashBoardRepository adminDashBoardRepository;

    @Autowired
    BugReportRepository bugReportRepository;

    @Autowired
    UserDashBoardRepository userDashBoardRepository;

    @Autowired
    JobReportRepository jobReportRepository;

    @Override
    public List<User> getAllPersonal() {
        return adminDashBoardRepository.findByUserRole(UserRole.PERSONEL);
    }

    @Override
    public List<Report> findAll() {
        return bugReportRepository.findAll();
    }

    @Override
    @Transactional
    public Report rejectReport(Integer reportId) {
        Report rpt = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found: " + reportId));

        rpt.setStatus(TaskStatus.IPTAL);
        rpt.setIptal_date(LocalDateTime.now());
        rpt.setPersonalId(null);
        rpt.setAtanma_date(null);

        return bugReportRepository.save(rpt);
    }

    @Override
    public Optional<Report> findById(Integer id) {
        return bugReportRepository.findById(id);
    }

    @Override
    @Transactional
    public Report completeReport(Integer reportId, Integer personalId) {
        Report rpt = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found: " + reportId));

        rpt.setStatus(TaskStatus.TAMAMLANDI);
        rpt.setBitis_date(LocalDateTime.now());
        rpt.setPersonalId(personalId);
        User actor = userDashBoardRepository.findById(personalId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + personalId));
        rpt.setPersonal_name(actor.getName() + " " + actor.getSurName());

        return bugReportRepository.save(rpt);
    }

    @Override
    @Transactional
    public Report assignReport(Integer reportId, Integer companyId, Department department) {
        Report rpt = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found: " + reportId));

        rpt.setStatus(TaskStatus.ATANDI);
        rpt.setPersonalId(companyId);
        rpt.setAtanma_date(LocalDateTime.now());
        rpt.setDepartment(department);

        Optional<User> user = Optional.of(new User());
        user = userDashBoardRepository.findByCompanyId(companyId);
        String name = user.get().getName();
        String surname = user.get().getSurName();
        rpt.setPersonal_name(name + " " + surname);

        return bugReportRepository.save(rpt);
    }

    @Override
    public Report deactivate(Integer reportId) {
        Report rpt = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found: " + reportId));
        rpt.setActiveAdmin(false);

        return bugReportRepository.save(rpt);

    }

    @Transactional
    public Report approveCancellationRequest(Integer reportId) {
        Report rpt = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found: " + reportId));
        rpt.setStatus(TaskStatus.BEKLIYOR);

        rpt.setPersonalId(null);
        rpt.setPersonal_name(null);
        rpt.setAtanma_date(null);
        rpt.setDepartment(null);

        return bugReportRepository.save(rpt);
    }

    @Transactional
    public Report rejectCancellationRequest(Integer reportId) {
        Report rpt = bugReportRepository.findById(reportId)
                .orElseThrow(() -> new EntityNotFoundException("Report not found: " + reportId));

        rpt.setStatus(TaskStatus.ATANDI);
        return bugReportRepository.save(rpt);
    }

    @Override
    public List<AdminMonthlyJobReportDTO> getLastMonthJobReports() {

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMonths(1);
        return jobReportRepository.findMonthlyForAdmin(from, to);
    }


    @Override
    public List<User> getSearchableUsers() {
        List<UserRole> rolesToSearch = List.of(UserRole.PERSONEL, UserRole.USER);
        return adminDashBoardRepository.findByUserRoleIn(rolesToSearch);
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateDto userDto) {
        User user = userDashBoardRepository.findByCompanyId(userDto.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with companyId: " + userDto.getCompanyId()));

        user.setName(userDto.getName());
        user.setSurName(userDto.getSurName());
        user.setPassword(userDto.getPassword());
        user.setUserRole(userDto.getUserRole());

        return userDashBoardRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer companyId) {
        if(!adminDashBoardRepository.existsByCompanyId(companyId)) {
            throw new EntityNotFoundException("User not found with id: " + companyId);
        }
        adminDashBoardRepository.deleteByCompanyId(companyId);
    }
}

