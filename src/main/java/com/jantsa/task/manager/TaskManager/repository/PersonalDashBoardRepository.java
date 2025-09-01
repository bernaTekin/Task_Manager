package com.jantsa.task.manager.TaskManager.repository;

import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonalDashBoardRepository extends JpaRepository<User, Integer> {
     List<User> findByUserRole(UserRole userRole);
}