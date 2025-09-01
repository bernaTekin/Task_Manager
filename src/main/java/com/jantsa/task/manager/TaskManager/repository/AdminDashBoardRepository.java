package com.jantsa.task.manager.TaskManager.repository;


import com.jantsa.task.manager.TaskManager.entity.User;
import com.jantsa.task.manager.TaskManager.enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdminDashBoardRepository extends JpaRepository<User,Integer> {

    List <User> findByUserRole(UserRole userRole);

    List<User> findByUserRoleIn(List<UserRole> roles);

    @Transactional
    void deleteByCompanyId(Integer companyId);

    boolean existsByCompanyId(Integer companyId);



}
