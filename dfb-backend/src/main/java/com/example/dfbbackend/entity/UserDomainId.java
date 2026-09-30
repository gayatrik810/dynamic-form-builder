package com.example.dfbbackend.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class UserDomainId implements Serializable {

    private Long userId;

    private Long domainId;
}