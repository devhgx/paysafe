package com.tai.paysafe.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String transactionType;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = true)
    private boolean confirmationStatus;
    @Column(nullable = true)
    private int processStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_bank_id", nullable = true)
    private UserBank withDrawBank;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_bank_id", nullable = true)
    private Bank depositBank;
    @Column(nullable = true)
    private String depositFromAccountBankNumber;
    @Column(nullable = true)
    private String depositFromAccountName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id", nullable = true)
    private User senderAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_account_id", nullable = true)
    private User recipientAccount;

    @Column(nullable = true)
    private String note;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = new Date();
    }
}


