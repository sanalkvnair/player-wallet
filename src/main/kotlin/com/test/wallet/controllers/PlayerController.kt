package com.test.wallet.controllers

import com.test.wallet.dto.PlayerDto
import com.test.wallet.dto.PlayersDto
import com.test.wallet.dto.Status
import com.test.wallet.dto.TransactionHistoryDto
import com.test.wallet.services.AccountService
import com.test.wallet.services.PlayerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import javax.validation.Valid

@Tag(description = "Player resource that provides creating of new player, and there account transaction.", name = "Player Resource")
@RestController
@RequestMapping("/api")
class PlayerController(private val playerService: PlayerService, private val accountService: AccountService) {

    @Operation(summary = "Get players", description = "Get list of all players")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully fetched list of players")
        ]
    )
    @GetMapping("/player")
    fun getPlayer(): ResponseEntity<PlayersDto> {
        return ResponseEntity.ok().body(playerService.getPlayer())
    }

    @Operation(summary = "Get player", description = "Get the specified player's account and transaction details")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successfully fetched player data."),
            ApiResponse(responseCode = "400", description = "Player with the specified playerId not found")
        ]
    )
    @GetMapping("/player/{playerId}")
    fun getPlayer(@PathVariable("playerId") playerId: Long): ResponseEntity<PlayerDto> {
        return ResponseEntity.ok().body(playerService.getPlayer(playerId))
    }

    @Operation(summary = "Create player", description = "Create player with zero balance account.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Successfully created player account."),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @PostMapping("/player")
    fun createPlayer(
        @Valid @RequestBody
        playerDto: PlayerDto
    ): ResponseEntity<PlayerDto> {
        val player = playerService.createPlayer(playerDto)
        return ResponseEntity.created(URI.create("/api/player/${player.playerId}")).body(player)
    }

    @Operation(summary = "Debit player account", description = "Debit player account with the amount specified.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Success accepted request"),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @PutMapping("/player/{playerId}/account/debit")
    fun playerAccountDebit(
        @PathVariable playerId: Long,
        @Valid @RequestBody
        transactionDto: TransactionHistoryDto
    ): ResponseEntity<Status> {
        return ResponseEntity.accepted().body(accountService.accountDebit(playerId, transactionDto))
    }

    @Operation(summary = "Credit player account", description = "Credit player account with the amount specified.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Success accepted request"),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @PutMapping("/player/{playerId}/account/credit")
    fun playerAccountCredit(
        @PathVariable playerId: Long,
        @Valid @RequestBody
        transactionDto: TransactionHistoryDto
    ): ResponseEntity<Status> {
        return ResponseEntity.accepted().body(accountService.accountCredit(playerId, transactionDto))
    }

    @Operation(summary = "Player account balance", description = "Get balance from player account.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @GetMapping("/player/{playerId}/account/balance")
    fun playerAccountBalance(@PathVariable playerId: Long): ResponseEntity<Status> {
        return ResponseEntity.ok().body(accountService.accountBalance(playerId))
    }
}
