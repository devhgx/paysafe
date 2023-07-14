package com.tai.paysafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
@Entity
@Table(name = "user_statement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column( nullable = false)
    private BigDecimal balance;

    @Column( nullable = false)
    private BigDecimal income;

    @Column( nullable = false)
    private BigDecimal expense;

    @Column( nullable = false)
    private boolean activeStatus;

    @Column( nullable = false)
    private Long userId;
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
