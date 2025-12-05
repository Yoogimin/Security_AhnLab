package com.ahnlab.security_ahnlab.dao;

import com.ahnlab.security_ahnlab.entity.SecurityLog;
import org.springframework.data.jpa.repository.Query; // <-- 이게 맞음!
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {

    // 통계용 쿼리 (공격 유형별 카운트)
    @Query("SELECT s.attackType, COUNT(s) FROM SecurityLog s GROUP BY s.attackType")
    List<Object[]> countByAttackType();
}