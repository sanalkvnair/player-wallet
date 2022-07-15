# Getting Started

The is a player wallet microservice written in Kotlin for handling player's account transaction management, which exposes REST API for below functionalities

* Create a new player.
* Get player and there specific transaction history.
* Show player account balance.
* Player account debit operation.
* Player account credit operation.

## The application can be tested via swagger ui or via the postman collection attached.

The application is running on default port 8080 can be changed via `server.port` property.

The application contains H2 database file for testing located in data directory.

