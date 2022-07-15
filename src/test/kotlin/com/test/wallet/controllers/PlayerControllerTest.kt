package com.test.wallet.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.test.wallet.dto.PlayerDto
import com.test.wallet.dto.PlayersDto
import com.test.wallet.dto.Status
import com.test.wallet.dto.TransactionHistoryDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.zalando.problem.Problem
import java.io.InputStream
import java.net.URL

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlayerControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var testObjectMapper: ObjectMapper

    @Test
    fun `get all players`() {
        val response = restTemplate.getForEntity(
            URL("http://localhost:$port/api/player").toString(),
            PlayersDto::class.java
        )
        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)
        assertTrue(response.body!!.players.isNotEmpty())
        assertEquals(3, response.body!!.players.size)
    }

    @Test
    fun `get a specific player`() {
        val response = restTemplate.getForEntity(
            URL("http://localhost:$port/api/player/101").toString(),
            PlayerDto::class.java
        )
        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)
    }

    @Test
    fun `get a player balance`() {
        val response = restTemplate.getForEntity(
            URL("http://localhost:$port/api/player/103").toString(),
            PlayerDto::class.java
        )
        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals(200.0, response.body!!.account.balance)
    }

    @Test
    fun `get a player transaction history`() {
        val response = restTemplate.getForEntity(
            URL("http://localhost:$port/api/player/103").toString(),
            PlayerDto::class.java
        )
        assertEquals(200, response.statusCodeValue)
        assertEquals(200, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals(200.0, response.body!!.account.balance)
        assertEquals(2, response.body!!.account.transactions.size)
        response.body!!.account.transactions.forEach { transactionHistory ->
            assert(transactionHistory.amount > 0)
        }
    }

    @Test
    fun `verify player can be created`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestJson = testObjectMapper.readValue(getInputStream("/requests/create-player.json"), PlayerDto::class.java)
        val httpEntity = HttpEntity(requestJson, headers)
        val response = restTemplate.postForEntity(
            URL("http://localhost:$port/api/player").toString(),
            httpEntity,
            PlayerDto::class.java
        )
        assertEquals(201, response.statusCodeValue)
        assertNotNull(response.body)
        assertNotNull(response.body!!.playerId)
    }

    @Test
    fun `verify account can be debited`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestJson = testObjectMapper.readValue(getInputStream("/requests/debit-account.json"), TransactionHistoryDto::class.java)
        val httpEntity = HttpEntity(requestJson, headers)
        val response = restTemplate.exchange(
            URL("http://localhost:$port/api/player/101/account/debit").toString(),
            HttpMethod.PUT,
            httpEntity,
            Status::class.java
        )
        assertEquals(202, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals(0.0, response.body!!.balance)
    }

    @Test
    fun `verify account debit fails`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestJson = testObjectMapper.readValue(getInputStream("/requests/debit-account.json"), TransactionHistoryDto::class.java)
        val httpEntity = HttpEntity(requestJson, headers)
        val response = restTemplate.exchange(
            URL("http://localhost:$port/api/player/102/account/debit").toString(),
            HttpMethod.PUT,
            httpEntity,
            Problem::class.java
        )
        assertEquals(400, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals("Insufficient Balance", response.body!!.title)
        assertEquals("Insufficient balance for debit operation.", response.body!!.detail)
    }

    @Test
    fun `verify account can be credited`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestJson = testObjectMapper.readValue(getInputStream("/requests/credit-account.json"), TransactionHistoryDto::class.java)
        val httpEntity = HttpEntity(requestJson, headers)
        val response = restTemplate.exchange(
            URL("http://localhost:$port/api/player/103/account/credit").toString(),
            HttpMethod.PUT,
            httpEntity,
            Status::class.java
        )
        assertEquals(202, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals(300.0, response.body!!.balance)
    }

    @Test
    fun `verify transaction fails with non unique transaction id`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestJson = testObjectMapper.readValue(getInputStream("/requests/non-unique-transaction-account.json"), TransactionHistoryDto::class.java)
        val httpEntity = HttpEntity(requestJson, headers)
        val response = restTemplate.exchange(
            URL("http://localhost:$port/api/player/103/account/credit").toString(),
            HttpMethod.PUT,
            httpEntity,
            Problem::class.java
        )
        assertEquals(400, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals("Transaction ID Error", response.body!!.title)
        assertEquals("Transaction ID 12348 not unique.", response.body!!.detail)
    }

    @Test
    fun `verify transaction fails due to non positive transaction amount`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val requestJson = testObjectMapper.readValue(getInputStream("/requests/negative-amount-transaction-account.json"), TransactionHistoryDto::class.java)
        val httpEntity = HttpEntity(requestJson, headers)
        val response = restTemplate.exchange(
            URL("http://localhost:$port/api/player/102/account/debit").toString(),
            HttpMethod.PUT,
            httpEntity,
            Problem::class.java
        )
        assertEquals(400, response.statusCodeValue)
        assertNotNull(response.body)
        assertEquals("Invalid Transaction Amount", response.body!!.title)
        assertEquals("Transaction amount must be greater then zero.", response.body!!.detail)
    }

    protected fun getInputStream(path: String): InputStream? {
        return this.javaClass.getResourceAsStream(path)
    }
}
