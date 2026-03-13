package com.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AuditLogService {

    public void logAction(String username, String action, String details) {
        // In a production system, this would write to an 'audit_logs' table
        log.info("AUDIT LOG | User: {} | Action: {} | Timestamp: {} | Details: {}",
                username, action, LocalDateTime.now(), details);
    }
}
