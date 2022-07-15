package com.test.wallet.services

import com.test.wallet.dto.Status
import com.test.wallet.dto.TransactionHistoryDto
import com.test.wallet.exceptions.insufficientBalanceProblem
import com.test.wallet.exceptions.playerNotFoundProblem
import com.test.wallet.exceptions.transactionAmountLessThanZeroProblem
import com.test.wallet.exceptions.transactionIdNotUniqueProblem
import com.test.wallet.repositories.PlayerRepository
import com.test.wallet.repositories.TransactionRepository
import com.test.wallet.repositories.model.TransactionHistory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.transaction.Transactional

interface AccountService {
    fun accountDebit(playerId: Long, transactionDto: TransactionHistoryDto): Status
    fun accountCredit(playerId: Long, transactionDto: TransactionHistoryDto): Status
    fun accountBalance(playerId: Long): Status
}

@Transactional
open class AccountServiceImpl(private val playerRepository: PlayerRepository, private val transactionRepository: TransactionRepository) : AccountService {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountServiceImpl::class.java)
    }
    override fun accountDebit(playerId: Long, transactionDto: TransactionHistoryDto): Status {
        if (transactionDto.amount <= 0) throw transactionAmountLessThanZeroProblem()
        var newBalance = 0.0
        if (checkTransactionUniqueness(transactionDto.transactionId)) {
            playerRepository.findById(playerId).ifPresentOrElse({ player ->
                debitBalance(player.account!!.balance, transactionDto.amount)
                player.account!!.addTransaction(transactionHistoryModelMapper(transactionDto, -transactionDto.amount))
                newBalance = player.account!!.balance
            }, {
                throw playerNotFoundProblem(playerId)
            })
        } else {
            throw transactionIdNotUniqueProblem(transactionDto.transactionId)
        }
        return Status("Account debited successfully", newBalance)
    }

    override fun accountCredit(playerId: Long, transactionDto: TransactionHistoryDto): Status {
        if (transactionDto.amount <= 0) throw transactionAmountLessThanZeroProblem()
        var newBalance = 0.0
        if (checkTransactionUniqueness(transactionDto.transactionId)) {
            playerRepository.findById(playerId).ifPresentOrElse({ player ->
                logger.info("Player previous balance: $player")
                player.account!!.addTransaction(transactionHistoryModelMapper(transactionDto, transactionDto.amount))
                newBalance = player.account!!.balance
                logger.info("Player updated balance: $player")
            }, {
                throw playerNotFoundProblem(playerId)
            })
        } else {
            throw transactionIdNotUniqueProblem(transactionDto.transactionId)
        }
        return Status("Account credited successfully", newBalance)
    }

    override fun accountBalance(playerId: Long): Status {
        var balance = 0.0
        playerRepository.findById(playerId)
            .ifPresentOrElse(
                { player ->
                    balance = player.account?.balance ?: 0.0
                },
                {
                    throw playerNotFoundProblem(playerId)
                }
            )
        return Status("Account Balance", balance)
    }

    private fun checkTransactionUniqueness(transactionId: String) = transactionRepository.findById(transactionId).isEmpty

    private fun debitBalance(balance: Double, amount: Double): Boolean = if (balance - amount >= 0) true else throw insufficientBalanceProblem()

    private fun transactionHistoryModelMapper(transactionHistoryDto: TransactionHistoryDto, amount: Double) = TransactionHistory(transactionHistoryDto.transactionId, amount, transactionHistoryDto.updateDtime)
}
