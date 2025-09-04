package com.jantsa.task.manager.TaskManager.service;


import com.jantsa.task.manager.TaskManager.dto.BugReportRequestDto;
import com.jantsa.task.manager.TaskManager.dto.BugReportResponseDTO;
import com.jantsa.task.manager.TaskManager.dto.CreateUserRequestDto;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import com.jantsa.task.manager.TaskManager.enums.UserRole;
import com.jantsa.task.manager.TaskManager.repository.BugReportRepository;
import com.jantsa.task.manager.TaskManager.repository.UserDashBoardRepository;

import com.jantsa.task.manager.TaskManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserDashBoardImpl implements UserDashBoard{

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDashBoardRepository userDashBoardRepository;

    @Autowired
    BugReportRepository bugReportRepository;

    @Override
    public boolean changePassword(Integer companyId, String oldPassword, String newPassword) {
        Optional<User> userOpt = userDashBoardRepository.findByCompanyId(companyId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userDashBoardRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(BugReportRequestDto bugReportRequestDto) {

        Optional<User> user = Optional.of(new User());
        user = userDashBoardRepository.findByCompanyId(bugReportRequestDto.getCompanyId());
        Report new_report = new Report();
        LocalDateTime now = LocalDateTime.now();
        new_report.setHeader(bugReportRequestDto.getHeader());
        new_report.setBody(bugReportRequestDto.getBody());
        new_report.setCompanyId(bugReportRequestDto.getCompanyId());
        new_report.setBitis_date(null);
        new_report.setKabul_date(null);
        new_report.setAtanma_date(null);
        new_report.setTalep_date(now);
        new_report.setIptal_date(null);
        new_report.setStatus(TaskStatus.BEKLIYOR);
        String isim = user.get().getName();
        String soyisim = user.get().getSurName();
        new_report.setGonderen_isim(isim + " " + soyisim);
        bugReportRepository.save(new_report);
    }
    @Override
    public List<User> findByUserRole() {
        return List.of();
    }

    @Override
    public User findByUserId(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User createUser(CreateUserRequestDto createUserRequestDto) {
        Optional<User> existingUser = userRepository.findByCompanyId(createUserRequestDto.getCompanyId());

        if (existingUser.isPresent()) {
            throw new IllegalStateException("Bu Şirket ID'si zaten kayıtlı. Lütfen farklı bir ID girin.");
        } else if(createUserRequestDto.getCompanyId() < 0) {
            throw new IllegalStateException("Negatif bir şirket ID si girilemez. Lütfen geçerli bir ID giriniz.");
        }

        User user = new User();
        user.setCompanyId(createUserRequestDto.getCompanyId());
        user.setName(createUserRequestDto.getName());
        user.setSurName(createUserRequestDto.getSurName());
        user.setPassword(createUserRequestDto.getPassword());
        user.setUserRole(UserRole.valueOf(createUserRequestDto.getUserRole()));

        return userRepository.save(user);
    }
}

