package com.jantsa.task.manager.TaskManager.repository;


import com.jantsa.task.manager.TaskManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDashBoardRepository extends JpaRepository<User,Integer> {

    Optional<User> findByCompanyId(Integer companyId);


}
