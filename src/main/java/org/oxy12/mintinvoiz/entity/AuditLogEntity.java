package org.oxy12.mintinvoiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "user_email")
    private String userEmail;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserEntity.Role userRole;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "device_info", columnDefinition = "TEXT")
    private String deviceInfo;
    
    @Column(name = "endpoint", nullable = false)
    private String endpoint;
    
    @Column(name = "http_method", nullable = false)
    private String httpMethod;
    
    @Column(name = "request_params", columnDefinition = "TEXT")
    private String requestParams;
    
    @Column(name = "status_code")
    private Integer statusCode;
    
    @Column(name = "response_snippet", columnDefinition = "TEXT")
    private String responseSnippet;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
    
    @Column(name = "action_type")
    private String actionType;
    
    @Column(name = "resource_id")
    private String resourceId;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @CreationTimestamp
    @Column(name = "timestamp", updatable = false)
    private LocalDateTime timestamp;
}