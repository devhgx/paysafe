package com.tai.paysafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column( nullable = false)
    private String firstName;

    @Column( nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column( nullable = false)
    private String role;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserBank> userBanks;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

    @PrePersist
    protected void onCreate() throws NoSuchAlgorithmException {
        this.createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }
}