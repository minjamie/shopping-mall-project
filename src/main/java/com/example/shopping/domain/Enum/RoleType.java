package com.example.shopping.domain.Enum;

public enum RoleType {

    ROLE_USER("일반 사용자"),
    ROLE_SELLER("판매자");


    private String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
