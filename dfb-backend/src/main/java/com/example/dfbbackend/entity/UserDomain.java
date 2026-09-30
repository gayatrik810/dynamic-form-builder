package com.example.dfbbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_domains")
@Getter
@Setter
public class UserDomain {

    @EmbeddedId
    private UserDomainId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("domainId")
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;
}