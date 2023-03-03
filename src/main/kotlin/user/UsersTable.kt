package user

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import user.Constant.MAX_EMAIL_LENGTH
import user.Constant.MAX_NAME_LENGTH

object UsersTable : IntIdTable() {
    val name = varchar("name", MAX_NAME_LENGTH).uniqueIndex()
    val email = varchar("email", MAX_EMAIL_LENGTH)

    fun init() {
        DB.connect()
        transaction {
            SchemaUtils.create(UsersTable)
        }
    }
}