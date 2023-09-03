package com.example.shopping.domain.Enum;

public enum RequestType {
    SELECT_REQUEST("배송시 요청사항을 선택해 주세요"),
    LEAVE_AT_DOOR("부재시 문앞에 놓아주세요."),
    LEAVE_AT_SECURITY("부재시 경비실에 맡겨 주세요."),
    CONTACT_IF_ABSENT("부재시 전화 또는 문자 주세요."),
    PUT_IN_MAILBOX("택배함에 넣어 주세요."),
    HANDLE_WITH_CARE("파손위험상품입니다. 배송시 주의해주세요."),
    CONTACT_BEFORE_DELIVERY("배송전에 연락주세요."),
    CUSTOM_INPUT("직접입력");

    private String description;

    RequestType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}