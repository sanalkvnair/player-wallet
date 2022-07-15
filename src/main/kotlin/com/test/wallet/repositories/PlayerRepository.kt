package com.test.wallet.repositories

import com.test.wallet.repositories.model.Player
import com.test.wallet.repositories.model.TransactionHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : JpaRepository<Player, Long>

@Repository
interface TransactionRepository : JpaRepository<TransactionHistory, String>
