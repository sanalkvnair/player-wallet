package com.test.wallet.configurations

import com.test.wallet.repositories.PlayerRepository
import com.test.wallet.repositories.TransactionRepository
import com.test.wallet.services.AccountServiceImpl
import com.test.wallet.services.PlayerService
import com.test.wallet.services.PlayerServiceImpl
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfiguration {

    @Bean
    fun getPlayerService(playerRepository: PlayerRepository): PlayerService = PlayerServiceImpl(playerRepository)

    @Bean
    fun getAccountService(playerRepository: PlayerRepository, transactionRepository: TransactionRepository) = AccountServiceImpl(playerRepository, transactionRepository)

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Wallet Service")
                    .description("API for handling players debit and credit transactions and viewing account balance.")
                    .version("v1.0")
                    .contact(
                        Contact()
                            .name("Sanal Nair")
                            .url("https://github.com/sanalkvnair")
                            .email("test@email.com")
                    )
                    .termsOfService("TOC")
                    .license(License().name("License").url("#"))
            )
    }
}
