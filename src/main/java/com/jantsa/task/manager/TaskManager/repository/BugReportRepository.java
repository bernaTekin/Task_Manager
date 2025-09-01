package com.jantsa.task.manager.TaskManager.repository;

import com.jantsa.task.manager.TaskManager.dto.BugReportRequestDto;
import com.jantsa.task.manager.TaskManager.dto.BugReportResponseDTO;
import com.jantsa.task.manager.TaskManager.entity.Report;
import com.jantsa.task.manager.TaskManager.enums.TaskStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface BugReportRepository extends JpaRepository<Report,Integer> {

    List<Report> findByPersonalId(Integer personalId);
    List<Report> findByCompanyId(Integer personalId);
    //ArrayList<BugReportResponseDTO> findByPersonalId(Integer personalId);
    List<Report> findByPersonalIdAndIsActivePersonelTrue(Integer personalId);






}
