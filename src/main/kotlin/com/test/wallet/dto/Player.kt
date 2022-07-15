package com.test.wallet.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Schema(name = "Players")
data class PlayersDto(
    val players: MutableList<PlayerDto> = mutableListOf()
)

@Schema(name = "Player")
data class PlayerDto(
    val playerId: Long? = null,
    @NotBlank(message = "Player name cannot be empty") val playerName: String,
    val account: AccountDto = AccountDto()
)

@Schema(name = "Account")
data class AccountDto(
    val accountNumber: Long? = null,
    val balance: Double = 0.0,
    val transactions: MutableList<TransactionHistoryDto> = mutableListOf()
)

@Schema(name = "TransactionHistory")
data class TransactionHistoryDto(
    @NotBlank(message = "Unique Transaction ID is required.") val transactionId: String,
    @NotBlank(message = "Transaction amount is required.") @Positive(message = "Transaction amount must be a positive number.")
    val amount: Double = 0.0,
    val updateDtime: LocalDateTime = LocalDateTime.now()
)

data class Status(
    val message: String,
    val balance: Double
)
