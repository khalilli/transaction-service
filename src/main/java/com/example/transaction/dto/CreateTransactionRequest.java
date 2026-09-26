package com.example.transaction.dto;

import com.example.transaction.entity.enums.TransactionStatus;
import com.example.transaction.entity.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(

        @NotNull
        UUID accountId,

        UUID paymentId,

        @NotNull
        TransactionType type,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount,

        @NotNull
        TransactionStatus status
) {}
