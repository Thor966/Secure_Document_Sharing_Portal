package com.doc.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class DocumentPermissionsDTO {

    private Long dpid;
    private Long documentId;
    private String documentName;
    private String grantedBy;
    private String grantedToUser;
    private LocalDateTime expiryTime;
    private String remainingTime;
    private String accessType;
    private String shareType;
    private String status;
    private LocalDateTime insertedOn;
}
