package com.jantsa.task.manager.TaskManager.repository;

import com.jantsa.task.manager.TaskManager.dto.AdminMonthlyJobReportDTO;
import com.jantsa.task.manager.TaskManager.entity.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface JobReportRepository extends JpaRepository<JobReport,Integer> {


    @Query("""
select new com.jantsa.task.manager.TaskManager.dto.AdminMonthlyJobReportDTO(
  r.id,
  r.header,
  r.body,
  jr.header,
  jr.body,
  r.gonderen_isim,
  r.atanma_date,
  r.kabul_date,
  r.iptal_date,
  r.bitis_date,
  r.personal_name,
  r.status,
  r.department
)
from Report r
left join JobReport jr on jr.report = r
where r.atanma_date between :from and :to
order by r.atanma_date desc
""")
    List<AdminMonthlyJobReportDTO> findMonthlyForAdmin(@Param("from") LocalDateTime from,
                                                       @Param("to") LocalDateTime to);

}




