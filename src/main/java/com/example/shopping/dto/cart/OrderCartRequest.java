package com.example.shopping.dto.cart;

import com.example.shopping.domain.Enum.RequestType;
import lombok.Getter;

@Getter
public class OrderCartRequest {
    private String deliveryName;
    private RequestType requestType;
    private String recipient;
    private String zipCode;
    private String mainAddress;
    private String detailAddress;
    private String contactA;
    private String contactB;
    private Boolean isDefault;
    private String customRequest;

    public OrderCartRequest(String deliveryName, RequestType requestType, String recipient, String zipCode, String mainAddress, String detailAddress, String contactA, String contactB, Boolean isDefault, String customRequest) {
        this.deliveryName = deliveryName;
        this.requestType = requestType;
        this.recipient = recipient;
        this.zipCode = zipCode;
        this.mainAddress = mainAddress;
        this.detailAddress = detailAddress;
        this.contactA = contactA;
        this.contactB = contactB;
        this.isDefault = isDefault == null ? false : isDefault;
        this.customRequest = customRequest;
    }


    @Override
    public String toString() {
        return "OrderCartRequest{" +
                " deliveryName='" + deliveryName + '\'' +
                ", requestType=" + requestType +
                ", zipCode='" + zipCode + '\'' +
                ", mainAddress='" + mainAddress + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", contactA='" + contactA + '\'' +
                ", contactB='" + contactB + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
}
