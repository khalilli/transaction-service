package com.example.transaction.dto;

import com.example.transaction.entity.enums.TransactionStatus;
import com.example.transaction.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID accountId,
        UUID paymentId,
        TransactionType type,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        Instant createdAt
) {}