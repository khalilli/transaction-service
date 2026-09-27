package com.example.transaction.service;

import com.example.transaction.dto.CreateTransactionRequest;
import com.example.transaction.dto.TransactionResponse;
import com.example.transaction.entity.Transaction;
import com.example.transaction.entity.enums.TransactionStatus;
import com.example.transaction.entity.enums.TransactionType;
import com.example.transaction.exception.ResourceNotFoundException;
import com.example.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateTransaction() {

        UUID accountId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();

        CreateTransactionRequest request =
                new CreateTransactionRequest(
                        accountId,
                        paymentId,
                        TransactionType.TRANSFER_DEBIT,
                        new BigDecimal("50.00"),
                        TransactionStatus.COMPLETED
                );

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(UUID.randomUUID());
        savedTransaction.setAccountId(accountId);
        savedTransaction.setPaymentId(paymentId);
        savedTransaction.setType(TransactionType.TRANSFER_DEBIT);
        savedTransaction.setAmount(new BigDecimal("50.00"));
        savedTransaction.setCurrency("AZN");
        savedTransaction.setStatus(TransactionStatus.COMPLETED);
        savedTransaction.setCreatedAt(Instant.now());

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponse response =
                transactionService.createTransaction(request);

        assertEquals(
                savedTransaction.getId(),
                response.id()
        );

        assertEquals(
                accountId,
                response.accountId()
        );

        assertEquals(
                paymentId,
                response.paymentId()
        );

        assertEquals(
                TransactionType.TRANSFER_DEBIT,
                response.type()
        );

        assertEquals(
                new BigDecimal("50.00"),
                response.amount()
        );

        assertEquals(
                "AZN",
                response.currency()
        );

        assertEquals(
                TransactionStatus.COMPLETED,
                response.status()
        );

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldGetTransaction() {

        UUID transactionId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAccountId(accountId);
        transaction.setPaymentId(paymentId);
        transaction.setType(TransactionType.TRANSFER_DEBIT);
        transaction.setAmount(new BigDecimal("30.00"));
        transaction.setCurrency("AZN");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setCreatedAt(createdAt);

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        TransactionResponse response =
                transactionService.getTransaction(transactionId);

        assertEquals(transactionId, response.id());
        assertEquals(accountId, response.accountId());
        assertEquals(paymentId, response.paymentId());
        assertEquals(TransactionType.TRANSFER_DEBIT, response.type());
        assertEquals(new BigDecimal("30.00"), response.amount());
        assertEquals("AZN", response.currency());
        assertEquals(TransactionStatus.COMPLETED, response.status());
        assertEquals(createdAt, response.createdAt());

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotExist() {

        UUID transactionId = UUID.randomUUID();

        when(transactionRepository.findById(transactionId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> transactionService.getTransaction(transactionId)
        );

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void shouldGetAllTransactions() {

        Transaction firstTransaction = new Transaction();
        firstTransaction.setId(UUID.randomUUID());
        firstTransaction.setAccountId(UUID.randomUUID());
        firstTransaction.setPaymentId(UUID.randomUUID());
        firstTransaction.setType(TransactionType.TRANSFER_DEBIT);
        firstTransaction.setAmount(new BigDecimal("25.00"));
        firstTransaction.setCurrency("AZN");
        firstTransaction.setStatus(TransactionStatus.COMPLETED);
        firstTransaction.setCreatedAt(Instant.now());

        Transaction secondTransaction = new Transaction();
        secondTransaction.setId(UUID.randomUUID());
        secondTransaction.setAccountId(UUID.randomUUID());
        secondTransaction.setPaymentId(UUID.randomUUID());
        secondTransaction.setType(TransactionType.TRANSFER_CREDIT);
        secondTransaction.setAmount(new BigDecimal("25.00"));
        secondTransaction.setCurrency("AZN");
        secondTransaction.setStatus(TransactionStatus.COMPLETED);
        secondTransaction.setCreatedAt(Instant.now());

        when(transactionRepository.findAll())
                .thenReturn(List.of(firstTransaction, secondTransaction));

        List<TransactionResponse> responses =
                transactionService.getAllTransactions();

        assertEquals(2, responses.size());

        assertEquals(
                firstTransaction.getId(),
                responses.get(0).id()
        );

        assertEquals(
                TransactionType.TRANSFER_DEBIT,
                responses.get(0).type()
        );

        assertEquals(
                secondTransaction.getId(),
                responses.get(1).id()
        );

        assertEquals(
                TransactionType.TRANSFER_CREDIT,
                responses.get(1).type()
        );

        verify(transactionRepository).findAll();
    }

    @Test
    void shouldGetTransactionsByAccount() {

        UUID accountId = UUID.randomUUID();

        Transaction firstTransaction = new Transaction();
        firstTransaction.setId(UUID.randomUUID());
        firstTransaction.setAccountId(accountId);
        firstTransaction.setPaymentId(UUID.randomUUID());
        firstTransaction.setType(TransactionType.TRANSFER_DEBIT);
        firstTransaction.setAmount(new BigDecimal("20.00"));
        firstTransaction.setCurrency("AZN");
        firstTransaction.setStatus(TransactionStatus.COMPLETED);
        firstTransaction.setCreatedAt(Instant.now());

        Transaction secondTransaction = new Transaction();
        secondTransaction.setId(UUID.randomUUID());
        secondTransaction.setAccountId(accountId);
        secondTransaction.setPaymentId(UUID.randomUUID());
        secondTransaction.setType(TransactionType.TRANSFER_CREDIT);
        secondTransaction.setAmount(new BigDecimal("15.00"));
        secondTransaction.setCurrency("AZN");
        secondTransaction.setStatus(TransactionStatus.COMPLETED);
        secondTransaction.setCreatedAt(Instant.now());

        when(transactionRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstTransaction, secondTransaction));

        List<TransactionResponse> responses =
                transactionService.getTransactionsByAccount(accountId);

        assertEquals(2, responses.size());

        assertEquals(
                firstTransaction.getId(),
                responses.get(0).id()
        );

        assertEquals(
                secondTransaction.getId(),
                responses.get(1).id()
        );

        assertEquals(
                accountId,
                responses.get(0).accountId()
        );

        assertEquals(
                accountId,
                responses.get(1).accountId()
        );

        verify(transactionRepository).findByAccountId(accountId);
    }

    @Test
    void shouldGetTransactionsByPayment() {

        UUID paymentId = UUID.randomUUID();

        Transaction debitTransaction = new Transaction();
        debitTransaction.setId(UUID.randomUUID());
        debitTransaction.setAccountId(UUID.randomUUID());
        debitTransaction.setPaymentId(paymentId);
        debitTransaction.setType(TransactionType.TRANSFER_DEBIT);
        debitTransaction.setAmount(new BigDecimal("40.00"));
        debitTransaction.setCurrency("AZN");
        debitTransaction.setStatus(TransactionStatus.COMPLETED);
        debitTransaction.setCreatedAt(Instant.now());

        Transaction creditTransaction = new Transaction();
        creditTransaction.setId(UUID.randomUUID());
        creditTransaction.setAccountId(UUID.randomUUID());
        creditTransaction.setPaymentId(paymentId);
        creditTransaction.setType(TransactionType.TRANSFER_CREDIT);
        creditTransaction.setAmount(new BigDecimal("40.00"));
        creditTransaction.setCurrency("AZN");
        creditTransaction.setStatus(TransactionStatus.COMPLETED);
        creditTransaction.setCreatedAt(Instant.now());

        when(transactionRepository.findByPaymentId(paymentId))
                .thenReturn(List.of(debitTransaction, creditTransaction));

        List<TransactionResponse> responses =
                transactionService.getTransactionsByPayment(paymentId);

        assertEquals(2, responses.size());

        assertEquals(
                debitTransaction.getId(),
                responses.get(0).id()
        );

        assertEquals(
                TransactionType.TRANSFER_DEBIT,
                responses.get(0).type()
        );

        assertEquals(
                creditTransaction.getId(),
                responses.get(1).id()
        );

        assertEquals(
                TransactionType.TRANSFER_CREDIT,
                responses.get(1).type()
        );

        assertEquals(
                paymentId,
                responses.get(0).paymentId()
        );

        assertEquals(
                paymentId,
                responses.get(1).paymentId()
        );

        verify(transactionRepository).findByPaymentId(paymentId);
    }
}