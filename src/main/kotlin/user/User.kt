package user

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
) {
    companion object {
        fun fromResultRow(resultRow: ResultRow) : User {
            return with(resultRow) {
                User(
                    this[UsersTable.id].value,
                    this[UsersTable.name],
                    this[UsersTable.email],
                )
            }
        }
    }
}
