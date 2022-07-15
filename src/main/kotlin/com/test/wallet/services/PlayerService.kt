package com.test.wallet.services

import com.test.wallet.dto.AccountDto
import com.test.wallet.dto.PlayerDto
import com.test.wallet.dto.PlayersDto
import com.test.wallet.dto.TransactionHistoryDto
import com.test.wallet.exceptions.playerNotFoundProblem
import com.test.wallet.repositories.PlayerRepository
import com.test.wallet.repositories.model.Account
import com.test.wallet.repositories.model.Player
import com.test.wallet.repositories.model.TransactionHistory
import javax.transaction.Transactional

interface PlayerService {
    fun getPlayer(): PlayersDto
    fun getPlayer(playerId: Long): PlayerDto
    fun createPlayer(playerDto: PlayerDto): PlayerDto
}

@Transactional
open class PlayerServiceImpl(private val playerRepository: PlayerRepository) : PlayerService {
    override fun getPlayer(): PlayersDto {
        val playersDto = PlayersDto()
        playerRepository.findAll().map { playerDtoMapper(it) }.map { playersDto.players.add(it) }
        return playersDto
    }

    override fun getPlayer(playerId: Long): PlayerDto {
        playerRepository.findById(playerId)
        return playerDtoMapper(playerRepository.findById(playerId).orElseThrow { throw playerNotFoundProblem(playerId) })
    }

    override fun createPlayer(playerDto: PlayerDto): PlayerDto {
        return playerDtoMapper(playerRepository.save(playerModelMapper(playerDto)))
    }

    private fun playerDtoMapper(player: Player): PlayerDto {
        val accountDto = AccountDto(player.account?.accountNumber, balance = player.account?.balance ?: 0.0)
        player.account?.transactions?.map {
            val transactionHistory = TransactionHistoryDto(it.transactionId, it.amount, it.updateDtime)
            accountDto.transactions.add(transactionHistory)
        }
        return PlayerDto(playerId = player.playerId, playerName = player.playerName, account = accountDto)
    }

    private fun playerModelMapper(playerDto: PlayerDto): Player {
        val player = Player(playerName = playerDto.playerName)
        val account = Account(balance = playerDto.account.balance)
        playerDto.account.transactions.map {
            val transactionHistory = TransactionHistory(it.transactionId, amount = it.amount, updateDtime = it.updateDtime)
            account.addTransaction(transactionHistory)
        }
        player.addAccount(account)
        return player
    }
}
