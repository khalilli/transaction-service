package com.example.transaction.controller;

import com.example.transaction.dto.CreateTransactionRequest;
import com.example.transaction.dto.TransactionResponse;
import com.example.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest request
    ) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{transactionId}")
    public TransactionResponse getTransaction(
            @PathVariable UUID transactionId
    ) {
        return transactionService.getTransaction(transactionId);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponse> getTransactionsByAccount(
            @PathVariable UUID accountId
    ) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @GetMapping("/payment/{paymentId}")
    public List<TransactionResponse> getTransactionsByPayment(
            @PathVariable UUID paymentId
    ) {
        return transactionService.getTransactionsByPayment(paymentId);
    }
}