package com.doc.dto;

public class UserStorageProjection {

    private Long userId;
    private Long totalStorage;

    public UserStorageProjection(Long userId, Long totalStorage) {
        this.userId = userId;
        this.totalStorage = totalStorage;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTotalStorage() {
        return totalStorage;
    }
}