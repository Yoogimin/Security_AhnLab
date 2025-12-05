package com.ahnlab.security_ahnlab.service;

import com.ahnlab.security_ahnlab.dao.SecurityLogRepository;
import com.ahnlab.security_ahnlab.entity.SecurityLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 조회 전용 최적화 (이거 한 줄이 면접 포인트!)
public class DashboardService {

    private final SecurityLogRepository repository;

    /**
     * [보안 로직] 접속자 환경 분석 및 위협 탐지 시뮬레이션
     * - Agent-less 환경에서 HTTP Header를 분석하여 위협 등급 산정
     * - 분석 결과(Entity)를 DB에 적재 (Audit)
     */
    @Transactional
    public void analyzeAndSaveLog(String ipAddress, String userAgent) {

        // 1. 초기값 설정 (Default: 정상 접근)
        String attackType = "Safe Access";
        String severity = "LOW";
        String status = "MONITORED";

        // 2. 위협 탐지 시나리오 (Threat Detection Scenario)
        // [Rule] Chrome 브라우저가 아닌 경우 '잠재적 위협'으로 간주 (시뮬레이션)
        if (userAgent == null || !userAgent.contains("Chrome")) {
            attackType = "Unknown User-Agent (Suspicious)";
            severity = "MEDIUM";
            status = "WARNING";
        }

        // 3. 로그 엔티티 생성 (Entity Mapping)
        SecurityLog securityLog = new SecurityLog();
        securityLog.setIpAddress(ipAddress);
        securityLog.setAttackType(attackType);
        securityLog.setSeverityLevel(severity);
        securityLog.setStatus(status);
        // detectedTime은 @CreationTimestamp에 의해 자동 기록됨

        // 4. 영구 저장 (Persistent Save)
        repository.save(securityLog);
    }

    // 1. 전체 로그 가져오기
    public List<SecurityLog> getAllLogs() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "detectedTime"));
    }

    // 2. 공격 유형별 통계 가져오기
    public List<Object[]> getAttackStatistics() {
        return repository.countByAttackType();
    }

    // 3. (PC 점검용) 로그 저장하기 - 이건 쓰기니까 따로 트랜잭션 검
    @Transactional
    public void saveLog(SecurityLog log) {
        repository.save(log);
    }
}