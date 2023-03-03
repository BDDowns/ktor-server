# ktor-server

Simple Ktor CRUD app based on Chapter 10 of [Kotlin Design Patterns and Best Practices](https://github.com/PacktPublishing/Kotlin-Design-Patterns-and-Best-Practices)

Differences betweent the projects:
* Users instead of Cats
* Basic input validation beyond null checks
* Factory to make user creation from db results cleaner
* Init function added to table to decouple from main
* Split tests by domain (server vs user)
