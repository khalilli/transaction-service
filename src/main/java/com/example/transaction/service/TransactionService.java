package com.example.transaction.service;

import com.example.transaction.dto.CreateTransactionRequest;
import com.example.transaction.dto.TransactionResponse;
import com.example.transaction.entity.Transaction;
import com.example.transaction.exception.ResourceNotFoundException;
import com.example.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionResponse createTransaction(
            CreateTransactionRequest request
    ) {
        Transaction transaction = new Transaction();

        transaction.setAccountId(request.accountId());
        transaction.setPaymentId(request.paymentId());
        transaction.setType(request.type());
        transaction.setAmount(request.amount());
        transaction.setCurrency("AZN");
        transaction.setStatus(request.status());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return toTransactionResponse(savedTransaction);
    }

    public TransactionResponse getTransaction(UUID transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Transaction not found")
                );

        return toTransactionResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    public List<TransactionResponse> getTransactionsByAccount(
            UUID accountId
    ) {
        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    public List<TransactionResponse> getTransactionsByPayment(
            UUID paymentId
    ) {
        return transactionRepository.findByPaymentId(paymentId)
                .stream()
                .map(this::toTransactionResponse)
                .toList();
    }

    private TransactionResponse toTransactionResponse(
            Transaction transaction
    ) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getPaymentId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }
}