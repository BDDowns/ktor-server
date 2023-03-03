package user

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initUserDB() {
    DB.connect()
    transaction {
        SchemaUtils.create(UsersTable)
    }
}