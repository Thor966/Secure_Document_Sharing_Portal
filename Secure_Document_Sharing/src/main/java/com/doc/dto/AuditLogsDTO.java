package com.doc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AuditLogsDTO {

    private String documentName;
    private String action;
    private String performedBy;
    private LocalDateTime time;
}

