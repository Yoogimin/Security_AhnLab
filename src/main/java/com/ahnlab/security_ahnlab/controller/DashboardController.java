package com.ahnlab.security_ahnlab.controller;

import com.ahnlab.security_ahnlab.entity.SecurityLog;
import com.ahnlab.security_ahnlab.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    // DashboardService
    private final DashboardService dsvc;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 1. [리스트용] Service를 통해 로그 가져오기
        List<SecurityLog> logs = dsvc.getAllLogs();
        model.addAttribute("logs", logs);

        // 2. [차트용] Service를 통해 통계 가져오기
        List<Object[]> stats = dsvc.getAttackStatistics();

        // 3. 차트 라이브러리용 데이터 가공
        List<String> labels = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] row : stats) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            labels.add(type);
            counts.add(count);
        }

        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", counts);

        return "dashboard";
    }

    // ✅ PC 점검 기능 (여기가 중요! 주석 대신 실제 코드를 넣어야 함)
    @PostMapping("/scan/my-pc")
    public String scanMyPc(HttpServletRequest request) {
        // 1. 접속자 정보 추출 (IP, User-Agent)
        String realIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        // 2. 간단한 보안 진단 로직 (시뮬레이션)
        String attackType = "Safe Access";
        String severity = "LOW";
        String status = "MONITORED";

        // (예시) 브라우저 정보가 없거나 이상하면 '의심'으로 간주
        if (userAgent == null || !userAgent.contains("Chrome")) {
            attackType = "Unknown User-Agent";
            severity = "MEDIUM";
        }

        // 3. Entity 생성 및 데이터 세팅 (NOT NULL 필드 필수!)
        SecurityLog log = new SecurityLog();
        log.setIpAddress(realIp);         // 필수
        log.setAttackType(attackType);    // 필수
        log.setSeverityLevel(severity);   // 필수
        log.setStatus(status);            // 필수
        // detectedTime은 @CreationTimestamp로 자동 생성됨

        // 4. Service를 호출해서 저장 (Repository 직접 호출 X)
        dsvc.saveLog(log);

        return "redirect:/dashboard";
    }
}