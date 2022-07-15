package com.test.wallet.exceptions

import org.zalando.problem.Problem
import java.net.URI

fun playerNotFoundProblem(playerId: Long): Throwable {
    return Problem.builder()
        .withType(URI.create("http://localhost:8080/api-problems"))
        .withTitle("Player not found")
        .withDetail("Player with playerId: $playerId doesn't exist")
        .withStatus(org.zalando.problem.Status.BAD_REQUEST)
        .build()
}

fun transactionIdNotUniqueProblem(transactionId: String): Throwable {
    return Problem.builder()
        .withType(URI.create("http://localhost:8080/api-problems"))
        .withTitle("Transaction ID Error")
        .withDetail("Transaction ID $transactionId not unique.")
        .withStatus(org.zalando.problem.Status.BAD_REQUEST)
        .build()
}

fun insufficientBalanceProblem(): Throwable {
    return Problem.builder()
        .withType(URI.create("http://localhost:8080/api-problems"))
        .withTitle("Insufficient Balance")
        .withDetail("Insufficient balance for debit operation.")
        .withStatus(org.zalando.problem.Status.BAD_REQUEST)
        .build()
}

fun transactionAmountLessThanZeroProblem(): Throwable {
    return Problem.builder()
        .withType(URI.create("http://localhost:8080/api-problems"))
        .withTitle("Invalid Transaction Amount")
        .withDetail("Transaction amount must be greater then zero.")
        .withStatus(org.zalando.problem.Status.BAD_REQUEST)
        .build()
}
