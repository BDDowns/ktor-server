package user

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable : IntIdTable() {
    val name = varchar("name", 20).uniqueIndex()
    val email = varchar("email", 40)
}