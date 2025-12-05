package com.ahnlab.security_ahnlab.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp; // 시간 자동 생성용

import java.util.Date;

@Entity
@Data
@Table(name = "SECURITY_LOG") // 테이블명 명시 (권장)
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_seq")
    @SequenceGenerator(name = "log_seq", sequenceName = "SEQ_LOG_ID", allocationSize = 1)
    private Long logId;

    // 👇 nullable = false 추가 (DB의 NOT NULL과 맞춤)
    @Column(name = "IP_ADDRESS", nullable = false, length = 50)
    private String ipAddress;

    @Column(name = "ATTACK_TYPE", nullable = false, length = 50)
    private String attackType;

    @Column(name = "SEVERITY_LEVEL", nullable = false, length = 20)
    private String severityLevel;

    // 👇 자바에서 저장할 때 자동으로 현재 시간 넣어주는 마법의 어노테이션
    @CreationTimestamp
    @Column(name = "DETECTED_TIME", nullable = false, updatable = false) // 수정 불가
    private Date detectedTime;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;
}