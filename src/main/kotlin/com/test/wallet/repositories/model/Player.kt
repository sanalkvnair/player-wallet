package com.test.wallet.repositories.model

import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.SequenceGenerator

@Entity(name = "player")
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_seq")
    @SequenceGenerator(name = "player_seq", sequenceName = "player_seq", allocationSize = 1)
    @Column(name = "PLAYERID")
    val playerId: Long? = null,
    @Column(name = "PLAYERNAME") val playerName: String,
    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "player", orphanRemoval = true)
    var account: Account? = null
) {
    fun addAccount(account: Account) {
        this.account = account
        account.player = this
    }

    override fun toString(): String {
        return "Player(playerId=$playerId, playerName='$playerName', account=$account)"
    }
}

@Entity(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", allocationSize = 1)
    @Column(name = "ACCOUNTNUMBER")
    val accountNumber: Long? = null,
    @Column(name = "BALANCE") var balance: Double = 0.0,
    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "PLAYERID", referencedColumnName = "PLAYERID")
    var player: Player? = null,
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, mappedBy = "account", orphanRemoval = true)
    val transactions: MutableList<TransactionHistory> = mutableListOf()
) {
    fun addTransaction(transactionHistory: TransactionHistory) {
        this.balance += transactionHistory.amount
        transactions += transactionHistory
        transactionHistory.account = this
    }

    override fun toString(): String {
        return "Account(accountNumber=$accountNumber, balance=$balance, transactionHistories=$transactions)"
    }
}

@Entity(name = "transaction_history")
data class TransactionHistory(
    @Id
    @Column(name = "TRANSACTIONID")
    val transactionId: String,
    @Column(name = "AMOUNT") val amount: Double,
    @Column(name = "UPDATEDTIME") val updateDtime: LocalDateTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNTNUMBER", referencedColumnName = "ACCOUNTNUMBER")
    var account: Account? = null
) {
    override fun toString(): String {
        return "TransactionHistory(transactionId='$transactionId', amount=$amount, updateDtime=$updateDtime)"
    }
}
