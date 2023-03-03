package user

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Routing.users() {
    route("/users") {
        get("/{id}") {
            val id = requireNotNull(call.parameters["id"]).toInt()
            val resultRow = transaction {
                UsersTable.select {
                    UsersTable.id.eq(id)
                }.firstOrNull()
            }
            if (resultRow == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond( toUser(resultRow) )
            }
        }
        get {
            val users = transaction {
                UsersTable
                    .selectAll()
                    .map { toUser(it) }.toList()
            }
            call.respond(users)
        }

        post {
            val parameters = call.receiveParameters()
            println(parameters.toString())
            val name = requireNotNull(parameters["name"])
            val email = requireNotNull(parameters["email"])
            User(0, name, email).validate()
            transaction {
                UsersTable.insert { user ->
                    user[UsersTable.name] = name
                    user[UsersTable.email] = email
                }
            }
            call.respond(HttpStatusCode.Created)
        }
    }
}

private fun toUser(row: ResultRow) = with(row) {
    User(
        this[UsersTable.id].value,
        this[UsersTable.name],
        this[UsersTable.email],
    )
}