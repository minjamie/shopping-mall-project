package com.example.shopping.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@MappedSuperclass
public class BaseEntity {

    @CreatedBy
    @Column(updatable = false, nullable = false)
    private String createdBy; //생성자

    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy; //수정자
}
